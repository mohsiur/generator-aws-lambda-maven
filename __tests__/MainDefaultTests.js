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
      libraries: 's3'
    });
  });

  it('creates files for default tests', () => {
    assert.file(['pom.xml', 'MainDefault/src/main/java/com/app/myapp/Test.java']);
  });

  it('file info for default invoker', () => {
    assert.fileContent(
      'MainDefault/src/main/java/com/app/myapp/Test.java',
      'package com.app.myapp.Test'
    );
    assert.fileContent(
      'MainDefault/src/main/java/com/app/myapp/Test.java',
      'public class Test'
    );
    assert.fileContent(
      'MainDefault/src/main/java/com/app/myapp/Test.java',
      'public Response handleRequest(Object input, Context context)'
    );
  });
});
