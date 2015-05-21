package com.philips.cl.di.dev.pa.dashboard;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FilterStatusView;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dicomm.communication.Error;

public class FilterStatusFragment extends BaseFragment implements AirPurifierEventListener, OnClickListener {

	/** Filter status bars */
	private FilterStatusView preFilterView, multiCareFilterView,
	activeCarbonFilterView, hepaFilterView;

	// Filter status texts
	private TextView preFilterText, multiCareFilterText,
	activeCarbonFilterText, hepaFilterText;

	//Buy online
	private FontTextView buyOnlineMulticare,buyOnlineActineCarbon,
	buyOnlineHepa;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.filter_status, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.FILTER_STATUS);
		initFilterStatusViews();
	}

	@Override
	public void onResume() {
		super.onResume();
		AirPurifierManager.getInstance().addAirPurifierEventListener(this);
		updateFilterViews();
	}

	@Override
	public void onPause() {
		super.onPause();
		AirPurifierManager.getInstance().removeAirPurifierEventListener(this);		
	}

	@Override
	public void onAirPurifierChanged() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateFilterViews();
			}
		});
	}

	@Override
	public void onAirPurifierEventReceived() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateFilterViews();
			}
		});

	}

	@Override
	public void onFirmwareEventReceived() {

	}

	@Override
	public void onErrorOccurred(Error purifierEventError) {
	}


	private void initFilterStatusViews() {
		preFilterView = (FilterStatusView) getView().findViewById(R.id.iv_pre_filter);
		multiCareFilterView = (FilterStatusView) getView().findViewById(R.id.iv_multi_care_filter);
		activeCarbonFilterView = (FilterStatusView) getView().findViewById(R.id.iv_active_carbon_filter);
		hepaFilterView = (FilterStatusView) getView().findViewById(R.id.iv_hepa_filter);

		preFilterText = (TextView) getView().findViewById(R.id.tv_rm_pre_filter_status);
		multiCareFilterText = (TextView) getView().findViewById(R.id.tv_rm_multi_care_filter_status);
		activeCarbonFilterText = (TextView) getView().findViewById(R.id.tv_rm_active_carbon_filter_status);
		hepaFilterText = (TextView) getView().findViewById(R.id.tv_rm_hepa_filter_status);

		buyOnlineMulticare = (FontTextView) getView().findViewById(R.id.buyonline_multicare);
		buyOnlineMulticare.setOnClickListener(this);
		buyOnlineMulticare.setText(getString(R.string.list_item_buy_online) + " >");

		buyOnlineActineCarbon = (FontTextView) getView().findViewById(R.id.buyonline_active_carbon);
		buyOnlineActineCarbon.setOnClickListener(this);
		buyOnlineActineCarbon.setText(getString(R.string.list_item_buy_online) + " >");

		buyOnlineHepa = (FontTextView) getView().findViewById(R.id.buyonline_hepa);
		buyOnlineHepa.setOnClickListener(this);
		buyOnlineHepa.setText(getString(R.string.list_item_buy_online) + " >");

		ImageView linkFilterClean=(ImageView) getView().findViewById(R.id.clean_filter_link);
		linkFilterClean.setOnClickListener(filterClickListener);

		ImageView linkFilterReplace=(ImageView) getView().findViewById(R.id.replace_filter_link);
		linkFilterReplace.setOnClickListener(filterClickListener);

		ImageButton backButton = (ImageButton) getView().findViewById(R.id.heading_back_imgbtn);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity mainActivity = (MainActivity) getActivity();
				mainActivity.onBackPressed();				
			}
		});
		backButton.setVisibility(View.VISIBLE);
		FontTextView heading=(FontTextView) getView().findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.filters));
	}

	private void disableFilterStatus() {
		/** Update filter bars */
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				preFilterView.setColorAndLength(Color.LTGRAY, 0);
				multiCareFilterView.setColorAndLength(Color.LTGRAY, 0);
				activeCarbonFilterView.setColorAndLength(Color.LTGRAY, 0);
				hepaFilterView.setColorAndLength(Color.LTGRAY, 0);

				/** Update filter texts */
				preFilterText.setText(AppConstants.EMPTY_STRING);
				multiCareFilterText.setText(AppConstants.EMPTY_STRING);
				activeCarbonFilterText.setText(AppConstants.EMPTY_STRING);
				hepaFilterText.setText(AppConstants.EMPTY_STRING);
			}
		});
	}

  
	private void updateFilterStatus(int preFilterStatus,
			int multiCareFilterStatus, int activeCarbonFilterStatus,
			int hepaFilterStatus) {
		
		ALog.d(ALog.FILTER_STATUS_FRAGMENT, "Update filter status");
		/** Update filter bars */
		preFilterView.setFilterValue(preFilterStatus, FilterStatusView.PRE_FILTER);
		multiCareFilterView.setFilterValue(multiCareFilterStatus, FilterStatusView.MUL_CARE_FILTER);
		activeCarbonFilterView.setFilterValue(activeCarbonFilterStatus, FilterStatusView.ACT_CARBON_FILTER);
		hepaFilterView.setFilterValue(hepaFilterStatus, FilterStatusView.HEPA_FILTER);

		/** Update filter texts */
		preFilterText.setText(Utils.getPreFilterStatusText(preFilterStatus));
		preFilterText.setTextColor(Utils.getPreFilterStatusColour(preFilterStatus));

		multiCareFilterText.setText(Utils.getMultiCareFilterStatusText(multiCareFilterStatus));
		multiCareFilterText.setTextColor(Utils.getMultiCareFilterStatusColour(multiCareFilterStatus));

		activeCarbonFilterText.setText(Utils.getActiveCarbonFilterStatusText(activeCarbonFilterStatus));
		activeCarbonFilterText.setTextColor(Utils.getActiveCarbonFilterStatusColour(activeCarbonFilterStatus));

		hepaFilterText.setText(Utils.getHEPAFilterFilterStatusText(hepaFilterStatus));
		hepaFilterText.setTextColor(Utils.getHEPAFilterStatusColour(hepaFilterStatus));

	}

	private AirPortInfo getAirPortInfo(AirPurifier purifier) {
		if (purifier == null) return null;
		return purifier.getAirPort().getAirPortInfo();
	}

	private void updateFilterViews() {
		ALog.i(ALog.FILTER_STATUS_FRAGMENT, "updateFilterStatus");
		final AirPurifier purifier = AirPurifierManager.getInstance().getCurrentPurifier();
		if(purifier == null || purifier.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED
				|| purifier.getAirPort().getAirPortInfo() == null) {
			disableFilterStatus();
			return ;
		}
		final AirPortInfo info = getAirPortInfo(purifier);
		updateFilterStatus(info.getPreFilterStatus(),
				info.getMulticareFilterStatus(),
				info.getActiveFilterStatus(),
				info.getHepaFilterStatus());
	}


	private OnClickListener filterClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent;
			switch (view.getId()) {
			case R.id.clean_filter_link:
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("http://www.philips-smartairpurifier.com/movies/filter_clean.mp4"), "video/mp4");
				startActivity(Intent.createChooser(intent,""));  
				break;

			case R.id.replace_filter_link:
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("http://www.philips-smartairpurifier.com/movies/filter_replace.mp4"), "video/mp4");
				view.getContext().startActivity(Intent.createChooser(intent,""));  
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (getActivity() == null) return;
		String uri="";
		MetricsTracker.trackActionBuyButton("Retailer lead");
		switch (v.getId()) {
		case R.id.buyonline_multicare:
			uri="http://detail.tmall.com/item.htm?spm=0.0.0.0.4pERVR&id=39880338072&origin=15_global_en_purifier-app_purifier-app";
			break;
		case R.id.buyonline_active_carbon:
			uri="http://detail.tmall.com/item.htm?spm=0.0.0.0.zulDHJ&id=39911620022&origin=15_global_en_purifier-app_purifier-app";
			break;
		case R.id.buyonline_hepa:
			uri="http://detail.tmall.com/item.htm?id=39899461374&origin=15_global_en_purifier-app_purifier-app";
			break;
		default:
			break;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		getActivity().startActivity(Intent.createChooser(intent,""));

	} 

}