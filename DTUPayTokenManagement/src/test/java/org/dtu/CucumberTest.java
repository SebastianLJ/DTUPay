package org.dtu;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json:target/cucumber.json"},
        features = "features",// directory of the feature files
        glue = "org.dtu",
        monochrome = true
)
public class CucumberTest {
}