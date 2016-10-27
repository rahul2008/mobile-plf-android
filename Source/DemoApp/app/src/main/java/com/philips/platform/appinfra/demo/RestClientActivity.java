package com.philips.platform.appinfra.demo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.TokenProviderInterface;
import com.philips.platform.appinfra.rest.request.AIStringRequest;
import com.philips.platform.appinfra.rest.request.HttpForbiddenException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310238114 on 9/1/2016.
 */
public class RestClientActivity extends AppCompatActivity {
    String[] requestTypeOption  ={"GET","POST","PUT","DELETE"};
   // String url = "https://hashim.herokuapp.com/RCT/test.php?action=data&id=aa";
   //String baseURL= "https://www.oldchaphome.nl";
    String baseURL= "https://hashim.herokuapp.com";

    String accessToken;
    private Spinner requestTypeSpinner;
     HashMap<String,String> params;
     HashMap<String,String> headers;
    EditText urlInput;
    EditText idInput;
    RestInterface mRestInterface;
    TextView loginStatus;
    TextView accessTokenTextView;
    TextView urlFired;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);
        params= new HashMap<String,String>();
        headers= new HashMap<String,String>();
        mRestInterface = AppInfraApplication.gAppInfra.getRestClient();
        //mRestInterface.setCacheLimit(2*1024*1023);// 1 MB cache
        urlInput= (EditText)findViewById(R.id.editTextURL);
        idInput= (EditText)findViewById(R.id.editTextID);
        loginStatus = (TextView) findViewById(R.id.textViewLogStatus);
        urlFired = (TextView) findViewById(R.id.textViewURLfired);
        accessTokenTextView= (TextView) findViewById(R.id.textViewAccessToken);
        urlInput.setText(baseURL);
        Button setHeaders = (Button)findViewById(R.id.buttonSetHeaders);
        setHeaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParamorheaderDialog(headers,"Enter Headers");
            }

        });

        Button setParams = (Button)findViewById(R.id.buttonSetParams);
        setParams.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             showParamorheaderDialog(params,"Enter Params");
                                         }

                                     });


        Button invoke = (Button)findViewById(R.id.buttonInvoke);
        invoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int methodType = Request.Method.GET;


                for(String  key: headers.keySet() ){

                }

                if(requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("GET")){
                    methodType = Request.Method.GET;

                }else if(requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("POST")){
                    methodType = Request.Method.POST;
                }else if(requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("PUT")){
                    methodType = Request.Method.PUT;
                }else if(requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("DELETE")){
                    methodType = Request.Method.DELETE;
                }

                if(requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("PUT")) {
                    AIStringRequest putRequest = null;
                    if(!mRestInterface.isValidURL(urlInput.getText().toString().trim())){ // if invalid url
                        showAlertDialog("URL Error","Invalid URL");
                        return ;
                    }
                    try {
                        if(!mRestInterface.isValidURL(urlInput.getText().toString().trim())){ // if invalid url
                            showAlertDialog("URL Error","Invalid URL");
                            return ;
                        }
                        putRequest = new AIStringRequest(Request.Method.PUT, urlInput.getText().toString().trim()+"/RCT/test.php?action=data&id="+idInput.getText().toString().trim(),
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.i("LOG", "" + response);
                                        //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
                                        showAlertDialog("Success Response",response);
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("LOG", "" + error);
                                        //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        String errorcode =   null!= error.networkResponse?   error.networkResponse.statusCode+"" : "";
                                        showAlertDialog("Volley Error ","Code:"+errorcode+ "\n Message:\n"+ error.toString());
                                    }
                                }


                        ) {

                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> paramList = new HashMap<String, String>();
                                for(String  key: params.keySet() ){
                                    paramList.put(key, params.get(key));
                                }
                               // paramList.put("name", "Alif");
                                return paramList;
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                int mStatusCode = response.statusCode;
                                return super.parseNetworkResponse(response);
                            }

                        };
                    } catch (HttpForbiddenException e) {
                        Log.i("LOG", "" + e.toString());
                        showAlertDialog("HttpForbiddenException",e.toString());
                    }
                    if(null!=putRequest) {
                        urlFired.setText(putRequest.getUrl());
                        mRestInterface.getRequestQueue().add(putRequest);
                    }
                }else{
                    if(!mRestInterface.isValidURL(urlInput.getText().toString().trim())){ // if invalid url
                        showAlertDialog("URL Error","Invalid URL");
                        return ;
                    }
                    AIStringRequest mStringRequest = null;
                    try {
                        mStringRequest = new AIStringRequest(methodType, urlInput.getText().toString().trim()+"/RCT/test.php?action=data&id="+idInput.getText().toString().trim(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG", "" + response);
                                //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
                                showAlertDialog("Success Response",response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("LOG", "" + error);

                                //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                                String errorcode =   null!= error.networkResponse?   error.networkResponse.statusCode+"" : "";
                                showAlertDialog("Volley Error ","Code:"+errorcode+ "\n Message:\n"+ error.toString());
                            }
                        });
                    } catch (HttpForbiddenException e) {
                        Log.i("LOG", "" + e.toString());
                        showAlertDialog("HttpForbiddenException",e.toString());
                    }
                    if(mStringRequest.getCacheEntry()!=null){
                        String cachedResponse = new String(mStringRequest.getCacheEntry().data);
                        Log.i("CACHED DATA: ", "" + cachedResponse);
                    }
                    // mStringRequest.setShouldCache(false); // set false to disable cache

                    if(null!=mStringRequest) {
                        urlFired.setText(mStringRequest.getUrl());
                        mRestInterface.getRequestQueue().add(mStringRequest);
                    }

                    }





            }

        });

        requestTypeSpinner =(Spinner)findViewById(R.id.spinnerRequestType);

        ArrayAdapter<String> input_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, requestTypeOption);
        requestTypeSpinner.setAdapter(input_adapter);

        requestTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // initializeDigitalCareLibrary();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button loginButton = (Button)findViewById(R.id.buttonLogin);
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mRestInterface.isValidURL(urlInput.getText().toString().trim()+"/RCT/test.php?action=authtoken")){ // if invalid url
                    showAlertDialog("URL Error","Invalid URL");
                    return ;
                }
                AIStringRequest mStringRequest = null;
                try {
                    mStringRequest = new AIStringRequest(Request.Method.GET, urlInput.getText().toString().trim()+"/RCT/test.php?action=authtoken", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG", "" + response);
                            //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
                            JSONObject jobj = null;
                            try {
                                jobj = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            accessToken = jobj.optString("access_token");
                            if(null!=accessToken){
                                loginStatus.setText("Logged In");
                                accessTokenTextView.setText(accessToken);
                            }
                            showAlertDialog("Success Response",response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("LOG", "" + error);
                            //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            String errorcode =   null!= error.networkResponse?   error.networkResponse.statusCode+"" : "";
                            showAlertDialog("Volley Error ","Code:"+errorcode+ "\n Message:\n"+ error.toString());
                        }
                    });
                } catch (HttpForbiddenException e) {
                    Log.i("LOG", "" + e.toString());
                    showAlertDialog("HttpForbiddenException",e.toString());
                }
                mStringRequest.setShouldCache(false); // set false to disable cache , by default its true
                if(null!=mStringRequest) {
                    urlFired.setText(mStringRequest.getUrl());
                    mRestInterface.getRequestQueue().add(mStringRequest);
                }

            }
        });

        Button autchCheckButton = (Button)findViewById(R.id.buttonCheck);
        assert autchCheckButton != null;
        autchCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mRestInterface.isValidURL(urlInput.getText().toString().trim()+"/RCT/test.php?action=authcheck")){ // if invalid url
                    showAlertDialog("URL Error","Invalid URL");
                    return ;
                }
                AIStringRequest mStringRequest = null;
                try {
                    mStringRequest = new AIStringRequest(Request.Method.GET, urlInput.getText().toString().trim()+"/RCT/test.php?action=authcheck",
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("LOG", "" + response);
                                    //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
                                    showAlertDialog("Success Response",response);
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("LOG", "" + error);
                                    //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    String errorcode =   null!= error.networkResponse?   error.networkResponse.statusCode+"" : "";
                                    showAlertDialog("Volley Error ","Code:"+errorcode+ "\n Message:\n"+ error.toString());
                                }
                            }


                    ) {

                        @Override
                        public Map<String, String> getHeaders()
                        {
                            TokenProviderInterface provider= new TokenProviderInterface() {
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
                            HashMap<String, String> header = mRestInterface.setTokenProvider(provider);
                            Map<String, String> paramList = new HashMap<String, String> ();
                           /* for(String  key: params.keySet() ){
                                paramList.put(key, params.get(key));
                            }*/
                            if(null!=header){
                                paramList.putAll(header);
                            }
                            // other header can be added here
                            return paramList;
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            int mStatusCode = response.statusCode;
                            return super.parseNetworkResponse(response);
                        }

                    };
                } catch (HttpForbiddenException e) {
                    Log.i("LOG", "" + e.toString());
                    showAlertDialog("HttpForbiddenException",e.toString());
                }
                if(null!=mStringRequest) {
                    urlFired.setText(mStringRequest.getUrl());
                    mRestInterface.getRequestQueue().add(mStringRequest);
                }
            }
        });

        Button logoutButton = (Button)findViewById(R.id.buttonLogout);
        assert logoutButton != null;
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessToken=null;
                loginStatus.setText("Not logged In");
                accessTokenTextView.setText(null);
            }
        });
    }

    void showParamorheaderDialog(final HashMap<String, String> keyValue,String title ){

        final Dialog dialog = new Dialog(RestClientActivity.this);
        dialog.setContentView(R.layout.rest_client_input_param);
        dialog.setTitle(title);
        final TextView name = (TextView) dialog.findViewById(R.id.editTextParamName);
        final TextView value = (TextView) dialog.findViewById(R.id.editTextParamValue);
        Button dialogButton = (Button) dialog.findViewById(R.id.buttonSave);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(null==name.getText() || name.getText().toString().isEmpty() || null==value.getText() || value.getText().toString().isEmpty()){
                  Toast.makeText(RestClientActivity.this, "name or value incorrect", Toast.LENGTH_SHORT).show();
              }else {
                  keyValue.put(name.getText().toString(), value.getText().toString());
                  dialog.dismiss();
              }
            }
        });

        dialog.show();
    }

    void showAlertDialog(String title, String msg){
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


}
