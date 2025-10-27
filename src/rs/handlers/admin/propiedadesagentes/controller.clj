(ns rs.handlers.admin.propiedadesagentes.controller
  (:require
   [rs.handlers.admin.propiedadesagentes.model :refer [get-propiedades get-propiedades-id]]
   [rs.handlers.admin.propiedadesagentes.view :refer [propiedades-view build-propiedades-form]]
   [rs.models.crud :refer [build-form-delete build-form-save crud-fix-id]]
   [rs.models.util :refer [user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

;; Main subgrid endpoint - this is what the subgrid AJAX calls
(defn propiedadesagentes-grid
  [request]
  (let [params (:params request)
  parent-id (crud-fix-id (get params :parent_id))
        title " Propiedades "
        rows (get-propiedades parent-id)
        content (propiedades-view title rows parent-id)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (html content)}
      {:status 403
       :headers {"Content-Type" "text/html"}
       :body "Not authorized to access this item!"})))

(defn propiedadesagentes-add-form
  [request parent-id]
  (let [title "New Propiedades"
  row {:id_agente (crud-fix-id parent-id)}
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-propiedades-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn propiedadesagentes-edit-form
  [request id]
  (let [title "Edit Propiedades"
  row (get-propiedades-id (crud-fix-id id))
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-propiedades-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn propiedadesagentes-save
  [request]
  (let [params (:params request)
        table "propiedades"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-save params table :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))

(defn propiedadesagentes-delete
  [request id]
  (let [table "propiedades"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-delete table (crud-fix-id id) :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))
