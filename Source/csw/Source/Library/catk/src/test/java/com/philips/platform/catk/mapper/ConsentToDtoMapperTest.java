/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.philips.platform.catk.datamodel.ConsentDTO;
import com.philips.platform.catk.dto.CreateConsentDto;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

public class ConsentToDtoMapperTest {
    private static final String DUTCH_LOCALE = "nl-NL";
    private CreateConsentDto result;
    private ConsentToDtoMapper givenMapper;
    private ConsentDTO givenConsent;
    private ConsentDTO activeTypeMomentLocaleNlNlVersion1Consent;
    private ConsentDTO activityTypeWithMissingCountry;
    private ConsentDTO activityTypeWithMissingLanguage;

    @Before
    public void setUp() throws Exception {
        givenMapper = new ConsentToDtoMapper("someSubjectId", "IN", "OneBackendProp", "OneBackend");
        activeTypeMomentLocaleNlNlVersion1Consent = new ConsentDTO(DUTCH_LOCALE, ConsentStates.active, "moment", 1);
        activityTypeWithMissingLanguage = new ConsentDTO("-NL", ConsentStates.active, "moment", 1);
        activityTypeWithMissingCountry = new ConsentDTO("nl-", ConsentStates.active, "moment", 1);
    }

    @Test
    public void map_correctlyTo_nlNL() {
        givenConsent(activeTypeMomentLocaleNlNlVersion1Consent);
        whenCallingMapWith();
        thenConsentIs(new CreateConsentDto("nl-NL", "urn:com.philips.consent:moment/IN/1/OneBackendProp/OneBackend", "Consent", "active", "someSubjectId"));
    }

    @Test
    public void map_correctlyTo_NL() {
        givenConsent(activityTypeWithMissingLanguage);
        whenCallingMapWith();
        thenConsentIs(new CreateConsentDto("-NL", "urn:com.philips.consent:moment/IN/1/OneBackendProp/OneBackend", "Consent", "active", "someSubjectId"));
    }

    @Test
    public void map_correctlyTo_nl() {
        givenConsent(activityTypeWithMissingCountry);
        whenCallingMapWith();
        thenConsentIs(new CreateConsentDto("nl-", "urn:com.philips.consent:moment/IN/1/OneBackendProp/OneBackend", "Consent", "active", "someSubjectId"));
    }

    private void givenConsent(ConsentDTO consent) {
        this.givenConsent = consent;
    }

    private void whenCallingMapWith() {
        result = givenMapper.map(givenConsent);
    }

    private void thenConsentIs(CreateConsentDto expectedDto) {
        assertEquals(expectedDto.getLanguage(), result.getLanguage());
        assertEquals(expectedDto.getPolicyRule(), result.getPolicyRule());
        assertEquals(expectedDto.getStatus(), result.getStatus());
        assertEquals(expectedDto.getSubject(), result.getSubject());
    }
}