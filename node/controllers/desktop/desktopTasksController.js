var Logger = require('../../lib/logger')
  , Step = require('step')
  , Utils = require('../utils')
  , _ = require('underscore')
;

function newTasks(req, res, next) {
  // TODO: Extract info from user to render
  res.render('desktop/newTasks.ejs');
}

function createTasks(req, res, next) {
  console.log(req.body);
  console.log(req.cookies);
  res.json({message: "OK"});
}

// Add routes
function addRoutes(app, prefix) {
  app.get(prefix + 'tasks/new', newTasks);
  app.post(prefix + 'tasks', createTasks);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
