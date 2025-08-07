(ns my-project.components.patient
  (:require [reagent.core :as r]
            [my-project.components.participant-button :as btn]
            [my-project.components.patient-chip :as chip]
            [my-project.components.avatar :as avatar]))

;; Patient Card Component (following Figma design exactly)
(defn patient-card
  "Patient card component matching Figma specifications exactly"
  [{:keys [name patient-id avatar-url 
           incomplete? is-speaker? show-patient-chip?
           added-to-session? is-provider? move-to-text ; New props for provider state and dynamic text
           on-add on-identify on-edit on-move on-remove
           on-click show-completed-chip?]
    :or {name "Ethan Green"
         patient-id "P2024-7439-XK"
         incomplete? true
         is-speaker? false
         show-patient-chip? true
         added-to-session? false ; Default: not added to session
         is-provider? false ; Default: not a provider
         move-to-text "Move to contributors" ; Default move-to text
         avatar-url "https://avatars.githubusercontent.com/u/92997159?v=4&s=32"}}]
  (r/with-let [;; Hover state for showing action buttons
               is-hovered? (r/atom false)]
    
    [:div {:class "patient-card"
           :on-click on-click
           :on-mouse-enter #(reset! is-hovered? true)
           :on-mouse-leave #(reset! is-hovered? false)}
     
     ;; Left side: Patient Info (PFP + Name & ID + Chip)
     [:div {:class "patient-info"}
      
      ;; Profile Picture (32x32 with storybook avatar)
      [:div {:class "patient-pfp"}
       [avatar/avatar {:src avatar-url
                       :alt (str name "'s profile")
                       :size 32}]]
      
      ;; Name & ID section
      [:div {:class "patient-name-id"}
       
       ;; Name row with patient chip
       [:div {:class "patient-name-row"}
        [:h3 {:class "patient-name"} name]
        
        ;; Patient chip wrapper - maintains space even when empty
        [:div {:class (str "patient-chip-wrapper" 
                          (when (:chip-fading? props) " chip-fading"))}
         (cond
           ;; Show completed chip if specified
           show-completed-chip?
           [chip/completed-patient-chip {:label "Patient completed"}]
           
           ;; Show incomplete chips
           incomplete?
           (if added-to-session?
             ;; Show active chip when added to session and incomplete
             [chip/incomplete-active-patient-chip {:label "Incomplete patient"}]
             ;; Show inactive chip when not added to session and incomplete
             [chip/incomplete-patient-chip {:label "Incomplete patient"}]))]]
       
       ;; ID row
       [:p {:class "patient-id"} (str "ID: " patient-id)]]]
     
     ;; Right side: Action buttons (Add + Edit actions)
     [:div {:class "patient-actions"}
      
      (when @is-hovered?
        (if added-to-session?
          ;; Added to session - show secondary actions based on completion status
          [:div {:class "patient-edit-section"}
           ;; Identify only shown for incomplete patients
           (when incomplete?
             [btn/identify-patient-button {:on-click on-identify}])
           [btn/edit-information-button {:on-click on-edit}]
           ;; Move to button - hidden for providers
           (when-not is-provider?
             [btn/move-to-button {:on-click on-move
                                 :move-to-text move-to-text}])
           [btn/remove-button {:on-click on-remove}]]
          
          ;; Not added to session - show only add button
          [:div {:class "patient-add-section"}
           [btn/add-participant-button {:on-click on-add}]]))]]))

;; Contact info component for informative cards
(defn patient-contact-info
  "Renders contact information with icons"
  [{:keys [icon text]}]
  [:div {:class "patient-contact-item"}
   [:i {:class (str "fas fa-" icon)}]
   [:span text]])

;; Informative patient card with extended details
(defn patient-informative-card
  "Informative patient card with extended contact details and match percentage"
  [{:keys [name patient-id avatar-url match-percentage
           date-of-birth phone email
           on-click selected?]
    :or {name "Sarah Johnson"
         patient-id "P2024-9821-MR"
         match-percentage 92
         avatar-url "https://avatars.githubusercontent.com/u/583231?v=4&s=32"}}]
  [:div {:class (str "patient-card patient-card--informative"
                     (when selected? " selected"))
         :on-click on-click}
   
   ;; Left side: Patient Info
   [:div {:class "patient-info"}
    
    ;; Profile Picture
    [:div {:class "patient-pfp"}
     [avatar/avatar {:src avatar-url
                     :alt (str name "'s profile")
                     :size 32}]]
    
    ;; Extended info section - all aligned left
    [:div {:class "patient-extended-info"}
     
     ;; Name and ID row (ID to the right of name)
     [:div {:class "patient-name-id-row"}
      [:h3 {:class "patient-name"} name]
      [:p {:class "patient-id"} (str "ID: " patient-id)]]
     
     ;; Contact information and DOB
     [:div {:class "patient-contact-info"}
      (when date-of-birth
        [patient-contact-info {:icon "calendar-alt" :text date-of-birth}])
      (when phone
        [patient-contact-info {:icon "phone" :text phone}])
      (when email
        [patient-contact-info {:icon "envelope" :text email}])]]]
   
   ;; Right side: Match percentage chip only
   (when match-percentage
     [:div {:class "patient-match-section"}
      [chip/match-percentage-chip match-percentage]])])

;; Export functions for convenience
(defn patient-regular
  "Regular patient card variant (default behavior)"
  [props]
  [patient-card props])

(defn patient-informative
  "Informative patient card variant with extended details"
  [props]
  [patient-informative-card props])