package com.chapter3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * @author SUNIL GULABANI
 */
public class UploadFileInFolder extends AmazonS3ClientInitializer{
	public UploadFileInFolder() {
		super();
	}
	
	public static void main(String[] args) {
		String bucketName = "sg-bucket-2015-using-java-api-" + new Date().getTime();
		String folderName = "sg-folder-" + new Date().getTime();
		
		UploadFileInFolder object = new UploadFileInFolder();
		object.createBucket(bucketName);
		object.createFolderInBucket(bucketName , folderName);
		
		object.uploadFileInFolder(bucketName, folderName,"C:\\admin.ico","admin.ico");
		
		object.listFolderContent(bucketName, folderName);
	}

	public void uploadFileInFolder(String bucketName, String folderName, String filePath, String fileNameSuffix){
		System.out.println("================ Upload File In Folder ================ ");
		// upload file to folder and set it to public
		String fileName = folderName + "/" + fileNameSuffix;
		
		System.out.println("fileName: " + fileName);
		s3.putObject(new PutObjectRequest(
								bucketName, 
								fileName,
								new File(filePath))
							.withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
	public void createFolderInBucket(String bucketName, String folderName){
		System.out.println("================ Create Folder In Bucket ================ ");
		// create folder into bucket
		System.out.println("Folder Name: " + folderName);
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + "/", emptyContent, metadata);
		// send request to S3 to create folder
		s3.putObject(putObjectRequest);
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
	
	public void listFolderContent(String bucketName, String folderName){
		System.out.println("================ List Folder Content ================ ");
		List<S3ObjectSummary> fileList = s3.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary objectSummary : fileList) {
			System.out.println( "\t- " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
		}
	}
}
