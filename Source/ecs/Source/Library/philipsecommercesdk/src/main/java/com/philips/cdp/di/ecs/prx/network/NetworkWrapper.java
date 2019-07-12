package com.philips.cdp.di.ecs.prx.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.philips.cdp.di.ecs.prx.error.PrxError;
import com.philips.cdp.di.ecs.prx.prxclient.PRXDependencies;
import com.philips.cdp.di.ecs.prx.prxclient.PrxConstants;
import com.philips.cdp.di.ecs.prx.request.PrxRequest;
import com.philips.cdp.di.ecs.prx.response.ResponseData;
import com.philips.cdp.di.ecs.prx.response.ResponseListener;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.GsonCustomRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A class which performs HTTP get, maintains request queue, handles caching etc.
 It is responsible for interacting with any third party libraries that is used for performing network operations.
 @since 1.0.0
 */
public class NetworkWrapper {

    private final PRXDependencies mPrxDependencies;
    private final LoggingInterface mPrxLogging;

    /**
     * NetworkWrapper constructor.
     * @param prxDependencies PRX dependencies
     * @since 1.0.0
     */
    public NetworkWrapper(PRXDependencies prxDependencies) {
        mPrxDependencies = prxDependencies;
        mPrxLogging = prxDependencies.mAppInfraLogging;
    }

    /**
     *  Execute custom JSON request.
     * @param prxRequest PRX Request
     * @param listener Response listener
     * @since 1.0.0
     */
    public void executeCustomJsonRequest(final PrxRequest prxRequest, final ResponseListener listener) {
        if (listener == null) {

        } else {
            final Response.Listener<JSONObject> responseListener = getVolleyResponseListener(prxRequest, listener);
            final Response.ErrorListener errorListener = getVolleyErrorListener(listener);
            if (mPrxDependencies != null && mPrxDependencies.getAppInfra() != null) {
                prxRequest.getRequestUrlFromAppInfra(new PrxRequest.OnUrlReceived() {
                    @Override
                    public void onSuccess(String url) {
                        GsonCustomRequest<JSONObject> request = null;
                        try {
                            request = new GsonCustomRequest<JSONObject>(prxRequest.getRequestType(),
                                    url, null, responseListener, errorListener,
                                    prxRequest.getHeaders(), prxRequest.getParams(), null) {

                                @Override
                                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                                    try {
                                        String jsonString = new String(response.data,
                                                HttpHeaderParser.parseCharset(response.headers));

                                        JSONObject result = null;

                                        if (jsonString.length() > 0)
                                            result = new JSONObject(jsonString);

                                        return Response.success(result,
                                                HttpHeaderParser.parseCacheHeaders(response));
                                    } catch (UnsupportedEncodingException e) {
                                        return Response.error(new ParseError(e));
                                    } catch (JSONException je) {
                                        return Response.error(new ParseError(je));
                                    }
                                }
                            };

                            request.setRetryPolicy(new DefaultRetryPolicy(
                                    prxRequest.getRequestTimeOut(),
                                    prxRequest.getMaxRetries(),
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            request.setShouldCache(true);
                        } catch (Exception e) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getDescription(), PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getId()));
                        }

                        if (request != null) {
                            if (mPrxDependencies.getAppInfra().getRestClient() != null) {
                                mPrxDependencies.getAppInfra().getRestClient().getRequestQueue().add(request);
                            }

                        }

                    }

                    @Override
                    public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
                        listener.onResponseError(new PrxError(PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getDescription(), PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getId()));
                    }
                });
            } else {
                listener.onResponseError(new PrxError(PrxError.PrxErrorType.INJECT_APPINFRA.getDescription(), PrxError.PrxErrorType.INJECT_APPINFRA.getId()));
            }
        }
    }


    private Response.ErrorListener getVolleyErrorListener(final ResponseListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if (error != null) {
                    final NetworkResponse networkResponse = error.networkResponse;
                    try {
                        if (error instanceof NoConnectionError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.NO_INTERNET_CONNECTION.getDescription(), PrxError.PrxErrorType.NO_INTERNET_CONNECTION.getId()));
                        } else if (error instanceof TimeoutError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.TIME_OUT.getDescription(), PrxError.PrxErrorType.TIME_OUT.getId()));
                        } else if (error instanceof AuthFailureError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.AUTHENTICATION_FAILURE.getDescription(), PrxError.PrxErrorType.AUTHENTICATION_FAILURE.getId()));
                        } else if (error instanceof NetworkError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.NETWORK_ERROR.getDescription(), PrxError.PrxErrorType.NETWORK_ERROR.getId()));
                        } else if (error instanceof ParseError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.PARSE_ERROR.getDescription(), PrxError.PrxErrorType.PARSE_ERROR.getId()));
                        } else if (networkResponse != null) {
                            listener.onResponseError(new PrxError(networkResponse.toString(), networkResponse.statusCode));
                        } else
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getDescription(), PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getId()));
                    } catch (Exception e) {
                        listener.onResponseError(new PrxError(PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getDescription(), PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getId()));
                    }
                }
            }
        };
    }


    private Response.Listener<JSONObject> getVolleyResponseListener(final PrxRequest prxRequest,
                                                                    final ResponseListener listener) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                ResponseData responseData = prxRequest.getResponseData(response);

                if (responseData != null) {
                    if (response != null)
                    listener.onResponseSuccess(responseData);
                } else {
                    listener.onResponseError(new PrxError("Null Response", 00));
                }
            }
        };
    }
}
