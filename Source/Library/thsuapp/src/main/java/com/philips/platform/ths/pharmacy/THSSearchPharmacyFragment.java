/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_PHARMACY;
import static com.philips.platform.ths.utility.THSConstants.THS_SEARCH_PHARMACY;

public class THSSearchPharmacyFragment extends THSBaseFragment implements SearchBox.ExpandListener,SearchBox.QuerySubmitListener,THSSearchFragmentViewInterface{

    public static String TAG = THSSearchPharmacyFragment.class.getSimpleName();
    private SearchBox searchPharmacy;
    private ActionBarListener actionBarListener;
    private THSSearchPharmacyPresenter thsSearchPharmacyPresenter;
    public static final int SEARCH_EVENT_ID = 4000;
    private String zipSearchString = null;
    private List<Pharmacy> pharmacies;
    private RelativeLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thsSearchPharmacyPresenter = new THSSearchPharmacyPresenter(getActivity(),this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.ths_pharmacy_search_fragment,container,false);
        searchPharmacy = (SearchBox) view.findViewById(R.id.ths_search_pharmacy_edittext);
        linearLayout = (RelativeLayout) view.findViewById(R.id.searchPharmacyContainerView);
        searchPharmacy.setExpandListener(this);
        searchPharmacy.setQuerySubmitListener(this);
        searchPharmacy.setQuery(searchPharmacy.getQuery());
        searchPharmacy.setSearchBoxHint(R.string.ths_pharmacy_search_hint);
        searchPharmacy.setDecoySearchViewHint(R.string.ths_pharmacy_search_hint);
        actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(R.string.search_pharmacy_fragment_name,true);
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_SEARCH_PHARMACY,null,null);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onQuerySubmit(CharSequence charSequence) {
        zipSearchString = String.valueOf(charSequence);
        createCustomProgressBar(linearLayout,SMALL);
        if(thsSearchPharmacyPresenter.validateZip(zipSearchString)) {
            thsSearchPharmacyPresenter.onEvent(SEARCH_EVENT_ID);
        }else {
            doTagging(ANALYTICS_PHARMACY,getString(R.string.ths_pharmacy_search_error),false);
            showError(getString(R.string.ths_pharmacy_search_error));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public String getZipCode() {
        return zipSearchString;
    }

    @Override
    public void setPharmacyList(List<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
        callPharmacyListFragment();
    }

    protected void callPharmacyListFragment() {
        THSPharmacyListFragment thsPharmacyListFragment = new THSPharmacyListFragment();
        thsPharmacyListFragment.setPharmaciesList(pharmacies);
        addFragment(thsPharmacyListFragment,THSPharmacyListFragment.TAG,null, true);
    }

    @Override
    public void onSearchExpanded() {
        searchPharmacy.getCollapseView().setVisibility(View.GONE);
    }

    @Override
    public void onSearchCollapsed() {

    }


}
