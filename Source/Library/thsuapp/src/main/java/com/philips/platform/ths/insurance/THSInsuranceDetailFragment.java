package com.philips.platform.ths.insurance;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by philips on 7/11/17.
 */

public class THSInsuranceDetailFragment extends THSBaseFragment implements BackEventListener, View.OnClickListener {
    public static final String TAG = THSInsuranceDetailFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    private RelativeLayout mProgressbarContainer;
    private THSInsuranceDetailPresenter mPresenter;
     EditText insuranceEditBox;
     EditText subscriptionIDEditBox;

     EditText relationshipEditBox;
     EditText firstNameEditBox;
     EditText lastNameEditBox;
     EditText relationDOBEditBox;
    ListView mHealPlanListView;
    AlertDialog.Builder mAlertDialog;
    THSSubscription thsSubscriptionExisting;


    private Button detailContinueButton;
    private Button detailSkipButton;
    THSHealthPlanListAdapter mTHSHealthPlanListAdapter;
    THSSubscriberRelationshipListAdapter mTHSSubscriberRelationshipListAdapter;
    CheckBox mNotPrimarySubscriberCheckBox;
    RelativeLayout mNotPrimarySubscriberRelativeLayout;



    /// editable fields
    THSHealthPlan mTHSHealthPlanList;
    THSRelationship mTHSRelationshipList;
    HealthPlan mHealthPlan;
    Relationship mInsuranceRelationship;
    RelativeLayout mRelativeLayoutInsuranceContainer;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_insurance_detail, container, false);
        mPresenter = new THSInsuranceDetailPresenter(this);
        mAlertDialog = new AlertDialog.Builder(getActivity());

        View convertView = (View) inflater.inflate(R.layout.ths_list, null);

        mHealPlanListView = (ListView) convertView.findViewById(R.id.ths_listView);
        mRelativeLayoutInsuranceContainer = (RelativeLayout) view.findViewById(R.id.ths_insurance_detail_container);
        insuranceEditBox = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_provider_select_insurance_edit_text);
        insuranceEditBox.setOnClickListener(this);
        relationshipEditBox = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_provider_select_relation_edit_text);
        relationshipEditBox.setOnClickListener(this);
        firstNameEditBox = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_provider_relation_firstname_edittext);
        firstNameEditBox.setOnClickListener(this);
        lastNameEditBox = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_provider_relation_lastname_edittext);
        lastNameEditBox.setOnClickListener(this);
        relationDOBEditBox = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_provider_relation_dob_edittext);
        relationDOBEditBox.setOnClickListener(this);

        subscriptionIDEditBox = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_subscription_edit_text);

        detailContinueButton = (Button) view.findViewById(R.id.ths_insurance_detail_continue_button);
        detailContinueButton.setOnClickListener(this);
        detailSkipButton = (Button) view.findViewById(R.id.ths_insurance_detail_skip_button);
        detailSkipButton.setOnClickListener(this);
        mNotPrimarySubscriberRelativeLayout = (RelativeLayout) view.findViewById(R.id.ths_insurance_detail_relationship_relative_layout);
        mNotPrimarySubscriberCheckBox = (CheckBox) view.findViewById(R.id.ths_insurance_detail_is_primary_subscriber_checkbox);
        mNotPrimarySubscriberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mNotPrimarySubscriberRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    mNotPrimarySubscriberRelativeLayout.setVisibility(View.GONE);
                }
            }
        });
        //mPresenter.getCurrentSubscription();
        mProgressbarContainer=(RelativeLayout) view.findViewById(R.id.ths_insurance_detail_container);
        return view;


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        mTHSHealthPlanList = ((THSInsuranceDetailPresenter) mPresenter).fetchHealthPlanList();
        mTHSRelationshipList = ((THSInsuranceDetailPresenter) mPresenter).fetchSubscriberRelationList();
        mTHSHealthPlanListAdapter = new THSHealthPlanListAdapter(getActivity(), mTHSHealthPlanList);
        mTHSSubscriberRelationshipListAdapter = new THSSubscriberRelationshipListAdapter(getActivity(), mTHSRelationshipList);
        createCustomProgressBar(mProgressbarContainer,BIG);
        mPresenter.fetchExistingSubscription();
    }


    @Override
    public boolean handleBackEvent() {
        return false;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ths_insurance_detail_provider_select_insurance_edit_text) {


            showInsuranceListDialog("Select Health Plan", mTHSHealthPlanListAdapter);

        } else if (view.getId() == R.id.ths_insurance_detail_provider_select_relation_edit_text) {
            showRelationshipListDialog("Select relationship", mTHSSubscriberRelationshipListAdapter);

        } else if (view.getId() == R.id.ths_insurance_detail_skip_button) {
            mPresenter.onEvent(R.id.ths_insurance_detail_skip_button);
        } else if (view.getId() == R.id.ths_insurance_detail_continue_button){
            createCustomProgressBar(mRelativeLayoutInsuranceContainer,BIG);
            mPresenter.onEvent(R.id.ths_insurance_detail_continue_button);

        }else if (view.getId() == R.id.ths_insurance_detail_provider_relation_dob_edittext){

            showDatePicker(relationDOBEditBox,getActivity(),false);
        }


    }

    private void showInsuranceListDialog(String title, BaseAdapter adapter) {
        mAlertDialog.setTitle(title);

        mAlertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                mHealthPlan= mTHSHealthPlanList.getHealthPlanList().get(position);
                insuranceEditBox.setText(mHealthPlan.getName());
                dialog.dismiss();
            }
        });
        AlertDialog alert = mAlertDialog.create();
        alert.show();
    }
    private void showRelationshipListDialog(String title, BaseAdapter adapter) {
        mAlertDialog.setTitle(title);

        mAlertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                mInsuranceRelationship= mTHSRelationshipList.getRelationShipList().get(position);
                relationshipEditBox.setText(mInsuranceRelationship.getName());
                dialog.dismiss();
            }
        });
        AlertDialog alert = mAlertDialog.create();
        alert.show();
    }

    public void showDatePicker(final EditText editText, final Context context, boolean allowFuture) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar calendar = Calendar.getInstance();
        String dateText = editText.getText().toString();

        if (dateText.length() > 0) {
            Date date;
            try {
                date = dateFormat.parse(dateText);
            }
            catch (ParseException exception) {
                throw new RuntimeException(exception);
            }

            calendar.setTime(date);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editText.setText(dateFormat.format(calendar.getTime()));
            }
        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        if (!allowFuture) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        datePickerDialog.show();
    }

}
