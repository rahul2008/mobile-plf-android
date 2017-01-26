package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.condition.ConditionAppLaunch;
import com.philips.platform.appframework.flowmanager.condition.ConditionIsDonePressed;
import com.philips.platform.appframework.flowmanager.condition.ConditionIsLoggedIn;
import com.philips.platform.appframework.flowmanager.condition.ConditionTest;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.JsonAlreadyParsedException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.NullEventException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appframework.flowmanager.states.AboutScreenState;
import com.philips.platform.appframework.flowmanager.states.DataServicesState;
import com.philips.platform.appframework.flowmanager.states.IAPRetailerFlowState;
import com.philips.platform.appframework.flowmanager.states.ProductRegistrationState;
import com.philips.platform.appframework.flowmanager.states.SettingsFragmentState;
import com.philips.platform.appframework.flowmanager.states.SplashState;
import com.philips.platform.appframework.flowmanager.states.SupportFragmentState;
import com.philips.platform.appframework.flowmanager.states.TestState;
import com.philips.platform.appframework.flowmanager.states.UserRegistrationOnBoardingState;
import com.philips.platform.appframework.flowmanager.states.WelcomeScreenState;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("deprecation")
public class BaseFlowManagerTest extends TestCase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private FlowManagerTest flowManagerTest;
    private Context context;
    private String path;
    private FlowManagerListener flowManagerListenerMock;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        flowManagerListenerMock = mock(FlowManagerListener.class);
        context = mock(Context.class);
        flowManagerTest = new FlowManagerTest();
        path = createFileFromInputStream(getClass().getClassLoader().getResourceAsStream("res/Appflow.json")).getPath();
        flowManagerTest.initialize(context, path, flowManagerListenerMock);
        verify(flowManagerListenerMock).onParseSuccess();
    }

    public void testGetFirstState() {
        final BaseState firstState = flowManagerTest.getFirstState();
        assertTrue(firstState != null);
    }

    public void testConditionIdException() {
        exception.expect(ConditionIdNotSetException.class);
        try {
            flowManagerTest.getNextState(flowManagerTest.getState(AppStates.ON_BOARDING_REGISTRATION), "URComplete");
        } catch (ConditionIdNotSetException e) {
            assertTrue(e.getMessage().equals("No condition id set on constructor"));
        }
    }

    public void testGetNextState() {
        try {
            flowManagerTest.getNextState(flowManagerTest.getCurrentState(), null);
        } catch (NullEventException e) {
            assertTrue(e.getMessage().equals("Null Event Found"));
        }
        try {
            flowManagerTest.getNextState(flowManagerTest.getState(AppStates.TEST), "test");
        } catch (StateIdNotSetException e) {
            assertTrue(e.getMessage().equals("No State id set on constructor"));
        }
        try {
            flowManagerTest.getNextState(flowManagerTest.getState("unknown"), "test");
        } catch (NoStateException e) {
            assertTrue(e.getMessage().equals("No State Found"));
        }
        assertEquals(flowManagerTest.getNextState(flowManagerTest.getState(AppStates.SPLASH), "onSplashTimeOut"), flowManagerTest.getState(AppStates.WELCOME));

    }

    public void testErrorCases() {
        exception.expect(JsonAlreadyParsedException.class);
        try {
            flowManagerTest.initialize(context, path, flowManagerListenerMock);
        } catch (JsonAlreadyParsedException e) {
            assertTrue(e.getMessage().equals("Json already parsed"));
        }
    }

    private File createFileFromInputStream(final InputStream inputStream) {
        try {
            String filename = "tempFile";
            FileOutputStream outputStream;
            final File file = File.createTempFile(filename, null, context.getCacheDir());
            outputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class FlowManagerTest extends BaseFlowManager {

        @Override
        public void populateStateMap(final Map<String, BaseState> uiStateMap) {
            uiStateMap.put(AppStates.ON_BOARDING_REGISTRATION, new UserRegistrationOnBoardingState());
            uiStateMap.put(AppStates.ABOUT, new AboutScreenState());
            uiStateMap.put(AppStates.WELCOME, new WelcomeScreenState());
            uiStateMap.put(AppStates.SETTINGS, new SettingsFragmentState());
            uiStateMap.put(AppStates.IAP, new IAPRetailerFlowState());
            uiStateMap.put(AppStates.PR, new ProductRegistrationState());
            uiStateMap.put(AppStates.SUPPORT, new SupportFragmentState());
            uiStateMap.put(AppStates.DATA_SYNC, new DataServicesState());
            uiStateMap.put(AppStates.SPLASH, new SplashState());
            uiStateMap.put(AppStates.TEST, new TestState());
        }

        @Override
        public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
            baseConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
            baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
            baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
            baseConditionMap.put(AppConditions.TEST, new ConditionTest());
        }
    }
}