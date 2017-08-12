//....
//sample of gulp file 
gulp.task('upload-to-s3', function () {
  var s3 = new AWS.S3();
  var zipFilePath = './' + outputName;
  getZipFile(function (data) {
    var params = {
      Bucket: 'lambda-function-container',
      Key: zipFilePath,
      Body: data
    };
    s3.putObject(params, function(err, data) {
      if (err) console.log('Object upload unsuccessful!');
      else console.log('Object ' + outputName + ' was uploaded!');
    });
  });
  function getZipFile (next) {
    fs.readFile(zipFilePath, function (err, data) {
          if (err) console.log(err);
          else {
            next(data);
          }
    });
  }
});