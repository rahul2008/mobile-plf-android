package com.philips.platform.aildemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.TokenProviderInterface;
import com.philips.platform.appinfra.rest.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RestClientActivity extends AppCompatActivity {
    String[] requestTypeOption = {"GET", "POST", "PUT", "DELETE"};
    // String url = "https://hashim.herokuapp.com/RCT/test.php?action=data&id=aa";
    //String baseURL= "https://www.oldchaphome.nl";
    String baseURL = "https://";

    String accessToken;
    private Spinner requestTypeSpinner;
    HashMap<String, String> params;
    HashMap<String, String> headers;
    EditText urlInput;
    RestInterface mRestInterface;
    TextView loginStatus;
    TextView accessTokenTextView;
    TextView urlFired;
    private Button invokeButton;
    private Button clearCacheButton;
    private Button loginButton;
    private Button authCheckButton;
    private Button logoutButton;
    private Button setHeaders;
    private Button setParams;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);
        params = new HashMap<>();
        headers = new HashMap<>();
        mRestInterface = AILDemouAppInterface.getInstance().getAppInfra().getRestClient();
        //mRestInterface.setCacheLimit(2*1024*1023);// 1 MB cache
        initViews();
        urlInput.setText(baseURL);

        ArrayAdapter<String> input_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, requestTypeOption);
        requestTypeSpinner.setAdapter(input_adapter);

        setHeaders.setOnClickListener(onClickSetHeaders());

        setParams.setOnClickListener(onClickSetParams());

        invokeButton.setOnClickListener(onClickInvoke(invokeButton));

        loginButton.setOnClickListener(onClickLogin(loginButton));

        authCheckButton.setOnClickListener(onClickAuthCheckButton(authCheckButton));

        logoutButton.setOnClickListener(onClickLogOut());

        clearCacheButton.setOnClickListener(view -> mRestInterface.clearCacheResponse());
    }

    @NonNull
    private View.OnClickListener onClickSetParams() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParamorheaderDialog(params, "Enter Params");
            }

        };
    }

    @NonNull
    private View.OnClickListener onClickSetHeaders() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParamorheaderDialog(headers, "Enter Headers");
            }

        };
    }

    @NonNull
    private View.OnClickListener onClickInvoke(final Button invokeButton) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableProgressBar();
                invokeButton.setEnabled(false);
                int methodType = Request.Method.GET;
                methodType = getMethodType(methodType);

                if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("PUT")) {
                    invokingWithPutRequest(invokeButton);
                } else {
                    invokeWithOtherRequest(methodType, invokeButton);
                }
            }
        };
    }

    private void invokeWithOtherRequest(final int methodType, final Button invokeButton) {
        StringRequest mStringRequest = null;
        try {
            mStringRequest = new StringRequest(methodType, urlInput.getText().toString().trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("appinfra "," success response ");
                    hideProgressBar();
                    invokeButton.setEnabled(true);
                    showAlertDialog("Success Response", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("appinfra "," error response ");
                    hideProgressBar();
                    invokeButton.setEnabled(true);
                    String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                    showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                }
            }, null, null, null)

            {
                @Override
                protected Map<String, String> getParams() {
                    Log.d("appinfra "," get params ");
                    Map<String, String> paramList = new HashMap<>();
                    for (String key : params.keySet()) {
                        paramList.put(key, params.get(key));
                    }
                    // paramList.put("name", "Alif");
                    return paramList;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    Log.d("appinfra "," parseNetworkResponse ");
                    if (response != null && response.data != null) {
                        if (response.headers == null) {
                            response = new NetworkResponse(
                                    response.statusCode,
                                    response.data,
                                    Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                    response.notModified,
                                    response.networkTimeMs);
                        }
                        return super.parseNetworkResponse(response);
                    } else {
                        return Response.error(new VolleyError("Response is null"));
                    }
                }
            };
        } catch (Exception e) {
            showAlertDialog("HttpForbiddenException", e.toString());
        }
        if (mStringRequest != null && mStringRequest.getCacheEntry() != null) {
            String cachedResponse = new String(mStringRequest.getCacheEntry().data);
            Log.i("appinfra CACHED DATA: ", "" + cachedResponse);
        }
        // mStringRequest.setShouldCache(false); // set false to disable cache
        if (mStringRequest != null) {
            String text = mStringRequest.getUrl() != null ? mStringRequest.getUrl() : "";
            urlFired.setText(text);
            final StringRequest finalMStringRequest = mStringRequest;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mRestInterface.getRequestQueue().add(finalMStringRequest);
                }
            }).start();

        }
    }

    private void invokingWithPutRequest(final Button invokeButton) {
        StringRequest putRequest = null;
        try {
            putRequest = new StringRequest(Request.Method.PUT, urlInput.getText().toString().trim(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hideProgressBar();
                            invokeButton.setEnabled(true);
//                            Log.i("LOG", "" + response);
                            params.clear();
                            showAlertDialog("Success Response", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Log.i("LOG", "" + error);
                            hideProgressBar();
                            invokeButton.setEnabled(true);
                            String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                            showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                        }
                    }, null, null, null
            ) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramList = new HashMap<String, String>();
                    for (String key : params.keySet()) {
                        paramList.put(key, params.get(key));
                    }
                    // paramList.put("name", "Alif");
                    return paramList;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null && response.data != null) {
                        if(response.headers == null) {
                            response = new NetworkResponse(
                                    response.statusCode,
                                    response.data,
                                    Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                    response.notModified,
                                    response.networkTimeMs);
                        }
                        return super.parseNetworkResponse(response);
                    } else {
                        return Response.error(new VolleyError("Response is null"));
                    }
                }

            };
        } catch (Exception e) {
            showAlertDialog("HttpForbiddenException", e.toString());
        }
        if (null != putRequest) {
            urlFired.setText(putRequest.getUrl());

            final StringRequest finalPutRequest = putRequest;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mRestInterface.getRequestQueue().add(finalPutRequest);
                }
            }).start();


        }
    }

    @NonNull
    private View.OnClickListener onClickLogOut() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessToken = null;
                loginStatus.setText("Not logged In");
                accessTokenTextView.setText(null);
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickAuthCheckButton(final Button authCheckButton) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableProgressBar();
                authCheckButton.setEnabled(false);
                TokenProviderInterface provider = new TokenProviderInterface() {
                    @Override
                    public Token getToken() {
                        return new Token() {
                            @Override
                            public TokenType getTokenType() {
                                return TokenType.OAUTH2;
                            }

                            @Override
                            public String getTokenValue() {
                                return accessToken;
                            }
                        };
                    }
                };
                Map<String, String> header = new HashMap<>();
                header.put("userName", "test");

                StringRequest mStringRequest = null;
                try {
                    mStringRequest = new StringRequest(Request.Method.GET, "https://hashim.herokuapp.com/RCT/test.php?action=authcheck"
                            ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    hideProgressBar();
                                    authCheckButton.setEnabled(true);
                                    //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
                                    showAlertDialog("Success Response", response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    hideProgressBar();
                                    authCheckButton.setEnabled(true);
                                    //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                                    showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                                }
                            }, header, null, provider
                    ) {

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            if (response != null && response.data != null) {
                                if(response.headers == null) {
                                    response = new NetworkResponse(
                                            response.statusCode,
                                            response.data,
                                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                            response.notModified,
                                            response.networkTimeMs);
                                }
                                return super.parseNetworkResponse(response);
                            } else {
                                return Response.error(new VolleyError("Response is null"));
                            }
                        }
                    };
                } catch (Exception e) {
                    showAlertDialog("HttpForbiddenException", e.toString());
                }
                if (null != mStringRequest) {
                    urlFired.setText(mStringRequest.getUrl());
                    final StringRequest finalMStringRequest = mStringRequest;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mRestInterface.getRequestQueue().add(finalMStringRequest);
                        }
                    }).start();
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickLogin(final Button loginButton) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableProgressBar();
                loginButton.setEnabled(false);
                StringRequest mStringRequest = null;
                try {
                    mStringRequest = new StringRequest(Request.Method.GET, "https://hashim.herokuapp.com/RCT/test.php?action=authtoken", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hideProgressBar();
                            loginButton.setEnabled(true);
                            //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
                            JSONObject jobj = null;
                            try {
                                jobj = new JSONObject(response);
                            } catch (JSONException e) {
                                Log.e(getClass() + "", "error parsing json");
                            }
                            accessToken = jobj.optString("access_token");
                            if (null != accessToken) {
                                loginStatus.setText("Logged In");
                                accessTokenTextView.setText(accessToken);
                            }
                            showAlertDialog("Success Response", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            loginButton.setEnabled(true);
                            //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                            showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                        }
                    }, null, null, null) {
                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            if (response != null && response.data != null) {
                                if(response.headers == null) {
                                    response = new NetworkResponse(
                                            response.statusCode,
                                            response.data,
                                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                            response.notModified,
                                            response.networkTimeMs);
                                }
                                return super.parseNetworkResponse(response);
                            } else {
                                return Response.error(new VolleyError("Response is null"));
                            }
                        }
                    };
                } catch (Exception e) {
                    Log.i("LOG", "" + " error while making string request");
                    showAlertDialog("HttpForbiddenException", e.toString());
                }
                if (null != mStringRequest) {
                    mStringRequest.setShouldCache(false); // set false to disable cache , by default its true
                    urlFired.setText(mStringRequest.getUrl());
                    final StringRequest finalMStringRequest = mStringRequest;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mRestInterface.getRequestQueue().add(finalMStringRequest);
                        }
                    }).start();

                }

            }
        };
    }

    private int getMethodType(int methodType) {
        if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("GET")) {
            methodType = Request.Method.GET;
        } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("POST")) {
            methodType = Request.Method.POST;
        } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("PUT")) {
            methodType = Request.Method.PUT;
        } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("DELETE")) {
            methodType = Request.Method.DELETE;
        }
        return methodType;
    }

    private void initViews() {
        urlInput = (EditText) findViewById(R.id.editTextURL);
        loginStatus = (TextView) findViewById(R.id.textViewLogStatus);
        urlFired = (TextView) findViewById(R.id.textViewURLfired);
        accessTokenTextView = (TextView) findViewById(R.id.textViewAccessToken);
        requestTypeSpinner = (Spinner) findViewById(R.id.spinnerRequestType);
        invokeButton = (Button) findViewById(R.id.buttonInvoke);
        clearCacheButton = (Button) findViewById(R.id.buttonClearCache);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        authCheckButton = (Button) findViewById(R.id.buttonCheck);
        logoutButton = (Button) findViewById(R.id.buttonLogout);
        setHeaders = (Button) findViewById(R.id.buttonSetHeaders);
        setParams = (Button) findViewById(R.id.buttonSetParams);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    void showParamorheaderDialog(final HashMap<String, String> keyValue, final String title) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(RestClientActivity.this);
                dialog.setContentView(R.layout.rest_client_input_param);
                dialog.setTitle(title);
                final TextView name = (TextView) dialog.findViewById(R.id.editTextParamName);
                final TextView value = (TextView) dialog.findViewById(R.id.editTextParamValue);
                Button dialogButton = (Button) dialog.findViewById(R.id.buttonSave);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == name.getText() || name.getText().toString().isEmpty() || null == value.getText() || value.getText().toString().isEmpty()) {
                            Toast.makeText(RestClientActivity.this, "name or value incorrect", Toast.LENGTH_SHORT).show();
                        } else {
                            keyValue.put(name.getText().toString(), value.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    void showAlertDialog(final String title, final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(RestClientActivity.this);
                builder1.setTitle(title);
                builder1.setMessage(msg);
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder1.setNegativeButton(null);

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    private void enableProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
