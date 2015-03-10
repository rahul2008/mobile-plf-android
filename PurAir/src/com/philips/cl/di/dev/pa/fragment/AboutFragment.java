package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AboutFragment extends BaseFragment implements OnClickListener {
	
	private FontTextView appNameTV, appVersionTV;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		setAppInformation();
		super.onActivityCreated(savedInstanceState);
	}
	
	private void initView() {
		ImageButton closeButton = (ImageButton) getView().findViewById(R.id.heading_close_imgbtn);
		closeButton.setVisibility(View.VISIBLE);
		FontTextView heading=(FontTextView) getView().findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.about));
		appNameTV = (FontTextView) getView().findViewById(R.id.about_app_name_tv);
		appVersionTV = (FontTextView) getView().findViewById(R.id.about_app_version_tv);
		FontTextView settingTV = (FontTextView) getView().findViewById(R.id.about_setting_tv);
		FontTextView helpTV = (FontTextView) getView().findViewById(R.id.about_help_tv);
		
		settingTV.setText(getString(R.string.list_item_settings) + " >");
		helpTV.setText(getString(R.string.help) + " >");
		
		settingTV.setOnClickListener(this);
		helpTV.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		
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
		case R.id.about_setting_tv:
			mainActivity.showFragment(new SettingsFragment());
			break;
		case R.id.about_help_tv:
			mainActivity.showFragment(new HelpAndDocFragment());
			break;
		case R.id.heading_close_imgbtn:
			mainActivity.showFirstFragment();
			break;
		default:
			break;
		}

	}

}
