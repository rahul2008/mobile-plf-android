package com.philips.cdp.registration.ui.customviews.countrypicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.countypicker.CountryPickerAdapter;
import com.philips.cdp.registration.countypicker.DividerItemDecoration;
import com.philips.cdp.registration.listener.SelectedCountryListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class CountryPicker extends DialogFragment implements
        Comparator<Country>{
    private RecyclerView countryListView;
    private CountryPickerAdapter adapter;

    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    /**
     * Hold countries that matched user query
     */
    private List<Country> selectedCountriesList;

    private CountryChangeListener listener;

    private String selectedCountry;

    /**
     * Set listener
     *
     * @param listener
     */
    public void setListener(CountryChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Get all countries with code and name from res/raw/countries.json
     *
     * @return
     */
    private List<Country> getAllCountries() {
        List<Country> allCountriesList = new ArrayList<>();
        List<String> recourseList = RegUtility.supportedCountryList();
        Locale locale = RegistrationHelper.getInstance().getLocale(getContext());
        String selectedCountry = locale.getCountry();
        recourseList.remove(selectedCountry);

        for (int i = 0; i < recourseList.size(); i++) {
            Country country = getCountry(recourseList.get(i));
            allCountriesList.add(country);
        }

        if (allCountriesList != null) {
            Collections.sort(allCountriesList, this);
            selectedCountriesList = new ArrayList<Country>();
            selectedCountriesList.addAll(allCountriesList);
        }
        selectedCountriesList.set(0, getCountry(RegistrationHelper.getInstance().getCountryCode()));
        String selectedCountryVal = getCountry(RegistrationHelper.getInstance().getCountryCode()).getName();
        for (int i = 1; i < selectedCountriesList.size(); i++) {
            if(selectedCountriesList.get(i).getName().equalsIgnoreCase(selectedCountryVal)){
                selectedCountriesList.remove(i);
            }
        }
        return allCountriesList;
    }

    @NonNull
    private Country getCountry(String countryCode) {
        Country country = new Country();
        country.setCode(countryCode);
        Locale nameLocale = new Locale("", countryCode);
        country.setName(nameLocale.getDisplayCountry());
        return country;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    /**
     * Create view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_selection_layout, null);

        getAllCountries();
        RegUtility.supportedCountryList();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        countryListView = (RecyclerView) view
                .findViewById(R.id.country_recycler_view);

        countryListView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        countryListView.setLayoutManager(mLayoutManager);
        countryListView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        countryListView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CountryPickerAdapter(getActivity(), selectedCountriesList, new SelectedCountryListener() {

            @Override
            public void onCountrySelected(int position) {
                updateCountryList(position);
            }

            private void updateCountryList(int position) {
                Country country = selectedCountriesList.get(position);
                if (listener != null) {

                    listener.onSelectCountry(country.getName(),
                            country.getCode());
                }
                country.setName(country.getName());
                RegistrationHelper.getInstance().setCountryCode(country.getName());
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), country.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }
        });
        countryListView.setAdapter(adapter);
        return view;
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }
}
