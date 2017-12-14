'use strict'

var util = require('util')
var path = require('path')
var yeoman = require('yeoman-generator');
var chalk = require('chalk');
var mkdirp = require('mkdirp');

var lambdaGenerator = module.exports = function lambdaGenerator(args, options, config){
	yeoman.generators.Base.apply(this, arguments);
};

util.inherits(lambdaGenerator, yeoman.generators.Base);

lambdaGenerator.prototype.askFor = function askFor() {
	
	var cb = this.async();

	console.log(chalk.green(
		'\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@' + 
		'\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@' + 
		'\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@' + 
		'\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@' +
		'\n%%%%%%%%%%%%%@@@@@@@@%%%%%%%&%*.   .(%@%%%%%%%%%%' +
		'\n%%%%%%%%%%%%%%///////(&%%@(   , .   ..  %%%%%%%%%' +
		'\n%%%%%%%%%%%%%%////////#%%.    ,,,,.,     .&%%%%%%' +
		'\n%%%%%%%%%%%%%%%%%%(////%  ..  , .   ,,..* ,&%%%%%' +
		'\n%%%%%%%%%%%%%%%%%%%////(,*  *,   *    , *  (%%%%%' +
		'\n%%%%%%%%%%%%%%%%%%%#//(,.  .       .       *%%%%%' +
		'\n%%%%%%%%%%%%%%%%%%#////(.. ,       ,       #%%%%%' +
		'\n%%%%%%%%%%%%%%%%%(/////(..,*. ,.,.  . ,  *(%%%%%%' +
		'\n%%%%%%%%%%%%%%%%(////((/(#...   .. ,   .*%%%%%%%%' +
		'\n%%%%%%%%%%%%%%%/////(%%///(#, ,,,    ./%%%%%%%%%%' +
		'\n%%%%%%%%%%%%%%/////#%%%#////(@%%%%%%%%%%%%%%%%%%%' +
		'\n%%%%%%%%%%%%%/////#%%%%%(////#%%%%%%%%%%%%%%%%%%%' +
		'\n%%%%%%%%%%%#/////%%%%%%%%(////%%%%@@%%%%%%%%%%%%%' +
		'\n%%%%%%%%%@#/////%%%%%%%%%%////(#(//(%%%%%%%%%%%%%' +
		'\n%%%%%%%%&(/////%%%%%%%%%%%#/////////%%%%%%%%%%%%%' +
		'\n%%%%%%%%(////(%%%%%%%%%%%%%#//////(#%%%%%%%%%%%%%' +
		'\n%%%%%%%((((((%%%%%%%%%%%%%%%((%%%%%%%%%%%%%%%%%%%' +
		'\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%' +
		'\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%' +
		'\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%' +
		chalk.yellow('\n Welcome to AWS Lambda Generator for Maven\n\n Let\'s get started\n\n')));

	// Setup all the prompts
	// 1. AWS Version  
	// 2. Package Name
	// 3. Base Name
	// 4. AWS Services
	// 5.
	// 6.
	// 7.
	// 8.
	// 9.
	// 10.

	var prompts = [

		// AWS Version
		{
			type 	: 'string',
			name 	: 'awsVersion',
			message	: 'Enter AWS Version to use:',
			default	: '1.11.247' 
		},

		{
			type	: 'string',
			name	: 'packageName',
			message	: 'Enter default package name:',
			default	: 'com.myapp'
		},

		{
			type	: 'string',
			name 	: 'applicationName',
			message : 'Enter name of the application:',
			default	: 'Application'
		},

		{
			type	: 'string',
			name 	: 'baseName',
			message : 'Enter name of the repo:',
			default	: 'Application'
		},
		
		{
			type	: 'checkbox',
			name 	: 'awsServices',
			message : 'Choose the AWS Services you\'d like to include:',
			choices : [
				{
					name 	: 'S3',
					value	: 's3'
				},

				{
					name    : 'SQS',
					value	: 'sqs'
				},

				{
					name    : 'SWF',
					value	: 'swf'
				},

				{
					name 	: 'SNS',
					value	: 'sns'
				},

				{
					name 	: 'DynamoDB',
					value	: 'dynamodb'
				},

				{
					name 	: 'CloudFormation',
					value	: 'cloudformation'
				}
			]
		}
	];

	this.prompt(prompts, function(props){
		this.awsVersion 		= props.awsVersion;
		this.packageName 		= props.packageName;
	//	this.baseName 			= props.baseName;
	//	this.basePath			= props.basePath;
		this.awsServices 		= props.awsServices;
		this.applicationName 	= props.applicationName;
	//	this.folderName			= props.folderName;

		var hasAwsServices = function(awsServicesStarter){
			return props.awsServices.indexOf(awsServicesStarter) !== -1;
		};

		this.s3 			= hasAwsServices('s3');
		this.sqs 			= hasAwsServices('sqs');
		this.swf 			= hasAwsServices('swf');
		this.sns 			= hasAwsServices('sns');
		this.dynamodb 		= hasAwsServices('dynamodb');
		this.cloudformation = hasAwsServices('cloudformation');

		cb();
	}.bind(this));

};

lambdaGenerator.prototype.app = function app() {
	var packageFolderSplit 	= this.packageName.split('.');
	var applicationName 	= this.applicationName;
	packageFolderSplit.shift();
	var basePageFolder		= packageFolderSplit.join('/');
	
	//var packageFolder 		= this.packageName.replace(/\./g, '/');
	//var mainFolder			= this.basePath + '/' + this.baseName + '/';
	var srcDir 				= 'src/main/java/' + basePageFolder;
	mkdirp(srcDir);

	// Set pom.xml file
	this.template('pom.xml', 'pom.xml');
	// Set Application.java
	var applicationDir = srcDir + '/' + applicationName
	mkdirp(applicationDir)
	this.template('java/Application.java', applicationDir + "/Application.java");

	if(this.awsServices){
		var awsServicesDir		= srcDir + '/utils/AmazonWebServices'; 
		mkdirp(awsServicesDir);

		if(this.s3) this.template('java/S3.java', awsServicesDir+'/S3.java');
		if(this.sqs) this.template('java/SQS.java', awsServicesDir+'/SQS.java');
		if(this.dynamodb){
			var operationsDir = srcDir + '/utils/Operations';
			mkdirp(operationsDir);
			this.template('java/DynamoDB.java', awsServicesDir+'/DynamoDB.java');
			this.template('java/RegexOperations.java', operationsDir+'/RegexOperations.java');
		}
		if(this.cloudformation) this.template('java/CloudFormation.java', awsServicesDir+'/CloudFormation.java');
	}

	var lambdaServiceDir = srcDir + '/utils/LambdaUtils';
	mkdirp(lambdaServiceDir);

	this.template('java/Response.java', lambdaServiceDir+'/Response.java');
	this.template('java/Request.java', lambdaServiceDir+'/Request.java');

	//this.config.set('packageName', this.packageName);
    //this.config.set('packageFolder', packageFolder);
};

//lambdaGenerator.prototype.projectfiles = function projectfiles() {};