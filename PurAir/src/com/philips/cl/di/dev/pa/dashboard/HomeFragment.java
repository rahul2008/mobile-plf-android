package com.philips.cl.di.dev.pa.dashboard;


import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirTutorialActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectPurifier;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.outdoorlocations.UpdateMyLocationsListener;
import com.philips.cl.di.dev.pa.outdoorlocations.UpdateMyPurifierListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeFragment extends BaseFragment implements OutdoorDataChangeListener, OnClickListener,
		AlertDialogBtnInterface, DrawerEventListener, NetworkStateListener {

	private ViewPager indoorViewPager;
	private ViewPager outdoorViewPager;
	private RelativeLayout noPurifierFlowLayout;
	
	private OutdoorPagerAdapter outdoorPagerAdapter;
	private IndoorPagerAdapter indoorPagerAdapter;
	
	private LinearLayout takeATourPopup;
	
    private CirclePageIndicator indicator;
    private int countIndoor;
    private UpdateMyPurifierListener updateMyPurifiersListener;
    private UpdateMyLocationsListener updateMyLocationsListener;
    private int prevPositionOutdoor;
    private int prevPositionIndoor;
	
	@Override
	public void onResume() {
		super.onResume();
		
		DrawerAdapter.getInstance().addDrawerListener(this);
		NetworkReceiver.getInstance().addNetworkStateListener(this);
		
		((MainActivity) getActivity()).setActionBar(this);
		if (indoorViewPager != null) {
			int currentPage = indoorViewPager.getCurrentItem();
			
			if (PurAirApplication.isDemoModeEnable()) {
				setRightMenuIconVisibilityDemoMode(currentPage);
			}
			else {
				setRightMenuIconVisibilityNormalMode(currentPage);
			}
		}
		notifyOutdoorPager();
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
		
		if(((MainActivity)getActivity()).getVisits()<3 && !((MainActivity)getActivity()).isTutorialPromptShown){
			takeATourPopup = (LinearLayout) getView().findViewById(R.id.take_tour_prompt_drawer);			
			showTakeATourPopup();
			
			FontTextView takeATourText = (FontTextView) getView().findViewById(R.id.lbl_take_tour);
			takeATourText.setOnClickListener(this);
			
			ImageButton takeATourCloseButton = (ImageButton) getView().findViewById(R.id.btn_close_tour_layout);
			takeATourCloseButton.setOnClickListener(this);
		}
	}

    private boolean getNoPurifierMode() {
        boolean noPurifierMode = false;
        Bundle bundle = getArguments();
        GPSLocation.getInstance().requestGPSLocation();
        if (bundle != null) {
            if(Utils.getUserGPSPermission() && !GPSLocation.getInstance().isLocationEnabled()) {
                //TODO : Show pop-up inviting the user to enable GPS
            } else {
                //Add user location to dashboard
                Location location = GPSLocation.getInstance().getGPSLocation();
                ALog.i(ALog.OUTDOOR_LOCATION, "My location " + location);
                if(location != null) {
                    OutdoorController.getInstance().startGetAreaIDTask(location.getLongitude(), location.getLatitude());
                }
            }
            noPurifierMode = bundle.getBoolean(AppConstants.NO_PURIFIER_FLOW, false);
        } else {
            noPurifierMode = false;
        }
        return noPurifierMode;
    }

    @SuppressWarnings("deprecation")
    private void initIndoorViewPager() {
        noPurifierFlowLayout = (RelativeLayout) getView().findViewById(R.id.hf_indoor_dashboard_rl_no_purifier);
        indoorViewPager = (ViewPager) getView().findViewById(R.id.hf_indoor_dashboard_viewpager);
        indoorViewPager.setOffscreenPageLimit(0);

        boolean noPurifierMode = getNoPurifierMode();

        if(noPurifierMode) {
            ((MainActivity)getActivity()).setTitle(getString(R.string.welcome));
            MetricsTracker.trackPage(TrackPageConstants.DASHBOARD_NO_PURIFIER);
            noPurifierFlowLayout.setVisibility(View.VISIBLE);
            indoorViewPager.setVisibility(View.INVISIBLE);
            Button connectPurifier = (Button) getView().findViewById(R.id.hf_indoor_dashboard_btn_connect);
            ImageView playVideo = (ImageView) getView().findViewById(R.id.hf_indoor_dashboard_btn_play);

            connectPurifier.setOnClickListener(withoutPurifierClickListener);
            playVideo.setOnClickListener(withoutPurifierClickListener);
        } else {
            noPurifierFlowLayout.setVisibility(View.GONE);
            noPurifierFlowLayout.setBackgroundDrawable(null); //Releases background from memory, this image is not used anymore.
            indoorViewPager.setVisibility(View.VISIBLE);

            countIndoor = getIndoorPageCount();
            setIndoorPagerAdapter(countIndoor);

            indicator = (CirclePageIndicator)getView().findViewById(R.id.indicator_indoor);
            indicator.setViewPager(indoorViewPager);
            indicator.setSnap(true);
            /**IndoorViewPager listener, We are using CirclePageIndicator for showing page indicator.
             *CirclePageIndicator block normal page change listener
             */
            indicator.setOnPageChangeListener(indoorPageChangeListener);
            setViewPagerIndicatorSetting(indicator, View.VISIBLE);

            indoorViewPager.setCurrentItem(PurifierManager.getInstance().getCurrentIndoorViewPagerPosition(), true);
        }
    }
    
    private void setIndoorPagerAdapter(int count) {
    	indoorPagerAdapter = new IndoorPagerAdapter(getChildFragmentManager(), countIndoor);
        indoorViewPager.setAdapter(indoorPagerAdapter);
    }

    private int getIndoorPageCount() {
        int countIndoor = 0;
        //For demo mode
        if (PurAirApplication.isDemoModeEnable()) {
            countIndoor = 1;
        } else if (DiscoveryManager.getInstance().getStoreDevices().size() > 0) {
            countIndoor = DiscoveryManager.getInstance().getStoreDevices().size() ;

            PurAirDevice purifier = DiscoveryManager.getInstance().getStoreDevices().get(0);
            if(purifier != null) {
                PurifierManager.getInstance().setCurrentPurifier(purifier);
            }
        }
        return countIndoor;
    }

    private void initOutdoorViewPager() {
        outdoorViewPager = (ViewPager) getView().findViewById(R.id.hf_outdoor_dashboard_viewpager);
        int count = outdoorPageCount();
		outdoorPagerAdapter = new OutdoorPagerAdapter(getChildFragmentManager(),count);
		outdoorViewPager.setAdapter(outdoorPagerAdapter);
		
        CirclePageIndicator indicator =
                (CirclePageIndicator)getView().findViewById(R.id.indicator_outdoor);
        indicator.setViewPager(outdoorViewPager);
        indicator.setSnap(true);
        /**IndoorViewPager listener, We are using CirclePageIndicator for showing page indicator.
         *CirclePageIndicator block normal page change listener
         */
        indicator.setOnPageChangeListener(outdoorPageChangeListener);
        setViewPagerIndicatorSetting(indicator, View.VISIBLE);
        outdoorViewPager.setCurrentItem(OutdoorManager.getInstance().getOutdoorViewPagerCurrentPage(), true);
    }

	private void initDashboardViewPager() {
		ALog.i(ALog.DASHBOARD, "HomeFragment$initDashboardViewPager");
        initIndoorViewPager();
        initOutdoorViewPager();
	}
	
	private void setViewPagerIndicatorSetting(CirclePageIndicator indicator, int visibility) {
		final float density = getResources().getDisplayMetrics().density;
		indicator.setPageColor(0xFF5D6577);
		indicator.setFillColor(0xFFB9BBC7);
        indicator.setRadius(4*density);
        indicator.setStrokeWidth(0.1f*density);
		indicator.setClickable(false);
		indicator.setVisibility(visibility);
	}
	
	@Override
	public void updateUIOnDataChange() {
		ALog.i(ALog.DASHBOARD, "nofifyDataSetChanged updateUI") ;
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					notifyOutdoorPager();
				} catch (IllegalStateException e) {
					ALog.e(ALog.ACTIVITY, "Error: " + e.getMessage());
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
		try {
			AlertDialogFragment dialog = AlertDialogFragment.newInstance(
					R.string.alert_take_tour, R.string.alert_taketour_text, R.string.alert_take_tour, R.string.close);
			dialog.setOnClickListener(this);
			dialog.show(getActivity().getSupportFragmentManager(), "");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
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
	
	private OnClickListener withoutPurifierClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.hf_indoor_dashboard_btn_connect) {
				ConnectPurifier.reset();
				ConnectPurifier.getInstance(getActivity()).startAddPurifierToAppFlow();
			} else if (v.getId() == R.id.hf_indoor_dashboard_btn_play) {
				startVideo();
			}
		}
	};

	@Override
	public void onPositiveButtonClicked() {
		Intent intentOd = new Intent(getActivity(), AirTutorialActivity.class);
		startActivity(intentOd);
	}

	@Override
	public void onNegativeButtonClicked() {/**NOP*/}
	
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
	public void onConnected(String ssid) {
		ALog.i(ALog.DASHBOARD, "HomeFragment$onConnected");
		OutdoorManager.getInstance().resetUpdatedTime();
		OutdoorManager.getInstance().startCitiesTask();
	}
	
	private void setRightMenuIconVisibility(int visibility) {
		if (getActivity() == null) return;
		((MainActivity)getActivity()).setRightMenuVisibility(visibility);
		
	}
	
	private void setRightMenuIconVisibilityDemoMode(int position) {
		//For demo mode
		if (position == 1) {
			setRightMenuIconVisibility(View.INVISIBLE);
		} else {
			setRightMenuIconVisibility(View.VISIBLE);
		}
	}
	
	private void setRightMenuIconVisibilityNormalMode(int position) {
		if (position >= DiscoveryManager.getInstance().getStoreDevices().size()) {
			setRightMenuIconVisibility(View.INVISIBLE);
		} else {
			setRightMenuIconVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDisconnected() {/**NOP*/}

	private OnPageChangeListener indoorPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageScrollStateChanged(int arg0) {/**NOP*/}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {/**NOP*/}
		
		@Override
		public void onPageSelected(int position) {
			PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(position);
			if (PurAirApplication.isDemoModeEnable()) {
				setRightMenuIconVisibilityDemoMode(position);
				return;
			}
			
			setRightMenuIconVisibilityNormalMode(position);
			
			if( position < DiscoveryManager.getInstance().getStoreDevices().size()) {
				PurAirDevice purifier = DiscoveryManager.getInstance().getStoreDevices().get(position);
				if (purifier == null) return;
		
				PurifierManager.getInstance().setCurrentPurifier(purifier) ;
			}
			
			if (prevPositionIndoor == indoorPagerAdapter.getCount() - 1 && updateMyPurifiersListener != null) {
				updateMyPurifiersListener.onUpdate();
			}
			prevPositionIndoor = position;
		}
	};
	
	private OnPageChangeListener outdoorPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageScrollStateChanged(int arg0) {/**NOP*/}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {/**NOP*/}
		
		@Override
		public void onPageSelected(int position) {
			OutdoorManager.getInstance().setOutdoorViewPagerCurrentPage(position);
			if (position == 0 && LocationUtils.getCurrentLocationAreaId().isEmpty()) {
				startCurrentCityAreaIdTask();
			}
			if (prevPositionOutdoor == outdoorPagerAdapter.getCount() - 1 && updateMyLocationsListener != null) {
				updateMyLocationsListener.onUpdate();
			}
			prevPositionOutdoor = position;
		}
	};

    public void notifyIndoorPager() {
        countIndoor = DiscoveryManager.getInstance().getStoreDevices().size() ;
        PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(countIndoor);
        indoorPagerAdapter.setCount(countIndoor) ;
        indoorPagerAdapter.notifyDataSetChanged();
        indoorViewPager.setCurrentItem(countIndoor, true);
    }

    public void gotoIndoorViewPage(int position) {
        if (indoorViewPager != null) {
            indoorViewPager.setCurrentItem(position, true);
        }
    }
    
    public void gotoOutdoorViewPage(int position) {
        if (outdoorViewPager != null) {
        	outdoorViewPager.setCurrentItem(position, true);
        }
    }
    
    private int outdoorPageCount() {
    	int count = 0;
		OutdoorManager.getInstance().processDataBaseInfo();
		List<String> myCityList = OutdoorManager.getInstance().getUsersCitiesList() ;
		if( myCityList != null ) {
			count = myCityList.size() ;
		}
		
		return count;
    }
    
	public synchronized void notifyOutdoorPager() {
		int count = outdoorPageCount();
		outdoorPagerAdapter.setCount(count) ;
		outdoorPagerAdapter.notifyDataSetChanged() ;
		outdoorViewPager.setCurrentItem(OutdoorManager.getInstance().getOutdoorViewPagerCurrentPage(), true);
	}
	
	private void startCurrentCityAreaIdTask() {
		String lat = LocationUtils.getCurrentLocationLat();
		String lon = LocationUtils.getCurrentLocationLon();
		if (!lat.isEmpty() && !lon.isEmpty()) {
			try {
				OutdoorController.getInstance().startGetAreaIDTask(Double.parseDouble(lon), Double.parseDouble(lat));
			} catch (NumberFormatException e) {
				ALog.e(ALog.ERROR, "OutdoorLocationFragment$showCurrentCityVisibility: " + "Error: " + e.getMessage());
			}
		}
	}
	
	public void setUpdateMyLocationsListner(UpdateMyLocationsListener updateMyLocationsListener) {
		this.updateMyLocationsListener = updateMyLocationsListener;
	}
	
	public void setUpdateMyPurifiersListner(UpdateMyPurifierListener updateMyPurifiersListener) {
		this.updateMyPurifiersListener = updateMyPurifiersListener;
	}
	
}
