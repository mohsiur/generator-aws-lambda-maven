'use strict';
const path = require('path');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');

describe('generator-aws-lambda-maven:app', () => {
  beforeAll(() => {
    return helpers.run(path.join(__dirname, '../generators/app')).withPrompts({
      awsVersion: `1.11.257`,
      packageName: 'testApp',
      lambdaInvoker: 'apigateway'
    });
  });

  it('creates files', () => {
    assert.file(['dummyfile.txt', 'src/main/dummfile2.txt', 'src/main/dummyfile3.txt']);
  });

  it('file info', () => {
    assert.fileContent('dummyfile.txt', '1.11.257');
    assert.fileContent('src/main/dummfile2.txt', 'testApp');
    assert.fileContent('src/main/dummyfile3.txt', 'apigateway');
  });
});
