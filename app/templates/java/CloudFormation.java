package <%=packageName%>.utils.AmazonWebServices;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;
import com.amazonaws.services.cloudformation.model.ListStacksResult;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class CloudFormation 
{
	private AmazonCloudFormation cfnClient;
	
	//	Default constructor
	public CloudFormation () 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.cfnClient = AmazonCloudFormationClientBuilder.standard().withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}
	
	//	Default constructor
	public CloudFormation (String region) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.cfnClient = AmazonCloudFormationClientBuilder.standard().withRegion(Regions.valueOf(region)).withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}
	
	//	Parameterized constructor
	@SuppressWarnings("deprecation")
	public CloudFormation (String swfAccessId, String swfSecretKey) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.cfnClient = AmazonCloudFormationClientBuilder.standard().withRegion(System.getenv("Region")).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	//	Parameterized constructor
	@SuppressWarnings("deprecation")
	public CloudFormation (String swfAccessId, String swfSecretKey, String region) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.cfnClient = AmazonCloudFormationClientBuilder.standard().withRegion(region).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	/************************************************
	 * 	Setting Credentials for Amazon CFN Client.	*
	 ************************************************/
	
	//	Set credentials for Amazon CFN
	public void setCloudFormationClient () 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.cfnClient = AmazonCloudFormationClientBuilder.standard().withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}
	
	//	Set credentials for Amazon CFN given keys.
	@SuppressWarnings("deprecation")
	public void setCloudFormationClient (String swfAccessId, String swfSecretKey) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.cfnClient = AmazonCloudFormationClientBuilder.standard().withRegion(System.getenv("Region")).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	//	Set credentials for Amazon CFN given keys and the region.
	@SuppressWarnings("deprecation")
	public void setCloudFormationClient (String swfAccessId, String swfSecretKey, String region) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.cfnClient = AmazonCloudFormationClientBuilder.standard().withRegion(region).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
	}
	
	
	/************************************************
	 * 	Getting Amazon CFN Client.					*
	 ************************************************/
	
	//	Set credentials for Amazon CFN
	public AmazonCloudFormation getCloudFormationClient () 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		AmazonCloudFormation cfnClient = AmazonCloudFormationClientBuilder.standard().withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
		return cfnClient;
	}
	
	//	Set credentials for Amazon CFN given keys.
	@SuppressWarnings("deprecation")
	public AmazonCloudFormation getCloudFormationClient (String swfAccessId, String swfSecretKey) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		AmazonCloudFormation cfnClient = AmazonCloudFormationClientBuilder.standard().withRegion(System.getenv("Region")).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
		return cfnClient;
	}
	
	//	Set credentials for Amazon CFN given keys and the region.
	@SuppressWarnings("deprecation")
	public AmazonCloudFormation getCloudFormationClient (String swfAccessId, String swfSecretKey, String region) 
	{
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		AmazonCloudFormation cfnClient = AmazonCloudFormationClientBuilder.standard().withRegion(region).withClientConfiguration(config).withCredentials((AWSCredentialsProvider) new StaticCredentialsProvider(new BasicAWSCredentials(swfAccessId, swfSecretKey))).build();
		return cfnClient;
	}
	
	/************************************************
	 * 	Listing operations.							*
	 ************************************************/
	
	public ListStacksResult listStacks()
	{
		return this.cfnClient.listStacks();
	}
}