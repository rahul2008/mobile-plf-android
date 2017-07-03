/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.test;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.devicetest.TestApplication;

import java.util.concurrent.CountDownLatch;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.app.Instrumentation.newApplication;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static java.util.concurrent.TimeUnit.MINUTES;

public class MyStepdefs {

    private static final String LOGTAG = "COMMLIB_TESTAPP";

    private CommCentral commCentral;
    private TestApplication app;

    @Before
    public void permissions() {
        // In M+, trying to call a number will trigger a runtime dialog. Make sure
        // the permission is granted before running this test.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(LOGTAG, "Permissionsfix");
            InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.ACCESS_COARSE_LOCATION");
        }
    }


    @And("^\"([^\"]*)\" is disabled$")
    public void isDisabled(String arg0) throws Throwable {
        // NOOP
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

        final CountDownLatch applianceFoundLatch = new CountDownLatch(1);

        commCentral.getApplianceManager().addApplianceListener(new ApplianceManager.ApplianceListener<Appliance>() {
            @Override
            public void onApplianceFound(@NonNull Appliance foundAppliance) {
                Log.i(LOGTAG, foundAppliance.toString());
                applianceFoundLatch.countDown();
            }

            @Override
            public void onApplianceUpdated(@NonNull Appliance updatedAppliance) {

            }

            @Override
            public void onApplianceLost(@NonNull Appliance lostAppliance) {

            }
        });

        commCentral.startDiscovery();
        applianceFoundLatch.await(3, MINUTES);
        Log.i(LOGTAG, "Found a device!");
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
