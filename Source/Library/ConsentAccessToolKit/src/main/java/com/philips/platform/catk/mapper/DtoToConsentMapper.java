package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.GetConsentsModel;
import com.philips.platform.catk.model.Consent;

public class DtoToConsentMapper {


    private int IDX_TYPE = 0;
    private int IDX_VERSION = 2;

    public Consent map(GetConsentsModel consentDto) {

        //urn:com.philips.consent:moment/IN/1/someProposition/someApplication
        String[] policyParts = parsePolicyUrn(consentDto.getPolicyRule());
        return new Consent(consentDto.getLanguage(), consentDto.getStatus(), policyParts[IDX_TYPE], Integer.parseInt(policyParts[IDX_VERSION]));
    }

    private String[] parsePolicyUrn(String privacyRule) {
        String[] urnParts = privacyRule.split(":");
        return urnParts[urnParts.length - 1].split("/");
    }

}
