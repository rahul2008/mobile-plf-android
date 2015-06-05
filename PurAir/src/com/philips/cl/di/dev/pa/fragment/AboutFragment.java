package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AboutFragment extends BaseFragment implements OnClickListener {

	private FontTextView appNameTV, appVersionTV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage("About");
		initViews();
		setAppInformation();
	} 

	private void initViews() {
		ImageButton closeButton = (ImageButton) getView().findViewById(R.id.heading_close_imgbtn);
		closeButton.setVisibility(View.VISIBLE);
		closeButton.setOnClickListener(this);
		FontTextView heading=(FontTextView) getView().findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.about));
		appNameTV = (FontTextView) getView().findViewById(R.id.about_app_name_tv);
		appVersionTV = (FontTextView) getView().findViewById(R.id.about_app_version_tv);
		FontTextView termsAndcondition = (FontTextView) getView().findViewById(R.id.terms_and_conditions);
		String angle_arrow_txt = "<big><b><font face=\"CentraleSans-Bold\"> ›</font></b></big>";
		String termConditionTxt=getString(R.string.terms_and_conditions)+", " + getString(R.string.eula) + ", " + getString(R.string.privacy_policy) + angle_arrow_txt;
		termsAndcondition.setText(Html.fromHtml(termConditionTxt));
		
		termsAndcondition.setOnClickListener(this);
		
	} 

	private void setAppInformation() {
		appNameTV.setText(getString(R.string.app_name));
		appVersionTV.setText(getString(R.string.version_number) + " " + Utils.getVersionNumber());
	}

	@Override
	public void onClick(View view) {
		MainActivity mainActivity = (MainActivity) getActivity();
		if (mainActivity == null) return;
		switch (view.getId()) {
		case R.id.terms_and_conditions:
			mainActivity.showFragment(new TermsAndConditionsFragment());
			break;
		case R.id.heading_close_imgbtn:
			mainActivity.onBackPressed();
			break;

		default:
			break;
		}

	}

}
