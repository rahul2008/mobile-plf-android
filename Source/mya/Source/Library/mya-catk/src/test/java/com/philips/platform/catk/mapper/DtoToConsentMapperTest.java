/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.consenthandlerinterface.datamodel.BackendConsent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentStatus;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class DtoToConsentMapperTest {
    private BackendConsent result;
    private DtoToConsentMapper givenMapper;
    private GetConsentDto givenGetDto;

    private GetConsentDto getActiveFromIndiaTypeMomentLocaleEnUsDto;
    private BackendConsent getActiveFromIndiaTypeMomentLocaleEnUsModel;
    private static final String TIMESTAMP = "2017-10-02T13:32:45.000Z";

    @Before
    public void setUp() throws Exception {
        givenMapper = new DtoToConsentMapper();
        getActiveFromIndiaTypeMomentLocaleEnUsDto = new GetConsentDto(TIMESTAMP, "en-US", "urn:com.philips.consent:moment/IN/1/someProposition/someApplication", "Consent", ConsentStatus.active, "someSubjectId");
        getActiveFromIndiaTypeMomentLocaleEnUsModel = new BackendConsent(Locale.US, ConsentStatus.active, "moment", 1);
        getActiveFromIndiaTypeMomentLocaleEnUsModel.setTimestamp(new DateTime(TIMESTAMP));
    }

    @Test
    public void map_mapsCorrectly() {
        givenDto(getActiveFromIndiaTypeMomentLocaleEnUsDto);
        whenCallingMapWith();
        thenConsentIs(getActiveFromIndiaTypeMomentLocaleEnUsModel);
        thenLocaleStringIs("en_US");
    }

    private void givenDto(GetConsentDto getDto) {
        this.givenGetDto = getDto;
    }

    private void whenCallingMapWith() {
        result = givenMapper.map(givenGetDto);
    }

    private void thenConsentIs(BackendConsent expectedConsent) {
        assertEquals(expectedConsent, result);
    }

    private void thenLocaleStringIs(String expectedLocaleString) {
        assertEquals(expectedLocaleString, result.getLocale().toString());
    }
}