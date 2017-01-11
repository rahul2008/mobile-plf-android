package philips.appframeworklibrary.flowmanager.stack;

import junit.framework.TestCase;

import philips.appframeworklibrary.flowmanager.base.BaseState;
import philips.appframeworklibrary.flowmanager.exceptions.NoStateException;
import philips.appframeworklibrary.flowmanager.states.AboutScreenState;
import philips.appframeworklibrary.flowmanager.states.DataServicesState;
import philips.appframeworklibrary.flowmanager.states.IAPRetailerFlowState;
import philips.appframeworklibrary.flowmanager.states.ProductRegistrationState;
import philips.appframeworklibrary.flowmanager.states.SettingsFragmentState;
import philips.appframeworklibrary.flowmanager.states.SupportFragmentState;
import philips.appframeworklibrary.flowmanager.states.UserRegistrationOnBoardingState;

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
        assertEquals(null, flowManagerStack.pop());
        flowManagerStack.push(iapRetailer);
        assertEquals(flowManagerStack.size(), 1);
        flowManagerStack.pop();
        assertEquals(flowManagerStack.size(), 0);
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
        flowManagerStack.push(dataSyncState);
        assertTrue(flowManagerStack.pop(supportState) instanceof SupportFragmentState);
        assertEquals(flowManagerStack.size(), 5);
        assertTrue(flowManagerStack.pop(prState) instanceof ProductRegistrationState);
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