(ns task2.core)
(defn new-filter-layer
  [number s] (filter
               (fn [t]
                 (not (= (mod t number) 0)))
               s))
(defn prime-set
  ([]
   (let [first-prime 2]
     (lazy-seq (cons first-prime
                     (prime-set (new-filter-layer first-prime (iterate inc 2)))))))
  ([s]
   (let [prime-now (nth (take 1 s) 0)]
     (lazy-seq
       (cons
         prime-now
         (prime-set
           (new-filter-layer prime-now s)))))))

; На каждое число в последовательности создаётся фильтр, который отсеевает по условию решета Эратосфена.
;  Time для демонстрации кэширования
;(print (doall (take 20 (prime-set))))