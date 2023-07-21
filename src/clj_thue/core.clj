(ns clj-thue.core
  (:require [instaparse.core :as insta]))

(def thue-parser
  (insta/parser
    "<S> = R* T I
     R = V <'::='> V <'\n'>
     <V> = #'[A-Za-z0-9_\\+]+'
     I = #'[A-Za-z0-9_\\+]+'
     T = <'::=\n'>"))

(defn gather-rules
  [thue]
  (->> thue
       (filter #(= :R (first %)))
       (map rest)))

(defn replace-first
  [target pattern replacement]
  (let [idx (.indexOf target pattern)]
    (when (not= -1 idx)
      (str (.substring target 0 idx)
           replacement
           (.substring target (+ idx (.length pattern)) (.length target))))))

(defn apply-rule
  [rules value]
  (->> rules
       (map (partial apply replace-first value))
       (filter identity)
       first))

(defn interpret
  [thue]
  (let [rules (gather-rules thue)
        initial-value (-> thue
                          last
                          second)]
    (loop [current-value initial-value]
      (let [updated-value (apply-rule rules current-value)]
        (if updated-value
          (recur updated-value)
          current-value)))))
