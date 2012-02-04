var Logger = require('../lib/logger')
  , Http = require('http')
  , Step = require('step')
;

var config = {};

function HttpRequest(method, path, callback) {
  var options = {
      host: config.host,
      port: config.port,
      path: config.base + path,
      method: method,
  };
  Logger.log("info", "Fetch " + JSON.stringify(options));
  var body = "";
  var request = Http.request(options, function(response) {
      response.on('data', function(data) { body += data; });
      response.on('end', function() { callback(undefined, body); });
  });
  request.on('error', function(err) { callback(err, undefined); });
  request.end();
}

function index(req, res, next) {
  var userId = req.params.userId;
  Logger.log("info", "Index for " + userId);
  Step(
    function fetchOwnTasks() {
      Logger.log("info", "Start");
      HttpRequest("GET", "list_tasks", this);
    },
    function renderTasks(err, data) {
      Logger.log("info", "err " + err + " data " + data);
      if (err) {
        res.write("" + err);
        res.end();
        throw err;
      } else {
        res.render('user.ejs', {userId: userId, tasks: JSON.parse(data)});
      }
    }
  );
}

function addRoutes(app, new_config, prefix) {
  config = new_config;
  Logger.log("info", new_config);
  app.get(prefix + ':userId', index);
}

module.exports = {
  addRoutes: addRoutes
};
