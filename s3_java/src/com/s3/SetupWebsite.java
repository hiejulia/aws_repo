package com.chapter5;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * @author SUNIL GULABANI
 */
public class SetupWebsite extends AmazonS3ClientInitializer{
	
	public SetupWebsite() {
		super();
	}
	
	public static void main(String[] args) {
		SetupWebsite mainApp = new SetupWebsite();
		String bucketName = "sg-website";
		mainApp.createBucket(bucketName);
		
		String indexHTML = "index.html" ;
		String errorHTML = "error.html" ;
		mainApp.uploadFileInBucket(bucketName, "index.html", indexHTML);
		mainApp.uploadFileInBucket(bucketName, "error.html", errorHTML);
		
		mainApp.getConfiguration(bucketName);
		mainApp.setConfiguration(bucketName, indexHTML, errorHTML);
		mainApp.getConfiguration(bucketName);
//		mainApp.deleteConfiguration(bucketName);
//		mainApp.deleteBucket(bucketName);
	}
	
	public BucketWebsiteConfiguration getConfiguration(String bucketName){
		System.out.println("================ Get Bucket Website Configuration ================ ");
		BucketWebsiteConfiguration configuration = s3.getBucketWebsiteConfiguration(bucketName);
		if(configuration!=null){
			System.out.println("Index Document Suffix: " + configuration.getIndexDocumentSuffix());
			System.out.println("Error Document: " + configuration.getErrorDocument());
		}else{
			System.out.println("No configuration found for this bucket: " + bucketName);
		}
		return configuration;
	}
	
	public void setConfiguration(String bucketName, String indexHTML, String errorHTML){
		System.out.println("================ Set Bucket Website Configuration ================ ");
		System.out.println("Bucket Name: " + bucketName);
		System.out.println("Index HTML" + indexHTML);
		System.out.println("Error HTML" + errorHTML);
		s3.setBucketWebsiteConfiguration(bucketName,new BucketWebsiteConfiguration(indexHTML, errorHTML));
	}
	
	public void deleteConfiguration(String bucketName){
		System.out.println("================ Delete Bucket Website Configuration ================ ");
		s3.deleteBucketWebsiteConfiguration(bucketName);
	}
	
	public void createBucket(String bucketName){
		System.out.println("================ Create Bucket ================ ");
        try {
            System.out.println("Bucket Name: " + bucketName + "\n");
            s3.createBucket(bucketName);
        } catch (AmazonServiceException exception) {
        	exception.printStackTrace();
        } catch (AmazonClientException exception) {
        	exception.printStackTrace();
        }
	}

	public void deleteBucket(String bucketName){
		System.out.println("================ Delete Bucket ================ ");
		System.out.println(" - bucket: " + bucketName);
		s3.deleteBucket(bucketName);
	}
	
	public void uploadFileInBucket(String bucketName, String filePath, String fileName){
		System.out.println("================ Upload File In Bucket ================ ");
		System.out.println("fileName: " + fileName);
		s3.putObject(new PutObjectRequest(
								bucketName,
								fileName,
								new File(filePath))
							.withCannedAcl(CannedAccessControlList.PublicRead));
	}
}
/**
OUTPUT:

================ Initialize AWS Credentials ================ 
================ Initialize Amazon S3 Object ================ 
================ Create Bucket ================ 
Bucket Name: sg-website

================ Upload File In Bucket ================ 
fileName: index.html
================ Upload File In Bucket ================ 
fileName: error.html
================ Get Bucket Website Configuration ================ 
No configuration found for this bucket: sg-website
================ Set Bucket Website Configuration ================ 
Bucket Name: sg-website
Index HTMLindex.html
Error HTMLerror.html
================ Get Bucket Website Configuration ================ 
Index Document Suffix: index.html
Error Document: error.html

*/