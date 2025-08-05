(ns my-project.components.patient-chip
  (:require [reagent.core :as r]))

(defn patient-chip
  "Patient chip component with three visual states: incomplete, incomplete-active, completed.
   All states are non-interactive and purely informative."
  [{:keys [status label show-completed-duration]
    :or {status "incomplete"
         label "Incomplete patient"
         show-completed-duration 3000}}]
  (r/with-let [;; Local state for managing temporary completed visibility
               visible? (r/atom true)
               
               ;; Set up timer for completed state auto-hide
               _ (when (= status "completed")
                   (js/setTimeout 
                     #(reset! visible? false) 
                     show-completed-duration))]
    
    (when @visible?
      (let [;; Determine icon based on status  
            chip-icon (case status
                        "incomplete" "exclamation-triangle"
                        "incomplete-active" "exclamation-triangle" 
                        "completed" "check"
                        "exclamation-triangle")
            
            ;; CSS classes for different states
            chip-classes (str "patient-chip"
                             " patient-chip--" status)]
        
        [:div {:class chip-classes}
         [:i {:class (str "fa fa-" chip-icon)}]
         [:span label]]))))

;; Convenience functions for specific chip types
(defn incomplete-patient-chip
  "Incomplete patient chip (default state)"
  [{:keys [label show-completed-duration]
    :or {label "Incomplete patient"}}]
  [patient-chip {:status "incomplete"
                 :label label
                 :show-completed-duration show-completed-duration}])

(defn incomplete-active-patient-chip  
  "Incomplete patient chip (active state - in current session)"
  [{:keys [label show-completed-duration]
    :or {label "Incomplete patient"}}]
  [patient-chip {:status "incomplete-active"
                 :label label
                 :show-completed-duration show-completed-duration}])

(defn completed-patient-chip
  "Patient completed chip (temporary state - auto-hides after duration)"
  [{:keys [label show-completed-duration]
    :or {label "Patient completed"
         show-completed-duration 3000}}]
  [patient-chip {:status "completed"
                 :label label
                 :show-completed-duration show-completed-duration}])

;; Legacy compatibility functions (keeping for existing usage)
(defn speaker-chip
  "Speaker role chip"
  [{:keys [state]
    :or {state "default"}}]
  [:div {:class (str "patient-status-chip patient-status-chip--speaker"
                     (when (= state "active") " patient-status-chip--active"))}
   [:span "Speaker"]])

(defn match-percentage-chip
  "Match percentage chip for informative patient cards"
  [{:keys [percentage state]
    :or {state "default"}}]
  [:div {:class (str "patient-status-chip patient-status-chip--match"
                     (when (= state "active") " patient-status-chip--active"))}
   [:i {:class "fa fa-sparkles"}]
   [:span (str percentage "%")]])