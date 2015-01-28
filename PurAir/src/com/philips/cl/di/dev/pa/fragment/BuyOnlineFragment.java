package com.philips.cl.di.dev.pa.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.ProductDto;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FilterStatusView;

public class BuyOnlineFragment extends BaseFragment {
	private BuyDataAdapter mAdapter;
	private FilterStatusView filterView;
	private TextView txtFilterStatus;
	private AirPortInfo airPortInfo;
	private PurAirDevice current;

	private ListView mList;
	private ArrayList<ProductDto> list;
	private int filterVisibility;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.buy_online_fragment, container, false);
		mList = (ListView) view.findViewById(R.id.list);

		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return view;

		current = ((MainActivity) parent).getCurrentPurifier();
		if (current != null) {
			airPortInfo = current.getAirPortInfo();
		}

		/* Create a list with known data */
		list = new ArrayList<ProductDto>();
		list.add(new ProductDto(R.string.buy_philips_smart_air_purifier, "AC4373",
				"Philips.com.cn", R.drawable.purifier));
		list.add(new ProductDto(R.string.buy_multi_care_filter, "AC4151",
				"Philips.com.cn", R.drawable.multi_care));
		list.add(new ProductDto(R.string.buy_active_carbon_filter, "AC4153",
				"Philips.com.cn", R.drawable.carbon_filter));
		list.add(new ProductDto(R.string.buy_hepa_filter, "AC4154", "Philips.com.cn",
				R.drawable.hepa_filter));

		/* Create an adapter to display the loaded data. */
		mAdapter = new BuyDataAdapter(getActivity(), list);
		mList.setAdapter(mAdapter);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPageProductView("Buy online");
	}

	/* Custom Adapter to inflate custom rows of list */
	class BuyDataAdapter extends ArrayAdapter<ProductDto> {
		private final LayoutInflater mInflater;

		public BuyDataAdapter(Context context, ArrayList<ProductDto> list) {
			super(context, android.R.layout.simple_list_item_2, list);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;

			if (convertView == null) {
				view = mInflater.inflate(R.layout.buy_online_list_item, parent,
						false);
			} else {
				view = convertView;
			}

			ProductDto item = getItem(position);
			TextView productName = (TextView) view
					.findViewById(R.id.lbl_product_name);
			TextView productPrice = (TextView) view
					.findViewById(R.id.lbl_product_price);
			TextView availability = (TextView) view
					.findViewById(R.id.lbl_availability);
			ImageView productImg = (ImageView) view
					.findViewById(R.id.product_img);
			Button buyOnline = (Button) view.findViewById(R.id.btn_buy_online);

			filterView = (FilterStatusView) view
					.findViewById(R.id.seekbar_filter);
			txtFilterStatus = (TextView) view
					.findViewById(R.id.txt_filter_status);
			
			if(current == null || current.getConnectionState() == ConnectionState.DISCONNECTED)
			{
				filterVisibility=View.GONE;
			}
			else
			{
				filterVisibility=View.VISIBLE;
			}

			if (airPortInfo != null) {
				switch (position) {
				case 0:
					setFilterVisibility(View.GONE);
					break;
				case 1:
					setFilterVisibility(filterVisibility);
					filterView
					.setMultiCareFilterValue(airPortInfo.getMulticareFilterStatus());
					txtFilterStatus
					.setText(Utils
							.getMultiCareFilterStatusText(airPortInfo.getMulticareFilterStatus()));
					break;
				case 2:
					setFilterVisibility(filterVisibility);
					filterView
					.setActiveCarbonFilterValue(airPortInfo.getActiveFilterStatus());
					txtFilterStatus
					.setText(Utils
							.getActiveCarbonFilterStatusText(airPortInfo.getActiveFilterStatus()));
					break;
				case 3:
					setFilterVisibility(filterVisibility);
					filterView
					.setHEPAfilterValue(airPortInfo.getHepaFilterStatus());
					txtFilterStatus
					.setText(Utils
							.getHEPAFilterFilterStatusText(airPortInfo.getHepaFilterStatus()));
					break;

				default:
					break;
				}
			} else {
				setFilterVisibility(View.GONE);
			}

			buyOnline.setTypeface(Fonts.getGillsans(getActivity()));

			productName.setText(getActivity().getString(item.getProductName()));
			productPrice.setText(item.getProductPrice());
			availability.setText(String.format(getString(R.string.available_from), item.getAvailability()));
			productImg.setImageResource(item.getProductImg());

			buyOnline.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String uri="";
					MetricsTracker.trackActionBuyButton();
					switch (position) {
					case 0:
						uri="http://www.philips.com.cn/c/air-purifier/ac4373_00/prd/?origin=15_global_en_purifier-app_purifier-app";
						break;
					case 1:
						uri="http://detail.tmall.com/item.htm?spm=0.0.0.0.4pERVR&id=39880338072&origin=15_global_en_purifier-app_purifier-app";
						break;
					case 2:
						uri="http://detail.tmall.com/item.htm?spm=0.0.0.0.zulDHJ&id=39911620022&origin=15_global_en_purifier-app_purifier-app";
						break;
					case 3:
						uri="http://detail.tmall.com/item.htm?id=39899461374&origin=15_global_en_purifier-app_purifier-app";
						break;
					default:
						break;
					}
					
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
					startActivity(Intent.createChooser(intent,""));
				}
			});

			return view;
		}


		private void setFilterVisibility(int visibility){
			
			filterView.setVisibility(visibility);
			txtFilterStatus.setVisibility(visibility);
		}
	}
	
	
	public void updateFilterStatus(PurAirDevice purifier){
		if(purifier==null || getActivity()==null) return;
		
		current=purifier;
		airPortInfo= purifier.getAirPortInfo();
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(filterVisibility==View.VISIBLE && current!=null && current.getConnectionState() != ConnectionState.DISCONNECTED) return;
				
				if(filterVisibility==View.GONE && current!=null && current.getConnectionState() == ConnectionState.DISCONNECTED) return;
				
				mList.setAdapter(null);
				mList.setAdapter(mAdapter);				
			}
		});			
	}
}
