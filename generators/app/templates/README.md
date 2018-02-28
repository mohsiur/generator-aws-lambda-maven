# <%=projectName%>

The following is a lambda for <%=applicationName%> and is invoked by <% if(invokerAPIGateway) { %> API Gateway <% } %> <% if(invokerDefault) { %> any given Object <% } %>

## Pre-requisites

To deploy into AWS a few pre-requisites need to be met

* [AWS cli](https://aws.amazon.com/cli/) installed and configured with your credentials

## Deployment

To deploy your lambda into AWS you will need to run the following CLI commands in your terminal which will copy your Lambda artifacts and Cloudformation Templates into S3 as well as deploy the Cloudformation Template.

You may have to append environment variables as required if needed.

`aws s3 cp <%=applicationName%>.template s3://<%=bucketName%>/<%=applicationName%>/CFT/`

`aws s3 cp target/<%=projectName%>.jar s3://<%=bucketName%>/<%=applicationName%>/`

`aws cloudformation create-stack --stack-name <%=stackName%> --template-url https://s3.amazonaws.com/<%=bucketName%>/<%=applicationName%>/CFT/<%=applicationName%>.template --capabilities CAPABILITY_IAM --parameters ParameterKey=BucketName,ParameterValue=<%=bucketName%> ParameterKey=Stage,ParameterValue=<%=stage%>`
