/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.test;

import android.util.Log;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.devicetest.TestApplication;
import com.philips.cdp2.commlib.devicetest.appliance.ReferenceAppliance;
import com.philips.cdp2.commlib.devicetest.time.TimePort;
import com.philips.cdp2.commlib.devicetest.util.Android;
import com.philips.cdp2.commlib.devicetest.util.ApplianceWaiter;
import com.philips.cdp2.commlib.devicetest.util.PortListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.app.Instrumentation.newApplication;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Steps {

    private static final String LOGTAG = "COMMLIB_TESTAPP";

    private CommCentral commCentral;
    private TestApplication app;
    private ReferenceAppliance current;
    private final Map<Class<? extends DICommPort<?>>, PortListener> portListeners = new ConcurrentHashMap<>();
    private Scenario scenario;

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;

        // In M+, trying to access bluetooth will trigger a runtime dialog. Make sure
        // the permission is granted before running tests.
        Log.d(LOGTAG, "Grant permissions");
        Android.grantPermission(ACCESS_COARSE_LOCATION);
    }

    @After
    public void cleanup() {
        if (commCentral != null) {
            commCentral.stopDiscovery();
        }
    }

    @And("^\"Subscribe to time port\" is disabled$")
    public void subscribeToTimeportIsDisabled() throws Throwable {
        current.getTimePort().unsubscribe();
    }

    @And("^\"Stay connected\" is disabled$")
    public void stayConnectedIsDisabled() throws Throwable {
        current.disableCommunication();
    }

    @Given("^distance between phone and BLE Reference Node is (\\d+) cm$")
    public void distanceBetweenPhoneAndBLEReferenceNodeIsCm(int arg0) throws Throwable {
        // Cannot guarantee this in code in any useful way.
    }

    @Given("^a BLE Reference Node is discovered and selected$")
    public void aBLEReferenceNodeIsDiscoveredAndSelected() throws Throwable {
        app = (TestApplication) newApplication(TestApplication.class, getTargetContext());
        Log.i(LOGTAG, app.toString());
        app.onCreate();
        commCentral = app.getCommCentral();

        ApplianceWaiter.Waiter<Appliance> waiter = ApplianceWaiter.forCppId("22:22:22:CC:6C:57");

        commCentral.getApplianceManager().addApplianceListener(waiter);
        commCentral.startDiscovery();

        current = (ReferenceAppliance) waiter.waitForAppliance(2, MINUTES).getAppliance();
        Log.i(LOGTAG, "Found our referenceAppliance!");
    }

    @When("^application requests time value from time port$")
    public void applicationRequestsTimeValueFromTimePort() throws Throwable {
        PortListener listener = new PortListener();

        portListeners.put(TimePort.class, listener);
        current.getTimePort().addPortListener(listener);
        Log.d(LOGTAG, "Reloading timeport");
        current.getTimePort().reloadProperties();
    }

    @Then("^time value is received without errors$")
    public void timeValueIsReceivedWithoutErrors() throws Throwable {
        Log.d(LOGTAG, "Waiting for timeport refresh");

        PortListener listener = portListeners.get(TimePort.class);
        listener.waitForPortUpdate(3, MINUTES);

        scenario.write("Errors:" + listener.errors.toString());

        assertEquals(emptyList(), listener.errors);
        assertTrue(listener.valueWasReceived);

        final String datetime = current.getTimePort().getPortProperties().datetime;
        scenario.write("Got time: " + datetime);
        Log.d(LOGTAG, datetime);
    }
}
