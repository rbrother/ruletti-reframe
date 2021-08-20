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

(def colors
  {:dim {:green (styles/fade "fade-green") :red (styles/fade "fade-red")
         :black (styles/fade "fade-gray")}
   :bright {:green (styles/green-bright) :red (styles/red-bright)
            :black (styles/gray-bright)}})

(defn money-ball [amount]
  [:div {:class (styles/money-ball)} "$" amount])

(defn tile-base [{:keys [span color content style]}]
  (let [phase @(rf/subscribe [::phase])
        bet @(rf/subscribe [::bet content])
        has-money? (> @(rf/subscribe [::money]) 0)
        has-bet? (and bet (> bet 0))]
    [:div {:class (styles/center-content)
           :style {:grid-column-end (str "span " (or span 1))
                   :display "inline-block"
                   :position "relative"}}
     [:div {:class color :style style} content]
     (when has-bet?
       [:div {:style {:position "absolute" :top "-6px" :right "-3px" :z-index 10}}
        [money-ball bet]])
     (when (and (= phase :betting) has-money?)
       [:div {:style {:position "absolute" :top "-10px" :left "-10px"}}
        [:button {:class (styles/small-button)
                  :on-click #(rf/dispatch [::bet :inc content])} "+"]])
     (when (and (= phase :betting) has-bet?)
       [:div {:style {:position "absolute" :bottom "-10px" :left "-10px"}}
        [:button {:class (styles/small-button)
                  :on-click #(rf/dispatch [::bet :dec content])} "-"]])]))

(defn tile [index]
  (let [{:keys [number] :as info} (get tile-info index)
        color @(rf/subscribe [::tile-color index])]
    (tile-base (assoc info :color color :content number))))

(defn group-tile [info]
  [tile-base (assoc info :style {:width "115px"})])

(defn intro-view []
  [:<> [:div {:class (styles/title-area)}
        [:div {:class (styles/scroller-wrapper)}
         [:div {:class (styles/scroller)}
          "Ruletti Re-Frame programmed by Robert J. Brotherus 2021-08-19.
          Remake of Ruletti-64 for Commodore 64 programmed in 1987
          and published in MikroBitti magazine 1988/05"]]]
   [:div {:class (styles/center-content)}
    [:button {:on-click #(rf/dispatch [::start-betting])} "Let's Play!"]]])

(defn group-tiles []
  [:div {:class (styles/line)}
   [group-tile {:content "Red" :color (get-in colors [:dim :red])}]
   [group-tile {:content "Black" :color (get-in colors [:dim :black])}]
   [group-tile {:content "1-11" :color (get-in colors [:dim :red])}]
   [group-tile {:content "12-22" :color (get-in colors [:dim :red])}]])

(defn betting-view []
  (let [money @(rf/subscribe [::money])
        total-bets @(rf/subscribe [::total-bets])]
    [:<> [:div {:class (styles/title-area)} "Betting"]
     [:div
      [:div {:class (styles/line)}
       [:span "Money: " [money-ball money]]
       [:span {:style {:margin-left "32px"}} "Bets: " [money-ball total-bets]]]
      [:div {:class (styles/line)} "Place bets with +/-"]
      [group-tiles]
      (when (> total-bets 0)
        [:div
         [:button {:on-click #(rf/dispatch [::roll-roulette])} "Roll the Roulette!"]])]]))

(defn rolling-view []
  [:<> [:div {:class (styles/title-area)} "Rolling"]
   [:div
    [:div {:class (styles/line)} [group-tiles]]
    [:div {:class (styles/line)} "Wait..."]]])

(defn winnings-view []
  [:<> [:div {:class (styles/title-area)} "Winnings"]
   [:div
    [:div {:class (styles/line)} [group-tiles]]
    [:div [:button {:on-click #(rf/dispatch [::start-betting])} "Continue"]]]])

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
  (let [title-phase (= :title @(rf/subscribe [::phase]))]
    [:div {:style {:text-align "center" :position "relative"}}
     [:h1 {:class (if title-phase (styles/title) (styles/title2))} "Ruletti"]
     (when (not title-phase) [roulette-wheel])]))

;; Subscriptions

(rf/reg-sub ::money (fn [db _] (:money db)))

(rf/reg-sub ::phase (fn [db _] (:phase db)))

(rf/reg-sub ::bets (fn [db _] (:bets db)))

(rf/reg-sub ::bet :<- [::bets]
  (fn [bets [_ target]] (get bets target)))

(rf/reg-sub ::total-bets :<- [::bets]
  (fn [bets _] (apply + (vals bets))))

(rf/reg-sub ::tile-color
  (fn [{:keys [rolling-index]} [_ index]]
    (let [brightness (if (= rolling-index index) :bright :dim)
          {:keys [color]} (get tile-info index)]
      (get-in colors [brightness color]))))

;; Events

(rf/reg-event-fx ::initialize-db
  (fn [_ _]
    {:db {:money 10, :phase :title, :rolling-index 0}
     :dispatch-later [{:ms 3000 :dispatch [::intro]}]}))

(rf/reg-event-fx ::intro
  (fn [{db :db} _]
    {:db (assoc db :phase :intro)
     :dispatch-later [{:ms 25000 :dispatch [::title-repeat]}]}))

(rf/reg-event-fx ::title-repeat
  (fn [{{phase :phase} :db} _]
    (when (= phase :intro) {:dispatch [::initialize-db]})))

(rf/reg-event-db ::start-betting
  (fn [db _]
    (-> db (assoc :phase :betting)
      (dissoc :bets))))

(rf/reg-event-db ::bet
  (fn [db [_ op target]]
    (-> db
      (update-in [:bets target] (if (= op :inc) inc dec))
      (update :money (if (= op :inc) dec inc)))))

(rf/reg-event-fx ::roll-roulette
  (fn [{db :db} _]
    {:db (assoc db :phase :rolling
           :step-delay 100
           :steps-to-slowdown (+ 20 (rand-int 23)))
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
