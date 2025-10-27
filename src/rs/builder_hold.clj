(ns rs.builder
  (:require [clojure.string :as str]
            [clj-time.local :as l]))

;;; Utility functions

(defn get-date []
  (str (l/local-now)))

;;; Alias generation

(defn generate-table-alias [table-name]
  (let [words (str/split table-name #"_")]
    (if (= (count words) 1)
      ;; Single word: take first 3 characters
      (subs table-name 0 (min 3 (count table-name)))
      ;; Multiple words: take first letter of each word
      (str/join "" (map #(subs % 0 1) words)))))

;;; Database utilities

(defn get-table-fields
  "Get table field information using PRAGMA table_info"
  [db table-name]
  (let [query-fn (resolve 'rs.models.crud/Query)]
    (when query-fn
      (query-fn db [(str "PRAGMA table_info(" table-name ")")]))))

(defn get-foreign-keys
  "Get foreign key information using PRAGMA foreign_key_list"
  [db table-name]
  (let [query-fn (resolve 'rs.models.crud/Query)]
    (when query-fn
      (query-fn db [(str "PRAGMA foreign_key_list(" table-name ")")]))))

(defn find-display-field
  "Find a suitable display field in the referenced table (name, title, descripcion, etc.)"
  [db table-name]
  (let [fields (get-table-fields db table-name)
        field-names (map :name fields)
        ;; Look for common display field patterns
        display-candidates ["nombre" "name" "title" "titulo" "descripcion" "description" "label"]]
    (or
     ;; First, look for exact matches
     (some #(when (some #{%} field-names) %) display-candidates)
     ;; If no exact match, look for fields containing these words
     (some #(when (some (fn [field] (re-find (re-pattern (str "(?i)" %)) field)) field-names)
              (first (filter (fn [field] (re-find (re-pattern (str "(?i)" %)) field)) field-names)))
           display-candidates)
     ;; Default to first non-id field
     (first (filter #(not= % "id") field-names))
     ;; Ultimate fallback to id
     "id")))

(defn get-table-comments
  "Extract field comments from CREATE TABLE SQL - for select option parsing"
  [db table-name]
  (let [query-fn (resolve 'rs.models.crud/Query)
        sql-result (when query-fn
                     (query-fn db [(str "SELECT sql FROM sqlite_master WHERE name = ? AND type = 'table'") table-name]))
        create-sql (:sql (first sql-result))]
    (when create-sql
      ;; Parse field comments from SQL like: field_name TYPE, -- option1, option2, option3
      (let [lines (clojure.string/split-lines create-sql)
            comment-map (atom {})]
        (doseq [line lines]
          (let [trimmed (clojure.string/trim line)]
            ;; Look for pattern: field_name TYPE DEFAULT 'value', -- comment with options
            (when-let [match (re-find #"^\s*(\w+)\s+\w+.*?--\s*(.+)" trimmed)]
              (let [field-name (second match)
                    comment (clojure.string/trim (last match))]
                (swap! comment-map assoc field-name comment)))))
        @comment-map)))) (defn build-foreign-key-joins
                           "Build JOIN clauses and enhanced SELECT for foreign keys"
                           [db table-name fields foreign-keys]
                           (let [main-alias (generate-table-alias table-name)
                                 joins (atom [])
                                 selects (atom [])]

                             ;; Add main table fields to select
                             (doseq [field fields]
                               (swap! selects conj (str main-alias "." (:name field))))

                             ;; Process foreign keys
                             (doseq [fk foreign-keys]
                               (let [fk-column (:from fk)
                                     ref-table (:table fk)
                                     ref-column (:to fk)
                                     ref-alias (generate-table-alias ref-table)
                                     display-field (find-display-field db ref-table)]

                                 ;; Add JOIN
                                 (swap! joins conj
                                        (str "LEFT JOIN " ref-table " " ref-alias
                                             " ON " main-alias "." fk-column " = " ref-alias "." ref-column))

                                 ;; Add display field to SELECT with alias
                                 (swap! selects conj
                                        (str ref-alias "." display-field " AS " fk-column "_display"))))

                             {:select (clojure.string/join ", " @selects)
                              :joins (clojure.string/join " " @joins)
                              :foreign-key-displays (into {}
                                                          (map (fn [fk]
                                                                 [(:from fk) (str (:from fk) "_display")])
                                                               foreign-keys))}))

;;; SQL query generation

(defn generate-smart-query
  ([table-name _fields]
   ;; This version requires database connection - no fallback
   (throw (ex-info "Database connection required for smart query generation"
                   {:table table-name})))
  ([table-name fields db-conn]
   (when-not db-conn
     (throw (ex-info "Database connection is required for smart query generation"
                     {:table table-name})))
   (let [main-alias (generate-table-alias table-name)
         ;; Get actual foreign keys from database using PRAGMA
         foreign-keys (get-foreign-keys db-conn table-name)
         ;; Build enhanced query with JOINs for foreign keys
         query-parts (if (seq foreign-keys)
                       (build-foreign-key-joins db-conn table-name fields foreign-keys)
                       ;; No foreign keys - simple query
                       {:select (str/join ", " (map #(str main-alias "." (:name %)) fields))
                        :joins ""
                        :foreign-key-displays {}})]
     {:select (:select query-parts)
      :from (str table-name " " main-alias)
      :joins (:joins query-parts)
      :foreign-key-displays (:foreign-key-displays query-parts)
      :full-query (str "SELECT " (:select query-parts)
                       " FROM " table-name " " main-alias
                       (when (seq (:joins query-parts))
                         (str " " (:joins query-parts))))})));;; Template generators

(defn build-ns-form
  "Build the namespace form for a model"
  [table-name]
  (let [ns-name (str "rs.models." (str/replace table-name #"_" "-"))]
    (str "(ns " ns-name "\n"
         "  (:require [rs.models.crud :refer [Query]]\n"
         "            [clojure.string :as str]))")))

(defn build-field-config
  "Build field configuration using database schema and smart field detection"
  [db table-name fields]
  (let [;; Import smart field configuration from util.clj
        smart-config-fn (resolve 'rs.models.util/smart-build-field-config)
        field-configs (if smart-config-fn
                        ;; Use smart field configuration from util.clj
                        (map (fn [field]
                               (let [field-name (:name field)
                                     db-type (:type field)
                                     comment (:comment field)  ;; Get comment from PRAGMA table_info
                                     required (= (:notnull field) 1)]
                                 (smart-config-fn {:field-name field-name
                                                   :db-type db-type
                                                   :comment comment
                                                   :table-name table-name
                                                   :required required})))
                             fields)
                        ;; Fallback if smart-build-field-config not available
                        (map (fn [field]
                               (let [field-name (:name field)
                                     field-type (:type field)]
                                 {:name field-name
                                  :type (case field-type
                                          "INTEGER" "number"
                                          "REAL" "number"
                                          "DATE" "date"
                                          "TEXT" "text"
                                          "text")
                                  :label (str/capitalize (str/replace field-name #"_" " "))}))
                             fields))]
    (str "[" (str/join "\n   " (map pr-str field-configs)) "]")))

(defn build-get-sql [table-name fields & [db-conn]]
  (let [query-info (generate-smart-query table-name fields db-conn)
        function-name (str "get-" (str/replace table-name #"_" "-") "-sql")]
    (str "(defn " function-name " []\n"
         "  \"" (:full-query query-info) "\")")))

(defn build-get-id-sql [table-name fields & [db-conn]]
  (let [query-info (generate-smart-query table-name fields db-conn)
        main-alias (generate-table-alias table-name)
        function-name (str "get-" (str/replace table-name #"_" "-") "-id-sql")]
    (str "(defn " function-name " [id]\n"
         "  (str \"" (:select query-info) "\"\n"
         "       \" FROM " (:from query-info) "\"\n"
         (when (seq (:joins query-info))
           (str "       \" " (:joins query-info) "\"\n"))
         "       \" WHERE " main-alias ".id = \" id))"))) ; WARNING: Assumes 'id' field exists

(defn build-crud-functions [table-name fields]
  (let [model-name (str/replace table-name #"_" "-")
        field-names (map :name fields)
        non-id-fields (remove #(= % "id") field-names)] ; WARNING: Assumes 'id' field exists
    (str
     ;; Get all records
     "(defn get-" model-name "s []\n"
     "  (Query [(" (str "get-" model-name "-sql") ")] :conn :default))\n\n"

     ;; Get by ID  
     "(defn get-" model-name "-by-id [id]\n"
     "  (first (Query [(" (str "get-" model-name "-id-sql") " id)] :conn :default)))\n\n"

     ;; Create
     "(defn create-" model-name "! [data]\n"
     "  (execute-one! db\n"
     "    [(str \"INSERT INTO " table-name " (\" \n"
     "          (str/join \", \" [" (str/join " " (map #(str "\"" % "\"") non-id-fields)) "])\n"
     "          \") VALUES (\" \n"
     "          (str/join \", \" (repeat " (count non-id-fields) " \"?\"))\n"
     "          \")\") \n"
     "     " (str/join " " (map #(str "(" % " data)") non-id-fields)) "]))\n\n"

     ;; Update
     "(defn update-" model-name "! [id data]\n"
     "  (execute-one! db\n"
     "    [(str \"UPDATE " table-name " SET \"\n"
     "          (str/join \", \" \n"
     "            [" (str/join "\n             " (map #(str "\"" % " = ?\"") non-id-fields)) "])\n"
     "          \" WHERE id = ?\") \n"
     "     " (str/join " " (map #(str "(" % " data)") non-id-fields)) " id]))\n\n"

     ;; Delete  
     "(defn delete-" model-name "! [id]\n"
     "  (execute-one! db [\"DELETE FROM " table-name " WHERE id = ?\" id]))"))) ; WARNING: All CRUD functions assume 'id' primary key))

;;; Model generation

(defn build-model [table-name fields & [db]]
  (str/join "\n\n"
            [(build-ns-form table-name)
             (str ";; Generated on " (get-date))
             ""
             ";; Field configuration"
             (str "(def field-config\n  " (if db
                                            (build-field-config db table-name fields)
                                            (build-field-config nil table-name fields)) ")")
             ""
             ";; SQL queries"
             (build-get-sql table-name fields db)
             (build-get-id-sql table-name fields db)
             ""
             ";; CRUD functions"
             (build-crud-functions table-name fields)]))

;;; Handler generation

(defn build-handler-ns [table-name]
  (let [handler-ns (str "rs.handlers." (str/replace table-name #"_" "-"))
        model-ns (str "rs.models." (str/replace table-name #"_" "-"))]
    (str "(ns " handler-ns "\n"
         "  (:require [ring.util.response :refer [response redirect]]\n"
         "            [rs.layout :refer [application]]\n"
         "            [" model-ns " :as model]))")))

(defn build-handler-functions [table-name fields]
  (let [model-name (str/replace table-name #"_" "-")
        display-name (str/capitalize (str/replace table-name #"_" " "))]
    (str
     ";; Index handler\n"
     "(defn index [request]\n"
     "  (let [records (model/get-" model-name "s)]\n"
     "    (application\n"
     "      [:div.container\n"
     "       [:h1 \"" display-name "\"]\n"
     "       [:div.row\n"
     "        [:div.col\n"
     "         [:table.table.table-striped\n"
     "          [:thead\n"
     "           [:tr\n"
     (str/join "\n"
               (map #(str "            [:th \"" (str/capitalize (str/replace (:name %) #"_" " ")) "\"]")
                    fields)) "\n"
     "            [:th \"Actions\"]]]\n"
     "          [:tbody\n"
     "           (for [record records]\n"
     "             [:tr\n"
     (str/join "\n"
               (map #(str "              [:td (:" (:name %) " record)]")
                    fields)) "\n"
     "              [:td\n"
     "               [:a.btn.btn-sm.btn-primary\n"
     "                {:href (str \"/" model-name "/\" (:id record))}\n"
     "                \"View\"]\n"
     "               [:a.btn.btn-sm.btn-warning.ms-1\n"
     "                {:href (str \"/" model-name "/\" (:id record) \"/edit\")}\n"
     "                \"Edit\"]]])]]]\n"
     "        [:div.col-auto\n"
     "         [:a.btn.btn-success {:href \"/" model-name "/new\"}\n"
     "          \"New " display-name "\"]]]]))\n\n"

     ";; Show handler\n"
     "(defn show [request]\n"
     "  (let [id (-> request :params :id Integer/parseInt)\n"
     "        record (model/get-" model-name "-by-id id)]\n"
     "    (if record\n"
     "      (application\n"
     "        [:div.container\n"
     "         [:h1 \"" display-name " #\" (:id record)]\n"
     "         [:div.card\n"
     "          [:div.card-body\n"
     (str/join "\n"
               (map #(str "           [:p [:strong \"" (str/capitalize (str/replace (:name %) #"_" " ")) ":\"] (:" (:name %) " record)]")
                    fields)) "]]\n"
     "         [:div.mt-3\n"
     "          [:a.btn.btn-warning {:href (str \"/" model-name "/\" id \"/edit\")}\n"
     "           \"Edit\"]\n"
     "          [:a.btn.btn-secondary.ms-2 {:href \"/" model-name "\"}\n"
     "           \"Back\"]]])\n"
     "      (redirect \"/" model-name "\"))))")))

(defn build-handler [table-name fields]
  (str/join "\n\n"
            [(build-handler-ns table-name)
             (str ";; Generated on " (get-date))
             ""
             (build-handler-functions table-name fields)]))

;;; Subgrid generation

(defn build-subgrid-model [parent-table child-table fields & [db-conn]]
  (let [parent-name (str/replace parent-table #"_" "-")
        child-name (str/replace child-table #"_" "-")
        parent-fk (str parent-name "_id")  ; Use parent-name instead of parent-table
        query-info (generate-smart-query child-table fields db-conn)
        main-alias (generate-table-alias child-table)]
    (str
     (build-ns-form (str parent-name child-name))  ; Remove dash for namespace
     "\n\n;; Generated on " (get-date) "\n\n"

     ";; Get records for parent\n"
     "(defn get-" child-name "-for-" parent-name " [" parent-name "-id]\n"  ; Remove extra 's'
     "  (Query\n"
     "    [(str \"SELECT " (:select query-info) "\"\n"  ; Add SELECT keyword
     "          \" FROM " (:from query-info) "\"\n"
     (when (seq (:joins query-info))
       (str "          \" " (:joins query-info) "\"\n"))
     "          \" WHERE " main-alias "." parent-fk " = ?\") " parent-name "-id] :conn :default))\n\n" ; WARNING: Hardcoded :default connection

     ";; Field configuration\n"
     "(def field-config\n  " (build-field-config db-conn child-table fields) ")")))

;;; Main generation functions

(defn generate-model-file [table-name fields file-path]
  (let [content (build-model table-name fields)]
    (spit file-path content)
    (println (str "Generated model: " file-path))))

(defn generate-handler-file [table-name fields file-path]
  (let [content (build-handler table-name fields)]
    (spit file-path content)
    (println (str "Generated handler: " file-path))))

(defn generate-subgrid-file [parent-table child-table fields file-path & [db-conn]]
  (let [content (build-subgrid-model parent-table child-table fields db-conn)]
    (spit file-path content)
    (println (str "Generated subgrid: " file-path))))

;; Duplicate database functions removed - definitions are at the top;;; Example usage functions

(defn generate-full-scaffold [table-name]
  ; WARNING: Uses hardcoded file paths and no database connection
  (let [fields (get-table-fields nil table-name) ; WARNING: No database connection
        model-path (str "src/rs/models/" (str/replace table-name #"_" "_") ".clj") ; WARNING: Hardcoded path
        handler-path (str "src/rs/handlers/" (str/replace table-name #"_" "_") ".clj")] ; WARNING: Hardcoded path
    (generate-model-file table-name fields model-path)
    (generate-handler-file table-name fields handler-path)))

;;; Database-aware generation functions

(defn generate-model-from-db [db table-name]
  (let [fields (get-table-fields db table-name)]
    (build-model table-name fields)))

(defn generate-subgrid-from-db [db parent-table child-table]
  (let [fields (get-table-fields db child-table)]
    (build-subgrid-model parent-table child-table fields db)))

(defn generate-smart-query-from-db [db table-name]
  (let [fields (get-table-fields db table-name)]
    (generate-smart-query table-name fields db)))

;;; Subgrid component generators

(defn build-subgrid-controller [child-table parent-table parent-fk-column & [rights]]
  (let [effective-rights (or rights ["U" "A" "S"])]
    (str
     "(ns rs.handlers.admin." child-table parent-table ".controller\n"
     "  (:require\n"
     "   [rs.handlers.admin." child-table parent-table ".model :refer [get-" child-table " get-" child-table "-id]]\n"
     "   [rs.handlers.admin." child-table parent-table ".view :refer [" child-table "-view build-" child-table "-form]]\n"
     "   [rs.models.crud :refer [build-form-delete build-form-save crud-fix-id]]\n"
     "   [rs.models.util :refer [user-level]]\n"
     "   [hiccup.core :refer [html]]))\n\n"

     ";; Permissions - default to all rights if none specified\n"
     "(def allowed-rights " (pr-str effective-rights) ")\n\n"

     ";; Main subgrid endpoint - this is what the subgrid AJAX calls\n"
     "(defn " child-table parent-table "-grid\n"
     "  [request]\n"
     "  (let [params (:params request)\n"
     "  parent-id (crud-fix-id (get params :parent_id))\n"
     "        title \" " (str/capitalize child-table) " \"\n"
     "        rows (get-" child-table " parent-id)\n"
     "        content (" child-table "-view title rows parent-id)\n"
     "        user-r (user-level request)]\n"
     "    (if (some #(= user-r %) allowed-rights)\n"
     "      {:status 200\n"
     "       :headers {\"Content-Type\" \"text/html\"}\n"
     "       :body (html content)}\n"
     "      {:status 403\n"
     "       :headers {\"Content-Type\" \"text/html\"}\n"
     "       :body \"Not authorized to access this item!\"})))\n\n"

     "(defn " child-table parent-table "-add-form\n"
     "  [request parent-id]\n"
     "  (let [title \"New " (str/capitalize child-table) "\"\n"
     "  row {:" parent-fk-column " (crud-fix-id parent-id)}\n"
     "        user-r (user-level request)]\n"
     "    (if (some #(= user-r %) allowed-rights)\n"
     "      (html (build-" child-table "-form title row))\n"
     "      {:status 403 :headers {\"Content-Type\" \"text/html\"} :body \"Not authorized to access this item!\"})))\n\n"

     "(defn " child-table parent-table "-edit-form\n"
     "  [request id]\n"
     "  (let [title \"Edit " (str/capitalize child-table) "\"\n"
     "  row (get-" child-table "-id (crud-fix-id id))\n"
     "        user-r (user-level request)]\n"
     "    (if (some #(= user-r %) allowed-rights)\n"
     "      (html (build-" child-table "-form title row))\n"
     "      {:status 403 :headers {\"Content-Type\" \"text/html\"} :body \"Not authorized to access this item!\"})))\n\n"

     "(defn " child-table parent-table "-save\n"
     "  [request]\n"
     "  (let [params (:params request)\n"
     "        table \"" child-table "\"\n"
     "        user-r (user-level request)]\n"
     "    (if (some #(= user-r %) allowed-rights)\n"
     "  (let [result (build-form-save params table :conn :default)]\n"
     "        (if result\n"
     "          {:status 200 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":true}\"}\n"
     "          {:status 500 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":false}\"}))\n"
     "      {:status 403 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":false,\\\"error\\\":\\\"Not authorized\\\"}\"})))\n\n"

     "(defn " child-table parent-table "-delete\n"
     "  [request id]\n"
     "  (let [table \"" child-table "\"\n"
     "        user-r (user-level request)]\n"
     "    (if (some #(= user-r %) allowed-rights)\n"
     "  (let [result (build-form-delete table (crud-fix-id id) :conn :default)]\n"
     "        (if result\n"
     "          {:status 200 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":true}\"}\n"
     "          {:status 500 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":false}\"}))\n"
     "      {:status 403 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":false,\\\"error\\\":\\\"Not authorized\\\"}\"})))\n")))

(defn build-subgrid-view [child-table parent-table parent-fk-column & [db-conn]]
  (let [;; Get actual fields and foreign keys for better view generation
        fields (when db-conn (get-table-fields db-conn child-table))
        foreign-keys (when db-conn (get-foreign-keys db-conn child-table))

        ;; Build field mapping for grid display (show display fields instead of IDs)
        grid-fields (when (and fields foreign-keys)
                      (let [fk-map (into {} (map (fn [fk] [(:from fk) (str (:from fk) "_display")]) foreign-keys))
                            regular-fields (remove #(contains? fk-map (:name %)) fields)]
                        (concat
                         ;; Regular fields
                         (map (fn [field]
                                {:key (:name field)
                                 :label (clojure.string/capitalize (clojure.string/replace (:name field) #"_" " "))})
                              regular-fields)
                         ;; Foreign key display fields
                         (map (fn [[fk-field display-field]]
                                {:key display-field
                                 :label (clojure.string/capitalize (clojure.string/replace fk-field #"_" " "))})
                              fk-map))))

        ;; Get actual field comments for select option parsing
        table-comments (when db-conn (get-table-comments db-conn child-table))

        ;; Generate form field strings from database schema at build time
        form-field-strings (when fields
                             (let [form-fields (remove #(or (= (:name %) "id")
                                                            (= (:name %) parent-fk-column))
                                                       fields)]
                               (map (fn [field]
                                      (let [field-name (:name field)
                                            db-type (:type field)
                                            ;; Use actual comment for select options, fallback to default value
                                            comment (or (get table-comments field-name)
                                                        (:dflt_value field))
                                            required (= (:notnull field) 1)]
                                        (str "   (build-field (smart-build-field-config {:field-name \"" field-name "\" :db-type \"" db-type "\""
                                             " :comment " (if comment (str "\"" comment "\"") "nil")
                                             "\n                                           :table-name \"" child-table "\" :required " required
                                             " :value (get row :" field-name ")}))")))
                                    form-fields)))]
    (str
     "(ns rs.handlers.admin." child-table parent-table ".view\n"
     "  (:require [rs.models.form :refer [form build-field build-modal-buttons]]\n"
     "            [rs.models.grid :refer [build-grid build-grid-with-custom-new]]\n"
     "            [rs.models.util :refer [smart-build-field-config]]))\n\n"

     "(defn " child-table "-view\n"
     "  ([title rows]\n"
     "   ;; If no parent-id, fallback to normal grid\n"
     "   (build-grid title rows \"" child-table "_table\" (apply array-map (interleave [:id :created] [\"Id\" \"Created\"])) \"/admin/" child-table "\" {:new true :edit true :delete true}))\n"
     "  ([title rows parent-id]\n"
     "   (let [labels [" (if grid-fields
                           (clojure.string/join "\n         "
                                                (map (fn [field] (str "\"" (:label field) "\""))
                                                     grid-fields))
                           "\"Id\" \"Created\"") "]\n"
     "         db-fields [" (if grid-fields
                              (clojure.string/join "\n                   "
                                                   (map (fn [field] (str ":" (:key field)))
                                                        grid-fields))
                              ":id :created") "]\n"
     "         fields (apply array-map (interleave db-fields labels))\n"
     "         table-id \"" child-table "_table\"\n"
     "         href \"/admin/" child-table parent-table "\"\n"
     "         args {:new true :edit true :delete true}\n"
     "         new-record-href (if parent-id\n"
     "                           (str href \"/add-form/\" parent-id)\n"
     "                           (str href \"/add-form\"))]\n"
     "     (if parent-id\n"
     "       (build-grid-with-custom-new title rows table-id fields href args new-record-href)\n"
     "       (build-grid title rows table-id fields href args)))))\n\n"

     "(defn build-" child-table "-fields\n"
     "  [row]\n"
     "  ;; Database-driven field generation using smart-build-field-config\n"
     "  (list\n"
     "   (build-field {:id \"id\" :type \"hidden\" :name \"id\" :value (:id row)})\n"
     "   (build-field {:id \"" parent-fk-column "\" :type \"hidden\" :name \"" parent-fk-column "\" :value (:" parent-fk-column " row)})\n\n"

     ;; Insert the generated form field strings
     (if form-field-strings
       (str/join "\n" form-field-strings)
       "   ;; No additional fields found in database schema")
     "\n   ))\n\n"     "(defn build-" child-table "-form\n"
     "  [title row]\n"
     "  (form \"/admin/" child-table parent-table "/save\" (build-" child-table "-fields row) (build-modal-buttons) title {:bare true}))\n")))

;;; Leiningen task compatibility

(defn build-subgrid
  [child-table parent-table parent-fk-column & [rights]]
  (println "Building subgrid for" child-table "related to" parent-table "via" parent-fk-column)

  ;; REQUIRE database introspection to get real field schema
  (let [db-conn (try
                  (require 'rs.models.crud)
                  (eval 'rs.models.crud/db)
                  (catch Exception e
                    (println "ERROR: Could not access database connection from rs.models.crud")
                    (throw (ex-info "Database connection required for subgrid generation"
                                    {:child-table child-table
                                     :parent-table parent-table
                                     :error (.getMessage e)}))))
        fields (get-table-fields db-conn child-table)        ;; Generate the subgrid model with database-specific namespace
        subgrid-content (str
                         "(ns rs.handlers.admin." child-table parent-table ".model\n"
                         "  (:require [rs.models.crud :refer [Query crud-fix-id]]))\n\n"
                         ";; Generated on " (get-date) "\n\n"
                         (let [query-info (generate-smart-query child-table fields db-conn)
                               main-alias (generate-table-alias child-table)
                               parent-fk-column parent-fk-column]
                           (str
                            "(def get-" child-table "-sql\n"
                            "  (str \"SELECT " (:select query-info) "\n"
                            "         FROM " (:from query-info) "\n"
                            (when (seq (:joins query-info))
                              (str "         " (:joins query-info) "\n"))
                            "         WHERE " main-alias "." parent-fk-column " = ?\n"
                            "         ORDER BY " main-alias ".id DESC\"))\n\n"

                            "(defn get-" child-table "\n"
                            "  [parent-id]\n"
                            "  (Query [get-" child-table "-sql parent-id] :conn :default))\n\n"

                            "(def get-" child-table "-id-sql\n"
                            "  (str \"SELECT " (:select query-info) "\n"
                            "         FROM " (:from query-info) "\n"
                            (when (seq (:joins query-info))
                              (str "         " (:joins query-info) "\n"))
                            "         WHERE " main-alias ".id = ?\"))\n\n"

                            "(defn get-" child-table "-id\n"
                            "  [id]\n"
                            "  (first (Query [get-" child-table "-id-sql (crud-fix-id id)] :conn :default)))\n")))

        ;; Create the file path - WARNING: Hardcoded paths
        file-path (str "src/rs/handlers/admin/" ; WARNING: Hardcoded path
                       child-table parent-table
                       "/model.clj")
        dir-path (str "src/rs/handlers/admin/" ; WARNING: Hardcoded path
                      child-table parent-table)]

    ;; Create directory if it doesn't exist
    (let [dir (java.io.File. dir-path)]
      (when-not (.exists dir)
        (.mkdirs dir)))

    ;; Write the model file
    (println "Generating file:" file-path)
    (spit file-path subgrid-content)

    ;; Generate controller file
    (let [controller-content (build-subgrid-controller child-table parent-table parent-fk-column rights)
          controller-path (str dir-path "/controller.clj")]
      (println "Generating controller:" controller-path)
      (spit controller-path controller-content))

    ;; Generate view file
    (let [view-content (build-subgrid-view child-table parent-table parent-fk-column db-conn)
          view-path (str dir-path "/view.clj")]
      (println "Generating view:" view-path)
      (spit view-path view-content))

    (println "Subgrid model, controller, and view generated successfully!")))

;; Alias for backwards compatibility  
(def build-subgrid-model-compat build-subgrid)

;; Build grid view function - defined first to avoid compilation order issues
(defn build-grid-view [table-name fields db-conn]
  (let [comments (get-table-comments db-conn table-name)
        ;; Generate grid fields - showing only key display fields, not all
        grid-fields (when fields
                      (let [key-fields (take 6 fields)]  ; Show first 6 fields max
                        (map (fn [field]
                               {:key (:name field)
                                :label (clojure.string/capitalize (clojure.string/replace (:name field) #"_" " "))})
                             key-fields)))
        form-fields (when fields
                      (map (fn [field]
                             (let [field-name (:name field)
                                   db-type (:type field)
                                   comment (get comments field-name)
                                   required (= 1 (:notnull field))]
                               {:field-name field-name
                                :db-type db-type
                                :comment comment
                                :required required}))
                           fields))]
    (str
     "(ns rs.handlers.admin." table-name ".view\n"
     "  (:require [rs.models.form :refer [form build-field build-modal-buttons]]\n"
     "            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]\n"
     "            [rs.models.util :refer [smart-build-field-config]]))\n\n"

     ";; " table-name "-view: If you want to show subgrids, pass a :subgrids vector in args.\n"
     ";; Example:\n"
     ";;   (let [args {:new true\n"
     ";;               :edit true\n"
     ";;               :delete true\n"
     ";;               :subgrids [{:title \"Phones\"\n"
     ";;                           :table-name \"phones\"\n"
     ";;                           :foreign-key \"user_id\"\n"
     ";;                           :href \"/admin/phonesusers\"\n"
     ";;                           :icon \"bi bi-telephone\"\n"
     ";;                           :label \"Phones\"}]}]\n"
     ";;     (build-grid-with-subgrids title rows table-id fields href args))\n"
     ";; Otherwise, just use build-grid as usual.\n\n"

     "(defn " table-name "-view\n"
     "  [title rows & [args]]\n"
     "  (let [labels [" (if grid-fields
                          (clojure.string/join "\n            "
                                               (map (fn [field] (str "\"" (:label field) "\""))
                                                    grid-fields))
                          "\"Id\" \"Created\"") "]\n"
     "        db-fields [" (if grid-fields
                             (clojure.string/join "\n                   "
                                                  (map (fn [field] (str ":" (:key field)))
                                                       grid-fields))
                             ":id :created") "]\n"
     "        fields (apply array-map (interleave db-fields labels))\n"
     "        table-id \"" table-name "_table\"\n"
     "        href \"/admin/" table-name "\"\n"
     "        args (or args {:new true :edit true :delete true})]\n"
     "    (if (and (map? args) (contains? args :subgrids))\n"
     "      (build-grid-with-subgrids title rows table-id fields href args)\n"
     "      (build-grid title rows table-id fields href args))))\n\n"

     "(defn build-" table-name "-fields\n"
     "  [row]\n"
     "  (list\n"
     "   (build-field {:id \"id\" :type \"hidden\" :name \"id\" :value (:id row)})\n\n"
     (when form-fields
       (clojure.string/join "\n"
                            (map (fn [field]
                                   (str "   ;; Smart fields based on database schema and migration comments\n"
                                        "   (build-field (smart-build-field-config {:field-name \"" (:field-name field) "\" :db-type \"" (:db-type field) "\" :comment " (if (:comment field) (str "\"" (:comment field) "\"") "nil")
                                        "\n                                           :table-name \"" table-name "\" :required " (:required field) " :value (get row :" (:field-name field) ")}))"))
                                 form-fields)))
     "))\n\n"
     "(defn " table-name "-form-view\n"
     "  [title row]\n"
     "  (form \"/admin/" table-name "/save\" (build-" table-name "-fields row) (build-modal-buttons) title {:bare true}))\n")))

(defn build-admin-crud-functions [table-name fields]
  (let [model-name (str/replace table-name #"_" "-")]
    (str
     ";; Get all records\n"
     "(defn get-" model-name "s []\n"
     "  (Query [(" (str "get-" model-name "-sql") ")] :conn :default))\n\n"

     ";; Get by ID  \n"
     "(defn get-" model-name "-by-id [id]\n"
     "  (first (Query [(" (str "get-" model-name "-id-sql") " id)] :conn :default)))")))

(defn build-admin-model [table-name fields & [db]]
  (let [model-name (str/replace table-name #"_" "-")]
    (str/join "\n\n"
              [(str "(ns rs.handlers.admin." table-name ".model\n"
                    "  (:require [rs.models.crud :refer [Query]]))")
               ""
               (str "(def get-" model-name "-sql\n"
                    "  (str \"SELECT * FROM " table-name "\"))")
               ""
               (str "(defn get-" model-name "\n"
                    "  []\n"
                    "  (Query get-" model-name "-sql :conn :default))")
               ""
               (str "(defn get-" model-name "-id\n"
                    "  [id]\n"
                    "  (first (Query (str \"SELECT * FROM " table-name " WHERE id=\" id) :conn :default)))")])))

(defn build-admin-handler [table-name fields]
  (let [handler-ns (str "rs.handlers.admin." table-name ".controller")
        model-ns (str "rs.handlers.admin." table-name ".model")
        view-ns (str "rs.handlers.admin." table-name ".view")
        model-name (str/replace table-name #"_" "-")
        title-cap (clojure.string/capitalize (clojure.string/replace table-name #"_" " "))]
    (str
     "(ns " handler-ns "\n"
     "  (:require\n"
     "   [" model-ns " :refer [get-" model-name " get-" model-name "-id]]\n"
     "   [" view-ns " :refer [" table-name "-view " table-name "-form-view]]\n"
     "   [rs.layout :refer [application error-404]]\n"
     "   [rs.models.crud :refer [build-form-delete build-form-save]]\n"
     "   [rs.models.util :refer [get-session-id user-level]]\n"
     "   [hiccup.core :refer [html]]))\n\n"

     "(def allowed-rights [\"U\" \"A\" \"S\"])\n\n"

     "(defn " table-name "\n"
     "  [request]\n"
     "  (let [title \"" title-cap "\"\n"
     "        ok (get-session-id request)\n"
     "        js nil\n"
     "        rows (get-" model-name ")\n"
     "        content (" table-name "-view title rows)\n"
     "        user-r (user-level request)]\n"
     "    (if (some #(= user-r %) allowed-rights)\n"
     "      (application request title ok js content)\n"
     "      (application request title ok nil (str \"Not authorized to access this item! (level(s) \" allowed-rights \")\")))))\n\n"

     "(defn " table-name "-add-form\n"
     "  [_]\n"
     "  (let [title \"New " title-cap "\"\n"
     "        row nil\n"
     "        content (" table-name "-form-view title row)]\n"
     "    (html content)))\n\n"

     "(defn " table-name "-edit-form\n"
     "  [_ id]\n"
     "  (let [title \"Edit " title-cap "\"\n"
     "        row (get-" model-name "-id id)\n"
     "        content (" table-name "-form-view title row)]\n"
     "    (html content)))\n\n"

     "(defn " table-name "-save\n"
     "  [{params :params}]\n"
     "  (let [table \"" table-name "\"\n"
     "  result (build-form-save params table :conn :default)]\n"
     "    (if result\n"
     "      {:status 200 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":true}\"}\n"
     "      {:status 500 :headers {\"Content-Type\" \"application/json\"} :body \"{\\\"ok\\\":false}\"})))\n\n"

     "(defn " table-name "-delete\n"
     "  [_ id]\n"
     "  (let [table \"" table-name "\"\n"
     "  result (build-form-delete table id :conn :default)]\n"
     "    (if result\n"
     "      {:status 302 :headers {\"Location\" \"/admin/" table-name "\"}}\n"
     "      (error-404 \"Unable to process record!\" \"/admin/" table-name "\"))))\n")))

(defn build-grid
  [table-name & [_rights]]
  (println "Building grid for table" table-name)

  ;; REQUIRE database introspection to get real field schema
  (let [db-conn (try
                  (require 'rs.models.crud)
                  (eval 'rs.models.crud/db)
                  (catch Exception e
                    (println "ERROR: Could not access database connection from rs.models.crud")
                    (throw (ex-info "Database connection required for grid generation"
                                    {:table-name table-name
                                     :error (.getMessage e)}))))
        fields (get-table-fields db-conn table-name)]

    ;; Generate the model
    (let [model-content (build-admin-model table-name fields db-conn)
          model-path (str "src/rs/handlers/admin/" table-name "/model.clj")
          model-dir (str "src/rs/handlers/admin/" table-name)]

      ;; Create directory if it doesn't exist
      (let [dir (java.io.File. model-dir)]
        (when-not (.exists dir)
          (.mkdirs dir)))

      ;; Write the model file
      (println "Generating file:" model-path)
      (spit model-path model-content))

    ;; Generate controller file
    (let [controller-content (build-admin-handler table-name fields)
          controller-path (str "src/rs/handlers/admin/" table-name "/controller.clj")]
      (println "Generating controller:" controller-path)
      (spit controller-path controller-content))

    ;; Generate view file
    (let [view-content (build-grid-view table-name fields db-conn)
          view-path (str "src/rs/handlers/admin/" table-name "/view.clj")]
      (println "Generating view:" view-path)
      (spit view-path view-content))

    (println "Grid model, controller, and view generated successfully!")))

;; === MISSING FUNCTIONS RESTORATION ===
;; These functions were accidentally deleted and need to be restored

(defn build-dashboard
  [table-name & [rights]]
  (println "Building dashboard for table" table-name)
  (println "Dashboard generation not yet implemented"))

(defn build-report
  [table-name & [rights]]
  (println "Building report for table" table-name)
  (println "Report generation not yet implemented"))

(defn clean-grid
  [table-name]
  (println "Cleaning grid for table" table-name)
  (let [dir-path (str "src/rs/handlers/admin/" table-name)]
    (when (.exists (java.io.File. dir-path))
      (println "Removing directory:" dir-path)
      (.delete (java.io.File. (str dir-path "/model.clj")))
      (.delete (java.io.File. (str dir-path "/controller.clj")))
      (.delete (java.io.File. (str dir-path "/view.clj")))
      (.delete (java.io.File. dir-path))
      (println "Grid files cleaned successfully!"))))

(defn clean-dashboard
  [table-name]
  (println "Cleaning dashboard for table" table-name)
  (println "Dashboard cleaning not yet implemented"))

(defn clean-report
  [table-name]
  (println "Cleaning report for table" table-name)
  (println "Report cleaning not yet implemented"))

(defn clean-subgrid
  [child-table parent-table]
  (println "Cleaning subgrid for" child-table "related to" parent-table)
  (let [dir-path (str "src/rs/handlers/admin/" child-table parent-table)]
    (when (.exists (java.io.File. dir-path))
      (println "Removing directory:" dir-path)
      (.delete (java.io.File. (str dir-path "/model.clj")))
      (.delete (java.io.File. (str dir-path "/controller.clj")))
      (.delete (java.io.File. (str dir-path "/view.clj")))
      (.delete (java.io.File. dir-path))
      (println "Subgrid files cleaned successfully!"))))


