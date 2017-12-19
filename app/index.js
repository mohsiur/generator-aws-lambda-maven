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
	// 3. Application Name
	// 4. Repo Name
	// 5. AWS Services
	// 6. Test Cases
	// 7. CI/CD platform
	// 8.
	// 9.
	// 10.

	var prompts = [

		// AWS Version
		{
			type 	: 'string',
			name 	: 'awsVersion',
			message	: 'Enter AWS Version to use:',
			default	: '1.11.250' 
		},

		{
			type	: 'string',
			name	: 'packageName',
			message	: 'Enter default package name:',
			default	: 'com.myapp'
		},

		{
			type 	: 'checkbox',
			name 	: 'component',
			message : 'Choose the type of component you are building :',
			choices : [
				{
					name 	: 'WebService',
					value	: 'webservice'
				},

				{
					name    : 'WorkFlow',
					value	: 'workflow'
				},

				{
					name 	: 'WorkPattern',
					value 	: 'workpattern'
				}
			]
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
			message : 'Enter name of the folder/Service you are in (will be the name of your jar):',
			default	: 'Service Name'
		},

		{
			type	: 'string',
			name 	: 'gitRepoName',
			message : 'Enter Github Repo URL :',
			default : 'https://github.com/'
		},

		{
			type	: 'string',
			name 	: 'branchName',
			message : 'Enter the Branch Name :',
			default : 'master'
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
				},

				{
					name 	: 'Glue',
					value	: 'glue'
				}
			]
		},

		{
			type	: 'checkbox',
			name 	: 'stage',
			message : 'Choose the testing scenarios:',
			choices : [
				{
					name 	: 'Acceptance',
					value	: 'acceptance'
				},

				{
					name    : 'Integration',
					value	: 'integration'
				}
			]
		},

		{
			type	: 'checkbox',
			name 	: 'continuosIntegration',
			message : 'Enter name of CI/CD platform:',
			choices : [
				{
					name 	: 'Jenkins',
					value	: 'jenkins'
				}
			]
		},

		{
			type	: 'string',
			name 	: 'credentialsId',
			message : 'Enter your aws credentials :',
			default : 'uuid'
		},

		{
			type	: 'string',
			name 	: 'notificationEmail',
			message : 'Enter the email you want to send notifications to :',
			default : 'notifications@email.com'
		},

		{
			type	: 'string',
			name 	: 'prefix',
			message : 'Enter the suggested prefix name seeing during launch of CFT:',
			default : 'heavywater'
		},

		{
			type	: 'string',
			name 	: 'suffix',
			message : 'Enter the suggested suffix name seeing during launch of CFT:',
			default : 'mohsiur'
		}

	];

	this.prompt(prompts, function(props){
		this.awsVersion 			= props.awsVersion;
		this.packageName 			= props.packageName;
		this.baseName 				= props.baseName;
		this.component 				= props.component;
		this.awsServices 			= props.awsServices;
		this.applicationName 		= props.applicationName;

		this.stage					= props.stage;
		this.continuosIntegration 	= props.continuosIntegration;

		this.gitRepoName			= props.gitRepoName;
		this.branchName				= props.branchName;

		this.credentialsId			= props.credentialsId;
		this.notificationEmail 		= props.notificationEmail;
		this.prefix					= props.prefix;
		this.suffix					= props.suffix;


		var hasAwsServices = function(awsServicesStarter){
			return props.awsServices.indexOf(awsServicesStarter) !== -1;
		};

		var hasStage = function(stageStarter){
			return props.stage.indexOf(stageStarter) !== -1;
		};
		var hasContinuosIntegration = function(continuosIntegrationStarter){
			return props.continuosIntegration.indexOf(continuosIntegrationStarter) !== -1;
		};

		var hasComponent = function(componentStarter){
			return props.component.indexOf(componentStarter) !== -1;
		};

		this.webservice 	= hasComponent('webservice');
		this.workpattern 	= hasComponent('workpattern');
		this.workflow 		= hasComponent('workflow');

		this.acceptance 	= hasStage('acceptance');
		this.integration 	= hasStage('integration');

		this.jenkins 		= hasContinuosIntegration('jenkins');

		this.s3 			= hasAwsServices('s3');
		this.sqs 			= hasAwsServices('sqs');
		this.swf 			= hasAwsServices('swf');
		this.sns 			= hasAwsServices('sns');
		this.dynamodb 		= hasAwsServices('dynamodb');
		this.cloudformation = hasAwsServices('cloudformation');
		this.glue			= hasAwsServices('glue');

		cb();
	}.bind(this));

};

