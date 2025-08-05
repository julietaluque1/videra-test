
(ns my-project.components.participant-button
  (:require [reagent.core :as r]))

(defn participant-button
  "Participant Button component matching Figma Button component set design exactly.
   Supports all button types: Add participant, Remove, Identify patient, Edit information, Move to.
   States: Default (icon only), Hover (icon + text).
   Auto-handles variant switching based on actual mouse hover."
  [{:keys [type on-click disabled? custom-text force-state]
    :or {type "add-participant"
         disabled? false}}]
  (r/with-let [;; Local state for hover - created once per component instance
               is-hovered? (r/atom false)]
    
    (let [;; Use forced state if provided (for showcase), otherwise use actual hover state
          current-state (or force-state (if @is-hovered? "hover" "default"))
          
          ;; Button type configurations from Figma
          button-config (case type
                          "add-participant" {:icon "plus"
                                            :text "Add participant"
                                            :default-variant "alternate"
                                            :hover-variant "alternate"}
                          "remove" {:icon "trash"
                                   :text "Remove"
                                   :default-variant "ghost"
                                   :hover-variant "secondary"}
                          "identify-patient" {:icon "user"
                                            :text "Identify patient"
                                            :default-variant "ghost"
                                            :hover-variant "secondary"}
                          "edit-information" {:icon "edit"
                                            :text "Edit information"
                                            :default-variant "ghost"
                                            :hover-variant "secondary"}
                          "move-to" {:icon "arrows-alt-v"
                                    :text (or custom-text "Move to")
                                    :default-variant "ghost"
                                    :hover-variant "secondary"}
                          ;; Default fallback
                          {:icon "plus"
                           :text "Add participant"
                           :default-variant "alternate"
                           :hover-variant "alternate"})
          
          ;; Determine current variant based on state
          current-variant (if (= current-state "hover")
                           (:hover-variant button-config)
                           (:default-variant button-config))
          
          ;; Should show text?
          show-text? (= current-state "hover")
          
          ;; CSS classes
          button-classes (str "participant-button"
                             " participant-button--" type
                             " participant-button--" current-variant
                             " participant-button--" current-state
                             (when disabled? " participant-button--disabled"))]
      
      [:button {:class button-classes
                :on-click (when (and on-click (not disabled?)) on-click)
                :on-mouse-enter #(when (not disabled?) (reset! is-hovered? true))
                :on-mouse-leave #(reset! is-hovered? false)
                :disabled disabled?
                :title (when (not show-text?) (:text button-config))}
       
       ;; Leading icon (always shown)
       [:i {:class (str "fa fa-" (:icon button-config))}]
       
       ;; Text (only shown on hover state)
       (when show-text?
         [:span {:class "participant-button__text"} 
          (:text button-config)])])))

;; Specific button type functions for convenience
(defn add-participant-button
  "Add participant button - primary action with blue background"
  [{:keys [on-click disabled? force-state]
    :or {disabled? false}}]
  [participant-button {:type "add-participant"
                      :on-click on-click
                      :disabled? disabled?
                      :force-state force-state}])

(defn remove-button
  "Remove button with trash icon"
  [{:keys [on-click disabled? force-state]
    :or {disabled? false}}]
  [participant-button {:type "remove"
                      :on-click on-click
                      :disabled? disabled?
                      :force-state force-state}])

(defn identify-patient-button
  "Identify patient button with user icon"
  [{:keys [on-click disabled? force-state]
    :or {disabled? false}}]
  [participant-button {:type "identify-patient"
                      :on-click on-click
                      :disabled? disabled?
                      :force-state force-state}])

(defn edit-information-button
  "Edit information button with edit icon"
  [{:keys [on-click disabled? force-state]
    :or {disabled? false}}]
  [participant-button {:type "edit-information"
                      :on-click on-click
                      :disabled? disabled?
                      :force-state force-state}])

(defn move-to-button
  "Move to button with arrows icon - supports custom text"
  [{:keys [on-click disabled? move-to-text force-state]
    :or {disabled? false}}]
  [participant-button {:type "move-to"
                      :on-click on-click
                      :disabled? disabled?
                      :custom-text move-to-text
                      :force-state force-state}])