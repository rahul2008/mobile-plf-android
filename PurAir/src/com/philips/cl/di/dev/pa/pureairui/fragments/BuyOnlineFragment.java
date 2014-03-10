package com.philips.cl.di.dev.pa.pureairui.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import com.philips.cl.di.dev.pa.dto.ProductDto;
import com.philips.cl.di.dev.pa.utils.Fonts;

public class BuyOnlineFragment extends ListFragment {
	private BuyDataAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Log.i(" BuyOnline", "Item clicked: " + id);
		Intent intent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://shop.philips.com.cn/product/JY0002/detail.htm"));
		startActivity(intent);
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
