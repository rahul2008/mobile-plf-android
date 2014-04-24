package com.philips.cl.di.dev.pa.ews;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class EWSDialogFragment extends DialogFragment {
	
	public static EWSDialogFragment newInstance() {
		EWSDialogFragment fragment =  new EWSDialogFragment();
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.error_alert_layout, container, false);
	
		return view; 
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		if (getActivity() == null) {
			return;
		}
		
		Button button = (Button) getView().findViewById(R.id.btn_error_popup);
		button.setTypeface(Fonts.getGillsansLight(getActivity()));
		button.setText(getActivity().getString(R.string.error_purifier_not_detect_btn_txt));
		
		TextView tvHeader = (TextView) getView().findViewById(R.id.tv_error_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(getActivity()));
		tvHeader.setText(getActivity().getString(R.string.error_ts01_04_title));
		TextView tvMessage = (TextView) getView().findViewById(R.id.tv_error_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(getActivity()));
		tvMessage.setText(getActivity().getString(R.string.error_ts01_04_message));
		ImageView ivGotoSupport = (ImageView) getView().findViewById(R.id.iv_goto_support);
		ImageView ivCloseErrorPopup = (ImageView) getView().findViewById(R.id.iv_close_error_popup);
		
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
				EWSActivity activity = (EWSActivity) getActivity() ;
				activity.showSupportFragment() ;
				break;
			case R.id.btn_error_popup:
				EWSDialogFactory.getInstance(getActivity()).getDialog(EWSDialogFactory.SUPPORT_TS01).show();
				break;
			default:
				break;
			}
			
		}
	};

}
