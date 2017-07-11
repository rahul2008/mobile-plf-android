package com.philips.platform.ths.insurance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;

/**
 * Created by philips on 7/11/17.
 */

public class THSInsuranceDetailFraagment extends THSBaseFragment implements BackEventListener , View.OnClickListener{
    public static final String TAG = THSInsuranceDetailFraagment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    private THSBasePresenter mPresenter;
    private EditText insuranceSearchEditBox;
    private EditText subscriptionIDEditBox;
    private Spinner availableInsuranceSpinner;
    private Button detailContinueButton;
    private Button detailSkipButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_insurance_confirmation, container, false);
        mPresenter = new THSInsuranceDetailPresenter(this);
        insuranceSearchEditBox = (com.philips.platform.uid.view.widget.EditText)view.findViewById(R.id.pth_insurance_detail_provider_select_edit_text);
        availableInsuranceSpinner=(Spinner)view.findViewById(R.id.pth_insurance_detail_provider_spinner);
        subscriptionIDEditBox = (com.philips.platform.uid.view.widget.EditText)view.findViewById(R.id.pth_insurance_detail_subscription_edit_text);
        detailContinueButton= (Button)view.findViewById(R.id.pth_insurance_detail_continue_button);
        detailSkipButton= (Button)view.findViewById(R.id.pth_insurance_detail_skip_button);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        ((THSInsuranceDetailPresenter)mPresenter).fetchHealthPlanList();
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

        }else if(view.getId() == R.id.pth_insurance_detail_continue_button){

        } else  if(view.getId() == R.id.pth_insurance_detail_skip_button){

        }


    }
}
