(ns ruletti-reframe.events
  (:require [re-frame.core :as rf]))

;; Helper-function for simpler syntax
(defn !! [& event-data] #(rf/dispatch (vec event-data)))

(rf/reg-event-fx :initialize-db
  (fn [_ _]
    {:db {:money 50, :phase :title, :rolling-index 0}
     :dispatch-later [{:ms 3000 :dispatch [:intro]}]}))

(rf/reg-event-fx :intro
  (fn [{db :db} _]
    {:db (assoc db :phase :intro)
     :dispatch-later [{:ms 25000 :dispatch [:title-repeat]}]}))

(rf/reg-event-fx :title-repeat
  (fn [{{phase :phase} :db} _]
    (when (= phase :intro) {:dispatch [:initialize-db]})))

(rf/reg-event-db :start-betting
  (fn [db _]
    (-> db (assoc :phase :betting)
      (dissoc :bets))))

(rf/reg-event-db :bet
  (fn [db [_ op target]]
    (-> db
      (update-in [:bets target] (if (= op :inc) inc dec))
      (update :money (if (= op :inc) dec inc)))))

(rf/reg-event-fx :roll-roulette
  (fn [{db :db} _]
    {:db (assoc db :phase :rolling
           :step-delay 100
           :steps-to-slowdown (+ 20 (rand-int 23)))
     :dispatch [:fast-roll]}))

(defn inc-rolling-index [index] (if (= index 22) 0 (inc index)))

(rf/reg-event-fx :fast-roll
  (fn [{{:keys [step-delay steps-to-slowdown] :as db} :db} _]
    (let [slow-down? (= steps-to-slowdown 0)
          next-event (if slow-down? [:slow-down-roll] [:fast-roll])]
      {:db (-> db (update :rolling-index inc-rolling-index)
             (update :steps-to-slowdown dec))
       :dispatch-later [{:ms step-delay, :dispatch next-event}]})))

(rf/reg-event-fx :slow-down-roll
  (fn [{{:keys [step-delay] :as db} :db} _]
    (let [next-event (if (> step-delay 2000) [:roll-finished] [:slow-down-roll])]
      {:db (-> db (update :rolling-index inc-rolling-index)
             (update :step-delay #(* % 1.5)))
       :dispatch-later [{:ms step-delay, :dispatch next-event}]})))

(rf/reg-event-db :roll-finished
  (fn [db _]
    (let [winnings @(rf/subscribe [:total-winnings])]
      (-> db (assoc :phase :winnings)
        (dissoc :step-delay :steps-to-slowdown)
        (update :money #(+ % winnings))))))
