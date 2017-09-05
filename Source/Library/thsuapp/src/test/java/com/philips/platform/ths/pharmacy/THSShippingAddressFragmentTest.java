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
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.intake.THSVisitContext;
import com.philips.platform.ths.registration.THSConsumer;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSShippingAddressFragmentTest {

    @Mock
    Address address;

    @Mock
    Pharmacy pharmacy;

    @Mock
    THSConsumer thsConsumer;

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

    @Before
    public void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);


        THSManager.getInstance().setPTHConsumer(thsConsumer);
        THSManager.getInstance().setVisitContext(pthVisitContext);

        when(thsConsumer.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

        thsShippingAddressFragment = new THSShippingAddressFragment();
        when(thsShippingAddressFragmentMock.getActivity()).thenReturn(activityMock);
        when(thsShippingAddressFragmentMock.getActivity().getApplicationContext()).thenReturn(applicationContextMock);
        when(thsShippingAddressFragmentMock.getSupportedCountries()).thenReturn(countryListMock);
        when(awsdkMock.getConsumerManager().getValidShippingStates(countryMock)).thenReturn(stateListMock);
        when(thsShippingAddressFragmentMock.getValidShippingStates(countryListMock)).thenReturn(stateListMock);

        thsShippingAddressFragment.setConsumerAndAddress(thsConsumer,address);
        thsShippingAddressFragment.setFragmentLauncher(fragmentLauncherMock);
    }

    @Test
    public void launchShippingFragment(){
        SupportFragmentTestUtil.startFragment(thsShippingAddressFragmentMock);
        thsShippingAddressFragmentMock.onClick(any(View.class));
        verify(thsShippingAddressFragmentMock).onClick(any(View.class));
    }
}
