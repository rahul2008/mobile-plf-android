package com.philips.platform.aildemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.appinfra.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310238114 on 10/25/2016.
 */
public class ContentLoaderCreateActivity extends AppCompatActivity {

    static List<ContentLoader> ContentLoaderList;
    private final String[] modelList = {"ContentArticle", "BeardStyle", "Asset"};
    EditText EditTextServiceId;
    EditText EditTextMaxHour;
    EditText EditTextContentLoaderLimit;
  /*  EditText EditTextContentClass;
    EditText EditTextContentType;*/
    Class contentClass;
    String ContentType;
    Spinner spinnerModelType;
    Button createCL;
    Button existingCLs;
    ContentLoader mContentLoader;
    List<String> ContentLoaderServiceIdList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_loader_create);
        ContentLoaderList= new ArrayList<ContentLoader>();
        ContentLoaderServiceIdList= new ArrayList<String>();
        EditTextServiceId = (EditText) findViewById(R.id.editTextServiceId);
     //   EditTextServiceId.setText("https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1"); //test
        EditTextMaxHour = (EditText) findViewById(R.id.editTextMaxAgeInHours);
        EditTextContentLoaderLimit =  (EditText) findViewById(R.id.editTextContentLoaderLimit);
        spinnerModelType = (Spinner) findViewById(R.id.spinnerModelTypes);
        ArrayAdapter<String> input_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, modelList);
        spinnerModelType.setAdapter(input_adapter);
       // EditTextContentType.setText("articles");
        createCL = (Button) findViewById(R.id.createCL);
        existingCLs = (Button) findViewById(R.id.gotoCL);


        createCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int magAge = getMaxAge();
                int contentLoaderLimitOptional = getContentLoaderLimitOptional();
                if (magAge < 0) {
                    showAlertDialog("Invalid Input", "Invalid Max age ");
                } else if (null == EditTextServiceId.getText() || "".equals(EditTextServiceId.getText().toString().trim())) {
                    showAlertDialog("Invalid Input", "Invalid Service ID ");

                } else {

                    switch (spinnerModelType.getSelectedItem().toString().trim()) {
                        case "ContentArticle":
                            contentClass=ContentArticle.class;
                            ContentType="articles";
                            break;
                        case "BeardStyle":
                            contentClass= BeardStyle.class;
                            ContentType="beardStyles";
                            break;
                        case "Asset":
                            contentClass= Asset.class;
                            ContentType="assets";
                            break;

                    }
                    ContentLoader mContentLoader = new ContentLoader(getApplicationContext(), EditTextServiceId.getText().toString().trim(), magAge, contentClass, ContentType, AILDemouAppInterface.getInstance().getAppInfra(), contentLoaderLimitOptional);

                    if(!ContentLoaderServiceIdList.isEmpty() && ContentLoaderServiceIdList.contains(EditTextServiceId.getText().toString().trim()))
                    {
                        showAlertDialog("Duplicate Service ID","Given Service ID already available, please use different Service ID to create Content Loader");
                    }
                    else {
                        ContentLoaderServiceIdList.add(mContentLoader.getmServiceId());
                        ContentLoaderList.add(mContentLoader);
                        Intent i = new Intent(ContentLoaderCreateActivity.this, com.philips.platform.aildemo.ContentLoaderList.class);
                        startActivity(i);
                    }
                }
            }
        });

        existingCLs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContentLoaderCreateActivity.this, com.philips.platform.aildemo.ContentLoaderList.class);
                startActivity(i);
            }
        });


    }

    int getMaxAge() {
        int res = -1;
        if (null != EditTextMaxHour.getText() && !"".equals(EditTextMaxHour.getText().toString().trim())) {
            res = Integer.parseInt(EditTextMaxHour.getText().toString().trim());
        }
        return res;
    }

    int getContentLoaderLimitOptional(){
        int res = 0;
        if (null != EditTextContentLoaderLimit.getText() && !"".equals(EditTextContentLoaderLimit.getText().toString().trim())) {
            res = Integer.parseInt(EditTextContentLoaderLimit.getText().toString().trim());
        }
        return res;
    }

    void showAlertDialog(String title, String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ContentLoaderCreateActivity.this);
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
