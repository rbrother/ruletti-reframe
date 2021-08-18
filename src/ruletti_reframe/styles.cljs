(ns ruletti-reframe.styles
  (:require-macros [garden.def :refer [defcssfn]])
  (:require [spade.core :refer [defglobal defclass]]
            [garden.units :refer [deg px]]
            [garden.color :refer [rgba]]))

(defglobal defaults
  [:body {:color "#00ff00", :background-color :black}])

(defclass center-content []
  {:display "flex", :align-items "center", :justify-content "center"})

(defclass tile []
  {:border "solid 3px black" :border-radius "8px"
   :width "80px" :height "64px" :color :black
   :display "flex" :align-items "center" :justify-content "center"
   :font-size "32px" :font-weight "bold"
   :font-family "sans-serif"})

(defclass center-area []
  {:margin "6px", :border "solid white 3px", :border-radius "10px"
   :grid-column-end "span 6", :grid-row-end "span 4"
   :display "grid" :grid-template-rows "48px 1fr"})

(defclass title-area [] {:border-bottom "solid white 3px"})

(defclass wheel []
  {:display :inline-grid
   :grid-template-columns "auto auto auto auto auto auto auto auto"
   :grid-template-rows "auto auto auto auto auto auto"})
