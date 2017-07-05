package com.philips.amwelluapp.intake;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.VisitManager;
import com.philips.amwelluapp.CustomRobolectricRunnerAmwel;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.utility.PTHConstants;
import com.philips.amwelluapp.utility.PTHManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowFrameLayout;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(CustomRobolectricRunnerAmwel.class)
public class PTHSymptomsFragmentTest {

    @Mock
    AWSDK awsdkMock;

    PTHSymptomsFragment pthSymptomsFragment;

    @Mock
    PTHSymptomsPresenter presenterMock;

    @Mock
    Typeface typefaceMock;

    @Mock
    PTHConsumer pthConsumer;

    @Mock
    PTHProviderInfo pthProviderInfoMock;

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
    PTHVisitContext pthVisitContextMock;

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

        pthSymptomsFragment = new PTHSymptomsFragment();
        pthSymptomsFragment.setActionBarListener(actionBarListenerMock);
        pthSymptomsFragment.face = typefaceMock;

        Bundle bundle = new Bundle();
        bundle.putParcelable(PTHConstants.THS_CONSUMER, pthConsumer);
        bundle.putParcelable(PTHConstants.THS_PROVIDER_INFO,pthProviderInfoMock);
        pthSymptomsFragment.setArguments(bundle);

        PTHManager.getInstance().setAwsdk(awsdkMock);

        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(pthProviderInfoMock.getProviderInfo()).thenReturn(providerInfoMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);

        SupportFragmentTestUtil.startFragment(pthSymptomsFragment);
    }

    @Test
    public void addTopicsToViewWithListSizeZero() throws Exception {
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(visitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(pthVisitContextMock);
        final int childCount = pthSymptomsFragment.topicLayout.getChildCount();
        assertThat(childCount).isEqualTo(0);
    }

    @Test
    public void addTopicsToView() throws Exception {
        topicList.add(topicMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pthVisitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(pthVisitContextMock);
        final int childCount = pthSymptomsFragment.topicLayout.getChildCount();
        assertThat(childCount).isEqualTo(1);
    }

    @Test
    public void addTopicsToViewClickCheckBox() throws Exception {
        topicList.add(topicMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pthVisitContextMock.getTopics()).thenReturn(topicList);
        pthSymptomsFragment.addTopicsToView(pthVisitContextMock);
        final View childAt = pthSymptomsFragment.topicLayout.getChildAt(0);
        childAt.performClick();

        final Topic topic = topicList.get(0);
        verify(topic).setSelected(true);
    }

    @Test
    public void onClick() throws Exception {
        pthSymptomsFragment.mPTHSymptomsPresenter = presenterMock;
        pthSymptomsFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = pthSymptomsFragment.getView().findViewById(R.id.continue_btn);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.continue_btn);
    }

}