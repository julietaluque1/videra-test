(ns my-project.app.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [my-project.components.patient :as patient]
            [my-project.components.participant-button :as pb]
            [my-project.components.patient-chip :as chip]
            [my-project.components.step2-screen :as step2]
            [my-project.components.step3-screen :as step3]
            [my-project.components.step4-screen :as step4]
            [my-project.components.alert :as alert]
            [my-project.components.button :as btn]
            [my-project.components.tabs :as tabs]
            [my-project.components.search-input :as search]
            [my-project.components.toast :as toast]
            [my-project.components.confirmation-dialog :as dialog]
            [my-project.components.identify-patient-dialog :as identify-dialog]
            [my-project.components.avatar :as avatar]))

;; State
(defonce current-match (r/atom nil))
(defonce active-component (r/atom "buttons"))

;; Pages
(defn home-page []
  [:div
   [:h1 "Hello from Reagent! 🚀"]
   [:p "This is a live-reloading ClojureScript app with React/Reagent"]
   [:div {:style {:background-color "#f0f8ff"
                  :padding "20px"
                  :border-radius "10px"
                  :margin "20px 0"}}
    [:h2 "Live editing test"]
    [:p "If you can see this styled box, live editing is working!"]]])

;; Static Toast Demo Component (shows toast permanently)
(defn static-toast-demo [type message]
  [:div {:class (str "toast-container toast--visible")}
   [:div {:class (str "toast toast--" type)}
    [:div {:class "toast-content"}
     [:i {:class (str "fas fa-" (case type
                                   "success" "circle-check"
                                   "info" "info-circle"
                                   "warning" "circle-exclamation"
                                   "error" "circle-exclamation"
                                   "circle-check"))}]
     [:span {:class "toast-message"} message]]]])

;; Static Dialog Preview Components
(defn static-confirmation-dialog-preview []
  [:div {:style {:width "542px" 
                 :max-width "100%"
                 :background "white"
                 :border-radius "6px"
                 :box-shadow "0 8px 24px rgba(0, 0, 0, 0.12)"
                 :padding "24px"
                 :position "relative"}}
   ;; Close button
   [:div {:style {:position "absolute" :top "16px" :right "16px"}}
    [btn/close-button {:size "sm"}]]
   
   ;; Content
   [:div {:style {:display "flex" 
                  :flex-direction "column" 
                  :gap "16px"
                  :align-items "center"
                  :text-align "center"}}
    
    ;; Header with icon and title
    [:div {:style {:display "flex"
                   :flex-direction "column"
                   :align-items "center"
                   :gap "12px"}}
     [:div {:style {:display "flex" :align-items "center" :justify-content "center"}}
      [:i {:class "fas fa-warning"
           :style {:color "#EE9500" :font-size "32px"}}]]
     [:h3 {:style {:font-size "20px" :font-weight "600" :margin "0"}} 
      "Remove participant?"]]
    
    ;; Description
    [:p {:style {:font-size "18px" :color "#6A708D" :margin "0" :line-height "1.5"}}
     "If removed, this participant will no longer be part of the session. Don't worry, you can add them back at any time."]
    
    ;; Actions
    [:div {:style {:display "flex" :gap "8px" :width "100%"}}
     [btn/button {:variant "outline"
                  :text "Cancel"
                  :style {:flex "1"}}]
     [btn/button {:variant "danger"
                  :text "Remove"
                  :style {:flex "1"}}]]]])

