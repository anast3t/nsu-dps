(ns task2.core-test
  (:require [clojure.test :refer :all]
            [task2.core :refer :all]))

(deftest basic
  (testing "Basic test"
    (is (= [2, 3] (sieve-of-eratosthenes 4)))))

(deftest zero
  (testing "Zero test"
    (is (= [] (sieve-of-eratosthenes 0)))))

(deftest firstTen
  (testing "First ten prime numbers"
    (is (= [2, 3, 5, 7, 11, 13, 17, 19, 23, 29] (sieve-of-eratosthenes 30)))))
