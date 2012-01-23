;; The timeline functions are used to manipulate time slots and time blocks. A
;; time slot is an opening in someone's timeline.  This is where a time block
;; (like a task) can be added. When you add blocks to slots, the slots become
;; smaller (or even disappear).
;;
;; Timelines represent worktime with things like vacations and weekends filtered
;; out. It's solid worktime from the perspective of someone laying out work. The
;; actual dates associated with a Timeline are different for each person, but
;; dates are only meaningful in the context of a calendar and schedule.
(ns com.ejorp.algos.timeline)

;; These will eventually become Java classes.
(defrecord Timeline [free-slots time-blocks])
(defrecord Timeblock [start duration])

;; ## Timeline functions
;; These will move into the Java Timeline class at some point.

(defn new-timeline
  "Constructs an empty Timeline."
  []
  (->Timeline [[0, nil]] []))

(defn free-slots
  "Returns the available slots on a timeline. Each slot is a pair with a start
  and end. nil is interpreted as the end of time.  Units are in hours."
  [tl]
  (:free-slots tl))

(defn time-blocks
  "Returns the Timeblocks on this timeline."
  [tl]
  (:time-blocks tl))

;; ## Block and Slot Algorithms
;; These are functions for manipulating blocks and slots together.

(defn fittable?
  "If a block can fit into a slot by sliding it forward in time, then it is fittable."
  [slot block]
  (let [[slot-start slot-end] slot 
        block-start (:start block)
        duration (:duration block)
        block-end (+ block-start duration)]
    (cond 
      (nil? slot-end) true
      (neg? (- slot-end block-end)) false
      (>= (- slot-end block-end) (- slot-start block-start)) true
      :else false)))

(defn add-block-to-slot
  "Returns the time slots resulting from adding a block to a slot."
  [[slot-start slot-end] block]
  (let [block-start (:start block)
        duration (:duration block)
        block-end (+ block-start duration)
        ]
   (cond (and (= slot-start block-start) (= slot-end block-end)) nil
         (= slot-start block-start) [[block-end slot-end]]
         (= slot-end block-end) [[slot-start block-start]]
         :else [[slot-start block-start], [block-end slot-end]])))


(defn fit-block-in-slot
  "Similar to add-block-to-slot, but this shifts the block start if necessary.
  The new slot and block are returned"
  [[slot-start slot-end] block]
  (let [block-start (:start block)
        new-block-start (if (< block-start slot-start) 
                          slot-start
                          block-start)
        block-duration (:duration block)
        new-block-end (+ new-block-start block-duration)
        new-block (->Timeblock new-block-start block-duration)
        new-slot (add-block-to-slot [slot-start slot-end] new-block)
       ]
    {:slot new-slot, :block new-block}))

(defn try-fitting-block
  "This attempts to fit a block into a slot. If it doesn't work, the original slot is
  returned. If it does fit, then the modified slot is returned along with the (perhaps)
  modified block"
  [slot block]
  (if (fittable? slot block)
    (fit-block-in-slot slot block)
    {:slot slot}))

(defn add-block-to-slots
  "This adds a block to a seq of slots. The resulting block and slots are returned."
  [slots block]
  (let [helper (fn [my-slots prior-slots]
                 (let [cur-slot (first my-slots)
                       rest-slots (rest my-slots)
                       fit-result (try-fitting-block cur-slot block)
                       new-block (:block fit-result)
                       new-slot (:slot fit-result)
                       done? (not (nil? new-block))
                       ]
                   (if done?
                     {:slots (concat prior-slots new-slot rest-slots), :block new-block}
                     (recur rest-slots (conj prior-slots cur-slot)))))]
    (if (nil? block)
      {}
      (helper slots []))))

(defn add-blocks-to-slots
  "This adds a seq of blocks to a seq of slots. The blocks are added in order."
  [slots blocks]
  (let [helper (fn [my-slots my-blocks placed-blocks]
                 (let [fit-results (add-block-to-slots my-slots (first my-blocks))
                       new-slots (:slots fit-results)
                       new-placed-blocks (conj placed-blocks (:block fit-results))
                       ]
                   (if (empty? my-blocks)
                     {:slots my-slots, :blocks placed-blocks}
                     (recur new-slots (rest my-blocks) new-placed-blocks))))]
    (helper slots blocks [])))


;; ### Conditioning functions
;; These functions condition a seq of slots to simplify them.

(defn filter-small-slots
  "Removes any free slots that are smaller than 0.5 h"
  [slots]
  (let [helper (fn [[slot-start slot-end]]
                 (cond
                   (nil? slot-end) true
                   (> (- slot-end slot-start) 0.5) true
                   :else false))]
    (filter helper slots)))


(defn combine-close-slots
  "Combines slots that are within 0.5h of each other"
  [slots]
  (let [helper (fn [my-slots next-slot]
                 (let [[slot1-start slot1-end] (first my-slots)
                       [slot2-start slot2-end] next-slot]
                   (cond
                     (empty? my-slots) [next-slot]
                     (nil? next-slot) my-slots
                     (<= (- slot2-start slot1-end) 0.5) (cons [slot1-start slot2-end] (rest my-slots))
                     :else (cons next-slot my-slots))))]
    (reverse (reduce helper [] slots))))

(defn condense-slots
  "Removes small slots and combines slots that are close."
  [slots]
  (combine-close-slots (filter-small-slots slots)))
