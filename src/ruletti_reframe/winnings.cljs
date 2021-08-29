(ns ruletti-reframe.winnings
  (:require [ruletti-reframe.components :as c]
            [ruletti-reframe.subscriptions :refer [??]]
            [ruletti-reframe.events :refer [!!]]
            [ruletti-reframe.tiles :as tiles]))

(defn winnings-table []
  (into [:div.winning-table
         [:span.table-head "Target"] [:span.table-head "Bet"]
         [:span.table-head "Factor"] [:span.table-head "Win"]]
    (->> (?? :winnings)
      (filter (fn [{:keys [winning]}] (> winning 0)))
      (map (fn [{:keys [target bet factor winning]}]
             [:<> [:span target] [:span [c/money-ball bet]]
              [:span "X " factor] [:span [c/money-ball winning]]])))))

(defn view []
  [:<> [:div.title-area "Winnings"]
   [:div
    [:div.line [tiles/group-tiles]]
    (if (= (?? :total-winnings) 0)
      [:div "No Wins! " [:span.sans-serif "\uD83D\uDE12"]]
      [winnings-table])
    [:div
     [c/money-view]
     (if (> (?? :money) 0)
       [:button.large {:on-click (!! :start-betting)} "Bet Again!"]
       " - GAME OVER")]]])
