package com.philips.platform.ths.pharmacy;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
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

import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSShippingAddressPresenterTest {
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
    THSShippingAddressFragment thsShippingAddressFragmentMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    THSShippingAddressFragment thsShippingAddressFragment;

    @Mock
    List<State> stateListMock;

    @Mock
    List<Country> countryListMock;

    @Mock
    Context applicationContextMock;

    @Mock
    Country countryMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    THSShippingAddressPresenter thsShippingAddressPresenterMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Before
    public void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        THSManager.getInstance().setPTHConsumer(thsConsumerWrapper);
        THSManager.getInstance().setVisitContext(pthVisitContext);

        when(thsConsumerWrapper.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

        thsShippingAddressFragment = new THSShippingAddressFragment();
        when(thsShippingAddressFragmentMock.getActivity()).thenReturn(activityMock);
        when(thsShippingAddressFragmentMock.getActivity().getApplicationContext()).thenReturn(applicationContextMock);
        when(thsShippingAddressFragmentMock.getSupportedCountries()).thenReturn(countryListMock);
        when(awsdkMock.getConsumerManager().getValidShippingStates(countryMock)).thenReturn(stateListMock);
        when(thsShippingAddressFragmentMock.getValidShippingStates(countryListMock)).thenReturn(stateListMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        thsShippingAddressFragment.setConsumerAndAddress(thsConsumerWrapper,address);
        thsShippingAddressFragment.setFragmentLauncher(fragmentLauncherMock);

        when(countryListMock.size()).thenReturn(1);
        when(stateListMock.size()).thenReturn(1);
        when(countryListMock.get(0)).thenReturn(countryMock);
        thsShippingAddressFragment.supportedCountries = countryListMock;
        when(awsdkMock.getSupportedCountries()).thenReturn(countryListMock);
        when(consumerManagerMock.getValidShippingStates(countryMock)).thenReturn(stateListMock);
    }

    @Test
    public void updateShippingAddressTest(){
        when(awsdkMock.getNewAddress()).thenReturn(addressMock);
        SupportFragmentTestUtil.startFragment(thsShippingAddressFragment);
        final View viewById = thsShippingAddressFragment.getView().findViewById(R.id.update_shipping_address);
        viewById.performClick();
        verify(awsdkMock.getConsumerManager()).updateShippingAddress(any(Consumer.class),any(Address.class),any(SDKValidatedCallback.class));
    }
}
