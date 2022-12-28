(ns task3.core)

(defn prepare-chunks
  "Create batch of chunks for use in futures"
  [s] (let [
            core-number (.availableProcessors (Runtime/getRuntime))
            chunk-size 10]
        (partition-all core-number (partition-all chunk-size s))))

; ->> - pass val through funcs

(defn convert-to-future
  "Pushes chunk to future and uses filter predicate"
  [pred s] (map #(->> %
                      (map
                        (fn [g]
                          (->> g
                               (filter pred)
                               (doall)
                               (future)))))
                s))



(defn convert-from-future
  "Derefs futures to get values"
  [s] (->> s
           (doall)
           (map deref)
           (reduce concat)))

(defn pfilter
  ([pred s] (pfilter (convert-to-future pred (prepare-chunks s))))
  ([s]
   (if (empty? s)
     (-> ())
     (lazy-seq (concat (convert-from-future (first s))
                       (pfilter (rest s)))))))