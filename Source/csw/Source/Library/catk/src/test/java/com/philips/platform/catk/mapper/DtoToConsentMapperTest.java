/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.philips.platform.catk.datamodel.ConsentDTO;
import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

public class DtoToConsentMapperTest {
    public static final String AMERICAN_LOCALE = "en-US";
    private ConsentDTO result;
    private DtoToConsentMapper givenMapper;
    private GetConsentDto givenGetDto;

    private GetConsentDto getActiveFromIndiaTypeMomentLocaleEnUsDto;
    private ConsentDTO getActiveFromIndiaTypeMomentLocaleEnUsModel;
    private static final String TIMESTAMP = "2017-10-02T13:32:45.000Z";

    @Before
    public void setUp() throws Exception {
        givenMapper = new DtoToConsentMapper();
        getActiveFromIndiaTypeMomentLocaleEnUsDto = new GetConsentDto(TIMESTAMP, AMERICAN_LOCALE, "urn:com.philips.consent:moment/IN/1/someProposition/someApplication", "Consent",
                ConsentStates.active, "someSubjectId");
        getActiveFromIndiaTypeMomentLocaleEnUsModel = new ConsentDTO(AMERICAN_LOCALE, ConsentStates.active, "moment", 1);
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

    private void thenConsentIs(ConsentDTO expectedConsent) {
        assertEquals(expectedConsent, result);
    }

    private void thenLocaleStringIs(String expectedLocaleString) {
        assertEquals(expectedLocaleString, result.getLocale().toString());
    }
}