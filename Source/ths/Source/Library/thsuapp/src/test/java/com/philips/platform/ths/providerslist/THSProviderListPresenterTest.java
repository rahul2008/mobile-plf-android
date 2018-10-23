package com.philips.platform.ths.providerslist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.philips.cdp.registration.User;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.appointment.THSAvailableProviderListBasedOnDateFragment;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowDatePickerDialog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProviderListPresenterTest {

    private THSProviderListPresenter thsProviderListPresenter;

    @Mock
    private THSProviderListFragmentMock thsProvidersListFragmentMock;

    @Mock
    private Consumer thsConsumerMock;

    @Mock
    private Practice practiceMock;

    @Mock
    private android.support.v4.app.FragmentActivity fragmentActivityMock;

    @Mock
    private AWSDK awsdkMock;

    @Mock
    private User userMock;

    @Mock
    private com.americanwell.sdk.manager.PracticeProvidersManager practiceProvidersManagerMock;

    @Mock
    private THSProviderInfo thsProviderInfoMock;
    private List<THSProviderInfo> providerInfoList;

    @Mock
    private android.view.View viewMock;

    @Mock
    private THSOnDemandSpeciality onDemandSpecialityMock;

    @Mock
    private TextView selectActionBarMock;
    @Mock
    private TextView scheduleActionBarMock;

    @Mock
    private com.americanwell.sdk.entity.provider.ProviderInfo providerInfoMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setConsumer(thsConsumerMock);
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiceProvidersManagerMock);
        SupportFragmentTestUtil.startFragment(thsProvidersListFragmentMock);
        when(thsProvidersListFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(thsProvidersListFragmentMock.isFragmentAttached()).thenReturn(true);
        when(thsProvidersListFragmentMock.getPractice()).thenReturn(practiceMock);
        when(thsProvidersListFragmentMock.getView()).thenReturn(viewMock);
        when(thsProvidersListFragmentMock.getContext()).thenReturn(RuntimeEnvironment.application);
        when(viewMock.findViewById(R.id.doctor_select_action_bar)).thenReturn(selectActionBarMock);
        when(viewMock.findViewById(R.id.doctor_select_action_bar)).thenReturn(scheduleActionBarMock);
        when(selectActionBarMock.getText()).thenReturn("Select provider");
        when(thsProviderInfoMock.getVisibility()).thenReturn(ProviderVisibility.ON_CALL);
        providerInfoList = new ArrayList<>();
        providerInfoList.add(thsProviderInfoMock);


        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);
        thsProviderListPresenter = new THSProviderListPresenter(thsProvidersListFragmentMock, thsProvidersListFragmentMock);
    }

    @Test
    public void fetchProvidersAndUpdateViews_WhenAsked() throws Exception {
        thsProviderListPresenter.fetchProviderList(thsConsumerMock, practiceMock);
        thsProviderListPresenter.onProvidersListReceived(providerInfoList, null);

        verify(thsProvidersListFragmentMock).updateMainView(true);
        verify(thsProvidersListFragmentMock).updateProviderAdapterList(providerInfoList);
    }

    @Test
    public void showErrorPopup_WhenFetchFailed() throws Exception {
        when(thsProvidersListFragmentMock.getString(R.string.ths_se_server_error_toast_message)).thenReturn("error");

        thsProviderListPresenter.fetchProviderList(thsConsumerMock, practiceMock);
        thsProviderListPresenter.onProvidersListFetchError(new Throwable());
        verify(thsProvidersListFragmentMock).showError("error", true, false);
    }


    @Test
    public void shouldHandleEvent_whenReceived() throws Exception {
        thsProviderListPresenter.onEvent(R.id.getStartedButton);

        assertThat(THSManager.getInstance().getVisitContext()).isEqualTo(null);

        thsProviderListPresenter.onEvent(R.id.getScheduleAppointmentButton);

        DatePickerDialog datePickerDialog = (DatePickerDialog) ShadowDatePickerDialog.getLatestDialog();
        Date dateTime = Calendar.getInstance().getTime();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.updateDate(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
        datePickerDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();

        verify(thsProvidersListFragmentMock).addFragment(isA(THSAvailableProviderListBasedOnDateFragment.class), eq(THSAvailableProviderListBasedOnDateFragment.TAG), any(Bundle.class), eq(true));
        verify(thsProvidersListFragmentMock).hideProgressBar();
    }

    @Test
    public void shouldLaunchProviderDetailsFragment_WhenAsked() throws Exception {
        List<THSOnDemandSpeciality> onDemandSpecialitiesList = new ArrayList<>();
        onDemandSpecialitiesList.add(onDemandSpecialityMock);
        thsProviderListPresenter.onResponse(onDemandSpecialitiesList, new THSSDKError());

        verify(thsProvidersListFragmentMock).addFragment(isA(THSProviderDetailsFragment.class), eq(THSProviderDetailsFragment.TAG), any(Bundle.class), eq(true));
        verify(thsProvidersListFragmentMock, times(2)).hideProgressBar();
    }

    @Test
    public void shouldShowError_WhensdkProvidesOnfailureCallback() throws Exception {
        when(thsProvidersListFragmentMock.getString(R.string.ths_se_server_error_toast_message)).thenReturn("error");

        thsProviderListPresenter.onFailure(new Throwable());

        verify(thsProvidersListFragmentMock).showError("error");

    }

    @Test
    public void shouldLaunchSymptomsFragment_WhenSelectProviderClicked() throws Exception {
        thsProviderListPresenter.onEvent(thsProviderInfoMock, R.id.provider_select, practiceMock);

        verify(thsProvidersListFragmentMock).addFragment(any(THSSymptomsFragment.class), eq(THSSymptomsFragment.TAG), any(Bundle.class), eq(true));
    }

    @Ignore
    @Test
    public void shouldLaunchAvailableProviderDetailFragment_WhenScheduleProviderWithAvailableSlotsClicked() throws Exception {
        when(thsProviderInfoMock.getProviderInfo()).thenReturn(providerInfoMock);
        thsProviderListPresenter.onEvent(thsProviderInfoMock, R.id.doctor_schedule_action_bar, practiceMock);

        DatePickerDialog datePickerDialog = (DatePickerDialog) ShadowDatePickerDialog.getLatestDialog();
        Date dateTime = Calendar.getInstance().getTime();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.updateDate(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
        datePickerDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();

        verify(thsProvidersListFragmentMock).addFragment(any(THSAvailableProviderDetailFragment.class), eq(THSAvailableProviderDetailFragment.TAG), any(Bundle.class), eq(true));
    }
}
