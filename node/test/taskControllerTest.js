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
  'Get task': {
    topic: ejorpTopic('GET', '/tasks/123', {}),

    'should get 200': function(res, body) {
      console.log('=====> ' + body);
      assert.equal(res.statusCode, 200);
    }
  },
  
  'Create task': {
    // NOTE: It would be nice to specify the context here
    topic: ejorpTopic('POST', '/tasks', {data: {title: 'A new task'}}),
    'should get 201': function(res, body) {
      console.log('=====> ' + body);
      // TODO: Check that we get a task ID
      assert.equal(res.statusCode, 201);
    }
  },

  'Update a task': {
    topic: ejorpTopic('PUT', '/tasks/123', {data: {title: 'Updated title'}}),
    'should get 204': function(res, body) {
      console.log(body);
      assert.equal(res.statusCode, 204);
    }
  },

  'Delete task': {
    topic: ejorpTopic('DELETE', '/tasks/123', {}),
    'should get 204': function(res, body) {
      console.log(body);
      assert.equal(res.statusCode, 204);
    }
  }

}).export(module);