(defn static-identify-dialog-preview []
  [:div {:style {:width "600px" 
                 :max-width "100%"
                 :background "white"
                 :border-radius "6px"
                 :box-shadow "0 8px 24px rgba(0, 0, 0, 0.12)"
                 :max-height "400px"
                 :overflow "hidden"
                 :display "flex"
                 :flex-direction "column"}}
   ;; Header
   [:div {:style {:padding "16px 24px 0 24px"
                  :display "flex"
                  :flex-direction "column"
                  :align-items "center"
                  :position "relative"}}
    [:h2 {:style {:font-size "18px" :font-weight "600" :margin "32px 0 0 0"}} 
     "Identify Patient"]
    [:div {:style {:position "absolute" :right "24px" :top "16px"}}
     [btn/close-button {:size "md"}]]]
   
   ;; Scrollable content preview
   [:div {:style {:padding "0 24px 24px 24px" :overflow-y "auto"}}
    [:p {:style {:font-size "14px" :text-align "center" :margin "16px 0 24px 0"}}
     "This patient was created with limited details. Select the correct full patient below to replace the incomplete record."]
    
    ;; Incomplete patient card
    [:div {:style {:margin-bottom "24px" :opacity "0.7"}}
     [patient/patient-regular {:name "Ethan Green"
                              :patient-id "KL78MNKL78MNKL78MNKL"
                              :incomplete? true
                              :added-to-session? true
                              :avatar-url "https://avatars.githubusercontent.com/u/92997159?v=4"}]]
    
    ;; Search input
    [:div {:style {:margin-bottom "24px"}}
     [search/patient-search-input {:placeholder "Search Patients by Name or Patient ID"}]]
    
    ;; Sample match
    [:div {:style {:padding "16px" 
                   :border "1px solid #E6E8ED" 
                   :border-radius "8px"
                   :margin-bottom "16px"}}
     [:div {:style {:display "flex" :align-items "center" :justify-content "space-between"}}
      [:span {:style {:font-weight "600"}} "Ethan Green"]
      [:span {:style {:color "#0077DB" :font-size "12px"}} "95% match"]]]
    
    [:div {:style {:font-size "12px" :color "#6A708D" :text-align "center" :font-style "italic"}}
     "... more content in actual dialog"]]
   
   ;; Fixed actions
   [:div {:style {:padding "16px 24px"
                  :border-top "1px solid #E6E8ED"
                  :background "white"}}
    [btn/button {:variant "primary"
                 :text "Confirm and replace"
                 :style {:width "100%"}}]]])

