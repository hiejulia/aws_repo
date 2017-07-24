var aws = require('aws-sdk');
var ddb;
var querystring = require('querystring');

var theContext;
var message;
var from;
var numMedia;
var mediaURL;
var timestamp;
var channel = 'default';



//init
function init(context){
    if(!ddb){
         var stackName = context.functionName.split('-z0mb1es-')[0];
        var stackRegion = context.functionName.split('-WriteMessagesToDynamoDB-')[1];
        ddb = new aws.DynamoDB({
        region: stackRegion,
        params: { TableName: stackName + "-messages" }
        });
    }
}

//dynamodb put
function dynamodbPut(params){
    ddb.putItem(params,dynamodbCallback)
}

//dynamodb callback
function dynamodbCallback(err, res){
    if(err){
        return theContext.fail(err)
    } else {
        return theContext.succeed()
    }
}
//lambda function 
exports.handler= function(event,context){
    init(context)
    theContext=context   
    if(event.message == null || event.message == 'null' || event.name == null || event.name == 'null') {
        return context.fail("Message and Name cannot be null");
    } else {
        message = event.message;
        from = event.name;
    }

    if (event.timestamp == null || event.timestamp == 'null') {
        event.timestamp = "" + new Date().getTime();
        timestamp = event.timestamp;
    }

    /**
     * For Debubugging input params to the lambda function
    console.log('Message: ' + message);
    console.log('Sender: ' + from);
    console.log('Channel: ' + channel);
    */

    var DDBparams = {
        "Item": {
            "channel":{"S":channel},
            "message":{"S":message},
            "timestamp":{"N":timestamp},
            "name":{"S":from}
        }
    };

    dynamoPut(DDBparams);
}