package com.chapter4;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
/**
 * @author SUNIL GULABANI
 */
public abstract class AmazonS3ClientInitializer {
	protected AWSCredentials credentials = null;
	protected AmazonS3 s3 ;
	
	public AmazonS3ClientInitializer() {
		initializeAWSCredentials();
		initializeAmazonS3Object();
	}
	
	public void initializeAWSCredentials(){
		System.out.println("================ Initialize AWS Credentials ================ ");
		credentials = new BasicAWSCredentials("YOUR_ACCESS_ID", "YOUR_SECRET_KEY");
		
		//Path:		
		//		LINUX:		/home/sunil/.aws/credentials
		//		WINDOWS:	C:\Users\SUNIL\.aws\credentials
		//credentials = new ProfileCredentialsProvider("sunilgulabani").getCredentials();
	}

	public void initializeAmazonS3Object(){
		System.out.println("================ Initialize Amazon S3 Object ================ ");
		s3 = new AmazonS3Client(credentials);
        Region region = Region.getRegion(Regions.AP_SOUTHEAST_1);
        s3.setRegion(region);
        s3.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
	}
}