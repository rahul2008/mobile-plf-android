package com.philips.cl.di.dev.pa.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class AirTutorialViewFragment extends BaseFragment {
	public static final String KEY_DESCRIP = "descrip"; 
	public static final String KEY_IMG = "img"; 
	public static final String KEY_INSTR = "instr"; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.air_tutorial_view, container,	false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ImageView tutorialImg = (ImageView) getView().findViewById(R.id.tutorial_img);
		TextView tutorialDesc = (TextView) getView().findViewById(R.id.tutorial_desc);
		TextView tutorialInstruction = (TextView) getView().findViewById(R.id.lbl_instruction);

		tutorialDesc.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		tutorialInstruction.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			tutorialDesc.setText(getString(bundle.getInt(KEY_DESCRIP, R.string.tutorial_dashboard_desc)));
			tutorialImg.setImageResource(bundle.getInt(KEY_IMG, R.drawable.tutorial_page1));
			tutorialInstruction.setText(getString(bundle.getInt(KEY_INSTR, R.string.dashboard_instruction)));
		}
	}

	public void setMargins(TextView v, int l, int t, int r, int b) {
		Resources resources = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
		int processedPx = Math.round(px);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		params.setMargins(l * processedPx, t * processedPx, r * processedPx, b * processedPx);
		v.setLayoutParams(params);
	}

}
