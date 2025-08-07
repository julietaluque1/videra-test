(ns my-project.components.button
  (:require [reagent.core :as r]))

(defn button
  "Button component following Storybook design system
   Variant: Primary, Secondary, Outline, Ghost, White, Alternate, Danger, Icon, Icon-No-Hover
   Size: xs, sm, md, lg
   State: Default, Hover, Active, Disabled"
  [{:keys [variant size state text leading-icon? trailing-icon? 
           leading-icon trailing-icon disabled? on-click icon-only? icon style class]
    :or {variant "primary"
         size "md"
         state "default"
         text "Button"
         leading-icon? false
         trailing-icon? false
         leading-icon "plus"
         trailing-icon "arrow-right"
         disabled? false
         icon-only? false}}]
  (let [;; Check if this is an icon button variant
        is-icon-button? (or (= variant "icon") (= variant "icon-no-hover") icon-only?)
        ;; CSS classes for different states
        button-classes (str "primary-button"
                           " primary-button--" variant
                           " primary-button--" size
                           " primary-button--" state
                           (when disabled? " primary-button--disabled")
                           (when is-icon-button? " primary-button--icon-only")
                           (when class (str " " class)))]
    
    [:button {:class button-classes
              :disabled disabled?
              :on-click (when (and on-click (not disabled?)) on-click)
              :style style}
     
     (if is-icon-button?
       ;; Icon-only button
       [:i {:class (str "fas fa-" (or icon "xmark"))}]
       ;; Regular button with optional icons and text
       [:<>
        ;; Leading icon
        (when leading-icon?
          [:i {:class (str "fas fa-" leading-icon)}])
        
        ;; Button text
        [:span {:class "primary-button-text"} text]
        
        ;; Trailing icon
        (when trailing-icon?
          [:i {:class (str "fas fa-" trailing-icon)}])])]))

;; Convenience functions for specific button types
(defn next-step-button
  "Next step button (primary variant)"
  [{:keys [on-click disabled? style]
    :or {disabled? false}}]
  [button {:variant "primary"
           :size "md"
           :text "Next step"
           :on-click on-click
           :disabled? disabled?
           :style style}])

(defn new-patient-button
  "New patient button (outline variant with plus icon)"
  [{:keys [on-click disabled?]
    :or {disabled? false}}]
  [button {:variant "outline"
           :size "md"
           :text "New patient"
           :leading-icon? true
           :leading-icon "plus"
           :on-click on-click
           :disabled? disabled?}])

(defn submit-feedback-button
  "Submit feedback button (ghost variant)"
  [{:keys [on-click disabled?]
    :or {disabled? false}}]
  [button {:variant "ghost"
           :size "md"
           :text "Submit feedback"
           :on-click on-click
           :disabled? disabled?}])

(defn close-button
  "Close button (X) for dialogs, alerts, and other closable components
   hover-background?: true for hover background, false for no hover effect"
  [{:keys [on-click class hover-background?]
    :or {hover-background? true}}]
  [button {:variant (if hover-background? "icon" "icon-no-hover")
           :size "sm"
           :icon "xmark"
           :icon-only? true
           :on-click on-click
           :class class}])

(defn icon-button
  "Generic icon button for navigation and actions
   icon: Font Awesome icon name (e.g. 'arrow-left', 'xmark', 'plus')
   hover-background?: true for hover background, false for no hover effect"
  [{:keys [icon on-click class hover-background?]
    :or {hover-background? true
         icon "xmark"}}]
  [button {:variant (if hover-background? "icon" "icon-no-hover")
           :size "sm"
           :icon icon
           :icon-only? true
           :on-click on-click
           :class class}])