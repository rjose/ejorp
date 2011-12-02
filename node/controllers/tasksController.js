var Logger = require('../lib/logger')
  , Utils = require('./utils')
  , _ = require('underscore')
;


function createTask(req, res, next) {
  Utils.performRequestInGroup(req.body.groupKey, req, res, next, function(userInfo) {
    var title = req.body.title;
    // TODO: Actually create a task
    console.log("Creating task: " + title);
    res.json({message: 'Created task'}, 201);
  });

}

function getTask(req, res, next) {
  // TODO: Perform an authenticated request. 
  // We'll pull the task first and check if its group is in the user's groups
  // We'll write this explicitly first and then see if we can generalize
  Logger.log("info", "Getting a task (log)");
  console.log("Getting task: " + req.params.taskId);
  res.json({id: 101, title: "An awesome task", message: 'Got task'});
}

function updateTask(req, res, next) {
  Logger.log("info", "Updating a task (log)");
  console.log("Updating task: " + req.params.taskId);
  res.json({message: "Updated task"}, 204);
}

function deleteTask(req, res, next) {
  Logger.log("info", "Deleting task");
  res.json({message: "Deleted task"}, 204);
}

function addChecklist(req, res, next) {
  Logger.log("info", "Adding checklist");
  res.json({message: "Added checklist"}, 200);
}

function blockedOnExisting(req, res, next) {
  Logger.log("info", "Blocked on existing task");
  res.json({message: "Added dependency"}, 200);
}

function blockedOnNew(req, res, next) {
  Logger.log("info", "Blocked on new task");
  res.json({message: "Added dependency"}, 201);
}

function mergeTasks(req, res, next) {
  Logger.log("info", "Merging tasks");
  res.json({message: "Merging tasks"}, 200);
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
