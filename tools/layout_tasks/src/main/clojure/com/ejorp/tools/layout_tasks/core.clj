(ns com.ejorp.tools.layout-tasks.core
  (:import (com.ejorp.models Task Week)))

;; ## Constants

;; This is the capacity of a full week. A pomodoro is roughly a 30 minute work cycle.
(def MAX_POMODOROS 20)


(defn parse-task
  "Given a `string`, this parses it and creates the associated Task."
  [string]
  (new Task string))

(defn effort-left
  "This returns the effort left for a Task."
  [task] (. task getEffortLeft))

(def week-fmt (org.joda.time.format.DateTimeFormat/forPattern "MMM d, yyyy"))

(defn current-week
  "Returns a Week record corresponding to the current week. If `date-string` is
  specified, then the result is the Week current for the associated date."
  ([]
   (let [cur-date (new org.joda.time.DateTime)
        ]
     (current-week cur-date)))

  ([date]
   (new Week date)))


(defn make-next-week
  "Creates a Week that is just after `week`."
  [week]
  (. week nextWeek))

(defn heading
  "Returns the heading for a Week when printed as a string. Basically, something
  like `Jan 1, 2012:`"
  [week]
  ; TODO: Figure out if this should be in the Week class
  (let [formatted-week (. week-fmt print (. week endDate))]
    (str formatted-week ":")))

(defn capacity-left
  "Returns the number of pomodoros left for the current week. As tasks are
  added to a week, the capacity goes down."
  [week]
  (. week pomodorosLeft))


;; ## Task layout
;; The following functions are used to extract tasks from text and
;; to schedule them over time.
(defn extract-incomplete-tasks
  "Given a seq of lines (as from stdin), this returns a seq of Tasks.
  The order is reversed because the tasks that should be done earlier
  appear lower in the input."
  [lines]
  (let [tasks (filter #(re-matches #"\s+-.*" %) lines)
        incomplete-tasks (remove #(re-matches #".*@done.*" %) tasks)]
    (reverse (map parse-task incomplete-tasks))))

(defn schedule-tasks
  "This schedules a seq of tasks starting from `week` onwards. The result is a seq of
  weeks with tasks such that the sum of the effort of the tasks is within the number of
  pomodoros for that week."
  [tasks week]
  (let [result (ref [])
        cur-week (ref week)
        process-task (fn [t]
                       (if (> (effort-left t) (capacity-left @cur-week))
                         (dosync
                           (ref-set result (conj @result @cur-week))
                           (ref-set cur-week (make-next-week @cur-week))))
                       (dosync
                         (. @cur-week addTask t)))
        ]
    (doseq [t tasks] (process-task t))
    (dosync
      (ref-set result (conj @result @cur-week))
      @result)))

;; ## Output
;; The following functions are used to convert tasks and weeks to strings for
;; output.
(defn task-as-string [task]
  (. task toString))

(defn week-as-string [week]
  (. week toString))
  ;   TODO: See if we can map the java function directly

(defn as-string [weeks]
  (let [week-strings (reverse (map week-as-string weeks))]
    (apply str (interleave week-strings (cycle "\n")))))

