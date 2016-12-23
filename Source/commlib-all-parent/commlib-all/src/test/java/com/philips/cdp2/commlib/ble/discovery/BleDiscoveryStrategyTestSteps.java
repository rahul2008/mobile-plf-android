/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;

import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.appliance.MixerFactory;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;

import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTestSteps {

    private CommCentral commCentral;
    private MixerFactory applianceFactory;

    public void notsetup() throws SHNBluetoothHardwareUnavailableException {
        initMocks(this);

        final Context mockContext = Mockito.mock(Context.class);
        final SHNCentral mockShnCentral = Mockito.mock(SHNCentral.class);

        final BleDeviceCache bleDeviceCache = new BleDeviceCache();
        final SHNDeviceScanner deviceScanner = Mockito.mock(SHNDeviceScanner.class);

        BleTransportContext bleTransportContext = new BleTransportContext(mockContext, false);
        when(bleTransportContext.createBlueLib(any(Context.class), anyBoolean())).thenReturn(mockShnCentral);

        this.applianceFactory = new MixerFactory(bleTransportContext);

        Set<DiscoveryStrategy> discoveryStrategies = new HashSet<DiscoveryStrategy>() {{
            add(new BleDiscoveryStrategy(mockContext, bleDeviceCache, deviceScanner, 30000L));
        }};

        this.commCentral = new CommCentral(discoveryStrategies, applianceFactory);
    }

    @Given("^a BlueLib mock$")
    public void aBlueLibMock() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^application has support for:$")
    public void applicationHasSupportFor(final List<String> applianceTypes) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^starting discovery for BLE appliances$")
    public void startingDiscoveryForBLEAppliances() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^startScanning is called (\\d+) time on BlueLib$")
    public void startscanningIsCalledTimeOnBlueLib(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^starting discovery for BLE appliances (\\d+) times$")
    public void startingDiscoveryForBLEAppliancesTimes(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^stopping discovery for BLE appliances$")
    public void stoppingDiscoveryForBLEAppliances() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^stopScanning is called on BlueLib$")
    public void stopscanningIsCalledOnBlueLib() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^stopScanning is not called on BlueLib$")
    public void stopscanningIsNotCalledOnBlueLib() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the following appliances are created:$")
    public void theFollowingAppliancesAreCreated(final List<String> appliances) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^no appliances are created$")
    public void noAppliancesAreCreated() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^toothbrush(\\d+) is discovered by BlueLib$")
    public void toothbrushIsDiscoveredByBlueLib(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^mixer(\\d+) is discovered by BlueLib$")
    public void mixerIsDiscoveredByBlueLib(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^shaver(\\d+) is discovered by BlueLib$")
    public void shaverIsDiscoveredByBlueLib(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();

    }
}