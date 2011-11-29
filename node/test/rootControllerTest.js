var vows = require('vows')
  , http = require('http')
  , assert = require('assert')
  , TestSupport = require('./support/testSupport')
  , RootController = require('../controllers/rootController')
  , EjorpEngine = require('../lib/ejorpEngine')
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
    topic: ejorpTopic('GET', '/', {}),
    'should get a 200 response': function(res, body) {
      assert.equal(res.statusCode, 200);
    }
  }
});

// Stub out ejorp engine to return a token for this call
EjorpEngine.mockResponse('generateAuthToken', 'abracadabra');

suite.addBatch({
  'Can sign in': {
    topic: ejorpTopic('POST', '/signin', {headers: {'Content-Type': 'application/json'}, data: {memberId: 'abc', loginHash: '123'}}),
    'should get a 201 response': function(res, body) {
      assert.equal(res.statusCode, 201);
    },

    'should have auth cookies set': function(res, body) {
      assert.deepEqual([ 'ejorp_auth=abracadabra', 'ejorp_userid=abc' ], res.headers['set-cookie']);
    }

  }
});

suite.export(module);
