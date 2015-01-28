package com.philips.cl.di.dev.pa.ews;

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
import com.philips.cl.di.dev.pa.demo.DemoModeActivity;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;

public class SetupCancelDialogFragment extends DialogFragment {

	public static SetupCancelDialogFragment newInstance() {
		SetupCancelDialogFragment fragment = new SetupCancelDialogFragment();

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
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		if (getActivity() == null) {
			return;
		}

        MetricsTracker.trackActionInAppNotification(getString(R.string.cancel_demo_setup_title));
		TextView tvHeader = (TextView) getView().findViewById(
				R.id.tv_cancel_wifi_setup_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(getActivity()));
		if (getActivity() instanceof DemoModeActivity) {
			tvHeader.setText(getString(R.string.cancel_demo_setup_title));
		}
		TextView tvMessage = (TextView) getView().findViewById(
				R.id.tv_cancel_wifi_setup_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(getActivity()));
		Button cancelWifiYes = (Button) getView().findViewById(
				R.id.btn_cancel_wifi_yes);
		cancelWifiYes.setTypeface(Fonts.getGillsansLight(getActivity()));

		Button cancelWifiNo = (Button) getView().findViewById(
				R.id.btn_cancel_wifi_no);
		cancelWifiNo.setTypeface(Fonts.getGillsansLight(getActivity()));
		cancelWifiYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                MetricsTracker.trackActionInAppNotificationPositiveResponse(getString(R.string.cancel_demo_setup_title));
				getActivity().finish();
			}
		});
		cancelWifiNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setCancelable(false);
	}
}
