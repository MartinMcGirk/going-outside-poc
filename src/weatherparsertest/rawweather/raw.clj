(ns weatherparsertest.rawweather.raw
  (:require [clojure.data.json :as json]))

(defn- build-url
  [apiKey lat lon]
  (str "https://api.darksky.net/forecast/" apiKey  "/" lat "," lon "?units=si"))

(defn- weatherdata
  [apiKey lat lon]
  (json/read-str (slurp (build-url apiKey lat lon)) :key-fn keyword))

(defn- get-val-from-maps
  " Applies an operation on a key value to a vector of maps"
  ([f k maps] (get-val-from-maps f k k maps))
  ([f ok nk maps]
   {nk (reduce f (map ok maps))}))

(defn- get-worst-values
  "Applies a vector of operations to a vector of maps
Each operation consists of a function to apply, and a key for the value that it is to be applied to and possibly a key to remap it to."
  [operations maps]
  (into {}
        (map
         #(let [f (:f %)
                ok (:ok %)
                nk (or (:nk %) (:ok %))]
            (get-val-from-maps f ok nk maps))
         operations)))

(def ^:private ops [{:ok :apparentTemperature :f min}
                    {:ok (comp #(* 100 %) :precipProbability) :f max :nk :rainProbability}
                    {:ok :precipIntensity :f max :nk :rainIntensity}
                    {:ok :windSpeed :f max}])

(defn get-x-hours-weather
  "Gets a useful representation of the weather for the next 
n hours. Expects an int number of hours as argument."
  [numHours apiKey lat lon]
  (let [weather (weatherdata apiKey lat lon)]
    (get-worst-values ops
                      (take numHours
                            (get-in weather [:hourly :data])))))
