package com.chapter3;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * @author SUNIL GULABANI
 */
public class ListFullHierarchy extends AmazonS3ClientInitializer{
	public ListFullHierarchy() {
		super();
	}
	
	public static void main(String[] args) {
		new CreateBucket().createBucket("sg-bucket-2015");
		new CreateBucketWithLogging().createBucketWithLogging("sg-bucket-2015-with-logging");
		new CreateBucketWithVersioning().createBucketWithVersioning("sg-bucket-2015-with-versioning");
		new CreateFolderInBucket().createFolderInBucket("sg-bucket-2015", "sg-folder");
		new UploadFileInBucket().uploadFileInBucket("sg-bucket-2015","C:\\admin.ico","admin.ico");
		new UploadFileInFolder().uploadFileInFolder("sg-bucket-2015", "sg-folder","C:\\admin.ico","admin.ico");

		ListFullHierarchy object = new ListFullHierarchy();
		object.listFullHierarchy();
	}
	
	public void listFullHierarchy(){
		System.out.println("================ Listing Full Hierarchy ================ ");
        for (Bucket bucket : s3.listBuckets()) {
            System.out.println(" - " + bucket.getName());
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucket.getName());
            ObjectListing objectListing;
            do {
            	objectListing = s3.listObjects(listObjectsRequest);
            	for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            		System.out.println( "\t\t- " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
            	}
            	listObjectsRequest.setMarker(objectListing.getNextMarker());
            } while (objectListing.isTruncated());
        }
	}
}
