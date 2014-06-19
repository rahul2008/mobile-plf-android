package com.philips.cl.di.dev.pa.ews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EWSSupportFragment extends Fragment {
	
	private ButtonClickListener buttonClickListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buttonClickListener = new ButtonClickListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_philips_support, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		EWSActivity ewsActivity = (EWSActivity) getActivity();
		
		if (ewsActivity.getApModeFailCounter() > 2 
				|| ewsActivity.getStep2FailCounter() > 2
				|| ewsActivity.getPowerOnFailCounter() > 2) {
			((FontTextView) getView().findViewById(
					R.id.contact_philips_support_message1)).setText(getActivity().getString(R.string.contact_philips_support_msg1_3attempt));
		}
		
		((EWSActivity) getActivity()).setActionBarHeading(EWSConstant.EWS_STEP_SUPPORT);
		
		((RelativeLayout) getView().findViewById
				(R.id.contact_support_phone_layout)).setOnClickListener(buttonClickListener);  
		((RelativeLayout) getView().findViewById
				(R.id.contact_support_email_layout)).setOnClickListener(buttonClickListener);  						
		((RelativeLayout)getView().findViewById
				(R.id.contact_support_website_layout)).setOnClickListener(buttonClickListener); 
	}
	
	private class ButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.contact_support_phone_layout:
				Intent dialSupportIntent = new Intent(Intent.ACTION_DIAL);
				dialSupportIntent.setData(Uri.parse("tel:" + getString(R.string.contact_philips_support_phone_num)));
				startActivity(Intent.createChooser(dialSupportIntent, "Air Purifier support"));
				break;
			case R.id.contact_support_email_layout:
				Intent supportEmailIntent = new Intent(
						Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.contact_philips_support_email), null));
				supportEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
				supportEmailIntent.putExtra(Intent.EXTRA_TEXT, "No template");
				startActivity(Intent.createChooser(supportEmailIntent, "Air Purifier support"));
				break;
			case R.id.contact_support_website_layout:
				Intent gotoSupportWebisteIntent = new Intent(Intent.ACTION_VIEW);
				gotoSupportWebisteIntent.setData(Uri.parse("http://" + getString(R.string.contact_philips_support_website)));
				startActivity(gotoSupportWebisteIntent);
				break;
			default:
				ALog.i(ALog.EWS, "Default...");
				break;
			}
		}
	}
	
}
