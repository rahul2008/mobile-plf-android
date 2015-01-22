package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.digitalcare.R;

public class ViewProductDetailsFragment extends BaseFragment {

	private static String TAG = ViewProductDetailsFragment.class.getSimpleName();
	private Context context;
	private float WIDTH, HEIGHT;

	/**
	 * PORTRAIT PHONE
	 */
//	private RelativeLayout mProductDetailsContainer, mProductButtonsContainer,
//			mProductVideoAdsContainer;
//	private ImageView mProductImage;
//	private TextView mProductName, mProductVariant;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_view_product,
				container, false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		context = getActivity().getBaseContext();
		init();

	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(getActivity(), "Landscape", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(getActivity(), "Portrait", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void init() {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		WIDTH = (float) width / 1000;
		HEIGHT = (float) height / 1000;
		Log.d(TAG, "Width & Height are : " + width + " & Height : " + height);
		Log.d(TAG, "WIDTH & HEIGHT are : " + WIDTH + " & Height : " + HEIGHT);

	}

}
