/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.appointment.THSConfirmAppointmentFragment;
import com.philips.platform.ths.appointment.THSSetReminderDialogFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSCustomButtonWithDrawableIcon;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PROVIDER_IMAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;

public class THSProviderDetailsDisplayHelper implements AdapterView.OnItemClickListener {

    private View.OnClickListener mOnClickListener;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    private Context mContext;
    private THSProviderDetailsViewInterface thsProviderDetailsViewInterface;
    protected CircularImageView providerImage;
    private Label providerName, practiceName, isAvailable, spokenLanguageValueLabel, yearsOfExpValueLabel,
            graduatedValueLabel, aboutMeValueLabel, mLabelDate, reminderValue;
    protected RatingBar providerRating;
    protected THSCustomButtonWithDrawableIcon detailsButtonOne, detailsButtonContinue, scheduleOptionButton;
    private RelativeLayout mTimeSlotContainer,ths_match_making_ProgressBarWithLabel, detailsButtonTwo;
    private View horizontalLineBelow, horizontalLineAbove;
    private THSExpandableHeightGridView gridView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private THSBaseFragment thsBaseFragment;
    private RelativeLayout available_provider_details_container,ths_match_making_ProgressBarWithLabel_container,bottomLayout;
    private List<Date> dates;


    public THSProviderDetailsDisplayHelper(Context context, View.OnClickListener onClickListener,
                                           SwipeRefreshLayout.OnRefreshListener onRefreshListener,
                                           THSProviderDetailsViewInterface thsProviderDetailsViewInterface,
                                           THSBaseFragment thsBaseFragment, View view) {
        mOnClickListener = onClickListener;
        mContext = context;
        mOnRefreshListener = onRefreshListener;
        this.thsProviderDetailsViewInterface = thsProviderDetailsViewInterface;
        this.thsBaseFragment = thsBaseFragment;
        setViews(view);
    }

