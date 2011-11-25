var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , PagesController = require('../controllers/pagesController')
  , DocCache = require('../lib/docCache')
  , Fs = require('fs')
  , app = require('../lib/ejorpServer').app
  , PORT = 7777
  , ejorpTopic = TestSupport.configureEjorpTopic(PORT)
;

// Set up server
PagesController.addRoutes(app, '/');
app.listen(PORT);

// Mock responses
DocCache.mockResponse('123#top-tasks', Fs.readFileSync('./test/data/top-tasks-1.json', 'utf-8'));

// Set up test suite
var suite = vows.describe('PagesController');

suite.addBatch({
  'When getting top tasks': {
    topic: ejorpTopic('GET', '/pages/top-tasks', {headers: {'Cookie': 'ejorp_auth=my-token'}}),

    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    },

    'should get a list of top tasks': function(res, body) {
      var data = JSON.parse(body);
      assert.equal(3, data.tasks.length);
    },

    'should have one active task': function(res, body) {
      var tasks = JSON.parse(body).tasks;
      var numActives = tasks.reduce(function(currentNumActive, task) {return currentNumActive + task.isActive ? 1 : 0}, 0)
      assert.equal(1, numActives);
    },

    'should get an alert': function(res, body) {
      var data = JSON.parse(body);
      assert.equal('FIRST_TIME_USER', data.alert.type);
    },
  },
});

suite.export(module);
