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
      appilcationName: 'MainDefaultApp'
    });
  });

  it('creates files for cloudformation tests', () => {
    assert.file([
      'pom.xml',
      'CFTest/src/main/java/com/app/myapp/utils/AmazonWebServices/CloudFormation.java',
      'MainDefaultApp.template'
    ]);
  });

  it('file info for cloudformation libraries', () => {
    assert.fileContent('pom.xml', 'aws-java-sdk-cloudformation');
    assert.fileContent(
      'CFTest/src/main/java/com/app/myapp/utils/AmazonWebServices/CloudFormation.java',
      'package com.app.myapp.utils.AmazonWebServices'
    );
  });
});
