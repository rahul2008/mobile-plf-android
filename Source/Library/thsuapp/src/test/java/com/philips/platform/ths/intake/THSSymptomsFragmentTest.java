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
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.PTHManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSSymptomsFragmentTest {

    @Mock
    AWSDK awsdkMock;

    THSSymptomsFragment pthSymptomsFragment;

    @Mock
    THSSymptomsPresenter presenterMock;

    @Mock
    Typeface typefaceMock;

    @Mock
    THSConsumer THSConsumer;

    @Mock
    THSProviderInfo THSProviderInfoMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    VisitManager visitManagerMock;


    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    THSVisitContext THSVisitContextMock;

    @Mock
    VisitContext visitContextMock;

    List<Topic> topicList = new ArrayList<>();

    @Mock
    Topic topicMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        PTHManager.getInstance().setAwsdk(awsdkMock);

        pthSymptomsFragment = new THSSymptomsFragment();
        pthSymptomsFragment.setActionBarListener(actionBarListenerMock);
        pthSymptomsFragment.face = typefaceMock;

        Bundle bundle = new Bundle();

        bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, THSProviderInfoMock);
        pthSymptomsFragment.setArguments(bundle);

        PTHManager.getInstance().setAwsdk(awsdkMock);

        when(THSConsumer.getConsumer()).thenReturn(consumerMock);
        when(THSProviderInfoMock.getProviderInfo()).thenReturn(providerInfoMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);

        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
    }

    @Test
    public void addTopicsToViewWithListSizeZero() throws Exception {
        when(THSVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(visitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(THSVisitContextMock);
        final int childCount = pthSymptomsFragment.topicLayout.getChildCount();
        assertThat(childCount).isEqualTo(0);
    }

    @Test
    public void addTopicsToView() throws Exception {
        topicList.add(topicMock);
        when(THSVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(THSVisitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(THSVisitContextMock);
        final int childCount = pthSymptomsFragment.topicLayout.getChildCount();
        assertThat(childCount).isEqualTo(1);
    }

    @Test
    public void addTopicsToViewClickCheckBox() throws Exception {
        topicList.add(topicMock);
        when(THSVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(THSVisitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(THSVisitContextMock);
        final View childAt = pthSymptomsFragment.topicLayout.getChildAt(0);
        childAt.performClick();

        final Topic topic = topicList.get(0);
        verify(topic).setSelected(true);
    }

    @Test
    public void onClick() throws Exception {
        pthSymptomsFragment.mTHSSymptomsPresenter = presenterMock;
        pthSymptomsFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = pthSymptomsFragment.getView().findViewById(R.id.continue_btn);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.continue_btn);
    }

}