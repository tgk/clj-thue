(ns clj-thue.core
  (:require [instaparse.core :as insta]))

(def thue-parser
  (insta/parser
    "S = R* T I
     R = V '::=' V '\n'
     V = #'[A-Za-z0-9_\\+]+'
     I = #'[A-Za-z0-9_\\+]+'
     T = '::=\n'"))

(defn gather-rules
  [thue]
  (for [r (filter #(= :R (first %)) (rest thue))]
    (let [[_V left _assign right] r] [(second left) (second right)])))

(defn replace-first
  [target pattern replacement]
  (let [idx (.indexOf target pattern)]
    (when (not= -1 idx)
      (str (.substring target 0 idx)
           replacement
           (.substring target (+ idx (.length pattern)) (.length target))))))

(defn apply-rule
  [rules value]
  (first
   (filter identity
           (for [[pattern replacement] rules]
             (replace-first value pattern replacement)))))

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
