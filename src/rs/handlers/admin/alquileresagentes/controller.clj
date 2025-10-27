(ns rs.handlers.admin.alquileresagentes.controller
  (:require
   [rs.handlers.admin.alquileresagentes.model :refer [get-alquileres get-alquileres-id]]
   [rs.handlers.admin.alquileresagentes.view :refer [alquileres-view build-alquileres-form]]
   [rs.models.crud :refer [build-form-delete build-form-save crud-fix-id]]
   [rs.models.util :refer [user-level]]
   [hiccup.core :refer [html]]))

;; Permissions - default to all rights if none specified
(def allowed-rights ["U" "A" "S"])

;; Main subgrid endpoint - this is what the subgrid AJAX calls
(defn alquileresagentes-grid
  [request]
  (let [params (:params request)
  parent-id (crud-fix-id (get params :parent_id))
        title " Alquileres "
        rows (get-alquileres parent-id)
        content (alquileres-view title rows parent-id)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (html content)}
      {:status 403
       :headers {"Content-Type" "text/html"}
       :body "Not authorized to access this item!"})))

(defn alquileresagentes-add-form
  [request parent-id]
  (let [title "New Alquileres"
  row {:id_agente (crud-fix-id parent-id)}
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-alquileres-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn alquileresagentes-edit-form
  [request id]
  (let [title "Edit Alquileres"
  row (get-alquileres-id (crud-fix-id id))
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (html (build-alquileres-form title row))
      {:status 403 :headers {"Content-Type" "text/html"} :body "Not authorized to access this item!"})))

(defn alquileresagentes-save
  [request]
  (let [params (:params request)
        table "alquileres"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-save params table :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))

(defn alquileresagentes-delete
  [request id]
  (let [table "alquileres"
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
  (let [result (build-form-delete table (crud-fix-id id) :conn :default)]
        (if result
          {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
          {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"}))
      {:status 403 :headers {"Content-Type" "application/json"} :body "{\"ok\":false,\"error\":\"Not authorized\"}"})))
