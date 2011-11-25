function lookUpUser(authToken, callback) {
  // SLIME: Implement this
  var result = {userId: 123};
  callback(null, result);
  return;
}

var interface = {
  lookUpUser: lookUpUser
};

module.exports = interface;

