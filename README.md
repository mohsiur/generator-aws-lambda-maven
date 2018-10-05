# generator-aws-lambda-maven [![Build Status](https://travis-ci.org/mohsiur/generator-aws-lambda-maven.svg?branch=master)](https://travis-ci.org/mohsiur/generator-aws-lambda-maven) [![Open Source Helpers](https://www.codetriage.com/mohsiur/generator-aws-lambda-maven/badges/users.svg)](https://www.codetriage.com/mohsiur/generator-aws-lambda-maven)

A Yeoman Generator Building a Lambda function which can be invoked by any type of Object, future invokers will be added.

## Installation

Install [npm](https://nodejs.org/en/) on your local machine

After installation run the following commands in your terminal

- `npm install -g yo`
- `npm install -g generator-aws-lambda-maven`

## Creating a new project

To create a new project, first be in the directory of all your projects then run the following command and follow the respective instructions as it appears on your command line.

`yo aws-lambda-maven`

## Lambda Invokers

The following AWS Resources can invoke the Lambda when deployed.

- [x] Default (Any String or Java object)
- [x] API Gateway
- [x] SNS
- [ ] DynamoDB
- [ ] Scheduler
- [ ] S3
- [ ] SQS

## To-Do List

- Add integration with other Lambda invokers
- Add more libraries

## Contributors

* [Mohsiur Rahman](https://github.com/mohsiur)
* [Kartik Reddy](https://github.com/)

## Contributing

Contributions and feedback are welcome! Proposals and pull requests will be considered and responded to. For more information, see the [CONTRIBUTING](CONTRIBUTING.md) file.
