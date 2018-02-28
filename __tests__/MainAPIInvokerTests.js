'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      projectName: 'MainAPIGateWayInvoker',
      packageName: 'com.app.myapp',
      className: 'Test',
      lambdaInvoker: 'apigateway',
      libraries: 's3',
      bucketName: 'lambda-artifacts',
      stage: 'dev',
      stackName: 'MainDefault-Lambda'
    });
  });

  it('creates files for API Invoker tests', () => {
    assert.file([
      'pom.xml',
      'MainAPIGateWayInvoker/src/main/java/com/app/myapp/Test.java',
      'MainAPIGateWayInvoker/src/main/java/com/app/myapp/utils/LambdaUtils/Request.java',
      'MainAPIGateWayInvoker/src/main/java/com/app/myapp/utils/LambdaUtils/Request.java'
    ]);
  });

  it('file info for API Invoker for lambda', () => {
    assert.fileContent(
      'MainAPIGateWayInvoker/src/main/java/com/app/myapp/Test.java',
      'import com.app.myapp.utils.LambdaUtils.Request;',
      'import com.app.myapp.utils.LambdaUtils.Response;',
      'public class Test implements RequestHandler<Request, Response>',
      'package com.app.myapp.Test',
      'public Response handleRequest(Request input, Context context)'
    );
  });
});
