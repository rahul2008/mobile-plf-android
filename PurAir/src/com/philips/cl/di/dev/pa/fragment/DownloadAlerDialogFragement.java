package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationActivity;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class DownloadAlerDialogFragement extends DialogFragment {
	
	private static final String EXTRA_TITLE = "title";
	private static final String EXTRA_MSG = "message";
	
	public static DownloadAlerDialogFragement newInstance(String title, String msg) {
		DownloadAlerDialogFragement fragment =  new DownloadAlerDialogFragement();
		
		Bundle args= new Bundle();
		args.putString(EXTRA_TITLE, title);
		args.putString(EXTRA_MSG, msg);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.download_alert_dialog, container, false);
	
		return view; 
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		String title = getArguments().getString(EXTRA_TITLE);
		String msg = getArguments().getString(EXTRA_MSG);
		
		Button button = (Button) getView().findViewById(R.id.alert_dialog_cancel);
		button.setTypeface(Fonts.getGillsansLight(getActivity()));
		
		FontTextView tvHeader = (FontTextView) getView().findViewById(R.id.alert_dialog_title);
		tvHeader.setText(title);
		
		FontTextView tvMessage = (FontTextView) getView().findViewById(R.id.alert_dialog_msg);
		tvMessage.setText(msg);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if (getActivity() instanceof AddOutdoorLocationActivity) {
					getActivity().finish();
				}
			}
		});
		
		setCancelable(false);
	}

}
