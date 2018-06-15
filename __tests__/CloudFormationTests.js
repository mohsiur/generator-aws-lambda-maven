'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      projectName: 'CFTest',
      packageName: 'com.app.myapp',
      libraries: 'cloudformation',
      bucketName: 'lambda-artifacts',
      stage: 'dev',
      stackName: 'MainDefault-Lambda',
      applicationName: 'MainDefaultApp',
      className: 'CFTestClass',
      lambdaInvoker: 'default'
    });
  });

  it('creates files for cloudformation tests', () => {
    assert.file([
      'CFTest/pom.xml',
      'CFTest/src/main/java/com/app/myapp/utils/AmazonWebServices/CloudFormation.java',
      'CFTest/MainDefaultApp.template',
      'CFTest/src/main/java/com/app/myapp/CFTestClass.java'
    ]);
  });

  it('file info for cloudformation libraries', () => {
    assert.fileContent('CFTest/pom.xml', 'aws-java-sdk-cloudformation');
    assert.fileContent(
      'CFTest/src/main/java/com/app/myapp/utils/AmazonWebServices/CloudFormation.java',
      'package com.app.myapp.utils.AmazonWebServices'
    );
    assert.fileContent(
      'CFTest/src/main/java/com/app/myapp/CFTestClass.java',
      'private static CloudFormation cft = new CloudFormation()'
    );
  });
});
