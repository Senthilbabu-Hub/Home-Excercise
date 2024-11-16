package CucumberOptions;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/Features", 
        glue = "stepDefinition", tags="@Incorrect or @Correct",
        plugin = {"pretty", "html:target/cucumber-reports.html"} 
)
public class TestNGRunner extends AbstractTestNGCucumberTests {
}