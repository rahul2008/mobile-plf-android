package com.philips.platform.appinfra.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;

public class LanguagePackActivity extends AppCompatActivity {

    private  LanguagePackInterface mLanguagePack;
    private  AppInfraInterface mAppInfra;
    private TextView overviewfileStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_pack);
        mLanguagePack = AppInfraApplication.gAppInfra.getLanguagePack();
        overviewfileStatus= (TextView) findViewById(R.id.overviewfileStatus);

        Button languagePackRefresh = (Button) findViewById(R.id.OverviewfileRefresh);
        languagePackRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overviewfileStatus.setText(null);
                mLanguagePack.refresh(new LanguagePackInterface.OnRefreshListener() {
                    @Override
                    public void onError(AILPRefreshResult error, String message) {
                        Log.e("LPactivity","error "+message);
                        overviewfileStatus.setText(error.toString() +"       "+message);
                    }

                    @Override
                    public void onSuccess(AILPRefreshResult result) {
                        Log.i("LPactivity","overview D/L success:  "+result);
                        overviewfileStatus.setText(result.toString());
                    }
                });

            }
        });
    }
}
