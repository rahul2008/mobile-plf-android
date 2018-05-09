/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.apisigning;

/*
 * The ApiSigning  WhiteBox Api Implementation  .
 */

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HSDPPHSApiSigning implements ApiSigningInterface {

    private static final long serialVersionUID = -1960924347821402867L;
    private byte[] secretKey;
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
        this.secretKey = hexStringToByteArray(hexSecretKey);
    }

    @Override
    public String createSignature(String requestMethod, String queryString, Map<String, String> headers, String dhpUrl, String requestbody) {
        final byte[] signatureKey = hashRequest(requestMethod, queryString, requestbody, joinHeaders(headers));
        //Log.v(AppInfraLogEventID.AI_API_SIGNING, "Created Signature Key");

        final String signature = signString(signatureKey, dhpUrl);
        //Log.v(AppInfraLogEventID.AI_API_SIGNING, "Signed the Signature Key String with dhp Url");

        return buildAuthorizationHeaderValue(joinHeaders(headers), signature);
    }

    private String joinHeaders(Map<String, String> headers) {
        final List<String> headerList = new LinkedList<String>();

        for (Map.Entry<String, String> header : headers.entrySet())
            headerList.add(header.getKey() + ":" + header.getValue());
        return joinHeaders(headerList);
    }

    private String joinHeaders(List<String> headerList) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String header : headerList) {
            stringBuilder.append(header);
            stringBuilder.append(";");
        }

        return stringBuilder.toString();
    }

    private String buildAuthorizationHeaderValue(String requestHeader, String signature) {
        final StringBuilder buffer = new StringBuilder(ALGORITHM_NAME);
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
     //   Log.v(AppInfraLogEventID.AI_API_SIGNING, "Build Authorization Header Value String "+buffer);
        return buffer.toString();
    }

    private byte[] hashRequest(String requestMethod, String queryString, String requestBody, String requestHeaders) {
        final PsLib psLib = new PsLib();
//        Log.v(AppInfraLogEventID.AI_API_SIGNING, "PS Library Loaded ");
        final byte[] kMethod = psLib.createHmac(this.secretKey,requestMethod.getBytes());
//        Log.v(AppInfraLogEventID.AI_API_SIGNING, "Created Hmac Key");
        final byte[] kQueryString = hash(queryString, kMethod);
        final byte[] kBody = hash(requestBody, kQueryString);
        return hash(requestHeaders, kBody);
    }

    private String signString(byte[] signatureKey, String uriToBeSigned) {
        final byte[] signatureArray = hash(uriToBeSigned, signatureKey);
//        Log.v(AppInfraLogEventID.AI_API_SIGNING, "Encode  signature Array to Base64 encode string ");
        return Base64.encodeToString(signatureArray,Base64.DEFAULT);
    }

    private byte[] hash(String data, byte[] key) {
        try {
            final Mac mac = Mac.getInstance(ALGORITHM_NAME);
            mac.init(new SecretKeySpec(key, ALGORITHM_NAME));
            if (data== null) {
                return mac.doFinal(null);
            }
//            Log.v(AppInfraLogEventID.AI_API_SIGNING, "hash Mac SecretKeySpec");
            return mac.doFinal(data.getBytes(UTF_8_CHARSET));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalArgumentException("Error during hash generation", e);
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        final int len = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
//        Log.v(AppInfraLogEventID.AI_API_SIGNING, "hexString To ByteArray type Casting");
        return data;
    }
 }