package <%=packageName%>.utils.AmazonWebServices;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.Tag;

@SuppressWarnings("deprecation")
public class S3 
{	
	private AmazonS3 s3client;
	
	//	Default constructor
	public S3 () 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		s3client = AmazonS3ClientBuilder.standard().withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}
	
	//	Parameterized constructor
	public S3 (String swfAccessId, String swfSecretKey) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		s3client = AmazonS3ClientBuilder.standard().withRegion(System.getenv("Region")).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	//	Parameterized constructor
	public S3 (String swfAccessId, String swfSecretKey, String region) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		s3client = AmazonS3ClientBuilder.standard().withRegion(region).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	/************************************************
	 * 	Setting Credentials for Amazon S3 Client.	*
	 ************************************************/
	
	//	Updates the client for Lambda services (Does not have instance credentials).
	public void setClientForLambda ()
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		s3client = AmazonS3ClientBuilder.standard().withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}
	
	//	Updates the client for EC2 using instance credentials.
	public void setClientForEC2 ()
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		s3client = AmazonS3ClientBuilder.standard().withClientConfiguration(config).withCredentials(new InstanceProfileCredentialsProvider(false)).build();
	}
	
	//	Updates the client for EC2 by passing credentials.
	public void setClientWithCredentials (String swfAccessId, String swfSecretKey)
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		s3client = AmazonS3ClientBuilder.standard().withRegion(System.getenv("Region")).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	//	Updates the client for EC2 by passing credentials and region.
	public void setClientWithCredentials (String swfAccessId, String swfSecretKey, String region)
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		s3client = AmazonS3ClientBuilder.standard().withRegion(region).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	
	/************************************************
	 * 	Getting Credentials for Amazon S3 Client.	*
	 ************************************************/
	
	//	Returns a new Amazon S3 client for EC2 using instance credentials.
	public AmazonS3 getClientForEC2 ()
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withClientConfiguration(config).withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		return s3client;
	}
	
	//	Returns a new Amazon S3 client for Lambda services (Does not have instance credentials).
	public AmazonS3 getClientForLambda ()
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
		return s3client;
	}
	
	//	Updates the client for EC2 by passing credentials.
	public AmazonS3 getClientWithCredentials (String swfAccessId, String swfSecretKey)
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(System.getenv("Region")).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
		return s3client;
	}
	
	//	Updates the client for EC2 by passing credentials and region.
	public AmazonS3 getClientWithCredentials (String swfAccessId, String swfSecretKey, String region)
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(region).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
		return s3client;
	}
	
	/************************************************
	 * 	Getting object info							*
	 ************************************************/
	
	/*
	 * 	Get the object date
	 */
	public Date getObjectDate(String s3Path) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		S3Object object = null;
		String bucketName = s3Path.split("/")[2];
		String folderPath = s3Path.replace("S3://", "").replace(bucketName+"/", "");
				
		/*	Check if the object exists. 
		 * 	If the object retrieve the S3 object.
		 */
		if (s3client.doesObjectExist(bucketName,folderPath)) 
		{
			try 
			{
				object = s3client.getObject(new GetObjectRequest(bucketName,folderPath));
			} 
			
			catch (AmazonS3Exception e) 
			{
				System.out.println("Cannot get the datapoint we need.");
			}
		}

		return object.getObjectMetadata().getLastModified();
	}
	
	/*
	 * 	Get the object date
	 */
	public Date getObjectDate(String bucketName, String folderPath) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		S3Object object = null;
				
		/*	Check if the object exists. 
		 * 	If the object retrieve the S3 object.
		 */
		if (s3client.doesObjectExist(bucketName,folderPath)) 
		{
			try 
			{
				object = s3client.getObject(new GetObjectRequest(bucketName,folderPath));
			} 
			
			catch (AmazonS3Exception e) 
			{
				System.out.println("Cannot get the datapoint we need.");
			}
		}
		
		
		return object.getObjectMetadata().getLastModified();
	}
	
	
	/*
	 * 	Get the object date
	 */
	public List<Tag> getObjectUserMetadata(String s3Path) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		S3Object object = null;
		String bucketName = s3Path.split("/")[2];
		String folderPath = s3Path.replace("S3://", "").replace(bucketName+"/", "");
			
		GetObjectTaggingRequest getTaggingRequest = new GetObjectTaggingRequest(bucketName, folderPath);
		GetObjectTaggingResult  getTagsResult = s3client.getObjectTagging(getTaggingRequest);
		List<Tag> tags = getTagsResult.getTagSet();
		
		return tags;
	}
	
	/*
	 * 	Get the object date
	 */
	public Map<String, String> getObjectUserMetadata(String bucketName, String folderPath) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		S3Object object = null;
				
		/*	Check if the object exists. 
		 * 	If the object retrieve the S3 object.
		 */
		if (s3client.doesObjectExist(bucketName,folderPath)) 
		{
			try 
			{
				object = s3client.getObject(new GetObjectRequest(bucketName,folderPath));
			} 
			
			catch (AmazonS3Exception e) 
			{
				System.out.println("Cannot get the datapoint we need.");
			}
		}
		
		return object.getObjectMetadata().getUserMetadata();
	}
	
	/************************************************
	 * 	Read and write objects to and from S3.		*
	 ************************************************/
	
	/* 	This function is used to read a string from a S3 path, S3 path sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public String readS3ObjectAsString (String s3Path) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", ""); 
		
        // Return the string.
        return s3client.getObjectAsString(bucketName, s3Key);
	}
	
	
	/* 	This function is used to read the file json from a S3 path, S3 path sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public String readS3ObjectAsString (String bucketName, String s3Key) 
	{
        // Return the string.
        return s3client.getObjectAsString(bucketName, s3Key);
	}
	
	/*	This function is used to get S3 Object as a stream.
	 */
	public InputStream readS3ObjectAsStream (String s3Path)
	{
		// Split S3 Path to get the bucket name and the folder path.
		S3Object object = null;
		String bucketName = s3Path.split("/")[2];
		String folderPath = s3Path.replace("S3://", "").replace(bucketName+"/", "");
				
		/*	Check if the object exists. 
		 * 	If the object retrieve the S3 object.
		 */
		if (s3client.doesObjectExist(bucketName,folderPath)) 
		{
			try 
			{
				object = s3client.getObject(new GetObjectRequest(bucketName,folderPath));
			} 
			
			catch (AmazonS3Exception e) 
			{
				System.out.println("Cannot get the datapoint we need.");
			}
		}
				
		InputStream objectData = object.getObjectContent();
		return objectData;
	}
	
	public InputStream readS3ObjectAsStream (String bucketName, String folderPath)
	{		
		S3Object object = null;
		/*	Check if the object exists. 
		 * 	If the object retrieve the S3 object.
		 */
		if (s3client.doesObjectExist(bucketName,folderPath)) 
		{
			try 
			{
				object = s3client.getObject(new GetObjectRequest(bucketName,folderPath));
			} 
			
			catch (AmazonS3Exception e) 
			{
				System.out.println("Cannot get the datapoint we need.");
			}
		}
				
		InputStream objectData = object.getObjectContent();
		return objectData;
	}
	
	// 	Persist the String to S3.
	public void persistStringAsS3Object (String bucketName, String s3Key, String text)
    {
		s3client.putObject(bucketName, s3Key, text);
    }	
	
	// 	Persist the File to S3.
	public void persistFileAsS3Object (String bucketName, String s3Key, File file)
    {
		s3client.putObject(bucketName, s3Key, file);
    }
	
	// 	Persist the File with metadata to S3.
	public void persistFileAsS3ObjectWithMetadata (String bucketName, String s3Key, File file, String key, String value)
    {
		PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, file); 
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(key, value));
		putRequest.setTagging(new ObjectTagging(tags));
		s3client.putObject(putRequest);	
    }
	
	// 	Persist the File with metadata to S3.
	public void persistFileAsS3ObjectWithMetadata (String bucketName, String s3Key, File file, Map<String, String> tagMap)
    {
		PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, file); 
		List<Tag> tags = new ArrayList<Tag>();

		for(Entry<String, String> tag : tagMap.entrySet())
			tags.add(new Tag(tag.getKey(), tag.getValue()));
		putRequest.setTagging(new ObjectTagging(tags));
		s3client.putObject(putRequest);	
    }
	
	// 	Persist the File with metadata to S3.
	public void persistFileAsS3ObjectWithMetadata (String bucketName, String s3Key, File file, List<Tag> tags)
    {
		PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, file); 
		
		putRequest.setTagging(new ObjectTagging(tags));
		s3client.putObject(putRequest);	
    }
	
	/* 	Persist the logJson to S3. 
	 * 	Essentially an overwrite operation in this case.
	 */
	public void persistStringAsS3Object (String s3Path, String text)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
		
		s3client.putObject(bucketName, s3Key, text);
    }
	
	// 	Persist the String to S3.
	public void persistStreamAsS3Object (String bucketName, String s3Key, InputStream content, int length) throws IOException
    {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(length);
		s3client.putObject(new PutObjectRequest(bucketName, s3Key, content, metadata));
    }
	
	/* 	Persist the logJson to S3. 
	 * 	Essentially an overwrite operation in this case.
	 */
	public void persistStreamAsS3Object (String s3Path, InputStream content, int length) throws IOException
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
				
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(length);
		
		s3client.putObject(new PutObjectRequest(bucketName, s3Key, content, metadata));
    }
	
	/* 	Persist the logJson to S3. 
	 * 	Essentially an overwrite operation in this case.
	 */
	public void persistFileAsS3Object (String s3Path, File file)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
		
		s3client.putObject(bucketName, s3Key, file);
    }
	
	// 	Persist the File with metadata to S3.
	public void persistFileAsS3ObjectWithMetadata (String s3Path, File file, String key, String value)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
				
		PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, file); 
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(key, value));
		putRequest.setTagging(new ObjectTagging(tags));
		s3client.putObject(putRequest);	
    }
	
	// 	Persist the File with metadata to S3.
	public void persistFileAsS3ObjectWithMetadata (String s3Path, File file, Map<String, String> tagMap)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
				
		PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, file); 
		List<Tag> tags = new ArrayList<Tag>();

		for(Entry<String, String> tag : tagMap.entrySet())
			tags.add(new Tag(tag.getKey(), tag.getValue()));
		putRequest.setTagging(new ObjectTagging(tags));
		s3client.putObject(putRequest);	
    }
	
	// 	Persist the File with metadata to S3.
	public void persistFileAsS3ObjectWithMetadata (String s3Path, File file, List<Tag> tags)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
				
		PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, file); 
		
		putRequest.setTagging(new ObjectTagging(tags));
		s3client.putObject(putRequest);	
    }


	/************************************************
	 * 	Folder uploads to S3.						*
	 ************************************************/
	
	/* 	Upload an entire directory to S3, given the directory path and extensions.
	 */
	public void uploadFilesFromDirectory (String s3Path, String folderPath, String[] extensions)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
		
		File local = new File(folderPath);
		List<File> files = (List<File>) FileUtils.listFiles(local, extensions, true);

		for (File file : files) 
		{
			s3client.putObject(bucketName, s3Key + "/" + file.getName(), file);
		} 
    }
	
	/* 	Upload an entire directory to S3, given the directory path and extensions.
	 */
	public void uploadFilesFromDirectory (String bucketName, String s3Key, String folderPath, String[] extensions)
    {
		// Split S3 Path to get the bucket name and the folder path.
		File local = new File(folderPath);
		List<File> files = (List<File>) FileUtils.listFiles(local, extensions, true);

		for (File file : files) 
		{
			s3client.putObject(bucketName, s3Key + "/" + file.getName(), file);
		} 
    }
	
	/* 	Upload an entire directory to S3, given the directory path and extensions.
	 */
	public void uploadFilesFromDirectory (String s3Path, List<File> files)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
		
		for (File file : files) 
		{
			s3client.putObject(bucketName, s3Key + "/" + file.getName(), file);
		} 
    }
	
	/* 	Upload an entire directory to S3, given the directory path and extensions.
	 */
	public void uploadFilesFromDirectory (String bucketName, String s3Key, List<File> files)
    {
		for (File file : files) 
		{
			s3client.putObject(bucketName, s3Key + "/" + file.getName(), file);
		} 
    }
	
	
	
	/* 	Upload an entire directory to S3, given the directory path and extensions.
	 */
	public void uploadDirectoryMaintainingStructure (String s3Path, String folderPath, String[] extensions)
    {
		// Split S3 Path to get the bucket name and the folder path.
		s3Path = s3Path.replace("S3://", "");
		String bucketName = s3Path.substring(0,s3Path.indexOf("/"));
		String s3Key = s3Path.replace(bucketName+"/", "");
		
		File local = new File(folderPath);
		List<File> files = (List<File>) FileUtils.listFiles(local, extensions, true);

		for (File file : files) 
		{	
			s3client.putObject(bucketName, s3Key + "/" + file.getParent().replaceAll("\\\\", "/").replace(folderPath, ""), file);
		} 
    }
	
	/* 	Upload an entire directory to S3, given the directory path and extensions.
	 */
	public void uploadDirectoryMaintainingStructure (String bucketName, String s3Key, String folderPath, String[] extensions)
    {
		// Split S3 Path to get the bucket name and the folder path.
		File local = new File(folderPath);
		List<File> files = (List<File>) FileUtils.listFiles(local, extensions, true);

		for (File file : files) 
		{
			s3client.putObject(bucketName, s3Key + "/" + file.getParent().replaceAll("\\\\", "/").replace(folderPath, ""), file);
		} 
    }
	
	
	/************************************************
	 * 	Copy objects to and from a S3 bucket/key.	*
	 ************************************************/
	
	/* 	This function is used to copy an object from a source S3 path, to target S3 path.
	 * 	S3 path sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public CopyObjectResult copyObject (String sourceS3Path, String targetS3Path) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		sourceS3Path = sourceS3Path.replace("S3://", "");
		String sourceBucketName = sourceS3Path.substring(0,sourceS3Path.indexOf("/"));
		String sourceS3Key = sourceS3Path.replace(sourceBucketName+"/", ""); 
		
		// Split S3 Path to get the bucket name and the folder path.
		targetS3Path = targetS3Path.replace("S3://", "");
		String targetBucketName = targetS3Path.substring(0,targetS3Path.indexOf("/"));
		String targetS3Key = targetS3Path.replace(targetBucketName+"/", ""); 
		
        // Return the string.
        return s3client.copyObject(sourceBucketName, sourceS3Key, targetBucketName, targetS3Key);
	}
	
	
	/* 	This function is used to copy an object from a source S3 path, to target S3 path.
	 * 	Sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public CopyObjectResult copyObject (String sourceBucketName, String sourceS3Key, String targetBucketName, String targetS3Key) 
	{
        // Return the string.
        return s3client.copyObject(sourceBucketName, sourceS3Key, targetBucketName, targetS3Key);
	}

	
	
	/************************************************
	 * 	Delete operations							*
	 ************************************************/
	
	/*
	 * 	This function is used to delete the object from a S3 path, 
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public boolean deleteS3Object (String s3Path) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		/*
		 * Check if the object exists. If the object retrieve the S3 object.
		 */
		if (s3client.doesObjectExist(bucketName, S3key)) 
		{
			try 
			{
				s3client.deleteObject(bucketName, S3key);
				//System.out.println("Successfully deleted object from S3.");
				return true;
			}

			catch (AmazonS3Exception e) 
			{
				//System.out.println("Cannot delete object from S3.");
				return false;
			}
		}

		//System.out.println("Object already deleted from S3.");
		return true;
	}
	
	/*
	 * 	This function is used to delete an object from a S3 path
	 * 	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public boolean deleteS3Object (String bucketName, String S3key) 
	{
		/*
		 * Check if the object exists. If the object retrieve the S3 object.
		 */
		if (s3client.doesObjectExist(bucketName, S3key)) 
		{
			try 
			{
				s3client.deleteObject(bucketName, S3key);
				//System.out.println("Successfully deleted object from S3.");
				return true;
			}

			catch (AmazonS3Exception e) 
			{
				//System.out.println("Cannot delete object from S3.");
				return false;
			}
		}

		//System.out.println("Object already deleted from S3.");
		return true;
	}
	
	/*
	 * 	This function is used to delete all objects from a S3 folder
	 * 	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public int deleteAllS3Objects (String bucketName, String s3Key)
	{
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		System.out.println("Deleting files from S3");
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;
		
		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if (deleteS3Object(objectSummary.getBucketName(), objectSummary.getKey()))	i++;
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects deleted = " + i);
		
		return i;
	}
	
	/*
	 * 	This function is used to delete all objects from a S3 folder
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public int deleteAllS3Objects (String s3Path) 
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(S3key);
        
		System.out.println("Deleting files from S3");
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if (deleteS3Object(objectSummary.getBucketName(), objectSummary.getKey()))	i++;
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects deleted = " + i);
		
		return i;
	}
	
	/************************************************
	 * 	Listing operations							*
	 ************************************************/
	
	/*	A function  that lists all the buckets.
	 *
	 */
	public List<Bucket> listBuckets()
	{
		return s3client.listBuckets();
	}
	
	/*	A function  that lists all the buckets.
	 *
	 */
	public List<String> listBucketsAsString()
	{
		List<String> buckets = new ArrayList<String>();
		for(Bucket bucket : s3client.listBuckets())
		{
			buckets.add(bucket.getName());
		}
		return buckets;
	}
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public List<String> listS3Objects (String s3Path)
	{
		List<String> objects = new ArrayList<String>();
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(S3key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				i++;
				objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listS3Objects (String bucketName, String s3Key)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				i++;
				objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listS3Objects (String bucketName, String s3Key, String delimiter)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key).withDelimiter(delimiter);;
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (String objectSummary : objectListing.getCommonPrefixes()) 
			{
				i++;
				objects.add("S3://"+bucketName+"/"+objectSummary);
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects Found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public List<String> listS3Objects (String s3Path, Date date)
	{
		List<String> objects = new ArrayList<String>();
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(S3key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getLastModified().compareTo(date) >= 0)
				{
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listS3Objects (String bucketName, String s3Key, Date date)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getLastModified().compareTo(date) >= 0)
				{
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listS3Objects (String bucketName, String s3Key, String delimiter, Date date)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key).withDelimiter(delimiter);;
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getLastModified().compareTo(date) >= 0)
				{
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects Found = " + i);
		
		return objects;
	}
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public List<String> listS3Objects (String s3Path, Date startDate, Date endDate)
	{
		List<String> objects = new ArrayList<String>();
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(S3key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getLastModified().compareTo(startDate) >= 0 && objectSummary.getLastModified().compareTo(endDate) <= 0)
				{
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listS3Objects (String bucketName, String s3Key, Date startDate, Date endDate)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getLastModified().compareTo(startDate) >= 0 && objectSummary.getLastModified().compareTo(endDate) <= 0)
				{
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public Map<Date, String> listS3ObjectsWithTimeStamps (String bucketName, String s3Key, Date startDate, Date endDate)
	{
		Map<Date, String> objects = new TreeMap<Date, String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getLastModified().compareTo(startDate) >= 0 && objectSummary.getLastModified().compareTo(endDate) <= 0)
				{
					i++;
					objects.put(objectSummary.getLastModified(), ("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey()));
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listS3Objects (String bucketName, String s3Key, String delimiter, Date startDate, Date endDate)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key).withDelimiter(delimiter);;
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getLastModified().compareTo(startDate) >= 0 && objectSummary.getLastModified().compareTo(endDate) <= 0)
				{
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects Found = " + i);
		
		return objects;
	}
	
	
	/************************************************
	 * 	Counting operations							*
	 ************************************************/
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public int countS3Objects (String s3Path)
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(S3key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			i += objectListing.getObjectSummaries().size(); 
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return i;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public int countS3Objects (String bucketName, String s3Key)
	{
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			i += objectListing.getObjectSummaries().size();
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return i;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public int countS3Objects (String bucketName, String s3Key, String delimiter)
	{
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key).withDelimiter(delimiter);;
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			i += objectListing.getObjectSummaries().size();
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects Found = " + i);
		
		return i;
	}
	
	
	/************************************************
	 * 	Filtered operations							*
	 ************************************************/
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public List<String> listFilteredS3Objects (String s3Path, String filter)
	{
		List<String> objects = new ArrayList<String>();
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(S3key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getKey().contains(filter))
				{	
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listFilteredS3Objects (String bucketName, String s3Key, String filter)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getKey().contains(filter))
				{	
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return objects;
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public List<String> listFilteredS3Objects (String bucketName, String s3Key, String delimiter, String filter)
	{
		List<String> objects = new ArrayList<String>();
		
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key).withDelimiter(delimiter);;
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getKey().contains(filter))
				{	
					i++;
					objects.add("S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey());
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects Found = " + i);
		
		return objects;
	}
	
	
	/*	A function  that lists all the buckets.
	 *
	 */
	public String findRequiredBucket (String filter)
	{
		for(Bucket bucket : s3client.listBuckets())
		{
			if(bucket.getName().contains(filter))	return bucket.getName();
		}
		
		return null;
	}
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public String findRequiredS3Object (String s3Path, String filter)
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(S3key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getKey().contains(filter))
				{	
					i++;
					return "S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey();
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return "";
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public String findRequiredS3Object (String bucketName, String s3Key, String filter)
	{
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key);
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				i++;
				if(objectSummary.getKey().contains(filter))
				{	
					return "S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey();
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects found = " + i);
		
		return "";
	}
	
	
	/*	This function returns a list of all objects on S3.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public String findRequiredS3Object (String bucketName, String s3Key, String delimiter, String filter)
	{
		//	List all objects at the bucket and S3 Key.
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3Key).withDelimiter(delimiter);;
        
		/*	List all objects in s3 and get their summaries.
		 * 	For any object having metadata.json or if it is EntityTaggingFinalOutput or Documents, then download it.
		 */
		ObjectListing objectListing;
		
		int i = 0;

		do
		{	
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
			{
				if(objectSummary.getKey().contains(filter))
				{	
					i++;
					return "S3://"+objectSummary.getBucketName()+"/"+objectSummary.getKey();
				}
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		}while(objectListing.isTruncated());
		
		System.out.println("Objects Found = " + i);
		
		return "";
	}
	
	/************************************************
	 * 	Check operations							*
	 ************************************************/
	
	/*	This function returns a boolean indicating whether the the object exists.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public boolean doesObjectExist(String s3Path)
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

		return s3client.doesObjectExist(bucketName, S3key);
	}
	
	/*	This function returns a boolean indicating whether the the object exists.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public boolean doesObjectExist(String bucketName, String S3key )
	{
		return s3client.doesObjectExist(bucketName, S3key);
	}
	
	/*	This function returns a boolean indicating whether the the object exists.
	 * 	S3 path	sample:
	 * 	"S3://hw-app-a5e2ccf9-3/DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json"
	 */
	public boolean doesBucketInPathExist(String s3Path)
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];

		return s3client.doesBucketExist(bucketName);
	}
	
	/*	This function returns a boolean indicating whether the the object exists.
	 * 	S3 path	sample:
	 * 	bucketName = hw-app-a5e2ccf9-3
	 * 	s3Key = DocumentProcessingVA/WPI1/WPE85/ClassifyPageWithElasticSearchWorkflow/5d6c469f-3a64-4bf8-883c-916bdb7/d73de8b0-2092-4dda-9ffc-e8dd4f94fd85.json
	 */
	public boolean doesBucketExist (String bucketName)
	{
		return s3client.doesBucketExist(bucketName);
	}
	
	/************************************************
	 * 	Formatting Operations						*
	 ************************************************/
	
	//	Given a list of Strings, it gets the bucketName (at location 2)
	public String getBucketName (List<String> tokens)
	{
		return tokens.get(2);
	}
	
	//	Given a list of Strings, it gets the S3 Key.
	public String getS3Key (List<String> tokens, int length)
	{
		String key = tokens.get(3);
		
		for(int i=4; i<length; i ++)
		{
			key += "/" + tokens.get(i);
		}
		
		return key;
	}
	
	//	Given a list of Strings, it gets the S3 Key.
	public String getS3Key (List<String> tokens)
	{
		String key = tokens.get(3);
		
		for(int i=4; i< tokens.size(); i ++)
		{
			key += "/" + tokens.get(i);
		}
		
		return key;
	}
	
	//	Given a list of Strings, it gets the S3 Key (at location 2)
	public String getS3KeyAsFolder (List<String> tokens, int length)
	{
		String key = tokens.get(3);
		
		for(int i=4; i<length; i ++)
		{
			key += "/" + tokens.get(i);
		}
		
		return key+"/";
	}
	
	//	Given a list of Strings, Build the S3 Path.
	public String buildS3Path (List<String> tokens)
	{
		return "S3://"+ getBucketName(tokens)+"/"+getS3Key(tokens);
	}
	
	//	Given a list of Strings, Build the S3 Path.
	public String buildS3Path (String bucketName, String s3Key)
	{
		return "S3://"+ bucketName+"/"+s3Key;
	}
	
	/************************************************
	 * 	get Unsigned URL for GET					*
	 ************************************************/
	
	/*	Default Expiration time = 3 days.
	 * 
	 */
	public URL getUnsignedUrl(String s3Path)
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

        java.util.Date expiration = new java.util.Date();
        long msec = System.currentTimeMillis();
        msec = msec + (1000 * 60 * 60 * 24 * 3);
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, S3key);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        return url;
    }
	
	/*	Default Expiration time = 3 days.
	 * 
	 */
	public URL getUnsignedUrl(String bucketName, String S3key)
	{
        java.util.Date expiration = new java.util.Date();
        long msec = System.currentTimeMillis();
        msec = msec + (1000 * 60 * 60 * 24 * 3);
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, S3key);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        return url;
    }
	
	/*	Default Expiration time = 3 days.
	 * 
	 */
	public URL getUnsignedUrl(String s3Path, int seconds)
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");

        java.util.Date expiration = new java.util.Date();
        long msec = System.currentTimeMillis();
        msec = msec + (seconds);
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, S3key);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        return url;
    }
	
	/*	Default Expiration time = 3 days.
	 * 
	 */
	public URL getUnsignedUrl(String bucketName, String S3key, int seconds)
	{
        java.util.Date expiration = new java.util.Date();
        long msec = System.currentTimeMillis();
        msec = msec + (seconds);
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, S3key);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        return url;
    }
	
	/************************************************
	 * 	Change object permissions.					*
	 ************************************************/
	/*	Make the object public.
	 * 
	 */
	public void makePublic(String bucketName, String S3key)
	{
		s3client.setObjectAcl(bucketName, S3key, CannedAccessControlList.PublicRead);
	}
	
	public void makePublic(String s3Path)
	{
		// Split S3 Path to get the bucket name and the folder path.
		String bucketName = s3Path.split("/")[2];
		String S3key = s3Path.replace("S3://", "").replace(bucketName + "/", "");
				
		s3client.setObjectAcl(bucketName, S3key, CannedAccessControlList.PublicRead);
	}

	/************************************************
	 * 	Check account info.							*
	 ************************************************/
	public Owner getS3AccountOwner() 
	{
		return s3client.getS3AccountOwner();
	}
}
