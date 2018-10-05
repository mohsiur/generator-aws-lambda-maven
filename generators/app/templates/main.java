package <%=packageName%>;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

<% if(s3) { %>import <%=packageName%>.utils.AmazonWebServices.S3;<% } %>
<% if(dynamodb) { %>import <%=packageName%>.utils.AmazonWebServices.DynamoDb;<% } %>
<% if(sqs) { %>import <%=packageName%>.utils.AmazonWebServices.SQS;<% } %>
<% if(cloudformation) { %>import <%=packageName%>.utils.AmazonWebServices.CloudFormation;<% } %>
<% if(glue) { %>import <%=packageName%>.utils.AmazonWebServices.Glue;<% } %>

<% if(invokerAPIGateway) { %>

import com.amazonaws.services.lambda.runtime.RequestHandler;
import <%=packageName%>.utils.LambdaUtils.Request;
import <%=packageName%>.utils.LambdaUtils.Response;

public class <%=className%> implements RequestHandler<Request, Response> 

<% } %>

<% if(invokerSNS) { %>
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
public class <%=className%> 
<% } %>

<% if(invokerDefault) { %>public class <%=className%><% } %>{

	private static LambdaLogger logger;
	<% if(s3) { %>// Initiate S3 Client
	private static S3 s3 = new S3();<% } %>
	<% if(dynamodb) { %>// Initiate DynamoDb Client
	private static DynamoDb dynamoDb = new DynamoDb();<% } %>
	<% if(sqs) { %>// Initiate S3 Client
	private static SQS sqs = new SQS();<% } %>
	<% if(cloudformation) { %>// Initiate Cloudformation Client
	private static CloudFormation cft = new CloudFormation();<% } %>
	<% if(glue) { %>// Initiate Glue Client
	private static Glue glue = new Glue();<% } %>
	
	<% if(invokerAPIGateway) { %>public Response handleRequest(Request input, Context context) <% } %>
	<% if(invokerSNS) { %>public String handleRequest(SNSEvent event, Context context) <% } %>
	<% if(invokerDefault) { %>public String handleRequest(Object input, Context context) <% } %>{
		
		<% if(invokerDefault) { %> String request = input.getRequest(); <% } %>
		<% if(invokerSNS) { %> String input = event.getRecords().get(0).getSNS().getMessage(); <% } %>

		// Initiate the Logger
		logger = context.getLogger();
		try 
		{
			String output = someFunction(request);
			logger.log(output);
			//	Send request for indexing and return response.
			<% if(invokerAPIGateway) { %>return new Response(output);<% } %>
			<% if(invokerDefault) { %>return output;<% } %>
			<% if(invokerSNS) { %>return output;<% } %>

		} 
		
		catch (Exception e) 
		{
			//	Returns the error message in case of failure.
			<% if(invokerAPIGateway) { %>return new Response(e.getMessage());<% } %>
			<% if(invokerDefault) { %>return e.getMessage();<% } %>
			<% if(invokerSNS) { %>return e.getMessage();<% } %>
		}
	}

	private String someFunction(String request){
		// TODO Auto-generated method stub
		return "Success";
	}

	
}