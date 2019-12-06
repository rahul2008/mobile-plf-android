/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.Internationalization;

import android.content.Context;
import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class InternationalizationTest {
    private InternationalizationInterface mInternationalizationInterface = null;

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        mInternationalizationInterface = mAppInfra.getInternationalization();
        assertNotNull(mInternationalizationInterface);
    }

    @Test
    public void test_getUILocaleString() {
        assertNotNull(mInternationalizationInterface.getUILocaleString());
    }

    @Test
    public void test_givenInterfaceCreated_whenGetBCP47UILocale_thenShouldReturnContainingUnderscore() {
        String localeString = mInternationalizationInterface.getBCP47UILocale();
        assertThat(localeString, matchesSimpleLocalePattern());
    }

    @NonNull
    private BaseMatcher<String> matchesSimpleLocalePattern() {
        return new BaseMatcher<String>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("Matching pattern: ^[a-z]{2,3}-[a-zA-Z]{2,3}");
            }

            @Override
            public boolean matches(final Object item) {
                Pattern pattern = Pattern.compile("^[a-z]{2,3}-[a-zA-Z]{2,3}");
                final Matcher matcher = pattern.matcher((String) item);
                return matcher.matches();
            }
        };
    }
}
