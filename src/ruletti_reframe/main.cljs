(ns ruletti-reframe.main
  (:require [re-frame.core :as rf]
            [ruletti-reframe.styles :as styles]))

(defn main-panel []
  [:div
   [:h1 "Hello from Ruletti"]])

(rf/reg-event-db ::initialize-db (fn [_ _] {}))
