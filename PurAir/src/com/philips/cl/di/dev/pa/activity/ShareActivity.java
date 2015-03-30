package com.philips.cl.di.dev.pa.activity;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ShareActivity extends BaseActivity implements OnClickListener {
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.share_activity);
	    
	    ImageButton backButton = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		
		FontTextView heading=(FontTextView) findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.share));
		
		initPage();
		//showImagesavingProgressDialog(R.string.notification_permission_check_msg);
	
	}

	
	private void initPage() {
		findViewById(R.id.share_menu_2).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.heading_back_imgbtn:
			finish();
			break;
		case R.id.share_menu_2:
			openEmailApp(this, "","",new File(AppConstants.CACHEDIR_IMG + "PhilipsAir.png"));
			break;
		default:
			break;
		}
		
	}
	
	public static void openEmailApp(Context context,String emailAddress,String content,File file) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { emailAddress });
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, ""
				);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		intent.putExtra(android.content.Intent.EXTRA_STREAM, Uri 
				.fromFile(file)); 
		intent.setType("image/png"); 
		context.startActivity(Intent.createChooser(intent, "Mail Chooser"));
	}
	
	
	

}
