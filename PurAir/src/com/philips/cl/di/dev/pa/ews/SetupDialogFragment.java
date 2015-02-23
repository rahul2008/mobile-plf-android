package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.demo.DemoModeActivity;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;

public class SetupDialogFragment extends DialogFragment {

	private static final String EXTRA_TITLE = "title";
	private static final String EXTRA_MSG = "message";
	private static final String EXTRA_BTNTXT = "btntxt";

	public static SetupDialogFragment newInstance(String title, String msg,
			String btnTxt) {
		SetupDialogFragment fragment = new SetupDialogFragment();

		Bundle args = new Bundle();
		args.putString(EXTRA_TITLE, title);
		args.putString(EXTRA_MSG, msg);
		args.putString(EXTRA_BTNTXT, btnTxt);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.error_alert_layout, container,
				false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		if (getActivity() == null) {
			return;
		}

		String title = getArguments().getString(EXTRA_TITLE);
		String msg = getArguments().getString(EXTRA_MSG);
		String btnTxt = getArguments().getString(EXTRA_BTNTXT);

		Button button = (Button) getView().findViewById(R.id.btn_error_popup);
		button.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		button.setText(btnTxt);

		TextView tvHeader = (TextView) getView().findViewById(
				R.id.tv_error_header);
		tvHeader.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		tvHeader.setText(title);
		TextView tvMessage = (TextView) getView().findViewById(
				R.id.tv_error_message);
		tvMessage.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		tvMessage.setText(msg);
		ImageView ivGotoSupport = (ImageView) getView().findViewById(
				R.id.iv_goto_support);
		ImageView ivCloseErrorPopup = (ImageView) getView().findViewById(
				R.id.iv_close_error_popup);

		ivGotoSupport.setOnClickListener(buttonClickEvent);
		button.setOnClickListener(buttonClickEvent);
		ivCloseErrorPopup.setOnClickListener(buttonClickEvent);
		setCancelable(false);
	}

	private OnClickListener buttonClickEvent = new OnClickListener() {

		@Override
		public void onClick(View view) {
			dismiss();
			switch (view.getId()) {
			case R.id.iv_goto_support:
				if (getActivity() instanceof EWSActivity) {
					EWSActivity activity = (EWSActivity) getActivity();
					activity.showSupportFragment();
				} else if (getActivity() instanceof DemoModeActivity) {
					DemoModeActivity activity = (DemoModeActivity) getActivity();
					activity.showSupportScreen();
				}

				break;
			case R.id.btn_error_popup:
				SetupDialogFactory.getInstance(getActivity())
						.getDialog(SetupDialogFactory.SUPPORT_UNPLUG_PURIFIER).show();
                MetricsTracker.trackPage("WiFi Setup : Unplug purifier");
				break;
			default:
				break;
			}

		}
	};

}
