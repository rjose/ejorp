(ns com.ejorp.algos.graph)

(defn make-dag
  "Creates a graph from a vector of edges"
  [edges]
  ; TODO: check for cycles
  (let [make-adj-next (fn [cur-map [v w]]
                        (assoc cur-map v (conj (get cur-map v) w)))
        make-adj-prev (fn [cur-map [v w]]
                        (assoc cur-map w (conj (get cur-map w) v)))
        ]
    {:vertices (into #{} (flatten edges))
     :adj-next (reduce make-adj-next {} edges) 
     :adj-prev (reduce make-adj-prev {} edges) 
     :edges edges}
    ))

(defn vertices
  "Returns the vertices of a graph"
  [graph]
  (:vertices graph))

(defn adj-next
  "Returns adjacent vertices after the specified vertex"
  [graph v]
  (get (:adj-next graph) v))

(defn adj-prev
  "Returns adjacent vertices before the specified vertex"
  [graph v]
  (get (:adj-prev graph) v))

(defn bfs-seq-helper
  [graph vertices visited path]

  (if (empty? vertices)
    path
    (let [first-vertex (first vertices)
          new-vertices (into (rest vertices) (adj-next graph first-vertex))
          new-visited (conj visited first-vertex)
          new-path (conj path first-vertex)]
      (recur graph new-vertices new-visited new-path))))

(defn bfs-seq
  "Returns a seq of breadth-first search"
  [graph v]
  (bfs-seq-helper graph [v] #{} []))


(defn any-in
  [to-search items]
  (let [in-search (flatten (for [item items]
                    (concat (map #(= item %) to-search))))
       ]
    (some true? in-search)))

(defn dfs-helper
  [graph vertices acc]
  (let [first-v (first vertices)
        adj (adj-next graph first-v)
        next-vertices (into adj (rest vertices))
        cur-visited (:visited acc)
        new-visited (if first-v
                      (conj cur-visited first-v)
                      cur-visited)
        new-acc {:visited new-visited}
        ]
    (cond (any-in new-visited adj) (throw (new Exception "Graph has a cycle"))
          (empty? vertices) new-acc
          :else (recur graph next-vertices new-acc))))

(defn dfs-seq
  [graph v]
  (let [acc (dfs-helper graph [v] {})]
    (:visited acc)))

