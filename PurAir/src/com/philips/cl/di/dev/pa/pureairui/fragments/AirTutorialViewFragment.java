package com.philips.cl.di.dev.pa.pureairui.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.utils.Fonts;

public class AirTutorialViewFragment extends Fragment{
    private int mTutorialDesc;
    private int mTutorialImage;
    private int mTutorialInstruction;
    private int[] mInstructionList;

    public static AirTutorialViewFragment newInstance(int content, int image, int instruction, int[] instructionList) {
    	AirTutorialViewFragment fragment = new AirTutorialViewFragment();
        
    	fragment.mTutorialDesc = content;
        fragment.mTutorialImage= image;
        fragment.mTutorialInstruction=instruction;
        fragment.mInstructionList=instructionList;
        return fragment;
    }    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.air_tutorial_view, container, false);
		initializeView(view);
		return view;    	
    }

    private void initializeView(View view){    	
    	
    	ImageView tutorialImg= (ImageView) view.findViewById(R.id.tutorial_img);
    	TextView tutorialDesc= (TextView) view.findViewById(R.id.tutorial_desc);
    	TextView tutorialInstruction=(TextView) view.findViewById(R.id.lbl_instruction);    	
    	
    	tutorialDesc.setTypeface(Fonts.getGillsans(getActivity()));
    	tutorialInstruction.setTypeface(Fonts.getGillsans(getActivity()));
    	
    	tutorialDesc.setText(getActivity().getText(mTutorialDesc));
    	tutorialImg.setImageResource(mTutorialImage);   	
    	tutorialInstruction.setText(getActivity().getText(mTutorialInstruction));
    	
    	/*if(mInstructionList!=null){
    		
    		TextView instruction1=(TextView) view.findViewById(R.id.dashboard_instruction_1);
    		TextView instruction2=(TextView) view.findViewById(R.id.dashboard_instruction_2);
    		TextView instruction3=(TextView) view.findViewById(R.id.dashboard_instruction_3);
    		TextView instruction4=(TextView) view.findViewById(R.id.dashboard_instruction_4);
    		
    		instruction1.setTypeface(Fonts.getGillsans(getActivity()));
    		instruction2.setTypeface(Fonts.getGillsans(getActivity()));
    		instruction3.setTypeface(Fonts.getGillsans(getActivity()));
    		instruction4.setTypeface(Fonts.getGillsans(getActivity()));    		
    		
    		instruction1.setVisibility(View.VISIBLE);
    		instruction2.setVisibility(View.VISIBLE);
    		instruction3.setVisibility(View.VISIBLE);
    		instruction4.setVisibility(View.VISIBLE);
    		
    	}*/
	}    
    
    public void setMargins (TextView v, int l, int t, int r, int b) {
    	Resources resources = getResources();
    	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
    	int processedPx=Math.round(px);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	params.setMargins(l*processedPx, t*processedPx, r*processedPx, b*processedPx);       
    	v.setLayoutParams(params);
    }
	
}
