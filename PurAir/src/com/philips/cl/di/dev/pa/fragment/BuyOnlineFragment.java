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
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FilterStatusView;

public class BuyOnlineFragment extends BaseFragment {
	private BuyDataAdapter mAdapter;
	private FilterStatusView filterView;
	private TextView lblFilter;
	private TextView txtFilterStatus;
	private AirPortInfo airPortInfo;
	
	private ListView mList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.buy_online_fragment, container, false);
		mList = (ListView) view.findViewById(R.id.list);
		
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return view;
		
		airPortInfo = ((MainActivity) parent).getAirPortInfo();
		
		/* Create a list with known data */
		ArrayList<ProductDto> list = new ArrayList<ProductDto>();
		list.add(new ProductDto("Philips Smart Air Purifier", "000 000 000",
				"Philips.com.cn", R.drawable.purifier));
		list.add(new ProductDto("Pre-Filter", "000 000 000", "Philips.com.cn",
				R.drawable.filter));
		list.add(new ProductDto("Multi Care Filter", "000 000 000",
				"Philips.com.cn", R.drawable.multi_care));
		list.add(new ProductDto("Active Carbon", "000 000 000",
				"Philips.com.cn", R.drawable.carbon_filter));
		list.add(new ProductDto("HEPA Filter", "000 000 000", "Philips.com.cn",
				R.drawable.hepa_filter));

		/* Create an adapter to display the loaded data. */
		mAdapter = new BuyDataAdapter(getActivity(), list);
		mList.setAdapter(mAdapter);
		
		return view;
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
		public View getView(int position, View convertView, ViewGroup parent) {
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
			lblFilter = (TextView) view.findViewById(R.id.lbl_filter);
			txtFilterStatus = (TextView) view
					.findViewById(R.id.txt_filter_status);

			if (airPortInfo != null) {
				switch (position) {
				case 0:
					filterView.setVisibility(View.GONE);
					txtFilterStatus.setVisibility(View.GONE);
					lblFilter.setVisibility(View.GONE);
					break;
				case 1:
					filterView.setVisibility(View.VISIBLE);
					txtFilterStatus.setVisibility(View.VISIBLE);
					lblFilter.setVisibility(View.VISIBLE);
					lblFilter.setText(R.string.pre_filter);
					filterView
							.setPrefilterValue(airPortInfo.getFilterStatus1());
					txtFilterStatus
							.setText(Utils
									.getPreFilterStatusText(airPortInfo.getFilterStatus1()));
					break;
				case 2:
					filterView.setVisibility(View.VISIBLE);
					txtFilterStatus.setVisibility(View.VISIBLE);
					lblFilter.setVisibility(View.VISIBLE);
					lblFilter.setText(R.string.multicarefilter);
					filterView
							.setMultiCareFilterValue(airPortInfo.getFilterStatus2());
					txtFilterStatus
							.setText(Utils
									.getMultiCareFilterStatusText(airPortInfo.getFilterStatus2()));
					break;
				case 3:
					filterView.setVisibility(View.VISIBLE);
					txtFilterStatus.setVisibility(View.VISIBLE);
					lblFilter.setVisibility(View.VISIBLE);
					lblFilter.setText(R.string.activecarbonfilter);
					filterView
							.setActiveCarbonFilterValue(airPortInfo.getFilterStatus3());
					txtFilterStatus
							.setText(Utils
									.getActiveCarbonFilterStatusText(airPortInfo.getFilterStatus3()));
					break;
				case 4:
					filterView.setVisibility(View.VISIBLE);
					txtFilterStatus.setVisibility(View.VISIBLE);
					lblFilter.setVisibility(View.VISIBLE);
					lblFilter.setText(R.string.hepa_filter);
					filterView
							.setHEPAfilterValue(airPortInfo.getFilterStatus4());
					txtFilterStatus
							.setText(Utils
									.getHEPAFilterFilterStatusText(airPortInfo.getFilterStatus4()));
					break;

				default:
					break;
				}
			} else {
				filterView.setVisibility(View.GONE);
				txtFilterStatus.setVisibility(View.GONE);
				lblFilter.setVisibility(View.GONE);
			}

			productName.setTypeface(Fonts.getGillsans(getActivity()));
			productPrice.setTypeface(Fonts.getGillsans(getActivity()));
			availability.setTypeface(Fonts.getGillsans(getActivity()));
			buyOnline.setTypeface(Fonts.getGillsans(getActivity()));

			productName.setText(item.getProductName());
			productPrice.setText(item.getProductPrice());
			availability.setText("available to purchase from "
					+ item.getAvailability());
			productImg.setImageResource(item.getProductImg());

			buyOnline.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://shop.philips.com.cn/product/JY0002/detail.htm"));
					startActivity(intent);
				}
			});

			return view;
		}
	}
}
