package com.chapter3;

import java.util.List;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * @author SUNIL GULABANI
 */
public class DeleteOperations extends AmazonS3ClientInitializer{
	
	public DeleteOperations() {
		super();
	}
	
	public static void main(String[] args) {
		DeleteOperations object = new DeleteOperations();

		object.deleteFolderAndItsObjects("sg-bucket-2015", "sg-folder");
		object.deleteObjectInBucket("sg-bucket-2015","admin.ico");
		object.deleteBucketObjects("sg-bucket-2015");
		object.deleteBucket("sg-bucket-2015");
	}
	
	public void deleteObjectInBucket(String bucketName, String fileKey){
		System.out.println("================ Delete Object In Bucket ================ ");
		System.out.println(" - bucket: " + bucketName);
		System.out.println("\t- fileKey: " + fileKey);
		
		s3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
	}
	
	public void deleteBucket(String bucketName){
		System.out.println("================ Delete Bucket ================ ");
		System.out.println(" - bucket: " + bucketName);

		s3.deleteBucket(bucketName);
	}
	
	public void deleteBucketObjects(String bucketName){
		System.out.println("================ Delete Bucket Object ================ ");
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName);
		ObjectListing objectListing;
		System.out.println(" - " + bucketName);
		do {
			objectListing = s3.listObjects(listObjectsRequest);
        	for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        		System.out.println("\t- " + objectSummary.getKey());
        		s3.deleteObject(bucketName, objectSummary.getKey());
        	}
        	listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());
	}

	public void deleteFolderAndItsObjects(String bucketName, String folderName) {
		System.out.println("================ Delete Bucket Folder and it's Object ================ ");
		System.out.println(" - " + bucketName);
		System.out.println("\t- " + folderName);
		List<S3ObjectSummary> fileList = s3.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary objectSummary : fileList) {
			System.out.println("\t\t- " + objectSummary.getKey());
			s3.deleteObject(bucketName, objectSummary.getKey());
		}
		s3.deleteObject(bucketName, folderName);
	}
}