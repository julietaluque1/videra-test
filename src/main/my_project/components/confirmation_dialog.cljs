
(ns my-project.components.confirmation-dialog
  (:require [reagent.core :as r]
            [my-project.components.button :as btn]))

(defn confirmation-dialog
  "Confirmation dialog for removing participants
   Props: is-visible?, on-confirm, on-cancel, title, description"
  [{:keys [is-visible? on-confirm on-cancel title description]
    :or {title "Remove participant?"
         description "If removed, this participant will no longer be part of the session. Don't worry, you can add them back at any time."}}]
  
  (when @is-visible?
    [:div {:class "confirmation-dialog-overlay"}
     [:div {:class "confirmation-dialog"}
      
      ;; Close button in top right
      [:button {:class "dialog-close-btn"
                :on-click on-cancel}
       [:i {:class "fas fa-xmark"}]]
      
      ;; Content
      [:div {:class "dialog-content"}
       
       ;; Icon and Title
       [:div {:class "dialog-header-section"}
        [:div {:class "dialog-icon"}
         [:i {:class "fas fa-triangle-exclamation"}]] ; Warning icon
        [:h3 {:class "dialog-title"} title]]
       
       ;; Description
       [:p {:class "dialog-description"} description]
       
       ;; Actions
       [:div {:class "dialog-actions"}
        [btn/button {:variant "outline"
                     :text "Cancel"
                     :on-click on-cancel}]
        [btn/button {:variant "danger"
                     :text "Remove"
                     :on-click on-confirm}]]]]]))

;; Global confirmation dialog state
(def confirmation-dialog-state (r/atom {:visible? false
                                       :title ""
                                       :description ""
                                       :on-confirm nil
                                       :on-cancel nil}))

;; Function to show confirmation dialog
(defn show-confirmation-dialog! [title description on-confirm]
  (reset! confirmation-dialog-state
          {:visible? true
           :title title
           :description description
           :on-confirm (fn []
                         (on-confirm)
                         (swap! confirmation-dialog-state assoc :visible? false))
           :on-cancel (fn []
                        (swap! confirmation-dialog-state assoc :visible? false))}))

;; Global confirmation dialog component
(defn global-confirmation-dialog []
  (let [{:keys [visible? title description on-confirm on-cancel]} @confirmation-dialog-state]
    [confirmation-dialog {:is-visible? (r/cursor confirmation-dialog-state [:visible?])
                          :title title
                          :description description
                          :on-confirm on-confirm
                          :on-cancel on-cancel}])) 