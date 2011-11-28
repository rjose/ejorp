var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , RootController = require('../controllers/rootController')
  , Auth = require('../lib/auth')
  , Fs = require('fs')
  , app = require('../lib/ejorpServer').app
  , PORT = 7777
  , ejorpTopic = TestSupport.configureEjorpTopic(PORT)
;

// Set up server
RootController.addRoutes(app);
app.listen(PORT);

// Set up test suite
var suite = vows.describe('RootController');

suite.addBatch({
  'When getting the root page': {
    topic: "TODO: Add topic",
    'should get a 200 response': function(topic) {
      assert.equal("TODO: Finish this", "DONE");
    }
  }
})

suite.export(module);
