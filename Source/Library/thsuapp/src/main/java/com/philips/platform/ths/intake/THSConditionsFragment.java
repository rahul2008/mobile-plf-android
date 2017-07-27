package com.philips.platform.ths.intake;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;


import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.List;

public class THSConditionsFragment extends THSBaseFragment implements BackEventListener, View.OnClickListener {
    THSConditionsPresenter mThsConditionsPresenter;
    public static final String TAG = THSConditionsFragment.class.getSimpleName();
    LinearLayout mLinerLayout;
    Button mContinueButton;
    Label mSkipLabel;
    List<THSConditions> mTHSConditions =null;
    RelativeLayout mRelativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_conditions, container, false);
        mLinerLayout = (LinearLayout)view.findViewById(R.id.checkbox_container);
        mContinueButton = (Button) view.findViewById(R.id.continue_btn);
        mContinueButton.setOnClickListener(this);
        mSkipLabel = (Label)view.findViewById(R.id.conditions_skip);
        mSkipLabel.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.conditions_container);
        AmwellLog.i(AmwellLog.LOG,"onCreateView called");
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AmwellLog.i(AmwellLog.LOG, "OnActivityCreated called");

        mThsConditionsPresenter = new THSConditionsPresenter(this);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.pth_prepare_your_visit), true);
        }


        if (getTHSConditions() == null) {
            createCustomProgressBar(mRelativeLayout, MEDIUM);
            getConditionsFromServer();
        } else {
            setConditions(getTHSConditions());
        }

    }

    private void getConditionsFromServer() {
        try {
        mThsConditionsPresenter.getConditions();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.continue_btn) {
            mThsConditionsPresenter.onEvent(R.id.continue_btn);
        }else if(i == R.id.conditions_skip){
            mThsConditionsPresenter.onEvent(R.id.conditions_skip);
        }
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setConditions(List<THSConditions> thsConditionses) {
        setTHSConditions(thsConditionses);
        List<Condition> conditionList = new ArrayList<>();
        if(thsConditionses == null || thsConditionses.isEmpty()){
            return;
        }
        for (int i=0;i<thsConditionses.size();i++) {
            conditionList.add(thsConditionses.get(i).getCondition());
        }

        if(getContext()!=null) {
            for (final Condition condition : conditionList
                    ) {
                CheckBox checkBox = new CheckBox(getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkBox.setLayoutParams(layoutParams);
                checkBox.setEnabled(true);
                checkBox.setText(condition.getName());
                if(condition.isCurrent()){
                    checkBox.setChecked(true);
                }
                mLinerLayout.addView(checkBox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        condition.setCurrent(true);
                    }
                });
            }
            mContinueButton.setEnabled(true);
        }
        hideProgressBar();
    }

    public List<THSConditions> getTHSConditions() {
        return mTHSConditions;
    }

    public void setTHSConditions(List<THSConditions> mTHSConditions) {
        this.mTHSConditions = mTHSConditions;
    }

}
