package com.chapter4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.CORSRule.AllowedMethods;
/**
 * @author SUNIL GULABANI
 */
public class CrossOriginResourceSharing extends AmazonS3ClientInitializer{
	public CrossOriginResourceSharing(){
		super();
	}
	
	public static void main(String[] args) {
		CrossOriginResourceSharing main = new CrossOriginResourceSharing();
		String bucketName = "sg-cors";
		main.createBucket(bucketName);
		main.addCORS(bucketName);
		main.listCORSConfig(bucketName);
//		main.deleteCORS(bucketName);
//		main.listCORSConfig(bucketName);
//		main.deleteBucket(bucketName);
	}
	
	private CORSRule createRule(
								String id,
								String[] allowedOrigins, 
								CORSRule.AllowedMethods[] allowedMethods,
								String[] exposedHeaders,
								int maxAgeSeconds
						){
		System.out.println("================ CREATE RULE ================ ");
		CORSRule rule = new CORSRule();
		
		rule.withId(id);

		if(allowedOrigins!=null && allowedOrigins.length > 0)
			rule.withAllowedOrigins(Arrays.asList(allowedOrigins));
		
		if(allowedMethods!=null && allowedMethods.length > 0)
			rule.withAllowedMethods(Arrays.asList(allowedMethods));
        
		if(maxAgeSeconds!= -1){
			rule.withMaxAgeSeconds(maxAgeSeconds);
		}
		
		if(exposedHeaders!=null && exposedHeaders.length > 0){
			rule.withExposedHeaders(Arrays.asList(exposedHeaders));
		}
        
		return rule;
	}
	
	public void addCORS(String bucketName){
		System.out.println("================ ADD CORS ================ ");
        BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
        
        List<CORSRule> rules = new ArrayList<CORSRule>();
        
        CORSRule.AllowedMethods[] allowedMethods1 = new CORSRule.AllowedMethods[] {CORSRule.AllowedMethods.GET, CORSRule.AllowedMethods.PUT, CORSRule.AllowedMethods.POST, CORSRule.AllowedMethods.DELETE};
        String[] allowedOrigins1 = new String[] {"http://*.sunilgulabani.com"};
        CORSRule rule1 = createRule("Sunil Gulabani Website Rule",allowedOrigins1,allowedMethods1,null,-1);
        
        CORSRule.AllowedMethods[] allowedMethods2 = new CORSRule.AllowedMethods[] {CORSRule.AllowedMethods.GET, CORSRule.AllowedMethods.POST};
        String[] allowedOrigins2 = new String[] {"*"};
        CORSRule rule2 = createRule("Third Party Website Rule",allowedOrigins2,allowedMethods2,null,1000);

        rules.add(rule1);
        rules.add(rule2);
        configuration.setRules(rules);

        s3.setBucketCrossOriginConfiguration(bucketName, configuration);
	}
	
	public void deleteCORS(String bucketName){
		System.out.println("================ DELETE CORS ================ ");
		s3.deleteBucketCrossOriginConfiguration(bucketName);		
	}
	
	public void listCORSConfig(String bucketName){
		System.out.println("================ LIST CORS ================ ");
		BucketCrossOriginConfiguration configuration = s3.getBucketCrossOriginConfiguration(bucketName);

		if (configuration == null){
            System.out.println("Configuration is null.");
            return;
        }else{
	        for (CORSRule rule : configuration.getRules()){
	            System.out.println("Id: " + rule.getId());
	            
	            if(rule.getAllowedOrigins()!=null){
	            	System.out.println("Allowed Origins: ");
	            	for(String allowedOrigin : rule.getAllowedOrigins()){
	            		System.out.println("\t-" + allowedOrigin);
	            	}
	            }
	            
	            if(rule.getAllowedMethods()!=null){
	            	List<AllowedMethods> allowedMethodsList = rule.getAllowedMethods();
	            	System.out.println("Allowed Methods: ");
	            	for(AllowedMethods allowedMethods : allowedMethodsList){
	            		System.out.println("\t-" + allowedMethods.toString());
	            	}
	            }
	            
	            if(rule.getAllowedHeaders()!=null){
	            	System.out.println("Allowed Headers: ");
	            	for(String allowedHeaders: rule.getAllowedHeaders()){
	            		System.out.println("\t-" + allowedHeaders);
	            	}
	            }
	            
	            if(rule.getExposedHeaders()!=null){
	            	System.out.println("Expose Header: ");
	            	for(String exposedHeaders : rule.getExposedHeaders()){
	            		System.out.println("\t-" + exposedHeaders);
	            	}
	            }
	            
	            System.out.println("Max Age Seconds: " + rule.getMaxAgeSeconds());
	            
	            System.out.println("------------------------------------------------");
	        }
        }
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
	Bucket Name: sg-cors
	
	================ ADD CORS ================ 
	================ CREATE RULE ================ 
	================ CREATE RULE ================ 
	================ LIST CORS ================ 
	Id: Sunil Gulabani Website Rule
	Allowed Origins: 
		-http://*.sunilgulabani.com
	Allowed Methods: 
		-GET
		-PUT
		-POST
		-DELETE
	Max Age Seconds: 0
	------------------------------------------------
	Id: Third Party Website Rule
	Allowed Origins: 
		-*
	Allowed Methods: 
		-GET
		-POST
	Max Age Seconds: 1000
	------------------------------------------------
	================ DELETE CORS ================ 
	================ LIST CORS ================ 
	Configuration is null.
	================ Delete Bucket ================ 
	 - bucket: sg-cors

*/