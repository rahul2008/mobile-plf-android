package com.philips.platform.appinfra.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310238114 on 10/25/2016.
 */
public class ContentLoaderCreateActivity extends AppCompatActivity {

    EditText EditTextServiceId;
    EditText EditTextMaxHour;
    EditText EditTextContentClass;
    EditText EditTextContentType;


    Button createCL;
    Button existingCLs;


    ContentLoader mContentLoader;
    static List<ContentLoader> ContentLoaderList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_loader_create);
        ContentLoaderList= new ArrayList<ContentLoader>();
        EditTextServiceId = (EditText) findViewById(R.id.editTextServiceId);
        EditTextServiceId.setText("https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1"); //test
        EditTextMaxHour = (EditText) findViewById(R.id.editTextMaxAgeInHours);
        EditTextContentClass = (EditText) findViewById(R.id.editTextContentClassType);
        EditTextContentType = (EditText) findViewById(R.id.editTextContentType);
        EditTextContentType.setText("articles");
        createCL = (Button) findViewById(R.id.createCL);
        existingCLs = (Button) findViewById(R.id.gotoCL);


        createCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int magAge = getMaxAge();
                if (magAge < 0) {
                    showAlertDialog("Invalid Input", "Invalid Max age ");
                } else if (null == EditTextServiceId.getText() || "".equals(EditTextServiceId.getText().toString().trim())) {
                    showAlertDialog("Invalid Input", "Invalid Service ID ");

                } else {
                    ContentLoader  mContentLoader = new ContentLoader(getApplicationContext(), EditTextServiceId.getText().toString().trim(), magAge, ContentArticle.class, EditTextContentType.getText().toString().trim(), AppInfraApplication.gAppInfra);
                    ContentLoaderList.add(mContentLoader);
                    Intent i = new Intent(ContentLoaderCreateActivity.this,ContentLoaderList.class);
                    startActivity(i);
                }
            }
        });

        existingCLs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContentLoaderCreateActivity.this,ContentLoaderList.class);
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
