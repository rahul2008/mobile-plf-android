package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.philips.pins.shinelib.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SharedPreferencesHelperTest {

    public static final String BOOLEAN_KEY = "BOOLEAN_KEY";
    public static final String SHORT_KEY = "SHORT_KEY";
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
        preferencesHelper.put(BOOLEAN_KEY, true);
        boolean res = preferencesHelper.get(BOOLEAN_KEY);

        assertThat(res).isTrue();
    }

    @Test
    public void whenShortIsPut_thenIsCanBeReturnedUsingGet() {
        short value = (short) 111;
        preferencesHelper.put(SHORT_KEY, value);
        short res = preferencesHelper.get(SHORT_KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenShortValueIsAlreadyPresent_thenPutIfValueChangedWillReturnFalse() {
        short value = (short) 111;
        preferencesHelper.put(SHORT_KEY, value);
        boolean valueChanged = preferencesHelper.putIfValueChanged(SHORT_KEY, value);

        assertThat(valueChanged).isFalse();
    }

    @Test
    public void whenShortValueIsNotAlreadyPresent_thenPutIfValueChangedWillReturnFalse() {
        short value = (short) 111;
        preferencesHelper.put(SHORT_KEY, value);
        boolean valueChanged = preferencesHelper.putIfValueChanged(SHORT_KEY, value + 1);

        assertThat(valueChanged).isTrue();
    }

    @Test
    public void whenAssureCorrectPersistenceIsCalled_AndNotAlreadyPresent_thenValueIsPut() {
        preferencesHelper.assureCorrectPersistence("", BOOLEAN_KEY, true);

        boolean res = preferencesHelper.get(BOOLEAN_KEY);

        assertThat(res).isTrue();
    }

    @Test
    public void whenAssureCorrectPersistenceIsCalled_AndAlreadyPresent_thenValueIsNotPut() {
        preferencesHelper.put(BOOLEAN_KEY, true);
        preferencesHelper.assureCorrectPersistence("", BOOLEAN_KEY, false);

        boolean res = preferencesHelper.get(BOOLEAN_KEY);

        assertThat(res).isTrue();
    }

    @Test
    public void whenAssureCorrectPersistenceIsCalled_AndAlreadyPresent_thenLogAssertIsCalled() {
        SHNLogger.registerLogger(loggerMock);

        preferencesHelper.put(BOOLEAN_KEY, true);
        preferencesHelper.assureCorrectPersistence(TAG, BOOLEAN_KEY, false);

        verify(loggerMock).logLine(eq(Log.ASSERT), eq(TAG), anyString(), any(Throwable.class));

        SHNLogger.unregisterLogger(loggerMock);
    }
}