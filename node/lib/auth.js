var Redis = require("redis")
  , Logger = require('./logger')
  , _ = require('underscore')
;

var m_client = Redis.createClient();

m_client.on("error", function (err) {
  Logger.log("fatal", err);
});

var m_mockUserData = null;
var m_mockedLoginHashes = null;

function lookUpUser(userId, authToken, callback) {
  if (m_mockUserData) {
    // TODO: Figure out what we should do if the record is missing
    var user = m_mockUserData[userId];
    if (!_.include(user.authTokens, authToken)) {
      callback("Invalid userId:" + userId + " for authToken: "+ authToken);
      return;
    }
    callback(null, user);
    return;
  }
  else {
    // SLIME: Implement this
    var result = {userId: "SLIME: 123"};
    callback(null, result);
    return;
  }
}

function mockLookUpUser(userId, authToken, options) {
  if (!m_mockUserData) {
    m_mockUserData = {};
  }

  var user = {userId: userId, authTokens: [authToken]};
  _(options).keys().forEach(function(k) {user[k] = options[k]});

  m_mockUserData[userId] = user;
}


function generateLoginHash() {
  // TODO: Implement this
  return "54321";
}


function storeLoginHash(loginHash) {
  if (m_mockedLoginHashes) {
    m_mockedLoginHashes[loginHash] = true;
    return;
  }
  // TODO: Implement this with a TTL of 15min
}

function doesLoginHashExist(loginHash) {
  if (m_mockedLoginHashes) {
    return !!m_mockedLoginHashes[loginHash];
  }
  // TODO: Implement this
}

function useMockLoginHashes() {
  m_mockedLoginHashes = {};
}

var interface = {
  lookUpUser: lookUpUser,
  mockLookUpUser: mockLookUpUser,
  generateLoginHash: generateLoginHash,
  storeLoginHash: storeLoginHash,
  useMockLoginHashes: useMockLoginHashes,
  doesLoginHashExist: doesLoginHashExist
};

module.exports = interface;

