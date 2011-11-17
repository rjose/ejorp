/*
 * GET home page.
 */
var http = require('http');


exports.index = function(req, res){
  var options = {
    host: '127.0.0.1',
    port: 5984,
    path: '/hello-world/123',
    method: 'GET'
  };

  var request = http.request(options, function(myres) {
    var body = '';
    console.log('STATUS: ' + myres.statusCode);
    myres.setEncoding('utf8');
    
    myres.on('data', function(chunk) {
      console.log('BODY: ' + chunk);
      body = body + chunk;
    })
  
    myres.on('end', function() {
      res.render('index', { title: 'Express', body: body })
    });
  });

  request.end();

  //http://127.0.0.1:5984/hello-world/123
};
