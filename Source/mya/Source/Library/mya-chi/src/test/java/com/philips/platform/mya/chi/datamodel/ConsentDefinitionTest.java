package com.philips.platform.mya.chi.datamodel;

import com.philips.platform.mya.chi.ConsentDefinitionException;

import org.junit.Test;

import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class ConsentDefinitionTest {

    @Test
    public void constructor_tranformsLocaledInAccourdanceWithHSDPFormat() {
        ConsentDefinition canadaConsentDefinition = new ConsentDefinition("somelocalizedText", "someToolTip", Collections.singletonList("someConsentType"), 2, Locale.CANADA);
        assertEquals("en-CA", canadaConsentDefinition.getLocale());
    }

    @Test(expected = ConsentDefinitionException.class)
    public void itShouldBeAbleToHandle_Locale_ENGLISH() throws Exception {
        ConsentDefinition def = new ConsentDefinition("test", "help", Collections.singletonList("type"), 0, Locale.ENGLISH);
    }

}