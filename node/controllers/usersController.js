var Logger = require('../lib/logger')
  , Http = require('http')
  , Step = require('step')
  , Utils = require('./utils')
;

var config = {};

function index(req, res, next) {
  var userId = req.params.userId;
  Logger.log("info", "Index for " + userId);
  Step(
    function fetchOwnTasks() {
      Logger.log("info", "Start");
      Utils.httpRequest("GET", "list_tasks/" + userId, this);
    },
    function renderTasks(err, data) {
      Logger.log("info", "err " + err + " data " + data);
      if (err) {
        res.write("" + err);
        res.end();
      } else {
        res.render('user.ejs', {user: JSON.parse(data)});
      }
    }
  );
}

function addRoutes(app, prefix) {
  app.get(prefix + ':userId', index);
}

module.exports = {
  addRoutes: addRoutes
};
