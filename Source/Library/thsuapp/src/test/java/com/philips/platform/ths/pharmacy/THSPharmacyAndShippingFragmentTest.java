package com.philips.platform.ths.pharmacy;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.intake.THSVisitContext;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSPharmacyAndShippingFragmentTest {

    THSPharmacyAndShippingFragment thsPharmacyAndShippingFragment;

    @Mock
    Address address;

    @Mock
    Pharmacy pharmacy;

    @Mock
    THSConsumerWrapper thsConsumerWrapper;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSVisitContext pthVisitContext;

    @Mock
    Consumer consumerMock;

    @Mock
    VisitContext visitManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    State stateMock;

    @Mock
    Address addressMock;

    @Mock
    THSPharmacyAndShippingFragment thsPharmacyAndShippingFragmentMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Before
    public void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);


        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapper);
        THSManager.getInstance().setVisitContext(pthVisitContext);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);
        when(thsConsumerWrapper.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(address.getState()).thenReturn(stateMock);
        when(stateMock.getCode()).thenReturn("12121");
        when(pharmacy.getAddress()).thenReturn(address);
        when(pharmacy.getAddress().getState()).thenReturn(stateMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        thsPharmacyAndShippingFragment = new THSPharmacyAndShippingFragment();
        thsPharmacyAndShippingFragment.setPharmacyAndAddress(address,pharmacy);
        thsPharmacyAndShippingFragment.setFragmentLauncher(fragmentLauncherMock);
    }

    @Test
    public void launchPharmacyShippingFragment(){
        when(consumerMock.getFullName()).thenReturn("Some value");
        SupportFragmentTestUtil.startFragment(thsPharmacyAndShippingFragment);
    }

    @Test
    public void testUpdateShippingAddressView(){
        SupportFragmentTestUtil.startFragment(thsPharmacyAndShippingFragment);
        thsPharmacyAndShippingFragmentMock.updateShippingAddressView(address);
        verify(thsPharmacyAndShippingFragmentMock).updateShippingAddressView(eq(address));
    }

    @Test
    public void testUpdatePharmacyDetailsView(){
        SupportFragmentTestUtil.startFragment(thsPharmacyAndShippingFragment);
        thsPharmacyAndShippingFragmentMock.updatePharmacyDetailsView(pharmacy);
        verify(thsPharmacyAndShippingFragmentMock).updatePharmacyDetailsView(eq(pharmacy));
    }

    @Test
    public void startSearchPharmacyTest(){
        SupportFragmentTestUtil.startFragment(thsPharmacyAndShippingFragmentMock);
        thsPharmacyAndShippingFragmentMock.startSearchPharmacy();
        verify(thsPharmacyAndShippingFragmentMock).startSearchPharmacy();
    }

}
