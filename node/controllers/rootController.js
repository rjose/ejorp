var Logger = require('../lib/logger')
  , Auth = require('../lib/auth')
  , Step = require('step')
;

function index(req, res, next) {
  res.render('index.ejs');
}

// Add routes
function addRoutes(app) {
  app.get('/', index);
}


var interface = {
  addRoutes: addRoutes
};

module.exports = interface;

