package org.dtu.steps;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        //path of feature file
        features = "src/test/resources/features",
        //path of step definition file
        glue = "org.dtu.steps"
)
public class TestRunner {
}