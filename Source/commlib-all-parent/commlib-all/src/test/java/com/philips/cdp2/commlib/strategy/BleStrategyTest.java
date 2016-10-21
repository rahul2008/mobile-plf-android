/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.strategy;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        format = {
                "json:" + "../build/cucumber-reports/libsoftwaregateway-app-results.json",
                "html:" + "build/reports/java" + "/html"
        },
        features = "src/test/features",
        strict = true,
        tags = {"@automated","@android","~@not_android"}
)
public class BleStrategyTest {

}
