(ns ruletti-reframe.styles
  (:require-macros [garden.def :refer [defcssfn]])
  (:require [spade.core :refer [defglobal defclass]]
            [garden.units :refer [deg px]]
            [garden.color :refer [rgba]]))

(defglobal defaults
  [:body {:color "#00ff00", :background-color :black
          :user-select "none"}]
  [:button {:background-color "#0f0"
            :color :black
            :margin "16px"
            :padding "6px 24px 6px 24px"
            :font-size "24px"
            :font-weight "bold"
            :border "none"
            :border-radius "8px"
            :cursor "pointer"}])

(def title-base
  {:font-family "wacamoler"
   :font-size "120px"
   :margin "0 0 12px 0"
   :letter-spacing "6px"
   :z-index 100})

(defclass title []
  (merge title-base
    {:position "relative"
     :animation-name "title-start"
     :animation-fill-mode "forwards"
     :animation-duration "3.0s"
     :color "#642"}))

(defclass title2 []
  (merge title-base
    {:background "-webkit-linear-gradient(#FA4, #111)"
     :-webkit-background-clip "text"
     :-webkit-text-fill-color "transparent"}))

(defclass small-button []
  {:background-color "black"
   :font-size "18px"
   :font-weight "normal"
   :width "22px" :height "22px"
   :padding "0"
   :border-radius "4px"
   :border "1px white solid"
   :color :white
   :opacity 0.4})

(defclass money-ball []
  {:display "inline-block"
   :background-color "yellow"
   :color :black
   :border-radius "14px"
   :padding "4px 6px 2px 6px"
   :margin "4px"
   :font-weight "bold"
   :font-family "sans-serif"
   :font-size "20px"})

(defclass line [] {:margin "8px"})

(def center-base {:display "flex", :align-items "center", :justify-content "center"})

(defclass center-content [] center-base)

(def tile-base
  (merge center-base
    {:border "solid 3px black" :border-radius "8px"
     :width "90px" :height "75px" :color :black
     :font-size "32px" :font-weight "bold"
     :font-family "sans-serif"
     :letter-spacing 0}))

(defclass center-area []
  {:margin "6px", :border "solid white 3px", :border-radius "10px"
   :grid-column-end "span 6", :grid-row-end "span 4"
   :display "grid" :grid-template-rows "48px 1fr"
   :font-family "donovanregular"
   :font-size "24px"
   :letter-spacing "2px"})

(defclass title-area []
  (merge center-base
    {:border-bottom "solid white 3px"}))

(defclass scroller-wrapper []
  {:position "relative"
   :overflow "hidden"
   :width "540px"})

(defclass scroller []
  {:position "relative"
   :left "500px"
   :white-space "nowrap"
   :animation-name "scroller"
   :animation-fill-mode "forwards"
   :animation-timing-function "linear"
   :animation-duration "25s"
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

(defclass winning-table []
  {:font-size "18px"
   :display "grid"
   :grid-template-columns "auto auto auto auto"})
