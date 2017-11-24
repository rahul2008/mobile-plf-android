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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by philips on 23/11/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
public class CCInterfaceTest {

    int DLS_THEME = com.philips.platform.uid.R.style.Theme_DLS_Purple_VeryDark;

    @Mock
    private Context mContext;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = RuntimeEnvironment.application.getApplicationContext();
        ccInterface = new CcInterface();
        ccInterface.init(ccDependenciesMock, ccSettingsMock);
    }

    @Test
    public void testInit() {
        Assert.assertNotNull(ccDependenciesMock);
        Assert.assertNotNull(ccSettingsMock);
    }

    @Test(expected = RuntimeException.class)
    public void testActivityLaunch() throws IllegalArgumentException{
        String[] ctnList = {"SCD888/26", "HX9192/03"};

        when(activityLauncherMock.getEnterAnimation()).thenReturn(R.anim.consumercare_left_in);
        when(activityLauncherMock.getExitAnimation()).thenReturn(R.anim.consumercare_right_out);
        when(activityLauncherMock.getUiKitTheme()).thenReturn(DLS_THEME);

        ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
        productsSelection.setCatalog(PrxConstants.Catalog.CARE);
        productsSelection.setSector(PrxConstants.Sector.B2C);

        when(productModelSelectionTypeMock.getHardCodedProductList()).thenReturn(ctnList);
        when(productModelSelectionTypeMock.getCatalog()).thenReturn(PrxConstants.Catalog.CARE);
        when(productModelSelectionTypeMock.getSector()).thenReturn(PrxConstants.Sector.B2C);

        when(ccLaunchInputMock.getProductModelSelectionType()).thenReturn(productsSelection);

        ccInterface.launch(activityLauncherMock, ccLaunchInputMock);
    }

    @Test(expected = RuntimeException.class)
    public void testFragmentLaunch() throws IllegalArgumentException{
        final String[] ctnList = { "HX6064/33"};

        when(activityLauncherMock.getEnterAnimation()).thenReturn(R.anim.consumercare_left_in);
        when(activityLauncherMock.getExitAnimation()).thenReturn(R.anim.consumercare_right_out);
        when(fragmentLauncherMock.getParentContainerResourceID()).thenReturn(DLS_THEME);
        when(fragmentLauncherMock.getActionbarListener()).thenReturn(actionBarListenerMock);

        ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
        productsSelection.setCatalog(PrxConstants.Catalog.CARE);
        productsSelection.setSector(PrxConstants.Sector.B2C);

        when(productModelSelectionTypeMock.getHardCodedProductList()).thenReturn(ctnList);
        when(productModelSelectionTypeMock.getCatalog()).thenReturn(PrxConstants.Catalog.CARE);
        when(productModelSelectionTypeMock.getSector()).thenReturn(PrxConstants.Sector.B2C);

        when(ccLaunchInputMock.getProductModelSelectionType()).thenReturn(productsSelection);

        ccInterface.launch(fragmentLauncherMock, ccLaunchInputMock);
    }
}
