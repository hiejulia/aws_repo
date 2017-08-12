package com.chapter4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * @author SUNIL GULABANI
 */
public class MultipartCopyObjects extends AmazonS3ClientInitializer{
	
	public MultipartCopyObjects() {
		super();
	}
	
	public static void main(String[] args) {
		MultipartCopyObjects main = new MultipartCopyObjects();
		
		String sourceBucketName = "sg-m1-copy-object" ;
		main.createBucket(sourceBucketName);
		
		String sourceObjectKey = "AmazonS3ClientInitializer.java";
		main.uploadFileInBucket(sourceBucketName, "C:\\Users\\SUNIL\\workspace\\Chapter4S3\\src\\com\\chapter4\\AmazonS3ClientInitializer.java", sourceObjectKey);
		
		String destinationBucketName = sourceBucketName + "-copy-1";
		main.createBucket(destinationBucketName);
		
		String destinationObjectKey = "AmazonS3ClientInitializer-copy-1.java";
		
		main.copyObjects3(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObjectKey);
		
		main.listAllFolderAndObjectsInBucket(destinationBucketName);
	}

	public void copyObjects3(String sourceBucketName, String sourceObjectKey,String destinationBucketName, String destinationObjectKey){
		System.out.println("================ COPY OBJECT USING MULTIPART ================ ");
		List<CopyPartResult> copyPartResultList = new ArrayList<CopyPartResult>();
		
		/**
		 * InitiateMultipartUploadRequest is used to specify the destination bucket name and object key for multipart upload.
		 */
		InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(destinationBucketName, destinationObjectKey);

		/**
		 * InitiateMultipartUploadResult provides the result of InitiateMultipartUploadRequest which contains the upload id.
		 */
		InitiateMultipartUploadResult initMultipartUploadResult = s3.initiateMultipartUpload(initiateMultipartUploadRequest);
		try {
			/**
			 * How much part size we will transfer. For our demo we gave 1MB. You can increase as per the need. 
			 */
			long partSizeInBytes = getBytesFromMB(1);
			
			/**
			 * This will specify what will be the first byte to be copied in CopyPartRequest.
			 */
			long bytePositionInBytes = 0;

			/**
			 * This is the total size of source object.
			 */
            long objectSizeInBytes = getSourceObjectLength(sourceBucketName, sourceObjectKey);

            CopyPartRequest copyPartRequest = null;
            
            CopyPartResult copyPartResult = null;
            
            for (int i = 1; bytePositionInBytes < objectSizeInBytes; i++){
            	copyPartRequest = getCopyPartRequest(
            								sourceBucketName, sourceObjectKey, 
            								destinationBucketName, destinationObjectKey, 
            								initMultipartUploadResult.getUploadId(), 
            								bytePositionInBytes, partSizeInBytes, objectSizeInBytes, i);

            	copyPartResult = s3.copyPart(copyPartRequest);
            	copyPartResultList.add(copyPartResult);
                bytePositionInBytes += partSizeInBytes;
            }

            List<PartETag> partEtags = getPartEtags(copyPartResultList);
            
            CompleteMultipartUploadResult completeMultipartUploadResult = 
            											s3.completeMultipartUpload(new CompleteMultipartUploadRequest(
            													destinationBucketName, destinationObjectKey,
            													initMultipartUploadResult.getUploadId(), partEtags));

            System.out.println("Bucket Name: " + completeMultipartUploadResult.getBucketName());
            System.out.println("Key: " + completeMultipartUploadResult.getKey());
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
	
	private long getBytesFromMB(int mb){
		return mb*1024*1024;
	}
	
	private long getSourceObjectLength(String sourceBucketName, String sourceObjectKey){
		return s3.getObjectMetadata(new GetObjectMetadataRequest(sourceBucketName, sourceObjectKey)).getContentLength();
	}
	
	/**
	 *  CopyPartRequest is used to copy part of the source object to a destination object in multipart upload.
	 */
	private CopyPartRequest getCopyPartRequest(
								String sourceBucketName, String sourceObjectKey, 
								String destinationBucketName, String destinationObjectKey,
								String uploadId, long bytePositionInBytes, 
								long partSizeInBytes, long objectSizeInBytes, int partNumber
			){
		CopyPartRequest copyPartRequest = new CopyPartRequest();
		
		copyPartRequest.withSourceBucketName(sourceBucketName);
		copyPartRequest.withSourceKey(sourceObjectKey);
		
    	copyPartRequest.withDestinationBucketName(destinationBucketName);
    	copyPartRequest.withDestinationKey(destinationObjectKey);
    	
    	copyPartRequest.withUploadId(uploadId);
    	copyPartRequest.withFirstByte(bytePositionInBytes);
    	
    	if(bytePositionInBytes + partSizeInBytes -1 >= objectSizeInBytes){
    		copyPartRequest.withLastByte(objectSizeInBytes - 1);
    	}else{
    		copyPartRequest.withLastByte(bytePositionInBytes + partSizeInBytes - 1);
    	}
    	
    	copyPartRequest.withPartNumber(partNumber);
    	return copyPartRequest;
	}
	
	/**
	 * ETag is the entity tag which is hash of the object's content. 
	 * This hash is not the MD5 digest.
	 * @param responses
	 * @return
	 */
    private List<PartETag> getPartEtags(List<CopyPartResult> responses){
        List<PartETag> partEtags = new ArrayList<PartETag>();
        for (CopyPartResult response : responses){
        	partEtags.add(new PartETag(response.getPartNumber(), response.getETag()));
        }
        return partEtags;
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
	Bucket Name: sg-m-copy-object

	================ Upload File In Bucket ================ 
	fileName: AmazonS3ClientInitializer.java
	================ Create Bucket ================ 
	Bucket Name: sg-m-copy-object-copy-1
	
	================ COPY OBJECT USING MULTIPART ================ 
	Bucket Name: sg-m-copy-object-copy-1
	Key: AmazonS3ClientInitializer-copy-1.java
	================ List Bucket's Folders And Objects ================ 
		- AmazonS3ClientInitializer-copy-1.java  (size = 1402)

*/