(ns my-project.components.step2-screen
  (:require [reagent.core :as r]
            [my-project.components.patient :as patient]
            [my-project.components.participant-button :as pb]
            [my-project.components.alert :as alert]
            [my-project.components.button :as btn]
            [my-project.components.tabs :as tabs]
            [my-project.components.search-input :as search]
            [my-project.components.toast :as toast]
            [my-project.components.confirmation-dialog :as dialog]
            [my-project.components.identify-patient-dialog :as identify-dialog]
            [reitit.frontend.easy :as rfe]))

;; Main Step 2 screen component
(defn step2-screen []
  (r/with-let [;; Available patients list (left panel)
               available-patients (r/atom 
                                   [{:id "mason-king" :name "Mason King" :patient-id "AB12CDAB12CDAB12CDAB" 
                                     :incomplete? true :avatar-url "https://avatars.githubusercontent.com/u/1024025?v=4&s=32"}
                                    {:id "olivia-lee" :name "Olivia Lee" :patient-id "AB12CDAB12CDAB12CDAB" 
                                     :incomplete? false :avatar-url "https://avatars.githubusercontent.com/u/583231?v=4&s=32"}
                                    {:id "ally-carter" :name "Ally Carter" :patient-id "AB12CDAB12CDAB12CDAB" 
                                     :incomplete? false :avatar-url "https://avatars.githubusercontent.com/u/19864447?v=4&s=32"}
                                    {:id "isabella-johnson" :name "Isabella Johnson" :patient-id "AB12CDAB12CDAB12CDAB" 
                                     :incomplete? false :avatar-url "https://avatars.githubusercontent.com/u/92997159?v=4&s=32"}
                                    {:id "sophia-miller" :name "Sophia Miller" :patient-id "AB12CDAB12CDAB12CDAB" 
                                     :incomplete? false :avatar-url "https://avatars.githubusercontent.com/u/1024025?v=4&s=32"}
                                    {:id "ethan-green" :name "Ethan Green" :patient-id "KL78MNKL78MNKL78MNKL" 
                                     :incomplete? true :avatar-url "https://avatars.githubusercontent.com/u/583231?v=4&s=32"}
                                    {:id "zoe-carter" :name "Zoe Carter" :patient-id "KL78MNKL78MNKL78MNKL" 
                                     :incomplete? false :avatar-url "https://avatars.githubusercontent.com/u/19864447?v=4&s=32"}
                                    {:id "grace-hall" :name "Grace Hall" :patient-id "KL78MNKL78MNKL78MNKL" 
                                     :incomplete? false :avatar-url "https://avatars.githubusercontent.com/u/92997159?v=4&s=32"}
                                    {:id "ella-loewe" :name "Ella Loewe" :patient-id "KL78MNKL78MNKL78MNKL" 
                                     :incomplete? true :avatar-url "https://avatars.githubusercontent.com/u/1024025?v=4&s=32"}
                                    {:id "elvis-thorn" :name "Elvis Thorn" :patient-id "KL78MNKL78MNKL78MNKL" 
                                     :incomplete? true :avatar-url "https://avatars.githubusercontent.com/u/583231?v=4&s=32"}])
               
               ;; Added patients organized by section
               subjects (r/atom []) ; Patients added as subjects
               contributors (r/atom []) ; Patients moved to contributors
               providers (r/atom [{:id "clara-everhart" :name "Clara Everhart (me)" 
                                   :patient-id "AB12CDAB12CDAB12CDAB" :incomplete? false 
                                   :avatar-url "https://avatars.githubusercontent.com/u/92997159?v=4&s=32"}])
               
               ;; Helper functions
               get-added-patient-ids (fn [] 
                                       (set (concat (map :id @subjects) 
                                                   (map :id @contributors))))
               
               is-patient-added? (fn [patient-id] 
                                   (contains? (get-added-patient-ids) patient-id))
               
               is-patient-in-contributors? (fn [patient-id]
                                             (some #(= (:id %) patient-id) @contributors))
               
               total-patients-count (fn [] (+ (count @subjects) (count @contributors)))
               
               ;; Action handlers with toast notifications
               add-patient-as-subject (fn [patient]
                                        (swap! subjects conj patient)
                                        (toast/show-success-toast! "Patient added as subject"))
               
               move-to-contributors (fn [patient-id]
                                      (when-let [patient (first (filter #(= (:id %) patient-id) @subjects))]
                                        (swap! subjects (fn [subj] (remove #(= (:id %) patient-id) subj)))
                                        (swap! contributors conj patient)
                                        (toast/show-success-toast! "Patient is now a contributor")))
               
               move-to-subjects (fn [patient-id]
                                  (when-let [patient (first (filter #(= (:id %) patient-id) @contributors))]
                                    (swap! contributors (fn [contrib] (remove #(= (:id %) patient-id) contrib)))
                                    (swap! subjects conj patient)
                                    (toast/show-success-toast! "Patient is now a subject")))
               
                               remove-patient (fn [patient-id]
                                (let [patient (or (first (filter #(= (:id %) patient-id) @subjects))
                                                 (first (filter #(= (:id %) patient-id) @contributors)))]
                                 (when patient
                                   (dialog/show-confirmation-dialog! 
                                    "Remove participant?"
                                    "If removed, this participant will no longer be part of the session. Don't worry, you can add them back at any time."
                                    (fn []
                                      (swap! subjects (fn [subj] (remove #(= (:id %) patient-id) subj)))
                                      (swap! contributors (fn [contrib] (remove #(= (:id %) patient-id) contrib)))
                                      (toast/show-success-toast! "Patient removed from session"))))))
               
               identify-patient (fn [patient]
                                 ;; Show identify patient dialog with dummy suggested matches
                                 (identify-dialog/show-identify-patient-dialog! 
                                  patient
                                  [{:name "Ethan Green"
                                    :id "KL78MNKL78MNKL78MNKL"
                                    :avatar "https://avatars.githubusercontent.com/u/92997159?v=4"
                                    :dob "10/05/1990"
                                    :phone "+15551234567"
                                    :email "ethangreen@gmail.com"
                                    :match-percentage 95}
                                   {:name "Ethan Green"
                                    :id "KL78MNKL78MNKL78MNKL"
                                    :avatar "https://avatars.githubusercontent.com/u/92997159?v=4"
                                    :dob "10/05/1990"
                                    :phone "+15551234567"
                                    :email "ethangreen@gmail.com"
                                    :match-percentage 92}]
                                  (fn [selected-patient]
                                    ;; Update the patient's incomplete status
                                    (let [patient-id (:id patient)
                                          update-patient (fn [p]
                                                          (if (= (:id p) patient-id)
                                                            (-> p
                                                                (assoc :incomplete? false)
                                                                (assoc :just-completed? true))
                                                            p))]
                                      ;; Update in available patients (source list)
                                      (swap! available-patients #(mapv update-patient %))
                                      ;; Update in subjects
                                      (swap! subjects #(mapv update-patient %))
                                      ;; Update in contributors  
                                      (swap! contributors #(mapv update-patient %))
                                      ;; Show success toast
                                      (toast/show-success-toast! "Patient identified successfully!")
                                      ;; Start fade out after 2.2 seconds
                                      (js/setTimeout 
                                       (fn []
                                         (let [add-fading (fn [p]
                                                           (if (= (:id p) patient-id)
                                                             (assoc p :chip-fading? true)
                                                             p))]
                                           (swap! available-patients #(mapv add-fading %))
                                           (swap! subjects #(mapv add-fading %))
                                           (swap! contributors #(mapv add-fading %))))
                                       2200)
                                      ;; Remove the just-completed flag after full delay
                                      (js/setTimeout 
                                       (fn []
                                         (let [remove-completed (fn [p]
                                                                 (if (= (:id p) patient-id)
                                                                   (-> p
                                                                       (dissoc :just-completed?)
                                                                       (dissoc :chip-fading?))
                                                                   p))]
                                           (swap! available-patients #(mapv remove-completed %))
                                           (swap! subjects #(mapv remove-completed %))
                                           (swap! contributors #(mapv remove-completed %))))
                                       2800)))))]
    
    [:div {:class "step2-screen"}
     
     ;; Toast Manager (fixed at top center)
     [toast/toast-manager]
     
     ;; Confirmation Dialog (fixed overlay)
     [dialog/global-confirmation-dialog]
     
     ;; Identify Patient Dialog (fixed overlay)
     [identify-dialog/global-identify-patient-dialog]
     
     ;; Nav Header
     [:div {:class "nav-header"}
      [:div {:class "nav-left"}
       [btn/icon-button {:icon "arrow-left"
                        :hover-background? true
                        :on-click #(js/console.log "Go back")}]]
      
      [:div {:class "nav-center"}
       [:h1 "Step 2 of 4"]]
      
      [:div {:class "nav-right"}
       [:div {:class "saved-indicator"}
        [:i {:class "fas fa-file-check"}]
        [:span "Note draft saved"]]
       [btn/close-button {:class "nav-close-btn"}]]]
     
     ;; Main Content
     [:div {:class "main-content"}
      
      ;; Left Panel (Patients List)
      [:div {:class "left-panel"}
       [:div {:class "left-content"}
        ;; Tabs (Patients/Providers)
        [tabs/patients-providers-tabs {:on-tab-change #(js/console.log "Tab changed to:" %)}]
        
        ;; Search and New Patient Button
        [:div {:class "navigation-section"}
         [search/patient-search-input {:on-search #(js/console.log "Search:" %)}]
         
         [btn/new-patient-button {:on-click #(js/alert "Add new patient")}]]
        
        ;; Patient List (Scrollable) - Only show patients not yet added
        [:div {:class "patient-list"}
         (for [patient @available-patients
               :let [patient-added? (is-patient-added? (:id patient))]
               :when (not patient-added?)] ; Only show patients not yet added
           ^{:key (:id patient)}
           [patient/patient-regular 
            (merge patient
                   {:added-to-session? false ; Always false for left list
                    :on-add #(add-patient-as-subject patient)
                    :on-identify #(identify-patient patient)
                    :on-edit #(js/console.log "Edit patient:" (:name patient))})])]]]
      
      ;; Right Panel (Added Participants/Providers)
      [:div {:class "right-panel"}
       [:div {:class "right-content"}
        ;; Alert/Toast at top
        [:div {:class "alert-section"}
         [alert/info-alert {:title "You can start the session with no patients and add them later."
                            :on-close #(js/console.log "Alert closed")}]]
        
        ;; Patients Section
        [:div {:class "patients-section"}
         [:div {:class "section-header"}
          [:h2 "PATIENTS"]
          [:span {:class "count"} (str (total-patients-count) " added")]]
         
         [:p {:class "section-description"}
          "Everyone added appears as a subject, which is the main focus of the session. To reclassify someone (like a support), you can either drag them over the 'Contributors' section or select 'Move to Contributors'."]
         
         [:div {:class "subjects-section"}
          [:h3 "SUBJECT(S)"]
          (for [patient @subjects
                :let [in-contributors? false]] ; Subjects are not in contributors
            ^{:key (:id patient)}
            [patient/patient-regular 
             (merge patient
                    {:added-to-session? true
                     :show-completed-chip? (:just-completed? patient)
                     :on-identify #(identify-patient patient)
                     :on-edit #(js/console.log "Edit patient:" (:name patient))
                     :on-move #(move-to-contributors (:id patient))
                     :move-to-text "Move to contributors"
                     :on-remove #(remove-patient (:id patient))})])]
         
         [:div {:class "contributors-section"}
          [:h3 "CONTRIBUTOR(S)"]
          (for [patient @contributors
                :let [in-contributors? true]] ; Contributors are in contributors
            ^{:key (:id patient)}
            [patient/patient-regular 
             (merge patient
                    {:added-to-session? true
                     :show-completed-chip? (:just-completed? patient)
                     :on-identify #(identify-patient patient)
                     :on-edit #(js/console.log "Edit patient:" (:name patient))
                     :on-move #(move-to-subjects (:id patient))
                     :move-to-text "Move to subjects"
                     :on-remove #(remove-patient (:id patient))})])]]
        
        ;; Divider
        [:div {:class "divider"}]
        
        ;; Providers Section
        [:div {:class "providers-section"}
         [:div {:class "section-header"}
          [:h2 "PROVIDERS"]
          [:span {:class "count"} (str (count @providers) " added")]]
         
         ;; Provider cards
         (for [provider @providers]
           ^{:key (:id provider)}
           [patient/patient-regular 
            (merge provider
                   {:added-to-session? true
                    :is-provider? true ; Mark as provider to hide Move to button
                    :on-edit #(js/console.log "Edit provider:" (:name provider))
                    :on-remove #(js/console.log "Remove provider:" (:name provider))})])]]
       
       ;; Bottom Bar with Secondary and Next Buttons (Fixed at bottom)
       [:div {:class "bottom-bar"}
        ;; Secondary button (left)
        [:button {:class "secondary-button"
                  :on-click #(rfe/push-state :step3)}
         "Skip this"]
        
        ;; Primary button (right)
        [btn/next-step-button {:on-click #(rfe/push-state :step3)}]]]]]))