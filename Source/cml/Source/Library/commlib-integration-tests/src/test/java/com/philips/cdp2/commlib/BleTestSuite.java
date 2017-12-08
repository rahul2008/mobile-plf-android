/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        format = {
                "json:../build/cucumber-reports/report.json",
                "html:../build/cucumber-reports/html"
        },
        features = "src/androidTest/assets/features",
        strict = true,
        tags = {"@automated", "@android", "~@not_android", "~@target"}
)
public class BleTestSuite {

}
