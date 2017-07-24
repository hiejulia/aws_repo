// very simple lambda function
exports.handler = function(event, context) {
    context.succeed(event.key1);  // SUCCESS with message
};


