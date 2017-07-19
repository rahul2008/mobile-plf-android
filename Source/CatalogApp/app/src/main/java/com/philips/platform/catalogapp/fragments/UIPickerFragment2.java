package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentUiPicker2Binding;
import com.philips.platform.catalogapp.databinding.FragmentUiPickerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kunal on 17/07/17.
 */

public class UIPickerFragment2 extends BaseFragment implements AdapterView.OnItemSelectedListener{

    private FragmentUiPicker2Binding fragmentUiPicker2Binding;
    private Spinner spinner;
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
            "Kansas","Arkansas",
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
    private ArrayAdapter<String> dataAdapter;

    @Override
    public int getPageTitle() {
        return R.string.page_title_uipicker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentUiPicker2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ui_picker2, container, false);
        fragmentUiPicker2Binding.setFrag(this);

        // Spinner element
        spinner = (Spinner) fragmentUiPicker2Binding.getRoot().findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.uipicker_item_text, STATES);

        // Drop down layout style - list view with radio button
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        return fragmentUiPicker2Binding.getRoot();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        //view.setSelected(true);
        spinner.setSelection(position, true);
        dataAdapter.notifyDataSetChanged();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}