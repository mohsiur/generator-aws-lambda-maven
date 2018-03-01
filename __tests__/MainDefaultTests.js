'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      projectName: 'MainDefault',
      packageName: 'com.app.myapp',
      className: 'Test',
      lambdaInvoker: 'default',
      libraries: 's3',
      bucketName: 'lambda-artifacts',
      stage: 'dev',
      stackName: 'MainDefault-Lambda',
      appilcationName: 'MainDefaultApp'
    });
  });

  it('creates files for default tests', () => {
    assert.file([
      'MainDefault/pom.xml',
      'MainDefault/src/main/java/com/app/myapp/Test.java',
      'MainDefault/README.md'
    ]);
  });

  it('file info for default invoker', () => {
    assert.fileContent(
      'MainDefault/src/main/java/com/app/myapp/Test.java',
      'package com.app.myapp',
      'public class Test',
      'public Response handleRequest(Object input, Context context)'
    );
    assert.fileContent(
      'MainDefault/README.md',
      'any given Object',
      'MainDefault-Lambda',
      'MainDefaultApp'
    );
  });
});
