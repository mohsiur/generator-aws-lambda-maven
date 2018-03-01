# generator-aws-lambda-maven [![Build Status](https://travis-ci.org/mohsiur/generator-aws-lambda-maven.svg?branch=master)](https://travis-ci.org/mohsiur/generator-aws-lambda-maven)

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
- [ ] API Gateway
- [ ] SNS
- [ ] DynamoDB
- [ ] Scheduler
- [ ] S3

## To-Do List

- Add integration with other Lambda invokers
- Add more libraries

## Contributors

* [Kartik Reddy](https://github.com/)
* [Mohsiur Rahman](https://github.com/mohsiur)

## Contributing

Contributions and feedback are welcome! Proposals and pull requests will be considered and responded to. For more information, see the [CONTRIBUTING](CONTRIBUTING.md) file.
