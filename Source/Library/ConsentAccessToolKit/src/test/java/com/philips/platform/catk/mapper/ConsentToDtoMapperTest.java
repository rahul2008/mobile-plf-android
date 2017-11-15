/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.CreateConsentModel;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class ConsentToDtoMapperTest {
    private CreateConsentModel result;
    private ConsentToDtoMapper givenMapper;
    private Consent givenConsent;
    private Consent activeTypeMomentLocaleNlNlVersion1Consent;

    @Before
    public void setUp() throws Exception {
        givenMapper = new ConsentToDtoMapper("someSubjectId", "IN", "OneBackendProp", "OneBackend");
        activeTypeMomentLocaleNlNlVersion1Consent = new Consent(new Locale("nl", "NL"), ConsentStatus.active, "moment", 1);
    }

    @Test
    public void map_mapsCorrectly() {
        givenConsent(activeTypeMomentLocaleNlNlVersion1Consent);
        whenCallingMapWith();
        thenConsentIs(new CreateConsentModel("nl-NL", "urn:com.philips.consent:moment/IN/1/OneBackendProp/OneBackend", "Consent", "active", "someSubjectId"));
    }

    private void givenConsent(Consent consent) {
        this.givenConsent = consent;
    }

    private void whenCallingMapWith() {
        result = givenMapper.map(givenConsent);
    }

    private void thenConsentIs(CreateConsentModel expectedDto) {
        assertEquals(expectedDto.getLanguage(), result.getLanguage());
        assertEquals(expectedDto.getPolicyRule(), result.getPolicyRule());
        assertEquals(expectedDto.getResourceType(), result.getResourceType());
        assertEquals(expectedDto.getStatus(), result.getStatus());
        assertEquals(expectedDto.getSubject(), result.getSubject());
    }
}