/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentUiPickerBinding;

public class UIPickerFragment extends BaseFragment {

    private FragmentUiPickerBinding fragmentUiPickerBinding;
    private ListPopupWindow listPopupWindow;

    private static final String[] STATES = new String[]{
            "Alabama",
            "Alaska",
            "Arizona",
            "Arkansas",
            "California",
            "Colorado",
            "Connecticut",
            "Delaware",
            "Florida",
            "Georgia",
            "Hawaii",
            "Idaho",
            "Illinois",
            "Indiana",
            "Iowa",
            "Kansas",
            "Kentucky",
            "Louisiana"
    };

    @Override
    public int getPageTitle() {
        return R.string.page_title_uipicker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentUiPickerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ui_picker, container, false);
        fragmentUiPickerBinding.setFrag(this);

        listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getContext(), R.layout.uipicker_item_text, STATES));
        listPopupWindow.setAnchorView(fragmentUiPickerBinding.selectedLocationLabel);
        listPopupWindow.setModal(true);
        listPopupWindow.setVerticalOffset(-100);

        listPopupWindow.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        fragmentUiPickerBinding.selectedLocationLabel.setText(STATES[position]);
                        listPopupWindow.dismiss();
                    }
                }
        );

        return fragmentUiPickerBinding.getRoot();
    }

    public void showListPopupWindow(){
        listPopupWindow.show();
    }

}