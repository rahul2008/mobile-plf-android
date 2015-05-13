package com.philips.cl.di.dev.pa.dashboard;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.demo.DemoModeActivity;
import com.philips.cl.di.dev.pa.fragment.AboutFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.StartFlowChooseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;

public class AddPurifierFragment extends BaseFragment implements
		OnClickListener {

	private ImageView ivAddNewPurifier;
	private Button btnGotoShop;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_new_purifier, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ivAddNewPurifier = (ImageView) getActivity().findViewById(
				R.id.iv_circle_add_purifier);
		ivAddNewPurifier.setOnClickListener(this);
		btnGotoShop = (Button) getActivity().findViewById(R.id.btn_go_to_shop);
		btnGotoShop.setOnClickListener(this);
		ImageButton infoImgBtn = (ImageButton) getView().findViewById(R.id.add_new_purifier_info_img_btn);
		infoImgBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_circle_add_purifier:
			if (PurAirApplication.isDemoModeEnable()) {
				Intent intent = new Intent((MainActivity) getActivity(), DemoModeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				((MainActivity) getActivity()).startActivity(intent);
			} else {
				List<DICommAppliance> storePurifiers = DiscoveryManager.getInstance().updateAddedAppliances();
				if (storePurifiers.size() >= AppConstants.MAX_PURIFIER_LIMIT) {
					showAlertDialog("",	getString(R.string.max_purifier_reached));
				} else {
					((MainActivity) getActivity()).showFragment(new StartFlowChooseFragment());
				}
			}
			break;
		case R.id.btn_go_to_shop:
			MetricsTracker.trackActionExitLink(AppConstants.PURIFIER_BUY_LINK);
			MetricsTracker.trackActionBuyButton("Philips lead");
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(AppConstants.PURIFIER_BUY_LINK));
			startActivity(intent);
			break;
		case R.id.add_new_purifier_info_img_btn:
			MainActivity activity = (MainActivity) getActivity();
			if (activity != null) {
				activity.showFragment(new AboutFragment());
			}
			break;
		default:
			break;
		}
	}

	private void showAlertDialog(String title, String message) {
		if (getActivity() == null)
			return;
		try {
			FragmentTransaction fragTransaction = getActivity()
					.getSupportFragmentManager().beginTransaction();

			Fragment prevFrag = getActivity().getSupportFragmentManager()
					.findFragmentByTag("max_purifier_reached");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}

			fragTransaction.add(
					DownloadAlerDialogFragement.newInstance(title, message),
					"max_purifier_reached").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, "Error: " + e.getMessage());
		}
	}
}
