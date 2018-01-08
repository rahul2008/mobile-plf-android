/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.consenthandlerinterface.datamodel.BackendConsent;

import org.joda.time.DateTime;

public class DtoToConsentMapper {
    private static int IDX_TYPE = 0;
    private static int IDX_VERSION = 2;

    public static BackendConsent map(GetConsentDto consentDto) {
        String[] policyParts = parsePolicyUrn(consentDto.getPolicyRule());
        BackendConsent consent = new BackendConsent(LocaleMapper.toLocale(consentDto.getLanguage()), consentDto.getStatus(), policyParts[IDX_TYPE], Integer.parseInt(policyParts[IDX_VERSION]));
        consent.setTimestamp(new DateTime(consentDto.getDateTime()));
        return consent;
    }

    private static String[] parsePolicyUrn(String privacyRule) {
        String[] urnParts = privacyRule.split(":");
        return urnParts[urnParts.length - 1].split("/");
    }
}
