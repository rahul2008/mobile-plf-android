package com.philips.platform.aildemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.request.ImageRequest;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.rest.request.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 310238114 on 10/20/2016.
 */
public class RestClientServiceIdActivity extends AppCompatActivity {
    String[] requestTypeOption = {"GET", "POST", "PUT", "DELETE"};
    String[] requestDataOption = {"StringRequest", "jsonObjectRequest", "imageRequest"};
    // String url = "https://hashim.herokuapp.com/RCT/test.php?action=data&id=aa";
    //String baseURL= "https://www.oldchaphome.nl";
    HashMap<String, String> params;
    HashMap<String, String> headers;
    EditText serviceIDInput;
    EditText pathComponentInput;
    RestInterface mRestInterface;
    TextView mResponse;
    ImageView mImageView;
    TextView urlFired;
    private Spinner requestTypeSpinner;
    private Spinner requestDataSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_service_id);

        params = new HashMap<String, String>();
        headers = new HashMap<String, String>();
        mRestInterface = AILDemouAppInterface.getInstance().getAppInfra().getRestClient();
        //mRestInterface.setCacheLimit(2*1024*1023);// 1 MB cache
        serviceIDInput = (EditText) findViewById(R.id.editTextServiceID);
        //   serviceIDInput.setText("userreg.janrain.api");
        pathComponentInput = (EditText) findViewById(R.id.editTextPathComponent);
        mResponse = (TextView) findViewById(R.id.textViewResponse);
        mImageView = (ImageView) findViewById(R.id.responseImageId);


        final Button setHeaders = (Button) findViewById(R.id.buttonSetHeadersSID);
        setHeaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParamorheaderDialog(headers, "Enter Headers");
            }

        });

        Button setParams = (Button) findViewById(R.id.buttonSetParamsSID);
        setParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParamorheaderDialog(params, "Enter Params");
            }

        });


        Button invoke = (Button) findViewById(R.id.buttonInvokeSID);
        invoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResponse.setText(null);
                mImageView.setImageBitmap(null);
                int methodType = Request.Method.GET;

                if (headers != null) {
                    for (String key : headers.keySet()) {

                    }
                }

                if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("GET")) {
                    methodType = Request.Method.GET;

                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("POST")) {
                    methodType = Request.Method.POST;
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("PUT")) {
                    methodType = Request.Method.PUT;
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("DELETE")) {
                    methodType = Request.Method.DELETE;
                }
                StringRequest mStringRequest = null;


                if (requestDataSpinner.getSelectedItem().toString().trim().equalsIgnoreCase(requestDataOption[0])) { // string
                    try {
                        mStringRequest = new StringRequest(methodType,
                                serviceIDInput.getText().toString(), ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE,
                                getPathComponentString()
                                , new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG", "" + response);

                                showAlertDialog("Success Response", response);
                                clearParamsAndHeaders();// clear headerd and params from rest client

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("LOG", "" + error);

                                //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                                String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                                showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                                clearParamsAndHeaders();// clear headerd and params from rest client

                            }
                        });

                    } catch (Exception e) {
                        Log.e("LOG REST SD", e.toString());
                    }
                    if (mStringRequest.getCacheEntry() != null) {
                        String cachedResponse = new String(mStringRequest.getCacheEntry().data);
                        Log.i("CACHED DATA: ", "" + cachedResponse);
                    }
                    // mStringRequest.setShouldCache(false); // set false to disable cache

                    if (null != mStringRequest) {
                        //  urlFired.setText(mStringRequest.getUrl());

                        final StringRequest finalMStringRequest = mStringRequest;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mRestInterface.getRequestQueue().add(finalMStringRequest);
                            }
                        }).start();
                    }
                } else if (requestDataSpinner.getSelectedItem().toString().trim().equalsIgnoreCase(requestDataOption[1])) { //json
                    JsonObjectRequest jsonRequest = null;
                    try {
                        jsonRequest = new JsonObjectRequest(methodType,
                                serviceIDInput.getText().toString(), ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE,
                                getPathComponentString(), null
                                , new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LOG", "" + response);
                                showAlertDialog("Success Response", response.toString());
                                clearParamsAndHeaders();// clear headerd and params from rest client
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("LOG", "" + error);
                                //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                                String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                                showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                                clearParamsAndHeaders();// clear headerd and params from rest client
                            }
                        });
                    } catch (Exception e) {
                        Log.e("LOG REST SD", e.toString());
                    }
                    if (null != jsonRequest) {
                        //  urlFired.setText(mStringRequest.getUrl());
                        final JsonObjectRequest finalJsonRequest = jsonRequest;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mRestInterface.getRequestQueue().add(finalJsonRequest);
                            }
                        }).start();
                    }
                } else if (requestDataSpinner.getSelectedItem().toString().trim().equalsIgnoreCase(requestDataOption[2])) { //image
                    ImageRequest imageRequest = null;
                    try {
                        imageRequest = new ImageRequest(serviceIDInput.getText().toString(), ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE,
                                getPathComponentString(), new Response.Listener<Bitmap>() {

                            @Override
                            public void onResponse(Bitmap response) {
                                Log.i("LOG", "" + response);
                                showAlertDialog("Success Response", response.toString());
                                clearParamsAndHeaders();// clear headerd and params from rest client
                            }
                        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("LOG", "" + error);
                                String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                                clearParamsAndHeaders();// clear headerd and params from rest client

                                showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                            }
                        });
                    } catch (Exception e) {
                        Log.e("LOG REST SD", e.toString());
                    }
                    if (null != imageRequest) {
                        final ImageRequest finalImageRequest = imageRequest;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mRestInterface.getRequestQueue().add(finalImageRequest);
                            }
                        }).start();
                    }
                }
            }

        });

        requestTypeSpinner = (Spinner) findViewById(R.id.spinnerRequestType);

        ArrayAdapter<String> input_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, requestTypeOption);
        requestTypeSpinner.setAdapter(input_adapter);


        requestDataSpinner = (Spinner) findViewById(R.id.spinnerRequestData);

        ArrayAdapter<String> input_data_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, requestDataOption);
        requestDataSpinner.setAdapter(input_data_adapter);


    }

    void showParamorheaderDialog(final HashMap<String, String> keyValue, String title) {

        final Dialog dialog = new Dialog(RestClientServiceIdActivity.this);
        dialog.setContentView(R.layout.rest_client_input_param);
        dialog.setTitle(title);
        final TextView name = (TextView) dialog.findViewById(R.id.editTextParamName);
        final TextView value = (TextView) dialog.findViewById(R.id.editTextParamValue);
        Button dialogButton = (Button) dialog.findViewById(R.id.buttonSave);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == name.getText() || name.getText().toString().isEmpty() || null == value.getText() || value.getText().toString().isEmpty()) {
                    Toast.makeText(RestClientServiceIdActivity.this, "name or value incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    keyValue.put(name.getText().toString(), value.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    void showAlertDialog(String title, String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RestClientServiceIdActivity.this);
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

    // get path component to be appended at base url returned by SD
    String getPathComponentString() {
        String path = "";
        if (null != pathComponentInput.getText() && null != pathComponentInput.getText().toString()) {
            path = pathComponentInput.getText().toString().trim();
        }
        return path;
    }

    private void clearParamsAndHeaders() {
        headers.clear();
        params.clear();
    }
}
