(ns rs.handlers.admin.tramites.controller
  (:require
   [rs.handlers.admin.tramites.model :refer [get-tramites get-tramites-id]]
   [rs.handlers.admin.tramites.view :refer [tramites-view tramites-form-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn tramites
  [request]
  (let [title "Trámites"
        ok (get-session-id request)
        js nil
        rows (get-tramites)
        content (tramites-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn tramites-add-form
  [request]
  (let [title "Agregar Trámite"
        ok (get-session-id request)
        js "function submitForm(event) {
              event.preventDefault();
              var form = event.target;
              var formData = new FormData(form);
              var xhr = new XMLHttpRequest();
              xhr.onload = function() {
                if (xhr.status === 200) {
                  window.location.reload();
                } else {
                  alert('Error al guardar el trámite');
                }
              };
              xhr.open('POST', form.action);
              xhr.send(formData);
            }"
        content (html (tramites-form-view title {}))]
    (application request title ok js content)))

(defn tramites-edit-form
  [request]
  (let [id (get-in request [:path-params :id])
        row (get-tramites-id id)
        title "Editar Trámite"
        ok (get-session-id request)
        js "function submitForm(event) {
              event.preventDefault();
              var form = event.target;
              var formData = new FormData(form);
              var xhr = new XMLHttpRequest();
              xhr.onload = function() {
                if (xhr.status === 200) {
                  window.location.reload();
                } else {
                  alert('Error al actualizar el trámite');
                }
              };
              xhr.open('POST', form.action);
              xhr.send(formData);
            }"
        content (html (tramites-form-view title row))]
    (application request title ok js content)))