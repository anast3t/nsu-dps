(ns task2.core)

(defn sieve-of-eratosthenes
  [n]
  (let [numbers (range 2 n) primes []]
    (loop [numbers numbers primes primes]
      (if (empty? numbers)
        primes
        (let [prime (first numbers)]
          (recur (remove #(zero? (mod % prime)) numbers) (conj primes prime)))))))


;(print (remove #(zero? (mod % 2)) [1, 2, 3, 4, 5]))
;(println (sieve-of-eratosthenes 0))
(defn new-filter-layer
  [number s]
  (filter
    (fn [t]
      (not (= (mod t number) 0)))
    s))
(defn prime-set
  ([]
   (let [first-prime 2]
     (lazy-seq
       (cons first-prime
             (new-filter-layer first-prime (iterate inc 2))))))

  ([s]
   (let [prime-now (take 1 s)]
     (lazy-seq
       (cons
         prime-now
         (prime-set
           (new-filter-layer prime-now s)))))))
(println (take 10
               (prime-set)))

