package com.philips.platform.csw;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class ConsentDefinitionTest {

    @Test
    public void constructor_tranformsLocaledInAccourdanceWithHSDPFormat() {
        ConsentDefinition canadaConsentDefinition = new ConsentDefinition("somelocalizedText", "someToolTip", "someConsentType", 2, Locale.CANADA);
        assertEquals("en-CA", canadaConsentDefinition.getLocaleString());
    }

    @Test
    public void itShouldBeAbleToHandle_Locale_ENGLISH() throws Exception {
        ConsentDefinition def = new ConsentDefinition("test", "help", "type", 0, Locale.ENGLISH);
        assertEquals("en_US", def.getLocaleString());
    }

    @Test
    public void itShouldBeAbleToHandle_Locale_getDefault() throws Exception {
        ConsentDefinition def = new ConsentDefinition("test", "help", "type", 0, Locale.getDefault());
        assertEquals("en_US", def.getLocaleString());
    }
}