(ns rs.handlers.admin.documentospropiedades.controller
  (:require
   [rs.handlers.admin.documentospropiedades.model :refer [get-documentos get-documentos-id]]
   [rs.handlers.admin.documentospropiedades.view :refer [documentos-view build-documentos-form]]
   [rs.models.crud :refer [build-form-delete build-form-save crud-fix-id]]
   [rs.models.util :refer [user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

;; Main subgrid endpoint - this is what the subgrid AJAX calls
(defn documentospropiedades-grid
  [request]
  (let [params (:params request)
  parent-id (crud-fix-id (get params :parent_id))
        title " Documentos "
        rows (get-documentos parent-id)
        content (documentos-view title rows parent-id)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (html content)}
      {:status 403
       :headers {"Content-Type" "text/html"}
       :body "Not authorized to access this item!"})))

(defn documentospropiedades-add-form
  [request parent-id]
  (let [title "New Documentos"
  row {:id_propiedad (crud-fix-id parent-id)}
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-documentos-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn documentospropiedades-edit-form
  [request id]
  (let [title "Edit Documentos"
  row (get-documentos-id (crud-fix-id id))
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-documentos-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn documentospropiedades-save
  [request]
  (let [params (:params request)
        table "documentos"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-save params table :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))

(defn documentospropiedades-delete
  [request id]
  (let [table "documentos"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-delete table (crud-fix-id id) :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))
