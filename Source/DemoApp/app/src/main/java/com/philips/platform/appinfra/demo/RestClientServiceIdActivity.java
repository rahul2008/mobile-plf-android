package com.philips.platform.appinfra.demo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.request.HttpForbiddenException;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.rest.request.StringRequest;

import java.util.HashMap;

/**
 * Created by 310238114 on 10/20/2016.
 */
public class RestClientServiceIdActivity extends AppCompatActivity {
    String[] requestTypeOption = {"GET", "POST", "PUT", "DELETE"};
    String[] requestDataOption = {"StringRequest", "jsonObjectRequest", "imageRequest"};
    // String url = "https://hashim.herokuapp.com/RCT/test.php?action=data&id=aa";
    //String baseURL= "https://www.oldchaphome.nl";
    String serviceIdString;

    private Spinner requestTypeSpinner;
    private Spinner requestDataSpinner;
    HashMap<String, String> params;
    HashMap<String, String> headers;
    EditText serviceIDInput;
    EditText pathComponentInput;
    RestInterface mRestInterface;
    TextView mResponse;
    ImageView mImageView;

    TextView urlFired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_service_id);

        params = new HashMap<String, String>();
        headers = new HashMap<String, String>();
        mRestInterface = AppInfraApplication.gAppInfra.getRestClient();
        //mRestInterface.setCacheLimit(2*1024*1023);// 1 MB cache
        serviceIDInput = (EditText) findViewById(R.id.editTextServiceID);
        serviceIDInput.setText("userreg.janrain.api");
        pathComponentInput = (EditText) findViewById(R.id.editTextPathComponent);
        mResponse = (TextView) findViewById(R.id.textViewResponse);
        mImageView = (ImageView) findViewById(R.id.responseImageId);
        serviceIdString = serviceIDInput.getText().toString();


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
                                serviceIdString, ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE,
                                getPathComponentString()
                                , new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG", "" + response);
                                //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
                                showAlertDialog("Success Response", response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("LOG", "" + error);

                                //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                                String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
                                showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
                            }
                        });

                    } catch (HttpForbiddenException e) {
                        Log.e("LOG REST SD", e.toString());
                        e.printStackTrace();
                    }
                    if (mStringRequest.getCacheEntry() != null) {
                        String cachedResponse = new String(mStringRequest.getCacheEntry().data);
                        Log.i("CACHED DATA: ", "" + cachedResponse);
                    }
                    // mStringRequest.setShouldCache(false); // set false to disable cache

                    if (null != mStringRequest) {
                        //  urlFired.setText(mStringRequest.getUrl());
                        mRestInterface.getRequestQueue().add(mStringRequest);
                    }
                } else if (requestDataSpinner.getSelectedItem().toString().trim().equalsIgnoreCase(requestDataOption[1])) { //json
//                    JsonObjectRequest jsonObjectRequest;
//                    try {
//
//                        jsonObjectRequest = new JsonObjectRequest(methodType,
//                                serviceIdString, ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE,
//                                getPathComponentString()
//                                , new Response.Listener<JsonObject>() {
//                            @Override
//                            public void onResponse(JsonObject response) {
//                                Log.i("LOG", "" + response);
//                                //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
//                                showAlertDialog("Success Response", response.toString());
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.i("LOG", "" + error);
//
//                                //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//                                String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
//                                showAlertDialog("Volley Error ", "Code:" + errorcode + "\n Message:\n" + error.toString());
//                            }
//                        });
//
//                    } catch (HttpForbiddenException exception) {
//
//                    }
//

                } else if (requestDataSpinner.getSelectedItem().toString().trim().equalsIgnoreCase(requestDataOption[2])) { //image


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
