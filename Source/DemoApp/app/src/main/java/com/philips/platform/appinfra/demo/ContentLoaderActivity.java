package com.philips.platform.appinfra.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.platform.appinfra.contentloader.ContentArticle;
import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;

/**
 * Created by 310238114 on 10/25/2016.
 */
public class ContentLoaderActivity extends AppCompatActivity {

    EditText EditTextServiceId;
    EditText EditTextMaxHour;
    EditText EditTextContentClass;
    EditText EditTextContentType;

    Button buttonInvokeCL;
    TextView textViewResponse;
    ContentLoader mContentLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_loader);
        EditTextServiceId = (EditText) findViewById(R.id.editTextServiceId);
        EditTextServiceId.setText("https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1"); //test
        EditTextMaxHour = (EditText) findViewById(R.id.editTextMaxAgeInHours);
        EditTextContentClass = (EditText) findViewById(R.id.editTextContentClassType);
        EditTextContentType = (EditText) findViewById(R.id.editTextContentType);
        buttonInvokeCL = (Button) findViewById(R.id.buttonInvokeCL);
        textViewResponse = (TextView) findViewById(R.id.textViewResponseCL);

        buttonInvokeCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewResponse.setText(null);
                int magAge = getMaxAge();
                if (magAge < 0) {
                    showAlertDialog("Invalid Input", "Invalid Max age ");
                } else if (null == EditTextServiceId.getText() || "".equals(EditTextServiceId.getText().toString().trim())) {
                    showAlertDialog("Invalid Input", "Invalid Service ID ");

                } else {
                    mContentLoader = new ContentLoader(EditTextServiceId.getText().toString().trim(), magAge, ContentArticle.class, "articles", AppInfraApplication.gAppInfra);

                    mContentLoader.refresh(new ContentLoaderInterface.OnRefreshListener() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            Log.i("CL onError:", "" + error);
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }

                        @Override
                        public void onSuccess(REFRESH_RESULT result) {
                            Log.i("CL onSuccess:", "" + result);
                            textViewResponse.setText(result.toString() +"\nPlease see response data with tag 'CL REFRSH RESP' in console log");
                        }
                    });
                }

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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ContentLoaderActivity.this);
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
