(ns ruletti-reframe.subscriptions
  (:require [re-frame.core :as rf]
            [ruletti-reframe.data :as data]))

(rf/reg-sub :money (fn [db _] (:money db)))

(rf/reg-sub :phase (fn [db _] (:phase db)))

(rf/reg-sub :rolling-index (fn [db _] (:rolling-index db)))

(rf/reg-sub :bets (fn [db _] (:bets db)))

(rf/reg-sub :bet :<- [:bets]
  (fn [bets [_ target]] (get bets target)))

(rf/reg-sub :total-bets :<- [:bets]
  (fn [bets _] (apply + (vals bets))))

(defn target-match? [target rolling-index]
  (let [{:keys [number color]} (get data/tile-info rolling-index)]
    (or (= target number)
      (and (= target "Red") (= color :red))
      (and (= target "Black") (= color :black))
      (and (= target "1-11") (<= 1 number 11))
      (and (= target "12-22") (>= number 12)))))

(rf/reg-sub :tile-style :<- [:rolling-index]
  (fn [rolling-index [_ content]]
    (let [brightness (if (target-match? content rolling-index) :bright :dim)
          {:keys [color]} (get data/tile-map content)]
      (get-in data/colors [brightness color]))))

(defn target-sort-fn [target]
  (if (number? target) 0 1))

(rf/reg-sub :winnings :<- [:rolling-index] :<- [:bets]
  (fn [[rolling-index bets] _]
    (->> data/tile-map
      (keys)
      (filter #(target-match? % rolling-index))
      (sort-by target-sort-fn)
      (map (fn [target]
             (let [bet (get bets target 0)
                   factor (if (number? target) 22 2)]
               {:target target :bet bet :factor factor
                :winning (* bet factor)}))))))

(rf/reg-sub :total-winnings :<- [:winnings]
  (fn [winnings _]
    (->> winnings (map :winning) (apply +))))

