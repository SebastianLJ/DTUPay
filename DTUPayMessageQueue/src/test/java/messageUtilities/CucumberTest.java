package messageUtilities;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json:target/cucumber.json"},
        features = "features",
        glue = "messageUtilities"
)
public class CucumberTest {
}