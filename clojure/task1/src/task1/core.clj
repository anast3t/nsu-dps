(ns task1.core)

;(defn step
;  "step"
;  [alphabet, words]
;  (reduce (fn [strings, elem]
;            (concat strings
;                   (map #(str elem %)
;                         (remove #{elem} alphabet))))
;          ()
;          words))
(defn step
  "step"
  [alphabet, words]
  (reduce (fn [strings, elem]
            (concat strings
                    (map #(str elem %)
                         (filter #(not (some
                                         (fn [u] (= u %))
                                         (vec elem)))
                                 alphabet))))
          ()
          words))
(defn stepper
  "combination of steps"
  [alphabet, n, TEST]
  (reduce (fn [res, nstep]
            (concat res
                    (step alphabet res)))
          TEST ;change on alphabet
          (range 0 (- n 1))))

;(println (seq (char-array "ab")))
;(println #{"a"})
;(println (type (seq "ab")))
;(println (type "ab"))
;(println (type (set (seq "a"))))
;(println (type  #{"a"}))
;(println (vec(char-array "ab")))
;(println ["a", "b"])
;(println (type(vec(char-array "ab"))))
;(println (type ["a", "b"]))
;(println (type (nth (vec(char-array "ab")) 1)))
;(println (type (nth ["a", "b"] 1)))
;(println (vec "ab"))
;
;(println
;  (filter #(not (some (fn [u] (= u %))
;                      (vec "ab")))
;          [\a, \b, \c]))
;(println (remove (set (seq "a")) ["a", "b"]))
;(println (remove #{"a"} ["a", "b"]))

(println (stepper [\a, \b, \c] 3 ["a", "b", "c"]))
;(println (type (nth (stepper [ "a", "b", "c"] 3) 3)))