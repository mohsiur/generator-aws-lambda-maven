package <%=packageName%>.utils.AmazonWebServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

import solutions.heavywater.Utilities.Operations.RegexOperations;


public class DynamoDb 
{
	/*	Attributes required for successful dynamo DB operations.
	 * 	dbClient	-->	Dynamo Client.
	 * 	table	-->	Table name.
	 * 	item	-->	Entry in the table.
	 */
	private AmazonDynamoDBClient dbClient = null;
	private DynamoDB dynamoDb = null;
	private Table table = null;
	private Item item = null;
	
	/*	Variables describing the table.
	 * 	tableName	-->	Table name.
	 */
	private String tableName;
	
	public DynamoDb ()
	{
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient();
		dynamoDb = new DynamoDB(dbClient);
	}
	
	/*	 
	 * 	Parameterized default constructor that accepts the region to build credentials.
	 */
	public DynamoDb (String region)
	{
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient();
		
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(region)));
		dynamoDb = new DynamoDB(dbClient);
	}
	
	/*	 
	 * 	Parameterized default constructor that accepts the access id and key to build credentials.
	 */
	public DynamoDb (String swfAccessId, String swfSecretKey)
	{
		//	Create credentials using hte access id and key.
		AWSCredentials aws = new BasicAWSCredentials(swfAccessId, swfSecretKey);
		
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(aws);
		
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(System.getenv("Region"))));
		dynamoDb = new DynamoDB(dbClient);
	}
	
	/*	 
	 * 	Parameterized default constructor that accepts the access id and key along with region to build credentials.
	 */
	
	public DynamoDb (String swfAccessId, String swfSecretKey, String region)
	{
		//	Create credentials using hte access id and key.
		AWSCredentials aws = new BasicAWSCredentials(swfAccessId, swfSecretKey);
			
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(aws);
		
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(region)));
		dynamoDb = new DynamoDB(dbClient);
	}
	
	
	/************************************************
	 * 	Setting Credentials for Amazon DynamoDB.	*
	 ************************************************/
	
	//	Updates the client for Lambda services (Does not have instance credentials).
	public void setDbForLambda ()
	{
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient();
			
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(System.getenv("Region"))));
		dynamoDb = new DynamoDB(dbClient);
	}
	
	//	Updates the client for EC2 using instance credentials.
	public void setDbForEC2 ()
	{	
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(new InstanceProfileCredentialsProvider(false));
		dynamoDb = new DynamoDB(dbClient);
	}
	
	//	Updates the client for EC2 by passing credentials.
	public void setDbWithCredentials (String swfAccessId, String swfSecretKey)
	{
		//	Create credentials using hte access id and key.
		AWSCredentials aws = new BasicAWSCredentials(swfAccessId, swfSecretKey);
				
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(aws);
			
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(System.getenv("Region"))));
		dynamoDb = new DynamoDB(dbClient);
	}
	
	//	Updates the client for EC2 by passing credentials and region.
	public void setDbWithCredentials (String swfAccessId, String swfSecretKey, String region)
	{
		//	Create credentials using hte access id and key.
		AWSCredentials aws = new BasicAWSCredentials(swfAccessId, swfSecretKey);
				
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(aws);
			
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(region)));
		dynamoDb = new DynamoDB(dbClient);
	}
	
	
	/************************************************
	 * 	Getting Credentials for Amazon DB Client.	*
	 * @return 										*			
	 ************************************************/
	
	//	Updates the client for Lambda services (Does not have instance credentials).
	public DynamoDB getDbForLambda ()
	{
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient();
			
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(System.getenv("Region"))));
		dynamoDb = new DynamoDB(dbClient);
		return dynamoDb;
	}
	
	//	Updates the client for EC2 using instance credentials.
	public DynamoDB getDbForEC2 ()
	{
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(new InstanceProfileCredentialsProvider(false));
		dynamoDb = new DynamoDB(dbClient);
		
		return dynamoDb;
	}
	
	//	Updates the client for EC2 by passing credentials.
	public DynamoDB getDbWithCredentials (String swfAccessId, String swfSecretKey)
	{
		//	Create credentials using hte access id and key.
		AWSCredentials aws = new BasicAWSCredentials(swfAccessId, swfSecretKey);
				
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(aws);
			
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(System.getenv("Region"))));
		dynamoDb = new DynamoDB(dbClient);
		
		return dynamoDb;
	}
	
	//	Updates the client for EC2 by passing credentials and region.
	public DynamoDB getDbWithCredentials (String swfAccessId, String swfSecretKey, String region)
	{
		//	Create credentials using hte access id and key.
		AWSCredentials aws = new BasicAWSCredentials(swfAccessId, swfSecretKey);
				
		//	Create a dynamo DB client.
		dbClient = new AmazonDynamoDBClient(aws);
			
		//	Set the region for the client and get the region dynamically as an envorinment variable. 
		dbClient.setRegion(Region.getRegion(Regions.valueOf(region)));
		dynamoDb = new DynamoDB(dbClient);
		
		return dynamoDb;
	}
	
	
	/************************************************
	 * 	Table.										*		
	 ************************************************/
	
	//	Select a table.
	public void setTable (String tableName)
	{
		this.tableName = tableName;
		this.table = dynamoDb.getTable(tableName);
	}
	
	//	Get the table.
	public Table getTable (String tableName)
	{
		return dynamoDb.getTable(tableName);
	}
	
	//	Get the table.
	public void updateTable ()
	{
		table.putItem(this.item);
	}
	
	
	/************************************************
	 * 	Item.										*		
	 ************************************************/

	//	Reset This Item.
	public void resetItem ()
	{
		this.item = new Item();
	}
	
	//	Set This Item.
	public void setItem (Item item)
	{
		this.item = item;
	}
	
	//	Return this Item.
	public Item getThisItem ()
	{
		return this.item;
	}
	
	//	Return a new Item.
	public Item getNewItem ()
	{
		return new Item();
	}
	
	//	Return a new Item.
	public Item newItem ()
	{
		this.item = new Item();
		return this.item;
	}
	
	//	Find an existing item given key and value.
	public Item findAndGetItem (String key, String value)
	{
		return table.getItem(key, value);
	}
	
	//	Find an existing item given key and value.
	public Item findAndGetItemWithPrimaryKey (String key, String value)
	{
		return table.getItem(new PrimaryKey().addComponent(key, value));
	}
	
	//	Find an existing item given key and value.
	public void findItem (String key, String value)
	{
		this.item = table.getItem(key, value);
	}
	
	//	Find an existing item given key and value.
	public void findItemWithPrimaryKey (String key, String value)
	{
		this.item = table.getItem(new PrimaryKey().addComponent(key, value));
	}

	//	Add a new (key,value) to item.
	public <T> void addToItem (String key, T value)
	{
		item.with(key, value);
	}
	
	//	Deletes an new Item.
	public DeleteItemOutcome deleteItem (String key, String value)
	{
		return table.deleteItem(new PrimaryKey().addComponent(key, value));
	}
	
	//	Update an existing Item.
	@SuppressWarnings("static-access")
	public UpdateItemOutcome updateItem (String key, String value, String expression, String nameMap, String valueMap)
	{
		/*	The updateItem method behaves as follows:
		 * 	If an item does not exist (no item in the table with the specified primary key), updateItem adds a new item to the table.
		 * 	If an item exists, updateItem performs the update as specified by the UpdateExpression parameter.
		 */
		UpdateItemSpec updateItemSpec = new UpdateItemSpec()
				.withPrimaryKey(key, value)
				.withUpdateExpression(expression)
				.withNameMap(new NameMap().with(new RegexOperations().getValue(expression, "set ([#a-zA-Z0-9]+)=([:a-zA-Z0-9]+)", 1), nameMap))
				.withValueMap(new ValueMap().withString(new RegexOperations().getValue(expression, "set ([#a-zA-Z0-9]+)=([:a-zA-Z0-9]+)", 2), valueMap))
				.withReturnValues(ReturnValue.ALL_NEW);
		
		return table.updateItem(updateItemSpec);
	}
	
	//	Put item into the table.
	public PutItemResult putItem (String tableName, Map<String, AttributeValue> item)
	{
		return dbClient.putItem(tableName, item);
	}
	
	//	Put item into the table.
	public void putItems (String tableName, List<Map<String, AttributeValue>> items)
	{
		for(Map<String, AttributeValue> item : items)
			dbClient.putItem(tableName, item);
	}
	
	//	Return a list of items.
	public List<Map<String, AttributeValue>> listItems()
	{
		List<Map<String, AttributeValue>> items = new ArrayList<Map<String, AttributeValue>>();
		Map<String, AttributeValue> exclusiveStartKey = null;
		while(true)
		{
			ScanRequest scanRequest;
			if(exclusiveStartKey != null)
				scanRequest = new ScanRequest()
					.withTableName(tableName)
					.withExclusiveStartKey(exclusiveStartKey);
			
			else
				scanRequest = new ScanRequest()
				.withTableName(tableName);
			
			ScanResult result = dbClient.scan(scanRequest);
		 	
			items.addAll(result.getItems());
			
			if(result.getLastEvaluatedKey() == null) break; 
			
			exclusiveStartKey = result.getLastEvaluatedKey();
		}
		
		return items;
	}
	
	/************************************************
	 * 	tableName.									*		
	 ************************************************/
	
	public String getTableName()
	{
		return this.tableName;
	}
	
	public TableCollection<ListTablesResult> listTableNames()
	{
		return dynamoDb.listTables();
	}
	
	/************************************************
	 * 	Clear Table.								*		
	 ************************************************/
	
	

	/************************************************
	 * 	Check Result of dynamo entries.				*		
	 ************************************************/
	
	public boolean verifyUpdateItemForString (UpdateItemOutcome outcome, String key, String value)
	{
		UpdateItemResult result = outcome.getUpdateItemResult();

		if(result.getAttributes().containsKey(key))
		{
			if(result.getAttributes().get(key).getS().equals(value))
			{
				return true;
			}
		}
		
		return false;
	}
	
}