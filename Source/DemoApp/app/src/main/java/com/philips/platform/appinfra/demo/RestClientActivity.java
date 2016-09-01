package com.philips.platform.appinfra.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by 310238114 on 9/1/2016.
 */
public class RestClientActivity extends AppCompatActivity {
    String[] requestTypeOption  ={"GET","POST","PUT","DELETE"};
    private Spinner requestTypeSpinner;
     HashMap<String,String> params;
     HashMap<String,String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);
        params= new HashMap<String,String>();
        headers= new HashMap<String,String>();

        EditText urlInput = (EditText)findViewById(R.id.editTextURL);
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

                for(String  key: params.keySet() ){

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
}
