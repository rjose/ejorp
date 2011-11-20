var http = require('http');

// This sets the port of the server that the ejorp topic is associated with. The
// result of this function is a topic that can be called like this:
//
//   ejorpTopic('GET', '/tasks/123')
function configureEjorpTopic(port) {
  function ejorpTopic(method, path) {
    var httpOptions = {
      host: '127.0.0.1',
      port: port,
      path: path,
      method: method
    };

    function result() {
      var self = this;
      var request = http.request(httpOptions, function(res) {
        var body = '';
        res.on('data', function(chunk) {
          body = body + chunk;
        });
        res.on('end', function() {
          self.callback(res, body);
        });
      });
      request.end();
    }

    return result;
  }
  return ejorpTopic;
}

var interface = {
  configureEjorpTopic: configureEjorpTopic
};

module.exports = interface;

