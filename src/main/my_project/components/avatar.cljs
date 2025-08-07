(ns my-project.components.avatar
  (:require [reagent.core :as r]))

;; Avatar component matching Storybook design system specifications exactly
(defn avatar
  "Avatar component matching Videra Health Storybook design system"
  [{:keys [src alt size variant]
    :or {size 32
         alt "User avatar"
         variant "default"}}]
  (let [;; Size configurations matching Storybook
        size-config (case size
                      16 {:width "16px" :height "16px" :border-radius "8px"}
                      20 {:width "20px" :height "20px" :border-radius "10px"}
                      24 {:width "24px" :height "24px" :border-radius "12px"}
                      32 {:width "32px" :height "32px" :border-radius "16px"}
                      40 {:width "40px" :height "40px" :border-radius "20px"}
                      48 {:width "48px" :height "48px" :border-radius "24px"}
                      64 {:width "64px" :height "64px" :border-radius "32px"}
                      ;; Default to 32px
                      {:width "32px" :height "32px" :border-radius "16px"})]
    
    [:div {:class (str "storybook-avatar storybook-avatar--" variant)
           :style (merge size-config
                         {:display "inline-flex"
                          :align-items "center"
                          :justify-content "center"
                          :overflow "hidden"
                          :background-color "#f8f9fa"
                          :border "1px solid rgba(0, 0, 0, 0.1)"
                          :position "relative"})}
     
     (if src
       [:img {:src src
              :alt alt
              :style {:width "100%"
                      :height "100%"
                      :object-fit "cover"
                      :display "block"}}]
       
       ;; Fallback for missing image
       [:div {:style {:width "100%"
                      :height "100%"
                      :display "flex"
                      :align-items "center"
                      :justify-content "center"
                      :background-color "#e9ecef"
                      :color "#6c757d"
                      :font-family "Inter, -apple-system, BlinkMacSystemFont, sans-serif"
                      :font-weight "500"
                      :font-size (case size
                                   16 "8px"
                                   20 "10px"
                                   24 "12px"
                                   32 "14px"
                                   40 "16px"
                                   48 "20px"
                                   64 "24px"
                                   "14px")}}
        (when alt
          (-> alt (subs 0 1) .toUpperCase))])]))