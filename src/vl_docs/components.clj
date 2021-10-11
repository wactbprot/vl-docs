(ns vl-docs.components
  ^{:author "Thomas Bock <wactbprot@gmail.com>"
    :doc "Component library."}
  (:require [clojure.string :as string]))

(defn width-trans [w]
  (condp = w
    :one-quarter "uk-width-1-4"
    :half "uk-width-1-2"
    :three-quarter "uk-width-3-4"
    :full "uk-width-1-1"
    :one-sixth "uk-width-1-6"
    :two-sixth "uk-width-2-6"
    :three-sixth "uk-width-3-6"
    :four-sixth "uk-width-4-6"
    :five-sixth "uk-width-5-6"))

(defn article [] [:article.uk-article])

(defn acc-frame [] [:ul {:uk-accordion ""}]) ;; collapsible: false

(defn acc-sheet 
  ([title content] (acc-sheet title content {:open false}))
  ([title content {o :open}]
   [:li (when o {:class "uk-open"})
    [:a.uk-accordion-title.uk-text-muted {:href "#"} title]
    [:div.uk-accordion-content
     [:div.uk-card.uk-card-body.uk-background-muted content]]]))

(defn button [data text] [:button.uk-button.uk-button-default.doc-button  (merge {} data) text])

(defn grid [] [:div {:class "uk-grid-small uk-child-width-expand@s" :uk-grid ""}])

(defn margin [] [:p {:uk-margin ""}])

(defn label [s]
  (when (seq s) [:label.uk-form-label.uk-text-muted {:for "form-stacked-text"} s]))

;;........................................................................
;; form
;;........................................................................
(defn form-heading [s][:h4.uk-heading-line.uk-text-center.uk-text-muted [:span s]])

(defn form [] [:form.uk-form-stacked.uk-grid.uk-child-width-auto {:uk-grid ""}])

(defn form-text-input [{l :label v :value :as data} {w :width} ]
  [:div {:class (width-trans w)}
   (label l)
   [:div.uk-form-controls
    [:input.uk-input.uk-text-emphasis.doc-input
     (merge {:id "form-stacked-text" :value v} data)]]])

(defn form-select [{l :label v :value o :options :as data} {w :width} ]
  [:div {:class (width-trans w)}
   (label l)
   [:div.uk-form-controls
    (into [:select.uk-select.doc-input (merge {:id "form-horizontal-select" :selected v} data)]
           (mapv (fn [s] [:option (when (= s v) {:selected "" :value s}) s]) o) )]])
