package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.datatypes.SHNSportTypeForThresholds;
import com.philips.pins.shinelib.datatypes.SHNUserConfiguration;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.ScalarConverters;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(PowerMockRunner.class)
public class SHNServiceUserDataCharacteristicsTest extends TestCase {

    private SHNFactory mockedShnFactory;
    private SHNService mockedShnService;
    private SHNCharacteristic mockedShnCharacteristic;
    private SHNServiceUserData shnServiceUserData;
    public static final float HEIGHT_RESOLUTION = 0.01f;
    public static final float CIRCUMFERENCE_RESOLUTION = 0.01f;
    public static final float WEIGHT_RESOLUTION = 0.005f;

    @Before
    public void setUp() {
        mockedShnFactory = Mockito.mock(SHNFactory.class);
        mockedShnService = Mockito.mock(SHNService.class);

        when(mockedShnFactory.createNewSHNService(any(UUID.class), any(Set.class), any(Set.class))).thenReturn(mockedShnService);

        mockedShnCharacteristic = Mockito.mock(SHNCharacteristic.class);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.USER_INDEX_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.FIRST_NAME_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.LAST_NAME_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.AGE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.DATE_OF_BIRTH_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.EMAIL_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.GENDER_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.WEIGHT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.HEIGHT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.VO2MAX_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.MAXIMUM_RECOMMENDED_HEART_RATE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.HEART_RATE_MAX_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.RESTING_HEART_RATE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.ANAEROBIC_THRESHOLD_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.AEROBIC_THRESHOLD_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.SPORT_TYPE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.DATE_OF_THRESHOLD_ASSESSMENT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.WAIST_CIRCUMFERENCE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.HIP_CIRCUMFERENCE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.FAT_BURN_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.FAT_BURN_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.AEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.AEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.ANAEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.ANAEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.FIVE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.THREE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.TWO_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.LANGUAGE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        shnServiceUserData = new SHNServiceUserData(mockedShnFactory);
    }

    private void assertGetStringCharacteristic(UUID uuid, SHNStringResultListener mockedShnStringResultListener, String name) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        shnCommandResultReporter.reportResult(SHNResult.SHNOk, name.getBytes());

