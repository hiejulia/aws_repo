// lambda func that return some text after it's been invoked through SNS topic

exports.handle = function(event,context,callback){
    const message = JSON.parse(event.Records[0].Sns.Message) 
    const text = message.content.text 
    //check if the text exists
    if(text && text.length){
        return callback(null, `Here is the message text from SNS topic ${text}`)
    }  else {
        //if no text was found return 
        return callback(null,`No text was found`)
    }
}