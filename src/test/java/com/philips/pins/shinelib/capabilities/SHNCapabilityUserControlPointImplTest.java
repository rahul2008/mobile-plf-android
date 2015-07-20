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
import static org.mockito.Mockito.times;
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

    private String UC_DATABASE_INCREMENT = "USER_CONFIGURATION_DATABASE_INCREMENT";

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

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).consentExistingUser(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(), any(SHNResultListener.class));
        assertEquals(userIndex, (int) integerArgumentCaptor.getAllValues().get(0));
        assertEquals(consentCode, (int) integerArgumentCaptor.getValue());
    }

    @Test
    public void whenSetCurrentUserResultIsReturnedThanListenerIsNotified() {
        int userIndex = 1;
        int consentCode = 30;
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.setCurrentUser(userIndex, consentCode, mockedShnResultListener);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedShnServiceUserData).consentExistingUser(anyInt(), anyInt(), shnResultListenerArgumentCaptor.capture());

        SHNResult result = SHNResult.SHNOk;
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(result);
        verify(mockedShnResultListener).onActionCompleted(result);
    }

    private void consentUserWithResult(int userIndex, int consentCode, SHNResult result){
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.setCurrentUser(userIndex, consentCode, mockedShnResultListener);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).consentExistingUser(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        assertEquals(userIndex, (int) integerArgumentCaptor.getAllValues().get(0));
        assertEquals(consentCode, (int) integerArgumentCaptor.getValue());

        shnResultListenerArgumentCaptor.getValue().onActionCompleted(result);
    }

    @Test
    public void whenSetCurrentUserIsOkThanUserConfigurationIncrementsAreRed() {
        int consentCode = 34;
        int userIndex = 1;
        consentUserWithResult(userIndex, consentCode, SHNResult.SHNOk);

        verify(mockedShnDevicePreferenceWrapper).getInt(UC_DATABASE_INCREMENT);
        verify(mockedShnUserConfiguration).getIncrementIndex();
    }

    @Test
    public void whenSetCurrentUserIsNotOkThanUserConfigurationIncrementsAreRed() {
        int consentCode = 34;
        int userIndex = 1;
        consentUserWithResult(userIndex, consentCode, SHNResult.SHNTimeoutError);

        verify(mockedShnDevicePreferenceWrapper, never()).getInt(UC_DATABASE_INCREMENT);
        verify(mockedShnUserConfiguration, never()).getIncrementIndex();
    }

    @Test
    public void whenUserConfigurationIncrementIndexsAreDifferentThanListenerIsNotified() {
        int consentCode = 34;
        int userIndex = 1;
        int userConfiguration = 6;

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(userIndex);
        when(mockedShnDevicePreferenceWrapper.getInt(UC_DATABASE_INCREMENT)).thenReturn(userConfiguration);
        when(mockedShnUserConfiguration.getIncrementIndex()).thenReturn(userConfiguration + 1);

        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);
        consentUserWithResult(userIndex, consentCode, SHNResult.SHNOk);

        verify(mockedShnCapabilityUserControlPointListener).onMismatchedDatabaseIncrement(userIndex);
    }

    @Test
    public void whenSetCurrentUserIsCalledThanDataIncrementIndexIsRequested() {
        int consentCode = 34;
        int userIndex = 1;
        consentUserWithResult(userIndex, consentCode, SHNResult.SHNOk);

        verify(mockedShnServiceUserData).getDatabaseIncrement(any(SHNIntegerResultListener.class));
    }

    @Test
    public void whenSetCurrentUserIsCalledThanUserIndexAndConsentCodeAreSavedToCash() {
        int consentCode = 34;
        int userIndex = 1;
        consentUserWithResult(userIndex, consentCode, SHNResult.SHNOk);

        verify(mockedShnDevicePreferenceWrapper).edit();
        verify(mockedEditor).putInt(UDS_USER_INDEX, userIndex);
        verify(mockedEditor).putInt(UDS_CONSENT_CODE, consentCode);
    }

    private void verifyUserConsentWithUserIndexAndConsentCode(int userIndex, int consentCode, int localIncrement, int receivedIncrement) {
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_DATABASE_INCREMENT)).thenReturn(localIncrement);
        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(userIndex);

        consentUserWithResult(userIndex, consentCode, SHNResult.SHNOk);

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

        verify(mockedShnCapabilityUserControlPointListener).onMismatchedDatabaseIncrement(userIndex);
    }

    @Test
    public void whenUserConfigurationIncrementIndexsAreDifferentThanDatabaseIncrementIsNotRequested() {
        int userIndex = 1;
        int consentCode = 34;
        int userConfiguration = 9;

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(userIndex);
        when(mockedShnDevicePreferenceWrapper.getInt(UC_DATABASE_INCREMENT)).thenReturn(userConfiguration);
        when(mockedShnUserConfiguration.getIncrementIndex()).thenReturn(userConfiguration + 1);

        consentUserWithResult(userIndex, consentCode, SHNResult.SHNOk);

        verify(mockedShnServiceUserData, never()).getDatabaseIncrement(any(SHNIntegerResultListener.class));
    }

    @Test
    public void whenDataIncrementIndexsAreTheSameThanListenerIsNotNotified() {
        int userIndex = 1;
        int consentCode = 34;
        int localIncrement = 9;

        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        verifyUserConsentWithUserIndexAndConsentCode(userIndex, consentCode, localIncrement, localIncrement);

        verify(mockedShnCapabilityUserControlPointListener, never()).onMismatchedDatabaseIncrement(anyInt());
    }

    @Test
    public void whenBothIncrementsAreDifferentThanListenerIsNotifiedOnce() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        int userIndex = 1;
        int consentCode = 34;
        int userConfiguration = 9;

        when(mockedShnDevicePreferenceWrapper.getInt(UDS_USER_INDEX)).thenReturn(userIndex);
        when(mockedShnDevicePreferenceWrapper.getInt(UC_DATABASE_INCREMENT)).thenReturn(userConfiguration);
        when(mockedShnUserConfiguration.getIncrementIndex()).thenReturn(userConfiguration + 1);

        consentUserWithResult(userIndex, consentCode, SHNResult.SHNOk);

        verify(mockedShnCapabilityUserControlPointListener, times(1)).onMismatchedDatabaseIncrement(userIndex);
        verify(mockedShnDevicePreferenceWrapper, never()).getInt(UDS_DATABASE_INCREMENT);
    }

    private void verifyAgeForTheUser(int userIndex, int localAge, int age){
        int consentCode = 34;
        int increment = 9;

        when(mockedShnServiceUserData.hasAgeCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getAge()).thenReturn(localAge);

        verifyUserConsentWithUserIndexAndConsentCode(userIndex, consentCode, increment, increment);

        ArgumentCaptor<SHNIntegerResultListener> shnIntegerResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNIntegerResultListener.class);
        verify(mockedShnServiceUserData).getAge(shnIntegerResultListenerArgumentCaptor.capture());

        shnIntegerResultListenerArgumentCaptor.getValue().onActionCompleted(age, SHNResult.SHNOk);
    }

    @Test
    public void whenAgeIsDifferentThanListenerIsNotified() {
        int userIndex = 1;
        int age = 40;

        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        verifyAgeForTheUser(userIndex, age, age - 1);

        verify(mockedShnCapabilityUserControlPointListener).onMismatchedDatabaseIncrement(userIndex);
    }

    @Test
    public void whenAgeIsNotDifferentThanListenerIsNotNotified() {
        int userIndex = 1;
        int age = 40;

        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        verifyAgeForTheUser(userIndex, age, age);

        verify(mockedShnCapabilityUserControlPointListener, never()).onMismatchedDatabaseIncrement(userIndex);
    }

    @Test
    public void whenDatabaseIncrementAndAgeAreDifferentThanListenerIsNotifiedOnce() {
        int userIndex = 1;
        int consentCode = 34;
        int increment = 9;

        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        verifyUserConsentWithUserIndexAndConsentCode(userIndex, consentCode, increment, increment + 1);

        verify(mockedShnCapabilityUserControlPointListener, times(1)).onMismatchedDatabaseIncrement(userIndex);
        verify(mockedShnServiceUserData, never()).getAge(any(SHNIntegerResultListener.class));
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
        when(mockedShnServiceUserData.hasAgeCharacteristic()).thenReturn(true);
        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        verify(mockedShnServiceUserData).setAge(anyInt(), shnResultListenerArgumentCaptor.capture());
    }

    @Test
    public void whenAgesIsPushedThanValueIsRedProperly() {
        Integer age = 33;
        when(mockedShnServiceUserData.hasAgeCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getAge()).thenReturn(age);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setAge(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(age, integerArgumentCaptor.getValue());
    }

    @Test
    public void whenAgeIsNotSupportedThanRestingHeartRateIsPushed() {
        when(mockedShnServiceUserData.hasAgeCharacteristic()).thenReturn(false);

        verifyPushUserConfiguration(10);

        verify(mockedShnServiceUserData).hasRestingHeartRateCharacteristic();
    }

    @Test
    public void whenRestingHeartRateIsPushedThanValueIsRedProperly() {
        // skip this fields
        when(mockedShnServiceUserData.hasAgeCharacteristic()).thenReturn(false);

        Integer beats = 120;
        when(mockedShnServiceUserData.hasRestingHeartRateCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(beats);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setRestingHeartRate(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(beats, integerArgumentCaptor.getValue());
    }

    @Test
    public void whenRestingHeartRatIsNotSupportedThanRestingHeartRateIsPushed() {
        when(mockedShnServiceUserData.hasRestingHeartRateCharacteristic()).thenReturn(false);

        verifyPushUserConfiguration(10);

        verify(mockedShnServiceUserData).hasHeartRateMaxCharacteristic();
    }

    @Test
    public void whenMaxHeartRateIsPushedThanValueIsRedProperly() {
        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);

        Integer beats = 127;
        when(mockedShnServiceUserData.hasHeartRateMaxCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(beats);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setHeartRateMax(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(beats, integerArgumentCaptor.getValue());
    }

    @Test
    public void whenMaxHeartRateIsNotSupportedThanHeightIsChecked() {
        when(mockedShnServiceUserData.hasRestingHeartRateCharacteristic()).thenReturn(false);

        verifyPushUserConfiguration(10);

        verify(mockedShnServiceUserData).hasHeightCharacteristic();
    }

    @Test
    public void whenHeightIsPushedThanValueIsRedProperly() {
        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        Integer height = 187;
        when(mockedShnServiceUserData.hasHeightCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(height);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);

        verify(mockedShnServiceUserData).setHeightInMeters(floatArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(height / 100f, floatArgumentCaptor.getValue(), 0.01);
    }

    @Test
    public void whenHeightIsNotSupportedThanGenderIsChecked() {
        SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Male;
        when(mockedShnServiceUserData.hasGenderCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getSex()).thenReturn(sex);

        when(mockedShnServiceUserData.hasHeightCharacteristic()).thenReturn(false);

        verifyPushUserConfiguration(10);

        verify(mockedShnServiceUserData).hasGenderCharacteristic();
    }

    @Test
    public void whenGenderIsPushedThanValueIsRedProperly() {
        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Male;
        when(mockedShnServiceUserData.hasGenderCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getSex()).thenReturn(sex);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<SHNUserConfiguration.Sex> sexArgumentCaptor = ArgumentCaptor.forClass(SHNUserConfiguration.Sex.class);

        verify(mockedShnServiceUserData).setSex(sexArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(sex, sexArgumentCaptor.getValue());
    }

    @Test
    public void whenGenderIsNotSupportedThanHeightIsChecked() {
        when(mockedShnServiceUserData.hasGenderCharacteristic()).thenReturn(false);

        verifyPushUserConfiguration(10);

        verify(mockedShnServiceUserData).hasWeightCharacteristic();
    }

    @Test
    public void whenWeightIsPushedThanValueIsRedProperly() {

        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);

        Double weight = 87.1;
        when(mockedShnServiceUserData.hasWeightCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getWeightInKg()).thenReturn(weight);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);

        verify(mockedShnServiceUserData).setWeightInKg(floatArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(weight, floatArgumentCaptor.getValue(), 0.01);
    }

    @Test
    public void whenWeightIsNotSupportedThanDateOfBirthIsChecked() {
        when(mockedShnServiceUserData.hasWeightCharacteristic()).thenReturn(false);

        Date date = new Date(1220L);
        when(mockedShnServiceUserData.hasDateOfBirthCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getDateOfBirth()).thenReturn(date);

        verifyPushUserConfiguration(10);

        verify(mockedShnServiceUserData).hasDateOfBirthCharacteristic();
    }

    @Test
    public void whenDateIsPushedThanValueIsRedProperly() {
        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getWeightInKg()).thenReturn(null);

        Date date = new Date(1220L);
        when(mockedShnServiceUserData.hasDateOfBirthCharacteristic()).thenReturn(true);
        when(mockedShnUserConfiguration.getDateOfBirth()).thenReturn(date);

        verifyPushUserConfiguration(10);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Date> dateArgumentCaptor = ArgumentCaptor.forClass(Date.class);

        verify(mockedShnServiceUserData).setDateOfBirth(dateArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);
        assertEquals(date, dateArgumentCaptor.getValue());
    }

    @Test
    public void whenDateIsNotSupportedThanDatabaseIncrementIsPushed() {
        when(mockedShnServiceUserData.hasDateOfBirthCharacteristic()).thenReturn(false);

        verifyPushUserConfiguration(10);

        verify(mockedShnServiceUserData).getDatabaseIncrement(any(SHNIntegerResultListener.class));
    }

    private void setUpEmptyConfiguration(){
        // skip this fields
        when(mockedShnUserConfiguration.getAge()).thenReturn(null);
        when(mockedShnUserConfiguration.getRestingHeartRate()).thenReturn(null);
        when(mockedShnUserConfiguration.getHeightInCm()).thenReturn(null);
        when(mockedShnUserConfiguration.getWeightInKg()).thenReturn(null);
        when(mockedShnUserConfiguration.getDateOfBirth()).thenReturn(null);
        when(mockedShnUserConfiguration.getSex()).thenReturn(null);
        when(mockedShnUserConfiguration.getMaxHeartRate()).thenReturn(null);
    }

    @Test
    public void whenDataIsPushedDataBaseIncrementIsPushedAsWell() {
        setUpEmptyConfiguration();

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        verify(mockedShnServiceUserData).getDatabaseIncrement(any(SHNIntegerResultListener.class));
        verify(mockedShnResultListener, never()).onActionCompleted(any(SHNResult.class)); // getIncrement should not finish the command
    }

    @Test
    public void whenDataIsFinishedPushingThanIncrementIsIncreasedBy1AndResultIsReported() {
        setUpEmptyConfiguration();

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        int receivedIncrement = 10;
        ArgumentCaptor<SHNIntegerResultListener> shnIntegerResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNIntegerResultListener.class);
        verify(mockedShnServiceUserData).getDatabaseIncrement(shnIntegerResultListenerArgumentCaptor.capture());
        shnIntegerResultListenerArgumentCaptor.getValue().onActionCompleted(receivedIncrement, SHNResult.SHNOk);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockedShnServiceUserData).setDatabaseIncrement(integerArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        assertEquals(receivedIncrement + 1, (int) integerArgumentCaptor.getValue());
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenGetDataBaseIncrementHasFailedThanSetIncrementIsNotCalledAndResultIsReported() {
        setUpEmptyConfiguration();

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        int receivedIncrement = 10;
        ArgumentCaptor<SHNIntegerResultListener> shnIntegerResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNIntegerResultListener.class);
        verify(mockedShnServiceUserData).getDatabaseIncrement(shnIntegerResultListenerArgumentCaptor.capture());
        shnIntegerResultListenerArgumentCaptor.getValue().onActionCompleted(receivedIncrement, SHNResult.SHNTimeoutError);

        verify(mockedShnServiceUserData, never()).setDatabaseIncrement(anyInt(), any(SHNResultListener.class));

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNTimeoutError);
    }

    @Test
    public void whenIncrementIsIncrementedSuccessfullyThanValuesAreSavedToThePreferences() {
        int increment = 12;
        when(mockedShnUserConfiguration.getIncrementIndex()).thenReturn(12);
        setUpEmptyConfiguration();

        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnCapabilityUserControlPoint.pushUserConfiguration(mockedShnResultListener);

        int receivedIncrement = 10;
        ArgumentCaptor<SHNIntegerResultListener> shnIntegerResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNIntegerResultListener.class);
        verify(mockedShnServiceUserData).getDatabaseIncrement(shnIntegerResultListenerArgumentCaptor.capture());
        shnIntegerResultListenerArgumentCaptor.getValue().onActionCompleted(receivedIncrement, SHNResult.SHNOk);

        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedShnServiceUserData).setDatabaseIncrement(anyInt(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        verify(mockedShnDevicePreferenceWrapper).edit();
        verify(mockedEditor).putInt(UDS_DATABASE_INCREMENT, receivedIncrement+1);
        verify(mockedEditor).putInt(UC_DATABASE_INCREMENT, increment);
    }

    @Test
    public void whenPushConfigurationIsCalledAndResultINotOkThanListenerIsNotified() {
        SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener mockedShnCapabilityUserControlPointListener = mock(SHNCapabilityUserControlPoint.SHNCapabilityUserControlPointListener.class);
        shnCapabilityUserControlPoint.setSHNCapabilityUserControlPointListener(mockedShnCapabilityUserControlPointListener);

        Integer age = 33;
        when(mockedShnServiceUserData.hasAgeCharacteristic()).thenReturn(true);
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
        when(mockedShnServiceUserData.hasAgeCharacteristic()).thenReturn(false);

        when(mockedShnServiceUserData.hasRestingHeartRateCharacteristic()).thenReturn(true);
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