package <%=packageName%>.utils.LambdaUtils;

/**
 * 	Simple POJO to handle Lambda response.
 * 		
 * 	@author Kartik
 */


public class Response 
{
	String response;

	//	Constructor.
	public Response() 
	{
		this.response = "";
	}

	//	Parameterized constructor.
	public Response(String response) 
	{
		this.response = response;
	}

	//	Returns a String.	
	public String getResponse() 
	{
		return response;
	}

	//	Setting the value of response.
	public void setResponse(String response) 
	{
		this.response = response;
	}
}