package com.philips.cl.di.dev.pa.fragment;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
		appNameTV = (FontTextView) getView().findViewById(R.id.about_app_name_tv);
		appVersionTV = (FontTextView) getView().findViewById(R.id.about_app_version_tv);
		FontTextView settingTV = (FontTextView) getView().findViewById(R.id.about_setting_tv);
		FontTextView helpTV = (FontTextView) getView().findViewById(R.id.about_help_tv);
		
		settingTV.setText(getString(R.string.list_item_settings) + " ->");
		helpTV.setText(getString(R.string.help) + " ->");
		
		ImageButton closeButton = (ImageButton) getView().findViewById(R.id.about_close_img_btn);
		
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
		switch (view.getId()) {
		case R.id.about_setting_tv:
			if (mainActivity != null) {
				mainActivity.showFragment(new SettingsFragment());
			}
			break;
		case R.id.about_help_tv:
			if (mainActivity != null) {
				mainActivity.showFragment(new HelpAndDocFragment());
			}
			break;
		case R.id.about_close_img_btn:
			
			break;
		default:
			break;
		}
		
	}

}
