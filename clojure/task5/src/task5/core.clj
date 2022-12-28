(ns task5.core)

(def philosophersNumber 5)
(def thinkTime 10)
(def diningTime 10)
(def periods 100)

(def transactionTries (atom 0))

(defn makeForks
  []
  (map
    #(-> {:fork    (ref 0),
          :forkNum (+ 1 %)})
    (range philosophersNumber)))
(def forks (makeForks))

(defn philosopher
  [right left id]
  (new Thread
       (fn []
         (dorun
           periods
           (repeatedly
             #(do (println (str id " is thinking"))
               (Thread/sleep thinkTime)
               (println (str id " preparing to eat"))
               (dosync
                 (println (str id " transaction started"))
                 (println (str id " trying to lock " (right :forkNum)))
                 (swap! transactionTries inc)
                 (alter (right :fork) inc)
                 (println (str id " trying to lock " (left :forkNum)))
                 (swap! transactionTries inc)
                 (alter (left :fork) inc)
                 (println (str id " got forks, starting to eat"))
                 (Thread/sleep diningTime))
               (println (str id " finished eating"))))))))

(def philosophers
  (map
    #(-> (philosopher
           (nth forks (mod (+ % 1) philosophersNumber))
           (nth forks %)
           %))

    (range philosophersNumber)))

(defn runPhilosophers
  []
  (doall (map #(. % (Thread/start)) philosophers)))

(runPhilosophers)

(time
  (doall
    (map
      (fn [phil] (. phil (Thread/join)))
      philosophers)))
(def goodTransactions
  ( reduce +
           (map #( deref %)
                (map #(% :fork)
                     forks))))
(println (str "Usefull: " goodTransactions))
(println (str "Total: ") @transactionTries)
(println (str "Bad: " (- @transactionTries goodTransactions)))


