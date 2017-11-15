/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.catk.model.Consent;

import java.util.Locale;

public class DtoToConsentMapper {
    private static int IDX_TYPE = 0;
    private static int IDX_VERSION = 2;

    private static int IDX_LANGUAGE = 0;
    private static int IDX_COUNTRY = 1;

    public static Consent map(GetConsentDto consentDto) {
        String[] policyParts = parsePolicyUrn(consentDto.getPolicyRule());
        return new Consent(getLocale(consentDto.getLanguage()), consentDto.getStatus(), policyParts[IDX_TYPE], Integer.parseInt(policyParts[IDX_VERSION]));
    }

    private static String[] parsePolicyUrn(String privacyRule) {
        String[] urnParts = privacyRule.split(":");
        return urnParts[urnParts.length - 1].split("/");
    }

    private static Locale getLocale(String language) {
        String[] localeParts = language.split("-");
        return new Locale(localeParts[IDX_LANGUAGE], localeParts[IDX_COUNTRY]);
    }

}
