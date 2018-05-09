package com.philips.platform.ths.providerdetails;


import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProviderListPresenter;
import com.philips.platform.ths.providerslist.THSProviderListViewInterface;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProviderDetailsFragmentTest {

    private THSLaunchActivity mActivity;
    private THSProviderDetailsFragmentMock providerDetailsFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    Date dateMock;

    @Mock
    ActionBarListener actionBarListener;

    @Mock
    FragmentLauncher framgmentLauncherMock;

    @Mock
    THSProviderListViewInterface THSProviderListViewInterface;

    @InjectMocks
    THSProviderListPresenter providerListPresenter;

    @Mock
    Consumer consumerMock;
    @Mock
    ProviderInfo providerInfo;

    @Mock
    Practice practiceMock;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Mock
    THSBaseView THSBaseView;

    @Mock
    Provider providerMock;

    @Mock
    THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelperMock;

    @Mock
    THSProviderEntity thsProviderEntityMock;

    @Mock
    THSProviderInfo thsProviderInfoMock;

    @Mock
    THSAvailableProvider thsAvailableProviderInfoMock;

    @Mock
    List listMock;

    @Mock
    THSProviderDetailsPresenter presenterMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        when(THSBaseView.getFragmentActivity()).thenReturn(mActivity);
        when(THSManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        providerDetailsFragment = new THSProviderDetailsFragmentMock();
        providerDetailsFragment.setConsumerAndPractice(consumerMock, practiceMock);
        providerDetailsFragment.setActionBarListener(actionBarListener);
    }

    @Test
    public  void testFragment(){
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        assertNotNull(providerDetailsFragment);
    }

    @Test
    public void updateView(){
        providerDetailsFragment.mThsProviderDetailsDisplayHelper = thsProviderDetailsDisplayHelperMock;
        providerDetailsFragment.updateView(providerMock);

        List list = new ArrayList();
        list.add(dateMock);
        verify(thsProviderDetailsDisplayHelperMock).updateView(any(Provider.class),(List)isNull());
    }

    @Test
    public void setTHSProviderEntity(){
        providerDetailsFragment.setTHSProviderEntity(thsProviderEntityMock);
        assertNull(providerDetailsFragment.mThsProviderInfo);
        assertNull(providerDetailsFragment.mThsAvailableProvider);
    }

    @Test
    public void setTHSProviderEntityInstanceTHSProviderInfo(){
        providerDetailsFragment.setTHSProviderEntity(thsProviderInfoMock);
        assertNotNull(providerDetailsFragment.mThsProviderInfo);
        assertNull(providerDetailsFragment.mThsAvailableProvider);
    }

    @Test
    public void setTHSProviderEntityInstanceTHSAvailablrProviderInfo(){
        providerDetailsFragment.setTHSProviderEntity(thsAvailableProviderInfoMock);
        assertNull(providerDetailsFragment.mThsProviderInfo);
        assertNotNull(providerDetailsFragment.mThsAvailableProvider);
    }

    @Test
    public void getTHSProviderInfoWhenThsProviderInfoIsNull(){
        THSProviderInfo thsProviderInfo = providerDetailsFragment.getTHSProviderInfo();
        assertNull(thsProviderInfo);
    }

    @Test
    public void getTHSProviderInfoWhenThsProviderInfoIsNotNull(){
        providerDetailsFragment.mThsProviderInfo = thsProviderInfoMock;
        THSProviderInfo thsProviderInfo = providerDetailsFragment.getTHSProviderInfo();
        assertNotNull(thsProviderInfo);
    }

    @Test
    public void getTHSProviderInfoWhenThsAvailableProviderInfoIsNotNull(){
        providerDetailsFragment.mThsAvailableProvider = thsAvailableProviderInfoMock;
        THSProviderInfo thsProviderInfo = providerDetailsFragment.getTHSProviderInfo();
        assertNotNull(thsProviderInfo);
    }

    @Test
    public void getPracticeInfoNotNull(){
        providerDetailsFragment.setConsumerAndPractice(consumerMock,practiceMock);
        Practice practiceInfo = providerDetailsFragment.getPractice();
        assertNotNull(practiceInfo);
    }

    @Test
    public void getConsumerInfoNotNull(){
        providerDetailsFragment.setConsumerAndPractice(consumerMock,practiceMock);
        Consumer consumerInfo = providerDetailsFragment.getConsumerInfo();
        assertNotNull(consumerInfo);
    }

    @Test
    public void dismissRefreshLayout(){
        providerDetailsFragment.mThsProviderDetailsDisplayHelper = thsProviderDetailsDisplayHelperMock;
        providerDetailsFragment.dismissRefreshLayout();
        verify(thsProviderDetailsDisplayHelperMock).dismissRefreshLayout();
    }

  /*  @Test
    public void getAppointmentTimeSlots(){
        List<Date> appointmentTimeSlots = providerDetailsFragment.getAppointmentTimeSlots();
        assertNull(appointmentTimeSlots);
    }*/

  /*  @Test
    public void getAppointmentTimeSlotsNotNull(){
        providerDetailsFragment.mThsAvailableProvider = thsAvailableProviderInfoMock;
        when(thsAvailableProviderInfoMock.getAvailableAppointmentTimeSlots()).thenReturn(listMock);
        List<Date> appointmentTimeSlots = providerDetailsFragment.getAppointmentTimeSlots();
        assertNotNull(appointmentTimeSlots);
    }*/

    @Test
    public void getFragmentTag(){
        String fragmentTag = providerDetailsFragment.getFragmentTag();
        assert fragmentTag.equalsIgnoreCase(THSProviderDetailsFragment.class.getSimpleName());
    }

    @Test
    public void getFragmentActivty(){
        FragmentActivity fragmentActivity = providerDetailsFragment.getFragmentActivity();
        assertNull(fragmentActivity);
    }

    @Test
    public void getFragmentActivtyNotNull(){
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        FragmentActivity fragmentActivity = providerDetailsFragment.getFragmentActivity();
        assertNotNull(fragmentActivity);
    }

    @Test
    public void onClickdetailsButtonOne() {
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        providerDetailsFragment.providerDetailsPresenter = presenterMock;
        providerDetailsFragment.setFragmentLauncher(framgmentLauncherMock);
        final View viewById = providerDetailsFragment.getView().findViewById(R.id.detailsButtonOne);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.detailsButtonOne);
    }

    @Test
    public void onClickdetailsButtonTwo() {
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        providerDetailsFragment.providerDetailsPresenter = presenterMock;
        providerDetailsFragment.setFragmentLauncher(framgmentLauncherMock);
        final View viewById = providerDetailsFragment.getView().findViewById(R.id.detailsButtonTwo);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.detailsButtonTwo);
    }
}
