(ns layout-tasks-clj.app
  (:gen-class)
  (:use layout-tasks-clj.core))

;; This implements an app that can lay out tasks in time from a file
;; in a markdown format that looks like this:
;;
;;  <pre> 
;;   Jan 12, 2012:
;;     - This is a task 4P
;;     - This is another task 6P
;;     - This task is done and will be skipped @done
;;  </pre> 
;;
;; We use functions from layout-tasks-clj.core to extract incomplete tasks from stdin, schedule them,
;; and then print them to stdout.
(defn -main []
  (let [input (line-seq (java.io.BufferedReader. *in*))
        incomplete-tasks (extract-incomplete-tasks input) 
        sched (schedule-tasks incomplete-tasks (current-week))
        ]
    (println (as-string sched))))
