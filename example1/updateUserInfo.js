'use strict'
//2nd lambda function to update 
var AWS = require('aws-sdk');
var DOC = require('dynamodb-doc');
var dynamo = new DOC.DynamoDB();

exports.handler = function(event, context) {
  var item = { username:"default",
               users: event.users || {}
          };

  var callback = function(err, data) {
    if (err) {
      console.log(err);
      context.fail('unable to update users at this time');
    } else {
      console.log(data);
      context.done(null, data);
    }
  };

  //put item to dynamodb

  dynamo.putItem({TableName:"Users", Item:item}, callback);
};