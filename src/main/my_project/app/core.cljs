(ns my-project.app.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [my-project.components.patient :as patient]
            [my-project.components.participant-button :as pb]
            [my-project.components.patient-chip :as chip]))

;; State
(defonce current-match (r/atom nil))
(defonce active-component (r/atom "buttons"))

;; Pages
(defn home-page []
  [:div
   [:h1 "Hello from Reagent! 🚀"]
   [:p "This is a live-reloading ClojureScript app with React/Reagent"]
   [:div {:style {:background-color "#f0f8ff"
                  :padding "20px"
                  :border-radius "10px"
                  :margin "20px 0"}}
    [:h2 "Live editing test"]
    [:p "If you can see this styled box, live editing is working!"]]])

;; Patient Completed Demo Component
(defn patient-completed-demo []
  (r/with-let [;; State for the animated simulation
               simulation-state (r/atom "inactive") ; "inactive" | "active" | "completed"
               
               ;; Function to run the simulation with smooth transitions
               run-simulation (fn []
                               (reset! simulation-state "active")
                               ;; Stay in active state longer for visibility
                               (js/setTimeout 
                                #(do (reset! simulation-state "completed")
                                     ;; Stay in completed state longer (5 seconds instead of 3)
                                     (js/setTimeout 
                                      (fn [] (reset! simulation-state "inactive"))
                                      5000))
                                1500))] ; Increased from 500ms to 1500ms for better visibility
    
    [:div {:class "button-group"}
     ;; Left: Permanent preview showing how the chip looks
     [:div {:style {:display "flex" :flex-direction "column" :align-items "flex-start" :gap "8px"}}
      [:div {:style {:font-size "11px" :color "#71717a" :font-weight "500"}} "Preview:"]
      [chip/patient-chip {:status "completed" 
                         :label "Patient completed"
                         :show-completed-duration 999999}]] ; Very long duration so it doesn't disappear
     
     ;; Right: Animated simulation showing the actual behavior
     [:div {:style {:display "flex" :flex-direction "column" :align-items "flex-start" :gap "8px"}}
      [:div {:style {:display "flex" :align-items "center" :gap "8px"}}
       [:div {:style {:font-size "11px" :color "#71717a" :font-weight "500"}} "Simulation:"]
       [:button {:style {:padding "4px 8px" :font-size "11px" :border "1px solid #e4e4e7" 
                        :border-radius "4px" :background "#fff" :cursor "pointer"}
                :on-click run-simulation}
        "Run Demo"]]
      [:div {:style {:min-height "20px" :min-width "120px" :position "relative"}}
       [:div {:style {:opacity (if (= @simulation-state "inactive") "0.6" "1")
                     :transition "opacity 0.4s ease-in-out, transform 0.3s ease-in-out"
                     :transform (case @simulation-state
                                  "active" "translateY(0px)"
                                  "completed" "translateY(0px)" 
                                  "inactive" "translateY(-2px)")}}
        (case @simulation-state
          "active" [chip/incomplete-active-patient-chip {:label "Incomplete patient"}]
          "completed" [chip/completed-patient-chip {:label "Patient completed" 
                                                   :show-completed-duration 5000}]
          "inactive" [:div {:style {:font-size "11px" :color "#a1a1aa" :font-style "italic"}} 
                      "Click 'Run Demo'"])]]]]]))

