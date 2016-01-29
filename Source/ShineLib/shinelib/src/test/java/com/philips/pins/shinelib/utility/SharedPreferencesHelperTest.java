package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.pins.shinelib.BuildConfig;
import com.philips.pins.shinelib.SHNCapabilityType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SharedPreferencesHelperTest {

    public static final String KEY = "KEY";

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
    public void whenDoubleIsPut_thenItCanBeRetrievedUsingGet() {
        double value = 35.1356;
        preferencesHelper.put(KEY, value);
        double result = preferencesHelper.get(KEY);

        assertThat(result).isEqualTo(value, within(0.01));
    }

    @Test
    public void whenShortMaxIsPut_thenIsCanBeReturnedUsingGet() {
        short value = Short.MAX_VALUE;
        preferencesHelper.put(KEY, value);
        short res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenShortMinIsPut_thenIsCanBeReturnedUsingGet() {
        short value = Short.MIN_VALUE;
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
    public void whenGetIsCalledForUnknownKey_thenDefaultValueIsReturned() {
        Object expected = new Object();
        Object actual = preferencesHelper.get("SomeRandomKey", expected);

        assertThat(actual).isEqualTo(expected);
    }
	
	@Test
    public void whenNoListIsSet_thenNullIsReturnedWhenUsingGet() {
        List<Integer> res = preferencesHelper.get(KEY);
        assertNull(res);
    }

    @Test
    public void whenAListOfValuesIsSet_thenItCanBeReturnedUsingGet() {
        List<Integer> testList = new ArrayList<>();
        testList.add(4);
        testList.add(2);
        testList.add(923);
        preferencesHelper.put(KEY, testList);

        List<Integer> res = preferencesHelper.get(KEY);

        assertThat(res).isEqualTo(testList);
    }

    @Test
    public void whenNullIsPassedForAIntKey_thenTheIntWillBeDeleted() {
        int value = 222;
        preferencesHelper.put(KEY, value);
        preferencesHelper.put(KEY, null);

        Integer res = preferencesHelper.get(KEY);

        assertThat(res).isNull();
    }
}
