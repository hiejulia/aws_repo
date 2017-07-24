'use strict'

var AWS = require('aws-sdk')
var DOC  = require('dynamodb-doc')
var dynamo = new DOC.DynamoDB()
//start lambda function here
exports.handler= function(event,context){

    var callback  = function(err,context){
        if(err){
            context.done('error',null);
        } else {
            if(data.Item && data.Item.users){
                context.done(null, data.Item.users)
            } else {
                context.done(null,{})
            }
        }

    };

    dynamo.getItem({TableName:"Users",Key:{username:"default"}},callback)
    


}
//get Users table from dynamobd
