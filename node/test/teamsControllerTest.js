var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , TeamsController = require('../controllers/teamsController')
  , EjorpEngine = require('../lib/ejorpEngine')
  , Auth = require('../lib/auth')
  , Fs = require('fs')
  , app = require('../lib/ejorpServer').app
  , PORT = 7777
  , ejorpTopic = TestSupport.configureEjorpTopic(PORT)
;

// Set up server
TeamsController.addRoutes(app, '/');
app.listen(PORT);

var DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
  'Cookie': 'ejorp_auth=my-token; ejorp_userid=123'
};

Auth.mockLookUpUser('123', 'my-token', {userId: '123', groups: [22, 33, 44], authTokens: ['my-token']});

// Set up test suite
var suite = vows.describe('TeamsController');

suite.addBatch({
  'When creating a team...': {
    topic: ejorpTopic('POST', '/teams', {headers: DEFAULT_HEADERS, data: {name: "Ejorp Dev"}}),
    'should get a 201 response': function(res, body) {
      assert.equal(res.statusCode, 201);
    }
  }
});

suite.addBatch({
  'When inviting others to join a team...': {
    topic: ejorpTopic('POST', '/teams/789/invitation', {headers: DEFAULT_HEADERS, data: {emails: ["borvo@test.com"]}}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  }
});

suite.addBatch({
  'When accepting an invitation': {
    topic: ejorpTopic('PUT', '/teams/789/invitation/abcd454', {headers: DEFAULT_HEADERS, data: {action: 'accept'}}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  }
});

suite.addBatch({
  'When user asks to join a team': {
    topic: ejorpTopic('POST', '/teams/invitation-request', {headers: DEFAULT_HEADERS, data: {email: ["owner@test.com"]}}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  }
});

suite.addBatch({
  'When team owner approves invitation request': {
    topic: ejorpTopic('PUT', '/teams/invitation-request/tfd133', {headers: DEFAULT_HEADERS, data: {action: 'accept', teamId: 789}}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  }
  // TODO: Add reject case
});


suite.addBatch({
  'When the owner of a team deletes the team': {
    topic: ejorpTopic('DELETE', '/teams/:teamId', {headers: DEFAULT_HEADERS}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  },

  'When a user leaves the team': {
    topic: ejorpTopic('PUT', '/teams/789', {headers: DEFAULT_HEADERS, data: {action: 'leave'}}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  },

  'When a user evicts a team member': {
    topic: ejorpTopic('PUT', '/teams/789', {headers: DEFAULT_HEADERS, data: {action: 'evict', member: 345}}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  }
  
});

suite.export(module);
