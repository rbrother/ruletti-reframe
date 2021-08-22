(ns ruletti-reframe.tiles
  (:require [re-frame.core :as rf]
            [medley.core :refer [index-by assoc-some]]
            [ruletti-reframe.components :as c]
            [ruletti-reframe.data :as data]))

(defn tile-base [{:keys [span style-class content style]}]
  (let [betting? (= :betting @(rf/subscribe [:phase]))
        bet @(rf/subscribe [:bet content])
        has-money? (> @(rf/subscribe [:money]) 0)
        has-bet? (and bet (> bet 0))]
    [:div.center-content
     {:style {:grid-column-end (str "span " (or span 1))
              :display (when-not (number? content) "inline-block")}}
     [:div.tile {:class style-class :style style}
      [:div.tile-text content]
      (when has-bet?
        [:div {:style {:position "absolute" :top "-8px" :right "-5px" :z-index 10}}
         [c/money-ball bet]])
      (when betting?
        [:<> (when has-money?
               [:button.small-plus {:on-click #(rf/dispatch [:bet :inc content])} "+"])
         (when has-bet?
           [:button.small-minus {:on-click #(rf/dispatch [:bet :dec content])} "-"])])]]))

(defn tile [index]
  (let [{:keys [number] :as info} (get data/tile-info index)]
    [tile-base (assoc info
                 :content number
                 :style-class @(rf/subscribe [:tile-style number]))]))

(defn group-tile [content]
  [tile-base {:content content
              :style {:width "115px"}
              :style-class @(rf/subscribe [:tile-style content])}])

(defn group-tiles []
  [:div.line
   [group-tile "Red"]
   [group-tile "Black"]
   [group-tile "1-11"]
   [group-tile "12-22"]])
