package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.SearchBox;

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



    @Override
    public boolean handleBackEvent() {
        return false;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intake_medication, container, false);
        searchBox = (SearchBox)view.findViewById(R.id.pth_intake_medication_searchbox);
        mExistingMedicationListView = (ListView) view.findViewById(R.id.pth_intake_medication_listview);
        mPresenter= new PTHMedicationPresenter(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener=getActionBarListener();
        ((PTHMedicationPresenter) mPresenter).fetchMedication();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != actionBarListener){
            actionBarListener.updateActionBar("Prepare your visit",true);
        }
    }

    public void showExistingMedicationList(PTHMedication pTHMedication){
        if(null!=pTHMedication) {
            mPTHExistingMedicationListAdapter = new PTHExistingMedicationListAdapter(getActivity(),pTHMedication);
            mExistingMedicationListView.setAdapter(mPTHExistingMedicationListAdapter);
        }
    }
}
