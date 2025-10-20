package com.saucedemo.runners;

import static io.cucumber.junit.platform.engine.Constants.*;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = 
    "com.saucedemo.stepdefinitions, com.saucedemo.context, com.saucedemo.hooks")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value =
    "pretty, html:target/cucumber-reports/report.html, json:target/cucumber-reports/cucumber.json," +
    "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@test")
public class TestRunner {
    // plus besoin de @AfterAll ici, les Hooks gèrent tout.
}