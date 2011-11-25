var _ = require('underscore');

var m_mockUserData = null;

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
    var result = {userId: 123};
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

var interface = {
  lookUpUser: lookUpUser,
  mockLookUpUser: mockLookUpUser
};

module.exports = interface;

