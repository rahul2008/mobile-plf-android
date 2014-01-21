package com.philips.cl.di.dev.pa.screens.customviews;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.interfaces.PercentDetailsClickListener;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PercentBarLayout extends LinearLayout {

	 PercentDetailsClickListener mCallback;
	/**
	 * Constructor
	 * @param Context
	 * @param AttributeSet
	 * @param int number of view to show
	 * */
	public PercentBarLayout(final Context context, AttributeSet attrs,  
			final int num, PercentDetailsClickListener pCallback, final int index, final int position) {
		super(context, attrs);
		//System.out.println("INDEXXXXXXXXXXXXXXX=="+index);
		mCallback = pCallback;
		for (int i = 0; i < num; i++) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.indoor_dboard_percent_bar, null);
			RelativeLayout indoorDashboardBarPerc = (RelativeLayout)v.findViewById(R.id.indoorDashboardBarPerc);
			TextView serialNum = (TextView)v.findViewById(R.id.indoorDashboardBarNum);
			final ImageView indexBg = (ImageView) v.findViewById(R.id.indoorDashboardBarNumBg);
			indoorDashboardBarPerc.addView(new AirView(context, 100, 100, 150));
			serialNum.setText(""+(i + 1));
			v.setPadding(10, 10, 10, 10);
			
			/**
			 * Item click listener
			 * */
			final int tempi = i;
			
			if (i == position) {
				indexBg.setImageResource(R.drawable.aqi_small_circle_100_150_2x);
			}
			
			if (tempi != num - 1) {
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mCallback.clickedPosition(tempi, index);
						//Toast.makeText(context, "Item Click " + tempi, Toast.LENGTH_SHORT).show();
					}
				});
			}else {
				v.setBackgroundColor(Color.TRANSPARENT);
			}
			LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			this.addView(v, parentParams);
		}
	}

	@Override
	public void setClickable(boolean clickable) {
		super.setClickable(clickable);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
	}

	@Override
	public View getChildAt(int index) {
		// TODO Auto-generated method stub
		return super.getChildAt(index);
	}

	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return super.getChildCount();
	}

	@Override
	public int indexOfChild(View child) {
		// TODO Auto-generated method stub
		return super.indexOfChild(child);
	}

	@Override
	@CapturedViewProperty
	public int getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}
}