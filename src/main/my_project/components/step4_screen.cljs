(ns my-project.components.step4-screen
  (:require [reagent.core :as r]
            [my-project.components.button :as btn]
            [my-project.components.toast :as toast]
            [reitit.frontend.easy :as rfe]))

;; Microphone selection component
(defn microphone-selector []
  [:div {:class "microphone-selector"}
   [:div {:class "microphone-header"}
    [:h3 "Connect your microphone to record"]]
   
   [:div {:class "microphone-container"}
    [:div {:class "microphone-icon"}
     [:i {:class "fas fa-microphone"}]
     [:span "Default - Source of microphone"]]
    
    [:div {:class "audio-indicator"}
     [:div {:class "audio-bar"}]
     [:div {:class "audio-bar"}]
     [:div {:class "audio-bar"}]]
    
    [:i {:class "fas fa-angle-down"}]]])

;; Text area component
(defn text-area [{:keys [title placeholder value on-change]}]
  [:div {:class "text-area-container"}
   [:div {:class "text-area-header"}
    [:h3 title]
    [:span {:class "optional-label"} "Optional"]]
   
   [:div {:class "text-area-field"}
    [:textarea {:placeholder placeholder
                :value value
                :on-change on-change}]]])

;; Main Step 4 screen component
(defn step4-screen []
  (r/with-let [selected-input (r/atom "listen") ; This would come from Step 3
                extra-context (r/atom "")]
    
    [:div {:class "step4-screen"}
     
     ;; Toast Manager (fixed at top center)
     [toast/toast-manager]
     
     ;; Nav Header
     [:div {:class "nav-header"}
      [:div {:class "nav-left"}
       [btn/icon-button {:icon "arrow-left"
                        :hover-background? true
                        :on-click #(rfe/push-state :step3)}]]
      
      [:div {:class "nav-center"}
       [:h1 "Step 4 of 4"]]
      
      [:div {:class "nav-right"}
       [btn/close-button {:class "nav-close-btn"}]]]
     
     ;; Main Content - Different layouts based on input choice
     [:div {:class "main-content"}
      
      ;; Listen Layout
      (when (= @selected-input "listen")
        [:div {:class "listen-layout"}
         
         ;; Header Section
         [:div {:class "header-section"}
          [:h2 {:class "screen-title"} "Get ready to record your session"]
          [:p {:class "screen-description"}
           "Select your microphone. Once you're set, you can start recording."]]
         
         ;; Body Section
         [:div {:class "body-section"}
          
          ;; Microphone Selector
          [microphone-selector]
          
          ;; Text Area
          [text-area {:title "Is there anything else we should know about the patient?"
                      :placeholder "Add any extra context or hints"
                      :value @extra-context
                      :on-change #(reset! extra-context (.. % -target -value))}]]
         
         ;; Actions Section
         [:div {:class "actions-section"}
          [:button {:class "secondary-button"
                    :on-click #(js/alert "Skip this step")}
           "Skip this"]
          
          [btn/button {:variant "primary"
                       :text "Start Session"
                       :on-click #(js/alert "Starting session...")}]]])
      
      ;; Upload Layout (placeholder)
      (when (= @selected-input "upload")
        [:div {:class "upload-layout"}
         [:h2 "Upload Layout - Coming Soon"]])
      
      ;; Write Layout (placeholder)
      (when (= @selected-input "write")
        [:div {:class "write-layout"}
         [:h2 "Write Layout - Coming Soon"]])
      
      ;; Dictate Layout (placeholder)
      (when (= @selected-input "dictate")
        [:div {:class "dictate-layout"}
         [:h2 "Dictate Layout - Coming Soon"]])]])) 