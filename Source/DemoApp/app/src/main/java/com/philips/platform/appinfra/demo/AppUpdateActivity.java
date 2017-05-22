package com.philips.platform.appinfra.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.appupdate.AppupdateInterface;

public class AppUpdateActivity extends AppCompatActivity {

	private Button appUpdateRefresh;
	private AppupdateInterface appupdateInterface;
	private TextView tvappversionval;
	private TextView tvminversionval;
	private TextView tvToBeDeprecatedDate;
	private TextView tvisDeprecated;
	private TextView tvisToBeDeprecated;
	private TextView tvisUpdateAvailable;
	private TextView tvDeprecateMessage;
	private TextView tvToBeDeprecatedMessage;
	private TextView tvUpdateMessage;
	private TextView tvMinimumOSverion;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appupdate);
		appUpdateRefresh = (Button) findViewById(R.id.appUpdateRefresh);
		tvappversionval = (TextView) findViewById(R.id.tvappversionval);
		tvminversionval = (TextView)findViewById(R.id.tvminversionval);

		tvisDeprecated = (TextView) findViewById(R.id.tvisDeprecated);
		tvToBeDeprecatedDate = (TextView) findViewById(R.id.tvToBeDeprecatedDate);


		tvisToBeDeprecated = (TextView) findViewById(R.id.tvisToBeDeprecated);

		tvisUpdateAvailable = (TextView) findViewById(R.id.tvisToBeDeprecated);

		tvDeprecateMessage = (TextView) findViewById(R.id.tvDeprecateMessage);

		tvToBeDeprecatedMessage = (TextView) findViewById(R.id.tvToBeDeprecatedMessage);

		tvUpdateMessage = (TextView) findViewById(R.id.tvUpdateMessage);

		tvMinimumOSverion = (TextView) findViewById(R.id.tvMinimumOSverion);


		appupdateInterface = AppInfraApplication.gAppInfra.getAppupdate();

		appUpdateRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				appupdateInterface.refresh(new AppupdateInterface.OnRefreshListener() {
					@Override
					public void onError(AIAppUpdateRefreshResult error, String message) {
						Toast.makeText(AppUpdateActivity.this, error.toString(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(AIAppUpdateRefreshResult result) {
						Toast.makeText(AppUpdateActivity.this, result.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});

		tvappversionval.setText(AppInfraApplication.gAppInfra.getAppIdentity().getAppVersion());
		tvminversionval.setText(appupdateInterface.getMinimumVersion());
		tvisDeprecated.setText(String.valueOf(appupdateInterface.isDeprecated()));
		tvisToBeDeprecated.setText(String.valueOf(appupdateInterface.isToBeDeprecated()));
		tvisUpdateAvailable.setText(String.valueOf(appupdateInterface.isUpdateAvailable()));
		tvDeprecateMessage.setText(appupdateInterface.getDeprecateMessage());
		tvToBeDeprecatedMessage.setText(appupdateInterface.getToBeDeprecatedMessage());
		tvUpdateMessage.setText(appupdateInterface.getUpdateMessage());
		tvMinimumOSverion.setText(appupdateInterface.getMinimumOSverion());
		tvToBeDeprecatedDate.setText(appupdateInterface.getToBeDeprecatedDate());
	}
}
