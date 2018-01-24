/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.practice.Practice;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.philips.platform.ths.utility.THSConstants.THS_MEDICATION_SEARCH_PAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_SEARCH_PAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_PHARMACY_SEARCH;

public class THSSearchFragment extends THSBaseFragment implements SearchBox.QuerySubmitListener, ListView.OnItemClickListener, TextWatcher {
    public static final String TAG = THSSearchFragment.class.getSimpleName();
    private THSBasePresenter mPresenter;
    protected THSMedication searchedMedicines;
    protected List<THSProviderInfo> providerInfoList;
    private ListView searchListView;
    protected THSSearchListAdapter mTHSSearchListAdapter;
    private UIDNavigationIconToggler navIconToggler;
    protected SearchBox searchBox;
    int searchType = 0;
    private Practice practice;
    protected List<Pharmacy> pharmacyList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (null != bundle && bundle.containsKey(THSConstants.SEARCH_CONSTANT_STRING)) {
            searchType = bundle.getInt(THSConstants.SEARCH_CONSTANT_STRING);
        }
        setHasOptionsMenu(true);
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_search_layout, container, false);
        searchListView = (ListView) view.findViewById(R.id.ths_search_listview);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        setAdapter();
        return view;
    }

    public void setAdapter() {
        mTHSSearchListAdapter = new THSSearchListAdapter(getFragmentActivity(), null);
        searchListView.setAdapter(mTHSSearchListAdapter);
        searchListView.setOnItemClickListener(this);
        mPresenter = new THSSearchPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        navIconToggler.hideNavigationIcon();
    }

    @Override
    public void onStop() {
        super.onStop();
        navIconToggler.restoreNavigationIcon();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(isFragmentAttached()) {
            super.onPrepareOptionsMenu(menu);
            MenuItem searchBoxitem = menu.findItem(R.id.search_pharmacy_menu);
            final View view = new  com.philips.platform.uid.view.widget.SearchBox(getContext());
            MenuItemCompat.setActionView(searchBoxitem, view);

            searchBox = (SearchBox) menu.findItem(R.id.search_pharmacy_menu).getActionView();
            searchBox.setQuerySubmitListener(this);
            searchBox.setQuery(searchBox.getQuery());
            String searchBoxHint = "";
            switch (searchType) {
                case THSConstants.MEDICATION_SEARCH_CONSTANT:
                    searchBox.getSearchTextView().addTextChangedListener(this);
                    searchBoxHint = getActivity().getResources().getString(R.string.ths_search_medication);
                    THSTagUtils.doTrackPageWithInfo(THS_MEDICATION_SEARCH_PAGE,null,null);
                    break;
                case THSConstants.PROVIDER_SEARCH_CONSTANT:
                    searchBox.getSearchTextView().addTextChangedListener(this);
                    searchBoxHint = getActivity().getResources().getString(R.string.ths_search_provider);
                    THSTagUtils.doTrackPageWithInfo(THS_PROVIDER_SEARCH_PAGE,null,null);
                    break;
                case THSConstants.PHARMACY_SEARCH_CONSTANT:
                    searchBoxHint = getActivity().getResources().getString(R.string.ths_search_pharmacy);
                    THSTagUtils.doTrackPageWithInfo(THS_PHARMACY_SEARCH,null,null);
                    break;
            }
            searchBox.setSearchBoxHint(searchBoxHint);
            searchBox.setDecoySearchViewHint(searchBoxHint);


        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 2) {
            searchFunction(s);
        } else if (s.length() == 2) {
            mTHSSearchListAdapter.setData(null);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void searchFunction(CharSequence s) {
        switch (searchType) {
            case THSConstants.MEDICATION_SEARCH_CONSTANT:
                ((THSSearchPresenter) mPresenter).searchMedication(s.toString());
                break;
            case THSConstants.PROVIDER_SEARCH_CONSTANT:
                ((THSSearchPresenter) mPresenter).searchProviders(s.toString(), practice);
                break;
            case THSConstants.PHARMACY_SEARCH_CONSTANT:
                ((THSSearchPresenter) mPresenter).searchPharmacy(s.toString());
                break;
        }
    }


    @Override
    public void onQuerySubmit(CharSequence charSequence) {
        if (null != charSequence && charSequence.length() > 2) {
            InputMethodManager imm = (InputMethodManager) THSSearchFragment.this.getFragmentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
            searchFunction(charSequence);
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (searchType) {
            case THSConstants.MEDICATION_SEARCH_CONSTANT:
                callMedicationFragment(position);
                break;
            case THSConstants.PROVIDER_SEARCH_CONSTANT:
                callProviderDetailsFragment(position);
                break;
        }


    }

    protected void callPharmacyListFragment() {

        Intent intent = new Intent(getActivity(), THSSearchFragment.class);
        intent.putParcelableArrayListExtra("selectedPharmacy", (ArrayList<? extends Parcelable>) pharmacyList);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        getFragmentActivity().getSupportFragmentManager().popBackStack();

    }

    protected void callProviderDetailsFragment(int position) {

        THSProviderDetailsFragment pthProviderDetailsFragment = new THSProviderDetailsFragment();
        pthProviderDetailsFragment.setActionBarListener(getActionBarListener());
        pthProviderDetailsFragment.setTHSProviderEntity(providerInfoList.get(position));
        pthProviderDetailsFragment.setConsumerAndPractice(THSManager.getInstance().getPTHConsumer(getContext()).getConsumer(), practice);
        pthProviderDetailsFragment.setFragmentLauncher(getFragmentLauncher());
        addFragment(pthProviderDetailsFragment, THSProviderDetailsFragment.TAG, null, true);

    }

    public void callMedicationFragment(int position) {

        Intent intent = new Intent(getFragmentActivity(), THSSearchFragment.class);
        Medication medication = searchedMedicines.getMedicationList().get(position);
        intent.putExtra("selectedMedication", medication);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        getFragmentActivity().getSupportFragmentManager().popBackStack();

    }

}
