package com.philips.pins.shinelib.capabilities;

import android.content.SharedPreferences;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNUserConfiguration;
import com.philips.pins.shinelib.services.SHNServiceUserData;
import com.philips.pins.shinelib.utility.SHNDevicePreferenceWrapper;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(PowerMockRunner.class)
public class SHNCapabilityUserControlPointImplTest extends TestCase {

    private SHNCapabilityUserControlPoint shnCapabilityUserControlPoint;
    private SHNServiceUserData mockedShnServiceUserData;
    private SHNUserConfiguration mockedShnUserConfiguration;
    private SHNDevicePreferenceWrapper mockedShnDevicePreferenceWrapper;
    private SharedPreferences.Editor mockedEditor;

    ArgumentCaptor<SHNServiceUserData.SHNServiceUserDataListener> SHNServiceUserDataListenerCaptor;

    private String UDS_DATABASE_INCREMENT = "UDS_DATABASE_INCREMENT";
    private final String UDS_USER_INDEX = "UDS_USER_ID";
    private final String UDS_CONSENT_CODE = "UDS_CONSENT_CODE";

    public void setUp() throws Exception {
        super.setUp();
        mockedShnServiceUserData = mock(SHNServiceUserData.class);
        mockedShnUserConfiguration = mock(SHNUserConfiguration.class);
        mockedShnDevicePreferenceWrapper = mock(SHNDevicePreferenceWrapper.class);
        mockedEditor = mock(SharedPreferences.Editor.class);
        Mockito.when(mockedShnDevicePreferenceWrapper.edit()).thenReturn(mockedEditor);

        shnCapabilityUserControlPoint = new SHNCapabilityUserControlPointImpl(mockedShnServiceUserData, mockedShnUserConfiguration, mockedShnDevicePreferenceWrapper);
        SHNServiceUserDataListenerCaptor = ArgumentCaptor.forClass(SHNServiceUserData.SHNServiceUserDataListener.class);
        verify(mockedShnServiceUserData).setShnServiceUserDataListener(SHNServiceUserDataListenerCaptor.capture());
    }

    @Test
    public void initTest() {
        SHNCapabilityUserControlPointImpl shnCapabilityUserControlPoint = new SHNCapabilityUserControlPointImpl(mockedShnServiceUserData, mockedShnUserConfiguration, mockedShnDevicePreferenceWrapper);
    }

    @Test
    public void whenInitialedThanServiceListenerIsSet() {
        assertNotNull(SHNServiceUserDataListenerCaptor.getValue());
    }

    @Test
    public void whenGetCurrentUserIndexIsCalledThanIndexIsRedFromPreferences() {
        shnCapabilityUserControlPoint.getCurrentUserIndex();

        verify(mockedShnDevicePreferenceWrapper).getInt(UDS_USER_INDEX);
    }

    @Test
    public void whenGetCurrentConsentCodeIsCalledThanIndexIsRedFromPreferences() {
        shnCapabilityUserControlPoint.getCurrentConsentCode();

        verify(mockedShnDevicePreferenceWrapper).getInt(UDS_CONSENT_CODE);
    }

    @Test
    public void whenRegisterNewUserIsCalledThan() {
        SHNIntegerResultListener mockedShnResultListener = mock(SHNIntegerResultListener.class);
        int consentCode = 34;
        shnCapabilityUserControlPoint.registerNewUser(consentCode, mockedShnResultListener);

        verify(mockedShnServiceUserData).registerNewUser(consentCode, mockedShnResultListener);
    }

    @Test
    public void whenDeleteUserIsCalledThan() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.deleteCurrentUser(mockedShnResultListener);