lambdaGenerator.prototype.app = function app() {
	var packageFolderSplit 	= this.packageName.split('.');
	var applicationName 	= this.applicationName;
	if(packageFolderSplit.indexOf('com') > -1)
		packageFolderSplit.shift();
	var basePageFolder		= packageFolderSplit.join('/');
	
	//var packageFolder 		= this.packageName.replace(/\./g, '/');
	//var mainFolder			= this.basePath + '/' + this.baseName + '/';
	var srcDir 				= this.baseName +'/src/main/java/' + basePageFolder;

	mkdirp(srcDir);

	// Set pom.xml file
	this.template('pom.xml', this.baseName + '/pom.xml');
	// Set Application.java
	var applicationDir = srcDir + '/' + applicationName
	mkdirp(applicationDir)
	this.template('java/Application.java', applicationDir + "/" + applicationName + ".java");

	if(this.awsServices){
		var awsServicesDir		= srcDir + '/utils/AmazonWebServices'; 
		mkdirp(awsServicesDir);

		if(this.s3) this.template('java/S3.java', awsServicesDir+'/S3.java');
		if(this.sqs) this.template('java/SQS.java', awsServicesDir+'/SQS.java');
		if(this.dynamodb){
			var operationsDir = srcDir + '/utils/Operations';
			mkdirp(operationsDir);
			this.template('java/DynamoDb.java', awsServicesDir+'/DynamoDb.java');
			this.template('java/RegexOperations.java', operationsDir+'/RegexOperations.java');
		}
		if(this.cloudformation) this.template('java/CloudFormation.java', awsServicesDir+'/CloudFormation.java');
	}

	var lambdaServiceDir = srcDir + '/utils/LambdaUtils';
	mkdirp(lambdaServiceDir);

	this.template('java/Response.java', lambdaServiceDir+'/Response.java');
	this.template('java/Request.java', lambdaServiceDir+'/Request.java');

	var srcTestDir = this.baseName + "/src/test/java/" + basePageFolder;
	mkdirp(srcTestDir);

	if(this.acceptance){
		this.template('tests/AcceptanceCaseSteps.java', srcTestDir+"/acceptance/steps/CaseSteps.java");
		this.template('tests/RunAcceptanceTest.java', srcTestDir+"/runner/RunAcceptanceTest.java");
		this.template('tests/TestCase.feature', this.baseName+"/src/test/resources/cucumber/TestCaseAcceptance.feature");
		this.template('jenkins/webserviceacceptance.dsld', this.baseName + "/" + applicationName+"Acceptance.dsld");
		this.template('stackDescriptor.json', this.baseName + "/stackDescriptor.json");
	}

	if(this.integration){
		this.template('tests/IntegrationCaseSteps.java', srcTestDir+"/integration/steps/CaseSteps.java");
		this.template('tests/RunIntegrationTest.java', srcTestDir+"/runner/RunIntegrationTest.java");
		this.template('tests/TestCase.feature', this.baseName+"/src/test/resources/cucumber/TestCaseIntegration.feature");
		this.template('jenkins/integration.dsld', this.baseName + "/" + applicationName+"Integration.dsld");
		this.template('stackDescriptor.json', this.baseName + "/integrationStackDescriptor.json");
	}

	if(this.webservice){
		this.template('Readme/Webservice.md', this.baseName + "/README.md");
	}

	if(this.workflow){
		this.template('Readme/Workflow.md', this.baseName + "/README.md");
	}

	if(this.workpattern){
		this.template('Readme/Workpattern.md', this.baseName + "/README.md");
	}
	console.log("A generic template has been created, please add the following files to launch into AWS, \n"
				+ "CloudFormation template, Scripts folder");
	//this.config.set('packageName', this.packageName);
    //this.config.set('packageFolder', packageFolder);
};

//lambdaGenerator.prototype.projectfiles = function projectfiles() {};