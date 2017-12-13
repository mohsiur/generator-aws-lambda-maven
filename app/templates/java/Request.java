package <%=packageName%>.utils.LambdaUtils;

/**
 * 	Simple POJO to handle Lambda requests.		
 * 
 * 	@author Kartik
 */

public class Request 
{
	String request;

	//	Constructor.
	public Request() 
	{
		this.request = "";
	}

	//	Parameterized constructor.
	public Request(String request) 
	{
		this.request = request;
	}

	//	Returns a String.
	public String getRequest() 
	{
		return request;
	}

	//	Setting the value of request.
	public void setRequest(String request) 
	{
		this.request = request;
	}
}
