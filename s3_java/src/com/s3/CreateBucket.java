package com.chapter3;

import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.Bucket;
/**
 * @author SUNIL GULABANI
 */
public class CreateBucket  extends AmazonS3ClientInitializer{
	public CreateBucket() {
		super();
	}
	
	public static void main(String[] args) {
		String bucketName = "sg-bucket-2015-using-java-api-" + new Date().getTime();
		
		CreateBucket object = new CreateBucket();
		object.createBucket(bucketName);
		object.listBucket();
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
	
	public void listBucket(){
		System.out.println("================ Listing Buckets ================ ");
        for (Bucket bucket : s3.listBuckets()) {
            System.out.println(" - " + bucket.getName());
        }
	}
}
