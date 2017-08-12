package com.chapter3;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * @author SUNIL GULABANI
 */
public class CreateFolderInBucket extends AmazonS3ClientInitializer{
	public CreateFolderInBucket() {
		super();
	}
	
	public static void main(String[] args) {
		String bucketName = "sg-bucket-2015-using-java-api-" + new Date().getTime();
		String folderName = "sg-folder-" + new Date().getTime();
		
		CreateFolderInBucket object = new CreateFolderInBucket();
		object.createBucket(bucketName);
		
		object.createFolderInBucket(bucketName, folderName);
		
		object.listFolder(bucketName, "sg-folder-");
	}
	
	public void createFolderInBucket(String bucketName, String folderName){
		System.out.println("================ Create Folder In Bucket ================ ");
		System.out.println("Folder Name: " + folderName);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + "/", emptyContent, metadata);

		s3.putObject(putObjectRequest);
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
	
	public void listFolder(String bucketName, String prefix){
		System.out.println("================ List Bucket with Prefix ================ ");
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	    												.withBucketName(bucketName)
	    												.withPrefix(prefix);
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
