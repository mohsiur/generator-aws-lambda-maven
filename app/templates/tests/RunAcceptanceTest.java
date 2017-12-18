package <%=packageName%>.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "pretty", "html:target/cucumber",
		"json:target/cucumber/cucumber.json" }, glue = "solutions.heavywater.services.acceptance.steps/", features = {
				"classpath:cucumber/TestCaseAcceptance.feature" }, monochrome = true)

public class RunAcceptanceTest {

}
