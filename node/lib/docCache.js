var Redis = require("redis");

m_client = Redis.createClient();
// TODO: Handle redis errors

function getDocument(documentId, callback) {
  // TODO: Figure out how to stub this out
  m_client.get(documentId, function(err, docString) {
    if(err) {
      // TODO: Handle error
    }
    callback(null, JSON.parse(docString));
  });

  //var document = {
    //tasks: [
      //{title: 'Get server up', recentActivity: [{personId: 10, picture: 'http://image.png'}], isActive: true},
      //{title: 'Get server deployed', recentActivity: [{personId: 14, picture: 'http://image.png'}], isActive: false},
      //{title: 'Test server', recentActivity: [{personId: 30, picture: 'http://image.png'}], isActive: false}],
    //alert: {type: 'FIRST_TIME_USER'}};
  //m_client.set(documentId, JSON.stringify(document));
  
  //callback(null, document);
  return;
}

var interface = {
  getDocument: getDocument
};

module.exports = interface;

