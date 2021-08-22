(ns ruletti-reframe.components
  (:require [ruletti-reframe.subscriptions :refer [??]]))

(defn money-ball [amount] [:div.money-ball "$" amount])

(defn money-view [] [:span "Money: " [money-ball (?? :money)]])
