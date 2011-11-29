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

Auth.mockLookUpUser('123', 'my-token', {groups: [22, 33, 44], authTokens: ['my-token']});

var DEFAULT_HEADERS = {'Content-Type': 'application/json',
  'Cookie': 'ejorp_auth=my-token; ejorp_userid=123'};

// Set up test suite
var suite = vows.describe('TasksController');

// The first batch of tests exercises basic CRUD operations
suite.addBatch({
  'Get task': {
    topic: ejorpTopic('GET', '/tasks/123', {}),

    'should get 200': function(res, body) {
      console.log('Get task=====> ' + body);
      assert.equal(res.statusCode, 200);
    }
  },
  'Create task': {
    // NOTE: It would be nice to specify the context here
    topic: ejorpTopic('POST', '/tasks', {headers: DEFAULT_HEADERS, data: {title: 'A new task', groupKey: 22}}),
    'should get 201': function(res, body) {
      console.log('Create task=====> ' + body);
      // TODO: Check that we get a task ID
      assert.equal(res.statusCode, 201);
    }
  },

  'Update a task': {
    topic: ejorpTopic('PUT', '/tasks/123', {data: {title: 'Updated title'}}),
    'should get 204': function(res, body) {
      console.log('Update task=====>', body);
      assert.equal(res.statusCode, 204);
    }
  },

  'Delete task': {
    topic: ejorpTopic('DELETE', '/tasks/123', {}),
    'should get 204': function(res, body) {
      console.log('Delete task=====>' + body);
      assert.equal(res.statusCode, 204);
    }
  }
});

// This batch of tests exercises some of the more interesting task operations
suite.addBatch({
  'Add checklist': {
    topic: ejorpTopic('PUT', '/tasks/123/checklist', 
                      {data: [{title: 'Item 1', done: false}, {title: 'Item 2', done: true}]}),
    'should get a 200': function(res, body) {
      console.log('Add checklist=====>' + body);
      assert.equal(res.statusCode, 200);
    }
  },

  'Blocked on new task': {
    topic: ejorpTopic('POST', '/tasks/123/blocked-on', {data: [{title: 'Preq 1'}]}),
    'should get a 201': function(res, body) {
      console.log('Blocked on task=====>' + body);
      assert.equal(res.statusCode, 201);
    }
  },

  'Blocked on existing task': {
    topic: ejorpTopic('PUT', '/tasks/123/blocked-on', {data: [{taskId: '321'}]}),
    'should get a 200': function(res, body) {
      console.log('Blocked on=====> ' + body);
      assert.equal(res.statusCode, 200);
    }
  },

  'Merge tasks': {
    topic: ejorpTopic('PUT', '/tasks/123/merge', {data: [{taskId: '321'}]}),
    'should get a 200': function(res, body) {
      console.log('Merge task=====> ' + body);
      assert.equal(res.statusCode, 200);
    }
  },
});


suite.export(module);
