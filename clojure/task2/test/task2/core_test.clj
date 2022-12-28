(ns task2.core-test
  (:require [clojure.test :refer :all]
            [task2.core :refer :all]))
;
(deftest basic
  (testing "Basic test"
    (is (= [2, 3] (take 2 (prime-set))))))

(deftest zero
  (testing "Zero test"
    (is (= [] (take 0 (prime-set))))))

(deftest firstTen
  (testing "First ten prime numbers"
    (is (= (vec (take 10 (prime-set))) (vec [2, 3, 5, 7, 11, 13, 17, 19, 23, 29])))))
