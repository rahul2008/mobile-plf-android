/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class DhpApiSigner implements ApiSigner{

    private static final String ALGORITHM_NAME = "HmacSHA256";
    private static final String SECRET_KEY_PREFIX = "DHPWS";
    public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");

    private final String secretKey;
    private final String sharedKey;

    public DhpApiSigner(String sharedKey, String secretKey) {
        this.sharedKey = sharedKey;
        this.secretKey = secretKey;
    }

    public String buildAuthorizationHeaderValue(String requestMethod, String queryString, Map<String, String> headers, String dhpUrl, String requestbody) {
        byte[] signatureKey = hashRequest(requestMethod, queryString, requestbody, joinHeaders(headers));
        String signature = signString(signatureKey, dhpUrl);

        return buildAuthorizationHeaderValue(joinHeaders(headers), signature);
    }

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
        byte[] kSecret = (SECRET_KEY_PREFIX + secretKey).getBytes(UTF_8_CHARSET);
        final byte[] kMethod = hash(requestMethod, kSecret);
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
            return mac.doFinal(data.getBytes(UTF_8_CHARSET));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Error during hash generation", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Error during hash generation", e);
        }
    }

    @Override
    public String createHeader(String httpMethod, String queryParams, Map<String, String> headers, String url, String body) {
        String header = buildAuthorizationHeaderValue(httpMethod, queryParams, headers, url, body);
        return header;
    }
}
