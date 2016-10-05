(ns weatherparsertest.core
  (:require [weatherparsertest.rawweather.raw :as weather])
  (:require [weatherparsertest.config.config :as config]))

(defn get-weather
  []
  (let [apiKey config/apiKey
        lat 55.9531
        lon 3.1889]
    (weather/get-x-hours-weather 15 apiKey lat lon)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