;; Component sections data
(def component-sections
  {"buttons" {:title "Buttons"
              :description "Expandable buttons with icon and text. Hover to see the full button label."
              :components [{:name "Primary Button"
                           :preview [pb/add-participant-button {:on-click #(js/alert "Add participant clicked!")}]
                           :code "[pb/add-participant-button {:on-click #(add-participant)}]"}
                          {:name "Secondary Buttons"
                           :preview [:div {:class "button-group"}
                                    [pb/identify-patient-button {:on-click #(js/alert "Identify patient clicked!")}]
                                    [pb/edit-information-button {:on-click #(js/alert "Edit information clicked!")}]
                                    [pb/move-to-button {:on-click #(js/alert "Move to clicked!")}]
                                    [pb/remove-button {:on-click #(js/alert "Remove clicked!")}]]
                           :code "[pb/identify-patient-button {:on-click #(identify-patient)}]\n[pb/edit-information-button {:on-click #(edit-information)}]\n[pb/move-to-button {:on-click #(move-to)}]\n[pb/remove-button {:on-click #(remove)}]"}]}
   "patient-cards" {:title "Patient Cards"
                    :description "Interactive patient card components with hover states."
                    :components [{:name "Regular Patient Card"
                                 :preview [patient/patient-regular {:name "Ethan Green"
                                                                    :patient-id "KL78MNKL78MNKL78MNKL"
                                                                    :incomplete? true
                                                                    :is-speaker? true
                                                                    :avatar-url "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=32&h=32&fit=crop&crop=face"
                                                                    :on-add #(js/alert "Add participant!")
                                                                    :on-identify #(js/alert "Identify patient!")
                                                                    :on-edit #(js/alert "Edit information!")
                                                                    :on-move #(js/alert "Move patient!")
                                                                    :on-remove #(js/alert "Remove patient!")
                                                                    :on-click #(js/alert "Patient clicked!")}]
                                 :code "[patient/patient-regular {:name \"Ethan Green\"\n                          :patient-id \"KL78MNKL78MNKL78MNKL\"\n                          :incomplete? true\n                          :is-speaker? true\n                          :on-add #(add-participant)\n                          :on-identify #(identify-patient)}]"}
                                {:name "Informative Patient Card" 
                                 :preview [patient/patient-informative {:name "Dr. Sarah Johnson"
                                                                        :patient-id "SJ99XYWZ88ABCD123456"
                                                                        :date-of-birth "03/15/1985"
                                                                        :phone "+15559876543"
                                                                        :email "s.johnson@hospital.org"
                                                                        :match-percentage 92
                                                                        :avatar-url "https://images.unsplash.com/photo-1494790108755-2616c9db9d7e?w=32&h=32&fit=crop&crop=face"
                                                                        :on-click #(js/alert "Informative patient clicked!")}]
                                 :code "[patient/patient-informative {:name \"Dr. Sarah Johnson\"\n                              :date-of-birth \"03/15/1985\"\n                              :phone \"+15559876543\"\n                              :email \"s.johnson@hospital.org\"\n                              :match-percentage 92}]"}]}
   "patient-chips" {:title "Patient Chips"
            :description "Non-interactive status indicators for patient states. Transparent backgrounds with specific color coding."
            :components [{:name "Incomplete Patient (Default)"
                         :preview [chip/incomplete-patient-chip {:label "Incomplete patient"}]
                         :code "[chip/incomplete-patient-chip {:label \"Incomplete patient\"}]"}
                        {:name "Incomplete Patient (Active)"
                         :preview [chip/incomplete-active-patient-chip {:label "Incomplete patient"}]
                         :code "[chip/incomplete-active-patient-chip {:label \"Incomplete patient\"}]"}
                        {:name "Patient Completed (Temporary)"
                         :preview [patient-completed-demo]
                         :code "[chip/completed-patient-chip {:label \"Patient completed\"\n                              :show-completed-duration 3000}]"}]}})

;; Sidebar navigation component
(defn component-sidebar []
  (let [current-active @active-component] ; Ensure reactivity by derefencing in render
    [:div {:class "component-sidebar"}
     [:div {:class "sidebar-header"}
      [:h3 "Components"]]
     [:nav {:class "sidebar-nav"}
      (for [[key section] component-sections]
        ^{:key key}
        [:button {:class (str "nav-item" (when (= current-active key) " nav-item--active"))
                  :on-click #(do 
                              (js/console.log "Switching from" current-active "to:" key)
                              (reset! active-component key))}
         (:title section)])]]))

;; Component content area
(defn component-content []
  (when-let [section (get component-sections @active-component)]
    [:div {:class "component-content"}
     [:div {:class "content-header"}
      [:h1 (:title section)]
      [:p (:description section)]]
     
     [:div {:class "component-section"}
      (for [component (:components section)]
        ^{:key (:name component)}
        [:div {:class "component-showcase"}
         [:h3 {:class "component-name"} (:name component)]
         [:div {:class "component-preview"}
          (:preview component)]
         [:div {:class "component-code"}
          [:pre {:class "code-block"}
           (:code component)]]])]]))

(defn component-library-page []
  [:div {:class "component-library"}
   [component-sidebar]
   [component-content]])

;; Routes
(def routes
  [["/" {:name :home
         :view home-page}]
   ["/component-library" {:name :component-library
                          :view component-library-page}]])

;; Current page view
(defn current-page []
  (when-let [match @current-match]
    (let [view (:view (:data match))]
      [view])))

;; Main app component
(defn app []
  [current-page])

;; Initialize router
(defn init-router! []
  (rfe/start!
    (rf/router routes)
    (fn [m] (reset! current-match m))
    {:use-fragment true}))

;; Render function
(defn render []
  (rdom/render [app] (.getElementById js/document "root")))

;; Main entry point
(defn ^:export main []
  (init-router!)
  (render))

;; Hot reload hook
(defn ^:dev/after-load reload! []
  (render))
