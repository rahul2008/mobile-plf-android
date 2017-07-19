package com.philips.platform.ths.intake;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.americanwell.sdk.entity.health.Medication;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

import static android.app.Activity.RESULT_OK;

/**
 * Created by philips on 7/11/17.
 */

public class THSMedicationSearchFragment extends THSBaseFragment implements SearchBox.QuerySubmitListener, ListView.OnItemClickListener {
    public static final String TAG = THSMedicationSearchFragment.class.getSimpleName();
    private THSBasePresenter mPresenter;
    THSMedication searchedMedicines;
    private ListView searchListView;
    THSSearchedMedicationListAdapter mTHSSearchedMedicationListAdapter;
    private UIDNavigationIconToggler navIconToggler;
     SearchBox searchBox;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_search_layout, container, false);
        mPresenter = new THSMedicationSearchPresenter(this);
        searchListView = (ListView) view.findViewById(R.id.pth_search_listview);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTHSSearchedMedicationListAdapter = new THSSearchedMedicationListAdapter(getFragmentActivity(),searchedMedicines);
        searchListView.setAdapter(mTHSSearchedMedicationListAdapter);
        searchListView.setOnItemClickListener(this);

    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
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
        inflater.inflate(R.menu.search_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchBox = (SearchBox) menu.findItem(R.id.search_pharmacy_menu).getActionView();
        searchBox.setQuerySubmitListener(this);
        searchBox.setQuery(searchBox.getQuery());
        searchBox.setSearchBoxHint("Search for medication");
        searchBox.setDecoySearchViewHint("Search for medication");
        searchBox.getSearchTextView().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>2){
                    ((THSMedicationSearchPresenter) mPresenter).searchMedication(s.toString());
                } else if(s.length()==2){
                    mTHSSearchedMedicationListAdapter.setData(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    @Override
    public void onQuerySubmit(CharSequence charSequence) {
        if (null != charSequence && charSequence.length() > 2) {
            InputMethodManager imm = (InputMethodManager)THSMedicationSearchFragment.this.getFragmentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
            showProgressBar();
            ((THSMedicationSearchPresenter) mPresenter).searchMedication(charSequence.toString());
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
        Intent intent = new Intent(getFragmentActivity(), THSMedicationSearchFragment.class);

        Medication medication= searchedMedicines.getMedicationList().get(position);

        intent.putExtra("selectedMedication", medication);

        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        getFragmentActivity().getSupportFragmentManager().popBackStack();


    }
}
