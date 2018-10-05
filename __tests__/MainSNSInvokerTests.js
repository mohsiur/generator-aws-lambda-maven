'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      projectName: 'MainSNSInvoker',
      packageName: 'com.app.myapp',
      className: 'Test',
      lambdaInvoker: 'sns',
      libraries: 's3',
      bucketName: 'lambda-artifacts',
      stage: 'dev'
    });
  });

  it('creates files for API Invoker tests', () => {
    assert.file([
      'MainSNSInvoker/pom.xml',
      'MainSNSInvoker/src/main/java/com/app/myapp/Test.java'
    ]);
  });

  it('file info for API Invoker for lambda', () => {
    assert.fileContent(
      'MainSNSInvoker/src/main/java/com/app/myapp/Test.java',
      'import com.amazonaws.services.lambda.runtime.events.SNSEvent;',
      'public class Test',
      'package com.app.myapp',
      'public Response handleRequest(SNSEvent event, Context context)',
      'String input = event.getRecords().get(0).getSNS().getMessage();',
      'aws-java-sdk-sns'
    );
  });
});
