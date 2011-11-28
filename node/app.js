
/**
 * Module dependencies.
 */
var express = require('express');

var app = module.exports = require('./lib/ejorpServer').app
  , routes = require('./routes')
  , RootController = require('./controllers/rootController')
  , TasksController = require('./controllers/tasksController')
;

var io = require('socket.io').listen(app);

// Configuration

app.configure('development', function(){
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true })); 
});

app.configure('production', function(){
  app.use(express.errorHandler()); 
});

// Routes
// TODO: Delete all of the socket examples
//app.get('/', routes.index);
//app.get('/socket-example', function(req, res) {
  //res.render('socket-example.ejs', {layout: false});
//});
RootController.addRoutes(app);
TasksController.addRoutes(app, '/');


//io.sockets.on('connection', function(socket) {
  //socket.emit('news', {hello: 'world'});
  //socket.on('my other event', function(data) {
    //console.log("----->")
    //console.log(data);
  //});
//});

app.listen(3000);
console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);

