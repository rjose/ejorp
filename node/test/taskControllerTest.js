var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , TasksController = require('../controllers/tasksController')
  , app = require('../lib/ejorpServer').app
  , PORT = 7777
  , ejorpTopic = TestSupport.configureEjorpTopic(PORT)
;

// TODO: Need to mock out connections to Couch and to ejorp-engine

// Set up server
TasksController.addRoutes(app, '/');
app.listen(PORT);


// Set up test suite
var suite = vows.describe('TasksController');

suite.addBatch({
  'RESTful calls': {
    topic: ejorpTopic('GET', '/tasks/123'),

    'should get 200': function(res, body) {
      console.log('=====> ' + body);
      assert.equal(res.statusCode, 200);
    }
  },

  'This is one context': {
    topic: 42,
    'is positive': function(num) {
      assert.isTrue(num === 42);
    }
  }
}).export(module);
