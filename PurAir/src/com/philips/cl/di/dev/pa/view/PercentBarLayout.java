package com.philips.cl.di.dev.pa.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.interfaces.PercentDetailsClickListener;
import com.philips.cl.di.dev.pa.utils.Utils;


public class PercentBarLayout extends LinearLayout {

	/**
	 * Constructor
	 * @param Context
	 * @param AttributeSet
	 * @param int number of view to show
	 * */
	public PercentBarLayout(final Context context, AttributeSet attrs, List<Integer> goodAirInfos, 
			PercentDetailsClickListener pCallback, final int index, final int position) {
		super(context, attrs);
		for (int i = 0; i < 2; i++) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.indoor_dboard_percent_bar, null);
			RelativeLayout indoorDashboardBarPerc = 
					(RelativeLayout)v.findViewById(R.id.indoorDashboardBarPerc);
			TextView serialNum = (TextView)v.findViewById(R.id.indoorDashboardBarNum);
			CustomTextView name = (CustomTextView)v.findViewById(R.id.indoorDashboardBarName);
			final ImageView indexBg = (ImageView) v.findViewById(R.id.indoorDashboardBarNumBg);
			
			serialNum.setText(""+(i + 1));
			v.setPadding(10, 10, 10, 10);
			
			if (i == 1) {
				name.setText(context.getString(R.string.outdoor_db));
				try {
					if (Utils.outdoorAQIPercentageList != null) {
						indoorDashboardBarPerc.addView(
								new AirView(context, Utils.outdoorAQIPercentageList.get(index), 60, 80));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				try {
					if (goodAirInfos != null) {
						indoorDashboardBarPerc.addView(new AirView(context, goodAirInfos.get(index), 60, 80));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			/**
			 * Item click listener
			 * */
			
			if (i == position) {
				indexBg.setImageResource(R.drawable.circle_5);
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