var aws = require('aws-sdk');
var ddb;

var theContext;

//dynamo callback
function dynamoCallback(err,res){
    if(err){
        theContext.fail(err)
    } else {
        theContext.succeed(res)
    }
}

//init
function init(context){
    if(!ddb){
        var stackName = context.functionName.split('-z0mb1es-')[0];
        var stackRegion = context.functionName.split('-GetMessagesFromDynamoDB-')[1];
        //config dynamodb
        ddb = new aws.DynamoDB({
            region:stackRegion,
            params:{TableName:stackName}
        })
    }
}

//lamdba function
exports.handler = function(event,context){
    init(context)
    theContext=context
    var params= {
         "KeyConditions": {
            "channel": {
                "AttributeValueList": [{
                    "S": "default"
                }],
                "ComparisonOperator": "EQ"
            }
        },
        "Limit": 20,
            "ScanIndexForward":false
    }
        var response = ddb.query(params,dynamoCallback)
}