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
	private int mTutorialDesc;
	private int mTutorialImage;
	private int mTutorialInstruction;

	public static AirTutorialViewFragment newInstance(int content, int image,
			int instruction) {
		AirTutorialViewFragment fragment = new AirTutorialViewFragment();

		fragment.mTutorialDesc = content;
		fragment.mTutorialImage = image;
		fragment.mTutorialInstruction = instruction;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.air_tutorial_view, container,	false);
		initializeView(view);
		return view;
	}

	private void initializeView(View view) {

		ImageView tutorialImg = (ImageView) view.findViewById(R.id.tutorial_img);
		TextView tutorialDesc = (TextView) view.findViewById(R.id.tutorial_desc);
		TextView tutorialInstruction = (TextView) view.findViewById(R.id.lbl_instruction);

		tutorialDesc.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		tutorialInstruction.setTypeface(Fonts.getCentraleSansLight(getActivity()));

		tutorialDesc.setText(getActivity().getText(mTutorialDesc));
		tutorialImg.setImageResource(mTutorialImage);
		tutorialInstruction.setText(getActivity().getText(mTutorialInstruction));

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
