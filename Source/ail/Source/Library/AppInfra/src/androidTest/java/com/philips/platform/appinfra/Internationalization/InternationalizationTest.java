package com.philips.platform.appinfra.Internationalization;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;

import android.content.Context;
import android.support.annotation.NonNull;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertThat;

/**
 * Internationalization Test class.
 */

public class InternationalizationTest extends AppInfraInstrumentation {
    private InternationalizationInterface mInternationalizationInterface = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        mInternationalizationInterface = mAppInfra.getInternationalization();
        assertNotNull(mInternationalizationInterface);

    }

    public void test_getUILocaleString() {
        assertNotNull(mInternationalizationInterface.getUILocaleString());
    }

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
