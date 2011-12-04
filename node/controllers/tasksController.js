var Logger = require('../lib/logger')
  , Step = require('step')
  , Utils = require('./utils')
  , _ = require('underscore')
;

function lookUpTask(taskId, callback) {
  // TODO: Implement this. Also allow people to mock it out.
  var result = {id: 101, groupKey: 22, title: "TODO: Implement lookUptask"};
  callback(null, result);
}

// This checks to see if the user can perform an operation on the
// specified task.
function performRequestOnTask(taskId, req, res, next, callback) {
  Step(
    function lookUpUser() {
      Utils.lookUpUserByReq(req, this.parallel());
      lookUpTask(taskId, this.parallel());
    },
    function(err, userInfo, task) {
      if (err || !_.include(userInfo.groups, task.groupKey)) {
        res.json({message: 'You are not authorized to complete this action'}, 403);
        return;
      }

      // This is where the action is
      callback(task);
      return;
    }
  ); 
}

function createTask(req, res, next) {
  Utils.performRequestInGroup(req.body.groupKey, req, res, next, function(userInfo) {
    var title = req.body.title;
    // TODO: Actually create a task
    res.json({message: 'Created task'}, 201);
  });

}

function getTask(req, res, next) {
  performRequestOnTask(req.params.taskId, req, res, next, function(task) {
    Logger.log("info", "Getting a task (log)");
    res.json(task, 200);
  });
}

function updateTask(req, res, next) {
  performRequestOnTask(req.params.taskId, req, res, next, function(task) {
    Logger.log("info", "Updating a task (log)");
    res.json({message: "Updated task"}, 204);
  });
}

function deleteTask(req, res, next) {
  performRequestOnTask(req.params.taskId, req, res, next, function(task) {
    Logger.log("info", "Deleting task");
    res.json({message: "Deleted task"}, 204);
  });
}

function addChecklist(req, res, next) {
  performRequestOnTask(req.params.taskId, req, res, next, function(task) {
    Logger.log("info", "Adding checklist");
    res.json({message: "Added checklist"}, 200);
  });
}

function blockedOnExisting(req, res, next) {
  performRequestOnTask(req.params.taskId, req, res, next, function(task) {
    Logger.log("info", "Blocked on existing task");
    res.json({message: "Added dependency"}, 200);
  });
}

function blockedOnNew(req, res, next) {
  performRequestOnTask(req.params.taskId, req, res, next, function(task) {
    Logger.log("info", "Blocked on new task");
    res.json({message: "Added dependency"}, 201);
  });
}

function mergeTasks(req, res, next) {
  performRequestOnTask(req.params.taskId, req, res, next, function(task) {
    Logger.log("info", "Merging tasks");
    res.json({message: "Merging tasks"}, 200);
  });
}

// Add routes
function addRoutes(app, prefix) {
  app.post(prefix + 'tasks', createTask);
  app.get(prefix + 'tasks/:taskId', getTask);
  app.put(prefix + 'tasks/:taskId', updateTask);
  app.delete(prefix + 'tasks/:taskId', deleteTask);

  app.put(prefix + 'tasks/:taskId/checklist', addChecklist);
  app.put(prefix + 'tasks/:taskId/blocked-on', blockedOnExisting);
  app.post(prefix + 'tasks/:taskId/blocked-on', blockedOnNew);
  app.put(prefix + 'tasks/:taskId/merge', mergeTasks);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
