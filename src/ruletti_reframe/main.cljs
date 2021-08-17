(ns ruletti-reframe.main
  (:require [re-frame.core :as rf]
            [ruletti-reframe.styles :as styles]
            [medley.core :refer [index-by]]))

(def tile-info
  [{:number 0, :color :green},   {:number 8, :color :black}
   {:number 1, :color :red}     {:number 16, :color :black}
   {:number 5, :color :red}     {:number 10, :color :black}
   {:number 7, :color :red}     {:number 20, :color :black}
   {:number 3, :color :red}     {:number 12, :color :black}
   {:number 17, :color :red}    {:number 14, :color :black}
   {:number 19, :color :red}    {:number 6, :color :black}
   {:number 9, :color :red}     {:number 2, :color :black}
   {:number 21, :color :red}    {:number 22, :color :black}
   {:number 11, :color :red}    {:number 18, :color :black}
   {:number 15, :color :red}    {:number 4, :color :black}
   {:number 13, :color :red}])

(def tile-info-map (index-by :number tile-info))

(def display-colors {:green "#161" :red "#732" :black "#444" })

(defn tile [number]
  (let [span (if (= number 0) 2 1)
        {:keys [color]} (get tile-info-map number)]
    [:div {:class (styles/center-content)
           :style {:grid-column-end (str "span " span)}}
     [:div {:class (styles/tile)
            :style {:background-color (get display-colors color)}}
      number]]))

(defn center-area []
  [:div {:class (styles/center-area) }])

(defn roulette-wheel []
  [:div {:class (styles/wheel)}
   [tile 15] [tile 4] [tile 13]       [tile 0]       [tile 8] [tile 1] [tile 16]
   [tile 18] [center-area]                                             [tile  5]
   [tile 11]                                                           [tile 10]
   [tile 22]                                                           [tile  7]
   [tile 21]                                                           [tile 20]
   [tile  2] [tile 9] [tile 6] [tile 19] [tile 14] [tile 17] [tile 12] [tile  3]])

(defn main-panel []
  [:div {:style {:text-align "center"}}
   [:h1 "Ruletti"]
   [roulette-wheel]])

;; Subscriptions

;; Events

(rf/reg-event-db ::initialize-db (fn [_ _] {}))
