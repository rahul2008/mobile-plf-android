package com.philips.platform.aildemo;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.appupdate.AppUpdateInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

public class AppUpdateActivity extends AppCompatActivity {

	private Button appUpdateRefresh;
	private AppUpdateInterface appupdateInterface;
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
	private TextView tvMinimumOSversionMessage;
	private Button fetchappupdateValues;
	byte[] plainByte;
	byte[] encryptedByte;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appupdate);
		appUpdateRefresh = (Button) findViewById(R.id.appUpdateRefresh);
		fetchappupdateValues = (Button) findViewById(R.id.fetchappupdateValues);
		tvappversionval = (TextView) findViewById(R.id.tvappversionval);
		tvminversionval = (TextView) findViewById(R.id.tvminversionval);

		tvisDeprecated = (TextView) findViewById(R.id.tvisDeprecated);
		tvToBeDeprecatedDate = (TextView) findViewById(R.id.tvToBeDeprecatedDate);


		tvisToBeDeprecated = (TextView) findViewById(R.id.tvisToBeDeprecated);

		tvisUpdateAvailable = (TextView) findViewById(R.id.tvisUpdateAvailable);

		tvDeprecateMessage = (TextView) findViewById(R.id.tvDeprecateMessage);

		tvToBeDeprecatedMessage = (TextView) findViewById(R.id.tvToBeDeprecatedMessage);

		tvUpdateMessage = (TextView) findViewById(R.id.tvUpdateMessage);

		tvMinimumOSverion = (TextView) findViewById(R.id.tvMinimumOSverion);

		tvMinimumOSversionMessage = findViewById(R.id.tvMinimumOSversionMessage);

		appupdateInterface = AILDemouAppInterface.getInstance().getAppInfra().getAppUpdate();
		SecureStorageInterface mSecureStorage = AILDemouAppInterface.getInstance().getAppInfra().getSecureStorage();

		String enc = "4324332423432432432435425435435346465464547657567.000343242342";

		try {
			plainByte= enc.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}

		SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
		encryptedByte=mSecureStorage.encryptData(plainByte,sseStore);
		try {
			String encBytesString = new String(encryptedByte, "UTF-8");
			Log.e("Encrypted Data",encBytesString);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] plainData= mSecureStorage.decryptData(encryptedByte,sseStore);
		String  result = Arrays.equals(plainByte,plainData)?"True":"False";
		try {
			String decBytesString = new String(plainByte, "UTF-8");
			Log.e("Decrypted Data",decBytesString);
		} catch (UnsupportedEncodingException e) {
		}


		appUpdateRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(appupdateInterface == null) {
					Toast.makeText(AppUpdateActivity.this, "enable appUpdate.autoRefresh to true in AppConfig.json", Toast.LENGTH_LONG).show();
					return;
				}
				appupdateInterface.refresh(new AppUpdateInterface.OnRefreshListener() {
					@Override
					public void onError(AIAppUpdateRefreshResult error, String message) {
						Toast.makeText(AppUpdateActivity.this, message, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(AIAppUpdateRefreshResult result) {
						Toast.makeText(AppUpdateActivity.this, result.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});

		fetchappupdateValues.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(appupdateInterface == null) {
					Toast.makeText(AppUpdateActivity.this, "enable appUpdate.autoRefresh to true in AppConfig.json", Toast.LENGTH_LONG).show();
					return;
				}

				tvappversionval.setText(AILDemouAppInterface.getInstance().getAppInfra().getAppIdentity().getAppVersion());
				tvminversionval.setText(appupdateInterface.getMinimumVersion());
				tvisDeprecated.setText(String.valueOf(appupdateInterface.isDeprecated()));
				tvisToBeDeprecated.setText(String.valueOf(appupdateInterface.isToBeDeprecated()));
				tvisUpdateAvailable.setText(String.valueOf(appupdateInterface.isUpdateAvailable()));
				tvDeprecateMessage.setText(appupdateInterface.getDeprecateMessage());
				tvToBeDeprecatedMessage.setText(appupdateInterface.getToBeDeprecatedMessage());
				tvUpdateMessage.setText(appupdateInterface.getUpdateMessage());
				tvMinimumOSverion.setText(appupdateInterface.getMinimumOSverion());
				tvMinimumOSversionMessage.setText(appupdateInterface.getMinimumOSMessage());
				SimpleDateFormat formatter = new SimpleDateFormat(AppUpdateInterface.APPUPDATE_DATE_FORMAT
						, Locale.ENGLISH);
				if(appupdateInterface.getToBeDeprecatedDate() != null) {
					String s = formatter.format(appupdateInterface.getToBeDeprecatedDate());
					tvToBeDeprecatedDate.setText(s);
				} else {
					tvToBeDeprecatedDate.setText(null);
				}
			}
		});

	}
}
