package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.philips.pins.shinelib.BuildConfig;
import com.philips.pins.shinelib.SHNCapabilityType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SharedPreferencesHelperTest {

    public static final String KEY = "KEY";
    public static final String TAG = "TAG";

    @Mock
    private SHNLogger.LoggerImplementation loggerMock;

    private SharedPreferencesHelper preferencesHelper;

    @Before
    public void setUp() {
        initMocks(this);

        SharedPreferences sharedPreferences = RuntimeEnvironment.application.getSharedPreferences("testPreferences", Context.MODE_PRIVATE);
        preferencesHelper = new SharedPreferencesHelper(sharedPreferences);
    }

    @Test
    public void whenBooleanIsPut_thenIsCanBeReturnedUsingGet() {
        preferencesHelper.put(KEY, true);
        boolean res = preferencesHelper.get(KEY);

        assertThat(res).isTrue();
    }

    @Test
    public void whenShortIsPut_thenIsCanBeReturnedUsingGet() {
        short value = (short) 111;
        preferencesHelper.put(KEY, value);
        short res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenIntIsPut_thenIsCanBeReturnedUsingGet() {
        int value = 222;
        preferencesHelper.put(KEY, value);
        int res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenEnumIsPut_thenIsCanBeReturnedUsingGet() {
        SHNCapabilityType value = SHNCapabilityType.DATA_STREAMING;
        preferencesHelper.put(KEY, value);
        SHNCapabilityType res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenFloatIsPut_thenIsCanBeReturnedUsingGet() {
        float value = 333f;
        preferencesHelper.put(KEY, value);
        float res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenLongIsPut_thenIsCanBeReturnedUsingGet() {
        long value = 444l;
        preferencesHelper.put(KEY, value);
        long res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenStringIsPut_thenIsCanBeReturnedUsingGet() {
        String value = "TEST";
        preferencesHelper.put(KEY, value);
        String res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenStringSetIsPut_thenIsCanBeReturnedUsingGet() {
        HashSet<String> value = new HashSet<>();
        value.add("TEST");
        preferencesHelper.put(KEY, value);
        Set<String> res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenShortValueIsAlreadyPresent_thenPutIfValueChangedWillReturnFalse() {
        short value = (short) 111;
        preferencesHelper.put(KEY, value);
        boolean valueChanged = preferencesHelper.putIfValueChanged(KEY, value);

        assertThat(valueChanged).isFalse();
    }

    @Test
    public void whenShortValueIsNotAlreadyPresent_thenPutIfValueChangedWillReturnFalse() {
        short value = (short) 111;
        preferencesHelper.put(KEY, value);
        boolean valueChanged = preferencesHelper.putIfValueChanged(KEY, value + 1);

        assertThat(valueChanged).isTrue();
    }

    @Test
    public void whenAssureCorrectPersistenceIsCalled_AndNotAlreadyPresent_thenValueIsPut() {
        preferencesHelper.assureCorrectPersistence("", KEY, true);

        boolean res = preferencesHelper.get(KEY);

        assertThat(res).isTrue();
    }

    @Test
    public void whenAssureCorrectPersistenceIsCalled_AndAlreadyPresent_thenValueIsNotPut() {
        preferencesHelper.put(KEY, true);
        preferencesHelper.assureCorrectPersistence("", KEY, false);

        boolean res = preferencesHelper.get(KEY);

        assertThat(res).isTrue();
    }

    @Test
    public void whenAssureCorrectPersistenceIsCalled_AndAlreadyPresent_thenLogAssertIsCalled() {
        SHNLogger.registerLogger(loggerMock);

        preferencesHelper.put(KEY, true);
        preferencesHelper.assureCorrectPersistence(TAG, KEY, false);

        verify(loggerMock).logLine(eq(Log.ASSERT), eq(TAG), anyString(), any(Throwable.class));

        SHNLogger.unregisterLogger(loggerMock);
    }
}