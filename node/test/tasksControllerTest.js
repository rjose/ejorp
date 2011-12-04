var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , Auth = require('../lib/auth')
  , TasksController = require('../controllers/tasksController')
  , app = require('../lib/ejorpServer').app
  , PORT = 7777
  , ejorpTopic = TestSupport.configureEjorpTopic(PORT)
;

// Set up server
TasksController.addRoutes(app, '/');
app.listen(PORT);

Auth.mockLookUpUser('123', 'my-token', {userId: '123', groups: [22, 33, 44], authTokens: ['my-token']});

var DEFAULT_HEADERS = {'Content-Type': 'application/json',
  'Cookie': 'ejorp_auth=my-token; ejorp_userid=123'};

// Set up test suite
var suite = vows.describe('TasksController');

// The first batch of tests exercises basic CRUD operations
suite.addBatch({
  'Get task': {
    topic: ejorpTopic('GET', '/tasks/123', {headers: DEFAULT_HEADERS}),

    'should get 200': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  },
  'Create task': {
    // NOTE: It would be nice to specify the context here
    topic: ejorpTopic('POST', '/tasks', {headers: DEFAULT_HEADERS, data: {title: 'A new task', groupKey: 22}}),
    'should get 201': function(res, body) {
      // TODO: Check that we get a task ID
      assert.equal(res.statusCode, 201);
    }
  },

// TODO: Test cases where user isn't in the right group
  'Update a task': {
    topic: ejorpTopic('PUT', '/tasks/123', {headers: DEFAULT_HEADERS, data: {title: 'Updated title'}}),
    'should get 204': function(res, body) {
      assert.equal(res.statusCode, 204);
    }
  },

  'Delete task': {
    topic: ejorpTopic('DELETE', '/tasks/123', {headers: DEFAULT_HEADERS}),
    'should get 204': function(res, body) {
      assert.equal(res.statusCode, 204);
    }
  }
});

// This batch of tests exercises some of the more interesting task operations
suite.addBatch({
  'Add checklist': {
    topic: ejorpTopic('PUT', '/tasks/123/checklist', 
                      {headers: DEFAULT_HEADERS, data: [{title: 'Item 1', done: false}, {title: 'Item 2', done: true}]}),
    'should get a 200': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  },

  'Blocked on new task': {
    topic: ejorpTopic('POST', '/tasks/123/blocked-on', {headers: DEFAULT_HEADERS, data: [{title: 'Preq 1'}]}),
    'should get a 201': function(res, body) {
      assert.equal(res.statusCode, 201);
    }
  },

  'Blocked on existing task': {
    topic: ejorpTopic('PUT', '/tasks/123/blocked-on', {headers: DEFAULT_HEADERS, data: [{taskId: '321'}]}),
    'should get a 200': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  },

  'Merge tasks': {
    topic: ejorpTopic('PUT', '/tasks/123/merge', {headers: DEFAULT_HEADERS, data: [{taskId: '321'}]}),
    'should get a 200': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  },
});


suite.export(module);
