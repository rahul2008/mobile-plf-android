package com.philips.cl.di.dev.pa.purifier;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.security.DiffieHellmanUtil;
import com.philips.cl.di.dev.pa.security.Util;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.LocalRequestType;
import com.philips.cl.di.dicomm.communication.Response;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public class ExchangeKeyRequest extends LocalRequest {

    private static final String SECURITY_PORTNAME = "security";
    private static final int SECURITY_PRODUCTID = 0;

    private String mRandomValue;

    public ExchangeKeyRequest(NetworkNode networkNode, ResponseHandler responseHandler) {
        super(networkNode, SECURITY_PORTNAME, SECURITY_PRODUCTID, LocalRequestType.PUT, new HashMap<String, Object>(), responseHandler, null);

        mRandomValue = Util.generateRandomNum();
        String sdiffie = DiffieHellmanUtil.generateDiffieKey(mRandomValue);
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
            ALog.d(ALog.SECURITY, "result hellmam= " + shellman + "     Length:= " + shellman.length());

            String skeyEnc = json.getString("key");
            ALog.d(ALog.SECURITY, "encrypted key= " + skeyEnc + "    length:= " + skeyEnc.length());

            String key = DiffieHellmanUtil.extractEncryptionKey(shellman, skeyEnc, mRandomValue);
            ALog.i(ALog.SECURITY, "decryted key= " + key);
            
            mNetworkNode.setEncryptionKey(key);

            return new Response(key, null, mResponseHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(null, Error.REQUESTFAILED, mResponseHandler);
    }
}