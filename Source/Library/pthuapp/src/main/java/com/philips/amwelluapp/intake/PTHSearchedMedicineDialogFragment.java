package com.philips.amwelluapp.intake;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.platform.uid.drawable.SeparatorDrawable;

/**
 * Created by philips on 6/30/17.
 */

public class PTHSearchedMedicineDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    Context mContext;
    PTHMedication searchedMedicines;
    ListView searchedMedicinesListView;
    PTHSearchedMedicationListAdapter PTHSearchedMedicationListAdapter;
    private PTHBasePresenter mPresenter;

    public void setData(Context context, PTHMedication pTHMedication,PTHBasePresenter presenter) {
        mContext = context;
        this.searchedMedicines = pTHMedication;
        mPresenter=presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pth_searched_medicine_dialog, null, false);
        searchedMedicinesListView = (ListView) view.findViewById(R.id.pth_searched_medication_list_view);
       // searchedMedicinesListView.setDivider(new SeparatorDrawable(getContext()));

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PTHSearchedMedicationListAdapter = new PTHSearchedMedicationListAdapter(mContext,searchedMedicines);

        searchedMedicinesListView.setAdapter(PTHSearchedMedicationListAdapter);

        searchedMedicinesListView.setOnItemClickListener(this);
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
        ((PTHMedicationPresenter) mPresenter).addSearchedMedication(searchedMedicines.getMedicationList().get(position));
        dismiss();
    }
}
