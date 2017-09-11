package com.philips.cdp.registration.ui.customviews.countrypicker;

import android.content.res.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.countypicker.*;
import com.philips.cdp.registration.countypicker.DividerItemDecoration;
import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;

import java.util.*;

import butterknife.*;

import static com.janrain.android.engage.JREngage.*;

public class CountryPicker extends RegistrationBaseFragment implements
        Comparator<Country> {
    @BindView(R2.id.country_recycler_view)
     RecyclerView countryListView;

    @BindView(R2.id.usr_country_selection_LinearLayout)
    LinearLayout usr_country_selection_LinearLayout;


    private CountryPickerAdapter adapter;


    ArrayList<Country> masterList;

    ArrayList<Country> recentList;

    ArrayList<Country> filtredList;


    private CountryChangeListener listener;


    public CountryPicker(CountryChangeListener listener, ArrayList<Country> rawMasterList, ArrayList<Country> recentList) {
        this.listener = listener;
        this.masterList = removeDuplicatesFromArrayList(rawMasterList);
        this.recentList = recentList;
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CountryPicker : onConfigurationChanged");
        setCustomParams(config);
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
    protected void setViewParams(Configuration config, int width) {
        //To be set
        applyParams(config, countryListView, width);
        applyParams(config, usr_country_selection_LinearLayout, width);
    }

    @Override
    protected void handleOrientation(View view) {
        //handle code
        handleOrientationOnView(view);

    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_country_region;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_selection_layout, null);
        ButterKnife.bind(this, view);


        countryListView = (RecyclerView) view
                .findViewById(R.id.country_recycler_view);

        countryListView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        countryListView.setLayoutManager(mLayoutManager);
        countryListView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        countryListView.setItemAnimator(new DefaultItemAnimator());

        filtredList = populateList(recentList, masterList);
        adapter = new CountryPickerAdapter(getActivity(), filtredList, new SelectedCountryListener() {

            @Override
            public void onCountrySelected(int position) {
                updateCountryList(position);
            }

            private void updateCountryList(int position) {
                Country country = filtredList.get(position);
                if (listener != null) {

                    listener.onSelectCountry(country.getName(),
                            country.getCode());
                }
                country.setName(country.getName());
                adapter.notifyDataSetChanged();
                countryListView.scrollToPosition(0);
            }
        });
        countryListView.setAdapter(adapter);
        return view;
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }


    private ArrayList<Country> populateList(final ArrayList<Country> recentList, ArrayList<Country> masterList) {
        ArrayList<Country> rl = new ArrayList<>(removeDuplicatesFromArrayList(recentList));
        ArrayList<Country> ml = new ArrayList<>(masterList);

        ArrayList<Country> fl = new ArrayList<>();
        fl.addAll(rl);
        fl.addAll(removeRecentElementsFromMasterList(rl, ml));
        return fl;
    }

    private ArrayList<Country> removeRecentElementsFromMasterList(final ArrayList<Country> rl, ArrayList<Country> ml) {
        ml.removeAll(rl);
        Collections.sort(ml, this);
        return ml;

    }


    private ArrayList<Country> removeDuplicatesFromArrayList(ArrayList<Country> rawMasterList) {

        ArrayList<Country> al = new ArrayList<>(rawMasterList);
// add elements to al, including duplicates
        Set<Country> hs = new LinkedHashSet<>();
        hs.addAll(al);
        al.clear();
        al.addAll(hs);
        return al;
    }

}
