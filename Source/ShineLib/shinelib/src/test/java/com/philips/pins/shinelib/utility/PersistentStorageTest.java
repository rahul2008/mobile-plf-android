/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.pins.shinelib.RobolectricTest;
import com.philips.pins.shinelib.SHNCapabilityType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

public class PersistentStorageTest extends RobolectricTest {

    public static final String KEY = "KEY";

    @Mock
    private SHNLogger.LoggerImplementation loggerMock;

    private PersistentStorage persistentStorage;

    @Before
    public void setUp() {
        initMocks(this);

        SharedPreferences sharedPreferences = RuntimeEnvironment.application.getSharedPreferences("testPreferences", Context.MODE_PRIVATE);
        persistentStorage = new PersistentStorage(sharedPreferences);
    }

    @Test
    public void whenBooleanIsPut_thenIsCanBeReturnedUsingGet() {
        persistentStorage.put(KEY, true);
        boolean res = persistentStorage.get(KEY);

        assertThat(res).isTrue();
    }

    @Test
    public void whenDoubleIsPut_thenItCanBeRetrievedUsingGet() {
        double value = 35.1356;
        persistentStorage.put(KEY, value);
        double result = persistentStorage.get(KEY);

        assertThat(result).isEqualTo(value, within(0.01));
    }

    @Test
    public void whenShortMaxIsPut_thenIsCanBeReturnedUsingGet() {
        short value = Short.MAX_VALUE;
        persistentStorage.put(KEY, value);
        short res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenShortMinIsPut_thenIsCanBeReturnedUsingGet() {
        short value = Short.MIN_VALUE;
        persistentStorage.put(KEY, value);
        short res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenIntIsPut_thenIsCanBeReturnedUsingGet() {
        int value = 222;
        persistentStorage.put(KEY, value);
        int res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenEnumIsPut_thenIsCanBeReturnedUsingGet() {
        SHNCapabilityType value = SHNCapabilityType.DATA_STREAMING;
        persistentStorage.put(KEY, value);
        SHNCapabilityType res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenFloatIsPut_thenIsCanBeReturnedUsingGet() {
        float value = 333f;
        persistentStorage.put(KEY, value);
        float res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenLongIsPut_thenIsCanBeReturnedUsingGet() {
        long value = 444l;
        persistentStorage.put(KEY, value);
        long res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenStringIsPut_thenIsCanBeReturnedUsingGet() {
        String value = "TEST";
        persistentStorage.put(KEY, value);
        String res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenStringSetIsPut_thenIsCanBeReturnedUsingGet() {
        HashSet<String> value = new HashSet<>();
        value.add("TEST");
        persistentStorage.put(KEY, value);
        Set<String> res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(value);
    }

    @Test
    public void whenGetIsCalledForUnknownKey_thenDefaultValueIsReturned() {
        Object expected = new Object();
        Object actual = persistentStorage.get("SomeRandomKey", expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenNoListIsSet_thenNullIsReturnedWhenUsingGet() {
        List<Integer> res = persistentStorage.get(KEY);
        assertNull(res);
    }

    @Test
    public void whenAListOfValuesIsSet_thenItCanBeReturnedUsingGet() {
        List<Integer> testList = new ArrayList<>();
        testList.add(4);
        testList.add(2);
        testList.add(923);
        persistentStorage.put(KEY, testList);

        List<Integer> res = persistentStorage.get(KEY);

        assertThat(res).isEqualTo(testList);
    }

    @Test
    public void whenNullIsPassedForABooleanKey_thenTheIntWillBeDeleted() {
        boolean value = true;
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Boolean res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForAIntKey_thenTheIntWillBeDeleted() {
        int value = 222;
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Integer res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForAFloatKey_thenTheIntWillBeDeleted() {
        float value = 222.f;
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Float res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForADoubleKey_thenTheIntWillBeDeleted() {
        double value = 222.0;
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Double res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForALongKey_thenTheIntWillBeDeleted() {
        long value = 222;
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Long res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForAStringKey_thenTheIntWillBeDeleted() {
        String value = "TEST";
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        String res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForAShortKey_thenTheIntWillBeDeleted() {
        short value = 222;
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Short res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForAEnumKey_thenTheIntWillBeDeleted() {
        SHNCapabilityType value = SHNCapabilityType.DATA_STREAMING;
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Short res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForASetKey_thenTheIntWillBeDeleted() {
        HashSet<String> value = new HashSet<>();
        value.add("TEST");
        persistentStorage.put(KEY, value);
        persistentStorage.put(KEY, null);

        Set res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenNullIsPassedForAListKey_thenTheIntWillBeDeleted() {
        List<Integer> testList = new ArrayList<>();
        testList.add(923);
        persistentStorage.put(KEY, testList);
        persistentStorage.put(KEY, null);

        Set res = persistentStorage.get(KEY);

        assertThat(res).isNull();
    }

    @Test
    public void whenClearIsCalled_ThenAllDataIsCleared() {
        persistentStorage.put(KEY, true);
        persistentStorage.clear();
        boolean res = persistentStorage.contains(KEY);

        assertThat(res).isFalse();
    }
}
