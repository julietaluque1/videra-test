(ns my-project.components.alert
  (:require [reagent.core :as r]
            [my-project.components.button :as btn]))

(defn alert
  "Alert/Toast component following Storybook design system
   Status: Info, Success, Warning, Error
   Variant: Subtle, Solid
   Supports closable and custom placement"
  [{:keys [status variant color-mode button-placement closable? 
           show-title? show-description? title description on-close]
    :or {status "info"
         variant "subtle"
         color-mode "light"
         button-placement "below"
         closable? true
         show-title? true
         show-description? false
         title "Alert title"
         description "Alert description"}}]
  (let [;; Determine icon based on status
        icon-class (case status
                     "info" "fas fa-circle-info"
                     "success" "fas fa-circle-check"
                     "warning" "fas fa-circle-exclamation"
                     "error" "fas fa-circle-exclamation"
                     "fas fa-circle-info")
        
        ;; CSS classes for different states
        alert-classes (str "alert"
                          " alert--" status
                          " alert--" variant
                          " alert--" color-mode)]
    
    [:div {:class alert-classes}
     ;; Close button (if closable)
     (when closable?
       [:div {:class "alert-close"}
        [btn/close-button {:on-click on-close
                          :hover-background? false}]])
     
     ;; Content
     [:div {:class "alert-content"}
      [:div {:class "alert-text"}
       ;; Icon
       [:i {:class icon-class}]
       
       ;; Text content
       [:div {:class "alert-text-content"}
        (when show-title?
          [:div {:class "alert-title"} title])
        (when show-description?
          [:div {:class "alert-description"} description])]]]]))

;; Convenience functions for specific alert types
(defn info-alert
  "Info alert with blue styling"
  [{:keys [title closable? on-close]
    :or {title "You can start the session with no patients and add them later."
         closable? true}}]
  [alert {:status "info"
          :variant "subtle"
          :title title
          :closable? closable?
          :on-close on-close}])

(defn success-alert
  "Success alert with green styling"
  [{:keys [title closable? on-close]
    :or {title "You successfully added a patient"
         closable? true}}]
  [alert {:status "success"
          :variant "solid"
          :title title
          :closable? closable?
          :on-close on-close}])

(defn warning-alert
  "Warning alert with yellow styling"
  [{:keys [title closable? on-close]
    :or {title "Warning message"
         closable? true}}]
  [alert {:status "warning"
          :variant "subtle"
          :title title
          :closable? closable?
          :on-close on-close}])

(defn error-alert
  "Error alert with red styling"
  [{:keys [title closable? on-close]
    :or {title "Error message"
         closable? true}}]
  [alert {:status "error"
          :variant "subtle"
          :title title
          :closable? closable?
          :on-close on-close}])