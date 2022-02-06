(ns ruletti-reframe.tiles
  (:require [medley.core :refer [index-by assoc-some]]
            [ruletti-reframe.components :as c]
            [ruletti-reframe.subscriptions :refer [??]]
            [ruletti-reframe.events :refer [!!]]
            [ruletti-reframe.data :as data]))

(defn tile-core [{:keys [content style]}]
  [:div.tile {:class (?? :tile-style content) :style style
              :on-click (!! :bet content)}
   [:div.tile-text content]
   [:div.tile-money [c/money-ball-opt (?? :bet content)]]])

(defn tile [index]
  (let [{:keys [number style]} (get data/tile-info index)]
    [:div.center-content {:style style}
     [tile-core {:content number}]]))

(defn group-tile [content]
  [:div.center-content {:style {:display "inline-block"}}
   [tile-core {:content content, :style {:width "120px"}}]])

(defn group-tiles []
  [:div.line
   [group-tile "Red"]
   [group-tile "Black"]
   [group-tile "1-11"]
   [group-tile "12-22"]])
