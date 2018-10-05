'use strict';
const Generator = require('yeoman-generator');
const chalk = require('chalk');
const yosay = require('yosay');

module.exports = class extends Generator {
  prompting() {
    // Have Yeoman greet the user.
    this.log(
      yosay(
        chalk.green(
          '     .++++++-            \n' +
            '     .///++++.           \n' +
            '         :+++/`          \n' +
            '          /+++/`         \n' +
            '         -/++++/         \n' +
            '       `:+++++++:        \n' +
            '      ./+++/-++++-       \n' +
            '     -++++:` -++++-      \n' +
            '   `/+++/.    :++++.     \n' +
            '  -/+++:`      :+++/--:/`\n' +
            '`:+++/.        /+++++++:\n' +
            '.....`          `//:-.`` ' +
            chalk.yellow(
              "\n Welcome to AWS Lambda Generator for Maven\n\n Let's get started\n\n"
            )
        )
      )
    );

    const prompts = [
      // Project Name
      {
        type: 'input',
        name: 'projectName',
        message: 'Enter the name of your project(same as Github Project)',
        default: 'Project Name'
      },
      // Package Name
      {
        type: 'input',
        name: 'packageName',
        message: 'Enter default package name:',
        default: 'com.myapp'
      },
      // Application Name
      {
        type: 'input',
        name: 'applicationName',
        message: 'Enter the name of your Application/Lambda function',
        default: 'Application'
      },

      // Description
      {
        type: 'input',
        name: 'description',
        message: 'Enter a brief description of your lambda',
        default:
          'Nulla sit ut in culpa pariatur adipisicing quis qui amet est dolor est minim ea.'
      },

      // Class Name
      {
        type: 'input',
        name: 'className',
        message: 'Enter the name of your class',
        default: 'Class'
      },

      // Invoker Type
      {
        type: 'list',
        name: 'lambdaInvoker',
        message: 'How do you want to invoke the lambda?',
        choices: [
          {
            name: 'Default'
          },
          {
            name: 'APIGateway'
          },
          {
            name: 'SNS'
          },
          {
            name: 'S3',
            disabled: 'Not available yet'
          },
          {
            name: 'DynamoDb',
            disabled: 'Not available yet'
          },
          {
            name: 'Scheduled',
            disabled: 'Not available yet'
          }
        ],
        filter: function(val) {
          return val.toLowerCase();
        }
      },

      // Libraries
      {
        type: 'checkbox',
        name: 'libraries',
        message: 'Choose the libraries you want to integrate:',
        choices: [
          {
            name: 'S3',
            value: 's3'
          },

          {
            name: 'SQS',
            value: 'sqs'
          },

          {
            name: 'DynamoDB',
            value: 'dynamodb'
          },

          {
            name: 'CloudFormation',
            value: 'cloudformation'
          },

          {
            name: 'Glue',
            value: 'glue'
          }
        ]
      },

      {
        type: 'input',
        name: 'bucketName',
        message: 'Enter the name of the Bucket your artifacts will be stored in',
        default: 'lambda-artifacts'
      },

      {
        type: 'input',
        name: 'stage',
        message: 'Enter the name of the stage you are creating this lambda for',
        default: 'dev'
      },
      // AWS Version
      {
        type: 'input',
        name: 'awsVersion',
        message: 'Enter AWS Version to use:',
        default: '1.11.257'
      }
    ];

    return this.prompt(prompts).then(props => {
      // To access props later use this.props.someAnswer;
      var hasAwsServices = function(awsServicesStarter) {
        return props.libraries.indexOf(awsServicesStarter) !== -1;
      };

      this.props = props;
      this.props.invokerAPIGateway = false;
      this.props.invokerS3 = false;
      this.props.invokerDynamoDb = false;
      this.props.invokerSNS = false;
      this.props.invokerScheduled = false;
      this.props.invokerDefault = false;
      // Set up the props for the invoker service
      switch (this.props.lambdaInvoker) {
        case 'apigateway':
          this.props.invokerAPIGateway = true;
          break;
        case 's3':
          this.props.invokerS3 = true;
          break;
        case 'dynamodb':
          this.props.invokerDynamoDb = true;
          break;
        case 'sns':
          this.props.invokerSNS = true;
          break;
        case 'scheduled':
          this.props.invokerScheduled = true;
          break;
        case 'default':
          this.props.invokerDefault = true;
          break;
        default:
          this.props.invokerDefault = true;
      }

      // Set up the props for all the library services
      this.props.s3 = hasAwsServices('s3');
      this.props.sqs = hasAwsServices('sqs');
      this.props.dynamodb = hasAwsServices('dynamodb');
      this.props.cloudformation = hasAwsServices('cloudformation');
      this.props.glue = hasAwsServices('glue');
    });
  }

  writing() {
    var packagePath = this.props.packageName.split('.').join('/');
    var folderName = this.props.projectName;
    var amazonWebServicePath =
      folderName + '/src/main/java/' + packagePath + '/utils/AmazonWebServices/';
    var mainPath = folderName + '/src/main/java/' + packagePath + '/';
    var lambdaUtilsPath =
      folderName + '/src/main/java/' + packagePath + '/utils/LambdaUtils/';
    /**
     * Create files if libraries for the following are wanted
     * S3             - S3.java
     * DynamoDB       - DynamoDB.java
     * SQS            - SQS.java
     * CloudFormation - CloudFormation.java
     * Glue           - Glue.java
     */
    if (this.props.s3) {
      this.fs.copyTpl(
        this.templatePath('AmazonServices/S3.java'),
        this.destinationPath(amazonWebServicePath + 'S3.java'),
        this.props
      );
    }

    if (this.props.dynamodb) {
      this.fs.copyTpl(
        this.templatePath('AmazonServices/DynamoDb.java'),
        this.destinationPath(amazonWebServicePath + 'DynamoDb.java'),
        this.props
      );
    }

    if (this.props.sqs) {
      this.fs.copyTpl(
        this.templatePath('AmazonServices/SQS.java'),
        this.destinationPath(amazonWebServicePath + 'SQS.java'),
        this.props
      );
    }

    if (this.props.cloudformation) {
      this.fs.copyTpl(
        this.templatePath('AmazonServices/CloudFormation.java'),
        this.destinationPath(amazonWebServicePath + 'CloudFormation.java'),
        this.props
      );
    }

    if (this.props.glue) {
      this.fs.copyTpl(
        this.templatePath('AmazonServices/Glue.java'),
        this.destinationPath(amazonWebServicePath + 'Glue.java'),
        this.props
      );
    }

    if (this.props.invokerAPIGateway) {
      this.fs.copyTpl(
        this.templatePath('LambdaUtils/Request.java'),
        this.destinationPath(lambdaUtilsPath + 'Request.java'),
        this.props
      );
      this.fs.copyTpl(
        this.templatePath('LambdaUtils/Response.java'),
        this.destinationPath(lambdaUtilsPath + 'Response.java'),
        this.props
      );
    }

    this.fs.copyTpl(
      this.templatePath('README.md'),
      this.destinationPath(folderName + '/README.md'),
      this.props
    );

    this.fs.copyTpl(
      this.templatePath('main.java'),
      this.destinationPath(mainPath + this.props.className + '.java'),
      this.props
    );

    this.fs.copyTpl(
      this.templatePath('pom.xml'),
      this.destinationPath(folderName + '/pom.xml'),
      this.props
    );
  }
};
