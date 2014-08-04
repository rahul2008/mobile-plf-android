package com.philips.cl.di.dev.pa.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.Utils;


public class StartFlowChooseFragment extends BaseFragment implements OnClickListener {

	
	private Button mBtnNewPurifier;
	private Button mBtnConnectedPurifier;
	private Bundle mBundle;
	private StartFlowDialogFragment mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.start_flow_choose_fragment, container, false);
		
		mBundle = new Bundle();
		mDialog = new StartFlowDialogFragment();
		
		mBtnNewPurifier = (Button) view.findViewById(R.id.start_flow_choose_btn_connect_new);
		mBtnConnectedPurifier = (Button) view.findViewById(R.id.start_flow_choose_btn_already_connected);
				
		mBtnNewPurifier.setOnClickListener(this);
		mBtnConnectedPurifier.setOnClickListener(this);
		
		return view;
	}
	
	private void startEWS() {
		Intent intent = new Intent(getActivity(), EWSActivity.class);
		getActivity().startActivity(intent);
		getFragmentManager().popBackStackImmediate();
	}
	
	private void showApSelectorDialog() {
		try {
			mBundle.clear();
			mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER, StartFlowDialogFragment.AP_SELCTOR);
			mDialog.setArguments(mBundle);
			mDialog.show(getFragmentManager(), "start_flow_dialog");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_flow_choose_btn_connect_new:
			startEWS();
			break;
		case R.id.start_flow_choose_btn_already_connected:
			DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
			final List<PurAirDevice> apItems = discoveryManager.getNewDevicesDiscovered();
			if (apItems.size() > 0) {
				showApSelectorDialog();
			} else {
				// TODO show troubleshoot flow
				Toast toast = Utils.getCustomToast(
						PurAirApplication.getAppContext().getString(R.string.no_purifier_found));
				toast.show();
				Log.e("TEMP", "-------------- Show troubleshoot flow!!! --------------");
			}
			break;
		default:
			break;
		}
	}
}
