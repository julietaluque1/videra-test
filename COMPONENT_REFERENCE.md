# Component Reference Guide

## Button Component (`button.cljs`)

### Basic Button
```clojure
[button {:variant "primary"  ; primary|secondary|outline|ghost|white|alternate|danger
         :size "md"          ; xs|sm|md|lg
         :text "Click me"
         :on-click #(handle-click)
         :disabled? false}]
```

### Icon Button
```clojure
[icon-button {:icon "arrow-left"        ; Any Font Awesome icon
              :hover-background? true   ; true = gray background on hover
              :on-click #(handle-click)}]
```

### Close Button
```clojure
[close-button {:hover-background? true  ; true for dialogs, false for alerts
               :on-click #(handle-close)}]
```

## Patient Card (`patient.cljs`)

```clojure
[patient/patient-regular {:name "John Doe"
                         :patient-id "P2024-1234"
                         :incomplete? true
                         :added-to-session? false
                         :show-completed-chip? false  ; For showing completed status
                         :avatar-url "https://..."
                         :on-add #(...)
                         :on-identify #(...)
                         :on-edit #(...)
                         :on-move #(...)
                         :on-remove #(...)
                         :on-click #(...)}]
```

## Patient Chip (`patient_chip.cljs`)

```clojure
; Incomplete patient chip (yellow)
[incomplete-patient-chip {:label "Incomplete patient"}]

; Active incomplete chip (blue)
[incomplete-active-patient-chip {:label "Incomplete patient"}]

; Completed chip (green, auto-hides)
[completed-patient-chip {:label "Patient completed"
                        :show-completed-duration 3000}]
```

## Dialogs

### Confirmation Dialog (`confirmation_dialog.cljs`)
```clojure
; Show dialog
(show-confirmation-dialog! "Remove participant?"
                          "Are you sure you want to remove this participant?"
                          (fn [] (println "Confirmed!")))

; Include global dialog in your app
[global-confirmation-dialog]
```

### Identify Patient Dialog (`identify_patient_dialog.cljs`)
```clojure
; Show dialog
(show-identify-patient-dialog! 
  {:name "John" :patient-id "123" :incomplete? true}  ; Incomplete patient
  [{:name "John Smith" :patient-id "456" :match-percentage 85}]  ; Matches
  (fn [selected] (println "Selected:" selected)))

; Include global dialog in your app
[global-identify-patient-dialog]
```

## Alert Component (`alert.cljs`)

```clojure
[info-alert {:title "Information message"
             :on-close #(handle-close)}]

[success-alert {:title "Success!"
                :on-close #(handle-close)}]

[warning-alert {:title "Warning!"
                :on-close #(handle-close)}]

[error-alert {:title "Error occurred"
              :on-close #(handle-close)}]
```

## Toast Notifications (`toast.cljs`)

```clojure
; Show toast
(show-toast! {:id (random-uuid)
              :type "success"  ; success|error|info|warning
              :message "Operation completed!"})

; Include toast manager in your app
[toast-manager]
```

## Tabs (`tabs.cljs`)

```clojure
[patients-providers-tabs {:on-tab-change #(handle-tab-change %)}]
; % will be "patients" or "providers"
```

## Search Input (`search_input.cljs`)

```clojure
[patient-search-input {:placeholder "Search patients..."
                      :on-search #(handle-search %)}]
```

## Avatar (`avatar.cljs`)

```clojure
[avatar {:url "https://..."
         :name "John Doe"     ; Used for initials if no URL
         :size 32}]           ; Size in pixels
```

## Step 3 Screen (`step3_screen.cljs`)

```clojure
; Complete screen with all components
[step3-screen]
```

## Important Notes:

1. **Font Awesome**: All icons use Font Awesome Pro (already set up in index.html)

2. **State Management**: 
   - Dialogs use global atoms for state
   - Toast uses global atom for notifications
   - Components use local reagent atoms for UI state

3. **Styling**: All styles are in `styles/main.less`

4. **Color Tokens**: Use the predefined color variables:
   - `@primary-*`, `@grays-*`, `@actions-*`, `@support-*`

5. **Testing Components**: Visit `/localhost` to see the component library