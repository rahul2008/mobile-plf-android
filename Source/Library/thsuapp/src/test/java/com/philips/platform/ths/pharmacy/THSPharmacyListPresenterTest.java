package com.philips.platform.ths.pharmacy;

import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSPharmacyListPresenterTest {
    @Mock
    THSPharmacyListViewListener thsPharmacyListViewListener;


    THSPharmacyListPresenter thsPharmacyListPresenter;

    @Mock
    THSPharmacyListFragment thsPharmacyListFragment;

    @Mock
    AWSDK awsdkMock;
    @Mock
    Consumer consumerMock;

    @Mock
    VisitContext visitManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;
    @Mock
    THSConsumerWrapper pthConsumerMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Pharmacy pharmacy;

    @Mock
    Consumer consumer;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsPharmacyListPresenter = new THSPharmacyListPresenter(thsPharmacyListViewListener);
        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(thsPharmacyListFragment.getFragmentActivity()).thenReturn(activityMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

    }

    @Test
    public void testOnEvent(){
        thsPharmacyListPresenter.onEvent(R.id.switch_view_layout);
        verify(thsPharmacyListViewListener).switchView();
        thsPharmacyListPresenter.onEvent(R.id.segment_control_view_one);
        verify(thsPharmacyListViewListener).showRetailView();
        thsPharmacyListPresenter.onEvent(R.id.segment_control_view_two);
        verify(thsPharmacyListViewListener).showMailOrderView();
        thsPharmacyListPresenter.onEvent(R.id.choose_pharmacy_button);
        verify(thsPharmacyListViewListener).setPreferredPharmacy();

    }

    @Test
    public void testFetchPharmacyList(){
        thsPharmacyListPresenter.fetchPharmacyList(pthConsumerMock,(float)0.11,(float)0.11,(int)0.5);
        when(pthConsumerMock.getConsumer()).thenReturn(consumer);
        verify(awsdkMock.getConsumerManager()).getPharmacies(any(Consumer.class),any(Float.class),any(Float.class),any(Integer.class),any(Boolean.class),any(SDKCallback.class));
    }

    @Test
    public void testUpdateConsumerPreferredPharmacy(){
        thsPharmacyListPresenter.updateConsumerPreferredPharmacy(pharmacy);
        when(pthConsumerMock.getConsumer()).thenReturn(consumer);
        verify(awsdkMock.getConsumerManager()).updateConsumerPharmacy(any(Consumer.class),any(Pharmacy.class),any(SDKCallback.class));
    }
}
