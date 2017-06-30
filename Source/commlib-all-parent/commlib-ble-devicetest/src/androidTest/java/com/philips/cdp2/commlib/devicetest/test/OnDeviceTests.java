/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@CucumberOptions(
        features = "features",
        glue = {"com.philips.cdp2.commlib.devicetest.test"}
)
@RunWith(Cucumber.class)
public class OnDeviceTests {

}
