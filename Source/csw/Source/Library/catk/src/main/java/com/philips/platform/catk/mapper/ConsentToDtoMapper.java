/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.datamodel.ConsentDTO;
import com.philips.platform.catk.dto.CreateConsentDto;

public class ConsentToDtoMapper {

    private final String subjectId;
    private final String country;
    private final String applicationName;
    private final String propositionName;

    public ConsentToDtoMapper(String subjectId, String country, String propositionName, String applicationName) {
        this.subjectId = subjectId;
        this.country = country;
        this.applicationName = applicationName;
        this.propositionName = propositionName;
    }

    public CreateConsentDto map(ConsentDTO consent) {
        return new CreateConsentDto(consent.getLocale(), buildPolicyRule(consent.getType(), consent.getVersion()), "Consent", consent.getStatus().name(), subjectId);
    }

    private String buildPolicyRule(String type, int version) {
        return new StringBuilder("urn:com.philips.consent:").append(type).append("/").append(country).append("/").append(version).append("/").append(propositionName).append("/")
                .append(applicationName).toString();
    }
}
