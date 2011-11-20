var Logger = require('../lib/logger');

function createTask(req, res, next) {
  console.log("Creating task");
  setTimeout(function() {
    res.json({message: 'Created task'}, 201);
  }, 1000);
}

function getTask(req, res, next) {
  Logger.log("info", "Getting a task (log)");
  console.log("Getting task: " + req.params.taskId);
  res.json({id: 101, title: "An awesome task"});
}

function addRoutes(app, prefix) {
  app.post(prefix + 'tasks', createTask);
  app.get(prefix + 'tasks/:taskId', getTask);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
