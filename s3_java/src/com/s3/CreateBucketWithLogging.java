package com.chapter3;

import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.BucketLoggingConfiguration;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.SetBucketLoggingConfigurationRequest;
/**
 * @author SUNIL GULABANI
 */
public class CreateBucketWithLogging extends AmazonS3ClientInitializer{
	public CreateBucketWithLogging() {
		super();
	}
	
	public static void main(String[] args) {
		String bucketName = "sg-bucket-2015-using-java-api-" + new Date().getTime() + "-with-logging";
		
		CreateBucketWithLogging object = new CreateBucketWithLogging();
		object.createBucketWithLogging(bucketName);
	}
	
	public void createBucketWithLogging(String bucketName){
		System.out.println("================ Create Bucket With Logging================ ");
        try {
            System.out.println("Bucket Name: " + bucketName + "\n");
            s3.createBucket(bucketName);
            
            
            AccessControlList acl = s3.getBucketAcl("sg-logs-bucket");
            acl.grantPermission(GroupGrantee.LogDelivery, Permission.FullControl);
            /*acl.grantPermission(GroupGrantee.LogDelivery, Permission.Write);
            acl.grantPermission(GroupGrantee.LogDelivery, Permission.WriteAcp);
            acl.grantPermission(GroupGrantee.LogDelivery, Permission.Read);
            acl.grantPermission(GroupGrantee.LogDelivery, Permission.ReadAcp);*/

            s3.setBucketAcl("sg-logs-bucket", acl);

            BucketLoggingConfiguration configuration = new BucketLoggingConfiguration("sg-logs-bucket", "sg-logs-");
            SetBucketLoggingConfigurationRequest request = new SetBucketLoggingConfigurationRequest(bucketName, configuration);
            s3.setBucketLoggingConfiguration(request);
            
        } catch (AmazonServiceException exception) {
        	exception.printStackTrace();
        } catch (AmazonClientException exception) {
        	exception.printStackTrace();
        }
	}
}
