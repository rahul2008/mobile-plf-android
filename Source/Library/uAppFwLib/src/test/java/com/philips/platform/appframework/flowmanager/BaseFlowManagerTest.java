package com.philips.platform.appframework.flowmanager;

import android.content.Context;
import android.support.annotation.Nullable;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.condition.ConditionAppLaunch;
import com.philips.platform.appframework.flowmanager.condition.ConditionIsDonePressed;
import com.philips.platform.appframework.flowmanager.condition.ConditionIsLoggedIn;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appframework.flowmanager.states.AboutScreenState;
import com.philips.platform.appframework.flowmanager.states.DataServicesState;
import com.philips.platform.appframework.flowmanager.states.IAPRetailerFlowState;
import com.philips.platform.appframework.flowmanager.states.ProductRegistrationState;
import com.philips.platform.appframework.flowmanager.states.SettingsFragmentState;
import com.philips.platform.appframework.flowmanager.states.SupportFragmentState;
import com.philips.platform.appframework.flowmanager.states.UserRegistrationOnBoardingState;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@SuppressWarnings("deprecation")
public class BaseFlowManagerTest extends TestCase {

    private FlowManagerTest flowManagerTest;
    private Context context;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
//        FlowManagerListener flowManagerListenerMock = mock(FlowManagerListener.class);
//        context = mock(Context.class);
//        flowManagerTest = new FlowManagerTest(createFileFromInputStream(getClass().getClassLoader().getResourceAsStream("res/Appflow.json")).getPath(), flowManagerListenerMock);
    }

    @Test
    public void testGetFirstState() {
//        final BaseState firstState = flowManagerTest.getFirstState();
//        assertTrue(firstState != null);
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

    @Nullable
    private InputStream getInputStream(final int resId) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(context.getString(resId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private class FlowManagerTest extends BaseFlowManager {

        FlowManagerTest(final String jsonPath, FlowManagerListener flowManagerListener) {
            super(jsonPath, flowManagerListener);
        }

        @Override
        public void populateStateMap(final Map<String, BaseState> uiStateMap) {
            uiStateMap.put(AppStates.ON_BOARDING_REGISTRATION, new UserRegistrationOnBoardingState());
            uiStateMap.put(AppStates.ABOUT, new AboutScreenState());
            uiStateMap.put(AppStates.SETTINGS, new SettingsFragmentState());
            uiStateMap.put(AppStates.IAP, new IAPRetailerFlowState());
            uiStateMap.put(AppStates.PR, new ProductRegistrationState());
            uiStateMap.put(AppStates.SUPPORT, new SupportFragmentState());
            uiStateMap.put(AppStates.DATA_SYNC, new DataServicesState());
        }

        @Override
        public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
            baseConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
            baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
            baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
        }
    }
}