var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , PagesController = require('../controllers/pagesController')
  , app = require('../lib/ejorpServer').app
  , PORT = 7777
  , ejorpTopic = TestSupport.configureEjorpTopic(PORT)
;

// TODO: Need to mock out connections to Couch and to ejorp-engine

// Set up server
PagesController.addRoutes(app, '/');
app.listen(PORT);


// Set up test suite
var suite = vows.describe('PagesController');

suite.addBatch({
  'Get top tasks': {
    topic: ejorpTopic('GET', '/pages/top-tasks', {}),
    'should get a 200': function(res, body) {
      console.log(body);
      assert.equal(res.statusCode, 200);
    }
  },
});

suite.export(module);
