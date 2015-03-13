package com.philips.cl.di.dev.pa.dashboard;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.TutorialPagerActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.AboutFragment;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectPurifier;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.outdoorlocations.UpdateMyLocationsListener;
import com.philips.cl.di.dev.pa.outdoorlocations.UpdateMyPurifierListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.DashboardUtil;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeFragment extends BaseFragment implements OutdoorDataChangeListener, OnClickListener,
		AlertDialogBtnInterface, NetworkStateListener {

	private static final int UPDATE_UI = 1;
	private static final String OUTDOOR_DEATAIL_FTAG = "outdoor_detail_fragment";
	private static final String INDOOR_DETAIL_FTAG = "indoor_detail_fragment";
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
    private ScrollView scrollView;
    private ViewGroup outdoorDetailContainer;
    private ViewGroup indoorDetailContainer;
    private ViewGroup titleLayout;
    private int screenHeight;// Without status bar height
	
	@Override
	public void onResume() {
		super.onResume();
		
		NetworkReceiver.getInstance().addNetworkStateListener(this);
		notifyOutdoorPager();
	}

	@Override
	public void onPause() {
		super.onPause();
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		
		titleLayout = (RelativeLayout) view.findViewById(R.id.home_fragment_title);
		titleLayout.setVisibility(View.GONE);
		
		initTitleView(view);
		
		scrollView = (ScrollView)view.findViewById(R.id.home_fragment_scroll_view);
		scrollView.setSelected(true);
		
		indoorDetailContainer = 
				(LinearLayout) view.findViewById(R.id.home_fragment_indoor_detail_container);
		indoorDetailContainer.setVisibility(View.GONE);
		
		outdoorDetailContainer = 
				(LinearLayout) view.findViewById(R.id.home_fragment_outdoor_detail_container);
		outdoorDetailContainer.setVisibility(View.GONE);
		
		ViewGroup indoorLayout = 
				(RelativeLayout) view.findViewById(R.id.home_fragment_indoor_viewpager_rl);
		ViewGroup outdoorLayout = 
				(RelativeLayout) view.findViewById(R.id.home_fragment_outdoor_viewpager_rl);
		
		screenHeight = MainActivity.getScreenHeight();
		ALog.i(ALog.DASHBOARD, "Before delay screenHeight Height=  " + screenHeight);
		LayoutParams params1 = indoorLayout.getLayoutParams();
		params1.height = screenHeight / 2;
		
		LayoutParams params2 = outdoorLayout.getLayoutParams();
		params2.height = screenHeight / 2;
		
		reSetViewPagerHeight(view, indoorLayout, outdoorLayout);
		
		return view;
	}
	
	private void reSetViewPagerHeight(View view, final ViewGroup indoorLayout,
			final ViewGroup outdoorLayout) {
		view.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				screenHeight = MainActivity.getScreenHeight();
				ALog.i(ALog.DASHBOARD, "After delay screenHeight Height=  " + screenHeight);
				LayoutParams params1 = indoorLayout.getLayoutParams();
				params1.height = screenHeight / 2;
				
				LayoutParams params2 = outdoorLayout.getLayoutParams();
				params2.height = screenHeight / 2;
			}
		}, 55); // 55 millisecond, View is taking time to load on UI. We given 50 second delay in MainActivity
	}

	private void initTitleView(View view) {
		ImageButton collapseBtn = (ImageButton) view.findViewById(R.id.heading_back_imgbtn);
		collapseBtn.setVisibility(View.VISIBLE);
		collapseBtn.setOnClickListener(this);
		
		ImageButton aboutBtn = (ImageButton) view.findViewById(R.id.heading_close_imgbtn);
		aboutBtn.setVisibility(View.VISIBLE);
		aboutBtn.setImageResource(R.drawable.info);
		aboutBtn.setOnClickListener(this);
		
		FontTextView title = (FontTextView) view.findViewById(R.id.heading_name_tv);
		title.setText(getString(R.string.dashboard_title));
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initDashboardViewPager();
		OutdoorManager.getInstance().setUIChangeListener(this);
		OutdoorManager.getInstance().startCitiesTask();
		ImageButton infoImgBtn = (ImageButton) getView().findViewById(R.id.home_fragment_info_img_btn);
		infoImgBtn.setOnClickListener(this);
		
		if(((MainActivity)getActivity()).getVisits()<3 && !((MainActivity)getActivity()).isTutorialPromptShown){
			takeATourPopup = (LinearLayout) getView().findViewById(R.id.take_tour_prompt_drawer);			
			showTakeATourPopup();
			
			FontTextView takeATourText = (FontTextView) getView().findViewById(R.id.lbl_take_tour);
			takeATourText.setOnClickListener(this);
			
			ImageButton takeATourCloseButton = (ImageButton) getView().findViewById(R.id.btn_close_tour_layout);
			takeATourCloseButton.setOnClickListener(this);
		}
	}


    @SuppressWarnings("deprecation")
    private void initIndoorViewPager() {
        noPurifierFlowLayout = (RelativeLayout) getView().findViewById(R.id.hf_indoor_dashboard_rl_no_purifier);
        indoorViewPager = (ViewPager) getView().findViewById(R.id.hf_indoor_dashboard_viewpager);
        indoorViewPager.setOffscreenPageLimit(0);

        boolean purifierMode = DashboardUtil.isNoPurifierMode(getArguments());

        if(purifierMode) {
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

            countIndoor = DashboardUtil.getIndoorPageCount();
            setIndoorPagerAdapter(countIndoor);

            indicator = (CirclePageIndicator)getView().findViewById(R.id.indicator_indoor);
            indicator.setViewPager(indoorViewPager);
            indicator.setSnap(true);
            /**IndoorViewPager listener, We are using CirclePageIndicator for showing page indicator.
             *CirclePageIndicator block normal page change listener
             */
            indicator.setOnPageChangeListener(indoorPageChangeListener);
            setViewPagerIndicatorSetting(indicator, View.VISIBLE);

            indoorViewPager.setCurrentItem(AirPurifierManager.getInstance().getCurrentIndoorViewPagerPosition(), true);
        }
    }
    
    private void setIndoorPagerAdapter(int count) {
    	indoorPagerAdapter = new IndoorPagerAdapter(getChildFragmentManager(), countIndoor);
        indoorViewPager.setAdapter(indoorPagerAdapter);
    }

    private void initOutdoorViewPager() {
        outdoorViewPager = (ViewPager) getView().findViewById(R.id.hf_outdoor_dashboard_viewpager);
        int count = DashboardUtil.getOutdoorPageCount();
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
		handler.sendEmptyMessage(UPDATE_UI);
	}
	
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if ( msg.what == UPDATE_UI ) {
				notifyOutdoorPager();
			} 
		};
	};
	
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
		MainActivity activity = (MainActivity) getActivity();
		if (activity == null) return;
		switch (v.getId()) {
		case R.id.lbl_take_tour:
			((MainActivity)getActivity()).isTutorialPromptShown = true;
			Intent intentOd = new Intent(getActivity(), TutorialPagerActivity.class);
			startActivity(intentOd);
			hideTakeATourPopup();
			break;
		case R.id.btn_close_tour_layout:
			activity.isTutorialPromptShown = true;
			hideTakeATourPopup();
			showTutorialDialog();
			break;
		case R.id.home_fragment_info_img_btn:
			activity.showFragment(new AboutFragment());
			break;
		case R.id.heading_back_imgbtn:
			removeIndoorDetails();
			removeOutdoorDetails();
			titleLayout.setVisibility(View.GONE);
			break;
		case R.id.heading_close_imgbtn:
			activity.showFragment(new AboutFragment());
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
		Intent intentOd = new Intent(getActivity(), TutorialPagerActivity.class);
		startActivity(intentOd);
	}

	@Override
	public void onNegativeButtonClicked() {/**NOP*/}
	
	@Override
	public void onConnected(String ssid) {
		ALog.i(ALog.DASHBOARD, "HomeFragment$onConnected");
		OutdoorManager.getInstance().resetUpdatedTime();
		OutdoorManager.getInstance().startCitiesTask();
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
			removeIndoorDetails();
			AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(position);
			if (PurAirApplication.isDemoModeEnable()) {
				return;
			}
			
			if( position < DiscoveryManager.getInstance().getStoreDevices().size()) {
				AirPurifier purifier = DiscoveryManager.getInstance().getStoreDevices().get(position);
				if (purifier == null) return;
		
				AirPurifierManager.getInstance().setCurrentPurifier(purifier) ;
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
			removeOutdoorDetails();
			OutdoorManager.getInstance().setOutdoorViewPagerCurrentPage(position);
			if (position == 0 && LocationUtils.getCurrentLocationAreaId().isEmpty()) {
				DashboardUtil.startCurrentCityAreaIdTask();
			}
			if (prevPositionOutdoor == outdoorPagerAdapter.getCount() - 1 && updateMyLocationsListener != null) {
				updateMyLocationsListener.onUpdate();
			}
			prevPositionOutdoor = position;
		}
	};

    public void notifyIndoorPager() {
        countIndoor = DiscoveryManager.getInstance().getStoreDevices().size() ;
        AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(countIndoor);
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
    	try {
	        if (outdoorViewPager != null) {
	        	outdoorViewPager.setCurrentItem(position, true);
	        }
    	} catch(IllegalStateException ies) {
    		ALog.e(ALog.ERROR, "IllegalStateException") ;
    	}
    }
    
	public synchronized void notifyOutdoorPager() {
		int count = DashboardUtil.getOutdoorPageCount();
		outdoorPagerAdapter.setCount(count) ;
		outdoorPagerAdapter.notifyDataSetChanged() ;
		outdoorViewPager.setCurrentItem(OutdoorManager.getInstance().getOutdoorViewPagerCurrentPage(), true);
	}
	
	public void setUpdateMyLocationsListner(UpdateMyLocationsListener updateMyLocationsListener) {
		this.updateMyLocationsListener = updateMyLocationsListener;
	}
	
	public void setUpdateMyPurifiersListner(UpdateMyPurifierListener updateMyPurifiersListener) {
		this.updateMyPurifiersListener = updateMyPurifiersListener;
	}
	
	//Detail
	private boolean isLoadingOutdoorDetail;
	public void toggleOutdoorDetailFragment(String cityName, OutdoorAQI outdoorAQI, int provider) {
		if (isLoadingOutdoorDetail) return;
		isLoadingOutdoorDetail = true;
		if (!removeOutdoorDetails()) {
			OutdoorDetailFragment fragment = new OutdoorDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString(AppConstants.OUTDOOR_CITY_NAME, cityName);
			bundle.putSerializable(AppConstants.OUTDOOR_AQI, outdoorAQI);
			bundle.putInt(AppConstants.OUTDOOR_DATAPROVIDER, provider) ;
			fragment.setArguments(bundle);
			addFragment(fragment, R.id.home_fragment_outdoor_detail_container, OUTDOOR_DEATAIL_FTAG) ;
			outdoorDetailContainer.setVisibility(View.VISIBLE);
			setTitleBarVisibity();
			scrollScrollView(true) ;
		}
		isLoadingOutdoorDetail = false;
	}
	
	public void toggleIndoorDetailFragment() {
		if (!removeIndoorDetails()) {
			IndoorDetailFragment fragment = new IndoorDetailFragment();
			addFragment(fragment, R.id.home_fragment_indoor_detail_container, INDOOR_DETAIL_FTAG);
			indoorDetailContainer.setVisibility(View.VISIBLE);
			setTitleBarVisibity();
		}
		scrollScrollView(false);
	}
	
	private void addFragment(Fragment fragment, int containerId, String tag) {
		MainActivity activity = (MainActivity)getActivity();
		if (activity == null) return;
		try{
			FragmentTransaction fragmentTransaction = 
					activity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(containerId, fragment, tag);
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, "IllegalStateException: " + e.getMessage());
		}
	}
	
	public boolean removeOutdoorDetails() {
		MainActivity activity = (MainActivity)getActivity();
		if (activity != null)  {
			try {
				FragmentTransaction fragmentTransaction = 
						activity.getSupportFragmentManager().beginTransaction();
				Fragment prevFragment = activity.getSupportFragmentManager()
						.findFragmentByTag(HomeFragment.OUTDOOR_DEATAIL_FTAG);
				Fragment prevFragmentMap = activity.getSupportFragmentManager()
						.findFragmentById(R.id.outdoor_detail_map_container);
				if (prevFragment != null) {
					fragmentTransaction.remove(prevFragment);
					if (prevFragmentMap != null) {
						fragmentTransaction.remove(prevFragmentMap);
					}
					fragmentTransaction.commit();
					outdoorDetailContainer.setVisibility(View.GONE);
					setTitleBarVisibity();
					return true;
				}
			} catch (IllegalStateException e) {
				ALog.e(ALog.TEMP, e.getMessage());
			}
		}
		return false;
	}
	
	private boolean removeIndoorDetails() {
		MainActivity activity = (MainActivity)getActivity();
		if (activity != null) {
			try {
				FragmentTransaction fragmentTransaction = 
						activity.getSupportFragmentManager().beginTransaction();
				Fragment prevFragment =  activity.getSupportFragmentManager().
						findFragmentByTag(HomeFragment.INDOOR_DETAIL_FTAG);
				if (prevFragment != null) {
					fragmentTransaction.remove(prevFragment);
					fragmentTransaction.commit();
					indoorDetailContainer.setVisibility(View.GONE);
					setTitleBarVisibity();
					return true;
				}
			} catch (IllegalStateException e) {
				ALog.e(ALog.TEMP, e.getMessage());
			}
		}
		return false;
	}
	
	/**
	 * Scroll to outdoor detail, normal case outdoor detail does not added,
	 *  that why given delay 100 milli sec.
	 */
	private void scrollScrollView(final boolean srcollUp) {
		scrollView.postDelayed(new Runnable() {
			@Override
			public void run() {
				int scrollY = 0;
				
				if (srcollUp) {
					int indoorDetailHeight = indoorDetailContainer.getHeight();
					if (indoorDetailContainer.getVisibility() == View.GONE) {
						indoorDetailHeight = 0;
					}
					scrollY = (screenHeight / 2) + indoorDetailHeight;
				}
				scrollView.scrollTo(0, scrollY);
			}
		}, 100); //100 millisecond
	}
	
	private void setTitleBarVisibity() {
		if (indoorDetailContainer.getVisibility() == View.GONE  
				&& outdoorDetailContainer.getVisibility() == View.GONE) {
			titleLayout.setVisibility(View.GONE);
		} else {
			titleLayout.setVisibility(View.VISIBLE);
		}
	}

}
