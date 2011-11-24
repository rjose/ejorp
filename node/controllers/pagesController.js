var Logger = require('../lib/logger');

// TODO: Hook this up to Redis to pull info out. If there's a miss, 
// we should hit the ejorp engine for it. While waiting, we should display
// some message. When done, we'll send data via a socket.
function topTasks(req, res, next) {
  console.log("Getting top tasks");
  // TODO: Stub this out 
  res.json({
    tasks: [
      {title: 'Get server up', recentActivity: [{personId: 10, picture: 'http://image.png'}], isActive: true},
      {title: 'Get server deployed', recentActivity: [{personId: 14, picture: 'http://image.png'}], isActive: false},
      {title: 'Test server', recentActivity: [{personId: 30, picture: 'http://image.png'}], isActive: false}],
    alert: {type: 'FIRST_TIME_USER'}},
  200);
}


// Add routes
function addRoutes(app, prefix) {
  app.get(prefix + 'pages/top-tasks', topTasks);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
