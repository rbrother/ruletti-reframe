(ns ruletti-reframe.components
  (:require [re-frame.core :as rf]))

(defn money-ball [amount] [:div.money-ball "$" amount])

(defn money-view []
  (let [money @(rf/subscribe [:money])]
    [:span "Money: " [money-ball money]]))
