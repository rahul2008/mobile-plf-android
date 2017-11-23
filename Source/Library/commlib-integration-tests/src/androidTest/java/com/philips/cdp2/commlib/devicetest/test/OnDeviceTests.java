/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import static com.philips.cdp2.commlib.devicetest.runner.CucumberRunner.JSON_PATH;
import static com.philips.cdp2.commlib.devicetest.runner.CucumberRunner.HTML_PATH;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        glue = {"com.philips.cdp2.commlib.devicetest.test"},
        format = { "json:" + JSON_PATH, "html:" + HTML_PATH },
        features = "features",
        tags = {"@automated", "@android", "~@not_android", "@target", "~@broken", "~@disabled"}
)
public class OnDeviceTests {

}
