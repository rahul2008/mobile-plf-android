package com.philips.platform.catk;

import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentDefinitionException;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class ConsentDefinitionTest {

    @Test
    public void constructor_tranformsLocaledInAccourdanceWithHSDPFormat() {
        ConsentDefinition canadaConsentDefinition = new ConsentDefinition("somelocalizedText", "someToolTip", "someConsentType", 2, Locale.CANADA);
        assertEquals("en-CA", canadaConsentDefinition.getLocaleString());
    }

    @Test(expected = ConsentDefinitionException.class)
    public void itShouldBeAbleToHandle_Locale_ENGLISH() throws Exception {
        ConsentDefinition def = new ConsentDefinition("test", "help", "type", 0, Locale.ENGLISH);
    }

}