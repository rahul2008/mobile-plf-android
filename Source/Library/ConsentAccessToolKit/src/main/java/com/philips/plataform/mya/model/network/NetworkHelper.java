package com.philips.plataform.mya.model.network;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.plataform.mya.model.error.ConsentError;
import com.philips.plataform.mya.model.listener.ConsentResponseListener;
import com.philips.plataform.mya.model.response.ConsentModel;
import com.philips.plataform.mya.model.utils.ConsentUtil;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.request.GsonCustomRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maqsood on 10/12/17.
 */

public class NetworkHelper {

    private Context mContext;
    private User mUser;
    private String mHsdpUUID;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public void getLatestConsentStatus(Context context, final ConsentResponseListener consentListener) {
        mContext = context;
        mUser = new User(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                performRefreshToken(consentListener);
            }
        }).start();
    }

    private void performRefreshToken(final ConsentResponseListener consentListener) {
        mUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                performGetLatestConsentStatus(consentListener);
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int errorCode) {
                consentListener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.USER_REFRESH_FAILED.getDescription(),
                        errorCode));
            }

            @Override
            public void onRefreshLoginSessionInProgress(String inProgress) {

            }
        });
    }

    private void performGetLatestConsentStatus(ConsentResponseListener consentListener) {
        GsonCustomRequest<JsonArray> request = null;
        mHsdpUUID = mUser.getHsdpUUID();
        String url = "https://hdc-css-mst.cloud.pcftest.com/consent/";
        url += mHsdpUUID+"?applicationName=OneBackend&propositionName=OneBackendProp";
        try {
            final Response.Listener<JsonArray> responseListener = getVolleyResponseListener(consentListener);
            final Response.ErrorListener errorListener = getVolleyErrorListener(consentListener);
            request = new GsonCustomRequest<JsonArray>(Request.Method.GET,
                    url, null, responseListener, errorListener,
                    getHeaders(), null, null) {

                @Override
                protected Response<JsonArray> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = (JsonArray)parser.parse(jsonString);

                        return Response.success(jsonArray,
                                HttpHeaderParser.parseCacheHeaders(response));
                    }catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (Exception e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    ConsentUtil.requestTimeOut,
                    ConsentUtil.maxRetries,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(true);

        } catch (Exception e) {
            consentListener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.UNKNOWN_EXCEPTION.getDescription(), ConsentError.ConsentErrorType.UNKNOWN_EXCEPTION.getId()));
        }

        addRequestToQueue(request);
    }

    private void addRequestToQueue(GsonCustomRequest<JsonArray> request) {
        AppInfra ai = new AppInfra.Builder().build(mContext);
        if (request != null) {
            if (ai.getRestClient() != null) {
                ai.getRestClient().getRequestQueue().add(request);
            } else {
                Log.d("Rest client", "Couldn't initialise REST Client");

            }
        }
    }

    private Map getHeaders() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api-version", "1");
        params.put("authorization", "bearer "+mUser.getHsdpAccessToken());
        params.put("content-type", "application/json");
        params.put("performerid",mHsdpUUID);
        params.put("cache-control", "no-cache");
        return params;
    }

    private Response.ErrorListener getVolleyErrorListener(final ConsentResponseListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if (error != null) {
                    final NetworkResponse networkResponse = error.networkResponse;
                    try {
                        if (error instanceof NoConnectionError) {
                            listener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.NO_INTERNET_CONNECTION.getDescription(), ConsentError.ConsentErrorType.NO_INTERNET_CONNECTION.getId()));
                        } else if (error instanceof TimeoutError) {
                            listener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.TIME_OUT.getDescription(), ConsentError.ConsentErrorType.TIME_OUT.getId()));
                        } else if (error instanceof AuthFailureError) {
                            listener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.AUTHENTICATION_FAILURE.getDescription(), ConsentError.ConsentErrorType.AUTHENTICATION_FAILURE.getId()));
                        } else if (error instanceof NetworkError) {
                            listener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.NETWORK_ERROR.getDescription(), ConsentError.ConsentErrorType.NETWORK_ERROR.getId()));
                        } else if (error instanceof ParseError) {
                            listener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.PARSE_ERROR.getDescription(), ConsentError.ConsentErrorType.PARSE_ERROR.getId()));
                        } else if (networkResponse != null) {
                            listener.onResponseFailureConsent(new ConsentError(networkResponse.toString(), networkResponse.statusCode));
                        } else
                            listener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.UNKNOWN_EXCEPTION.getDescription(), ConsentError.ConsentErrorType.UNKNOWN_EXCEPTION.getId()));
                    } catch (Exception e) {
                        listener.onResponseFailureConsent(new ConsentError(ConsentError.ConsentErrorType.UNKNOWN_EXCEPTION.getDescription(), ConsentError.ConsentErrorType.UNKNOWN_EXCEPTION.getId()));
                    }
                }
            }
        };
    }

    private Response.Listener<JsonArray> getVolleyResponseListener(final ConsentResponseListener listener) {
        return new Response.Listener<JsonArray>() {
            @Override
            public void onResponse(final JsonArray response) {
                Type listType = new TypeToken<ArrayList<ConsentModel>>(){}.getType();
                final List<ConsentModel> consentModelList = new Gson().fromJson(response, listType);
                if (consentModelList != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResponseSuccessConsent(consentModelList);
                        }
                    });

                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResponseFailureConsent(new ConsentError("Null Response", 00));
                        }
                    });
                }
            }
        };
    }
}
