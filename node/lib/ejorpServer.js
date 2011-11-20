
var express = require('express');

var app = express.createServer();

app.configure(function(){
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(__dirname + '/public'));
});


app.configure('test', function(){
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
});

module.exports = {
  app: app
}
