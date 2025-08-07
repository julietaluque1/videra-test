(ns my-project.components.toast
  (:require [reagent.core :as r]))

(defn toast
  "Toast notification component that slides from top center
   Props: message, type (success/info), duration (ms), on-close"
  [{:keys [message type duration on-close]
    :or {type "success"
         duration 3000}}]
  (let [is-visible? (r/atom false)
        timeout-id (atom nil)]
    
    (r/create-class
      {:component-did-mount
       (fn [_]
         ;; Trigger visibility after mount to enable slide-in animation
         (js/setTimeout 
           (fn [] (reset! is-visible? true))
           10) ; Small delay to ensure CSS transition works
         
         ;; Set up auto-hide after duration
         (reset! timeout-id
                 (js/setTimeout 
                   (fn []
                     (reset! is-visible? false)
                     ;; Call on-close after slide-out animation
                     (js/setTimeout on-close 300))
                   duration)))
       
       :component-will-unmount
       (fn [_]
         ;; Cleanup timeout on unmount
         (when @timeout-id
           (js/clearTimeout @timeout-id)))
       
       :reagent-render
       (fn [{:keys [message type]}]
         [:div {:class (str "toast-container" (when @is-visible? " toast--visible"))}
          [:div {:class (str "toast toast--" type)}
           [:div {:class "toast-content"}
            [:i {:class (str "fas fa-" (case type
                                          "success" "circle-check"
                                          "info" "info-circle"
                                          "warning" "circle-exclamation"
                                          "error" "circle-exclamation"
                                          "circle-check"))}]
            [:span {:class "toast-message"} message]]]])})))

;; Global toast state
(def toasts (r/atom []))
(def next-id (r/atom 0))

;; Function to add a new toast
(defn add-toast! [message type]
  (let [id (swap! next-id inc)]
    (swap! toasts conj {:id id :message message :type type})
    id))

;; Function to remove a toast
(defn remove-toast! [id]
  (swap! toasts (fn [toast-list] 
                   (remove #(= (:id %) id) toast-list))))

;; Toast manager for handling multiple toasts
(defn toast-manager []
  ;; Render all toasts
  [:div {:class "toast-manager"}
   (for [toast-data @toasts]
     ^{:key (:id toast-data)}
     [toast (merge toast-data
                   {:on-close #(remove-toast! (:id toast-data))})])])

;; Convenience functions for different toast types
(defn show-success-toast! [message]
  (add-toast! message "success"))

(defn show-info-toast! [message]
  (add-toast! message "info"))

(defn show-warning-toast! [message]
  (add-toast! message "warning"))

(defn show-error-toast! [message]
  (add-toast! message "error"))