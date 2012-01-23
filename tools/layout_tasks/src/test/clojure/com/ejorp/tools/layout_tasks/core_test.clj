(ns com.ejorp.tools.layout-tasks.core-test
  (:use clojure.test)
  (:use midje.sweet)
  (:use com.ejorp.tools.layout-tasks.core))

;; ## Test Data

(def cur-w (current-week "Jan 8, 2012"))
(def next-w (make-next-week cur-w))

;;
;; ### Data for tasks that fit in one week
;;
;; The following test data is for the case where all of the
;; tasks will fit into one week.
(def sample-input1
  [
   "Jan 1, 2012:"
   "\t- Task two 8P"
   "\t- Task one: 4P"
   "\t- Task three 8P @done"
   ])

(def incomplete-tasks1 (extract-incomplete-tasks sample-input1))
(def sched1 (schedule-tasks incomplete-tasks1 cur-w))
(def task1 (parse-task "\t- Task one: 5P"))

;;
;; ### Data for tasks that fit into two weeks
;;
;; For this data, the tasks will only fit into two weeks.
(def sample-input2
  [
   "Jan 1, 2012:"
   "\t- Task three 8P"
   "\t- Task two 9P"
   "\t- Task one: 4P"
   ])

(def incomplete-tasks2 (extract-incomplete-tasks sample-input2))
(def sched2 (schedule-tasks incomplete-tasks2 cur-w))


;; ## Tests

;; We should be able to parse the effort left from a task.
(fact (effort-left task1) => 5)

;; Out of the three tasks, one is marked done, so there should be 2 incomplete tasks
(fact (count incomplete-tasks1) => 2)

;; If today is Jan 3, then the end date for the current week is Jan 8 (Sunday)
(fact (. cur-w endDate) => (. week-fmt parseDateTime "Jan 8, 2012"))

;; The heading for the current week and the next week should be correct
(fact (heading cur-w) => "Jan 8, 2012:")
(fact (heading next-w) => "Jan 15, 2012:")

;; The number of weeks in our schedule should only be 1 since all tasks should fit in that week.
(fact (count sched1) => 2)

;; The number of tasks in the first week should be 2 (the number of incomplete tasks)
(fact (count (. (nth sched1 1) getTasks)) => 2)

;; When we get the schedule as a string, it should be correct.
(def expected1 (str "Jan 15, 2012:\n"
                    "\t- Task one: 4P\n"
                    "\t- Task two 8P\n\n"
                    "Jan 8, 2012:\n\n" 
                    ))
(fact (as-string sched1) => expected1)


;; For tasks that fit into two weeks, the schedule should have 2 weeks in it.
(fact (count sched2) => 3)

;; A two week schedule should be correct.
(def expected2 (str "Jan 22, 2012:\n"
                    "\t- Task three 8P\n\n"
                    "Jan 15, 2012:\n"
                    "\t- Task one: 4P\n"
                    "\t- Task two 9P\n\n"
                    "Jan 8, 2012:\n\n" 
                    ))
(fact (as-string sched2) => expected2)
