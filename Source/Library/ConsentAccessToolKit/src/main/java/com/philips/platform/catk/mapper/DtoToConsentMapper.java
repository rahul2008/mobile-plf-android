/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.GetConsentsModel;
import com.philips.platform.catk.model.Consent;

public class DtoToConsentMapper {
    private static int IDX_TYPE = 0;
    private static int IDX_VERSION = 2;

    public static Consent map(GetConsentsModel consentDto) {
        String[] policyParts = parsePolicyUrn(consentDto.getPolicyRule());
        return new Consent(consentDto.getLanguage(), consentDto.getStatus(), policyParts[IDX_TYPE], Integer.parseInt(policyParts[IDX_VERSION]));
    }

    private static String[] parsePolicyUrn(String privacyRule) {
        String[] urnParts = privacyRule.split(":");
        return urnParts[urnParts.length - 1].split("/");
    }
}
