package com.philips.platform.ths.insurance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;

/**
 * Created by philips on 7/11/17.
 */

public class THSInsuranceDetailFragment extends THSBaseFragment implements BackEventListener , View.OnClickListener, AdapterView.OnItemClickListener{
    public static final String TAG = THSInsuranceDetailFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    private THSInsuranceDetailPresenter mPresenter;
    private EditText insuranceSearchEditBox;
    private EditText subscriptionIDEditBox;
     ListView mHealPlanListView;
    private Button detailContinueButton;
    private Button detailSkipButton;
     THSHealthPlanListAdapter mTHSHealthPlanListAdapter;
    THSHealthPlan mTHSHealthPlan;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_insurance_details, container, false);
        mPresenter = new THSInsuranceDetailPresenter(this);
        insuranceSearchEditBox = (com.philips.platform.uid.view.widget.EditText)view.findViewById(R.id.pth_insurance_detail_provider_select_edit_text);
        insuranceSearchEditBox.setOnClickListener(this);
        mHealPlanListView=(ListView) view.findViewById(R.id.pth_insurance_detail_provider_listview);
        mHealPlanListView.setOnItemClickListener(this);
        subscriptionIDEditBox = (com.philips.platform.uid.view.widget.EditText)view.findViewById(R.id.pth_insurance_detail_subscription_edit_text);
        //subscriptionIDEditBox.setOnClickListener(this);
        detailContinueButton= (Button)view.findViewById(R.id.pth_insurance_detail_continue_button);
        detailContinueButton.setOnClickListener(this);
        detailSkipButton= (Button)view.findViewById(R.id.pth_insurance_detail_skip_button);
        detailSkipButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        mTHSHealthPlan =  ((THSInsuranceDetailPresenter)mPresenter).fetchHealthPlanList();
        mTHSHealthPlanListAdapter= new THSHealthPlanListAdapter(getActivity(),mTHSHealthPlan);
        mHealPlanListView.setAdapter(mTHSHealthPlanListAdapter);

    }



    @Override
    public boolean handleBackEvent() {
        return false;
    }


    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.pth_insurance_detail_provider_select_edit_text){
            if(mHealPlanListView.getVisibility()==View.GONE) {
                mHealPlanListView.setVisibility(View.VISIBLE);
            }else{
                mHealPlanListView.setVisibility(View.GONE);
            }

        }else if(view.getId() == R.id.pth_insurance_detail_continue_button){


        } else  if(view.getId() == R.id.pth_insurance_detail_skip_button){

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
        insuranceSearchEditBox.setText(mTHSHealthPlan.getHealthPlanList().get(position).getName());
        mHealPlanListView.setVisibility(View.GONE);
    }
}
