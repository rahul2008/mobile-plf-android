package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.CreateConsentModel;
import com.philips.platform.catk.dto.GetConsentsModel;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.response.ConsentStatus;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsentToDtoMapperTest {

    private CreateConsentModel result;

    private ConsentToDtoMapper givenMapper;

    private Consent givenConsent;

    private Consent activeTypeMomentLocaleNlNlVersion1Consent;

    @Before
    public void setUp() throws Exception {
        givenMapper = new ConsentToDtoMapper("someSubjectId", "IN", "OneBackendProp", "OneBackend");
        activeTypeMomentLocaleNlNlVersion1Consent = new Consent("nl-NL", ConsentStatus.active, "moment", 1);
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
        assertEquals(expectedDto, result);
    }

}