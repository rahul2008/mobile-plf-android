/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.mapper;

import static org.junit.Assert.assertEquals;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.philips.platform.mya.catk.dto.GetConsentDto;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

public class DtoToConsentMapperTest {
    public static final String AMERICAN_LOCALE = "en-US";
    private BackendConsent result;
    private DtoToConsentMapper givenMapper;
    private GetConsentDto givenGetDto;

    private GetConsentDto getActiveFromIndiaTypeMomentLocaleEnUsDto;
    private BackendConsent getActiveFromIndiaTypeMomentLocaleEnUsModel;
    private static final String TIMESTAMP = "2017-10-02T13:32:45.000Z";

    @Before
    public void setUp() throws Exception {
        givenMapper = new DtoToConsentMapper();
        getActiveFromIndiaTypeMomentLocaleEnUsDto = new GetConsentDto(TIMESTAMP, AMERICAN_LOCALE, "urn:com.philips.consent:moment/IN/1/someProposition/someApplication", "Consent",
                ConsentStatus.active, "someSubjectId");
        getActiveFromIndiaTypeMomentLocaleEnUsModel = new BackendConsent(AMERICAN_LOCALE, ConsentStatus.active, "moment", 1);
        getActiveFromIndiaTypeMomentLocaleEnUsModel.setTimestamp(new DateTime(TIMESTAMP));
    }

    @Test
    public void map_mapsCorrectly() {
        givenDto(getActiveFromIndiaTypeMomentLocaleEnUsDto);
        whenCallingMapWith();
        thenConsentIs(getActiveFromIndiaTypeMomentLocaleEnUsModel);
        thenLocaleStringIs("en-US");
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