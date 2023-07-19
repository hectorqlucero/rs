(ns sk.handlers.clientes.handler
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as java-io]
   [ring.util.io :refer [piped-input-stream]]
   [hiccup.core :refer [html]]
   [pdfkit-clj.core :refer [as-stream gen-pdf]]
   [clj-pdf.core :refer [pdf template]]
   [sk.layout :refer [application]]
   [sk.models.util :refer [get-session-id user-level]]
   [sk.handlers.clientes.model :refer [get-rows]]
   [sk.handlers.clientes.view :refer [clientes-view clientes-activos-view clientes-scripts casas-view]]))

(defn clientes-activos [_]
  (let [title "Clientes"
        ok (get-session-id)
        js (clientes-scripts)
        content (clientes-activos-view title)]
    (application title ok js content)))

(defn get-casas [clientes_id]
  (let [content (casas-view clientes_id)]
    content))

(defn clientes [_]
  (let [title "Clientes"
        ok (get-session-id)
        js (clientes-scripts)
        content (clientes-view title)]
    (application title ok js content)))

(defn clientes-reporte [_]
  (let [title "Clientes"
        ok (get-session-id)
        js nil
        content (html (clientes-view title))]
    (if
     (or
      (= (user-level) "U")
      (= (user-level) "A")
      (= (user-level) "S"))
      {:status 200
       :headers {"Content-Type" "application/pdf"
                 "Content-Disposition" "attachment;filename='clientes.pdf'"}
       :body (as-stream (gen-pdf content))}
      (application title ok js "Solo miembros pueden accessar esta opción!!!"))))

(def clientes-pdf-template
  (template
   (list
    [:cell {:align :left} (str $nombre)]
    [:cell {:align :left} (str $paterno)]
    [:cell {:align :left} (str $materno)]
    [:cell {:align :left} (str $telefono)]
    [:cell {:align :left} (str $celular)]
    [:cell {:align :left} (str $email)]
    [:cell {:align :left} (str $ingresos)]
    [:cell {:align :left} (str $pc)]
    [:cell {:align :left} (str $tipo_creditos_id)])))

(defn generate-report-header []
  [{:background-color [233 233 233]}
   [:paragraph {:align :left} "NOMBRE"]
   [:paragraph {:align :left} "PATERNO"]
   [:paragraph {:align :left} "MATERNO"]
   [:paragraph {:align :left} "TELEFONO"]
   [:paragraph {:align :left} "CELULAR"]
   [:paragraph {:align :left} "EMAIL"]
   [:paragraph {:align :left} "INGRESOS"]
   [:paragraph {:align :left} "PC"]
   [:paragraph {:align :left} "TIPO_CREDITOS_ID"]])

(defn generate-report-body []
  (let [rows (get-rows)]
    (into
     [:table
      {:cell-border true
       :style :normal
       :size 10
       :border true
       :header (generate-report-header)}]
     (clientes-pdf-template rows))))

(defn generate-report-header-options [title]
  {:title title
   :header {:x 20
            :y 830
            :table
            [:pdf-table
             {:border false
              :width-percent 100}
             [100]
             [[:pdf-cell {:type :bold :size 16 :align :center} title]]]}
   :footer "page"
   :left-margin 10
   :right-margin 10
   :top-margin 10
   :bottom-margin 25
   :size :a4
   :orientation :portrait
   :font {:family :helvetica :size 10}
   :align :center
   :pages true})

(defn generate-report [title]
  (piped-input-stream
   (fn [output-stream]
     (pdf
      [(generate-report-header-options title)
       (generate-report-body)]
      output-stream))))

(defn clientes-pdf [_]
  (let [title "Clientes"
        ok (get-session-id)
        js nil
        content "Solo miembros pueden accessar esta opción!!!"]
    (if
     (or
      (= (user-level) "U")
      (= (user-level) "A")
      (= (user-level) "S"))
      {:status 200
       :headers {"Content-Type" "application/pdf"
                 "Content-Disposition" "attachment;filename='clientes'"}
       :body (generate-report title)}
      (application title ok js content))))

(def clientes-csv-headers
  ["NOMBRE " "PATERNO " "MATERNO " "TELEFONO " "CELULAR " "EMAIL " "INGRESOS " "PC " "TIPO_CREDITOS "])

(def clientes-csv-template
  (template
   [(str $nombre) (str $paterno) (str $materno) (str $telefono) (str $celular) (str $email) (str $ingresos_formatted) (str $pc_formatted) (str $tipo_creditos)]))

(defn build-csv [filename]
  (let [rows (get-rows)]
    (with-open [writer (java-io/writer filename)]
      (csv/write-csv writer (cons (vec clientes-csv-headers) (clientes-csv-template rows))))))

(defn clientes-csv [_]
  (build-csv "clientes.csv")
  (let [filename "clientes.csv"
        my-file (slurp filename)]
    (java-io/delete-file filename)
    {:status 200
     :headers {"Content-Type" "text/csv"
               "Content-Disposition" "attachment;filename=clientes.csv"}
     :body my-file}))
