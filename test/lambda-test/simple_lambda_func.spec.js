//lambda test 
//mock

//import aws lambda mock context and tape
var context = require('aws-lambda-mock-context');
var test = require('tape');

var lambdaToTest = require('./simple_lambda_func');
//create context object
var ctx= context()
//test event object
var testEvent = {
    key1:'name'
}

var response = null
var error = null;


//test case
//test capture response
test("capture response",t => {
    lambdaToTest.handler(testEvent,ctx)
    //capture the res or errors
    ctx.Promise
        .then(res => {
            response = res   
            t.end()
        })
        .catch(err => {
            error = err;
            t.end()  
        })
})

test("check response",t => {
    t.equals(res,'name')
    t.end();
})