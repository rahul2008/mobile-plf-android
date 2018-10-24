package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentExpanderBinding;
import com.philips.platform.uid.view.widget.Expander;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIDExpanderListener;

public class ExpanderFragment extends  BaseFragment{
    UIDExpanderListener mUidExpanderListener = new UIDExpanderListener() {

        @Override
        public void expanderPanelExpanded() {
            Log.v("Expand","expanderPanelExpanded");
        }

        @Override
        public void expanderPanelCollapsed() {
            Log.v("Expand","expanderPanelCollapsed");
        }
    };



    private FragmentExpanderBinding fragmentExpanderBinding;



    @Override
    public int getPageTitle() {
        return R.string.page_title_expander;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentExpanderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expander, container, false);
        fragmentExpanderBinding.setFrag(this);
        return fragmentExpanderBinding.getRoot();

    /*    ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_expander, container, false);
        expanderOne = (Expander)view.findViewById(R.id.catalog_expander_one);


        Label lb =(Label)view.findViewById(R.id.sampleTextID);

        expanderTwo = (Expander)view.findViewById(R.id.catalog_expander_two);



        expanderThree = (Expander)view.findViewById(R.id.catalog_expander_three);



        expanderFour = (Expander)view.findViewById(R.id.catalog_expander_four);

        return  view;*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentExpanderBinding.toggleIconWithIcons.setChecked(true);
        fragmentExpanderBinding.catalogExpanderOne.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_star));
        fragmentExpanderBinding.catalogExpanderOne.setExpanderTitle("Single line title");
        fragmentExpanderBinding.catalogExpanderOne.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        fragmentExpanderBinding.catalogExpanderOne.setExpanderListener(mUidExpanderListener);
       ;

       //View contentView = getLayoutInflater().inflate(R.layout.fragment_expander_content_default_layout, relativeLayout, false);
        fragmentExpanderBinding.catalogExpanderTwo.setExpanderTitle("Single line title");
        fragmentExpanderBinding.catalogExpanderTwo.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_personportrait));
        fragmentExpanderBinding.catalogExpanderTwo.setExpanderContentView(R.layout.fragment_expander_content_default_layout);


      /*  expanderTwo.setSeparatorVisible(false);
        Log.v("isExpand",""+expanderTwo.isExpanded());
        expanderTwo.expand(true);
        Log.v("isExpand",""+expanderTwo.isExpanded());
        //expanderTwo.getTitleLabel().setText("customise Expander content new");
*/

        //Expandder 3
        fragmentExpanderBinding.catalogExpanderThree.setExpanderTitle("Multiple line title which a bit longer");
        fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_balloonspeech));
        fragmentExpanderBinding.catalogExpanderThree.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
       // expanderThree.getTitleLabel().setText("title with empty content (new)");





        // expander 4
        fragmentExpanderBinding.catalogExpanderFour.setExpanderTitle("Multiple line title which a bit longer in this and can hold additional information");
        fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_calendar));
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
        fragmentExpanderBinding.catalogExpanderFour.setExpanderContentView(R.layout.fragment_expander_content_default_layout);

//        expanderFour.getTitleLabel().setText("customise Expander title new"); // this line will throw null pntr exception


    }

    public void enableIcon(boolean enabled){
        if(enabled) {
            fragmentExpanderBinding.catalogExpanderOne.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_star));
            fragmentExpanderBinding.catalogExpanderTwo.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_personportrait));
            fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_balloonspeech));
            fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_calendar));
        }else{
            fragmentExpanderBinding.catalogExpanderOne.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderTwo.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(null);

        }
    }

    public void openMultiline(boolean enabled){
        fragmentExpanderBinding.catalogExpanderOne.expand(enabled);
        fragmentExpanderBinding.catalogExpanderTwo.expand(enabled);
        fragmentExpanderBinding.catalogExpanderThree.expand(enabled);
        fragmentExpanderBinding.catalogExpanderFour.expand(enabled);
    }
}