        verify(mockedShnStringResultListener).onActionCompleted(name, SHNResult.SHNOk);
    }

    private void assertGetUInt8Characteristic(UUID uuid, SHNIntegerResultListener mockedShnIntegerResultListener, int value) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(1).put((byte) value).array();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        verify(mockedShnIntegerResultListener).onActionCompleted(value, SHNResult.SHNOk);
    }

    private void assertGetUInt16Characteristic(UUID uuid, SHNObjectResultListener mockedShnObjectResultListener, float value, float resolution) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) (value / resolution)).array();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);

        verify(mockedShnObjectResultListener).onActionCompleted(floatArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        assertEquals(value, floatArgumentCaptor.getValue(), 0.01);
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    private void assertGetUInt32Characteristic(UUID uuid, SHNIntegerResultListener mockedShnIntegerResultListener, int value) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        verify(mockedShnIntegerResultListener).onActionCompleted(value, SHNResult.SHNOk);
    }

    @Test
    public void whenGetFirstNameIsCalledThenItIsReturned() {
        SHNStringResultListener mockedShnStringResultListener = mock(SHNStringResultListener.class);
        shnServiceUserData.getFirstName(mockedShnStringResultListener);

        assertGetStringCharacteristic(SHNServiceUserData.FIRST_NAME_CHARACTERISTIC_UUID, mockedShnStringResultListener, "Jack");
    }

    @Test
    public void whenGetLastNameIsCalledThenItIsReturned() {
        SHNStringResultListener mockedShnStringResultListener = mock(SHNStringResultListener.class);
        shnServiceUserData.getLastName(mockedShnStringResultListener);

        assertGetStringCharacteristic(SHNServiceUserData.LAST_NAME_CHARACTERISTIC_UUID, mockedShnStringResultListener, "Jones");
    }

    @Test
    public void whenGetEmailIsCalledThenItIsReturned() {
        SHNStringResultListener mockedShnStringResultListener = mock(SHNStringResultListener.class);
        shnServiceUserData.getEmail(mockedShnStringResultListener);

        assertGetStringCharacteristic(SHNServiceUserData.EMAIL_CHARACTERISTIC_UUID, mockedShnStringResultListener, "jack@jones.com");
    }

    @Test
    public void whenGetAgeIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);

        int age = 89;
        shnServiceUserData.getAge(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.AGE_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, age);
    }

    private void assertGetDateCharacteristic(UUID uuid, byte[] date) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());

        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byte[] result = byteBuffer.put(date).array();

        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);
    }

    @Test
    public void whenGetDateOfBirthIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);

        byte[] birthDate = new byte[]{
                (byte) 0xA8, 0x07,  // year 1960
                6,                  // month june = 6
                8,                  // day 8th
        };

        shnServiceUserData.getDateOfBirth(mockedShnObjectResultListener);

        assertGetDateCharacteristic(SHNServiceUserData.DATE_OF_BIRTH_CHARACTERISTIC_UUID, birthDate);
        Date date = new Date(1960 - 1900, 6 - 1, 8);
        verify(mockedShnObjectResultListener).onActionCompleted(date, SHNResult.SHNOk);
    }

    @Test
    public void whenGetGenderIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);

        int sex = 1;
        shnServiceUserData.getSex(mockedShnObjectResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.GENDER_CHARACTERISTIC_UUID);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put((byte) sex).array();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        verify(mockedShnObjectResultListener).onActionCompleted(SHNUserConfiguration.Sex.Female, SHNResult.SHNOk);
    }

    @Test
    public void whenGetWeightIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        float value = 53.6f;
        float resolution = 0.005f;
        shnServiceUserData.getWeightInKg(mockedShnObjectResultListener);

        assertGetUInt16Characteristic(SHNServiceUserData.WEIGHT_CHARACTERISTIC_UUID, mockedShnObjectResultListener, value, resolution);
    }

    @Test
    public void whenGetHeightIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        float value = 1.67f;
        float resolution = 0.01f;
        shnServiceUserData.getHeightInMeters(mockedShnObjectResultListener);

        assertGetUInt16Characteristic(SHNServiceUserData.HEIGHT_CHARACTERISTIC_UUID, mockedShnObjectResultListener, value, resolution);
    }

    @Test
    public void whenGetVO2MaxIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 130;
        shnServiceUserData.getVO2Max(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.VO2MAX_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetHeartRateMaxIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 125;
        shnServiceUserData.getHeartRateMax(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.HEART_RATE_MAX_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetRestingHeartRateIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 88;
        shnServiceUserData.getRestingHeartRate(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.RESTING_HEART_RATE_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetMaxRecommendedHeartRateIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 88;
        shnServiceUserData.getMaximumRecommendedHeartRate(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.MAXIMUM_RECOMMENDED_HEART_RATE_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetAnaerobicThresholdIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 105;
        shnServiceUserData.getAnaerobicThreshold(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.ANAEROBIC_THRESHOLD_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetAerobicThresholdIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 100;
        shnServiceUserData.getAerobicThreshold(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.AEROBIC_THRESHOLD_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetSportTypeIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        SHNSportTypeForThresholds thresholds = SHNSportTypeForThresholds.Cycling;
        shnServiceUserData.getSportType(mockedShnObjectResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.SPORT_TYPE_CHARACTERISTIC_UUID);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(1).put((byte) 2).array(); // cycling == 2
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        verify(mockedShnObjectResultListener).onActionCompleted(thresholds, SHNResult.SHNOk);
    }

    @Test
    public void whenGetDateOfAssessmentIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);

        byte[] birthDate = new byte[]{
                (byte) 0xA9, 0x07,  // year 1961
                2,                  // month february =
                16,                 // day 16th
        };

        shnServiceUserData.getDateOfThresholdAssessment(mockedShnObjectResultListener);

        assertGetDateCharacteristic(SHNServiceUserData.DATE_OF_THRESHOLD_ASSESSMENT_CHARACTERISTIC_UUID, birthDate);
        Date date = new Date(1961 - 1900, 2 - 1, 16);
        verify(mockedShnObjectResultListener).onActionCompleted(date, SHNResult.SHNOk);
    }

    @Test
    public void whenGetWaistCircumferenceIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        float value = 2.41f;
        float resolution = 0.01f;
        shnServiceUserData.getWaistCircumference(mockedShnObjectResultListener);

        assertGetUInt16Characteristic(SHNServiceUserData.WAIST_CIRCUMFERENCE_CHARACTERISTIC_UUID, mockedShnObjectResultListener, value, resolution);
    }

    @Test
    public void whenGetHipCircumferenceIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        float value = 1.41f;
        float resolution = 0.01f;
        shnServiceUserData.getHipCircumference(mockedShnObjectResultListener);

        assertGetUInt16Characteristic(SHNServiceUserData.HIP_CIRCUMFERENCE_CHARACTERISTIC_UUID, mockedShnObjectResultListener, value, resolution);
    }

    @Test
    public void whenGetFatBurnHeartRateLowerLimitThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 101;
        shnServiceUserData.getFatBurnHeartRateLowerLimit(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.FAT_BURN_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetFatBurnHeartRateUpperLimitIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 102;
        shnServiceUserData.getFatBurnHeartRateUpperLimit(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.FAT_BURN_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetAerobicHeartRateLowerLimitIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 103;
        shnServiceUserData.getAerobicHeartRateLowerLimit(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.AEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetAerobicHeartRateUpperLimitIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 104;
        shnServiceUserData.getAerobicHeartRateUpperLimit(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.AEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetAnaerobicHeartRateLowerLimitIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 105;
        shnServiceUserData.getAnaerobicHeartRateLowerLimit(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.ANAEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetAnaerobicHeartRateUpperLimitIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 106;
        shnServiceUserData.getAnaerobicHeartRateUpperLimit(mockedShnIntegerResultListener);

        assertGetUInt8Characteristic(SHNServiceUserData.ANAEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    @Test
    public void whenGetFiveZoneHeartRateLimitsLimitIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        byte[] fiveLimits = {(byte) 106, (byte) 108, (byte) 107, (byte) 109};
        shnServiceUserData.getFiveZoneHeartRateLimits(mockedShnObjectResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.FIVE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(4).put(fiveLimits).array();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        ArgumentCaptor<SHNServiceUserData.FiveZoneHeartRateLimits> fiveZoneHeartRateLimitsArgumentCaptor = ArgumentCaptor.forClass(SHNServiceUserData.FiveZoneHeartRateLimits.class);
        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        verify(mockedShnObjectResultListener).onActionCompleted(fiveZoneHeartRateLimitsArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(106, fiveZoneHeartRateLimitsArgumentCaptor.getValue().getVeryLightToLightLimit());
        assertEquals(108, fiveZoneHeartRateLimitsArgumentCaptor.getValue().getLightToModerateLimit());
        assertEquals(107, fiveZoneHeartRateLimitsArgumentCaptor.getValue().getModerateToHardLimit());
        assertEquals(109, fiveZoneHeartRateLimitsArgumentCaptor.getValue().getHardToMaximumLimit());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenGetThreeZoneHeartRateLimitsLimitIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        byte[] threeLimits = {(byte) 106, (byte) 108};
        shnServiceUserData.getThreeZoneHeartRateLimits(mockedShnObjectResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.THREE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(2).put(threeLimits).array();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        ArgumentCaptor<SHNServiceUserData.ThreeZoneHeartRateLimits> threeZoneHeartRateLimitsArgumentCaptor = ArgumentCaptor.forClass(SHNServiceUserData.ThreeZoneHeartRateLimits.class);
        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        verify(mockedShnObjectResultListener).onActionCompleted(threeZoneHeartRateLimitsArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(106, threeZoneHeartRateLimitsArgumentCaptor.getValue().getLightToModerateLimit());
        assertEquals(108, threeZoneHeartRateLimitsArgumentCaptor.getValue().getModerateToHardLimit());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenGetTwoZoneHeartRateLimitsLimitIsCalledThenItIsReturned() {
        SHNObjectResultListener mockedShnObjectResultListener = mock(SHNObjectResultListener.class);
        byte[] threeLimits = {(byte) 106};
        shnServiceUserData.getTwoZoneHeartRateLimits(mockedShnObjectResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.TWO_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        byte[] result = ByteBuffer.allocate(1).put(threeLimits).array();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, result);

        ArgumentCaptor<SHNServiceUserData.TwoZoneHeartRateLimits> twoZoneHeartRateLimitsArgumentCaptor = ArgumentCaptor.forClass(SHNServiceUserData.TwoZoneHeartRateLimits.class);
        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        verify(mockedShnObjectResultListener).onActionCompleted(twoZoneHeartRateLimitsArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(106, twoZoneHeartRateLimitsArgumentCaptor.getValue().getFatBurnFitnessLimit());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenGetLanguageIsCalledThenItIsReturned() {
        SHNStringResultListener mockedShnStringResultListener = mock(SHNStringResultListener.class);
        shnServiceUserData.getLanguage(mockedShnStringResultListener);

        assertGetStringCharacteristic(SHNServiceUserData.LANGUAGE_CHARACTERISTIC_UUID, mockedShnStringResultListener, "zh"); // Chinese
    }

    @Test
    public void whenGetDatabaseIncrementIsCalledThenItIsReturned() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        int value = 125;
        shnServiceUserData.getDatabaseIncrement(mockedShnIntegerResultListener);

        assertGetUInt32Characteristic(SHNServiceUserData.DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID, mockedShnIntegerResultListener, value);
    }

    // setters tests
    private void assertSetStringCharacteristic(UUID uuid, String name, SHNResult result) {
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());
        String resultString = new String(byteArrayArgumentCaptor.getValue(), StandardCharsets.UTF_8);
        assertEquals(name, resultString);

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    private void assertSetUInt8Characteristic(UUID uuid, int request, SHNResult result) {
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayArgumentCaptor.getValue());
        int response = ScalarConverters.ubyteToInt(byteBuffer.get());

        assertEquals(request, response);

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    private void assertSetUInt16Characteristic(UUID uuid, float request, SHNResult result, float resolution) {
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayArgumentCaptor.getValue());
        int response = ScalarConverters.ushortToInt(byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getShort());

        int rawValue = Math.round(request / resolution);
        assertEquals(rawValue, response);

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    @Test
    public void whenSetFirstNameIsCalledThenProperCharacteristicIsSet() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        String name = "Jack";
        shnServiceUserData.setFirstName(name, mockedShnResultListener);

        assertSetStringCharacteristic(SHNServiceUserData.FIRST_NAME_CHARACTERISTIC_UUID, name, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetLastNameIsCalledThenProperCharacteristicIsSet() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        String name = "Jones";
        shnServiceUserData.setLastName(name, mockedShnResultListener);

        assertSetStringCharacteristic(SHNServiceUserData.LAST_NAME_CHARACTERISTIC_UUID, name, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetEmailIsCalledThenProperCharacteristicIsSet() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        String name = "jack@jones.com";
        shnServiceUserData.setEmail(name, mockedShnResultListener);

        assertSetStringCharacteristic(SHNServiceUserData.EMAIL_CHARACTERISTIC_UUID, name, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetAgeIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int age = 89;
        shnServiceUserData.setAge(age, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.AGE_CHARACTERISTIC_UUID, age, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    private void assetSetDateCharacteristic(UUID uuid, byte[] date) {
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        for (int i = 0; i < date.length; i++) {
            assertEquals("Mismatch at byte " + i, date[i], byteArrayArgumentCaptor.getValue()[i]);
        }

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, null);
    }

    @Test
    public void whenSetDateOfBirthIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        Date date = new Date(100L);
        shnServiceUserData.setDateOfBirth(date, mockedShnResultListener);

        byte[] birthDate = new byte[]{
                (byte) 0xB2, 0x07,  // year 1970
                1,                  // month june = 6
                1,                  // day 8th
        };

        assetSetDateCharacteristic(SHNServiceUserData.DATE_OF_BIRTH_CHARACTERISTIC_UUID, birthDate);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetWeightIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        float weight = 89.6f;
        shnServiceUserData.setWeightInKg(weight, mockedShnResultListener);

        assertSetUInt16Characteristic(SHNServiceUserData.WEIGHT_CHARACTERISTIC_UUID, weight, SHNResult.SHNOk, WEIGHT_RESOLUTION);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetHeightIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        float height = 1.45f;
        shnServiceUserData.setHeightInMeters(height, mockedShnResultListener);

        assertSetUInt16Characteristic(SHNServiceUserData.HEIGHT_CHARACTERISTIC_UUID, height, SHNResult.SHNOk, HEIGHT_RESOLUTION);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenGenderSetToMaleThenProperValuerIsWritten() {
        SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Female;
        assertSetSexIsCalledWithSex(1, sex);
    }

    @Test
    public void whenGenderSetToFemaleThenProperValuerIsWritten() {
        SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Male;
        assertSetSexIsCalledWithSex(0, sex);
    }

    @Test
    public void whenGenderSetToUnspecifiedThenProperValuerIsWritten() {
        SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Unspecified;
        assertSetSexIsCalledWithSex(2, sex);
    }

    private void assertSetSexIsCalledWithSex(int value, SHNUserConfiguration.Sex sex) {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);
        shnServiceUserData.setSex(sex, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.GENDER_CHARACTERISTIC_UUID, value, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetVO2IsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int mlPerKgPerMinute = 200;
        shnServiceUserData.setVO2Max(mlPerKgPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.VO2MAX_CHARACTERISTIC_UUID, mlPerKgPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetHeartRateIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 140;
        shnServiceUserData.setHeartRateMax(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.HEART_RATE_MAX_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetRestingHeartRateIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 78;
        shnServiceUserData.setRestingHeartRate(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.RESTING_HEART_RATE_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetMaximumRecommendedHeartRateIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 120;
        shnServiceUserData.setMaximumRecommendedHeartRate(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.MAXIMUM_RECOMMENDED_HEART_RATE_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetAerobicThresholdIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 130;
        shnServiceUserData.setAerobicThreshold(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.AEROBIC_THRESHOLD_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetAnaerobicThresholdIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 130;
        shnServiceUserData.setAnaerobicThreshold(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.ANAEROBIC_THRESHOLD_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetDateOfAssessmentIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        Date date = new Date(12445821000L); // May 25 1970
        shnServiceUserData.setDateOfThresholdAssessment(date, mockedShnResultListener);

        byte[] birthDate = new byte[]{
                (byte) 0xB2, 0x07,  // year 1970
                5,                  // month may = 5
                25,                 // day 25th
        };

        assetSetDateCharacteristic(SHNServiceUserData.DATE_OF_THRESHOLD_ASSESSMENT_CHARACTERISTIC_UUID, birthDate);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetWaistCircumferenceIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        float meters = 1.45f;
        shnServiceUserData.setWaistCircumference(meters, mockedShnResultListener);

        assertSetUInt16Characteristic(SHNServiceUserData.WAIST_CIRCUMFERENCE_CHARACTERISTIC_UUID, meters, SHNResult.SHNOk, CIRCUMFERENCE_RESOLUTION);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetHipCircumferenceIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        float meters = 1.45f;
        shnServiceUserData.setHipCircumference(meters, mockedShnResultListener);

        assertSetUInt16Characteristic(SHNServiceUserData.HIP_CIRCUMFERENCE_CHARACTERISTIC_UUID, meters, SHNResult.SHNOk, CIRCUMFERENCE_RESOLUTION);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetFatBurnHeartRateLowerLimitIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 130;
        shnServiceUserData.setFatBurnHeartRateLowerLimit(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.FAT_BURN_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetFatBurnHeartRateUpperLimitIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 131;
        shnServiceUserData.setFatBurnHeartRateUpperLimit(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.FAT_BURN_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetAerobicHeartRateLowerLimitIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 132;
        shnServiceUserData.setAerobicHeartRateLowerLimit(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.AEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetAerobicHeartRateUpperLimitIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 133;
        shnServiceUserData.setAerobicHeartRateUpperLimit(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.AEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetAnaerobicHeartRateLowerLimitIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 134;
        shnServiceUserData.setAnaerobicHeartRateLowerLimit(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.ANAEROBIC_HEART_RATE_LOWER_LIMIT_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetAnaerobicHeartRateUpperLimitIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 135;
        shnServiceUserData.setAnaerobicHeartRateUpperLimit(beatsPerMinute, mockedShnResultListener);

        assertSetUInt8Characteristic(SHNServiceUserData.ANAEROBIC_HEART_RATE_UPPER_LIMIT_CHARACTERISTIC_UUID, beatsPerMinute, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    private void verifyForCharacteristic(UUID uuid, byte[] request) {
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(uuid);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        for (int i = 0; i < request.length; i++) {
            assertEquals("Mismatch at byte " + i, request[i], byteArrayArgumentCaptor.getValue()[i]);
        }

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, null);
    }

    @Test
    public void whenSetFiveZoneHeartRateLimitsIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        byte[] request = {10, 20, 30, 40};
        SHNServiceUserData.FiveZoneHeartRateLimits limits = new SHNServiceUserData.FiveZoneHeartRateLimits(10, 20, 30, 40);
        shnServiceUserData.setFiveZoneHeartRateLimits(limits, mockedShnResultListener);

        verifyForCharacteristic(SHNServiceUserData.FIVE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, request);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetThreeZoneHeartRateLimitsIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        byte[] request = {10, 20};
        SHNServiceUserData.ThreeZoneHeartRateLimits limits = new SHNServiceUserData.ThreeZoneHeartRateLimits(10, 20);
        shnServiceUserData.setThreeZoneHeartRateLimits(limits, mockedShnResultListener);

        verifyForCharacteristic(SHNServiceUserData.THREE_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, request);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetTwoZoneHeartRateLimitsIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        byte[] request = {101};
        SHNServiceUserData.TwoZoneHeartRateLimits limits = new SHNServiceUserData.TwoZoneHeartRateLimits(101);
        shnServiceUserData.setTwoZoneHeartRateLimits(limits, mockedShnResultListener);

        verifyForCharacteristic(SHNServiceUserData.TWO_ZONE_HEART_LIMITS_CHARACTERISTIC_UUID, request);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenSetLanguageCalledThenProperCharacteristicIsSet() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        String name = "zh";
        shnServiceUserData.setLanguage(name, mockedShnResultListener);

        assertSetStringCharacteristic(SHNServiceUserData.LANGUAGE_CHARACTERISTIC_UUID, name, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteByteCharacteristicWithOutOfBoundValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int beatsPerMinute = 256;
        shnServiceUserData.setFatBurnHeartRateLowerLimit(beatsPerMinute, mockedShnResultListener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteShortCharacteristicWithOutOfBoundValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        float height = 250000;
        shnServiceUserData.setHeightInMeters(height, mockedShnResultListener);
    }

    @Test
    public void whenGetIndexIsCalledThenProperCharacteristicIsUsed() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        shnServiceUserData.getUserIndex(mockedShnIntegerResultListener);

        byte[] userId = {0x04};

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.USER_INDEX_CHARACTERISTIC_UUID);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        shnCommandResultReporter.reportResult(SHNResult.SHNOk, userId);

        verify(mockedShnIntegerResultListener).onActionCompleted(userId[0], SHNResult.SHNOk);
    }

    @Test
    public void whenUserIdIsMaxThenItIsParsedProperly() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        shnServiceUserData.getUserIndex(mockedShnIntegerResultListener);

        byte[] index = {(byte) 0xFF};

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        shnCommandResultReporter.reportResult(SHNResult.SHNOk, index);

        verify(mockedShnIntegerResultListener).onActionCompleted(255, SHNResult.SHNOk);
    }

    @Test
    public void whenSetDataBaseChangeIncrementIsCalledThenProperCharacteristicIsSetWithValue() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int increment = 140;
        shnServiceUserData.setDatabaseIncrement(increment, mockedShnResultListener);

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);

        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayArgumentCaptor.getValue()).order(ByteOrder.LITTLE_ENDIAN);
        long response = ScalarConverters.uintToLong(byteBuffer.getInt());

        assertEquals(increment, response);

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, null);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }
}