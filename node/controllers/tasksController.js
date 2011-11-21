var Logger = require('../lib/logger');

function createTask(req, res, next) {
  console.log("Creating task");
  setTimeout(function() {
    res.json({message: 'Created task'}, 201);
  }, 100);
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

// Add routes
function addRoutes(app, prefix) {
  app.post(prefix + 'tasks', createTask);
  app.get(prefix + 'tasks/:taskId', getTask);
  app.put(prefix + 'tasks/:taskId', updateTask);
  app.delete(prefix + 'tasks/:taskId', deleteTask);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
