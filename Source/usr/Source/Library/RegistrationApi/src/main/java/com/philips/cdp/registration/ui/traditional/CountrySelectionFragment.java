package com.philips.cdp.registration.ui.traditional;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.dao.Country;
import com.philips.cdp.registration.listener.CountrySelectionListener;
import com.philips.cdp.registration.ui.traditional.countrySelection.CountrySelectionAdapter;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountrySelectionFragment extends RegistrationBaseFragment implements
        Comparator<Country> {
    @BindView(R2.id.country_recycler_view)
    RecyclerView countryListView;

    private CountrySelectionAdapter countryListAdapter;

    private RecyclerViewSeparatorItemDecoration separatorItemDecoration;

    private ArrayList<Country> masterList = new ArrayList<>();

    private ArrayList<Country> recentList = new ArrayList<>();

    private ArrayList<Country> filtredList = new ArrayList<>();

    private CountrySelectionListener listener;

    public CountrySelectionFragment() {
    }


    public CountrySelectionFragment(CountrySelectionListener listener,
                                    ArrayList<Country> rawMasterList, ArrayList<Country> recentList) {
        this.listener = listener;
        this.masterList = removeDuplicatesFromArrayList(rawMasterList);
        this.recentList = recentList;
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CountrySelectionFragment : onConfigurationChanged");
        setCustomParams(config);
    }


    @Override
    protected void setViewParams(Configuration config, int width) {
        applyParams(config, countryListView, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);

    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_DLS_Country_Selection_Nav_Title_Text;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_selection_layout, null);
        ButterKnife.bind(this, view);

        countryListView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        countryListView.setLayoutManager(mLayoutManager);
        separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        countryListView.addItemDecoration(separatorItemDecoration);
        countryListView.setItemAnimator(new DefaultItemAnimator());
        filtredList = populateList(recentList, masterList);
        countryListAdapter = new CountrySelectionAdapter(filtredList, this::updateCountryList);
        countryListView.setAdapter(countryListAdapter);
        handleOrientationOnView(view);
        return view;
    }

    private void updateCountryList(int position) {
        Country country = filtredList.get(position);
        if (listener != null) {
            trackPage(AppTaggingPages.COUNTRY);
            listener.onSelectCountry(country.getName(),
                    country.getCode());
        }
        countryListAdapter.notifyDataSetChanged();
        countryListView.scrollToPosition(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }


    private ArrayList<Country> populateList(final ArrayList<Country> recentList, ArrayList<Country> masterList) {
        ArrayList<Country> recentCountryList = new ArrayList<>(removeDuplicatesFromArrayList(recentList));
        ArrayList<Country> masterCountryList = new ArrayList<>(masterList);

        ArrayList<Country> filteredCountryList = new ArrayList<>();
        filteredCountryList.addAll(recentCountryList);
        filteredCountryList.addAll(removeRecentElementsFromMasterList(recentCountryList, masterCountryList));
        return filteredCountryList;
    }

    private ArrayList<Country> removeRecentElementsFromMasterList(final ArrayList<Country> recentList, ArrayList<Country> masterList) {
        masterList.removeAll(recentList);
        Collections.sort(masterList, this);
        return masterList;

    }

    private ArrayList<Country> removeDuplicatesFromArrayList(ArrayList<Country> rawMasterList) {
        ArrayList<Country> countryList = new ArrayList<>(rawMasterList);
        Set<Country> countryHashSet = new LinkedHashSet<>();
        countryHashSet.addAll(countryList);
        countryList.clear();
        countryList.addAll(countryHashSet);
        return countryList;
    }
}
