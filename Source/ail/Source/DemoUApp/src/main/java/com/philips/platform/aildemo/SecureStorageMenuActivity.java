package com.philips.platform.aildemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

/**
 * Created by 310238114 on 8/26/2016.
 */
public class SecureStorageMenuActivity extends AppCompatActivity {

	TextView deviceCapable;
	TextView deviceLock;
	ToggleButton toggleButton;
	boolean isOldSecureStorageEnabled=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secure_storage_menu);
		AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
		SecureStorageInterface mSecureStorage = appInfra.getSecureStorage();

		deviceCapable = (TextView) findViewById(R.id.devicecapableStatus);
		deviceCapable.setText(mSecureStorage.getDeviceCapability());

		deviceLock = (TextView) findViewById(R.id.devicelockStatus);
		deviceLock.setText(Boolean.toString(mSecureStorage.deviceHasPasscode()));

		toggleButton=(ToggleButton)findViewById(R.id.toggleButton);
		toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				isOldSecureStorageEnabled=isChecked;
			}
		});

		Button secureStorageButton = (Button) findViewById(R.id.secureStorage);
		secureStorageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SecureStorageMenuActivity.this, SecureStorageActivity.class);
				intent.putExtra(Constants.IS_OLD_SS_ENABLED,isOldSecureStorageEnabled);
				startActivity(intent);

			}
		});
		Button encryptDecryptButton = (Button) findViewById(R.id.EncryptDecrypt);
		encryptDecryptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SecureStorageMenuActivity.this, SecureStorageEncryptDecryptActivity.class);
				i.putExtra(Constants.IS_OLD_SS_ENABLED,isOldSecureStorageEnabled);
				startActivity(i);
			}
		});
		Button passWordCreationButton = (Button) findViewById(R.id.createPaaasword);
		passWordCreationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SecureStorageMenuActivity.this, SecureStoragePasswordActivity.class);
				intent.putExtra(Constants.IS_OLD_SS_ENABLED,isOldSecureStorageEnabled);
				startActivity(intent);

			}
		});
	}

	public void onClick(View view) {
		AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
		if(view.getId() == R.id.isCodeTampered) {
			Toast.makeText(SecureStorageMenuActivity.this,String.valueOf(appInfra.getSecureStorage().isCodeTampered()),Toast.LENGTH_SHORT).show();
		} else if(view.getId() == R.id.isEmulator){
			Toast.makeText(SecureStorageMenuActivity.this,String.valueOf(appInfra.getSecureStorage().isEmulator()),Toast.LENGTH_SHORT).show();
		}
	}

}
