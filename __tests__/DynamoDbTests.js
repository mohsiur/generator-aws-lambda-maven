'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      projectName: 'DynamoDBTest',
      packageName: 'com.app.myapp',
      libraries: 'dynamodb',
      bucketName: 'lambda-artifacts',
      stage: 'dev',
      stackName: 'MainDefault-Lambda'
    });
  });

  it('creates files for dynamodb tests', () => {
    assert.file([
      'pom.xml',
      'DynamoDBTest/src/main/java/com/app/myapp/utils/AmazonWebServices/DynamoDb.java'
    ]);
  });

  it('file info for dynamodb libraries', () => {
    assert.fileContent('pom.xml', 'aws-java-sdk-dynamodb');
    assert.fileContent(
      'DynamoDBTest/src/main/java/com/app/myapp/utils/AmazonWebServices/DynamoDb.java',
      'package com.app.myapp.utils.AmazonWebServices'
    );
  });
});
