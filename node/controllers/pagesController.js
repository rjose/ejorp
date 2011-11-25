var Logger = require('../lib/logger')
  , DocCache = require('../lib/docCache')
  , Auth = require('../lib/auth')
  , Step = require('step')
;

// TODO: Hook this up to Redis to pull info out. If there's a miss, 
// we should hit the ejorp engine for it. While waiting, we should display
// some message. When done, we'll send data via a socket.
function topTasks(req, res, next) {
  var authToken = req.cookies.ejorp_auth;
  Step(
    function lookUpUser() {
      Auth.lookUpUser(authToken, this);
    },
    function requestDocument(err, user) {
      if (err) {
        // TODO: Test this
        res.json(Auth.unauthorizedResponse());
        return next();
      }
      var docId = user.userId + "#top-tasks";
      DocCache.getDocument(docId, this);
    },
    function returnDocument(err, document) {
      if (err) {
         // TODO: Make request to ejorp engine if we couldn't find it in the doc cache
      }
      res.json(document, 200);
      return next();
    }
  );
}


// Add routes
function addRoutes(app, prefix) {
  app.get(prefix + 'pages/top-tasks', topTasks);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
