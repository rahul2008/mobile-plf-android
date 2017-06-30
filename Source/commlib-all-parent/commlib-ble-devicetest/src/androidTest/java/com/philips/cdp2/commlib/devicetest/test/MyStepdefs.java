/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.test;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MyStepdefs {


    @And("^\"([^\"]*)\" is disabled$")
    public void isDisabled(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("^distance between phone and BLE Reference Node is (\\d+) cm$")
    public void distanceBetweenPhoneAndBLEReferenceNodeIsCm(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("^a BLE Reference Node is discovered and selected$")
    public void aBLEReferenceNodeIsDiscoveredAndSelected() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^application requests time value from time port$")
    public void applicationRequestsTimeValueFromTimePort() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^time value is received without errors$")
    public void timeValueIsReceivedWithoutErrors() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }
}