        verify(mockedShnServiceUserData).deleteUser(mockedShnResultListener);
    }

    @Test
    public void whenSetCurrentUserIsCalledThanConsentRequestIsSentToServices() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        int consentCode = 34;
        int userIndex = 1;
        shnCapabilityUserControlPoint.setCurrentUser(userIndex, consentCode, mockedShnResultListener);

        verify(mockedShnServiceUserData).consentExistingUser(userIndex, consentCode, mockedShnResultListener);
    }

    @Test
    public void whenSetCurrentUserIsCalledThanDataIncrementIndexIsRequested() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        int consentCode = 34;
        int userIndex = 1;
        shnCapabilityUserControlPoint.setCurrentUser(userIndex, consentCode, mockedShnResultListener);

        verify(mockedShnServiceUserData).getDatabaseIncrement(any(SHNIntegerResultListener.class));
    }

    @Test
    public void whenSetCurrentUserIsCalledThanUserIndexAndConsentCodeAreSavedToCash() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        int consentCode = 34;
        int userIndex = 1;
        shnCapabilityUserControlPoint.setCurrentUser(userIndex, consentCode, mockedShnResultListener);

        verify(mockedShnDevicePreferenceWrapper).edit();
        verify(mockedEditor).putInt(UDS_USER_INDEX, userIndex);
        verify(mockedEditor).putInt(UDS_CONSENT_CODE, consentCode);
    }

    private void verifyUserConsentWithUserIndexAndConsentCode(int userIndex, int consentCode, int localIncrement, int receivedIncrement) {
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT)).thenReturn(localIncrement);
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(userIndex);

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.setCurrentUser(userIndex, consentCode, mockedShnResultListener);

        ArgumentCaptor<SHNIntegerResultListener> shnIntegerResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNIntegerResultListener.class);
        verify(mockedShnServiceUserData).getDatabaseIncrement(shnIntegerResultListenerArgumentCaptor.capture());

        shnIntegerResultListenerArgumentCaptor.getValue().onActionCompleted(receivedIncrement, SHNResult.SHNOk);
    }

    @Test
    public void whenDataIncrementIndexsAreDifferentThanListenerIsNotified() {
        int userIndex = 1;
        int consentCode = 34;
        int localIncrement = 9;

        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        int receivedIncrement = 10;
        verifyUserConsentWithUserIndexAndConsentCode(userIndex, consentCode, localIncrement, receivedIncrement);

        verify(mockedShnCapabilityUserControlPointListener).onMismatchedDatabaseIncrement(userIndex, localIncrement, receivedIncrement);
    }

    @Test
    public void whenDataIncrementIndexsAreTheSameThanListenerIsNotNotified() {
        int userIndex = 1;
        int consentCode = 34;
        int localIncrement = 9;

        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        verifyUserConsentWithUserIndexAndConsentCode(userIndex, consentCode, localIncrement, localIncrement);

        verify(mockedShnCapabilityUserControlPointListener, never()).onMismatchedDatabaseIncrement(anyInt(), anyInt(), anyInt());
    }

    @Test
    public void whenServicesBecomesAvailableThanIndexAndConsentCodeAreRedFromPreferences() {
        SHNServiceUserDataListenerCaptor.getValue().onServiceStateChanged(mockedShnServiceUserData, SHNService.State.Available);

        verify(mockedShnDevicePreferenceWrapper).getInt(UDS_USER_INDEX);
        verify(mockedShnDevicePreferenceWrapper).getInt(UDS_CONSENT_CODE);
    }

    @Test
    public void whenServicesBecomesAvailableThanAutoConsentIsPerformedWithStoredValues() {
        int index = 1;
        int consentCode = 336;
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(index);
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_CONSENT_CODE)).thenReturn(consentCode);

        SHNServiceUserDataListenerCaptor.getValue().onServiceStateChanged(mockedShnServiceUserData, SHNService.State.Available);

        verify(mockedShnDevicePreferenceWrapper).getInt(UDS_USER_INDEX);
        verify(mockedShnDevicePreferenceWrapper).getInt(UDS_CONSENT_CODE);

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<Integer> consentCodeCaptor = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedShnServiceUserData).consentExistingUser(indexCaptor.capture(), consentCodeCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        assertEquals(index, (int) indexCaptor.getValue());
        assertEquals(consentCode, (int) consentCodeCaptor.getValue());
    }

    private void autoConsentUserWithResult(int index, int consentCode, SHNResult result) {
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(index);
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_CONSENT_CODE)).thenReturn(consentCode);

        SHNServiceUserDataListenerCaptor.getValue().onServiceStateChanged(mockedShnServiceUserData, SHNService.State.Available);

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<Integer> consentCodeCaptor = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedShnServiceUserData).consentExistingUser(indexCaptor.capture(), consentCodeCaptor.capture(), shnResultListenerArgumentCaptor.capture());

        shnResultListenerArgumentCaptor.getValue().onActionCompleted(result);
    }

    @Test
    public void whenAutoConsentHasFailedThanListenerIsNotified() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        int index = 1;
        int consentCode = 336;
        SHNResult result = SHNResult.SHNTimeoutError;
        autoConsentUserWithResult(index, consentCode, result);
        verify(mockedShnCapabilityUserControlPointListener).onAutoConsentFailed(index, consentCode, result);
    }

    @Test
    public void whenAutoConsentHasSucceededThanListenerIsNotNotified() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        int index = 1;
        int consentCode = 336;
        SHNResult result = SHNResult.SHNOk;

        autoConsentUserWithResult(index, consentCode, result);
        verify(mockedShnCapabilityUserControlPointListener, never()).onAutoConsentFailed(index, consentCode, result);
    }

    @Test
    public void whenServiceStateChangeToUnavailableThanAutoConsentIsNotCalled() {
        SHNServiceUserDataListenerCaptor.getValue().onServiceStateChanged(mockedShnServiceUserData, SHNService.State.Unavailable);

        verify(mockedShnServiceUserData, never()).consentExistingUser(anyInt(), anyInt(), any(SHNResultListener.class));
    }

    @Test
    public void whenThereIsNoUserIndexAndConsentCodeInPreferencesThanAutoConsentIsNotCalled() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        int index = -1;
        int consentCode = -1;

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(index);
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_CONSENT_CODE)).thenReturn(consentCode);

        SHNServiceUserDataListenerCaptor.getValue().onServiceStateChanged(mockedShnServiceUserData, SHNService.State.Available);

        verify(mockedShnServiceUserData, never()).consentExistingUser(anyInt(), anyInt(), any(SHNResultListener.class));
    }

    private void verifyPushUserConfiguration(int increment) {
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT)).thenReturn(increment);

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);
    }

    @Test
    public void whenPerformingInitialConsentThanUserConfigurationIsStarted() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        verify(mockedShnServiceUserData).setAge(anyInt(), shnResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenAgesIsPushedThanValueIsRedProperly() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        Integer age = 33;
        when(mockedShnUserConfiguration.getAge()).thenReturn(age);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setAge(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(age, integerArgumentCaptor.getValue());
    }

    @Test
    public void whenRestingHeartRateIsPushedThanValueIsRedProperly() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);

        Integer beats = 120;
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(beats);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setRestingHeartRate(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(beats, integerArgumentCaptor.getValue());
    }

    @Test
    public void whenMaxHeartRateIsPushedThanValueIsRedProperly() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);

        Integer beats = 127;
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(beats);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setHeartRateMax(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(beats, integerArgumentCaptor.getValue());
    }

    @Test
    public void whenHeightIsPushedThanValueIsRedProperly() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        Integer height = 187;
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(height);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);

        verify(mockedShnServiceUserData).setHeightInMeters(floatArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(height / 100f, floatArgumentCaptor.getValue(), 0.01);
    }

    @Test
    public void whenGenderIsPushedThanValueIsRedProperly() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Male;
        when(mockedShnUserConfiguration.getSex()).thenReturn(sex);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<SHNUserConfiguration.Sex> sexArgumentCaptor = ArgumentCaptor.forClass(SHNUserConfiguration.Sex.class);

        verify(mockedShnServiceUserData).setSex(sexArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(sex, sexArgumentCaptor.getValue());
    }

    @Test
    public void whenWeightIsPushedThanValueIsRedProperly() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        Double weight = 87.1;
        when(mockedShnUserConfiguration.getWeightInKg()).thenReturn(weight);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);

        verify(mockedShnServiceUserData).setWeightInKg(floatArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(weight, floatArgumentCaptor.getValue(), 0.01);
    }

    @Test
    public void whenDateIsPushedThanValueIsRedProperly() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getWeightInKg()).thenReturn(null);

        Date date = new Date(1220L);
        when(mockedShnUserConfiguration.getDateOfBirth()).thenReturn(date);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Date> dateArgumentCaptor = ArgumentCaptor.forClass(Date.class);

        verify(mockedShnServiceUserData).setDateOfBirth(dateArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(date, dateArgumentCaptor.getValue());
    }

    @Test
    public void whenDataIsPushedDataBaseIncrementIsPushedAsWell() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getWeightInKg()).thenReturn(null);
        when(mockedShnUserConfiguration.getDateOfBirth()).thenReturn(null);
        when(mockedShnUserConfiguration.getSex()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT)).thenReturn(11);

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        verify(mockedShnServiceUserData).setDatabaseIncrement(anyInt(), any(SHNResultListener.class));
    }

    @Test
    public void whenDataIsFinishedPushingThanResultIsReported() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getWeightInKg()).thenReturn(null);
        when(mockedShnUserConfiguration.getDateOfBirth()).thenReturn(null);
        when(mockedShnUserConfiguration.getSex()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT)).thenReturn(11);

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        verify(mockedShnServiceUserData).setDatabaseIncrement(anyInt(),shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        mockedShnResultListener.onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenNotOkResultIsReportedThanListenerIsNotified() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        Integer age = 33;
        when(mockedShnUserConfiguration.getAge()).thenReturn(age);

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT)).thenReturn(11);

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setAge(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNTimeoutError);

        mockedShnResultListener.onActionCompleted(SHNResult.SHNTimeoutError);
    }

    @Test
    public void whenNotOkResultIsReportedInTheMiddleThanIndexIsNotIncremented() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        Integer beast = 133;
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(beast);

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT)).thenReturn(11);

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setRestingHeartRate(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNTimeoutError);

        verify(mockedShnServiceUserData, never()).setDatabaseIncrement(anyInt(), any(SHNResultListener.class));
        mockedShnResultListener.onActionCompleted(SHNResult.SHNTimeoutError);
    }
}