/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.CreateConsentDto;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class ConsentToDtoMapperTest {
    private CreateConsentDto result;
    private ConsentToDtoMapper givenMapper;
    private Consent givenConsent;
    private Consent activeTypeMomentLocaleNlNlVersion1Consent;
    private Consent activityTypeWithMissingCountry;
    private Consent activityTypeWithMissingLanguage;

    @Before
    public void setUp() throws Exception {
        givenMapper = new ConsentToDtoMapper("someSubjectId", "IN", "OneBackendProp", "OneBackend");
        activeTypeMomentLocaleNlNlVersion1Consent = new Consent(new Locale("nl", "NL"), ConsentStatus.active, "moment", 1);
        activityTypeWithMissingLanguage = new Consent(new Locale("", "NL"), ConsentStatus.active, "moment", 1);
        activityTypeWithMissingCountry = new Consent(new Locale("nl", ""), ConsentStatus.active, "moment", 1);
    }

    @Test
    public void map_correctlyTo_nlNL() {
        givenConsent(activeTypeMomentLocaleNlNlVersion1Consent);
        whenCallingMapWith();
        thenConsentIs(new CreateConsentDto("nl-NL", "urn:com.philips.consent:moment/IN/1/OneBackendProp/OneBackend", "Consent", "active", "someSubjectId"));
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowExceptionWhenLocaleIsMissingCountry() {
        givenConsent(activityTypeWithMissingCountry);
        whenCallingMapWith();
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowExceptionWhenLocaleIsMissingLanguage() {
        givenConsent(activityTypeWithMissingLanguage);
        whenCallingMapWith();
    }

    private void givenConsent(Consent consent) {
        this.givenConsent = consent;
    }

    private void whenCallingMapWith() {
        result = givenMapper.map(givenConsent);
    }

    private void thenConsentIs(CreateConsentDto expectedDto) {
        assertEquals(expectedDto.getLanguage(), result.getLanguage());
        assertEquals(expectedDto.getPolicyRule(), result.getPolicyRule());
        assertEquals(expectedDto.getResourceType(), result.getResourceType());
        assertEquals(expectedDto.getStatus(), result.getStatus());
        assertEquals(expectedDto.getSubject(), result.getSubject());
    }
}