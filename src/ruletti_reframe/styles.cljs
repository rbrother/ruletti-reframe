(ns ruletti-reframe.styles
  (:require-macros [garden.def :refer [defcssfn]])
  (:require [spade.core :refer [defglobal defclass]]
            [garden.units :refer [deg px]]
            [garden.color :refer [rgba]]))

(def center-base {:display "flex", :align-items "center", :justify-content "center"})

(def title-base
  {:font-family "wacamoler"
   :font-size "120px"
   :margin "0 0 12px 0"
   :letter-spacing "6px"
   :z-index 100})

(def fade-animation-base
  {:animation-fill-mode "forwards"
   :animation-duration "0.5s"})

(def bet-button-base
  {:position "absolute"
   :left "3px"
   :background-color "black"
   :font-size "18px"
   :font-weight "normal"
   :width "22px" :height "22px"
   :padding "0"
   :border-radius "4px"
   :border "1px white solid"
   :color :white
   :opacity 0.4})

(defglobal defaults
  [:body {:color "#00ff00", :background-color :black
          :user-select "none"}]
  [:div.center-content center-base]
  [:div.line {:margin "8px"}]
  [:.sans-serif {:font-family "sans-serif"}]
  [:.debug {:background "#00A"}]
  [:.table-head {:border-bottom "1px green solid"}]
  [:div.title
   (merge title-base
     {:position "relative"
      :animation-name "title-start"
      :animation-fill-mode "forwards"
      :animation-duration "3.0s"
      :color "#642"})]
  [:div.title2
   (merge title-base
     {:background "linear-gradient(#FA4, #111)"
      :-webkit-background-clip "text"
      :-webkit-text-fill-color "transparent"})]
  [:div.wheel
   {:display :inline-grid
    :grid-template-columns "auto auto auto auto auto auto auto auto"
    :grid-template-rows "auto auto auto auto auto auto"}]
  [:div.center-area
   {:margin "6px", :border "solid white 3px", :border-radius "10px"
    :grid-column-end "span 6", :grid-row-end "span 4"
    :display "grid" :grid-template-rows "48px 1fr"
    :font-family "donovanregular"
    :font-size "24px"
    :letter-spacing "2px"}]
  [:div.title-area
   (assoc center-base :border-bottom "solid white 3px")]
  [:div.scroller-wrapper
   {:position "relative"
    :overflow "hidden"
    :width "540px"}]
  [:div.scroller
   {:position "relative", :left "500px"
    :white-space "nowrap"
    :animation-name "scroller"
    :animation-fill-mode "forwards"
    :animation-timing-function "linear"
    :animation-duration "25s"
    :animation-iteration-count "infinite"}]
  [:button.large
   {:background-color "#0b0", :color :black
    :margin "8px", :padding "6px 24px 6px 24px"
    :font-size "24px", :font-weight "bold"
    :border "none", :border-radius "8px"
    :cursor "pointer"}]
  [:button.large:hover {:background-color "#0f0"}]
  [:button.small-plus (assoc bet-button-base :top "3px")]
  [:button.small-minus (assoc bet-button-base :bottom "3px")]
  [:button:hover {:opacity 1.0}]
  [:div.money-ball
   {:display "inline-block"
    :background-color "yellow", :color :black
    :border-radius "14px", :padding "4px 6px 2px 6px", :margin "4px"
    :font-weight "bold", :font-family "sans-serif", :font-size "20px"}]
  [:div.tile
   (merge center-base
     {:border "solid 3px black" :border-radius "8px"
      :width "90px" :height "86px" :color :black
      :font-size "32px" :font-weight "bold" :font-family "sans-serif"
      :letter-spacing 0
      :position "relative"})]
  [:.green-bright {:background-color "#2F2"}]
  [:.red-bright {:background-color "#F53"}]
  [:.gray-bright {:background-color "#BBB"}]
  [:.fade-green (assoc fade-animation-base :animation-name "fade-green")]
  [:.fade-red (assoc fade-animation-base :animation-name "fade-red")]
  [:.fade-gray (assoc fade-animation-base :animation-name "fade-gray")]
  [:div.winning-table
   {:display "grid"
    :grid-template-columns "auto auto auto auto"}])
