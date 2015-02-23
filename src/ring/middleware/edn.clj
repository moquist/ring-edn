(ns ring.middleware.edn
  (:require clojure.edn))

(defn- edn-request?
  [req]
  (if-let [^String type (:content-type req)]
    (not (empty? (re-find #"^application/(vnd.+)?edn" type)))))

(defprotocol EdnRead
  (-read-edn [this]))

(extend-type String
  EdnRead
  (-read-edn [s]
    (clojure.edn/read-string s)))

(extend-type java.io.InputStream
  EdnRead
  (-read-edn [is]
    (clojure.edn/read
     {:eof nil}
     (java.io.PushbackReader.
                       (java.io.InputStreamReader.
                        is "UTF-8")))))

(defn wrap-edn-params
  [handler]
  (fn [req]
    (if-let [body (and (edn-request? req) (:body req))]
      (let [edn-body (binding [*read-eval* false] (-read-edn body))
            req* (if (map? edn-body)
                   (assoc req
                     :edn-params edn-body
                     :params (merge (:params req) edn-body))
                   (assoc req :edn-body edn-body))]
        (handler req*))
      (handler req))))
