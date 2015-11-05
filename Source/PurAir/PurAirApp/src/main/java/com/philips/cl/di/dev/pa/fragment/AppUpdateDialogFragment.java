package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.AppUpdater;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class AppUpdateDialogFragment extends DialogFragment {

	public static AppUpdateDialogFragment newInstance() {
		AppUpdateDialogFragment fragment = new AppUpdateDialogFragment();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.cancel_wifi_setup, container,
				false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		MetricsTracker.trackPage(TrackPageConstants.APP_UPDATE_AVAILABLE);
		if (getActivity() == null) {
			return;
		}

		TextView tvHeader = (TextView) getView().findViewById(
				R.id.tv_cancel_wifi_setup_header);
		tvHeader.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		tvHeader.setText(getString(R.string.app_update)) ;
		TextView tvMessage = (TextView) getView().findViewById(
				R.id.tv_cancel_wifi_setup_message);
		
		tvMessage.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		tvMessage.setText(getString(R.string.app_update_msg));
		Button updateButton = (Button) getView().findViewById(
				R.id.btn_cancel_wifi_yes);
		updateButton.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		updateButton.setText(getString(R.string.update)) ;
		Button cancelButton = (Button) getView().findViewById(
				R.id.btn_cancel_wifi_no);
		cancelButton.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		cancelButton.setText(getString(R.string.cancel));
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUpdater appUpdater = AppUpdater.getInstance(getActivity().getApplicationContext());
				appUpdater.fetchComponents(AppConstants.COMPONENT_ID);
				dismiss();
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setCancelable(false);
	}
}
