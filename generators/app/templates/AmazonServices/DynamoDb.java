package <%=packageName%>.utils.AmazonWebServices;

/**
 * 	AWS Cloudformation Library.		
 * 
 * 	@author Kartik
 *  @contributors Mohsiur
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

import com.comcast.videoplatform.utils.Operations.RegexOperations;


public class DynamoDb 
{
	/*	Attributes required for successful dynamo DB operations.
	 * 	dbClient	-->	Dynamo Client.
	 * 	table	-->	Table name.
	 * 	item	-->	Entry in the table.
	 */
	private AmazonDynamoDB dynamoDb = null;
	private Table table = null;
	private Item item = null;
	
	/*	Variables describing the table.
	 * 	tableName	-->	Table name.
	 */
	private String tableName;
	
	public DynamoDb ()
	{
		//	Create a dynamo DB client.
		dynamoDb = AmazonDynamoDBClientBuilder.defaultClient();
	}
	
	
	/************************************************
	 * 	Table.										*		
	 ************************************************/
	
	
	// Create a table
	public void createTable(String tableName, Map<String, String> tableKeySchema, Map<String, String> tableAttributes, Long readCapacityUnits, Long writeCapacityUnits ) {
		CreateTableRequest createTableRequest = new CreateTableRequest();
		// Set the table Name
		createTableRequest.setTableName(tableName);
		
		// Set the Key Schema for the tables
		List<KeySchemaElement> keySchema = setKeySchemaElement(tableKeySchema);
		createTableRequest.setKeySchema(keySchema);
		
		// Set the Attribute Definitions for the tables
		List<AttributeDefinition> attributeDefinitions = setAttributeDefinitions(tableAttributes);
		createTableRequest.setAttributeDefinitions(attributeDefinitions);
		
		// Set the Provision Throughput
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(readCapacityUnits,writeCapacityUnits);
		createTableRequest.setProvisionedThroughput(provisionedThroughput);
		
		// Create The Table
		dynamoDb.createTable(createTableRequest);
	}
	
	// Delete the Table Name
	public boolean deleteTable(String tableName) {
		// Delete the table
		try {
			dynamoDb.deleteTable(tableName);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	/**
	 * Set a list of Attribute Definitions
	 * @param tableAttributes
	 * @return
	 */
	private List<AttributeDefinition> setAttributeDefinitions(Map<String, String> tableAttributes) {
		List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
		ScalarAttributeType attributeType = null;
		
		for(String attribute : tableAttributes.keySet()) {
			// Set the Attribute Name
			AttributeDefinition attributeDefinition = new AttributeDefinition();
			attributeDefinition.setAttributeName(attribute);
			
			// Set the Attribute Type
			if(tableAttributes.get(attribute) == "S")
				attributeType = ScalarAttributeType.S;
			else if(tableAttributes.get(attribute) == "B")
				attributeType = ScalarAttributeType.B;
			else if(tableAttributes.get(attribute) == "N")
				attributeType = ScalarAttributeType.N;
			attributeDefinition.setAttributeType(attributeType);
			
			// Add to List
			attributeDefinitions.add(attributeDefinition);
		}
		return attributeDefinitions;
	}

	/**
	 * Set a List of Key Schema Elements
	 * @param tableKeySchema
	 * @return
	 */
	private List<KeySchemaElement> setKeySchemaElement(Map<String, String> tableKeySchema) {
		List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
		
		for(String attributeName : tableKeySchema.keySet()) {
			KeySchemaElement keySchemaElement = new KeySchemaElement();
			if(tableKeySchema.get(attributeName) == "HASH") {
				keySchemaElement.setAttributeName(attributeName);
				keySchemaElement.setKeyType(KeyType.HASH);
			}
			else if(tableKeySchema.get(attributeName) == "RANGE") {
				keySchemaElement.setAttributeName(attributeName);
				keySchemaElement.setKeyType(KeyType.RANGE);
			}
			keySchema.add(keySchemaElement);
		}
		
		return keySchema;
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
		return dynamoDb.putItem(tableName, item);
	}
	
	//	Put item into the table.
	public void putItems (String tableName, List<Map<String, AttributeValue>> items)
	{
		for(Map<String, AttributeValue> item : items)
			dynamoDb.putItem(tableName, item);
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
			
			ScanResult result = dynamoDb.scan(scanRequest);
		 	
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
	
	public List<String> listTableNames(String tablePrefix)
	{
		try {
			return dynamoDb.listTables(tablePrefix).getTableNames();
		}
		catch(Exception e) {
			return null;
		}
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