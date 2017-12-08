package com.philips.cdp.digitalcare;

import android.content.Context;

import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerCC;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import junit.framework.Assert;

import org.apache.tools.ant.taskdefs.Length;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by philips on 23/11/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
public class CCInterfaceTest {

    int DLS_THEME = com.philips.platform.uid.R.style.Theme_DLS_Purple_VeryDark;

    @Mock
    CcDependencies ccDependenciesMock;

    @Mock
    CcLaunchInput ccLaunchInputMock;

    @Mock
    CcSettings ccSettingsMock;

    @Mock
    CcInterface ccInterface;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    ActivityLauncher activityLauncherMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ProductModelSelectionType productModelSelectionTypeMock;

    @Mock
    private HardcodedProductList hardcodedProductList;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    AppTaggingInterface mockAppTaggingInterface;

    @Mock
    private DigitalCareConfigManager mockDigitalCareConfigManager;

    private Context mContext;

    private DigitalCareConfigManager digitalCareConfigManager;

    private  AppInfraInterface appInfraInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = mock(Context.class);
        appInfraInterface = Mockito.mock(AppInfraInterface.class);
        digitalCareConfigManager = DigitalCareConfigManager.getInstance();
        ccInterface = new CcInterface();
        ccInterface.init(ccDependenciesMock, ccSettingsMock);


        when(activityLauncherMock.getEnterAnimation()).thenReturn(R.anim.consumercare_left_in);
        when(activityLauncherMock.getExitAnimation()).thenReturn(R.anim.consumercare_right_out);
        when(activityLauncherMock.getUiKitTheme()).thenReturn(DLS_THEME);

        String[] ctnList = {"SCD888/26", "HX9192/03"};

        ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
        productsSelection.setCatalog(PrxConstants.Catalog.CARE);
        productsSelection.setSector(PrxConstants.Sector.B2C);

        when(productModelSelectionTypeMock.getHardCodedProductList()).thenReturn(ctnList);
        when(productModelSelectionTypeMock.getCatalog()).thenReturn(PrxConstants.Catalog.CARE);
        when(productModelSelectionTypeMock.getSector()).thenReturn(PrxConstants.Sector.B2C);

        when(ccLaunchInputMock.getProductModelSelectionType()).thenReturn(productsSelection);
    }

    @Test
    public void testInit() {
        Assert.assertNotNull(ccDependenciesMock);
        Assert.assertNotNull(ccSettingsMock);
    }

    @After
    public void tearDown(){
        mContext = null;
        appInfraInterface = null;
        digitalCareConfigManager = null;
    }
    @Test
    public void testActivityLaunch() throws IllegalArgumentException{

        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        when(activityLauncherMock.getScreenOrientation()).thenReturn(com.philips.platform.uappframework.
                launcher.ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED);

        ccInterface.launch(activityLauncherMock, ccLaunchInputMock);
    }

    @Test
    public void testActivityLaunchNull() throws IllegalArgumentException{
        ccInterface = mock(CcInterface.class);
        digitalCareConfigManager.initializeDigitalCareLibrary(null, null);
        when(activityLauncherMock.getScreenOrientation()).thenReturn(com.philips.platform.uappframework.
                launcher.ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED);

       verify(ccInterface, never()).launch(activityLauncherMock, ccLaunchInputMock);
    }

    @Test(expected = NullPointerException.class)
    public void testFragmentLaunch() throws IllegalArgumentException{

        digitalCareConfigManager.initializeDigitalCareLibrary(mContext, appInfraInterface);
        when(fragmentLauncherMock.getActionbarListener()).thenReturn(actionBarListenerMock);

        ccInterface.launch(fragmentLauncherMock, ccLaunchInputMock);
    }
}
