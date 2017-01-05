/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.apisigning;

/*
 * Created by 310209604 on 2016-10-26.
 */

import android.util.Base64;

import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HSDPPHSApiSigning implements ApiSigningInterface {
    private String secretKey;
    private String sharedKey;
    private static final String ALGORITHM_NAME = "HmacSHA256";
    public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");

    /**
     * Create an API signer instance according to HSDP specification
     *
     * @param sharedKey    Key shared with server identifying the signing key
     * @param hexSecretKey secret API signing key formatted as a 128byte hex string
     */
    public HSDPPHSApiSigning(String sharedKey, String hexSecretKey) {
        this.sharedKey = sharedKey;
        this.secretKey = hexSecretKey;
    }

    @Override
    public String createSignature(String requestMethod, String queryString, Map<String, String> headers, String dhpUrl, String requestbody) {
        byte[] signatureKey = hashRequest(requestMethod, queryString, requestbody, joinHeaders(headers));
        String signature = signString(signatureKey, dhpUrl);

        return buildAuthorizationHeaderValue(joinHeaders(headers), signature);
    }

   /* public String buildAuthorizationHeaderValue(String requestMethod, String queryString, Map<String, String> headers, String dhpUrl, String requestbody) {
    }*/

    private String joinHeaders(Map<String, String> headers) {
        List<String> headerList = new LinkedList<String>();

        for (Map.Entry<String, String> header : headers.entrySet())
            headerList.add(header.getKey() + ":" + header.getValue());

        return joinHeaders(headerList);
    }

    private String joinHeaders(List<String> headerList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String header : headerList) {
            stringBuilder.append(header);
            stringBuilder.append(";");
        }

        return stringBuilder.toString();
    }

    private String buildAuthorizationHeaderValue(String requestHeader, String signature) {
        StringBuilder buffer = new StringBuilder(ALGORITHM_NAME);
        buffer.append(";");
        buffer.append("Credential:");
        buffer.append(sharedKey);
        buffer.append(";");
        buffer.append("SignedHeaders:");

        if (requestHeader != null && !requestHeader.isEmpty())
            for (String h : requestHeader.split(";")) {
                String headerName = h.split(":")[0];
                buffer.append(headerName);
                buffer.append(",");
            }

        buffer.append(";");
        buffer.append("Signature:");
        buffer.append(signature);
        return buffer.toString();
    }

    private byte[] hashRequest(String requestMethod, String queryString, String requestBody, String requestHeaders) {
        PshmacLib pshmacLib = new PshmacLib();
        byte[] key = hexStringToByteArray(secretKey);
        byte[] kMethod = pshmacLib.createHmac(key,requestMethod.getBytes());
        String resultBase64 = Base64.encodeToString(kMethod,Base64.DEFAULT);
        kMethod= Base64.decode(resultBase64,Base64.DEFAULT);
        final byte[] kQueryString = hash(queryString, kMethod);
        final byte[] kBody = hash(requestBody, kQueryString);
        return hash(requestHeaders, kBody);
    }

    private String signString(byte[] signatureKey, String uriToBeSigned) {
        byte[] signatureArray = hash(uriToBeSigned, signatureKey);
        return Base64.encodeToString(signatureArray,Base64.DEFAULT);
    }

    private byte[] hash(String data, byte[] key) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM_NAME);
            mac.init(new SecretKeySpec(key, ALGORITHM_NAME));
            if (data== null) return mac.doFinal(null);
            return mac.doFinal(data.getBytes(UTF_8_CHARSET));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Error during hash generation", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Error during hash generation", e);
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
 }