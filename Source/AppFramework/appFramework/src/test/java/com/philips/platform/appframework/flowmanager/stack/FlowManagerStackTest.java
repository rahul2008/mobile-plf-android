package com.philips.platform.appframework.flowmanager.stack;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.baseapp.screens.aboutscreen.AboutScreenState;
import com.philips.platform.baseapp.screens.consumercare.SupportFragmentState;
import com.philips.platform.baseapp.screens.dataservices.DataSyncScreenState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.settingscreen.SettingsFragmentState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;

import junit.framework.TestCase;


public class FlowManagerStackTest extends TestCase {


    private FlowManagerStack flowManagerStack;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        flowManagerStack = new FlowManagerStack();
    }

    public void testPop() {
        BaseState aboutState = new AboutScreenState();
        BaseState iapRetailer = new IAPRetailerFlowState();
        flowManagerStack.push(aboutState);
        flowManagerStack.push(aboutState);
        try {
            assertEquals(aboutState, flowManagerStack.pop());
            flowManagerStack.push(iapRetailer);
            flowManagerStack.pop();
            assertEquals(flowManagerStack.size(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e instanceof NoStateException);
        }
    }

    public void testPopByState() {
        BaseState aboutState = new AboutScreenState();
        BaseState prState = new ProductRegistrationState();
        BaseState iapRetailer = new IAPRetailerFlowState();
        BaseState urState = new UserRegistrationOnBoardingState();
        BaseState supportState = new SupportFragmentState();
        BaseState settingsState = new SettingsFragmentState();
        BaseState dataSyncState = new DataSyncScreenState();
        flowManagerStack.push(aboutState);
        flowManagerStack.push(prState);
        flowManagerStack.push(iapRetailer);
        flowManagerStack.push(urState);
        flowManagerStack.push(supportState);
        flowManagerStack.push(settingsState);
        flowManagerStack.push(dataSyncState);
        try {
            assertTrue(flowManagerStack.pop(supportState) instanceof SupportFragmentState);
        } catch (NoStateException e) {
            e.printStackTrace();
        }
        assertEquals(flowManagerStack.size(), 5);
        try {
            assertTrue(flowManagerStack.pop(prState) instanceof ProductRegistrationState);
        } catch (NoStateException e) {
            e.printStackTrace();
        }
        assertEquals(flowManagerStack.size(), 2);
    }

    public void testPush() throws NoStateException {
        BaseState aboutState = new AboutScreenState();
        BaseState prState = new ProductRegistrationState();
        BaseState iapRetailer = new IAPRetailerFlowState();
        flowManagerStack.push(aboutState);
        flowManagerStack.push(aboutState);
        assertEquals(1, flowManagerStack.size());
        flowManagerStack.push(iapRetailer);
        flowManagerStack.push(prState);
        assertEquals(3, flowManagerStack.size());
        assertEquals(iapRetailer, flowManagerStack.pop());
    }

    public void testClearStack() {
        BaseState aboutState = new AboutScreenState();
        BaseState prState = new ProductRegistrationState();
        BaseState iapRetailer = new IAPRetailerFlowState();
        flowManagerStack.push(aboutState);
        flowManagerStack.push(iapRetailer);
        flowManagerStack.push(prState);
        assertEquals(3, flowManagerStack.size());
        flowManagerStack.clear();
        assertEquals(0, flowManagerStack.size());
    }
}