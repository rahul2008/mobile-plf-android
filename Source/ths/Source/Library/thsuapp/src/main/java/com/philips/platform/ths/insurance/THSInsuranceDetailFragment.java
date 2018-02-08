/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

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
import com.americanwell.sdk.entity.insurance.Subscription;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;
import static com.philips.platform.ths.utility.THSConstants.THS_INSURANCE_DETAIL;


public class THSInsuranceDetailFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSInsuranceDetailFragment.class.getSimpleName();
    protected EditText insuranceEditBox;
    protected EditText subscriberIDEditBox;
    protected EditText relationshipEditBox;
    protected EditText firstNameEditBox;
    protected EditText lastNameEditBox;
    protected EditText relationDOBEditBox;
    protected ListView mHealPlanListView;
    protected THSSubscription thsSubscriptionExisting;
    protected CheckBox mNotPrimarySubscriberCheckBox;
    protected RelativeLayout mNotPrimarySubscriberRelativeLayout;
    protected THSRelationship mTHSRelationshipList;
    protected HealthPlan mHealthPlan;
    protected Relationship mInsuranceRelationship;
    protected Label mSuffixLabel;
    protected EditText mSuffixEditText;
    boolean isLaunchedFromCostSummary = false;
    private ActionBarListener actionBarListener;
    private RelativeLayout mProgressbarContainer;
    protected THSInsuranceDetailPresenter mPresenter;
    private AlertDialog.Builder mAlertDialog;
    private Button detailContinueButton;
    private Button detailSkipButton;
    private THSHealthPlanListAdapter mTHSHealthPlanListAdapter;
    private THSSubscriberRelationshipListAdapter mTHSSubscriberRelationshipListAdapter;
    /// editable fields
    private THSHealthPlan mTHSHealthPlanList;
    private RelativeLayout mRelativeLayoutInsuranceContainer;
    static final long serialVersionUID = 171L;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_insurance_detail, container, false);
        mPresenter = new THSInsuranceDetailPresenter(this);
        mAlertDialog = new AlertDialog.Builder(getActivity());

        View convertView = inflater.inflate(R.layout.ths_list, null);
        Bundle bundle = getArguments();
        if (null != bundle) {
            isLaunchedFromCostSummary = bundle.getBoolean(IS_LAUNCHED_FROM_COST_SUMMARY);
        }
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

        subscriberIDEditBox = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_subscriber_edit_text);


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
        mProgressbarContainer = (RelativeLayout) view.findViewById(R.id.ths_insurance_detail_container);

        mSuffixLabel = (Label) view.findViewById(R.id.ths_insurance_detail_suffix_edit_text_label);
        mSuffixEditText = (com.philips.platform.uid.view.widget.EditText) view.findViewById(R.id.ths_insurance_detail_suffix_edit_text);


        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        mTHSHealthPlanList = mPresenter.fetchHealthPlanList();
        mTHSRelationshipList = mPresenter.fetchSubscriberRelationList();
        mTHSHealthPlanListAdapter = new THSHealthPlanListAdapter(getActivity(), mTHSHealthPlanList);
        mTHSSubscriberRelationshipListAdapter = new THSSubscriberRelationshipListAdapter(getActivity(), mTHSRelationshipList);
        showProgressbar();
        mPresenter.fetchExistingSubscription();
    }

    protected void showProgressbar() {
        createCustomProgressBar(mProgressbarContainer, BIG);
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_INSURANCE_DETAIL, null, null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Insurance", true);
        }
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
        } else if (view.getId() == R.id.ths_insurance_detail_continue_button) {
            Relationship relationship;
            if (!mNotPrimarySubscriberCheckBox.isChecked()) {
                relationship = mTHSRelationshipList.getRelationShipList().get(0);// primary subscriber by default
            } else {
                relationship = mInsuranceRelationship;
            }
            String firstName = null;
            String lastName = null;
            String dob = null;
            if (null != relationship && !relationship.isPrimarySubscriber()) {
                firstName = (firstNameEditBox.getText() == null) ? null : firstNameEditBox.getText().toString().trim();
                lastName = (lastNameEditBox.getText() == null) ? null : lastNameEditBox.getText().toString().trim();
                dob = (relationDOBEditBox.getText() == null) ? null : relationDOBEditBox.getText().toString().trim();
            }
            mPresenter.updateTHSInsuranceSubscription(mHealthPlan, subscriberIDEditBox.getText().toString().trim(), mSuffixEditText.getText().toString().trim(), relationship, firstName, lastName, dob);


        } else if (view.getId() == R.id.ths_insurance_detail_provider_relation_dob_edittext) {

            showDatePicker(relationDOBEditBox, getActivity(), false);
        }


    }

    public void updateInsuranceUI(THSSubscription tHSSubscription) {
        thsSubscriptionExisting = tHSSubscription;
        Subscription subscription = tHSSubscription.getSubscription();
        if (null != subscription) {
            if (subscription.getHealthPlan() != null) {
                mHealthPlan = subscription.getHealthPlan();
                insuranceEditBox.setText(subscription.getHealthPlan().getName());
            }
            if (subscription.getHealthPlan() != null && subscription.getHealthPlan().isUsesSuffix() && subscription.getSubscriberId() != null) {
                subscriberIDEditBox.setText(subscription.getSubscriberId());
            }
            if (subscription.getHealthPlan().isUsesSuffix()) {
                mSuffixLabel.setVisibility(View.VISIBLE);
                mSuffixEditText.setVisibility(View.VISIBLE);
                mSuffixEditText.setText(subscription.getSubscriberSuffix());
            } else {
                mSuffixLabel.setVisibility(View.GONE);
                mSuffixEditText.setVisibility(View.GONE);
            }
            if (subscription.getRelationship().isPrimarySubscriber()) {
                //if user is  a primary subscriber
                mNotPrimarySubscriberCheckBox.setChecked(false);
                mNotPrimarySubscriberRelativeLayout.setVisibility(View.GONE);
            } else {
                //if user is NOT a primary subscriber
                mNotPrimarySubscriberCheckBox.setChecked(true);
                mNotPrimarySubscriberRelativeLayout.setVisibility(View.VISIBLE);
            }

            if (subscription.getRelationship() != null) {
                mInsuranceRelationship = subscription.getRelationship();
                relationshipEditBox.setText(subscription.getRelationship().getName());
            }
            if (subscription.getPrimarySubscriberFirstName() != null) {
                firstNameEditBox.setText(subscription.getPrimarySubscriberFirstName());
            }
            if (subscription.getPrimarySubscriberLastName() != null) {
                lastNameEditBox.setText(subscription.getPrimarySubscriberLastName());
            }
            if (subscription.getPrimarySubscriberDateOfBirth() != null) {
                relationDOBEditBox.setText(subscription.getPrimarySubscriberDateOfBirth().toString());
            }


        }
    }

    private void showInsuranceListDialog(String title, BaseAdapter adapter) {
        mAlertDialog.setTitle(title);

        mAlertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                mHealthPlan = mTHSHealthPlanList.getHealthPlanList().get(position);
                insuranceEditBox.setText(mHealthPlan.getName());
                if (mHealthPlan.isUsesSuffix()) {
                    mSuffixLabel.setVisibility(View.VISIBLE);
                    mSuffixEditText.setVisibility(View.VISIBLE);
                } else {
                    mSuffixLabel.setVisibility(View.GONE);
                    mSuffixEditText.setVisibility(View.GONE);
                }
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
                mInsuranceRelationship = mTHSRelationshipList.getRelationShipList().get(position);
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
            } catch (ParseException exception) {
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
