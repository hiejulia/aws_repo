#!/bin/bash

# bash script that will zip up a deployment package and upload a lambda function 
# to a s3 bucket and the script will then deploy the function to lambda 
# this is node version

echo -n "Enter the name of the file to zip"
read FilesToBeZipped
echo -n "Enter the name of the output zip file "
read ZipFileName
echo -n "Enter the name of the s3 bucket to upload"
read BucketName
echo -n "Enter the name of lambda funcc"
read FunctionName
echo -n "Enter description of lambda fucn"
read description
echo -n "Enter the ARN of the role you wish to implement"
read Role