package com.philips.platform.ths.intake;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.VisitManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSSymptomsFragmentTest {

    @Mock
    AWSDK awsdkMock;

    THSSymptomsFragmentMock pthSymptomsFragment;

    @Mock
    THSSymptomsPresenter presenterMock;

    @Mock
    Typeface typefaceMock;

    @Mock
    THSProviderInfo pthProviderInfoMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    VisitManager visitManagerMock;


    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    THSVisitContext pthVisitContextMock;

    @Mock
    VisitContext visitContextMock;

    List<Topic> topicList = new ArrayList<>();

    @Mock
    Topic topicMock;

    @Mock
    THSConsumerWrapper pthConsumer;

    @Mock
    THSVisitContext pthVisitContext;

    @Mock
    Consumer consumerMock;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSConsumer thsConsumerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        pthSymptomsFragment = new THSSymptomsFragmentMock();
        pthSymptomsFragment.setActionBarListener(actionBarListenerMock);

        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_PROVIDER_INFO,pthProviderInfoMock);
        pthSymptomsFragment.setArguments(bundle);

        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumer);
        THSManager.getInstance().setVisitContext(pthVisitContext);
        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(pthProviderInfoMock.getProviderInfo()).thenReturn(providerInfoMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);
    }

 /*   @Test
    public void addTopicsToViewWithListSizeZero() throws Exception {
        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(visitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(pthVisitContextMock);
        final int childCount = pthSymptomsFragment.topicLayout.getChildCount();
        assertThat(childCount).isEqualTo(0);
    }*/

/*    @Test
    public void addTopicsToViewWithListHavingTopicsSelected() throws Exception {
        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
        topicList.add(topicMock);
        when(topicMock.isSelected()).thenReturn(true);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pthVisitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(pthVisitContextMock);
        final int childCount = pthSymptomsFragment.topicLayout.getChildCount();
        assertThat(childCount).isEqualTo(1);
    }*/

/*    @Test
    public void addTopicsToView() throws Exception {
        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
        topicList.add(topicMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pthVisitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(pthVisitContextMock);
        final int childCount = pthSymptomsFragment.topicLayout.getChildCount();
        assertThat(childCount).isEqualTo(1);
    }*/

  /*  @Test
    public void addTopicsToViewClickCheckBox() throws Exception {
        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
        topicList.add(topicMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pthVisitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(pthVisitContextMock);
        final View childAt = pthSymptomsFragment.topicLayout.getChildAt(0);
        childAt.performClick();

        final Topic topic = topicList.get(0);
        verify(topic).setSelected(true);
    }*/

    @Test
    public void onClick() throws Exception {
        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
        pthSymptomsFragment.thsSymptomsPresenter = presenterMock;
        pthSymptomsFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = pthSymptomsFragment.getView().findViewById(R.id.continue_btn);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.continue_btn);
    }

    @Test
    public void onActivityCreatedWhenActionBarIsNull(){
        pthSymptomsFragment.setActionBarListener(null);
        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
        assertNull(pthSymptomsFragment.getActionBarListener());
    }

    @Test
    public void handleBackEvent() throws Exception {
        Assert.assertEquals(pthSymptomsFragment.handleBackEvent(),false);
    }

   /* @Test
    public void getVisistContextWhenPthContextIsNotNull() {
        pthSymptomsFragment.mThsVisitContext = pthVisitContext;
        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
    }*/
}