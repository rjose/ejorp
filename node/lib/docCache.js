function getDocument(documentId, callback) {
  // TODO: Make the real call or read this info from file
  var document = {
    tasks: [
      {title: 'Get server up', recentActivity: [{personId: 10, picture: 'http://image.png'}], isActive: true},
      {title: 'Get server deployed', recentActivity: [{personId: 14, picture: 'http://image.png'}], isActive: false},
      {title: 'Test server', recentActivity: [{personId: 30, picture: 'http://image.png'}], isActive: false}],
    alert: {type: 'FIRST_TIME_USER'}};
  
  callback(null, document);
  return;
}

var interface = {
  getDocument: getDocument
};

module.exports = interface;

