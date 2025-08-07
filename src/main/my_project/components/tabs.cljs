(ns my-project.components.tabs
  (:require [reagent.core :as r]))

(defn tabs
  "Tabs/Segmented Control component following Storybook design system
   Supports multiple tabs with active state management"
  [{:keys [tabs active-tab on-tab-change]
    :or {tabs [{"Patients" "patients"} {"Providers" "providers"}]
         active-tab "patients"}}]
  (r/with-let [current-tab (r/atom active-tab)]
    
    [:div {:class "tabs-container"}
     (for [[label value] tabs]
       ^{:key value}
       [:button {:class (str "tab"
                            (when (= @current-tab value) " tab--active"))
                :on-click (fn []
                           (reset! current-tab value)
                           (when on-tab-change
                             (on-tab-change value)))}
        label])]))

(defn segmented-control
  "Segmented control specifically for Patients/Providers switching"
  [{:keys [active-option on-change]
    :or {active-option "patients"}}]
  (r/with-let [current-option (r/atom active-option)]
    
    [:div {:class "segmented-control"}
     [:button {:class (str "tab"
                          (when (= @current-option "patients") " tab--active"))
              :on-click (fn []
                         (reset! current-option "patients")
                         (when on-change (on-change "patients")))}
      "Patients"]
     
     [:button {:class (str "tab"
                          (when (= @current-option "providers") " tab--active"))
              :on-click (fn []
                         (reset! current-option "providers")
                         (when on-change (on-change "providers")))}
      "Providers"]]))

;; Specific tabs for the Step 3 screen
(defn patients-providers-tabs
  "Patients/Providers tabs for Step 3 screen"
  [{:keys [on-tab-change]
    :or {}}]
  [segmented-control {:active-option "patients"
                      :on-change on-tab-change}])