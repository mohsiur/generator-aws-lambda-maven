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
        list: ['apigateway', 's3', 'scheduled']
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
      this.props = props;
    });
  }

  writing() {
    this.fs.copyTpl(
      this.templatePath('dummyfile.txt'),
      this.destinationPath('dummyfile.txt'),
      this.props
    );
    this.fs.copyTpl(
      this.templatePath('dummyfile2.txt'),
      this.destinationPath('src/main/dummfile2.txt'),
      this.props
    );

    this.fs.copyTpl(
      this.templatePath('dummyfile3.txt'),
      this.destinationPath('src/main/dummyfile3.txt'),
      this.props
    );
  }

  install() {
    this.installDependencies();
  }
};
