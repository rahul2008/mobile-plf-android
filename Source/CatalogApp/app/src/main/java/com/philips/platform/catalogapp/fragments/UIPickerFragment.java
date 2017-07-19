/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentUiPickerBinding;
import com.philips.platform.uid.view.widget.UIPicker;

public class UIPickerFragment extends BaseFragment {

    private FragmentUiPickerBinding fragmentUiPickerBinding;
    private UIPicker listPopupWindow;
    public static final String SELECTED_LOCATION = "SELECTED_LOCATION";
    public static final String SELECTED_POSITION = "SELECTED_POSITION";
    public final ObservableField<String> selectedLocation = new ObservableField<>("Alabama");
    private ArrayAdapter adapter;
    private int selectedPosition = 0;
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

        listPopupWindow = new UIPicker(getContext());
        adapter = new ArrayAdapter<>(getContext(), R.layout.uipicker_item_text, STATES);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(fragmentUiPickerBinding.selectedLocationLabel);
        listPopupWindow.setModal(true);
        //listPopupWindow.setHeight(600);
        //listPopupWindow.setWidth(300);
        //listPopupWindow.setDropDownGravity(Gravity.START);

        listPopupWindow.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedLocation.set(STATES[position]);
                        //view.setSelected(true);
                        selectedPosition = position;
                        listPopupWindow.setSelection(position);

                        listPopupWindow.dismiss();
                    }
                }
        );

        return fragmentUiPickerBinding.getRoot();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SELECTED_LOCATION, selectedLocation.get());
        outState.putInt(SELECTED_POSITION, selectedPosition);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String savedLocation = savedInstanceState.getString(SELECTED_LOCATION);
            selectedLocation.set(savedLocation);
            selectedPosition = savedInstanceState.getInt(SELECTED_POSITION);
            //listPopupWindow.setSelection(selectedPosition);
        }
    }

    public void showListPopupWindow(){
        //listPopupWindow.setVerticalOffset(-fragmentUiPickerBinding.anchorLayout.getHeight());
        //listPopupWindow.shouldShowBelowAnchorView(true);

        listPopupWindow.show();
        listPopupWindow.setSelection(selectedPosition);

        /*listPopupWindow.setHeight(600);
        listPopupWindow.setWidth(600);
        listPopupWindow.setDropDownGravity(Gravity.START);
        listPopupWindow.shouldShowBelowAnchorView(true);*/

    }

}