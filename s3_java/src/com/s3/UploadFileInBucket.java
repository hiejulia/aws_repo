package com.chapter3;

import java.io.File;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * @author SUNIL GULABANI
 */
public class UploadFileInBucket extends AmazonS3ClientInitializer{
	public UploadFileInBucket() {
		super();
	}
	
	public static void main(String[] args) {
		String bucketName = "sg-bucket-2015-using-java-api-" + new Date().getTime();
		
		UploadFileInBucket object = new UploadFileInBucket();
		object.createBucket(bucketName);
		
		object.uploadFileInBucket(bucketName,"C:\\admin.ico","admin.ico");
		object.listAllFolderAndObjectsInBucket(bucketName);
	}
	
	public void uploadFileInBucket(String bucketName, String filePath, String fileName){
		System.out.println("================ Upload File In Bucket ================ ");
		// upload file to folder and set it to public
		System.out.println("fileName: " + fileName);
		s3.putObject(new PutObjectRequest(
								bucketName,
								fileName,
								new File(filePath))
							.withCannedAcl(CannedAccessControlList.PublicRead));
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
	
	public void listAllFolderAndObjectsInBucket(String bucketName){
		System.out.println("================ List Bucket's Folders And Objects ================ ");
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	    												.withBucketName(bucketName);
		ObjectListing objectListing;

		do {
			objectListing = s3.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				System.out.println( "\t- " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());
	}
}
