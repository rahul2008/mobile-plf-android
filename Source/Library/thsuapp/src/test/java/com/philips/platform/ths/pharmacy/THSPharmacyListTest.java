package com.philips.platform.ths.pharmacy;

import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.pharmacy.PharmacyType;
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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSPharmacyListTest {

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumer thsConsumer;

    @Mock
    THSVisitContext pthVisitContext;

    @Mock
    Consumer consumerMock;

    @Mock
    VisitContext visitManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSPharmacyListFragment thsPharmacyListFragment;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        when(thsConsumer.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

        thsShippingAddressFragment = new THSShippingAddressFragment();
        thsShippingAddressFragment.setActionBarListener(actionBarListenerMock);
        thsShippingAddressFragment.setConsumerAndAddress(thsConsumer,address);
        thsShippingAddressFragment.setFragmentLauncher(fragmentLauncherMock);

    }

    @Test
    public void testValidateForMailOrder(){
        when(pharmacy.getType()).thenReturn(PharmacyType.MailOrder);
        thsPharmacyListFragment.validateForMailOrder(pharmacy);
        verify(thsPharmacyListFragment).validateForMailOrder(eq(pharmacy));
    }

    @Test
    public void testShowShippingFragment(){
        thsPharmacyListFragment.showShippingFragment();
        verify(thsPharmacyListFragment).showShippingFragment();
    }

    @Test
    public void testLaunchInsuranceCostSummary(){
        thsPharmacyListFragment.launchInsuranceCostSummary();
        verify(thsPharmacyListFragment).launchInsuranceCostSummary();
    }

    @Test
    public void testHandleBackEvent(){
        thsPharmacyListFragment.handleBackEvent();
        verify(thsPharmacyListFragment).handleBackEvent();
        when(thsPharmacyListFragment.handleBackEvent()).thenReturn(false);
        assertEquals(thsPharmacyListFragment.handleBackEvent(),false);
    }

    @Test
    public void testHideSelectedPharmacy(){
        thsPharmacyListFragment.hideSelectedPharmacy();
        verify(thsPharmacyListFragment).hideSelectedPharmacy();
        when(thsPharmacyListFragment.hideSelectedPharmacy()).thenReturn(true);
        assertEquals(thsPharmacyListFragment.hideSelectedPharmacy(),true);
    }

    @Test
    public void testShowSelectedPharmacyDetails(){
        thsPharmacyListFragment.showSelectedPharmacyDetails(pharmacy);
        verify(thsPharmacyListFragment).showSelectedPharmacyDetails(Matchers.eq(pharmacy));
    }
}
