(ns ruletti-reframe.main
  (:require [re-frame.core :as rf]
            [ruletti-reframe.styles :as styles]
            [medley.core :refer [index-by]]))

(def tile-info
  [{:number 0, :color :green, :span 2}, {:number 8, :color :black}
   {:number 1, :color :red} {:number 16, :color :black}
   {:number 5, :color :red} {:number 10, :color :black}
   {:number 7, :color :red} {:number 20, :color :black}
   {:number 3, :color :red} {:number 12, :color :black}
   {:number 17, :color :red} {:number 14, :color :black}
   {:number 19, :color :red} {:number 6, :color :black}
   {:number 9, :color :red} {:number 2, :color :black}
   {:number 21, :color :red} {:number 22, :color :black}
   {:number 11, :color :red} {:number 18, :color :black}
   {:number 15, :color :red} {:number 4, :color :black}
   {:number 13, :color :red}])

(def display-colors
  {:dim {:green "#161" :red "#732" :black "#444"}
   :bright {:green "#2F2" :red "#F53" :black "#BBB"}})

(defn tile [index]
  (let [{:keys [span number]} (get tile-info index)
        color @(rf/subscribe [::tile-color index])]
    [:div {:class (styles/center-content)
           :style {:grid-column-end (str "span " (or span 1))}}
     [:div {:class (styles/tile)
            :style {:background-color color}}
      number]]))

(defn intro-view []
  [:<> [:div {:class (styles/title-area)} "Ruletti Re-Frame by Robert Brotherus 2021"]
   [:div [:button {:on-click #(rf/dispatch [::start-betting])} "Start"]]])

(defn betting-view []
  [:<> [:div {:class (styles/title-area)} "Betting"]
   [:div [:button {:on-click #(rf/dispatch [::roll-roulette])} "Ready"]]])

(defn rolling-view []
  [:<> [:div {:class (styles/title-area)} "Rolling"]
   [:div "Wait..."]])

(defn winnings-view []
  [:<> [:div {:class (styles/title-area)} "Winnings"]
   [:div [:button {:on-click #(rf/dispatch [::start-betting])} "Continue"]]])

(defn center-area []
  [:div {:class (styles/center-area)}
   (case @(rf/subscribe [::phase])
     :intro [intro-view]
     :betting [betting-view]
     :rolling [rolling-view]
     :winnings [winnings-view]
     [:div])])

(defn roulette-wheel [] ;; TODO: In the end, format this as a rectangle
  [:div {:class (styles/wheel)}
   [tile 20] [tile 21] [tile 22] [tile 0] [tile 1] [tile 2] [tile 3]
   [tile 19] [center-area] [tile 4]
   [tile 18] [tile 5]
   [tile 17] [tile 6]
   [tile 16] [tile 7]
   [tile 15] [tile 14] [tile 13] [tile 12] [tile 11] [tile 10] [tile 9] [tile 8]])

(defn main-panel []
  [:div {:style {:text-align "center"}}
   [:h1 "Ruletti"]
   [roulette-wheel]])

;; Subscriptions

(rf/reg-sub ::phase (fn [db _] (:phase db)))

(rf/reg-sub ::tile-color
  (fn [{:keys [rolling-index]} [_ index]]
    (let [brightness (if (= rolling-index index) :bright :dim)
          {:keys [color]} (get tile-info index)]
      (get-in display-colors [brightness color]))))

;; Events

(rf/reg-event-db ::initialize-db (fn [_ _] {:phase :intro}))

(rf/reg-event-db ::start-betting (fn [db _] (assoc db :phase :betting)))

(rf/reg-event-fx ::roll-roulette
  (fn [{db :db} _] {:db (assoc db :phase :rolling
                          :rolling-index 0
                          :step-delay 200
                          :steps-to-slowdown (+ 10 (rand-int 23)))
                    :dispatch [::fast-roll]}))

(defn inc-rolling-index [index] (if (= index 22) 0 (inc index)))

(rf/reg-event-fx ::fast-roll
  (fn [{{:keys [step-delay steps-to-slowdown] :as db} :db} _]
    (let [slow-down? (= steps-to-slowdown 0)
          next-event (if slow-down? [::slow-down-roll] [::fast-roll])]
      {:db (-> db (update :rolling-index inc-rolling-index)
             (update :steps-to-slowdown dec))
       :dispatch-later [{:ms step-delay, :dispatch next-event}]})))

(rf/reg-event-fx ::slow-down-roll
  (fn [{{:keys [step-delay] :as db} :db} _]
    (let [next-event (if (> step-delay 2000) [::roll-finished] [::slow-down-roll])]
      {:db (-> db (update :rolling-index inc-rolling-index)
             (update :step-delay #(* % 1.5)))
       :dispatch-later [{:ms step-delay, :dispatch next-event}]})))

(rf/reg-event-db ::roll-finished
  (fn [db _]
    (-> db (assoc :phase :winnings)
      (dissoc :step-delay :steps-to-slowdown))))
