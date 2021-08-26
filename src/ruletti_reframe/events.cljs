(ns ruletti-reframe.events
  (:require [re-frame.core :as rf]
            [ruletti-reframe.subscriptions :refer [??]]))

;; Helper-function for simpler syntax
(defn !! [& event-data] #(rf/dispatch (vec event-data)))

(rf/reg-fx :play-sound (fn [name] (.play (js/Audio. (str "sounds/" name)))))

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

(rf/reg-event-fx :start-betting
  (fn [{db :db} _]
    {:play-sound "saloon-piano.ogg"
     :db (-> db (assoc :phase :betting)
           (dissoc :bets))}))

(rf/reg-event-fx :bet
  (fn [{db :db} [_ op target]]
    {:play-sound "money-drop.wav"
     :db (-> db
           (update-in [:bets target] (if (= op :inc) inc dec))
           (update :money (if (= op :inc) dec inc)))}))

(rf/reg-event-fx :roll-roulette
  (fn [{db :db} _]
    {:play-sound "saloon-piano2.ogg"
     :db (assoc db :phase :rolling
           :step-delay 100
           :steps-to-slowdown (+ 20 (rand-int 23)))
     :dispatch [:fast-roll]}))

(defn inc-rolling-index [index] (if (= index 22) 0 (inc index)))

(rf/reg-event-fx :fast-roll
  (fn [{{:keys [step-delay steps-to-slowdown] :as db} :db} _]
    (let [slow-down? (= steps-to-slowdown 0)
          next-event (if slow-down? [:slow-down-roll] [:fast-roll])]
      {:play-sound "hihat3.wav"
       :db (-> db (update :rolling-index inc-rolling-index)
             (update :steps-to-slowdown dec))
       :dispatch-later [{:ms step-delay, :dispatch next-event}]})))

(rf/reg-event-fx :slow-down-roll
  (fn [{{:keys [step-delay] :as db} :db} _]
    (let [next-event (if (> step-delay 2000) [:winnings] [:slow-down-roll])]
      {:play-sound "hihat3.wav"
       :db (-> db (update :rolling-index inc-rolling-index)
             (update :step-delay #(* % 1.5)))
       :dispatch-later [{:ms step-delay, :dispatch next-event}]})))

(rf/reg-event-fx :winnings
  (fn [{db :db} _]
    (let [winnings (?? :total-winnings)]
      {:play-sound (cond (> winnings 9) "cheer.ogg"
                     (> winnings 0) "success-jingle.ogg"
                     :else "fail.mp3")
       :db (-> db (assoc :phase :winnings)
             (dissoc :step-delay :steps-to-slowdown)
             (update :money #(+ % winnings)))})))
