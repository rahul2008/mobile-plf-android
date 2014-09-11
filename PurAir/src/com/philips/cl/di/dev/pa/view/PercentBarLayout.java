package com.philips.cl.di.dev.pa.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.HomeOutdoorData;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.PercentDetailsClickListener;

public class PercentBarLayout extends LinearLayout {

	/**
	 * Constructor
	 * 
	 * @param Context
	 * @param AttributeSet
	 * @param int number of view to show
	 * */
	public PercentBarLayout(final Context context, AttributeSet attrs,
			List<Integer> goodAirInfos, PercentDetailsClickListener pCallback,
			final int index, final int position) {
		super(context, attrs);
		for (int i = 0; i < 2; i++) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.indoor_dboard_percent_bar, null);
			RelativeLayout indoorDashboardBarPerc = (RelativeLayout) view
					.findViewById(R.id.indoorDashboardBarPerc);
			TextView serialNum = (TextView) view
					.findViewById(R.id.indoorDashboardBarNum);
			FontTextView name = (FontTextView) view
					.findViewById(R.id.indoorDashboardBarName);
			final ImageView indexBg = (ImageView) view
					.findViewById(R.id.indoorDashboardBarNumBg);

			serialNum.setText("" + (i + 1));
			view.setPadding(10, 10, 10, 10);
			FontTextView percentTxt = new FontTextView(context);
			RelativeLayout.LayoutParams percentTxtParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			percentTxtParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			percentTxt.setGravity(Gravity.CENTER);
			percentTxt.setTextColor(Color.WHITE);
			percentTxt.setTextSize(21);
			if (i == 1) {
				name.setText(context.getString(R.string.outdoor_db));
				try {
					List<Integer> outdoorAqiPercentList = 
							HomeOutdoorData.getInstance().getOutdoorPercentList();
					if (outdoorAqiPercentList != null && outdoorAqiPercentList.size() > 0) {
						indoorDashboardBarPerc.addView(
								new AirView(context,	outdoorAqiPercentList.get(index), 60, 80));
						percentTxt.setText(outdoorAqiPercentList.get(index)	+ "%");
						indoorDashboardBarPerc.addView(percentTxt, percentTxtParams);
					}

				} catch (Exception e) {
					ALog.e(ALog.INDOOR_RDCP, e.getMessage());
				}
			} else {
				try {
					if (goodAirInfos != null) {
						indoorDashboardBarPerc.addView(
								new AirView(context,	goodAirInfos.get(index), 60, 80));
						percentTxt.setText(goodAirInfos.get(index) + "%");
						indoorDashboardBarPerc.addView(percentTxt,	percentTxtParams);
					}
				} catch (Exception e) {
					ALog.e(ALog.INDOOR_RDCP, e.getMessage());
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
			this.addView(view, parentParams);
		}
	}

}