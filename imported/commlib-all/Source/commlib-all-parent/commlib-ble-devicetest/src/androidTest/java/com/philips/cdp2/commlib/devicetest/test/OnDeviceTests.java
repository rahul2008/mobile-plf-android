/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@CucumberOptions(
        features = "commlib-bdd",
        glue = {"com.philips.cdp2.commlib.devicetest.test"},
        format = {
                "json:/data/data/com.philips.cdp2.commlib.devicetest/report.json",
                "html:/data/data/com.philips.cdp2.commlib.devicetest/cucumber-reports/html"
        },
        tags = {"@automated", "@android", "~@not_android", "@target"}
)
@RunWith(Cucumber.class)
public class OnDeviceTests {

}
