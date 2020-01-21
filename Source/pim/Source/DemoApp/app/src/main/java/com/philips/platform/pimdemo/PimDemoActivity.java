package com.philips.platform.pimdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;

import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.Button;
import com.pim.demouapp.PIMDemoUAppActivity;

import java.util.ArrayList;
import java.util.List;

public class PimDemoActivity extends UIDActivity {
    private AppCompatSpinner selectLibreary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pim_demo);
        Button launchUApp = findViewById(R.id.launch);
        launchUApp.setOnClickListener(v -> {
            Intent intent = new Intent(PimDemoActivity.this, PIMDemoUAppActivity.class);
            intent.putExtra("SelectedLib", selectLibreary.getSelectedItem().toString());
            startActivity(intent);
        });

        selectLibreary = findViewById(R.id.selectLibrary);
        List<String> libraryList = new ArrayList<>();
        libraryList.add("PIM");
        libraryList.add("USR");
        ArrayAdapter libraryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, libraryList);
        selectLibreary.setAdapter(libraryAdapter);

    }

}
