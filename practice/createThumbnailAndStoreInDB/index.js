//create thumbnail and store in db 
//dependencies
var async = require('async');
var AWS = require('aws-sdk');
var gm = require('gm')
            .subClass({ imageMagick: true }); // Enable ImageMagick integration.
var util = require('util');


//constants > default size of the thumbnail
var DEFAULT_MAX_WIDTH  = 200;
var DEFAULT_MAX_HEIGHT = 200;
var DDB_TABLE = 'images';
//get ref to aws s3
var s3 = new AWS.S3();
var dynamodb = new AWS.DynamoDB();

//get image type
function getImageType(key,callback) {
    //type match 
    var typeMatch = key.match(/\.([^.]*)$/);
  if (!typeMatch) {
      callback("Could not determine the image type for key: ${key}");
      return;
  }
  var imageType = typeMatch[1];
  //img type jpg and png
  if (imageType != "jpg" && imageType != "png") {
      callback('Unsupported image type: ${imageType}');
      return;
  }
  return imageType;
}

//main lambda function goes here
exports.handler= (event, context,callback) => {
    //read options from the event
    var srcBucket = event.Records[0].s3.bucket.name;
  var srcKey    = event.Records[0].s3.object.key;
  var dstBucket = srcBucket;
  var dstKey    = "thumbs/" + srcKey;
    //src key
    //get the image type : 
     var imageType = getImageType(srcKey, callback);

     //download the image from s3 > transform and uplaod to different s3 bucket
     // and write the metadata to dynamodb
     async.waterfall([
        //function download image 
        function downloadImage(next){
            //down image from s3 buckeet
            s3.getObject({
                Bucket:srcBucket,
                Key:srcKey
            },next);
        },
        //transform image
        function transformImage(response,text){
            gm(responseBody).size(function(err,size){
                var metadata = response.Metadata;
                var max_width;
        if ('width' in metadata) {
          max_width = metadata.width;
        } else {
          max_width = DEFAULT_MAX_WIDTH;
        }
        var max_height;
        if ('height' in metadata) {
          max_height = metadata.height;
        } else {
          max_height = DEFAULT_MAX_HEIGHT;
        }

        //infer the scaling factor to avoid stretching the image unnaturally
        var scalingFactor = Math.min(
          max_width / size.width,
          max_height / size.height
        );
        var width  = scalingFactor * size.width;
        var height = scalingFactor * size.height;
        //transform the image buffer in memory
        this.resize(width, height)
          .toBuffer(imageType, function(err, buffer) {
            if (err) {
              next(err);
            } else {
              next(null, response.ContentType, metadata, buffer);
            }
          });
    


            });
        },

        //upload thumbnail
        function uploadThumbnail(contentType, metadata, data, next){
            //stream teh transform image to a different s3 bucket
            s3.putObject({
                Bucket: dstBucket,
          Key: dstKey,
          Body: data,
          ContentType: contentType,
          Metadata: metadata
            },function(err,buffer){
                if(err){
                    next(err)
                } else {
                    next(null, metadata)
                }
            })
        },
        //store metadata in dynamodb 
        function storeMetadata(metadata,next){
            var params = {
                //tablename and item
                TableName: DDB_TABLE,
        Item: {
          name: { S: srcKey },
          thumbnail: { S: dstKey },
          timestamp: { S: (new Date().toJSON()).toString() },
            }
        };
        if ('author' in metadata) {
        params.Item.author = { S: metadata.author };
      }
      if ('title' in metadata) {
        params.Item.title = { S: metadata.title };
      }
      if ('description' in metadata) {
        params.Item.description = { S: metadata.description };
      }
      dynamodb.putItem(params, next);


        }



     ],function(err){
         if(err){
             console.log(err)
         } else {
             console.log('ok')
         }
            //callback
            callback();
     })
}






