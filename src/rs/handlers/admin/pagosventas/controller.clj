(ns rs.handlers.admin.pagosventas.controller
  (:require
   [rs.handlers.admin.pagosventas.model :refer [get-pagos get-pagos-id]]
   [rs.handlers.admin.pagosventas.view :refer [pagos-view build-pagos-form]]
   [rs.models.crud :refer [build-form-delete build-form-save crud-fix-id]]
   [rs.models.util :refer [user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

;; Main subgrid endpoint - this is what the subgrid AJAX calls
(defn pagosventas-grid
  [request]
  (let [params (:params request)
  parent-id (crud-fix-id (get params :parent_id))
        title " Pagos "
        rows (get-pagos parent-id)
        content (pagos-view title rows parent-id)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (html content)}
      {:status 403
       :headers {"Content-Type" "text/html"}
       :body "Not authorized to access this item!"})))

(defn pagosventas-add-form
  [request parent-id]
  (let [title "New Pagos"
  row {:id_venta (crud-fix-id parent-id)}
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-pagos-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn pagosventas-edit-form
  [request id]
  (let [title "Edit Pagos"
  row (get-pagos-id (crud-fix-id id))
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-pagos-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn pagosventas-save
  [request]
  (let [params (:params request)
        table "pagos"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-save params table :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))

(defn pagosventas-delete
  [request id]
  (let [table "pagos"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-delete table (crud-fix-id id) :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))
