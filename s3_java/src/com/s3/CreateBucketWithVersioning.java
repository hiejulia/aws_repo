package com.chapter3;

import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;
/**
 * @author SUNIL GULABANI
 */
public class CreateBucketWithVersioning extends AmazonS3ClientInitializer {
	public CreateBucketWithVersioning() {
		super();
	}
	
	public static void main(String[] args) {
		String bucketName = "sg-bucket-2015-" + new Date().getTime() + "-with-versioning";
		
		CreateBucketWithVersioning object = new CreateBucketWithVersioning();
		object.createBucketWithVersioning(bucketName);
	}
	
	public void createBucketWithVersioning(String bucketName){
		System.out.println("================ Create Bucket with Versioning ================ ");
        try {
            System.out.println("Bucket Name: " + bucketName + "\n");
            s3.createBucket(bucketName);
            
            BucketVersioningConfiguration configuration = new BucketVersioningConfiguration(BucketVersioningConfiguration.ENABLED);
            SetBucketVersioningConfigurationRequest request = new SetBucketVersioningConfigurationRequest(bucketName, configuration);
            s3.setBucketVersioningConfiguration(request);
        } catch (AmazonServiceException exception) {
        	exception.printStackTrace();
        } catch (AmazonClientException exception) {
        	exception.printStackTrace();
        }
	}
}
