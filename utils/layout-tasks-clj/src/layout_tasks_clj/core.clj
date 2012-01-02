(ns layout-tasks-clj.core)

;; ## Constants

;; This is the capacity of a full week. A pomodoro is roughly a 30 minute work cycle.
(def MAX_POMODOROS 20)

;; Any task with unspecified effort defaults to 2 pomodoros.
(def DEFAULT_EFFORT 2)

;; ## Records
;; We're using a couple of records right now. I wonder if it would be better to actually define
;; classes in Java and refer to them here. It would involve another step to create a jar and install
;; it, but I think it would work better.

;; ### Task
;; A task is something that must be done. It has the raw-text from the input as well as the
;; effort left (in pomodoros).
(defrecord Task [raw-text effort-left])

(defn parse-task
  "Given a `string`, this parses it and creates the associated Task."
  [string]
  (let [m (re-matches #".*(\d+)P" string)
        effort-left (if m (Integer/parseInt (nth m 1)) DEFAULT_EFFORT)]
    (->Task string effort-left)))

(defn effort-left
  "This returns the effort left for a Task."
  [task] (:effort-left task))

;; ### Week
;; A Week represents a time range in which tasks can be done. Each week has a capacity giving
;; the number of pomodoros that will fit. The tasks for a week are added such that each week
;; has a reasonable number of tasks in it based on the effort of each task.
(defrecord Week [end-date tasks capacity])

(def week-fmt (org.joda.time.format.DateTimeFormat/forPattern "MMM d, yyyy"))

(defn current-week
  "Returns a Week record corresponding to the current week. If `date-string` is
  specified, then the result is the Week current for the associated date."
  [&[date-string]]
  (let [d (if date-string
            (. week-fmt parseDateTime date-string)
            (org.joda.time.DateTime.))
        cur-sunday (. d withDayOfWeek org.joda.time.DateTimeConstants/SUNDAY)]
    ; TODO: Handle partial week
    (->Week cur-sunday [] MAX_POMODOROS)))

(defn make-next-week
  "Creates a Week that is just after `week`."
  [week]
  (let [cur-sunday (:end-date week)
        next-sunday (. cur-sunday plus org.joda.time.Weeks/ONE)]
    (->Week next-sunday [] MAX_POMODOROS)))

(defn heading
  "Returns the heading for a Week when printed as a string. Basically, something
  like `Jan 1, 2012:`"
  [week]
  (let [formatted-week (. week-fmt print (:end-date week))]
    (str formatted-week ":")))

(defn week-seq
  "Returns a sequence of weeks starting at the current week. If `date-string`
  is specified, then the first week corresponds to the specified date."
  [&[date-string]]
  (iterate make-next-week (apply current-week date-string)))

(defn capacity-left
  "Returns the number of pomodoros left for the current week. As tasks are
  added to a week, the capacity goes down."
  [week]
  (:capacity week))


(defn add-task
  "Given a `week` and a `task`, returns a new week with the task added to it. The capacity of
  the new week is automatically updated."
  [week task]
  (let [cur-capacity (:capacity week)
        cur-tasks (:tasks week)]
   (assoc week
           :capacity (- cur-capacity (effort-left task))
           :tasks (conj cur-tasks task))))

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
                         (ref-set cur-week (add-task @cur-week t))))
        ]
    (doseq [t tasks] (process-task t))
    (dosync
      (ref-set result (conj @result @cur-week))
      @result)))

;; ## Output
;; The following functions are used to convert tasks and weeks to strings for
;; output.
(defn task-as-string [task]
  (:raw-text task))

(defn week-as-string [week]
  (let [h (heading week)
        task-strings (map task-as-string (:tasks week))]
    (apply str (interleave (cons h task-strings) (cycle "\n")))))

(defn as-string [weeks]
  (let [week-strings (reverse (map week-as-string weeks))]
    (apply str (interleave week-strings (cycle "\n")))))
