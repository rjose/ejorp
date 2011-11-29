var Logger = require('../lib/logger')
  , Auth = require('../lib/auth')
  , EjorpEngine = require('../lib/ejorpEngine')
  , Step = require('step')
;

function index(req, res, next) {
  var loginHash = Auth.generateLoginHash();
  Auth.storeLoginHash(loginHash);
  res.render('index.ejs', {loginHash: loginHash});
}

function signin(req, res, next) {
  var memberId = req.body.memberId;
  var loginHash = req.body.loginHash;

  EjorpEngine.generateAuthToken(memberId, loginHash, function(err, authToken) {
    // TODO: Handle error
    res.cookie('ejorp_auth', authToken);
    res.cookie('ejorp_userid', memberId);
    res.json({message: "Signed in!"}, 201);
  });
}

// Add routes
function addRoutes(app) {
  app.get('/', index);
  app.post('/signin', signin);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;

