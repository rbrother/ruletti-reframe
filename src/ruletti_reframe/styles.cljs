(ns ruletti-reframe.styles
  (:require-macros [garden.def :refer [defcssfn]])
  (:require [spade.core :refer [defglobal defclass]]
            [garden.units :refer [deg px]]
            [garden.color :refer [rgba]]))

(defglobal defaults
  [:body {:color "#00ff00", :background-color :black
          :font-family "sans-serif"}]
  [:button {:background-color "#0f0"
            :color :black
            :margin "16px"
            :padding "6px 24px 6px 24px"
            :font-size "24px"
            :font-weight "bold"
            :border "none"
            :border-radius "8px"
            :cursor "pointer"}])

(def center-base {:display "flex", :align-items "center", :justify-content "center"})

(defclass center-content [] center-base)

(def tile-base
  (merge center-base
    {:border "solid 3px black" :border-radius "8px"
     :width "80px" :height "64px" :color :black
     :font-size "32px" :font-weight "bold"}))

(defclass center-area []
  {:margin "6px", :border "solid white 3px", :border-radius "10px"
   :grid-column-end "span 6", :grid-row-end "span 4"
   :display "grid" :grid-template-rows "48px 1fr"})

(defclass title-area []
  (merge center-base
    {:border-bottom "solid white 3px"
     :font-size "24px"}))

(defclass scroller-wrapper []
  {:position "relative"
   :overflow "hidden"
   :width "480px"})

(defclass scroller []
  {:position "relative"
   :left "500px"
   :white-space "nowrap"
   :animation-name "scroller"
   :animation-fill-mode "forwards"
   :animation-timing-function "linear"
   :animation-duration "20s"
   :animation-iteration-count "infinite"})

(defclass wheel []
  {:display :inline-grid
   :grid-template-columns "auto auto auto auto auto auto auto auto"
   :grid-template-rows "auto auto auto auto auto auto"})

(defclass green-bright [] (merge tile-base {:background-color "#2F2"}))
(defclass red-bright [] (merge tile-base {:background-color "#F53"}))
(defclass gray-bright [] (merge tile-base {:background-color "#BBB"}))

(defclass fade [animation-name]
  (merge tile-base
    {:animation-name animation-name
     :animation-fill-mode "forwards"
     :animation-duration "0.5s"}))
