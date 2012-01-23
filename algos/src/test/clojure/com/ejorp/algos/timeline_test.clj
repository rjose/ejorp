(ns com.ejorp.algos.timeline-test
  (:use clojure.test)
  (:use midje.sweet)
  (:use com.ejorp.algos.timeline))

(def t1 (new-timeline))

; In the beginning, all slots are free, and there are no time blocks
(def free1 (free-slots t1))
(fact free1 => [[0, nil]])

(def blocks1 (time-blocks t1))
(fact blocks1 => [])


; Test fitting blocks into slots
(fact (fittable? [0, nil] (->Timeblock 6 24)) => true)
(fact (fittable? [10, 15] (->Timeblock 6 2)) => true)
(fact (fittable? [10, 15] (->Timeblock 16 2)) => false)
(fact (fittable? [10, 15] (->Timeblock 15 2)) => false)
(fact (fittable? [10, 15] (->Timeblock 10 2)) => true)
(fact (fittable? [10, 15] (->Timeblock 10 5)) => true)
(fact (fittable? [10, 15] (->Timeblock 10 6)) => false)

; Test adding a block to a slot
(fact (add-block-to-slot [10, 15] (->Timeblock 10 2)) => [[12, 15]])
(fact (add-block-to-slot [10, 15] (->Timeblock 12 3)) => [[10, 12]])
(fact (add-block-to-slot [10, 15] (->Timeblock 11 3)) => [[10, 11] [14, 15]])

(fact (add-block-to-slots [[6, 12] [16, 20]] (->Timeblock 6 2)) =>
      {:slots [[8, 12] [16, 20]], :block (->Timeblock 6 2)})

; Add time blocks in priority order
(def r1 (add-blocks-to-slots free1 [(->Timeblock 0 16)]))
(fact r1 => {:slots [[16, nil]] :blocks [(->Timeblock 0 16)]})

(def r2 (add-blocks-to-slots free1 [(->Timeblock 2 16)]))
(fact r2 => {:slots [[0, 2] [18, nil]] :blocks [(->Timeblock 2 16)]})

(def r3 (add-blocks-to-slots free1 [(->Timeblock 2 16), (->Timeblock 1 4)]))
(fact r3 => {:slots [[0, 2] [22, nil]] :blocks [(->Timeblock 2 16), (->Timeblock 18 4)]})

; Filter, combine, and condense slots
(fact (filter-small-slots [[0, 0.5], [2, 4], [5, 5.5]]) => [[2, 4]])
(fact (combine-close-slots [[0, 0.5] [1, 4] [4.2 5]]) => [[0, 5]])
(fact (combine-close-slots [[0, 0.5] [1, 4] [6 nil]]) => [[0, 4] [6, nil]])
(fact (condense-slots [[0, 0.5] [2, 4] [4.5, 7] [10, nil]]) => [[2, 7] [10, nil]])


