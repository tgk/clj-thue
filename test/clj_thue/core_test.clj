(ns clj-thue.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [clj-thue.core :as sut]))

(def hello-world "a::=HelloWorld
::=
a")

(def increment
  "1_::=1++
0_::=1
01++::=10
11++::=1++0
_0::=_
_1++::=10
::=
_1111111111_")

(deftest thue-parser-test
  (testing "Parses basic program"
    (is (= [:S [:R [:V "a"] [:V "HelloWorld"]] [:T] [:I "a"]]
           (sut/thue-parser hello-world)))))

(deftest gather-rules-test
  (testing "Collects all the replacement rules"
    (is (= [["a" "HelloWorld"]]
           (sut/gather-rules (sut/thue-parser hello-world))))))

(deftest interpret-test
  (testing "Interprets the hello-world program"
    (is (= "HelloWorld" (sut/interpret (sut/thue-parser hello-world)))))
  (testing "Interprets the increment program"
    (is (= "10000000000" (sut/interpret (sut/thue-parser increment))))))
