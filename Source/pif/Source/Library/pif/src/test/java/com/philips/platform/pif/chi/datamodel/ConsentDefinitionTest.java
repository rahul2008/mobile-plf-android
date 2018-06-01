package com.philips.platform.pif.chi.datamodel;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * (C) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
public class ConsentDefinitionTest {

    private List<String> consentTypes;
    private ConsentDefinition consentDefinition;

    @Before
    public void Before()
    {
        consentTypes = Arrays.asList("consentA", "consentB");
        consentDefinition = new ConsentDefinition(1,2,consentTypes,99);
    }

    @Test
    public void ConsentDefinitionConstructor() {
        assertEquals(1, consentDefinition.getText());
        assertEquals(2, consentDefinition.getHelpText());
        assertEquals( consentTypes, consentDefinition.getTypes());
        assertEquals(99, consentDefinition.getVersion());
        assertEquals(0, consentDefinition.getRevokeWarningText());

    }

    @Test
    public void ConsentDefinitionConstructorWithRevokeWarning() {
        consentDefinition = new ConsentDefinition(1,2,consentTypes,99, 8);

        assertEquals(1, consentDefinition.getText());
        assertEquals(2, consentDefinition.getHelpText());
        assertEquals( consentTypes, consentDefinition.getTypes());
        assertEquals(99, consentDefinition.getVersion());
        assertEquals(8, consentDefinition.getRevokeWarningText());
    }

    @Test
    public void ConsentDefinitionTextManipulation() {
        consentDefinition.setText(100);
        assertEquals(100, consentDefinition.getText());
    }

    @Test
    public void ConsentDefinitonHelpTextManipulation() {
        consentDefinition.setHelpText(200);
        assertEquals(200, consentDefinition.getHelpText());
    }

    @Test
    public void ConsentDefinitionConsentTypesManipulation() {

        List<String> newConsentTypes = Arrays.asList("consentX", "consentY", "consentZ");
        consentDefinition.setTypes(newConsentTypes);
        assertEquals(newConsentTypes, consentDefinition.getTypes());
    }

    @Test
    public void ConsentDefinitionVersionManipulation() {
        consentDefinition.setVersion(999);
        assertEquals(999, consentDefinition.getVersion());
    }

    @Test
    public void ConsentDefinitionRevokeTextmanipulation() {
        consentDefinition.setRevokeWarningText(800);
        assertEquals(800, consentDefinition.getRevokeWarningText());
    }
}