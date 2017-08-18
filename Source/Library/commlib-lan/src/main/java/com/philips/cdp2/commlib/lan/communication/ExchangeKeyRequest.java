/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.common.SecurityPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.Response;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.security.ByteUtil;
import com.philips.cdp.dicommclient.security.EncryptionUtil;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.GsonProvider;

import java.util.HashMap;

import static com.philips.cdp.dicommclient.port.common.SecurityPortProperties.DIFFIE;
import static com.philips.cdp.dicommclient.util.DICommLog.Verbosity.ERROR;

public class ExchangeKeyRequest extends LanRequest {

    private static final String SECURITY_PORTNAME = "security";
    private static final int SECURITY_PRODUCTID = 0;

    private String mRandomValue;

    public ExchangeKeyRequest(final @NonNull NetworkNode networkNode, ResponseHandler responseHandler) {
        super(networkNode, null, SECURITY_PORTNAME, SECURITY_PRODUCTID, LanRequestType.PUT, new HashMap<String, Object>(), responseHandler, null);

        mRandomValue = ByteUtil.generateRandomNum();
        mDataMap.put(DIFFIE, EncryptionUtil.generateDiffieKey(mRandomValue));
    }

    @Override
    public Response execute() {
        Response response = doExecute();
        final String responseData = response.getResponseMessage();

        if (response.getError() == null) {
            final Gson gson = GsonProvider.get();
            try {
                final SecurityPortProperties securityPortProperties = gson.fromJson(responseData, SecurityPortProperties.class);

                final String key = securityPortProperties.getKey();
                final String hellman = securityPortProperties.getHellman();
                final String encryptionKey = EncryptionUtil.extractEncryptionKey(hellman, key, mRandomValue);

                return new Response(encryptionKey, null, mResponseHandler);
            } catch (Exception e) {
                log(ERROR, DICommLog.SECURITY, "Exception during key exchange");
            }
            return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
        } else {
            return new Response(responseData, Error.REQUEST_FAILED, mResponseHandler);
        }
    }

    @VisibleForTesting
    Response doExecute() {
        return super.execute();
    }

    @Override
    protected void log(DICommLog.Verbosity verbosity, @NonNull String tag, @NonNull String message) {
        // Logging disabled
    }
}
