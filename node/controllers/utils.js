var Auth = require('../lib/auth')
  , Http = require('http')
  , Logger = require('../lib/logger')
  , Step = require('step')
  , _ = require('underscore')
;

var config = {};

function configure(new_config) {
  config = new_config;
}

function httpRequest(method, path, callback) {
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

function lookUpUserByReq(req, callback) {
  var userId = req.cookies.ejorp_userid;
  var authToken = req.cookies.ejorp_auth;

  Auth.lookUpUser(userId, authToken, callback);
  // TODO: Implement the failover to ejorp engine here
}


function performAuthenticatedRequest(req, res, next, callback) {
  Step(
    function lookUpUser() {
      lookUpUserByReq(req, this);
    },
    function(err, userInfo) {
      if (err) {
        res.json({message: 'You are not authorized to complete this action'}, 403);
        return;
      }

      // This is where the action is
      callback(userInfo);
      return;
    }
  ); 
}


function inAuthorizedGroup(groupKey, userInfo) {
  return _.include(userInfo.groups, groupKey);
}

function performRequestInGroup(groupKey, req, res, next, callback) {
  Step(
    function lookUpUser() {
      lookUpUserByReq(req, this);
    },
    function createTask(err, userInfo) {
      if (err || !inAuthorizedGroup(groupKey, userInfo)) {
        res.json({message: 'You are not authorized to complete this action'}, 403);
        return;
      }

      // This is where the action is
      callback(userInfo);
      return;
    }
  );
}

var interface = {
  configure: configure,
  httpRequest: httpRequest,
  lookUpUserByReq: lookUpUserByReq,
  performAuthenticatedRequest: performAuthenticatedRequest,
  performRequestInGroup: performRequestInGroup
}

module.exports = interface;
