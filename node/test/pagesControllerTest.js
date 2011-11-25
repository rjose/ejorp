var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , PagesController = require('../controllers/pagesController')
  , DocCache = require('../lib/docCache')
  , app = require('../lib/ejorpServer').app
  , PORT = 7777
  , ejorpTopic = TestSupport.configureEjorpTopic(PORT)
;

// TODO: Need to mock out connections to Couch and to ejorp-engine

// Set up server
PagesController.addRoutes(app, '/');
app.listen(PORT);

// TODO: Read this from a file
var document = {
  tasks: [
    {title: 'Get server up', recentActivity: [{personId: 10, picture: 'http://image.png'}], isActive: true},
    {title: 'Get server deployed', recentActivity: [{personId: 14, picture: 'http://image.png'}], isActive: false},
    {title: 'Test server', recentActivity: [{personId: 30, picture: 'http://image.png'}], isActive: false}],
  alert: {type: 'FIRST_TIME_USER'}};

DocCache.mockResponse('123#top-tasks', JSON.stringify(document));

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
