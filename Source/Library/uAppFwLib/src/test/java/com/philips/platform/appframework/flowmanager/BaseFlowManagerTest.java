package com.philips.platform.appframework.flowmanager;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.condition.ConditionAppLaunch;
import com.philips.platform.appframework.flowmanager.condition.ConditionIsDonePressed;
import com.philips.platform.appframework.flowmanager.condition.ConditionIsLoggedIn;
import com.philips.platform.appframework.flowmanager.condition.ConditionTest;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.JsonAlreadyParsedException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.NullEventException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appframework.flowmanager.states.AboutScreenState;
import com.philips.platform.appframework.flowmanager.states.DataServicesState;
import com.philips.platform.appframework.flowmanager.states.HomeState;
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
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("deprecation")
public class BaseFlowManagerTest extends TestCase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private FlowManagerTest flowManagerTest;
    private Context context;
    private String path;
    private FlowManagerListener flowManagerListenerMock;
    private Handler handlerMock;
    private Runnable runnableMock;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        flowManagerListenerMock = mock(FlowManagerListener.class);
        context = mock(Context.class);
        runnableMock = mock(Runnable.class);
        handlerMock = mock(Handler.class);
        when(handlerMock.post(runnableMock)).thenReturn(true);
        flowManagerTest = new FlowManagerTest()  {
            @NonNull
            @Override
            protected Handler getHandler(Context context) {
                return handlerMock;
            }

            @NonNull
            @Override
            protected Runnable getRunnable(FlowManagerListener flowManagerListener) {
                return runnableMock;
            }
        };
        File fileFromInputStream = createFileFromInputStream(getClass().getClassLoader().getResourceAsStream("res/Appflow.json"));
        if (fileFromInputStream != null)
            path = fileFromInputStream.getPath();

        flowManagerTest.initialize(context, path, flowManagerListenerMock);
         sleep(2);

        verify(handlerMock).post(runnableMock);


    }



    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testAlreadyInitialize() {
        try {
            flowManagerTest.initialize(context, path, flowManagerListenerMock);
        } catch (JsonAlreadyParsedException e) {
            assertEquals(e.getMessage(), "Json already parsed");
        }
    }

    public void testGetCondition() {
        final BaseCondition condition = flowManagerTest.getCondition(AppConditions.CONDITION_APP_LAUNCH);
        assertTrue(condition instanceof ConditionAppLaunch);
        assertFalse(condition.isSatisfied(context));
        try {
            flowManagerTest.getCondition("");
        } catch (ConditionIdNotSetException e) {
            assertEquals(e.getMessage(), "There is no Condition Id for the passed Condition");
        }
    }

    public void testGetFirstState() {
        final BaseState firstState = flowManagerTest.getFirstState();
        assertTrue(firstState != null);
    }

    public void testNoEventFoundException() {
        try {
            flowManagerTest.getNextState(flowManagerTest.getState(AppStates.SPLASH), "testing");
        } catch (NoEventFoundException e) {
            assertEquals(e.getMessage(), "No Event found with that Id");
        }
    }

    public void testConditionIdException() {
        exception.expect(ConditionIdNotSetException.class);
        try {
            flowManagerTest.getNextState(flowManagerTest.getState(AppStates.ON_BOARDING_REGISTRATION), "URComplete");
        } catch (ConditionIdNotSetException e) {
            assertTrue(e.getMessage().equals("There is no Condition Id for the passed Condition"));
        }
    }

    public void testGetNextState() {
        try {
            flowManagerTest.getNextState(flowManagerTest.getCurrentState(), null);
        } catch (NullEventException e) {
            assertTrue(e.getMessage().equals("Passed Event is not valid"));
        }
        try {
            flowManagerTest.getNextState(flowManagerTest.getState(AppStates.TEST), "test");
        } catch (StateIdNotSetException e) {
            assertTrue(e.getMessage().equals("There is no State Id for the passed State"));
        }
        try {
            flowManagerTest.getNextState(flowManagerTest.getState("unknown"), "test");
        } catch (NoStateException e) {
            assertTrue(e.getMessage().equals("No State found with that Id"));
        }
        assertEquals(flowManagerTest.getNextState(flowManagerTest.getState(AppStates.SPLASH), "onSplashTimeOut"), flowManagerTest.getState(AppStates.WELCOME));
    }

    public void testBackState() {
        flowManagerTest = new FlowManagerTest() {
            @NonNull
            @Override
            protected Handler getHandler(Context context) {
                return handlerMock;
            }

            @NonNull
            @Override
            protected Runnable getRunnable(FlowManagerListener flowManagerListener) {
                return runnableMock;
            }

        };
        flowManagerTest.initialize(context, path, flowManagerListenerMock);
       sleep(2);
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.SPLASH), "onSplashTimeOut");
        assertNull(flowManagerTest.getBackState());
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.SPLASH), "onSplashTimeOut");
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.WELCOME), "welcome_done");
        flowManagerTest.getBackState();
        assertEquals(flowManagerTest.getState(AppStates.HOME), flowManagerTest.getCurrentState());

        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.SPLASH), "onSplashTimeOut");
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.WELCOME), "welcome_done");
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.HOME), "settings");
        flowManagerTest.getBackState(flowManagerTest.getCurrentState());
        assertEquals(flowManagerTest.getState(AppStates.WELCOME), flowManagerTest.getCurrentState());

        flowManagerTest = new FlowManagerTest(context, path) {
            @NonNull
            @Override
            protected Handler getHandler(Context context) {
                return handlerMock;
            }

            @NonNull
            @Override
            protected Runnable getRunnable(FlowManagerListener flowManagerListener) {
                return runnableMock;
            }

        };
        sleep(2);
        try {
            flowManagerTest.getBackState();
        } catch (NoStateException e) {
            assertEquals(e.getMessage(), "No State found with that Id");
        }
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.SPLASH), "onSplashTimeOut");
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.HOME), "support");
        flowManagerTest.getNextState(flowManagerTest.getState(AppStates.HOME), "settings");
        flowManagerTest.getBackState();
        assertEquals(flowManagerTest.getState(AppStates.WELCOME), flowManagerTest.getCurrentState());
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

        public FlowManagerTest() {
        }

        public FlowManagerTest(Context context, String path) {
            super(context, path);
        }

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
            uiStateMap.put(AppStates.HOME, new HomeState());
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