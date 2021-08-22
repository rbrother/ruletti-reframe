(ns ruletti-reframe.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [ruletti-reframe.main :as main]
   [ruletti-reframe.styles] ; Needed for global style generation
   [ruletti-reframe.subscriptions] ; Needed for subs to register
   [ruletti-reframe.events] ; Needed for events to register
   ))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
