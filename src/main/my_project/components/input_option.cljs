(ns my-project.components.input-option
  (:require [reagent.core :as r]))

(defn input-option [{:keys [title description icon icon-svg selected? on-click]}]
  [:div {:class (str "input-option" 
                     (when selected? " input-option--selected"))
         :on-click on-click}
   
   ;; Centered inner container (144x144px)
   [:div {:class "input-option-icon-container"}
    (if icon-svg
      ;; Custom SVG icon with wrapper for consistent sizing
      [:div {:class "input-option-svg-wrapper"}
       [:object {:data icon-svg
                 :type "image/svg+xml"
                 :class "input-option-svg-icon"
                 :aria-label title}
        ;; Fallback content if object fails to load
        [:img {:src icon-svg
               :alt title
               :class "input-option-svg-icon"}]]]
      ;; Font Awesome icon (fallback)
      [:i {:class (str "fas fa-" icon)}])]
   
   ;; Content below icon (8px gap)
   [:div {:class "input-option-content"}
    [:h3 {:class "input-option-title"} title]
    [:p {:class "input-option-description"} description]]]) 