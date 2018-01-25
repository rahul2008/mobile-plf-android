/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_ADD_VITALS_PAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_CONDITION_PAGE;

public class THSMedicalConditionsFragment extends THSBaseFragment implements View.OnClickListener {
    protected THSMedicalConditionsPresenter thsMedicalConditionsPresenter;
    public static final String TAG = THSMedicalConditionsFragment.class.getSimpleName();
    protected LinearLayout checkBoxLinearLayout;
    private Button continueButton;
    private Button skipLabel;
    protected List<THSCondition> thsConditionsList = null;
    private RelativeLayout conditionsRelativeLayout;
    private boolean isMedicalConditionChecked = false;
    private List<CheckBox> checkBoxList;
    protected int NumberOfConditionSelected=0;
    private Label mLabelPatientName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_medical_conditions, container, false);
        checkBoxLinearLayout = (LinearLayout) view.findViewById(R.id.checkbox_container);
        continueButton = (Button) view.findViewById(R.id.continue_btn);
        continueButton.setOnClickListener(this);
        continueButton.setEnabled(false);
        skipLabel = (Button) view.findViewById(R.id.conditions_skip);
        skipLabel.setOnClickListener(this);
        conditionsRelativeLayout = (RelativeLayout) view.findViewById(R.id.conditions_container);

        mLabelPatientName = (Label) view.findViewById(R.id.ths_medical_conditions_patient_name);
        String name = getString(R.string.ths_dependent_name, THSManager.getInstance().getThsConsumer(getContext()).getFirstName());
        mLabelPatientName.setText(name);

        AmwellLog.i(AmwellLog.LOG, "onCreateView called");
        checkBoxList = new ArrayList<>();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AmwellLog.i(AmwellLog.LOG, "OnActivityCreated called");

        thsMedicalConditionsPresenter = new THSMedicalConditionsPresenter(this);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_prepare_your_visit), true);
        }


        if (getTHSConditions() == null) {
            createCustomProgressBar(conditionsRelativeLayout, BIG);
            getConditionsFromServer();
        } else {
            setConditions(getTHSConditions());
        }

    }

    private void getConditionsFromServer() {
        try {
            thsMedicalConditionsPresenter.getConditions();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

        thsMedicalConditionsPresenter.onEvent( view.getId());

    }


    public void setConditions(List<THSCondition> thsConditionses) {
        setTHSConditions(thsConditionses);
        List<Condition> conditionList = new ArrayList<>();
        if (thsConditionses == null || thsConditionses.isEmpty()) {
            return;
        }
        for (int i = 0; i < thsConditionses.size(); i++) {
            conditionList.add(thsConditionses.get(i).getCondition());
        }
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/centralesansbook.ttf");
        if (getContext() != null) {
            int i=0;
            for (final Condition condition : conditionList) {
                final CheckBox checkBox = new CheckBox(getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkBox.setLayoutParams(layoutParams);
                checkBox.setEnabled(true);
                checkBox.setTag(i);
                checkBox.setTypeface(typeface);
                checkBox.setText(condition.getName());
                if (condition.isCurrent()) {
                    checkBox.setChecked(true);
                }
                checkBoxLinearLayout.addView(checkBox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            NumberOfConditionSelected++;
                        }else{
                            NumberOfConditionSelected--;
                        }
                        condition.setCurrent(isChecked);
                        validateCheckBox();
                    }
                });
                checkBoxList.add(checkBox);
                i++;
            }
            validateCheckBox();

        }
        hideProgressBar();
    }

    private void validateCheckBox() {

        isMedicalConditionChecked = false;
        for(CheckBox checkBoxItem: checkBoxList){
            if(checkBoxItem.isChecked()){
                isMedicalConditionChecked = true;
            }
        }
        continueButton.setEnabled(isMedicalConditionChecked);
    }

    public List<THSCondition> getTHSConditions() {
        return thsConditionsList;
    }

    public void setTHSConditions(List<THSCondition> mTHSConditions) {
        this.thsConditionsList = mTHSConditions;
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_CONDITION_PAGE,null,null);
    }
}
