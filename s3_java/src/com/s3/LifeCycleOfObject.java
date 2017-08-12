package com.chapter4;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration.Rule;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration.Transition;
import com.amazonaws.services.s3.model.StorageClass;
/**
 * @author SUNIL GULABANI
 */
public class LifeCycleOfObject extends AmazonS3ClientInitializer{
	public LifeCycleOfObject(){
		super();
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
	}
	
	public static void main(String[] args) {
		LifeCycleOfObject main = new LifeCycleOfObject();
		String bucketName = "sg-lifecycle";
		main.createBucket(bucketName);
		main.addBucketLifeCycleConfiguration(bucketName);
		main.getBucketLifecycleConfiguration(bucketName);
		/*main.deleteBucketLifecycleConfiguration(bucketName);
		main.getBucketLifecycleConfiguration(bucketName);*/

//		main.deleteBucket(bucketName);
	}
	
	public void addBucketLifeCycleConfiguration(String bucketName){
		System.out.println("================ ADD BUCKET LIFECYCLE CONFIGURATION ================ ");
		
		Rule archiveRule = createRule("Archive in 0 days","archived/",0);

        List<Rule> rulesList = new ArrayList<Rule>();
        rulesList.add(archiveRule);

        BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration();
        configuration.withRules(rulesList);
        
        // Save configuration.
        s3.setBucketLifecycleConfiguration(bucketName, configuration);		
	}
	
	public void getBucketLifecycleConfiguration(String bucketName){
		System.out.println("================ GET BUCKET LIFECYCLE CONFIGURATION ================ ");
		BucketLifecycleConfiguration configuration = s3.getBucketLifecycleConfiguration(bucketName);
		if(configuration!=null){
			List<Rule> rulesList = configuration.getRules();
			for(Rule rule : rulesList){
				System.out.println("Id: " + rule.getId());
				System.out.println("Prefix: " + rule.getPrefix());
				System.out.println("Status: " + rule.getStatus());
			}
		}else{
			System.out.println("No Bucket Life Cycle Configuration Found.");
		}
	}
	
	public void deleteBucketLifecycleConfiguration(String bucketName){
		System.out.println("================ DELETE BUCKET LIFECYCLE CONFIGURATION ================ ");
		s3.deleteBucketLifecycleConfiguration(bucketName);
	}
	
	/**
	 *	Bucket Life Cycle configuration requires rules to be applied. Multiple rules can be applied on the bucket.
	 *	Rules can be formed with several parameters and depending on the requirements.
	 *
	 *	We can also provide expirationDate or expirationInDays to form a rule. 
	 * 
	 *		rule.withExpirationDate(expirationDate);
	 *
	 *		rule.withExpirationInDays(expirationInDays);
	 */
	private Rule createRule(String id, String prefix, int days){
		Rule rule = new Rule();
		rule.withId(id);
		rule.withPrefix(prefix);
		
		/**
		 * Transition is to archive objects into Amazon Glacier.
		 */
		rule.withTransition(new Transition()
								.withDays(days)
								.withStorageClass(StorageClass.Glacier));
	    
		rule.withStatus(BucketLifecycleConfiguration.ENABLED.toString());
	    
	    return rule;
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
}

/**
	Output
	
	================ Initialize AWS Credentials ================ 
	================ Initialize Amazon S3 Object ================
	================ Create Bucket ================
	Bucket Name: sg-lifecycle

	================ ADD BUCKET LIFECYCLE CONFIGURATION ================ 
	================ GET BUCKET LIFECYCLE CONFIGURATION ================ 
	Id: Archive in 0 days
	Prefix: archived/
	Status: Enabled

*/