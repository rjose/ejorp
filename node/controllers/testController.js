var Logger = require('../lib/logger')
  , Step = require('step')
  , Utils = require('./utils')
;

function create(req, res, next) {
  var testSuite = req.params.testSuite;
  Logger.log("info", "Create test suite " + testSuite);
  Step(
    function putRequest() {
      Utils.httpRequest("PUT", "create_test_data/" + testSuite, this);
    },
    function displayResults(err, data) {
      Logger.log("info", "err " + err + " data " + data);
      if (err) {
        res.write("" + err);
        res.end();
        throw err;
      } else {
        res.write(data);
        res.end();
      }
    }
  );
}

function addRoutes(app, prefix) {
  app.get(prefix + 'create/:testSuite', create);
}

module.exports = {
  addRoutes: addRoutes
};
