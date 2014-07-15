package com.philips.cl.di.dev.pa.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirTutorialActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeFragment extends BaseFragment implements OutdoorDataChangeListener, OnClickListener, AlertDialogBtnInterface, DrawerEventListener, NetworkStateListener{

	private ViewPager indoorViewPager;
	private ViewPager outdoorViewPager;
	private RelativeLayout noPurifierFlowLayout;
	
	private OutdoorPagerAdapter outdoorPagerAdapter;
	private IndoorPagerAdapter indoorPagerAdapter;
	
	private LinearLayout takeATourPopup;
	
	private boolean mNoPurifierMode;
	
	@Override
	public void onResume() {
		super.onResume();
		DrawerAdapter.getInstance().addDrawerListener(this);
		NetworkReceiver.getInstance().addNetworkStateListener(this);
		
		((MainActivity) getActivity()).setActionBar(this);
	}


	@Override
	public void onPause() {
		super.onPause();
		DrawerAdapter.getInstance().removeDrawerListener(this);
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		
		ALog.i(ALog.DASHBOARD, "Density " + getResources().getDisplayMetrics().densityDpi);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initDashboardViewPager();
		OutdoorManager.getInstance().setUIChangeListener(this);
		OutdoorManager.getInstance().startCitiesTask();
		
		if(((MainActivity)getActivity()).getVisits()<=3 && !((MainActivity)getActivity()).isTutorialPromptShown){
			takeATourPopup = (LinearLayout) getView().findViewById(R.id.take_tour_prompt_drawer);			
			showTakeATourPopup();
			
			FontTextView takeATourText = (FontTextView) getView().findViewById(R.id.lbl_take_tour);
			takeATourText.setOnClickListener(this);
			
			ImageButton takeATourCloseButton = (ImageButton) getView().findViewById(R.id.btn_close_tour_layout);
			takeATourCloseButton.setOnClickListener(this);
		}
		
	}
	
	private void initDashboardViewPager() {
		ALog.i(ALog.DASHBOARD, "HomeFragment$initDashboardViewPager");
		noPurifierFlowLayout = (RelativeLayout) getView().findViewById(R.id.hf_indoor_dashboard_rl_no_purifier);
		indoorViewPager = (ViewPager) getView().findViewById(R.id.hf_indoor_dashboard_viewpager);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mNoPurifierMode = bundle.getBoolean(AppConstants.NO_PURIFIER_FLOW, false);
		} else {
			mNoPurifierMode = false;
		}
				
		if(mNoPurifierMode) {
			noPurifierFlowLayout.setVisibility(View.VISIBLE);
			indoorViewPager.setVisibility(View.INVISIBLE);
		} else {
			noPurifierFlowLayout.setVisibility(View.GONE);
			indoorViewPager.setVisibility(View.VISIBLE);
			indoorPagerAdapter = new IndoorPagerAdapter(getChildFragmentManager());
			indoorViewPager.setAdapter(indoorPagerAdapter);
		}		
	
		outdoorViewPager = (ViewPager) getView().findViewById(R.id.hf_outdoor_dashboard_viewpager);
		int count = 1 ;
		if( OutdoorManager.getInstance().getCitiesList() != null && OutdoorManager.getInstance().getCitiesList().size() > 0 ) {
			count = OutdoorManager.getInstance().getCitiesList().size() ;
		}
		outdoorPagerAdapter = new OutdoorPagerAdapter(getChildFragmentManager(),count);
		//outdoorViewPager.setOffscreenPageLimit(3);
		outdoorViewPager.setAdapter(outdoorPagerAdapter);
		
		CirclePageIndicator indicator = (CirclePageIndicator)getView().findViewById(R.id.indicator);
		indicator.setViewPager(outdoorViewPager);
		indicator.setSnap(true);

		final float density = getResources().getDisplayMetrics().density;
		indicator.setPageColor(0xFF5D6577);
		indicator.setFillColor(0xFFB9BBC7);   
		indicator.setStrokeWidth(0.1f*density);
		indicator.setClickable(false);
		indicator.setVisibility(View.GONE);
	}
	
	@Override
	public void updateUIOnDataChange() {
		ALog.i(ALog.DASHBOARD, "notifyUIOnDataChange " + getActivity());	
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					int size = 1;
					if( OutdoorManager.getInstance().getCitiesList() != null ) {
						size = OutdoorManager.getInstance().getCitiesList().size() ;
					}
					outdoorPagerAdapter.mCount(size) ;
					outdoorPagerAdapter.notifyDataSetChanged() ;
				} catch (IllegalStateException e) {
					ALog.e(ALog.ACTIVITY, e.getMessage());
				}
			}
		});
	}
	
	private void showTakeATourPopup() {
		if(!((MainActivity)getActivity()).isTutorialPromptShown) {
			takeATourPopup.setVisibility(View.VISIBLE);
			Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
			takeATourPopup .startAnimation(bottomUp);
		}
	}
	
	private void hideTakeATourPopup() {
		if (takeATourPopup != null) {
			takeATourPopup.setVisibility(View.GONE);
		}
	}
	
	private void showTutorialDialog() {
		AlertDialogFragment dialog = AlertDialogFragment.newInstance(R.string.alert_take_tour, R.string.alert_taketour_text, R.string.alert_take_tour, R.string.close);
		dialog.setOnClickListener(this);
		dialog.show(getActivity().getSupportFragmentManager(), "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lbl_take_tour:
			((MainActivity)getActivity()).isTutorialPromptShown = true;
			Intent intentOd = new Intent(getActivity(), AirTutorialActivity.class);
			startActivity(intentOd);
			hideTakeATourPopup();
			break;
			
		case R.id.btn_close_tour_layout:
			((MainActivity)getActivity()).isTutorialPromptShown = true;
			hideTakeATourPopup();
			showTutorialDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPositiveButtonClicked() {
		Intent intentOd = new Intent(getActivity(), AirTutorialActivity.class);
		startActivity(intentOd);
	}

	@Override
	public void onNegativeButtonClicked() {
		// NOP
	}
	
	@Override
	public void onDrawerEvent(DrawerEvent event, View drawerView) {
		switch (event) {
		case DRAWER_CLOSED:
			break;
		case DRAWER_OPENED:
			hideTakeATourPopup();
			break;
		default:
			break;
		}
	}

	@Override
	public void onConnected() {
		ALog.i(ALog.DASHBOARD, "HomeFragment$onConnected");
		OutdoorManager.getInstance().startCitiesTask();
	}

	@Override
	public void onDisconnected() {
		
	}
}
