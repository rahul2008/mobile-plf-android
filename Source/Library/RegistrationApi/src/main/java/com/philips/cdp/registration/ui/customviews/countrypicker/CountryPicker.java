package com.philips.cdp.registration.ui.customviews.countrypicker;

import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.DialogFragment;
import android.view.*;
import android.widget.ListView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.*;

public class CountryPicker extends DialogFragment implements
        Comparator<Country> {
    private ListView countryListView;

    private CountryAdapter adapter;

    /**
     * Hold countries that matched user query
     */
    private List<Country> selectedCountriesList;

    private CountryChangeListener listener;



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
            // Sort the all countries list based on country name
            Collections.sort(allCountriesList, this);
            // Initialize selected countries with all countries
            selectedCountriesList = new ArrayList<Country>();
            selectedCountriesList.addAll(allCountriesList);
        }
        selectedCountriesList.add(0, getCountry(selectedCountry));
        // Return
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
        // Get countries from the json
        getAllCountries();
        RegUtility.supportedCountryList();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Get view components
        countryListView = (ListView) view
                .findViewById(R.id.usr_countrySelection_countryList);
        // Set adapter
        adapter = new CountryAdapter(getActivity(), selectedCountriesList);
        countryListView.setAdapter(adapter);

        // Inform listener
        countryListView.setOnItemClickListener((parent, view1, position, id) -> {
            if (listener != null) {

                Country country = selectedCountriesList.get(position);
                listener.onSelectCountry(country.getName(),
                        country.getCode());
            }
        });

        return view;
    }

    /**
     * Search allCountriesList contains text and put result into
     * selectedCountriesList
     *
     * @param text
     */
//    @SuppressLint("DefaultLocale")
//    private void search(String text) {
//        selectedCountriesList.clear();
//
//        for (Country country : allCountriesList) {
//            if (country.getName().toLowerCase(Locale.ENGLISH)
//                    .contains(text.toLowerCase())) {
//                selectedCountriesList.add(country);
//            }
//        }
//
//        adapter.notifyDataSetChanged();
//    }

    /**
     * Support sorting the countries list
     */
    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }

}
