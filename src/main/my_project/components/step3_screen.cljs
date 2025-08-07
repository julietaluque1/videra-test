(ns my-project.components.step3-screen
  (:require [reagent.core :as r]
            [my-project.components.button :as btn]
            [my-project.components.toast :as toast]
            [my-project.components.input-option :as input-option]
            [reitit.frontend.easy :as rfe]))

;; Main Step 3 screen component
(defn step3-screen []
  (r/with-let [selected-option (r/atom "listen") ; Default to listen option
                options (r/atom [{:id "listen"
                                  :title "Listen"
                                  :description "Capture the session live, as it happens."
                                  :icon-svg "/custom-icons/livesession-md.svg"
                                  :selected? true}
                                 {:id "upload"
                                  :title "Upload"
                                  :description "Already recorded? Upload your session file."
                                  :icon-svg "/custom-icons/upload-md.svg"
                                  :selected? false}
                                 {:id "write"
                                  :title "Write"
                                  :description "Type in session details manually."
                                  :icon-svg "/custom-icons/write-md.svg"
                                  :selected? false}
                                 {:id "dictate"
                                  :title "Dictate"
                                  :description "Dictate notes to capture insights."
                                  :icon-svg "/custom-icons/save-md.svg"
                                  :selected? false}])]
    
    [:div {:class "step3-screen"}
     
     ;; Toast Manager (fixed at top center)
     [toast/toast-manager]
     
     ;; Nav Header
     [:div {:class "nav-header"}
      [:div {:class "nav-left"}
       [btn/icon-button {:icon "arrow-left"
                        :hover-background? true
                        :on-click #(rfe/push-state :home)}]]
      
      [:div {:class "nav-center"}
       [:h1 "Step 3 of 4"]]
      
      [:div {:class "nav-right"}
       [btn/close-button {:class "nav-close-btn"}]]]
     
     ;; Main Content - Single Column Centered Layout
     [:div {:class "main-content"}
      
      ;; Header Section
      [:div {:class "header-section"}
       [:h2 {:class "screen-title"} "Choose Input"]
       [:p {:class "screen-description"}
        "Select how you would like to input patient information for this session."]]
      
      ;; Options Grid - Centered
             [:div {:class "options-container"}
        [:div {:class "options-grid"}
         (for [option @options]
           ^{:key (:id option)}
           [input-option/input-option 
            {:title (:title option)
             :description (:description option)
             :icon-svg (:icon-svg option)
             :selected? (= @selected-option (:id option))
             :on-click #(reset! selected-option (:id option))}])]]
      
             ;; Single Next Button - Centered
       [:div {:class "next-button-container"}
        [btn/next-step-button {:on-click #(if @selected-option
                                           (rfe/push-state :step4)
                                           (toast/show-warning-toast! "Please select an input method"))
                              :disabled? (nil? @selected-option)
                              :style {:width "321px"}}]]]])) 