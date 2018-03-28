package com.philips.platform.ths.pharmacy;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.pharmacy.PharmacyType;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.intake.THSVisitContext;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class) @Config(shadows = THSShadowMapFragment.class)
public class THSPharmacyListFragmentTest {

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapper;

    @Mock
    THSVisitContext pthVisitContext;

    @Mock
    Consumer consumerMock;

    @Mock
    VisitContext visitManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSPharmacyListFragment thsPharmacyListFragmentMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Pharmacy pharmacy;
    @Mock
    THSShippingAddressFragment thsShippingAddressFragment;
    @Mock
    Address address;

    @Mock
    FragmentActivity activityMock;


    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    THSPharmacyListFragment thsPharmacyListFragment;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    Address addressMock;

    @Mock
    State stateMock;

    @Mock
    List<Pharmacy> pharmacyListMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        when(thsConsumerWrapper.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);


        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        thsShippingAddressFragment = new THSShippingAddressFragment();
        thsShippingAddressFragment.setActionBarListener(actionBarListenerMock);
        thsShippingAddressFragment.setConsumerAndAddress(thsConsumerWrapper,address);
        thsShippingAddressFragment.setFragmentLauncher(fragmentLauncherMock);

        thsPharmacyListFragment = new THSPharmacyListFragment();
        thsPharmacyListFragment.setActionBarListener(actionBarListenerMock);
        thsPharmacyListFragment.setFragmentLauncher(fragmentLauncherMock);

    }

    @Test
    public void launchPharmacyListFragment(){
        SupportFragmentTestUtil.startFragment(thsPharmacyListFragment);
    }

    @Test
    public void testShowShippingFragment(){
        thsPharmacyListFragmentMock.showShippingFragment();
        verify(thsPharmacyListFragmentMock).showShippingFragment();
    }

    @Test
    public void testLaunchInsuranceCostSummary(){
        thsPharmacyListFragmentMock.launchInsuranceCostSummary();
        verify(thsPharmacyListFragmentMock).launchInsuranceCostSummary();
    }

    @Test
    public void testHandleBackEvent(){
        thsPharmacyListFragmentMock.handleBackEvent();
        verify(thsPharmacyListFragmentMock).handleBackEvent();
        when(thsPharmacyListFragmentMock.handleBackEvent()).thenReturn(false);
        assertEquals(thsPharmacyListFragmentMock.handleBackEvent(),false);
    }

    @Test
    public void testHideSelectedPharmacy(){
        thsPharmacyListFragmentMock.hideSelectedPharmacy();
        verify(thsPharmacyListFragmentMock).hideSelectedPharmacy();
        when(thsPharmacyListFragmentMock.hideSelectedPharmacy()).thenReturn(true);
        assertEquals(thsPharmacyListFragmentMock.hideSelectedPharmacy(),true);
    }



    @Test
    public void testShowSelectedPharmacyDetails(){
        when(pharmacy.getAddress()).thenReturn(addressMock);
        when(pharmacy.getAddress().getAddress1()).thenReturn("Smoething");
        when(pharmacy.getAddress().getAddress2()).thenReturn("Smoething else");
        when(pharmacy.getAddress().getState()).thenReturn(stateMock);
        when(pharmacy.getAddress().getState().getName()).thenReturn("Chicago");
        when(pharmacy.getAddress().getZipCode()).thenReturn("60060");
        when(pharmacy.getEmail()).thenReturn("TestEmail");
        SupportFragmentTestUtil.startFragment(thsPharmacyListFragment);

        thsPharmacyListFragment.showSelectedPharmacyDetails(pharmacy);
        assertNotNull(pharmacy.getEmail());
    }

    @Test
    public void testHandlebackTrue(){

        SupportFragmentTestUtil.startFragment(thsPharmacyListFragment);
        thsPharmacyListFragment.pharmacy_segment_control_one.setSelected(true);
        thsPharmacyListFragment.handleBackEvent();
        assertEquals(thsPharmacyListFragment.handleBackEvent(),false);
    }



    @Test
    public void testHideSelectedPharmacyTest(){

        SupportFragmentTestUtil.startFragment(thsPharmacyListFragment);
        thsPharmacyListFragment.selectedPharmacyLayout.setVisibility(View.VISIBLE);
        thsPharmacyListFragment.hideSelectedPharmacy();
        assertEquals(thsPharmacyListFragment.hideSelectedPharmacy(),false);
    }

    @Test
    public void testShowMapFragment(){
        SupportFragmentTestUtil.startFragment(thsPharmacyListFragment);
        thsPharmacyListFragment.showMapFragment();
        assertEquals(thsPharmacyListFragment.isListSelected,false);

    }

    @Test
    public void testShowListFragment(){
        SupportFragmentTestUtil.startFragment(thsPharmacyListFragment);
        thsPharmacyListFragment.hideMapFragment();
        assertEquals(thsPharmacyListFragment.isListSelected,true);

    }

    @Test
    public void testValidateForMailOrder(){
        SupportFragmentTestUtil.startFragment(thsPharmacyListFragment);
        thsPharmacyListFragment.mFragmentLauncher = fragmentLauncherMock;
        when(fragmentLauncherMock.getParentContainerResourceID()).thenReturn(R.id.conditions_container);
        when(pharmacy.getType()).thenReturn(PharmacyType.MAIL_ORDER);
        thsPharmacyListFragment.validateForMailOrder(pharmacy);
        assertTrue(thsPharmacyListFragment.getActivity().getSupportFragmentManager().findFragmentByTag(THSShippingAddressFragment.TAG) != null);
    }

    @Test
    public void testSwtichView(){
        thsPharmacyListFragmentMock.switchView();
        verify(thsPharmacyListFragmentMock,atLeastOnce()).switchView();
    }
}
