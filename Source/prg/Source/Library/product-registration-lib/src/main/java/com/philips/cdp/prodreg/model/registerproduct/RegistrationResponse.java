/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.model.registerproduct;

import com.google.gson.Gson;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

public class RegistrationResponse extends ResponseData {
    private RegistrationResponseNewData data;

    private String success;

    public RegistrationResponseNewData getData() {
        return data;
    }

    public void setData(RegistrationResponseNewData data) {
        this.data = data;
    }

    public String isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "ProdRegResponse [data = " + data + ", success = " + success + "]";
    }

    public ResponseData parseJsonResponseData(JSONObject response) {
        RegistrationResponse registrationResponse;
        registrationResponse = new Gson().fromJson(response.toString(), RegistrationResponse.class);
        return registrationResponse;
    }
}
