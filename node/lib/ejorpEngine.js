var m_mockResponses = null;

function generateAuthToken(memberId, loginHash, callback) {
 if (m_mockResponses && m_mockResponses['generateAuthToken']) {
  callback(null, m_mockResponses['generateAuthToken']);
  return;
 }
 // TODO: Make call to ejorp engine if not mocked 
}

function mockResponse(funcName, response) {
  if (!m_mockResponses) {
    m_mockResponses = {};
  }
  m_mockResponses[funcName] = response;
}

var interface = {
  generateAuthToken: generateAuthToken,
  mockResponse: mockResponse
};

module.exports = interface;
