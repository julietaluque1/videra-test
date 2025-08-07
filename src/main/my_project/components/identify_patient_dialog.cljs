(ns my-project.components.identify-patient-dialog
  (:require [reagent.core :as r]
            [my-project.components.button :as btn]
            [my-project.components.search-input :as search]
            [my-project.components.patient :as patient]))

(defn identify-patient-dialog
  "Identify Patient dialog for matching incomplete patients"
  [{:keys [is-visible? on-close on-confirm incomplete-patient suggested-matches search-results]}]
  (let [selected-patient (r/atom nil)]
    (fn [{:keys [is-visible? on-close on-confirm incomplete-patient suggested-matches search-results]}]
      (when @is-visible?
        [:div {:class "identify-patient-dialog-overlay"
               :on-click (fn [e]
                           (reset! selected-patient nil)
                           (when on-close (on-close)))}
         [:div {:class "identify-patient-dialog"
                :on-click (fn [e] (.stopPropagation e))}
          
          ;; Close button in top right
          [:button {:class "dialog-close-btn"
                    :on-click (fn [e]
                                (.stopPropagation e)
                                (reset! selected-patient nil)
                                (when on-close (on-close)))}
           [:i {:class "fas fa-xmark"}]]
          
          ;; Header
          [:div {:class "dialog-header"}
           [:h2 {:class "dialog-title"} "Identify Patient"]]
          
          ;; Scrollable Content
          [:div {:class "dialog-scrollable-content"}
           
           ;; Description
           [:p {:class "dialog-description"}
            "This patient was created with limited details. Select the correct full patient below to replace the incomplete record."]
           
           ;; Incomplete patient (Variant 2: Incomplete + Added) - non-interactive
           [:div {:class "incomplete-patient-section"}
            [:div {:class "non-interactive-wrapper"}
             [patient/patient-regular 
              (merge incomplete-patient
                     {:incomplete? true
                      :added-to-session? true
                      :on-identify nil
                      :on-edit nil
                      :on-move nil
                      :on-remove nil
                      :on-click nil})]]]
           
           ;; Search section
           [:div {:class "search-section"}
            [search/patient-search-input {:placeholder "Search Patients by Name or Patient ID"
                                         :on-search (fn [query] 
                                                     ;; TODO: Implement search functionality
                                                     )}]]
           
           ;; Suggested matches (Informative patient cards with AI confidence)
           (when (not-empty suggested-matches)
             [:div {:class "suggested-matches-section"}
              [:h3 "Suggested matches"]
              [:div {:class "matches-list"}
               (for [match suggested-matches]
                 ^{:key (:patient-id match)}
                 [patient/patient-informative 
                  (merge match
                         {:match-percentage (:match-percentage match)
                          :selected? (and @selected-patient 
                                           (= (:patient-id @selected-patient) (:patient-id match))
                                           (= (:match-percentage @selected-patient) (:match-percentage match)))
                          :on-click #(if (and @selected-patient 
                                              (= (:patient-id @selected-patient) (:patient-id match))
                                              (= (:match-percentage @selected-patient) (:match-percentage match)))
                                       (reset! selected-patient nil)
                                       (reset! selected-patient match))})])]])
           
           ;; Search results
           (when (not-empty search-results)
             [:div {:class "search-results-section"}
              [:h3 "Search results"]
              [:div {:class "results-list"}
               (for [result search-results]
                 ^{:key (:patient-id result)}
                 [patient/patient-informative 
                  (merge result
                         {:selected? (and @selected-patient 
                                          (= (:patient-id @selected-patient) (:patient-id result))
                                          (= (:match-percentage @selected-patient) (:match-percentage result)))
                          :on-click #(if (and @selected-patient 
                                              (= (:patient-id @selected-patient) (:patient-id result))
                                              (= (:match-percentage @selected-patient) (:match-percentage result)))
                                       (reset! selected-patient nil)
                                       (reset! selected-patient result))})])]])]
          
          ;; Fixed bottom container with Confirm button
          [:div {:class "dialog-fixed-actions"}
           [btn/button {:variant "primary"
                        :size "sm"
                        :text "Confirm"
                        :on-click #(when @selected-patient
                                    (on-confirm @selected-patient))
                        :disabled? (nil? @selected-patient)}]]]]))))

;; Global state
(def identify-patient-dialog-state (r/atom {:visible? false
                                           :incomplete-patient nil
                                           :suggested-matches []
                                           :search-results []
                                           :on-confirm nil
                                           :on-close nil}))

;; Show dialog function
(defn show-identify-patient-dialog! [incomplete-patient suggested-matches on-confirm]
  (reset! identify-patient-dialog-state
          {:visible? true
           :incomplete-patient incomplete-patient
           :suggested-matches suggested-matches
           :search-results []
           :on-confirm (fn [selected-patient]
                         (on-confirm selected-patient)
                         (swap! identify-patient-dialog-state assoc :visible? false))
           :on-close (fn []
                       (swap! identify-patient-dialog-state assoc :visible? false))}))

;; Global dialog component
(defn global-identify-patient-dialog []
  (let [{:keys [visible? incomplete-patient suggested-matches search-results on-confirm on-close]} @identify-patient-dialog-state]
    [identify-patient-dialog {:is-visible? (r/cursor identify-patient-dialog-state [:visible?])
                             :incomplete-patient incomplete-patient
                             :suggested-matches suggested-matches
                             :search-results search-results
                             :on-confirm on-confirm
                             :on-close on-close}]))