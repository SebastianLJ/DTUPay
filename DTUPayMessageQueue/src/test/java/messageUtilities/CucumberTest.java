package messageUtilities;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json:target/cucumber.json"},
        features = "features",// directory of the feature files
        glue = "messageUtilities"
)
public class CucumberTest {
}