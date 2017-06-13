/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.security.ByteUtil;
import com.philips.cdp.dicommclient.security.EncryptionUtil;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.communication.LanRequest;
import com.philips.cdp2.commlib.lan.communication.LanRequestType;

import org.json.JSONObject;

import java.util.HashMap;

import static com.philips.cdp.dicommclient.util.DICommLog.Verbosity.DEBUG;
import static com.philips.cdp.dicommclient.util.DICommLog.Verbosity.ERROR;
import static com.philips.cdp.dicommclient.util.DICommLog.Verbosity.INFO;

public class ExchangeKeyRequest extends LanRequest {

    private static final String SECURITY_PORTNAME = "security";
    private static final int SECURITY_PRODUCTID = 0;

    private String mRandomValue;

    public ExchangeKeyRequest(String applianceIpAddress, int protocolVersion, boolean isHttps, ResponseHandler responseHandler) {
        super(applianceIpAddress, protocolVersion, isHttps, SECURITY_PORTNAME, SECURITY_PRODUCTID, LanRequestType.PUT, new HashMap<String, Object>(), responseHandler, null);

        mRandomValue = ByteUtil.generateRandomNum();
        String sdiffie = EncryptionUtil.generateDiffieKey(mRandomValue);
        mDataMap.put("diffie", sdiffie);
    }

    @Override
    public Response execute() {
        Response response = super.execute();
        String responseData = response.getResponseMessage();

        JSONObject json;
        try {
            json = new JSONObject(responseData);
            String shellman = json.getString("hellman");
            log(DEBUG, DICommLog.SECURITY, "result hellman= " + shellman + ", length= " + shellman.length());

            String skeyEnc = json.getString("key");
            log(DEBUG, DICommLog.SECURITY, "encrypted key= " + skeyEnc + ", length= " + skeyEnc.length());

            String key = EncryptionUtil.extractEncryptionKey(shellman, skeyEnc, mRandomValue);
            log(INFO, DICommLog.SECURITY, "decrypted key= " + key);

            return new Response(key, null, mResponseHandler);
        } catch (Exception e) {
            log(ERROR, DICommLog.SECURITY, "Exception during key exchange");
        }
        return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
    }
}
