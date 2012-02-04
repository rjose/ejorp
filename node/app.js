
/**
 * Module dependencies.
 */
var express = require('express');

var app = module.exports = require('./lib/ejorpServer').app
  , routes = require('./routes')
  , RootController = require('./controllers/rootController')
  , TasksController = require('./controllers/tasksController')
  , UsersController = require('./controllers/usersController')
;

var io = require('socket.io').listen(app);

// Configuration

app.configure('development', function(){
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
});

app.configure('production', function(){
  app.use(express.errorHandler());
});

var config = {
  'host': 'localhost',
  'port': 8080,
  'base': '/ejorp-1.0-SNAPSHOT/resources/'
};

RootController.addRoutes(app);
TasksController.addRoutes(app, '/');
UsersController.addRoutes(app, config, '/users/');

app.listen(3000);
console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);

