(ns ruletti-reframe.components
  (:require [ruletti-reframe.subscriptions :refer [??]]))

(defn money-ball [amount] [:div.money-ball "$" amount])

(defn money-ball-opt [amount] (when (> amount 0) [money-ball amount]))

(defn money-view [] [:span "Money: " [money-ball (?? :money)]])
