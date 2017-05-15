/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.response.DhpResponseVerifier;
import com.philips.dhpclient.util.HsdpLog;
import com.philips.ntputils.ServerTime;
import com.philips.ntputils.constants.ServerTimeConstants;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

public class DhpApiClient {

    private final static ObjectMapper JSON_MAPPER;
    private final static int DHP_RESPONSE_TIME_LOGGING_THRESHOLD_MS = 500;
    private final static String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private  ApiSigner apiSigner ;
    private  String apiBaseUrl ;
    protected  String dhpApplicationName ;

    private DhpResponseVerifier responseVerifier = new DhpResponseVerifier() {
        public void verify(DhpResponse dhpResponse) {
            // no-op by default
        }
    };

    public DhpApiClient(DhpApiClientConfiguration dhpApiClientConfiguration) {
        if(dhpApiClientConfiguration !=null) {
            if (dhpApiClientConfiguration.getApiBaseUrl() == null || dhpApiClientConfiguration.getDhpApplicationName() == null)
                throw new IllegalArgumentException("Missing DHP authentication communication settings");

            apiBaseUrl = dhpApiClientConfiguration.getApiBaseUrl();
            dhpApplicationName = dhpApiClientConfiguration.getDhpApplicationName();
            apiSigner = ApiSigner.Get.signer(dhpApiClientConfiguration.getSigningKey(), dhpApiClientConfiguration.getSigningSecret());
        }
    }

    protected DhpResponse sendSignedRequest(String httpMethod, String apiEndpoint, String queryParams, Map<String, String> headers, Object body) {
        String bodyString = asJsonString(body);
        addSignedDateHeader(headers);
        sign(headers, apiEndpoint, queryParams, httpMethod, bodyString);
        URI uri = URI.create(apiBaseUrl + apiEndpoint + queryParams(queryParams));
        HsdpLog.d("Hsdp URI : ",""+uri.toString());
        HsdpLog.d("Hsdp Headers : ",""+headers);
        HsdpLog.d("Hsdp httpMethod type : ",""+httpMethod);
        if(body != null) {
            HsdpLog.d("Hsdp body : ", "" + body.toString());
        }

        return sendRestRequest(httpMethod, uri, headers, bodyString);
    }

    protected DhpResponse sendSignedRequestForSocialLogin(String httpMethod, String apiEndpoint, String queryParams, Map<String, String> headers, Object body) {
        String bodyString = asJsonString(body);
        addSignedDateHeader(headers);

        sign(headers, apiEndpoint, queryParams, httpMethod, bodyString);
        URI uri = URI.create(apiBaseUrl + apiEndpoint + queryParams(queryParams));

        HsdpLog.d("Hsdp URI : ",""+uri.toString());
        HsdpLog.d("Hsdp httpMethod type : ",""+httpMethod);
        HsdpLog.d("Hsdp headers : ",""+headers);
        HsdpLog.d("Hsdp headers body : ",""+body);
        return sendRestRequest(httpMethod, uri, headers, bodyString);
    }

    private void addSignedDateHeader(Map<String, String> headers) {
        headers.put("SignedDate", UTCDatetimeAsString());
    }

    protected DhpResponse sendRestRequest(String httpMethod, String apiEndpoint, String queryParams, Map<String, String> headers, Object body) {
        String bodyString = asJsonString(body);
        URI uri = URI.create(apiBaseUrl + apiEndpoint + queryParams(queryParams));
        return sendRestRequest(httpMethod, uri, headers, bodyString);
    }

    @SuppressWarnings("unchecked")
    protected DhpResponse sendRestRequest(String httpMethod, URI uri, Map<String, String> headers, String body) {
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        long requestStart = System.currentTimeMillis();
        try {
            @SuppressWarnings("rawtypes")
            Map<String, Object> rawResponse = establishConnection(uri, httpMethod, headers, body);

            DhpResponse response = new DhpResponse(rawResponse);
            responseVerifier.verify(response);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            //throw new DhpCommunicationException(e);
        } finally {
            long requestEnd = System.currentTimeMillis();
            long requestDuration = (requestEnd - requestStart);

            if (requestDuration > DHP_RESPONSE_TIME_LOGGING_THRESHOLD_MS)
                HsdpLog.d("DHP request : ",""+String.format("DHP request %s %s took %d ms", httpMethod, uri, requestDuration));
        }
    }

    private Map<String, Object> establishConnection(URI uri, String httpMethod, Map<String, String> headers, String body) throws IOException, JSONException {
        HttpURLConnection urlConnection = openHttpURLConnection(uri);
        try {
            urlConnection.setRequestMethod(httpMethod);
            addRequestHeaders(headers, urlConnection);

            addRequestBody(body, urlConnection);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);
        } finally {
            urlConnection.disconnect();
        }

    }

    private Map<String, Object> readStream(InputStream in) throws IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        in.close();

        return JSON_MAPPER.readValue(responseStrBuilder.toString(), Map.class);
    }

    private void addRequestBody(String body, HttpURLConnection urlConnection) throws IOException {
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);

        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
        writeStream(out, body);
    }

    private HttpURLConnection openHttpURLConnection(URI uri) throws IOException {
        return (HttpURLConnection) uri.toURL().openConnection();
    }

    private void addRequestHeaders(Map<String, String> headers, HttpURLConnection urlConnection) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private void writeStream(OutputStream out, String body) throws IOException {
        out.write(body.getBytes(Charset.forName("UTF-8")));

        out.flush();
    }

    private String queryParams(String queryParamString) {
        if (queryParamString == null || queryParamString.isEmpty())
            return "";

        return "?" + queryParamString;
    }

    private void sign(Map<String, String> headers, String url, String queryParams, String httpMethod, String body) {
        String authHeaderValue = apiSigner.createHeader(httpMethod, queryParams, headers, url, body);
        headers.put("Authorization", authHeaderValue);
    }

    private String asJsonString(Object request) {
        try {
            return JSON_MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String UTCDatetimeAsString(){
        return ServerTime.getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT);
    }

    public void setResponseVerifier(DhpResponseVerifier responseVerifier) {
        this.responseVerifier = responseVerifier;
    }
}
