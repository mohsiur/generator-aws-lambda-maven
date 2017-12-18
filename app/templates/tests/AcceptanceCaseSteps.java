package <%=packageName%>.integration.steps;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


public class CaseSteps{


	@Before
	public void setUp(){


	}

	@Given("^All dependencies are launched and working properly$")
	public void test_case_dependencies(){

	}

	@Then("^I test case number 1$")
	public void test_case_1(){

	}

	@After
	public void saveTestResults() throws IOException
	{	
		System.out.println("I save results After.");
		//System.out.println(testresults.toString());
		//FileUtils.writeStringToFile(new File("src/test/resources/" + listViewName + "/"+ time + "/testResults.json"), testresults.toJSONString());
	}

}