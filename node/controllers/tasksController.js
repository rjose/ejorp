var Logger = require('../lib/logger')
  , Step = require('step')
  , Auth = require('../lib/auth')
  , _ = require('underscore')
;

function lookUpUserByReq(req, callback) {
  var userId = req.cookies.ejorp_userid;
  var authToken = req.cookies.ejorp_auth;
  
  Auth.lookUpUser(userId, authToken, callback);
  // TODO: Implement the failover to ejorp engine here
}


function inAuthorizedGroup(groupKey, userInfo) {
  return _.include(userInfo.groups, groupKey); 
}

function createTask(req, res, next) {
  Step(
    function lookUpUser() {
      lookUpUserByReq(req, this);
    },
    function createTask(err, userInfo) {
      if (err || !inAuthorizedGroup(req.body.groupKey, userInfo)) {
        res.json({message: 'You are not authorized to complete this action'}, 403);
        return;
      }

      var title = req.body.title;
      // TODO: Actually create a task

      console.log("Creating task: " + title);
      res.json({message: 'Created task'}, 201);
    }
  );
}

function getTask(req, res, next) {
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
