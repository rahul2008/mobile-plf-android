/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
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

import java.util.ArrayList;

public class ExpanderFragment extends BaseFragment {
    UIDExpanderListener mUidExpanderListener = new UIDExpanderListener() {

        @Override
        public void expanderPanelExpanded(Expander expander) {
            Log.v("Expand", "expanderPanelExpanded");
            collapseOtherExpanders(expander);
        }

        @Override
        public void expanderPanelCollapsed(Expander expander) {
            Log.v("Expand", "expanderPanelCollapsed");
        }
    };


    private FragmentExpanderBinding fragmentExpanderBinding;
    private ArrayList<Expander> gallerayExpanderList;


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
        fragmentExpanderBinding.catalogExpanderTwo.setExpanderListener(mUidExpanderListener);


        //Expandder 3
        //fragmentExpanderBinding.catalogExpanderThree.setExpanderTitle("Multiple line title which a bit longer");
        fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_balloonspeech));
        fragmentExpanderBinding.catalogExpanderThree.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        fragmentExpanderBinding.catalogExpanderThree.getTitleLabel().setText("Multiple line title which a bit longer");
        fragmentExpanderBinding.catalogExpanderThree.setExpanderListener(mUidExpanderListener);


        // expander 4
        fragmentExpanderBinding.catalogExpanderFour.setExpanderTitle("Multiple line title which a bit longer in this and can hold additional information");
        fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_calendar));
        RelativeLayout rl = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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
        fragmentExpanderBinding.catalogExpanderFour.setExpanderListener(mUidExpanderListener);


        gallerayExpanderList = new ArrayList<Expander>();
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderOne);
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderTwo);
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderThree);
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderFour);


    }

    public void enableIcon(boolean enabled) {
        if (enabled) {
            fragmentExpanderBinding.catalogExpanderOne.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_star));
            fragmentExpanderBinding.catalogExpanderTwo.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_personportrait));
            fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_balloonspeech));
            fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(getActivity().getResources().getString(R.string.dls_calendar));
        } else {
            fragmentExpanderBinding.catalogExpanderOne.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderTwo.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(null);

        }
    }

    public void openMultiline(boolean enabled) {
        fragmentExpanderBinding.catalogExpanderOne.expand(enabled);
        fragmentExpanderBinding.catalogExpanderTwo.expand(enabled);
        fragmentExpanderBinding.catalogExpanderThree.expand(enabled);
        fragmentExpanderBinding.catalogExpanderFour.expand(enabled);
    }

    private void collapseOtherExpanders(Expander aExpander) {
        if (!fragmentExpanderBinding.toggleIconOpenMultipleItems.isChecked()) {
            for (Expander expander : gallerayExpanderList) {
                if (!expander.equals(aExpander)) {
                    expander.expand(false);
                }
            }
        }

    }
}