    private void setViews(View view) {
        available_provider_details_container = (RelativeLayout) view.findViewById(R.id.available_provider_details_container);
        ths_match_making_ProgressBarWithLabel_container = view.findViewById(R.id.ths_match_making_ProgressBarWithLabel_container);
        ths_match_making_ProgressBarWithLabel = view.findViewById(R.id.ths_match_making_ProgressBarWithLabel);
        bottomLayout = view.findViewById(R.id.bottomLayout);
        available_provider_details_container.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeProviderLayout);
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        providerImage = (CircularImageView) view.findViewById(R.id.details_providerImage);
        reminderValue = (Label) view.findViewById(R.id.reminderValue);
        providerName = (Label) view.findViewById(R.id.details_providerNameLabel);
        practiceName = (Label) view.findViewById(R.id.details_practiceNameLabel);
        isAvailable = (Label) view.findViewById(R.id.details_isAvailableLabel);
        providerRating = (RatingBar) view.findViewById(R.id.ths_rating_bar);
        spokenLanguageValueLabel = (Label) view.findViewById(R.id.spokenLanguageValueLabel);
        yearsOfExpValueLabel = (Label) view.findViewById(R.id.yearsOfExpValueLabel);
        graduatedValueLabel = (Label) view.findViewById(R.id.graduatedValueLabel);
        aboutMeValueLabel = (Label) view.findViewById(R.id.aboutMeValueLabel);
        detailsButtonOne = (THSCustomButtonWithDrawableIcon) view.findViewById(R.id.detailsButtonOne);
        scheduleOptionButton = (THSCustomButtonWithDrawableIcon) view.findViewById(R.id.schedule_option);
        scheduleOptionButton.setOnClickListener(mOnClickListener);
        detailsButtonTwo = (RelativeLayout) view.findViewById(R.id.schedule_container);
        horizontalLineBelow = view.findViewById(R.id.horizontalLineOne_new);
        horizontalLineAbove = view.findViewById(R.id.horizontalLineOne);
        detailsButtonContinue = (THSCustomButtonWithDrawableIcon) view.findViewById(R.id.detailsButtonContinue);
        gridView = (THSExpandableHeightGridView) view.findViewById(R.id.grid);
        gridView.setOnItemClickListener(this);
        detailsButtonOne.setVisibility(Button.GONE);
        detailsButtonOne.setEnabled(false);
        detailsButtonOne.setOnClickListener(mOnClickListener);
        detailsButtonTwo.setOnClickListener(mOnClickListener);
        detailsButtonContinue.setOnClickListener(mOnClickListener);
        mLabelDate = (Label) view.findViewById(R.id.calendar_container);
        mLabelDate.setOnClickListener(mOnClickListener);
        mTimeSlotContainer = (RelativeLayout) view.findViewById(R.id.calendar_container_view);
        RelativeLayout set_a_reminder_layout = (RelativeLayout) view.findViewById(R.id.set_reminder_layout);
        set_a_reminder_layout.setOnClickListener(mOnClickListener);
    }

    public void launchSetRemainderDialogFragment(THSBaseFragment thsBaseFragment) {

        THSSetReminderDialogFragment thsSetReminderDialogFragment = new THSSetReminderDialogFragment();
        thsSetReminderDialogFragment.setDialogFragmentCallback((THSAvailableProviderDetailFragment) thsBaseFragment);
        thsSetReminderDialogFragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), THSSetReminderDialogFragment.TAG);
    }

    public void updateView(Provider provider, List<Date> dates) {
        available_provider_details_container.setVisibility(View.VISIBLE);
        setDODProgressVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        providerName.setText(provider.getFullName());
        swipeRefreshLayout.setRefreshing(false);
        providerRating.setRating(provider.getRating());
        spokenLanguageValueLabel.setText(getSpokenLanguages(provider.getSpokenLanguages()));
        yearsOfExpValueLabel.setText(String.valueOf(provider.getYearsExperience()));
        graduatedValueLabel.setText(provider.getSchoolName());
        aboutMeValueLabel.setText(provider.getTextGreeting());
        practiceName.setText(provider.getSpecialty().getName());

        if (thsProviderDetailsViewInterface.getProvider().hasImage()) {
            try {
                if (null == thsProviderDetailsViewInterface.getTHSProviderInfo()) {
                    THSManager.getInstance().getAwsdk(thsBaseFragment.getContext()).getPracticeProvidersManager().
                            newImageLoader(thsProviderDetailsViewInterface.getProvider(), providerImage,
                                    ProviderImageSize.SMALL).placeholder(providerImage.getResources().
                            getDrawable(R.drawable.doctor_placeholder,mContext.getTheme())).build().load();
                } else {

                    THSManager.getInstance().getAwsdk(thsBaseFragment.getContext()).getPracticeProvidersManager().
                            newImageLoader(thsProviderDetailsViewInterface.getTHSProviderInfo().getProviderInfo(), providerImage,
                                    ProviderImageSize.SMALL).placeholder(providerImage.getResources().
                            getDrawable(R.drawable.doctor_placeholder,mContext.getTheme())).build().load();
                }
            } catch (Exception e) {
                final String errorTag = THSTagUtils.createErrorTag(ANALYTIC_FETCH_PROVIDER_IMAGE, e.getMessage());
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SERVER_ERROR, errorTag);
            }
        }

        checkAvailability(provider);
        updateViewBasedOnType(provider, dates);
    }

    private void updateViewBasedOnType(Provider provider, List<Date> dates) {
        this.dates = dates;
        String providerAvailabilityString = null;
        String providerVisibility = provider.getVisibility().toString();
        Context context = isAvailable.getContext();
        if (providerVisibility.equals(THSConstants.WEB_AVAILABLE)) {
            providerAvailabilityString = context.getResources().getString(R.string.ths_provider_available);
            isAvailable.setTextColor(ContextCompat.getColor(context, com.philips.platform.uid.R.color.uid_signal_green_level_30));
        } else if (providerVisibility.equals(THSConstants.PROVIDER_OFFLINE)) {
            providerAvailabilityString = context.getResources().getString(R.string.ths_offline);
        } else if (providerVisibility.equals(THSConstants.PROVIDER_WEB_BUSY)) {
            providerAvailabilityString = context.getResources().getString(R.string.ths_provider_occupied);
            isAvailable.setTextColor(ContextCompat.getColor(context, com.philips.platform.uid.R.color.uid_signal_orange_level_30));
        }else if((providerVisibility.equals(THSConstants.PROVIDER_OFFLINE) || providerVisibility.equals(THSConstants.PROVIDER_WEB_BUSY)) &&
                !thsProviderDetailsViewInterface.getPractice().isShowScheduling()){
            detailsButtonTwo.setVisibility(View.GONE);
            horizontalLineBelow.setVisibility(View.GONE);
        }

        if (dates != null) {
            mTimeSlotContainer.setVisibility(View.VISIBLE);
            horizontalLineAbove.setVisibility(View.VISIBLE);
            isAvailable.setText(String.valueOf(dates.size()) + " " + context.getString(R.string.ths_available_time_slots_text));

            isAvailable.setTextColor(UIDHelper.getColorFromAttribute(context.getTheme(),R.attr.uidHyperlinkDefaultNormalTextColor,  Color.TRANSPARENT));
            mLabelDate.setText(new SimpleDateFormat(THSConstants.DATE_FORMATTER, Locale.getDefault()).
                    format(((THSAvailableProviderDetailFragment) thsBaseFragment).getDate()));
            setAppointmentsToView(dates);
        } else {
            detailsButtonContinue.setVisibility(View.GONE);
            mTimeSlotContainer.setVisibility(View.GONE);
            horizontalLineAbove.setVisibility(View.GONE);
            isAvailable.setText(providerAvailabilityString);
        }
    }

    protected void setDODVisibility(boolean show){
        if(show) {
            thsBaseFragment.createCustomProgressBar(ths_match_making_ProgressBarWithLabel, 2);
            setDODProgressVisibility(View.VISIBLE);
            setProviderViewVisibility();
        }else {
            thsBaseFragment.hideProgressBar();
            setDODProgressVisibility(View.GONE);
            setProviderViewVisibility();
        }
    }

    private void setDODProgressVisibility(int visible) {

        ths_match_making_ProgressBarWithLabel_container.setVisibility(visible);
    }

    private void setProviderViewVisibility() {
        available_provider_details_container.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
    }

    /**
     * This method checks for the provider visibility, if visible, user can schedule an appointment or start video chat right away
     * If provider is busy, user can schedule an appointment or wait in line.
     * If provider is offline, user can schedule an appointment
     *
     * @param provider : provider object to be passed
     */
    private void checkAvailability(Provider provider) {

        if (ProviderVisibility.ON_CALL.equalsIgnoreCase(provider.getVisibility()) || ProviderVisibility.WEB_BUSY.equalsIgnoreCase(provider.getVisibility())) {
            if (isAvailableProviderData()) {
                setButtonVisibilityForAvailableProvider();
            } else {
                detailsButtonOne.setVisibility(Button.VISIBLE);
                detailsButtonOne.setEnabled(true);
                detailsButtonOne.setText(mContext.getString(R.string.ths_ill_wait_in_line_button_text));
                if (THSManager.getInstance().isMatchMakingVisit()) {
                    detailsButtonTwo.setVisibility(View.GONE);
                    horizontalLineBelow.setVisibility(View.GONE);
                }else {
                    checkForUrgentCare();
                }
            }
        } else if (ProviderVisibility.WEB_AVAILABLE.equalsIgnoreCase(provider.getVisibility())) {
            if (thsProviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSAvailableProviderDetailFragment.TAG)) {
                setButtonVisibilityForAvailableProvider();
            } else {
                detailsButtonOne.setVisibility(Button.VISIBLE);
                detailsButtonOne.setEnabled(true);
                detailsButtonOne.setText(mContext.getString(R.string.ths_insurancedetail_selectprovider));
                if (THSManager.getInstance().isMatchMakingVisit()) {
                    detailsButtonTwo.setVisibility(View.GONE);
                    horizontalLineBelow.setVisibility(View.GONE);
                } else {
                    checkForUrgentCare();
                }
            }
        } else if (ProviderVisibility.OFFLINE.equalsIgnoreCase(provider.getVisibility())) {
            if (thsProviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSAvailableProviderDetailFragment.TAG)) {
                setButtonVisibilityForAvailableProvider();
            } else {
                detailsButtonOne.setVisibility(Button.GONE);
                detailsButtonTwo.setVisibility(View.GONE);
                horizontalLineBelow.setVisibility(View.GONE);
                scheduleVisitAvailability();
            }
        }
    }

    private void checkForUrgentCare() {
        scheduleOptionButton.setVisibility(View.GONE);
        if(null == thsProviderDetailsViewInterface.getPractice() || thsProviderDetailsViewInterface.getPractice().isShowScheduling()) {
            detailsButtonTwo.setVisibility(View.VISIBLE);
            horizontalLineBelow.setVisibility(View.VISIBLE);
        }else {
            detailsButtonTwo.setVisibility(View.GONE);
            horizontalLineBelow.setVisibility(View.GONE);
        }
    }

    private void scheduleVisitAvailability() {
        if(null == thsProviderDetailsViewInterface.getPractice() || thsProviderDetailsViewInterface.getPractice().isShowScheduling()) {
            scheduleOptionButton.setVisibility(View.VISIBLE);
        }else {
            scheduleOptionButton.setVisibility(View.GONE);
        }
    }

    private boolean isAvailableProviderData() {
        return thsProviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSAvailableProviderDetailFragment.TAG);
    }

    private void setButtonVisibilityForAvailableProvider() {
        detailsButtonOne.setVisibility(View.GONE);
        detailsButtonTwo.setVisibility(View.GONE);
        horizontalLineBelow.setVisibility(View.GONE);
        detailsButtonContinue.setVisibility(View.VISIBLE);
    }

    private void setAppointmentsToView(List<Date> dates) {
        THSAppointmentGridAdapter itemsAdapter = new THSAppointmentGridAdapter(mContext, dates);
        gridView.setAdapter(itemsAdapter);
        gridView.setExpanded(true);
    }

    /**
     * This method is used to break up the list of spoken languages into comma separated strings to
     * display on the screen.
     */

    private String getSpokenLanguages(List<Language> spokenLanguages) {

        String languageList = "";
        for (int i = 0; i < spokenLanguages.size(); i++) {
            if (languageList.length() == 0) {
                languageList = spokenLanguages.get(i).getName();
            } else {
                languageList = languageList + " , " + spokenLanguages.get(i).getName();
            }
        }
        return languageList;
    }

    void dismissRefreshLayout() {
        swipeRefreshLayout.setRefreshing(false);
    }

    void setRefreshing() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

  /*  public void updateEstimateCost(EstimatedVisitCost estimatedVisitCost) {
        visitCostValueLabel.setText(String.valueOf("$" + estimatedVisitCost.getCost()));
    }*/

    public void updateContinueButtonState(boolean isEnabled) {
        detailsButtonContinue.setEnabled(isEnabled);
    }

    public void launchConfirmAppointmentFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, thsProviderDetailsViewInterface.getTHSProviderInfo());
        bundle.putSerializable(THSConstants.THS_DATE, dates.get(position));
        bundle.putString(THSConstants.THS_SET_REMINDER_EXTRA_KEY, "" + thsProviderDetailsViewInterface.getReminderTime());
        final THSConfirmAppointmentFragment fragment = new THSConfirmAppointmentFragment();
        fragment.setFragmentLauncher(thsBaseFragment.getFragmentLauncher());
        thsBaseFragment.addFragment(fragment, THSConfirmAppointmentFragment.TAG, bundle, true);
    }

    public void setReminderValue(String reminderTime) {
        reminderValue.setText(reminderTime + " " + mContext.getResources().getString(R.string.ths_schedule_appointment_text));
    }

    public String getReminderValue() {
        return reminderValue.getText().toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        thsProviderDetailsViewInterface.onCalenderItemClick(position);
    }
}
