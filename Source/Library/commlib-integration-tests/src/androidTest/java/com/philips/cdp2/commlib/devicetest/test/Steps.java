/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.test;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp2.commlib.BuildConfig;
import com.philips.cdp2.commlib.cloud.communication.CloudCommunicationStrategy;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.devicetest.TestApplication;
import com.philips.cdp2.commlib.devicetest.appliance.ReferenceAppliance;
import com.philips.cdp2.commlib.devicetest.port.time.TimePort;
import com.philips.cdp2.commlib.devicetest.util.ApplianceWaiter;
import com.philips.cdp2.commlib.devicetest.util.CloudSignOnWaiter;
import com.philips.cdp2.commlib.devicetest.util.PairingWaiter;
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

import static android.app.Instrumentation.newApplication;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MINUTES;
import static junit.framework.Assert.assertNotNull;
import static org.assertj.core.api.Java6Assertions.assertThat;
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
    public void before(Scenario scenario) throws Throwable {
        this.scenario = scenario;
        this.app = (TestApplication) newApplication(TestApplication.class, getTargetContext());
        this.commCentral = app.getCommCentral();

        Log.i(LOGTAG, app.toString());
    }

    @After
        public void cleanup() throws InterruptedException {
        Log.i(LOGTAG, "Start cleanup");

        if (current != null) {
            current.getTimePort().unsubscribe();

            PortListener listener = portListeners.get(TimePort.class);
            listener.waitForPortUpdates(1, 1, MINUTES);

            commCentral.getApplianceManager().forgetStoredAppliance(current);

            current.disableCommunication();
            current.getTimePort().removePortListener(listener);
            current.getCommunicationStrategy().forceStrategyType(null);

            current = null;
        }

        commCentral.stopDiscovery();
        commCentral = null;

        portListeners.clear();

        Log.i(LOGTAG, "End cleanup");

        this.app = null;
    }

    @Given("^The environment is logged$")
    public void environmentIsLogged() {
        scenario.write("---- Logging environment ----");
        scenario.write("CommLib version: " + BuildConfig.LIBRARY_VERSION);
        scenario.write("BlueLib version: " + com.philips.pins.shinelib.BuildConfig.LIBRARY_VERSION);
        scenario.write("Phone mfg: " + Build.MANUFACTURER);
        scenario.write("Phone brand: " + Build.BRAND);
        scenario.write("Phone model: " + Build.MODEL);
        scenario.write("Phone codename: " + Build.DEVICE);
        scenario.write("Android version: " + Build.VERSION.RELEASE + " " + Build.VERSION.SECURITY_PATCH);
        scenario.write("---- Logging environment ----");
    }

    @And("^stay connected is disabled$")
    public void stayConnectedIsDisabled() throws Throwable {
        current.disableCommunication();
    }

    @Given("^distance between device and appliance is (\\d+) cm$")
    public void distanceBetweenDeviceAndApplianceIsCm(int arg0) throws Throwable {
        // Cannot guarantee this in code in any useful way.
    }

    @Given("^an appliance with cppId \"([^\"]*)\" is discovered and selected$")
    public void anApplianceWithCppIdIsDiscoveredAndSelected(final String cppId) throws Throwable {
        current = (ReferenceAppliance) commCentral.getApplianceManager().findApplianceByCppId(cppId);
        if (current == null) {
            ApplianceWaiter.Waiter<Appliance> waiter = ApplianceWaiter.forCppId(cppId);

            commCentral.getApplianceManager().addApplianceListener(waiter);
            commCentral.startDiscovery();

            current = (ReferenceAppliance) waiter.waitForAppliance(1, MINUTES).getAppliance();
            commCentral.stopDiscovery();
        }

        assertNotNull("Appliance not found!", current);

        Log.i(LOGTAG, "Found our referenceAppliance!");

        PortListener listener = new PortListener();
        portListeners.put(TimePort.class, listener);
        current.getTimePort().addPortListener(listener);
    }

    @When("^device requests time value from time port$")
    public void deviceRequestsTimeValueFromTimePort() throws Throwable {
        Log.d(LOGTAG, "Reloading timeport");
        current.getTimePort().reloadProperties();
    }

    @When("^device subscribes on time port$")
    public void deviceSubscribesOnTimePort() throws Throwable {
        current.getTimePort().subscribe();
    }

    @Then("^time value is received without errors$")
    public void timeValueIsReceivedWithoutErrors() throws Throwable {
        timeValueIsReceivedTimesWithoutErrors(1);
    }

    @Then("^time value is received (\\d+) times without errors$")
    public void timeValueIsReceivedTimesWithoutErrors(int count) throws Throwable {
        Log.d(LOGTAG, String.format("Waiting for %d timeport updates", count));

        PortListener listener = portListeners.get(TimePort.class);
        listener.waitForPortUpdates(count, 1, MINUTES);

        scenario.write("Errors:" + listener.errors.toString());

        assertEquals("Errors received from time port", emptyList(), listener.errors);
        assertThat(listener.receivedCount).as("Time values received from timeport").isGreaterThanOrEqualTo(count);

        final String datetime = current.getTimePort().getPortProperties().datetime;
        scenario.write("Got time: " + datetime);
        Log.d(LOGTAG, datetime);
    }

    @Given("^device is connected to SSID \"(.*?)\"$")
    public void deviceIsConnectedToSSID(String ssid) throws Throwable {
        WifiManager wifiManager = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        assertEquals("Wifi turned off", SupplicantState.COMPLETED, wifiInfo.getSupplicantState());

        ssid = String.format("\"%s\"", ssid); // According to spec the getSSID is returned with double quotes
        assertEquals("Connected to wrong network", ssid, wifiInfo.getSSID());
    }

    @Given("^bluetooth is turned on$")
    public void bluetoothIsTurnedOn() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    @Given("^appliance is paired to cloud$")
    public void applianceIsPairedToCloud() throws Throwable {
        app.getCommCentral().getApplianceManager().storeAppliance(current);

        PairingWaiter pairingWaiter = new PairingWaiter();
        PairingHandler pairingHandler = new PairingHandler<>(pairingWaiter, current, app.getCloudController());
        pairingHandler.startPairing();

        pairingWaiter.waitForPairingCompleted(1, MINUTES);

        assertTrue("Pairing failed", pairingWaiter.pairingSucceeded);
    }

    @Given("^appliance is stored on device$")
    public void applianceIsStoredOnDevice() throws Throwable {
        app.getCommCentral().getApplianceManager().storeAppliance(current);
    }

    @Given("^cloudCommunication is used$")
    public void cloudCommunicationIsUsed() throws Throwable {
        current.getCommunicationStrategy().forceStrategyType(CloudCommunicationStrategy.class);
    }

    @Given("^wifi is turned off$")
    public void wifiIsTurnedOff() throws Throwable {
        WifiManager wifiManager = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    @Given("^is signed on to cloud$")
    public void isSignedOnToCloud() throws Throwable {
        CloudController cloudController = app.getCloudController();

        CloudSignOnWaiter cloudSignOnWaiter = new CloudSignOnWaiter();
        cloudController.addSignOnListener(cloudSignOnWaiter);

        if (!cloudController.isSignOn()) {
            cloudSignOnWaiter.waitForSignOn(1, MINUTES);
        }

        cloudController.removeSignOnListener(cloudSignOnWaiter);

        assertTrue("Sign on failed", cloudController.isSignOn());
    }
}
