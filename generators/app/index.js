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
      // Application Name
      {
        type: 'input',
        name: 'applicationName',
        message: 'Enter the name of your application',
        default: 'Some Application'
      },
      // Package Name
      {
        type: 'input',
        name: 'packageName',
        message: 'Enter default package name:',
        default: 'com.myapp'
      },
      // Class Name
      {
        type: 'input',
        name: 'className',
        message: 'Enter the name of your class',
        default: 'Application'
      },
      // Invoker Type
      {
        type: 'list',
        name: 'lambdaInvoker',
        message: 'How do you want to invoke the lambda?',
        choices: [
          {
            name: 'APIGateway'
          },
          {
            name: 'Default'
          },
          {
            name: 'S3',
            disabled: 'Not available yet'
          },

          {
            name: 'SNS',
            disabled: 'Not available yet'
          },
          {
            name: 'DynamoDb',
            disabled: 'Not available yet'
          }
        ],
        filter: function(val) {
          return val.toLowerCase();
        }
      },

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
      // Set up the props for the invoker service
      this.props.invokerAPIGateway = this.props.lambdaInvoker;
      this.props.invokerS3 = this.props.lambdaInvoker;
      this.props.invokerDynamoDb = this.props.lambdaInvoker;
      this.props.invokerSNS = this.props.lambdaInvoker;
      this.props.invokerScheduled = this.props.lambdaInvoker;
      this.props.invokerDefault = this.props.lambdaInvoker;

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

    if (this.props.invokerAPIGateway === 'apigateway') {
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
      this.templatePath('main.java'),
      this.destinationPath(mainPath + this.props.className + '.java'),
      this.props
    );

    this.fs.copyTpl(
      this.templatePath('pom.xml'),
      this.destinationPath('pom.xml'),
      this.props
    );
  }
};
