(ns ruletti-reframe.data
  (:require [medley.core :refer [index-by assoc-some]]))

(def tile-info
  [{:number 0, :color :green, :span 2}, {:number 8, :color :black}
   {:number 1, :color :red} {:number 16, :color :black}
   {:number 5, :color :red} {:number 10, :color :black}
   {:number 7, :color :red} {:number 20, :color :black}
   {:number 3, :color :red} {:number 12, :color :black}
   {:number 17, :color :red} {:number 14, :color :black}
   {:number 19, :color :red} {:number 6, :color :black}
   {:number 9, :color :red} {:number 2, :color :black}
   {:number 21, :color :red} {:number 22, :color :black}
   {:number 11, :color :red} {:number 18, :color :black}
   {:number 15, :color :red} {:number 4, :color :black}
   {:number 13, :color :red}])

(def tile-map
  (assoc (index-by :number tile-info)
    "Red" {:color :red}
    "Black" {:color :black}
    "1-11" {:color :red}
    "12-22" {:color :red}))

(def colors
  {:dim {:green "fade-green" :red "fade-red" :black "fade-gray"}
   :bright {:green "green-bright" :red "red-bright" :black "gray-bright"}})
