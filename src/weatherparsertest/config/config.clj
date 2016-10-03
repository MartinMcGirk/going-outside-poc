(ns weatherparsertest.config.config
  (:require [clojure.edn :as edn]))

(defn load-config
  "Given a filename, load & return a config file"
  [filename]
  (edn/read-string (slurp filename)))

(def apiKey (str (:darkskyApiKey (load-config "resources/config.edn"))))
