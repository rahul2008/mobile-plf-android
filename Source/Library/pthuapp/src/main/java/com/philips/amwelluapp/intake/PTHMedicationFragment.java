package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.americanwell.sdk.entity.health.Medication;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philips on 6/28/17.
 */

public class PTHMedicationFragment extends PTHBaseFragment implements BackEventListener {
    public static final String TAG = PTHBaseFragment.class.getSimpleName();
    SearchBox searchBox;
    private ActionBarListener actionBarListener;
    private PTHBasePresenter mPresenter;
    ListView mExistingMedicationListView;
    PTHExistingMedicationListAdapter mPTHExistingMedicationListAdapter;
    Button searchButton;
    PTHMedication mExistingMedication;


    @Override
    public boolean handleBackEvent() {
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intake_medication, container, false);
        mPresenter = new PTHMedicationPresenter(this);
        searchBox = (SearchBox) view.findViewById(R.id.pth_intake_medication_searchbox);
        searchBox.getCollapseView().setVisibility(View.GONE);
        searchBox.getClearIconView().setVisibility(View.GONE);


        mExistingMedicationListView = (ListView) view.findViewById(R.id.pth_intake_medication_listview);
        searchButton = (Button) view.findViewById(R.id.pth_intake_medication_searchbox_button);
        mPTHExistingMedicationListAdapter = new PTHExistingMedicationListAdapter(getActivity());
        mExistingMedicationListView.setAdapter(mPTHExistingMedicationListAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicineName = searchBox.getSearchTextView().getText().toString();
                if (null != medicineName && medicineName.length() > 2) {
                    searchMedication(medicineName);
                }
            }
        });

        // load existing medicines
        ((PTHMedicationPresenter) mPresenter).fetchMedication();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Prepare your visit", true);
        }
    }

    public void showExistingMedicationList(PTHMedication pTHMedication) {
        if (null != pTHMedication) {
            mExistingMedication = pTHMedication;
            mPTHExistingMedicationListAdapter.setData(pTHMedication);

        }
    }

    public void searchMedication(String medicineName) {
        ((PTHMedicationPresenter) mPresenter).searchMedication(medicineName);
    }

    public void showSearchedMedicationList(PTHMedication pTHMedication) {
        FragmentManager manager = getFragmentManager();
        PTHSearchedMedicineDialogFragment pTHSearchedMedicineDialogFragment = new PTHSearchedMedicineDialogFragment();
        pTHSearchedMedicineDialogFragment.setData(getActivity(), pTHMedication, ((PTHMedicationPresenter) mPresenter));
        pTHSearchedMedicineDialogFragment.show(manager, "Select medicine");
    }

    public void addSearchedMedicineToExistingMedication(Medication searchedMedication) {
        if (null == mExistingMedication) {
            mExistingMedication = new PTHMedication();
            List<Medication> medication = new ArrayList<Medication>();
            mExistingMedication.setMedicationList(medication);

        }
        if (null!=mExistingMedication.getMedicationList() && !mExistingMedication.getMedicationList().contains(searchedMedication)) {
            mExistingMedication.getMedicationList().add(searchedMedication);
            mPTHExistingMedicationListAdapter.setData(mExistingMedication);
        }

    }

}
