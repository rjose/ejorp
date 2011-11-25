var Redis = require("redis")
  , Logger = require('./logger')
;

var m_client = Redis.createClient();
var m_mockedResponses = null;

m_client.on("error", function (err) {
  Logger.log("fatal", err);
});


function getDocument(documentId, callback) {
  if (m_mockedResponses) { 
    callback(null, JSON.parse(m_mockedResponses[documentId]));
    return;
  } 
  else {
    m_client.get(documentId, function(err, docString) {
      if(err) {
        // TODO: Handle error
      }
      callback(null, JSON.parse(docString));
    });
    return;
  }
}

// This is used to mock out responses from the DocCache for testing
function mockResponse(key, value) {
  if (!m_mockedResponses) {
    m_mockedResponses = {};
  }
  m_mockedResponses[key] = value;
}

var interface = {
  getDocument: getDocument,
  mockResponse: mockResponse
};

module.exports = interface;

