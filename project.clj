(defproject fogus/ring-edn "0.3.0-SNAPSHOT"
  :description "A Ring middleware that augments :params by parsing a request body as Extensible Data Notation (EDN)."
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:test {:dependencies [[ring-clj-params "0.1.0"]]}})
