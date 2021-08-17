(ns ruletti-reframe.styles
  (:require-macros
    [garden.def :refer [defcssfn]])
  (:require
    [spade.core   :refer [defglobal defclass]]
    [garden.units :refer [deg px]]
    [garden.color :refer [rgba]]))

(defcssfn linear-gradient
  ([c1 p1 c2 p2]
   [[c1 p1] [c2 p2]])
  ([dir c1 p1 c2 p2]
   [dir [c1 p1] [c2 p2]]))

(defglobal defaults
  [:body
   {:color               "#00ff00"
    :background-color    :black}])

(defclass level1
  []
  {:background-color :black
   :color :green})
