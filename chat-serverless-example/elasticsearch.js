var AWS = require('aws-sdk');
var path = require('path');

//config aws 
//....



//post the given doc to elasticsearch
function postToES(doc, context) {
    var req = new AWS.HttpRequest(endpoint);

    console.log('create post request');
    req.method = 'POST';
    req.path = path.join('/', esDomain.index, esDomain.doctype);
    req.region = esDomain.region;
    req.headers['presigned-expires'] = false;
    req.headers['Host'] = endpoint.host;
    req.body = doc;

    console.log('Creating the Signer for the post request');
    var signer = new AWS.Signers.V4(req , 'es');  // es: service code
    signer.addAuthorization(creds, new Date());

    console.log('Sending Data');
    var send = new AWS.NodeHttpClient();
    send.handleRequest(req, null, function(httpResp) {
        var respBody = '';
        httpResp.on('data', function (chunk) {
            respBody += chunk;
        });
        httpResp.on('end', function (chunk) {
            console.log('Response: ' + respBody);
            context.succeed('Lambda added document ' + doc);
        });
    }, function(err) {
        console.log('Error: ' + err);
        context.fail('Lambda failed with error ' + err);
    });
}



//main lambda function 
exports.handler = function(event, context) {
    
        console.log(JSON.stringify(event, null, '  '));
        event.Records.forEach(function(record) {
            if (typeof record.dynamodb.NewImage != 'undefined')
             {
                var doc = {message: {
                    name: record.dynamodb.NewImage.name.S,
                    message: record.dynamodb.NewImage.message.S,
                    channel: record.dynamodb.NewImage.channel.S,
                    timestamp: record.dynamodb.NewImage.timestamp.N}
                }
                console.log('document posted to ElasticSearch: ' + JSON.stringify(doc))
                postToES(JSON.stringify(doc), context);
             }
             else
             {
                 console.log('skipping non-inserts');
             }
        });
}