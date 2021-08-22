(ns ruletti-reframe.tiles
  (:require [medley.core :refer [index-by assoc-some]]
            [ruletti-reframe.components :as c]
            [ruletti-reframe.subscriptions :refer [??]]
            [ruletti-reframe.events :refer [!!]]
            [ruletti-reframe.data :as data]))

(defn tile-core [{:keys [content style]}]
  (let [bet (?? :bet content)]
    [:div.tile {:class (?? :tile-style content) :style style}
     [:div.tile-text content]
     (when (> bet 0)
       [:div {:style {:position "absolute" :top "-8px" :right "-5px" :z-index 10}}
        [c/money-ball bet]])
     (when (= (?? :phase) :betting)
       [:<> (when (> (?? :money) 0)
              [:button.small-plus {:on-click (!! :bet :inc content)} "+"])
        (when (> bet 0)
          [:button.small-minus {:on-click (!! :bet :dec content)} "-"])])]))

(defn tile [index]
  (let [{:keys [number span]} (get data/tile-info index)]
    [:div.center-content {:style {:grid-column-end (str "span " (or span 1))}}
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
