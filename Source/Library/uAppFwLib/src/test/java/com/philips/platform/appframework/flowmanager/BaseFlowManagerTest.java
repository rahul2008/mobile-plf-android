package com.philips.platform.appframework.flowmanager;

import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class BaseFlowManagerTest extends TestCase {

    /*private FlowManagerTest flowManagerTest;
    private Context context;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        AppFlowJsonListener appFlowJsonListenerMock = mock(AppFlowJsonListener.class);
        context = mock(Context.class);
        flowManagerTest = new FlowManagerTest(createFileFromInputStream(getClass().getClassLoader().getResourceAsStream("assets/Appflow.json")).getPath(), appFlowJsonListenerMock);
    }

    @Test
    public void testGetFirstState() {
        final BaseState firstState = flowManagerTest.getFirstState();
        assertTrue(firstState != null);
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

        FlowManagerTest(final String jsonPath, AppFlowJsonListener appFlowJsonListener) {
            super(jsonPath, appFlowJsonListener);
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
    }*/
}