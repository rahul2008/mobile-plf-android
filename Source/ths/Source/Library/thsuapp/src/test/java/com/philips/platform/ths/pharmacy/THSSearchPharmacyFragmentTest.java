package com.philips.platform.ths.pharmacy;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.base.THSBaseFragment;
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
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSSearchPharmacyFragmentTest {
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
    FragmentActivity activityMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    THSSearchPharmacyFragment thsSearchPharmacyFragmentMock;

    @Mock
    List<Pharmacy> pharmacyListMock;

    @Mock
    THSBaseFragment thsBaseFragmentMock;

    THSSearchPharmacyFragment thsSearchPharmacyFragment;

    @Mock
    Context applicationContextMock;
    @Before
    public void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);


        THSManager.getInstance().setPTHConsumer(thsConsumerWrapper);
        THSManager.getInstance().setVisitContext(pthVisitContext);

        when(thsConsumerWrapper.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        thsSearchPharmacyFragment = new THSSearchPharmacyFragment();
        thsSearchPharmacyFragment.setActionBarListener(actionBarListenerMock);
        thsSearchPharmacyFragment.setFragmentLauncher(fragmentLauncherMock);

    }

    @Test
    public void lauchSearchPharmacy(){
        SupportFragmentTestUtil.startFragment(thsSearchPharmacyFragment);
    }

    @Test
    public void verifySetList(){
        SupportFragmentTestUtil.startFragment(thsSearchPharmacyFragmentMock);
        thsSearchPharmacyFragmentMock.callPharmacyListFragment();
        verify(thsSearchPharmacyFragmentMock).callPharmacyListFragment();
    }

}
