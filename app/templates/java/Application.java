package <%=packageName%>.<%=applicationName%>;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import solutions.heavywater.utils.LambdaUtils.Request;
import solutions.heavywater.utils.LambdaUtils.Response;

public class <%=applicationName%> implements RequestHandler<Request, Response>
{

	static LambdaLogger logger;
	
	public Response handleRequest(Request input, Context context) 
	{
		
		String request = input.getRequest();
			
		try 
		{
			String output = someFunction(request);
			logger.log(output);
			//	Send request for indexing and return response.
			return new Response(output);
		} 
		
		catch (Exception e) 
		{
			//	Returns the error message in case of failure.
			return new Response(e.getMessage());
		}
	}

	private String someFunction(String request){
		// TODO Auto-generated method stub
	}

	
}