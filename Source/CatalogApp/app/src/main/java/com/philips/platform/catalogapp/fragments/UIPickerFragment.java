/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentUiPickerBinding;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIPicker;

public class UIPickerFragment extends BaseFragment {

    private UIPicker listPopupWindow;
    public static final String SELECTED_LOCATION = "SELECTED_LOCATION";
    public static final String SELECTED_POSITION = "SELECTED_POSITION";
    public final ObservableField<String> selectedLocation = new ObservableField<>("Alabama");
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
        FragmentUiPickerBinding fragmentUiPickerBinding;
        Context popupThemedContext = UIDHelper.getPopupThemedContext(getContext());
        fragmentUiPickerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ui_picker, container, false);
        fragmentUiPickerBinding.setFrag(this);

        listPopupWindow = new UIPicker(popupThemedContext);
        ArrayAdapter adapter = new PickerAdapter(popupThemedContext, R.layout.uipicker_item_text, STATES);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(fragmentUiPickerBinding.selectedLocationLabel);
        listPopupWindow.setModal(true);
        //listPopupWindow.setHeight(600);
        //listPopupWindow.setWidth(300);
        //listPopupWindow.setDropDownGravity(Gravity.START);
        //listPopupWindow.shouldNotOverlapAnchorView(true);

        listPopupWindow.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedLocation.set(STATES[position]);
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
        }
    }

    public void showListPopupWindow(){
        listPopupWindow.show();
        listPopupWindow.setSelection(selectedPosition);
    }


    class PickerAdapter extends ArrayAdapter<String> {
        LayoutInflater inflater;
        int resID;
        public PickerAdapter(@NonNull Context context, @LayoutRes int resource, String[] states) {
            super(context, resource, states);
            resID = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(UIPickerFragment.this.getContext()));
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Label view;
            if (convertView == null) {
                view = (Label) inflater.inflate(resID, parent, false);
            } else {
                view = (Label) convertView;
            }
            view.setText(getItem(position));
            return view;
        }
    }
}