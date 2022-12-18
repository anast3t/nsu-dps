(ns task2.core)

(defn sieve-of-eratosthenes
  [n]
  (let [numbers (range 2 n) primes []] ;
    (loop [numbers numbers primes primes]
      (if (empty? numbers)
        primes
        (let [prime (first numbers)]
          (recur (remove #(zero? (mod % prime)) numbers) (conj primes prime)))))))


;(print (remove #(zero? (mod % 2)) [1, 2, 3, 4, 5]))
(println (sieve-of-eratosthenes 0))