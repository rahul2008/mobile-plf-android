package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.view.widget.Expander;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIDExpanderDelegate;

public class ExpanderFragmentOne extends  BaseFragment{
    UIDExpanderDelegate mUidExpanderDelegate = new UIDExpanderDelegate() {
        @Override
        public void expanderPanelWillExpand() {
            Log.v("Expand","expanderPanelWillExpand");
        }

        @Override
        public void expanderPanelDidExpand() {
            Log.v("Expand","expanderPanelDidExpand");
        }

        @Override
        public void expanderPanelWillCollapse() {
            Log.v("Expand","expanderPanelWillCollapse");
        }

        @Override
        public void expanderPanelDidCollapse() {
            Log.v("Expand","expanderPanelDidCollapse");
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

        //expanderOne.setExpanderTitle("Short title");
        expanderOne.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_calendar));
        expanderOne.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        expanderOne.setExpanderDelegate(mUidExpanderDelegate);
       ;
      //  expanderOne.setTitleImage(getActivity().getResources().getDrawable(R.drawable.alert_dialog_icon));
       // RelativeLayout relativeLayout = inflater.inflate(R.layout.fragment_expander_content_default_layout, null, false);



      /*  ViewStub viee = expanderOne.findViewById(R.id.uid_expander_view_content);
        viee.setLayoutResource(R.layout.fragment_expander_content_default_layout);
        RelativeLayout rl= (RelativeLayout) viee.inflate();*/

       //View contentView = getLayoutInflater().inflate(R.layout.fragment_expander_content_default_layout, relativeLayout, false);
        expanderTwo.setExpanderTitle("Long title: Life has got all those twists and turns. You've got to hold on tight and off you go. With the right kind of coaching and determination you can accomplish anything. ");
        expanderTwo.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_circleplay));
        expanderTwo.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        expanderTwo.setSeparatorVisible(false);

        Log.v("isExpand",""+expanderTwo.isExpanded());
        expanderTwo.expand(true);
        Log.v("isExpand",""+expanderTwo.isExpanded());
        //expanderTwo.getTitleLabel().setText("customise Expander content new");



      /*  LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl.addView(1, layoutInflater.inflate(R.layout.fragment_expander_content_default_layout, this, false) );*/
        expanderThree.setExpanderTitle("title with empty content");
        expanderThree.getTitleLabel().setText("title with empty content (new)");





        // expander 4
        expanderFour.setExpanderTitle("Expander dynamic layout title");
        RelativeLayout rl = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lp =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        Label label = new Label(getContext());
        label.setText("customise Expander title");
        RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paramsLabel.addRule(RelativeLayout.CENTER_HORIZONTAL);
        label.setGravity(Gravity.CENTER);
        label.setLayoutParams(paramsLabel);
        rl.addView(label);
        expanderFour.setExpanderCustomPanelView(rl);
        Label label1 = new Label(getContext());
        label1.setText("customise Expander content");
        label1.setLayoutParams(paramsLabel);
        expanderFour.setExpanderContentView(label1);
        expanderFour.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        expanderFour.setExpanderContentView(label1);
//        expanderFour.getTitleLabel().setText("customise Expander title new");

    }
}
