package <%=packageName%>.utils.AmazonWebServices;

/**
 * AWS Glue Lobrary.
 *
 * @author mohsiur
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClientBuilder;
import com.amazonaws.services.glue.model.BatchCreatePartitionRequest;
import com.amazonaws.services.glue.model.BatchCreatePartitionResult;
import com.amazonaws.services.glue.model.Column;
import com.amazonaws.services.glue.model.CreateDatabaseRequest;
import com.amazonaws.services.glue.model.CreateDatabaseResult;
import com.amazonaws.services.glue.model.CreatePartitionRequest;
import com.amazonaws.services.glue.model.CreatePartitionResult;
import com.amazonaws.services.glue.model.CreateTableRequest;
import com.amazonaws.services.glue.model.CreateTableResult;
import com.amazonaws.services.glue.model.Database;
import com.amazonaws.services.glue.model.DatabaseInput;
import com.amazonaws.services.glue.model.DeleteDatabaseRequest;
import com.amazonaws.services.glue.model.GetDatabasesRequest;
import com.amazonaws.services.glue.model.PartitionInput;
import com.amazonaws.services.glue.model.SerDeInfo;
import com.amazonaws.services.glue.model.StorageDescriptor;
import com.amazonaws.services.glue.model.TableInput;

public class Glue {

	private final static Integer MAX_NUMBER = 1000;
	private AWSGlue glueClient;
	private String databaseName;
	private String databaseDescription;
	private String inputFormat;
	private String outputFormat;
	private String serializationLibrary;
	private String integer;
	private String string;
	private String struct;
	private boolean compressed;
	private int numBuckets;
	
	private Map<String, String> serdeParameters;
	private Map<String, String> storageParameters;
	private Map<String, String> tableParameters;

	
	/**
	 * Default Constructor to create a new AWS Glue Client
	 * @param None
	 */
	public Glue() {
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		this.glueClient = AWSGlueClientBuilder.standard().withClientConfiguration(config).withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}
	
	/**
	 * Configuration for Glue paramaters
	 * @param inputFormat - The Input format passed in StorageDescriptor
	 * @param outputFormat - The Output format passed in StorageDescriptor
	 * @param serializationLibrary - The Serialization Library passed in SerDeInfo
	 * @param compressed
	 * @param numBuckets
	 * @param struct
	 * @param serdeParameters
	 * @param storageParameters
	 */
	public void configureGlueForTable(String inputFormat, String outputFormat, String serializationLibrary, boolean compressed, int numBuckets, String struct, Map<String, String> serdeParameters, Map<String, String> tableParameters) {
		this.inputFormat 			= inputFormat;
		this.outputFormat 			= outputFormat;
		this.serializationLibrary 	= serializationLibrary;
		this.serdeParameters 		= serdeParameters;
		this.tableParameters 		= tableParameters;
		this.compressed 				= compressed;
		this.numBuckets 				= numBuckets;
		this.struct 					= struct;
		this.integer					= "int";
		this.string					= "string";
	}
	

	/**
	 * Configuration for Glue paramaters
	 * @param inputFormat - The Input format passed in StorageDescriptor
	 * @param outputFormat - The Output format passed in StorageDescriptor
	 * @param serializationLibrary - The Serialization Library passed in SerDeInfo
	 * @param compressed
	 * @param numBuckets
	 * @param struct
	 * @param serdeParameters
	 * @param storageParameters
	 */
	public void configureGlueForPartition(String inputFormat, String outputFormat, String serializationLibrary, boolean compressed, int numBuckets, String struct, Map<String, String> serdeParameters, Map<String, String> storageParameters) {
		this.inputFormat 			= inputFormat;
		this.outputFormat 			= outputFormat;
		this.serializationLibrary 	= serializationLibrary;
		this.serdeParameters 		= serdeParameters;
		this.storageParameters 		= storageParameters;
		this.struct 					= struct;
		this.compressed 				= compressed;
		this.numBuckets 				= numBuckets;
		this.integer					= "int";
		this.string					= "string";
	}
	/**
	 * Create Batch Partition Result 
	 * 
	 * @return BatchCreatePartitionResult - Creates one or more partitions in a batch operation
	 */
	public BatchCreatePartitionResult createBatchPartition() {
		BatchCreatePartitionRequest request = new BatchCreatePartitionRequest();
		return glueClient.batchCreatePartition(request);
	}
	
	/**
	 * Create Batch Partition Result With CatlogID
	 * @param catalogID - The ID of the catalog in which the partion is to be created.
	 * @return BatchCreatePartitionResult - Creates one or more partitions in a batch operation
	 */
	public BatchCreatePartitionResult createBatchPartitionWithCatalogID(String catalogID) {
		BatchCreatePartitionRequest request = new BatchCreatePartitionRequest();
		return glueClient.batchCreatePartition(request.withCatalogId(catalogID));
	}

	/**
	 * Create Batch Partition Result with DatabaseName
	 * @param catalogID - The ID of the catalog in which the partion is to be created.
	 * @return BatchCreatePartitionResult - Creates one or more partitions in a batch operation
	 */
	public BatchCreatePartitionResult createBatchPartitionWithDatabaseName(String databaseName) {
		BatchCreatePartitionRequest request = new BatchCreatePartitionRequest();
		return glueClient.batchCreatePartition(request.withDatabaseName(databaseName));
	}
	
	/**
	 * Create Batch Partition Result with TableName
	 * @param catalogID - The ID of the catalog in which the partion is to be created.
	 * @return BatchCreatePartitionResult - Creates one or more partitions in a batch operation
	 */
	public BatchCreatePartitionResult createBatchPartitionWithTableName(String tableName) {
		BatchCreatePartitionRequest request = new BatchCreatePartitionRequest();
		return glueClient.batchCreatePartition(request.withTableName(tableName));
	}


	/**
	 * Create a database with the name of database and description
	 * @param databaseName
	 * @param description
	 * @return
	 */
	public CreateDatabaseResult createDatabase(String databaseName, String description) {
		CreateDatabaseRequest request = new CreateDatabaseRequest();
		
		DatabaseInput databaseInput = new DatabaseInput();
		// Set the Database Name and descriptipn
		this.databaseName = databaseName;
		this.databaseDescription = description;
		
		databaseInput.setName(this.databaseName);
		databaseInput.setDescription(this.databaseDescription);
		// Set the Database for Glue
		request.setDatabaseInput(databaseInput);
		
		return glueClient.createDatabase(request);	
	}
	
	/**
	 * Get a List of Strings of All the Databases that exist in the Data catalog
	 * @return List<String> of number of databases that have been created
	 */
	public List<String> getDatabases() {
		GetDatabasesRequest request = new GetDatabasesRequest();

		List<Database> databases = glueClient.getDatabases(request.withMaxResults(MAX_NUMBER)).getDatabaseList();
		List<String> res = new ArrayList<String>();
		
		for(Database db : databases) {
			System.out.println(db.getName());
			res.add(db.getName());
		}
		
		return res;
	}
	
	/**
	 * Delete a database with the name of database
	 * @return boolean if database has been deleted
	 */
	public boolean deleteDatabase(String dbName) {
		DeleteDatabaseRequest request = new DeleteDatabaseRequest();
		
		request.setName(dbName);
	
		glueClient.deleteDatabase(request);
		
		if(getDatabases().contains(dbName)) return false;
		else return true;
	}
	
	/**
	 * Create a Partition Result 
	 * @param databaseName - Name of the database
	 * @param tableName - Name of the table
	 * @param partitionValues - Partition Values
	 * @param colValues - Column Values
	 * @param s3Path - The Location of the S3 Path
	 * @return CreatePartitionResult
	 */
	public CreatePartitionResult createPartition(String databaseName, String tableName, Collection<String> partitionValues, Collection<String> colValues, String s3Path) {
		// Create The Serde Info
		SerDeInfo serdeInfo = createSerdeInfo();
		// Create The Storage Descriptor
		StorageDescriptor storageDescriptor = createStorageDescriptor(colValues, s3Path, serdeInfo);
		// Create The Partition Input
		PartitionInput partitionInput = createPartitionInput(storageDescriptor, partitionValues);
		// Create the Partition Request
		CreatePartitionRequest partitionRequest = createPartitionRequest(databaseName, tableName, partitionInput);

		return glueClient.createPartition(partitionRequest);
	}
	
	/**
	 * Create a SerDeInfo used in other methods
	 * @return SerDeInfo
	 */
	private SerDeInfo createSerdeInfo() {
		SerDeInfo serdeInfo = new SerDeInfo();
		// Set the parameters
		serdeInfo.setParameters(serdeParameters);
		// Set the Serialization Library
		serdeInfo.setSerializationLibrary(serializationLibrary);
		// Return the Serde Info
		return serdeInfo;
	}

	/** 
	 * Create PartitionInput
	 * 
	 * @param storageDescriptor 
	 * @param values
	 * @return
	 */
	private PartitionInput createPartitionInput(StorageDescriptor storageDescriptor, Collection<String> values) {
		PartitionInput partition = new PartitionInput();
		// Set the storage descriptor
		partition.setStorageDescriptor(storageDescriptor);
		// Set the Values
		partition.setValues(values);
		// Return the partition
		return partition;
	}

	/**
	 * Create Storage Descriptor
	 * @param values - The values used for the Column names
	 * @param location - The location of where the file exists
	 * @param serdeInfo - The SerDeInfo
	 * @return
	 */
	private StorageDescriptor createStorageDescriptor(Collection<String> values, String location, SerDeInfo serdeInfo) {
		StorageDescriptor storageDescriptor = new StorageDescriptor();
		// Set the columns from the given value
		List<Column> columns = setColumns(values);
		// Set the columns as a list of columns
		storageDescriptor.setColumns(columns);
		// Set the location
		storageDescriptor.setLocation(location);
		// Set compressed
		storageDescriptor.setCompressed(compressed);
		// Set Input Format
		storageDescriptor.setInputFormat(inputFormat);
		// Set Output Format
		storageDescriptor.setOutputFormat(outputFormat);
		// Set Number of Buckets
		storageDescriptor.setNumberOfBuckets(numBuckets);
		// Set SerdeInfo
		storageDescriptor.setSerdeInfo(serdeInfo);
		// Set Parameters
		if(storageParameters != null)
			storageDescriptor.setParameters(storageParameters);
		// Return the Storage Descriptor
		return storageDescriptor;
	}
	

	/**
	 * Set the columns 
	 * @param values
	 * @return List<Columns>
	 */
	private List<Column> setColumns(Collection<String> values) {
		List<Column> columns = new ArrayList<Column>();
		for(String value : values) {
			Column column = new Column();
			column.setName(value);
			// For Num of Pages type int
			if(value.equalsIgnoreCase("NUM_OF_PAGES")) column.setType(integer);
			// For final output type struct
			else if(value.equalsIgnoreCase("final_output")) column.setType(struct);
			// Default type string
			else column.setType(string);
			// Add the columns
			columns.add(column);
		}
		
		return columns;
	}

	/**
	 * Create a Partition Request
	 * @param databaseName - Database Name
	 * @param tableName - Table Name
	 * @param partitionInput - Partition Input
	 * @return
	 */
	private CreatePartitionRequest createPartitionRequest(String databaseName, String tableName, PartitionInput partitionInput) {
		CreatePartitionRequest partitionRequest = new CreatePartitionRequest();
		// Set the DatabaseName
		partitionRequest.setDatabaseName(databaseName);
		// Set the Table Name
		partitionRequest.setTableName(tableName);
		// Set the Partition Input
		partitionRequest.setPartitionInput(partitionInput);
		// Create The Partition with the Partition Request
		return partitionRequest;
		
	}
	
	/**
	 * Create the Table
	 * @param databaseName - Name of the database where the table needs to go
	 * @param tableName - Name of the table
	 * @param description - Description of the table
	 * @param colValues - The schema of the table
	 * @param partitionValues - The partition keys
	 * @param s3Path - The root S3 Path
	 * @return
	 */
	public CreateTableResult createTable(String databaseName, String tableName, String description, Collection<String> colValues, Collection<String> partitionValues, String s3Path) {
		// Set the partition key values
		List<Column> partitionKeys = setColumns(partitionValues);
		// Create the SerDeInfo
		SerDeInfo serdeInfo = createSerdeInfo();
		// Create the Storage Descriptor
		StorageDescriptor storageDescriptor = createStorageDescriptor(colValues, s3Path, serdeInfo);
		// Create the table input
		TableInput tableInput = createTableInput(tableName, description, storageDescriptor, partitionKeys);
		// Create the table request
		CreateTableRequest tableRequest = createTableRequest(databaseName, tableInput);
		// Create the table
		return glueClient.createTable(tableRequest);
	}

	/**
	 * Create the Table Input
	 * @param tableName
	 * @param description
	 * @param storageDescriptor
	 * @param partitionKeys
	 * @return
	 */
	private TableInput createTableInput(String tableName, String description, StorageDescriptor storageDescriptor, List<Column> partitionKeys) {
		TableInput tableInput = new TableInput();
		// Set the table Name
		tableInput.setName(tableName);
		// Set the table description
		tableInput.setDescription(description);
		// Set the table Storage Descriptor
		tableInput.setStorageDescriptor(storageDescriptor);
		// Set the table Parameters
		tableInput.setParameters(tableParameters);
		// Set the partition keys
		tableInput.setPartitionKeys(partitionKeys);
		// Return the table input
		return tableInput;
	}

	/**
	 * Create the TableRequest
	 * @param databaseName
	 * @param tableInput
	 * @return
	 */
	private CreateTableRequest createTableRequest(String databaseName, TableInput tableInput) {
		CreateTableRequest tableRequest = new CreateTableRequest();
		// Set the database name
		tableRequest.setDatabaseName(databaseName);
		// Set the table input
		tableRequest.setTableInput(tableInput);
		// Return the table request
		return tableRequest;
	}
	
}
