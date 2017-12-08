package com.philips.platform.appframework.flowmanager.stack;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.states.AboutScreenState;
import com.philips.platform.appframework.flowmanager.states.DataServicesState;
import com.philips.platform.appframework.flowmanager.states.IAPRetailerFlowState;
import com.philips.platform.appframework.flowmanager.states.ProductRegistrationState;
import com.philips.platform.appframework.flowmanager.states.SettingsFragmentState;
import com.philips.platform.appframework.flowmanager.states.SupportFragmentState;
import com.philips.platform.appframework.flowmanager.states.UserRegistrationOnBoardingState;

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
        assertEquals(flowManagerStack.pop(), null);
        flowManagerStack.push(aboutState);
        flowManagerStack.pop();
        assertEquals(flowManagerStack.size(), 0);
        flowManagerStack.push(aboutState);
        flowManagerStack.push(aboutState);
        flowManagerStack.push(iapRetailer);
        assertEquals(flowManagerStack.size(), 3);
        flowManagerStack.pop();
        assertEquals(flowManagerStack.size(), 2);
    }

    public void testPopByState() {
        BaseState aboutState = new AboutScreenState();
        BaseState prState = new ProductRegistrationState();
        BaseState iapRetailer = new IAPRetailerFlowState();
        BaseState urState = new UserRegistrationOnBoardingState();
        BaseState supportState = new SupportFragmentState();
        BaseState settingsState = new SettingsFragmentState();
        BaseState dataSyncState = new DataServicesState();
        flowManagerStack.push(aboutState);
        flowManagerStack.push(prState);
        flowManagerStack.push(iapRetailer);
        flowManagerStack.push(urState);
        flowManagerStack.push(supportState);
        flowManagerStack.push(settingsState);
        flowManagerStack.push(prState);
        flowManagerStack.push(dataSyncState);
        assertTrue(flowManagerStack.pop(supportState) instanceof SupportFragmentState);
        assertEquals(flowManagerStack.size(), 5);
        flowManagerStack.push(prState);
        flowManagerStack.push(dataSyncState);
        assertTrue(flowManagerStack.pop(prState) instanceof ProductRegistrationState);
        assertEquals(flowManagerStack.size(), 6);
    }

    public void testPush() throws NoStateException {
        BaseState aboutState = new AboutScreenState();
        BaseState prState = new ProductRegistrationState();
        BaseState iapRetailer = new IAPRetailerFlowState();
        flowManagerStack.push(aboutState);
        flowManagerStack.push(aboutState);
        assertEquals(2, flowManagerStack.size());
        flowManagerStack.push(iapRetailer);
        flowManagerStack.push(prState);
        assertEquals(4, flowManagerStack.size());
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