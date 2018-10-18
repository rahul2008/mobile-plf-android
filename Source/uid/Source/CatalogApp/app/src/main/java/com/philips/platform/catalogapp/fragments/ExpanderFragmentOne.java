package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.view.widget.Expander;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIDExpanderListener;

public class ExpanderFragmentOne extends  BaseFragment{
    UIDExpanderListener mUidExpanderListener = new UIDExpanderListener() {
        @Override
        public void expanderPanelBeginExpanding() {
            Log.v("Expand","expanderPanelBeginExpanding");
        }

        @Override
        public void expanderPanelExpanded() {
            Log.v("Expand","expanderPanelExpanded");
        }

        @Override
        public void expanderPanelBeginCollapsing() {
            Log.v("Expand","expanderPanelBeginCollapsing");
        }

        @Override
        public void expanderPanelCollapsed() {
            Log.v("Expand","expanderPanelCollapsed");
        }
    };


    Expander expanderOne;
    Expander expanderTwo;
    Expander expanderThree;
    Expander expanderFour;



    @Override
    public int getPageTitle() {
        return R.string.page_title_expander;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_expander_one, container, false);
        expanderOne = (Expander)view.findViewById(R.id.catalog_expander_one);
        expanderOne =(Expander)view.getChildAt(0);

        Label lb =(Label)view.findViewById(R.id.sampleTextID);

        expanderTwo = (Expander)view.findViewById(R.id.catalog_expander_two);
        expanderTwo =(Expander)view.getChildAt(1);


        expanderThree = (Expander)view.findViewById(R.id.catalog_expander_three);
        expanderThree =(Expander)view.getChildAt(2);


        expanderFour = (Expander)view.findViewById(R.id.catalog_expander_four);
        expanderFour =(Expander)view.getChildAt(3);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        expanderOne.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_star));
        expanderOne.setExpanderTitle("Single line title");
        expanderOne.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        expanderOne.setExpanderDelegate(mUidExpanderListener);
       ;

       //View contentView = getLayoutInflater().inflate(R.layout.fragment_expander_content_default_layout, relativeLayout, false);
        expanderTwo.setExpanderTitle("Single line title");
        expanderTwo.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_personportrait));
        expanderTwo.setExpanderContentView(R.layout.fragment_expander_content_default_layout);


      /*  expanderTwo.setSeparatorVisible(false);
        Log.v("isExpand",""+expanderTwo.isExpanded());
        expanderTwo.expand(true);
        Log.v("isExpand",""+expanderTwo.isExpanded());
        //expanderTwo.getTitleLabel().setText("customise Expander content new");
*/

        //Expandder 3
        expanderThree.setExpanderTitle("Multiple line title which a bit longer");
        expanderThree.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_balloonspeech));
        expanderThree.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
       // expanderThree.getTitleLabel().setText("title with empty content (new)");





        // expander 4
        expanderFour.setExpanderTitle("Multiple line title which a bit longer in this and can hold additional information");
        expanderFour.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_calendar));
        RelativeLayout rl = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lp =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        Label label = new Label(getContext());
        label.setText("customise Expander title");
        RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLabel.addRule(RelativeLayout.CENTER_VERTICAL);
        label.setLayoutParams(paramsLabel);
        rl.addView(label);
      //  expanderFour.setExpanderCustomPanelView(rl);
        Label label1 = new Label(getContext());
        label1.setText("customise Expander content");
        label1.setLayoutParams(paramsLabel);
        expanderFour.setExpanderContentView(label1);

//        expanderFour.getTitleLabel().setText("customise Expander title new"); // this line will throw null pntr exception

    }
}
