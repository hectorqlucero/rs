{:connections
 {;; --- Local SQLite database ---
  :sqlite {:db-type   "sqlite"
           :db-class  "org.sqlite.JDBC"
           :db-name   "db/rs.sqlite"}                   ;; No user/pwd needed for SQLite
  ;; --- Default connection used by the app ---
  :main :sqlite ; Used for migrations
  :default :sqlite ; Used for generators (lein grid, lein dashboard, etc.)
  }

 ;; --- Other global app settings ---
 :uploads      "./uploads/rs/"      ;; Path for file uploads
 :site-name    "RS"                 ;; App/site name
 :company-name "Ruiz Software Solutions"            ;; Company name
 :port         3000                        ;; App port
 :tz           "US/Pacific"                ;; Timezone
 :base-url     "http://0.0.0.0:3000/"      ;; Base URL
 :img-url      "https://0.0.0.0/uploads/"  ;; Image base URL
 :path         "/uploads/"                 ;; Uploads path (for web)
 :max-upload-mb 5                            ;; Optional: max image upload size in MB
 :allowed-image-exts ["jpg" "jpeg" "png" "gif" "bmp" "webp"] ;; Optional: allowed image extensions
 ;; --- Theme selection ---
 :theme "sketchy" ;; Options: "default" (Bootstrap), "cerulean", "slate", "minty", "lux", "cyborg", "sandstone", "superhero", "flatly", "yeti"
 ;; Optional email config
 :email-host   "smtp.example.com"
 :email-user   "user@example.com"
 :email-pwd    "emailpassword"}
