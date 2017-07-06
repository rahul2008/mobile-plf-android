package com.philips.amwelluapp.intake;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;

import java.util.List;

public class THSConditionsFragment extends PTHBaseFragment implements BackEventListener, View.OnClickListener {
    THSConditionsPresenter mThsConditionsPresenter;
    public static final String TAG = THSConditionsFragment.class.getSimpleName();
    LinearLayout mLinerLayout;
    Button mContinueButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_conditions, container, false);
        mLinerLayout = (LinearLayout)view.findViewById(R.id.checkbox_container);
        mContinueButton = (Button) view.findViewById(R.id.continue_btn);
        mContinueButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mThsConditionsPresenter = new THSConditionsPresenter(this);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.pth_prepare_your_visit), true);
        }

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
        }
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setConditions(List<Condition> conditions) {
        for (final Condition condition:conditions
                ) {
            CheckBox checkBox = new CheckBox(getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setEnabled(true);
            checkBox.setText(condition.getName());
            mLinerLayout.addView(checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    condition.setCurrent(true);
                }
            });
        }
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }
}
