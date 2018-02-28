'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      projectName: 'SQSTest',
      packageName: 'com.app.myapp',
      libraries: 'sqs',
      bucketName: 'lambda-artifacts',
      stage: 'dev',
      stackName: 'MainDefault-Lambda'
    });
  });

  it('creates files for sqs tests', () => {
    assert.file([
      'pom.xml',
      'SQSTest/src/main/java/com/app/myapp/utils/AmazonWebServices/SQS.java'
    ]);
  });

  it('file info for sqs libraries', () => {
    assert.fileContent('pom.xml', 'aws-java-sdk-sqs');
    assert.fileContent(
      'SQSTest/src/main/java/com/app/myapp/utils/AmazonWebServices/SQS.java',
      'package com.app.myapp.utils.AmazonWebServices'
    );
  });
});
