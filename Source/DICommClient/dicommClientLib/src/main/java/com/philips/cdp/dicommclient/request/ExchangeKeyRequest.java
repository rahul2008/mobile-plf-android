/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import java.util.HashMap;

import org.json.JSONObject;

import com.philips.cdp.dicommclient.security.ByteUtil;
import com.philips.cdp.dicommclient.security.EncryptionUtil;
import com.philips.cdp.dicommclient.util.DICommLog;

public class ExchangeKeyRequest extends LocalRequest {

    private static final String SECURITY_PORTNAME = "security";
    private static final int SECURITY_PRODUCTID = 0;

    private String mRandomValue;

    public ExchangeKeyRequest(String applianceIpAddress, int protocolVersion, ResponseHandler responseHandler) {
        super(applianceIpAddress, protocolVersion, SECURITY_PORTNAME, SECURITY_PRODUCTID, LocalRequestType.PUT, new HashMap<String, Object>(), responseHandler, null);

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
            DICommLog.d(DICommLog.SECURITY, "result hellmam= " + shellman + "     Length:= " + shellman.length());

            String skeyEnc = json.getString("key");
            DICommLog.d(DICommLog.SECURITY, "encrypted key= " + skeyEnc + "    length:= " + skeyEnc.length());

            String key = EncryptionUtil.extractEncryptionKey(shellman, skeyEnc, mRandomValue);
            DICommLog.i(DICommLog.SECURITY, "decryted key= " + key);

            return new Response(key, null, mResponseHandler);
        } catch (Exception e) {
            DICommLog.e(DICommLog.SECURITY, "Exception during key exchange");
        }
        return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
    }
}
