package com.chapter4;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * @author SUNIL GULABANI
 */
public class CopyObjects extends AmazonS3ClientInitializer{
	
	public CopyObjects() {
		super();
	}
	
	public static void main(String[] args) {
		CopyObjects main = new CopyObjects();
		
		String sourceBucketName = "sg-copy-object" ;
		String sourceObjectKey = "AmazonS3ClientInitializer.java";
		main.createBucket(sourceBucketName);
		main.uploadFileInBucket(sourceBucketName, "C:\\Users\\SUNIL\\workspace\\Chapter4S3\\src\\com\\chapter4\\AmazonS3ClientInitializer.java", sourceObjectKey);

		String destinationBucketName = sourceBucketName + "-copy-1";
		main.createBucket(destinationBucketName);
		String destinationObjectKey = "AmazonS3ClientInitializer-copy-1.java";
		
		main.copyObjects1(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObjectKey);
		
		destinationObjectKey = "AmazonS3ClientInitializer-copy-2.java";
		main.copyObjects2(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObjectKey);
		
		main.listAllFolderAndObjectsInBucket(destinationBucketName);
	}
	
	/**
	 * Amazon S3 provides copy object functionality where you copy objects of your amazon s3 from one place to other place.
	 * You have to define the source bucket name, source object key, destination bucket name and destination object key.
	 * Following is the api using which we can copy the object.
	 */
	public void copyObjects1(String sourceBucketName, String sourceObjectKey, String destinationBucketName, String destinationObjectKey){
		System.out.println("================ COPY OBJECT ================ ");
		try {
			s3.copyObject(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObjectKey);
		} catch (AmazonServiceException exception) {
        	exception.printStackTrace();
        } catch (AmazonClientException exception) {
        	exception.printStackTrace();
        }
	}
	
	/**
	 * We can also copy objects using the CopyObjectRequest class. This is used when we want to add metadata of newly object copied, 
	 * provide CannedAccessControlList and constraints.
	 */
	public void copyObjects2(String sourceBucketName, String sourceObjectKey, String destinationBucketName, String destinationObjectKey){
		System.out.println("================ COPY OBJECT USINg CopyObjectRequest ================ ");
		try {
			CopyObjectRequest copyObject = new CopyObjectRequest(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObjectKey);
			s3.copyObject(copyObject);
		} catch (AmazonServiceException exception) {
        	exception.printStackTrace();
        } catch (AmazonClientException exception) {
        	exception.printStackTrace();
        }
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

/**
	Output:
	
	================ Initialize AWS Credentials ================ 
	================ Initialize Amazon S3 Object ================ 
	================ Create Bucket ================
	Bucket Name: sg-copy-object

	================ Upload File In Bucket ================ 
	fileName: AmazonS3ClientInitializer.java
	================ Create Bucket ================ 
	Bucket Name: sg-copy-object-copy-1

	================ COPY OBJECT ================ 
	================ COPY OBJECT USINg CopyObjectRequest ================ 
	================ List Bucket's Folders And Objects ================ 
		- AmazonS3ClientInitializer-copy-1.java  (size = 1402)
		- AmazonS3ClientInitializer-copy-2.java  (size = 1402)
*/