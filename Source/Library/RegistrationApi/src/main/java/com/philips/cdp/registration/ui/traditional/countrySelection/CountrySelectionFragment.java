package com.philips.cdp.registration.ui.traditional.countrySelection;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.view.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;

import butterknife.*;

public class CountrySelectionFragment extends RegistrationBaseFragment {

    @BindView(R2.id.usr_countrySelection_countryList)
    RecyclerView countryList;

    private CountrySelectionAdapter countrySelectionAdapter = new CountrySelectionAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_selection_layout, null);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        countryList.setAdapter(countrySelectionAdapter);
        countryList.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {

    }

    @Override
    public int getTitleResourceId() {
        return R.string.google_client_id;
    }
}
