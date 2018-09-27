package com.philips.platform.ths.providerslist;

import android.view.View;
import android.widget.RelativeLayout;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.philips.cdp.registration.User;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import org.assertj.android.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProvidersListAdapterTest {
    @Mock
    private THSProviderInfo thsProviderInfoMock;
    private THSProvidersListAdapter thsProvidersListAdapter;

    @Mock
    private Practice practiceMock;

    @Mock
    private Consumer thsConsumerMock;

    @Mock
    private AWSDK awsdkMock;

    @Mock
    private com.americanwell.sdk.manager.PracticeProvidersManager practiceProvidersManagerMock;

    @Mock
    private User userMock;

    private RelativeLayout viewGroup;

    @Mock
    private THSAvailableProvider thsAvailableProviderMock;

    @Mock
    private com.americanwell.sdk.entity.provider.ProviderInfo providerInfoMock;

    @Mock
    private com.americanwell.sdk.entity.provider.ProviderType specialityMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        viewGroup = new RelativeLayout(RuntimeEnvironment.application);
        ArrayList<THSProviderInfo> providerInfoList = new ArrayList<>();
        providerInfoList.add(thsProviderInfoMock);
        providerInfoList.add(thsProviderInfoMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setConsumer(thsConsumerMock);
        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiceProvidersManagerMock);
        THSProviderListFragmentMock thsProviderListFragmentMock = new THSProviderListFragmentMock();
        thsProviderListFragmentMock.setPractice(practiceMock);
        SupportFragmentTestUtil.startFragment(thsProviderListFragmentMock);


        thsProvidersListAdapter = new THSProvidersListAdapter(providerInfoList, practiceMock);

    }

    private THSProvidersListAdapter.MyViewHolder getViewHolder() {
        return thsProvidersListAdapter.onCreateViewHolder(viewGroup, 0);
    }

    @Test
    public void ShouldInitializeViewHolderAndBindViewsOnCreateViewHolder() throws Exception {
        THSProvidersListAdapter.MyViewHolder viewHolder = getViewHolder();

        Assertions.assertThat(viewHolder.practice).isNotNull();
        Assertions.assertThat(viewHolder.name).isNotNull();
        Assertions.assertThat(viewHolder.isAvailble).isNotNull();
        Assertions.assertThat(viewHolder.providerRating).isNotNull();
        Assertions.assertThat(viewHolder.providerImage).isNotNull();
        Assertions.assertThat(viewHolder.relativeLayout).isNotNull();
        Assertions.assertThat(viewHolder.notificationBadge).isNotNull();
        Assertions.assertThat(viewHolder.providerPatientWaitingCount).isNotNull();


        THSManager.getInstance().setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);

        viewHolder = getViewHolder();

        Assertions.assertThat(viewHolder.doctorSelectActionBar).isNotNull();
        Assertions.assertThat(viewHolder.doctorScheduleActionBar).isNotNull();
        Assertions.assertThat(viewHolder.doctorAvailableActionBar).isNotNull();
        Assertions.assertThat(viewHolder.scheduleDoctorButton).isNotNull();
        Assertions.assertThat(viewHolder.selectDoctorButton).isNotNull();
    }

    @Test
    public void ShouldUpdateViewWithTextWhenProviderOnline() throws Exception {
        THSManager.getInstance().setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);
        final THSProvidersListAdapter.MyViewHolder viewHolderMock = Mockito.mock(THSProvidersListAdapter.MyViewHolder.class);
        setUpViewholder(viewHolderMock, ProviderVisibility.WEB_AVAILABLE);

        thsProvidersListAdapter.onBindViewHolder(viewHolderMock, 0);
        Assertions.assertThat(viewHolderMock.isAvailble).hasText("Available now");
        assertThat(viewHolderMock.doctorSelectActionBar.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(viewHolderMock.doctorAvailableActionBar.getVisibility()).isEqualTo(View.GONE);
        assertThat(viewHolderMock.doctorScheduleActionBar.getVisibility()).isEqualTo(View.GONE);
        Assertions.assertThat(viewHolderMock.isAvailble).hasText("Available now");
    }

    @Test
    public void ShouldUpdateViewWithTextWhenProviderOnlineAndNonSchedulable() throws Exception {
        THSManager.getInstance().setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);
        when(practiceMock.isShowScheduling()).thenReturn(true);
        final THSProvidersListAdapter.MyViewHolder viewHolderMock = Mockito.mock(THSProvidersListAdapter.MyViewHolder.class);
        setUpViewholder(viewHolderMock, ProviderVisibility.WEB_AVAILABLE);

        thsProvidersListAdapter.onBindViewHolder(viewHolderMock, 0);
        Assertions.assertThat(viewHolderMock.isAvailble).hasText("Available now");
        assertThat(viewHolderMock.doctorSelectActionBar.getVisibility()).isEqualTo(View.GONE);
        assertThat(viewHolderMock.doctorAvailableActionBar.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(viewHolderMock.doctorScheduleActionBar.getVisibility()).isEqualTo(View.GONE);
        Assertions.assertThat(viewHolderMock.isAvailble).hasText("Available now");
    }

    @Test
    public void ShouldUpdateViewWithTextWhenProviderOfflineAndSchedulable() throws Exception {
        THSManager.getInstance().setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);
        when(practiceMock.isShowScheduling()).thenReturn(true);
        final THSProvidersListAdapter.MyViewHolder viewHolderMock = Mockito.mock(THSProvidersListAdapter.MyViewHolder.class);
        setUpViewholder(viewHolderMock, THSConstants.PROVIDER_OFFLINE);

        thsProvidersListAdapter.onBindViewHolder(viewHolderMock, 0);

        Assertions.assertThat(viewHolderMock.isAvailble).hasText("Schedule appointment");
        assertThat(viewHolderMock.doctorSelectActionBar.getVisibility()).isEqualTo(View.GONE);
        assertThat(viewHolderMock.doctorAvailableActionBar.getVisibility()).isEqualTo(View.GONE);
        assertThat(viewHolderMock.doctorScheduleActionBar.getVisibility()).isEqualTo(View.VISIBLE);
        Assertions.assertThat(viewHolderMock.isAvailble).hasText("Schedule appointment");
    }

    @Test
    public void ShouldUpdateViewWithTextWhenProviderOffline() throws Exception {
        THSManager.getInstance().setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);
        when(thsProviderInfoMock.getSpecialty()).thenReturn(specialityMock);
        when(specialityMock.getName()).thenReturn("specialist");
        when(thsProviderInfoMock.hasImage()).thenReturn(true);
        final THSProvidersListAdapter.MyViewHolder viewHolderMock = Mockito.mock(THSProvidersListAdapter.MyViewHolder.class);
        setUpViewholder(viewHolderMock, THSConstants.PROVIDER_OFFLINE);

        thsProvidersListAdapter.onBindViewHolder(viewHolderMock, 0);
        Assertions.assertThat(viewHolderMock.isAvailble).hasText("Schedule appointment");
        assertThat(viewHolderMock.doctorScheduleActionBar.getVisibility()).isEqualTo(View.GONE);
        assertThat(viewHolderMock.doctorSelectActionBar.getVisibility()).isEqualTo(View.GONE);
        assertThat(viewHolderMock.doctorAvailableActionBar.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void ShouldUpdateViewWithTextWhenProviderBusy() throws Exception {
        THSManager.getInstance().setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);
        final THSProvidersListAdapter.MyViewHolder viewHolderMock = Mockito.mock(THSProvidersListAdapter.MyViewHolder.class);
        setUpViewholder(viewHolderMock, THSConstants.PROVIDER_WEB_BUSY);

        thsProvidersListAdapter.onBindViewHolder(viewHolderMock, 0);

        assertThat(viewHolderMock.doctorSelectActionBar.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(viewHolderMock.doctorScheduleActionBar.getVisibility()).isEqualTo(View.GONE);
        assertThat(viewHolderMock.doctorAvailableActionBar.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void ShouldUpdateViewforNonTHSProviderInfo() throws Exception {
        List<THSAvailableProvider> thsAvailableProviderList = new ArrayList<>();
        thsAvailableProviderList.add(thsAvailableProviderMock);
        thsProvidersListAdapter = new THSProvidersListAdapter(thsAvailableProviderList, practiceMock);
        THSManager.getInstance().setProviderListABFlow(THSConstants.THS_PROVIDERLIST_ABFLOW2);
        final THSProvidersListAdapter.MyViewHolder viewHolderMock = Mockito.mock(THSProvidersListAdapter.MyViewHolder.class);
        setUpViewholder(viewHolderMock, THSConstants.PROVIDER_WEB_BUSY);
        when(viewHolderMock.getAdapterPosition()).thenReturn(0);
        when(thsAvailableProviderMock.getProviderInfo()).thenReturn(providerInfoMock);
        when(thsAvailableProviderMock.getAvailableAppointmentTimeSlots()).thenReturn(Collections.singletonList(new Date(System.currentTimeMillis())));
        thsProvidersListAdapter.onBindViewHolder(viewHolderMock, 0);

        assertThat(viewHolderMock.isAvailble.getText()).isEqualTo("Available timeslots");
        assertThat(viewHolderMock.notificationBadge.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(viewHolderMock.notificationBadge.getText()).contains("1");
    }

    private void setUpViewholder(THSProvidersListAdapter.MyViewHolder viewHolderMock, String providerAvailable) {
        when(thsProviderInfoMock.getVisibility()).thenReturn(providerAvailable);
        when(thsProviderInfoMock.getProviderInfo()).thenReturn(providerInfoMock);
        when(providerInfoMock.getFullName()).thenReturn("fullName");
        when(thsProviderInfoMock.getRating()).thenReturn(3);
        when(viewHolderMock.getAdapterPosition()).thenReturn(1);
        viewHolderMock.isAvailble = getViewHolder().isAvailble;
        viewHolderMock.selectDoctorButton = getViewHolder().selectDoctorButton;
        viewHolderMock.doctorScheduleActionBar = getViewHolder().doctorScheduleActionBar;
        viewHolderMock.doctorSelectActionBar = getViewHolder().doctorSelectActionBar;
        viewHolderMock.doctorAvailableActionBar = getViewHolder().doctorAvailableActionBar;
        viewHolderMock.relativeLayout = getViewHolder().relativeLayout;
        viewHolderMock.scheduleDoctorButton = getViewHolder().scheduleDoctorButton;
        viewHolderMock.providerPatientWaitingCount = getViewHolder().providerPatientWaitingCount;
        viewHolderMock.notificationBadge = getViewHolder().notificationBadge;
        viewHolderMock.providerRating = getViewHolder().providerRating;
        viewHolderMock.name = getViewHolder().name;
        viewHolderMock.providerImage = getViewHolder().providerImage;
        viewHolderMock.practice = getViewHolder().practice;
    }
}