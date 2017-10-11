/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.appointment.THSConfirmAppointmentFragment;
import com.philips.platform.ths.appointment.THSSetReminderDialogFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.NotificationBadge;
import com.philips.platform.uid.view.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class THSProviderDetailsDisplayHelper implements AdapterView.OnItemClickListener {

    private View.OnClickListener mOnClickListener;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    private Context mContext;
    private THSProviderDetailsViewInterface thsProviderDetailsViewInterface;
    protected CircularImageView providerImage;
    protected ImageView isAvailableImage;
    private Label providerName, practiceName, isAvailable, spokenLanguageValueLabel, yearsOfExpValueLabel,
            graduatedValueLabel, aboutMeValueLabel, mLabelDate, visitCostValueLabel, reminderValue;
    protected RatingBar providerRating;
    protected Button detailsButtonOne, detailsButtonTwo, detailsButtonContinue;
    private RelativeLayout mTimeSlotContainer;
    private THSExpandableHeightGridView gridView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private THSBaseFragment thsBaseFragment;
    private NotificationBadge notificationBadge;
    private RelativeLayout available_provider_details_container;
    private List<Date> dates;
    private Label details_isAvailableImage_text;
    private FrameLayout details_isAvailableImage_layout;


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
        available_provider_details_container.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeProviderLayout);
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        visitCostValueLabel = (Label) view.findViewById(R.id.visitCostValueLabel);
        providerImage = (CircularImageView) view.findViewById(R.id.details_providerImage);
        reminderValue = (Label) view.findViewById(R.id.reminderValue);
        providerName = (Label) view.findViewById(R.id.details_providerNameLabel);
        practiceName = (Label) view.findViewById(R.id.details_practiceNameLabel);
        isAvailable = (Label) view.findViewById(R.id.details_isAvailableLabel);
        details_isAvailableImage_text = (Label) view.findViewById(R.id.details_isAvailableImage_text);
        isAvailableImage = (ImageView) view.findViewById(R.id.details_isAvailableImage);
        notificationBadge = (NotificationBadge) view.findViewById(R.id.notification_badge);
        providerRating = (RatingBar) view.findViewById(R.id.providerRatingValue);
        spokenLanguageValueLabel = (Label) view.findViewById(R.id.spokenLanguageValueLabel);
        yearsOfExpValueLabel = (Label) view.findViewById(R.id.yearsOfExpValueLabel);
        graduatedValueLabel = (Label) view.findViewById(R.id.graduatedValueLabel);
        aboutMeValueLabel = (Label) view.findViewById(R.id.aboutMeValueLabel);
        detailsButtonOne = (Button) view.findViewById(R.id.detailsButtonOne);
        detailsButtonTwo = (Button) view.findViewById(R.id.detailsButtonTwo);
        detailsButtonContinue = (Button) view.findViewById(R.id.detailsButtonContinue);
        gridView = (THSExpandableHeightGridView) view.findViewById(R.id.grid);
        gridView.setOnItemClickListener(this);
        details_isAvailableImage_layout = (FrameLayout) view.findViewById(R.id.details_isAvailableImage_layout);
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
        providerName.setText(provider.getFullName());
        swipeRefreshLayout.setRefreshing(false);
        providerRating.setRating(provider.getRating());
        providerRating.setText(String.valueOf(provider.getRating()));
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
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
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
            providerAvailabilityString = context.getResources().getString(R.string.provider_available);
            isAvailableImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.green_available_icon, context.getTheme()));
        } else if (providerVisibility.equals(THSConstants.PROVIDER_OFFLINE)) {
            providerAvailabilityString = context.getResources().getString(R.string.provider_offline);
            isAvailableImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.provider_offline_icon, context.getTheme()));
        } else if (providerVisibility.equals(THSConstants.PROVIDER_WEB_BUSY)) {
            providerAvailabilityString = context.getResources().getString(R.string.provider_busy);
            isAvailableImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.waiting_patient_icon,context.getTheme()));
            details_isAvailableImage_text.setText(String.valueOf(provider.getWaitingRoomCount()));
            isAvailableImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.waiting_patient_icon, context.getTheme()));
        }

        if (dates != null) {
            mTimeSlotContainer.setVisibility(View.VISIBLE);
            isAvailable.setText(context.getString(R.string.ths_available_time_slots_text));
            mLabelDate.setText(new SimpleDateFormat(THSConstants.DATE_FORMATTER, Locale.getDefault()).
                    format(((THSAvailableProviderDetailFragment) thsBaseFragment).getDate()));
            setAppointmentsToView(dates);
            isAvailableImage.setVisibility(View.GONE);
            details_isAvailableImage_layout.setVisibility(View.GONE);
            notificationBadge.setVisibility(View.VISIBLE);
            notificationBadge.setText(String.valueOf(dates.size()));

        } else {
            detailsButtonContinue.setVisibility(View.GONE);
            mTimeSlotContainer.setVisibility(View.GONE);
            isAvailable.setText(providerAvailabilityString);
        }
    }

    /**
     * This method checks for the provider visibility, if visible, user can schedule an appointment or start video chat right away
     * If provider is busy, user can schedule an appointment or wait in line.
     * If provider is offline, user can schedule an appointment
     *
     * @param provider : provider object to be passed
     */
    private void checkAvailability(Provider provider) {

        if (ProviderVisibility.isOnCall(provider.getVisibility()) || ProviderVisibility.isVideoBusy(provider.getVisibility())) {
            isAvailableImage.setVisibility(ImageView.GONE);
            if (isAvailableProviderData()) {
                setButtonVisibilityForAvailableProvider();
            } else {
                isAvailableImage.setVisibility(ImageView.VISIBLE);
                detailsButtonOne.setVisibility(Button.VISIBLE);
                detailsButtonOne.setEnabled(true);
                if (THSManager.getInstance().isMatchMakingVisit()) {
                    detailsButtonOne.setText(mContext.getString(R.string.ths_continue));
                    detailsButtonTwo.setVisibility(View.GONE);
                }else {
                    detailsButtonOne.setText(mContext.getString(R.string.ths_ill_wait_in_line_button_text));
                    detailsButtonTwo.setVisibility(View.VISIBLE);
                    detailsButtonTwo.setText(mContext.getString(R.string.ths_schedule_an_appointment_button_text));
                }
            }
        } else if (ProviderVisibility.isVideoAvailable(provider.getVisibility())) {
            isAvailableImage.setVisibility(ImageView.VISIBLE);
            if (thsProviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSAvailableProviderDetailFragment.TAG)) {
                setButtonVisibilityForAvailableProvider();
            } else {
                detailsButtonOne.setVisibility(Button.VISIBLE);
                detailsButtonOne.setEnabled(true);
                if (THSManager.getInstance().isMatchMakingVisit()) {
                    detailsButtonOne.setText(mContext.getString(R.string.ths_continue));
                    detailsButtonTwo.setVisibility(View.GONE);
                } else {
                    detailsButtonOne.setText(mContext.getString(R.string.ths_see_this_doctor_now_button_text));
                    detailsButtonTwo.setVisibility(View.VISIBLE);
                    detailsButtonTwo.setText(mContext.getString(R.string.ths_schedule_an_appointment_button_text));
                }
            }
        } else if (ProviderVisibility.isOffline(provider.getVisibility())) {
            isAvailableImage.setVisibility(ImageView.GONE);
            if (thsProviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSAvailableProviderDetailFragment.TAG)) {
                setButtonVisibilityForAvailableProvider();
            } else {
                isAvailableImage.setVisibility(ImageView.VISIBLE);
                detailsButtonOne.setVisibility(Button.GONE);
                detailsButtonTwo.setVisibility(View.VISIBLE);
                detailsButtonTwo.setText(mContext.getString(R.string.ths_schedule_an_appointment_button_text));
            }
        }
    }

    private boolean isAvailableProviderData() {
        return thsProviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSAvailableProviderDetailFragment.TAG);
    }

    private void setButtonVisibilityForAvailableProvider() {
        detailsButtonOne.setVisibility(View.GONE);
        detailsButtonTwo.setVisibility(View.GONE);
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

    public void updateEstimateCost(EstimatedVisitCost estimatedVisitCost) {
        visitCostValueLabel.setText(String.valueOf("$" + estimatedVisitCost.getCost()));
    }

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
        reminderValue.setText(reminderTime + " " + mContext.getResources().getString(R.string.ths_before_appointment));
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
