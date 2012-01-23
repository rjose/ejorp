(ns com.ejorp.algos.graph-test
  (:use clojure.test)
  (:use midje.sweet)
  (:use com.ejorp.algos.graph))

(def n1 "A")
(def n2 "B")
(def n3 "C")

(def g1 (make-dag [[n1 n2] [n2 n3]]))

; Check that we know the vertices of a graph
(fact (sort (vertices g1)) => [n1 n2 n3])

; Check that we know which nodes are adjacent (next)
(fact (adj-next g1 n1) => [n2])
(fact (adj-next g1 n2) => [n3])
(fact (adj-next g1 n3) => nil)

; Check that we know which nodes are adjacent (previous)
(fact (adj-prev g1 n1) => nil)
(fact (adj-prev g1 n2) => [n1])
(fact (adj-prev g1 n3) => [n2])

; Check depth-first search seq
(def ds (dfs-seq g1 n1))
(fact (first ds) => n3)
(fact (nth ds 1) => n2)

; TODO: Check that case where node is not in graph

; Test that doing a dfs on a cyclic graph throws an exception
(def cyclic1 (make-dag [[n1 n2] [n2 n3] [n3 n1]]))
(is (thrown? Exception (dfs-seq cyclic1 n1)))
