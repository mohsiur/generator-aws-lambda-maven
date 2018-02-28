'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      projectName: 'S3Test',
      packageName: 'com.app.myapp',
      libraries: 's3',
      className: 'Application',
      bucketName: 'lambda-artifacts',
      stage: 'dev',
      stackName: 'MainDefault-Lambda'
    });
  });

  it('creates files for s3 tests', () => {
    assert.file([
      'pom.xml',
      'S3Test/src/main/java/com/app/myapp/utils/AmazonWebServices/S3.java'
    ]);
  });

  it('file info for s3 libraries', () => {
    assert.fileContent('pom.xml', 'aws-java-sdk-s3');
    assert.fileContent(
      'S3Test/src/main/java/com/app/myapp/utils/AmazonWebServices/S3.java',
      'package com.app.myapp.utils.AmazonWebServices'
    );
    assert.fileContent(
      'S3Test/src/main/java/com/app/myapp/Application.java',
      'private static S3 s3 = new S3();'
    );
    assert.fileContent(
      'S3Test/src/main/java/com/app/myapp/Application.java',
      'import com.app.myapp.utils.AmazonWebServices.S3'
    );
  });
});
