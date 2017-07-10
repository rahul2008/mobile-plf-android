package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.americanwell.sdk.entity.health.Medication;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.ArrayList;
import java.util.List;



public class THSMedicationFragment extends THSBaseFragment implements  View.OnClickListener {
    public static final String TAG = THSBaseFragment.class.getSimpleName();
    SearchBox searchBox;
    private ActionBarListener actionBarListener;
    private THSBasePresenter mPresenter;
    ListView mExistingMedicationListView;
    THSExistingMedicationListAdapter mTHSExistingMedicationListAdapter;
    Button searchButton;
    THSMedication mExistingMedication;
    Button updateMedicationButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.intake_medication, container, false);
        mPresenter = new THSMedicationPresenter(this);
        searchBox = (SearchBox) view.findViewById(R.id.pth_intake_medication_searchbox);

        updateMedicationButton = (Button) view.findViewById(R.id.pth_intake_medication_continue_button);
        updateMedicationButton.setOnClickListener(this);

        searchBox.getCollapseView().setVisibility(View.GONE);
        searchBox.getClearIconView().setVisibility(View.GONE);


        mExistingMedicationListView = (ListView) view.findViewById(R.id.pth_intake_medication_listview);
        searchButton = (Button) view.findViewById(R.id.pth_intake_medication_searchbox_button);
        searchButton.setOnClickListener(this);
        mTHSExistingMedicationListAdapter = new THSExistingMedicationListAdapter(getActivity());
        mExistingMedicationListView.setAdapter(mTHSExistingMedicationListAdapter);
        createCustomProgressBar(view, BIG);
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


        // load existing medicines
        ((THSMedicationPresenter) mPresenter).fetchMedication();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Prepare your visit", true);
        }
    }

    public void showExistingMedicationList(THSMedication pTHMedication) {
        hideProgressBar();
        if (null != pTHMedication) {
            mExistingMedication = pTHMedication;
            mTHSExistingMedicationListAdapter.setData(pTHMedication);

        }
    }


    public void showSearchedMedicationList(THSMedication pTHMedication) {
        FragmentManager manager = getFragmentManager();
        THSSearchedMedicineDialogFragment pTHSearchedMedicineDialogFragment = new THSSearchedMedicineDialogFragment();
        pTHSearchedMedicineDialogFragment.setData(getActivity(), pTHMedication, ((THSMedicationPresenter) mPresenter));
        pTHSearchedMedicineDialogFragment.show(manager, "Select medicine");
    }

    public void addSearchedMedicineToExistingMedication(Medication searchedMedication) {
        if (null == mExistingMedication) {
            mExistingMedication = new THSMedication();
            List<Medication> medication = new ArrayList<Medication>();
            mExistingMedication.setMedicationList(medication);

        }
        if (null != mExistingMedication.getMedicationList() && !mExistingMedication.getMedicationList().contains(searchedMedication)) {
            mExistingMedication.getMedicationList().add(searchedMedication);
            mTHSExistingMedicationListAdapter.setData(mExistingMedication);
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    //TODO: Review comment - Please move it to Presenter
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pth_intake_medication_searchbox_button) {
            String medicineName = searchBox.getSearchTextView().getText().toString();
            if (null != medicineName && medicineName.length() > 2) {
                showProgressBar();
                ((THSMedicationPresenter) mPresenter).searchMedication(medicineName);
            }
        } else if (id == R.id.pth_intake_medication_continue_button) {
            showProgressBar();
            ((THSMedicationPresenter) mPresenter).updateMedication(mExistingMedication);
            mPresenter.onEvent(R.id.pth_intake_medication_continue_button);
        }
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }
}