;; Toast Demo with Run Demo Button
(defn toast-demo-with-button [type message]
  [:div {:style {:display "flex" :align-items "center" :gap "24px"}}
   ;; Static toast preview
   [static-toast-demo type message]
   
   ;; Run Demo button
   [:button {:class "primary-button primary-button--outline primary-button--sm"
             :on-click (case type
                         "success" #(toast/show-success-toast! message)
                         "info" #(toast/show-info-toast! message)
                         "warning" #(toast/show-warning-toast! message)
                         "error" #(toast/show-error-toast! message))}
    "Run Demo"]])

;; Patient Completed Demo Component
(defn patient-completed-demo []
  (r/with-let [;; State for the animated simulation
               simulation-state (r/atom "inactive") ; "inactive" | "active" | "completed"
               
               ;; Function to run the simulation with smooth transitions
               run-simulation (fn []
                               (reset! simulation-state "active")
                               ;; Stay in active state longer for visibility
                               (js/setTimeout 
                                #(do (reset! simulation-state "completed")
                                     ;; Stay in completed state longer (5 seconds instead of 3)
                                     (js/setTimeout 
                                      (fn [] (reset! simulation-state "inactive"))
                                      5000))
                                1500))] ; Increased from 500ms to 1500ms for better visibility
    
    [:div {:class "button-group"}
     ;; Left: Permanent preview showing how the chip looks
     [:div {:style {:display "flex" :flex-direction "column" :align-items "flex-start" :gap "8px"}}
      [:div {:style {:font-size "11px" :color "#71717a" :font-weight "500"}} "Preview:"]
      [chip/patient-chip {:status "completed" 
                         :label "Patient completed"
                         :show-completed-duration 999999}]] ; Very long duration so it doesn't disappear
     
     ;; Right: Animated simulation showing the actual behavior
     [:div {:style {:display "flex" :flex-direction "column" :align-items "flex-start" :gap "8px"}}
      [:div {:style {:display "flex" :align-items "center" :gap "8px"}}
       [:div {:style {:font-size "11px" :color "#71717a" :font-weight "500"}} "Simulation:"]
       [:button {:style {:padding "4px 8px" :font-size "11px" :border "1px solid #e4e4e7" 
                        :border-radius "4px" :background "#fff" :cursor "pointer"}
                :on-click run-simulation}
        "Run Demo"]]
      [:div {:style {:min-height "20px" :min-width "120px" :position "relative"}}
       [:div {:style {:opacity (if (= @simulation-state "inactive") "0.6" "1")
                     :transition "opacity 0.4s ease-in-out, transform 0.3s ease-in-out"
                     :transform (case @simulation-state
                                  "active" "translateY(0px)"
                                  "completed" "translateY(0px)" 
                                  "inactive" "translateY(-2px)")}}
        (case @simulation-state
          "active" [chip/incomplete-active-patient-chip {:label "Incomplete patient"}]
          "completed" [chip/completed-patient-chip {:label "Patient completed" 
                                                   :show-completed-duration 5000}]
          "inactive" [:div {:style {:font-size "11px" :color "#a1a1aa" :font-style "italic"}} 
                      "Click 'Run Demo'"])]]]]))


;; Demo wrapper for patient identification flow
(defn patient-identification-demo []
  (let [patient-state (r/atom {:incomplete? true
                               :added-to-session? true
                               :just-completed? false})]
    (fn []
      (let [state @patient-state]
        [patient/patient-regular 
         {:name "Emma Wilson"
          :patient-id "P2024-5672-LN"
          :incomplete? (:incomplete? state)
          :added-to-session? (:added-to-session? state)
          :show-completed-chip? (:just-completed? state)
        :avatar-url "https://avatars.githubusercontent.com/u/1024025?v=4&s=32"
        :on-add #()
        :on-identify #(identify-dialog/show-identify-patient-dialog! 
                       {:name "Emma Wilson"
                        :patient-id "P2024-5672-LN"
                        :room "ICU"
                        :incomplete? true
                        :added-to-session? true}
                       [{:name "Emma Wilson Smith"
                         :patient-id "98765"
                         :birth-date "01/15/1975"
                         :incomplete? false
                         :added-to-session? false
                         :match-percentage 85}
                        {:name "Emma K. Wilson"
                         :patient-id "87654"
                         :birth-date "03/22/1980"
                         :incomplete? false
                         :added-to-session? false
                         :match-percentage 75}]
                       (fn [selected] 
                         ;; Show completed chip temporarily
                         (swap! patient-state assoc :just-completed? true)
                         ;; After 3 seconds, hide the chip and mark as complete
                         (js/setTimeout 
                           (fn [] 
                             (swap! patient-state assoc 
                                     :incomplete? false
                                     :just-completed? false))
                           3000)))
        :on-edit #()
        :on-move #()
        :on-remove #(dialog/show-confirmation-dialog! 
                     "Remove participant?"
                     "If removed, this participant will no longer be part of the session. Don't worry, you can add them back at any time."
                     (fn [] nil))
        :on-click #()}]))))

;; Component sections data
(def component-sections
  {"buttons" {:title "Buttons"
              :description "Expandable buttons with icon and text. Hover to see the full button label."
              :components [{:name "Primary Button"
                           :preview [pb/add-participant-button {:on-click #()}]
                           :code "[pb/add-participant-button {:on-click #(add-participant)}]"}
                          {:name "Secondary Buttons"
                           :preview [:div {:class "button-group"}
                                    [pb/identify-patient-button {:on-click #()}]
                                    [pb/edit-information-button {:on-click #()}]
                                    [pb/move-to-button {:on-click #()}]
                                    [pb/remove-button {:on-click #()}]]
                           :code "[pb/identify-patient-button {:on-click #(identify-patient)}]\n[pb/edit-information-button {:on-click #(edit-information)}]\n[pb/move-to-button {:on-click #(move-to)}]\n[pb/remove-button {:on-click #(remove)}]"}]}
   "patient-cards" {:title "Patient Cards"
                    :description "Interactive patient card components with two main variants: Regular and Informative."
                    :variants [{:title "Regular Patient Cards"
                               :description "Standard patient cards with four scenarios based on completion status and session state"
                               :components [{:name "Variant 1: Incomplete + Not Added"
                                           :description "Shows inactive chip and Add button only"
                                           :preview [patient/patient-regular {:name "Ethan Green"
                                                                              :patient-id "P2024-7439-XK"
                                                                              :incomplete? true
                                                                              :added-to-session? false
                                                                              :avatar-url "https://avatars.githubusercontent.com/u/92997159?v=4&s=32"
                                                                              :on-add #()
                                                                              :on-identify #(identify-dialog/show-identify-patient-dialog! 
                                                                               {:name "John"
                                                                                :patient-id "45678"
                                                                                :room "ICU"
                                                                                :incomplete? true
                                                                                :added-to-session? false}
                                                                               [{:name "John Smith"
                                                                                 :patient-id "98765"
                                                                                 :birth-date "01/15/1975"
                                                                                 :incomplete? false
                                                                                 :added-to-session? false
                                                                                 :match-percentage 85}
                                                                                {:name "John Doe"
                                                                                 :patient-id "87654"
                                                                                 :birth-date "03/22/1980"
                                                                                 :incomplete? false
                                                                                 :added-to-session? false
                                                                                 :match-percentage 75}]
                                                                               (fn [selected] nil))
                                                                              :on-edit #()
                                                                              :on-move #()
                                                                              :on-remove #(dialog/show-confirmation-dialog! 
                                                                               "Remove participant?"
                                                                               "If removed, this participant will no longer be part of the session. Don't worry, you can add them back at any time."
                                                                               (fn [] nil))
                                                                              :on-click #()}]
                                           :code "[patient/patient-regular {:incomplete? true :added-to-session? false}]"}
                                          {:name "Variant 2: Incomplete + Added (Interactive Demo)"
                                           :description "Shows active chip and 4 actions. Try 'Identify patient' to see the chip change to completed!"
                                           :preview [patient-identification-demo]
                                           :code "[patient/patient-regular {:incomplete? true :added-to-session? true}]"}
                                          {:name "Variant 3: Complete + Not Added"
                                           :description "Shows no chip and Add button only"
                                           :preview [patient/patient-regular {:name "Alex Johnson"
                                                                              :patient-id "P2024-8134-QR"
                                                                              :incomplete? false
                                                                              :added-to-session? false
                                                                              :avatar-url "https://avatars.githubusercontent.com/u/583231?v=4&s=32"
                                                                              :on-add #()
                                                                              :on-identify #(identify-dialog/show-identify-patient-dialog! 
                                                                               {:name "John"
                                                                                :patient-id "45678"
                                                                                :room "ICU"
                                                                                :incomplete? true
                                                                                :added-to-session? false}
                                                                               [{:name "John Smith"
                                                                                 :patient-id "98765"
                                                                                 :birth-date "01/15/1975"
                                                                                 :incomplete? false
                                                                                 :added-to-session? false
                                                                                 :match-percentage 85}
                                                                                {:name "John Doe"
                                                                                 :patient-id "87654"
                                                                                 :birth-date "03/22/1980"
                                                                                 :incomplete? false
                                                                                 :added-to-session? false
                                                                                 :match-percentage 75}]
                                                                               (fn [selected] nil))
                                                                              :on-edit #()
                                                                              :on-move #()
                                                                              :on-remove #(dialog/show-confirmation-dialog! 
                                                                               "Remove participant?"
                                                                               "If removed, this participant will no longer be part of the session. Don't worry, you can add them back at any time."
                                                                               (fn [] nil))
                                                                              :on-click #()}]
                                           :code "[patient/patient-regular {:incomplete? false :added-to-session? false}]"}
                                          {:name "Variant 4: Complete + Added"
                                           :description "Shows no chip and 3 actions (Edit, Move, Remove - no Identify)"
                                           :preview [patient/patient-regular {:name "Sarah Martinez"
                                                                              :patient-id "P2024-9567-TU"
                                                                              :incomplete? false
                                                                              :added-to-session? true
                                                                              :avatar-url "https://avatars.githubusercontent.com/u/19864447?v=4&s=32"
                                                                              :on-add #()
                                                                              :on-identify #(identify-dialog/show-identify-patient-dialog! 
                                                                               {:name "John"
                                                                                :patient-id "45678"
                                                                                :room "ICU"
                                                                                :incomplete? true
                                                                                :added-to-session? false}
                                                                               [{:name "John Smith"
                                                                                 :patient-id "98765"
                                                                                 :birth-date "01/15/1975"
                                                                                 :incomplete? false
                                                                                 :added-to-session? false
                                                                                 :match-percentage 85}
                                                                                {:name "John Doe"
                                                                                 :patient-id "87654"
                                                                                 :birth-date "03/22/1980"
                                                                                 :incomplete? false
                                                                                 :added-to-session? false
                                                                                 :match-percentage 75}]
                                                                               (fn [selected] nil))
                                                                              :on-edit #()
                                                                              :on-move #()
                                                                              :on-remove #(dialog/show-confirmation-dialog! 
                                                                               "Remove participant?"
                                                                               "If removed, this participant will no longer be part of the session. Don't worry, you can add them back at any time."
                                                                               (fn [] nil))
                                                                              :on-click #()}]
                                           :code "[patient/patient-regular {:incomplete? false :added-to-session? true}]"}]}
                              {:title "Informative Patient Cards"
                               :description "Extended patient information with contact details and match percentage"
                               :components [{:name "Informative Card"
                                           :description "Extended layout with contact info, DOB, and AI match percentage"
                                           :preview [patient/patient-informative {:name "Sarah Johnson"
                                                                                  :patient-id "P2024-9821-MR"
                                                                                  :date-of-birth "03/15/1985"
                                                                                  :phone "+15559876543"
                                                                                  :email "s.johnson@hospital.org"
                                                                                  :match-percentage 92
                                                                                  :avatar-url "https://avatars.githubusercontent.com/u/583231?v=4&s=32"
                                                                                  :on-click #()}]
                                           :code "[patient/patient-informative {:name \"Sarah Johnson\"\n                              :date-of-birth \"03/15/1985\"\n                              :phone \"+15559876543\"\n                              :email \"s.johnson@hospital.org\"\n                              :match-percentage 92}]"}]}]}
   "patient-chips" {:title "Patient Chips"
            :description "Non-interactive status indicators for patient states. Transparent backgrounds with specific color coding."
            :components [{:name "Incomplete Patient (Default)"
                         :preview [chip/incomplete-patient-chip {:label "Incomplete patient"}]
                         :code "[chip/incomplete-patient-chip {:label \"Incomplete patient\"}]"}
                        {:name "Incomplete Patient (Active)"
                         :preview [chip/incomplete-active-patient-chip {:label "Incomplete patient"}]
                         :code "[chip/incomplete-active-patient-chip {:label \"Incomplete patient\"}]"}
                        {:name "Patient Completed (Temporary)"
                         :preview [patient-completed-demo]
                         :code "[chip/completed-patient-chip {:label \"Patient completed\"\n                              :show-completed-duration 3000}]"}]}
   "alerts" {:title "Alert Components"
            :description "Persistent messages with dismiss (X) button. Stay visible until manually closed by the user."
            :components [{:name "Info Alert"
                         :preview [alert/info-alert {:title "You can start the session with no patients and add them later."
                                                     :on-close #()}]
                         :code "[alert/info-alert {:title \"Information message\"\n                          :on-close #(handle-close)}]"}
                        {:name "Success Alert"
                         :preview [alert/success-alert {:title "Operation completed successfully"
                                                        :on-close #()}]
                         :code "[alert/success-alert {:title \"Success message\"\n                            :on-close #(handle-close)}]"}
                        {:name "Warning Alert"
                         :preview [alert/warning-alert {:title "Please review the information before proceeding"
                                                        :on-close #()}]
                         :code "[alert/warning-alert {:title \"Warning message\"\n                            :on-close #(handle-close)}]"}
                        {:name "Error Alert"
                         :preview [alert/error-alert {:title "An error occurred while processing your request"
                                                      :on-close #()}]
                         :code "[alert/error-alert {:title \"Error message\"\n                          :on-close #(handle-close)}]"}]}
   "standard-buttons" {:title "Standard Buttons"
                        :description "Button components following Storybook design system with different variants and sizes."
                        :components [{:name "Primary Button"
                           :preview [btn/next-step-button {:on-click #()}]
                                  :code "[btn/next-step-button {:on-click #(handle-next)}]"}
                                 {:name "Outline Button"
                                  :preview [btn/new-patient-button {:on-click #()}]
                                  :code "[btn/new-patient-button {:on-click #(handle-new-patient)}]"}
                                 {:name "Ghost Button"
                                  :preview [btn/submit-feedback-button {:on-click #()}]
                                  :code "[btn/submit-feedback-button {:on-click #(handle-feedback)}]"}
                                 {:name "Danger Button"
                                  :preview [btn/button {:variant "danger"
                                                        :text "Remove"
                                                        :on-click #()}]
                                  :code "[btn/button {:variant \"danger\"\n                    :text \"Remove\"\n                    :on-click #(handle-remove)}]"}
                                 {:name "Button Sizes"
                                  :preview [:div {:style {:display "flex" :gap "12px" :align-items "center"}}
                                           [btn/button {:variant "primary"
                                                       :size "xs"
                                                       :text "Extra Small"
                                                       :on-click #()}]
                                           [btn/button {:variant "primary"
                                                       :size "sm"
                                                       :text "Small"
                                                       :on-click #()}]
                                           [btn/button {:variant "primary"
                                                       :size "md"
                                                       :text "Medium"
                                                       :on-click #()}]
                                           [btn/button {:variant "primary"
                                                       :size "lg"
                                                       :text "Large"
                                                       :on-click #()}]]
                                  :code "[btn/button {:variant \"primary\"\n                    :size \"xs|sm|md|lg\"\n                    :text \"Button\"\n                    :on-click #(handle-click)}]"}
                                 {:name "Icon Buttons"
                                  :preview [:div {:style {:display "flex" :gap "24px" :align-items "center"}}
                                           [:div {:style {:display "flex" :flex-direction "column" :gap "8px"}}
                                            [:span {:style {:font-size "12px" :color "#71717a"}} "With hover background:"]
                                            [:div {:style {:display "flex" :gap "8px" :align-items "center"}}
                                             [btn/icon-button {:icon "arrow-left" :hover-background? true :on-click #()}]
                                             [btn/close-button {:hover-background? true :on-click #()}]
                                             [btn/icon-button {:icon "plus" :hover-background? true :on-click #()}]
                                             [btn/icon-button {:icon "ellipsis-h" :hover-background? true :on-click #()}]]]
                                           [:div {:style {:display "flex" :flex-direction "column" :gap "8px"}}
                                            [:span {:style {:font-size "12px" :color "#71717a"}} "Without hover background:"]
                                            [:div {:style {:display "flex" :gap "8px" :align-items "center"}}
                                             [btn/icon-button {:icon "arrow-left" :hover-background? false :on-click #()}]
                                             [btn/close-button {:hover-background? false :on-click #()}]
                                             [btn/icon-button {:icon "plus" :hover-background? false :on-click #()}]
                                             [btn/icon-button {:icon "ellipsis-h" :hover-background? false :on-click #()}]]]]
                                  :code "[btn/icon-button {:icon \"arrow-left|xmark|plus|etc\"\n                       :hover-background? true|false\n                       :on-click #(handle-click)}]"}]}
   "tabs" {:title "Tabs"
          :description "Tab/Segmented control components for navigation and option selection."
          :components [{:name "Patients/Providers Tabs"
                       :preview [tabs/patients-providers-tabs {:on-tab-change #()}]
                       :code "[tabs/patients-providers-tabs {:on-tab-change #(handle-tab-change %)}]"}]}
   "search-inputs" {:title "Search Inputs"
                   :description "Search input components with icons and placeholder text."
                   :components [{:name "Patient Search"
                                :preview [search/patient-search-input {:on-search #()}]
                                :code "[search/patient-search-input {:on-search #(handle-search %)}]"}]}
   "toasts" {:title "Toast Notifications"
            :description "Temporary feedback messages that slide in from the top. No dismiss button - they auto-hide after 3 seconds."
            :components [{:name "Success Toast"
                         :preview [toast-demo-with-button "success" "Action completed successfully!"]
                         :code "(toast/show-success-toast! \"Action completed successfully!\")"}
                        {:name "Info Toast"
                         :preview [toast-demo-with-button "info" "Information message"]
                         :code "(toast/show-info-toast! \"Information message\")"}
                        {:name "Warning Toast"
                         :preview [toast-demo-with-button "warning" "Warning message"]
                         :code "(toast/show-warning-toast! \"Warning message\")"}
                        {:name "Error Toast"
                         :preview [toast-demo-with-button "error" "Error occurred!"]
                         :code "(toast/show-error-toast! \"Error occurred!\")"}]}
   "avatars" {:title "Avatar Components"
             :description "Avatar components for displaying user profile pictures with fallback to initials."
             :components [{:name "Avatar with Image"
                          :preview [avatar/avatar {:src "https://avatars.githubusercontent.com/u/92997159?v=4"
                                                  :alt "User Avatar"
                                                  :size 32}]
                          :code "[avatar/avatar {:src \"image-url\" :alt \"User Avatar\" :size 32}]"}
                         {:name "Avatar with Initials"
                          :preview [avatar/avatar {:name "John Doe"
                                                  :size 32}]
                          :code "[avatar/avatar {:name \"John Doe\" :size 32}]"}
                         {:name "Avatar Sizes"
                          :preview [:div {:style {:display "flex" :gap "16px" :align-items "center"}}
                                   [avatar/avatar {:name "SM" :size 24}]
                                   [avatar/avatar {:name "MD" :size 32}]
                                   [avatar/avatar {:name "LG" :size 48}]
                                   [avatar/avatar {:name "XL" :size 64}]]
                          :code "[avatar/avatar {:name \"Name\" :size 24|32|48|64}]"}]}})

;; Sidebar navigation component
(defn component-sidebar []
  (let [current-active @active-component] ; Ensure reactivity by derefencing in render
    [:div {:class "component-sidebar"}
     [:div {:class "sidebar-header"}
      [:h3 "Components"]]
     [:nav {:class "sidebar-nav"}
      (for [[key section] component-sections]
        ^{:key key}
        [:button {:class (str "nav-item" (when (= current-active key) " nav-item--active"))
                  :on-click #(do 
                              (reset! active-component key))}
         (:title section)])]]))

;; Component content area
(defn component-content []
  (when-let [section (get component-sections @active-component)]
    [:div {:class "component-content"}
     [:div {:class "content-header"}
      [:h1 (:title section)]
      [:p (:description section)]]
     
     [:div {:class "component-section"}
      (if (:variants section)
        ;; Render variants (like Patient Cards with containers)
        (for [variant (:variants section)]
          ^{:key (:title variant)}
          [:div {:class "variant-container"}
           [:div {:class "variant-header"}
            [:h2 {:class "variant-title"} (:title variant)]
            [:p {:class "variant-description"} (:description variant)]]
           [:div {:class "variant-components"}
            (for [component (:components variant)]
              ^{:key (:name component)}
              [:div {:class "component-showcase"}
               [:h3 {:class "component-name"} (:name component)]
               (when (:description component)
                 [:p {:class "component-description"} (:description component)])
               [:div {:class "component-preview"}
                (:preview component)]
               [:div {:class "component-code"}
                [:pre {:class "code-block"}
                 (:code component)]]])]])
        
        ;; Render normal components (like Buttons, Chips)
        (for [component (:components section)]
          ^{:key (:name component)}
          [:div {:class "component-showcase"}
           [:h3 {:class "component-name"} (:name component)]
           (when (:description component)
             [:p {:class "component-description"} (:description component)])
           [:div {:class "component-preview"}
            (:preview component)]
           [:div {:class "component-code"}
            [:pre {:class "code-block"}
             (:code component)]]]))]]))

(defn component-library-page []
  [:div 
   ;; Toast Manager (needed for toast demos)
   [toast/toast-manager]
   
   ;; Confirmation Dialog (needed for dialog demos)
   [dialog/global-confirmation-dialog]
   
   ;; Identify Patient Dialog (needed for dialog demos)
   [identify-dialog/global-identify-patient-dialog]
   
   [:div {:class "component-library"}
    [component-sidebar]
    [component-content]]])

;; Routes
(def routes
  [["/" {:name :home
         :view step2/step2-screen}]
   ["/step3" {:name :step3
              :view step3/step3-screen}]
   ["/step4" {:name :step4
              :view step4/step4-screen}]
   ["/component-library" {:name :component-library
                          :view component-library-page}]])

;; Current page view
(defn current-page []
  (when-let [match @current-match]
    (let [view (:view (:data match))]
      [view])))

;; Main app component
(defn app []
  [current-page])

;; Initialize router
(defn init-router! []
  (rfe/start!
    (rf/router routes)
    (fn [m] (reset! current-match m))
    {:use-fragment true}))

;; Render function
(defn render []
  (rdom/render [app] (.getElementById js/document "root")))

;; Main entry point
(defn ^:export main []
  (init-router!)
  (render))

;; Hot reload hook
(defn ^:dev/after-load reload! []
  (render))
