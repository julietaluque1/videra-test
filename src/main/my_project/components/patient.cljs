(ns my-project.components.patient
  (:require [reagent.core :as r]
            [my-project.components.participant-button :as btn]
            [my-project.components.patient-chip :as chip]))

;; Patient Component
(defn patient-contact-info
  "Renders contact information with icons"
  [{:keys [icon text]}]
  [:div {:class "patient-contact-item"}
   [:i {:class (str "fa fa-" icon)}]
   [:span text]])

(defn patient-component
  "Main Patient component with configurable props"
  [{:keys [type state name patient-id avatar-url 
           incomplete? is-speaker? match-percentage
           date-of-birth phone email
           on-add on-identify on-edit on-move on-remove
           on-click]
    :or {type "regular"
         state "default"
         incomplete? false
         is-speaker? false}}]
  (let [is-hover? (= state "hover")
        is-selected? (= state "selected")
        is-informative? (= type "informative")
        show-actions? (or is-hover? is-selected?)
        container-classes (str "patient-card"
                              (when is-hover? " patient-card--hover")
                              (when is-selected? " patient-card--selected")
                              (when is-informative? " patient-card--informative"))]
    [:div {:class container-classes
           :on-click on-click}
     
     ;; Main patient info section
     [:div {:class "patient-info"}
      
      ;; Profile picture
      [:div {:class "patient-pfp"}
       (if avatar-url
         [:img {:src avatar-url
                :alt (str name "'s profile")
                :class "patient-pfp__image"}]
         [:div {:class "patient-pfp__placeholder"}])]
      
      ;; Name and ID section
      [:div {:class "patient-details"}
       (if is-informative?
         ;; Informative layout
         [:div {:class "patient-extended-info"}
          [:div {:class "patient-name-id"}
           [:h3 {:class "patient-name"} name]
           [:p {:class "patient-id"} (str "ID: " patient-id)]]
          
          [:div {:class "patient-contact-info"}
           (when date-of-birth
             [patient-contact-info {:icon "calendar-alt" :text date-of-birth}])
           (when phone
             [patient-contact-info {:icon "phone" :text phone}])
           (when email
             [patient-contact-info {:icon "envelope" :text email}])]]
         
         ;; Regular layout
         [:div
          [:div {:class "patient-name-section"}
           [:h3 {:class "patient-name"} name]
           
           ;; Status chips
           [:div {:class "patient-chips"}
            (when incomplete?
              [chip/incomplete-patient-chip {:state (if (= state "hover") "active" "default")}])
            (when is-speaker?
              [chip/speaker-chip {:state (if (= state "hover") "active" "default")}])]]
          
          ;; Patient ID
          (when patient-id
            [:p {:class "patient-id"} (str "ID: " patient-id)])])]]
     
     ;; Right side - actions and match percentage
     [:div {:class "patient-actions-section"}
      
      ;; Match percentage (for informative type)
      (when (and is-informative? match-percentage)
        [:div {:class "patient-match"}
         [chip/match-percentage-chip {:percentage match-percentage
                                     :state (if (= state "hover") "active" "default")}]
         [:button {:class "patient-expand-btn"}
          [:i {:class "fa fa-chevron-down"}]]])
      
      ;; Action buttons (show on hover/selected for regular type)
      (when (and (= type "regular") show-actions?)
        [:div {:class "patient-actions"}
         [:div {:class "patient-add-section"}
          [btn/add-participant-button {:state state
                                      :on-click on-add}]]
         
         [:div {:class "patient-edit-section"}
          [btn/identify-patient-button {:state state
                                       :on-click on-identify}]
          [btn/edit-information-button {:state state
                                       :on-click on-edit}]
          [btn/move-to-button {:state state
                              :on-click on-move}]
          [btn/remove-button {:state state
                             :on-click on-remove}]]])]]))

;; Export with different presets
(defn patient-regular
  "Regular patient card variant"
  [props]
  [patient-component (assoc props :type "regular")])

(defn patient-informative
  "Informative patient card variant with extended details"
  [props]
  [patient-component (assoc props :type "informative")])