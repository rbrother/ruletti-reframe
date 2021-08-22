(ns ruletti-reframe.main
  (:require [medley.core :refer [index-by assoc-some]]
            [ruletti-reframe.subscriptions :refer [??]]
            [ruletti-reframe.events :refer [!!]]
            [ruletti-reframe.components :as c]
            [ruletti-reframe.tiles :as tiles :refer [tile]]
            [ruletti-reframe.winnings :as winnings]))

(defn intro-view []
  [:<> [:div.title-area
        [:div.scroller-wrapper
         [:div.scroller
          "Ruletti Re-Frame programmed by Robert J. Brotherus 2021-08-19.
          Remake of Ruletti-64 for Commodore 64 programmed in 1987
          and published in MikroBitti magazine 1988/05"]]]
   [:div.center-content
    [:button.large {:on-click (!! :start-betting)} "Let's Play!"]]])

(defn betting-view []
  (let [total-bets (?? :total-bets)]
    [:<> [:div.title-area "Betting"]
     [:div
      [:div.line
       [c/money-view]
       [:span {:style {:margin-left "32px"}} "Bets: " [c/money-ball total-bets]]]
      [:div.line "Place bets with +/-"]
      [tiles/group-tiles]
      (when (> total-bets 0)
        [:button.large {:on-click (!! :roll-roulette)} "Roll the Roulette!"])]]))

(defn rolling-view []
  [:<> [:div.title-area "Rolling"]
   [:div
    [:div.line [tiles/group-tiles]]
    [:div.line "Wait..."]]])

(defn center-area []
  [:div.center-area
   (case (?? :phase)
     :intro [intro-view]
     :betting [betting-view]
     :rolling [rolling-view]
     :winnings [winnings/view]
     [:div])])

(defn roulette-wheel []
  [:div.wheel
   [tile 20] [tile 21] [tile 22] [tile 0] [tile 1] [tile 2] [tile 3]
   [tile 19] [center-area] [tile 4]
   [tile 18] [tile 5]
   [tile 17] [tile 6]
   [tile 16] [tile 7]
   [tile 15] [tile 14] [tile 13] [tile 12] [tile 11] [tile 10] [tile 9] [tile 8]])

(defn main-panel []
  (let [title-phase (= :title (?? :phase))]
    [:div {:style {:text-align "center" :position "relative"}}
     [:div {:class (if title-phase "title" "title2")} "Ruletti"]
     (when (not title-phase) [roulette-wheel])]))
