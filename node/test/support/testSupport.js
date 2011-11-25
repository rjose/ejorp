var http = require('http');

// This sets the port of the server that the ejorp topic is associated with. The
// result of this function is a topic that can be called like this:
//
//   ejorpTopic('GET', '/tasks/123')
function configureEjorpTopic(port) {
  function ejorpTopic(method, path, options) {
    var httpOptions = {
      host: '127.0.0.1',
      port: port,
      path: path,
      method: method,
      headers: options.headers
    };

    function result() {
      var self = this;
      var request = http.request(httpOptions, function(res) {
        var body = '';

        res.on('data', function(chunk) {
          body = body + chunk;
        });

        res.on('end', function() {
          // NOTE: This is where the result is ultimately returned
          // The "callback" function will be set as part of the vows framework
          self.callback(res, body);
        });
      });
      if (options.data) {
        request.write(JSON.stringify(options.data));
      }
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

