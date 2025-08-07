(ns my-project.components.search-input
  (:require [reagent.core :as r]))

(defn search-input
  "Search Input component following Storybook design system
   State: Default, Focus, Error
   Text Size: sm, md, lg
   Supports leading/trailing icons and help text"
  [{:keys [state text-size error? show-header? show-help-text? show-title?
           requested? optional? leading-icon? trailing-icon? 
           input-text placeholder help-text title
           on-change on-focus on-blur]
    :or {state "default"
         text-size "md"
         error? false
         show-header? false
         show-help-text? false
         show-title? true
         requested? false
         optional? false
         leading-icon? true
         trailing-icon? false
         placeholder "Search Patients by Name or Patient ID"
         help-text "Help Text"
         title "Search"}}]
  (r/with-let [input-value (r/atom (or input-text ""))
               is-focused? (r/atom false)]
    
    (let [;; CSS classes for different states
          field-classes (str "search-input-field"
                            " search-input--" state
                            " search-input--" text-size
                            (when error? " search-input--error")
                            (when @is-focused? " search-input--focused"))]
      
      [:div {:class "search-input-container"}
       
       ;; Header (if enabled)
       (when show-header?
         [:div {:class "search-input-header"}
          (when show-title?
            [:label {:class "search-input-title"} 
             title
             (when requested? [:span {:class "required"} "*"])
             (when optional? [:span {:class "optional"} "(optional)"])])])
       
       ;; Input field
       [:div {:class field-classes}
        ;; Leading icon
        (when leading-icon?
          [:i {:class "far fa-search"}])
        
        ;; Text input
        [:div {:class "search-input-text"}
         [:input {:type "text"
                  :value @input-value
                  :placeholder placeholder
                  :on-change (fn [e]
                              (let [value (-> e .-target .-value)]
                                (reset! input-value value)
                                (when on-change (on-change value))))
                  :on-focus (fn [e]
                             (reset! is-focused? true)
                             (when on-focus (on-focus e)))
                  :on-blur (fn [e]
                            (reset! is-focused? false)
                            (when on-blur (on-blur e)))}]]
        
        ;; Trailing icon
        (when trailing-icon?
          [:i {:class "fas fa-times"}])]
       
       ;; Help text (if enabled)
       (when show-help-text?
         [:div {:class "search-input-help"} help-text])])))

;; Convenience function for patient search
(defn patient-search-input
  "Search input specifically for patient search in Step 3"
  [{:keys [on-search]
    :or {}}]
  [search-input {:placeholder "Search Patients by Name or Patient ID"
                 :leading-icon? true
                 :text-size "md"
                 :on-change on-search}])