var Winston = require('winston');

var nodeEnv = process.env.NODE_ENV || 'development';
var logger = null;

switch(nodeEnv) {
  case "development":
    logger = new (Winston.Logger)({
      transports: [
        new (Winston.transports.Console)(),
        new (Winston.transports.File)({filename: './log/ejorp-node.log'})
      ]
    });
    break;

  default:
    logger = new (Winston.Logger)({
      transports: [
        new (Winston.transports.Console)()
      ]
    });
    break;
}

module.exports = logger;
