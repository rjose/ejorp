var Logger = require('../lib/logger');

// TODO: Hook this up to Redis to pull info out. If there's a miss, 
// we should hit the ejorp engine for it. While waiting, we should display
// some message. When done, we'll send data via a socket.
function topTasks(req, res, next) {
  console.log("Getting top tasks");
  // TODO: Read some sample data from file and return it
  res.json({message: 'Top tasks'}, 200);
}


// Add routes
function addRoutes(app, prefix) {
  app.get(prefix + 'pages/top-tasks', topTasks);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
