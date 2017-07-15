package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSPickTimeFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;

public class THSProviderDetailsDisplayHelper {

    View.OnClickListener mOnClickListener;
    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    Context mContext;
    THSPRoviderDetailsViewInterface mThsPRoviderDetailsViewInterface;
    protected ImageView providerImage,isAvailableImage;
    protected Label providerName,practiceName,isAvailable,spokenLanguageValueLabel,yearsOfExpValueLabel,graduatedValueLabel,aboutMeValueLabel;
    protected RatingBar providerRating;
    protected Button detailsButtonOne,detailsButtonTwo,detailsButtonContinue;
    RelativeLayout mTimeSlotContainer;
    THSExpandableHeightGridView gridView;
    protected SwipeRefreshLayout swipeRefreshLayout;

    THSProviderDetailsDisplayHelper(Context context, View.OnClickListener onClickListener,
                                    SwipeRefreshLayout.OnRefreshListener onRefreshListener,
                                    THSPRoviderDetailsViewInterface thspRoviderDetailsViewInterface){
        mOnClickListener = onClickListener;
        mContext = context;
        mOnRefreshListener = onRefreshListener;
        mThsPRoviderDetailsViewInterface = thspRoviderDetailsViewInterface;
    }

    void setViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeProviderLayout);
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        providerImage = (ImageView) view.findViewById(R.id.details_providerImage);
        providerName = (Label) view.findViewById(R.id.details_providerNameLabel);
        practiceName = (Label) view.findViewById(R.id.details_practiceNameLabel);
        isAvailable = (Label) view.findViewById(R.id.details_isAvailableLabel);
        isAvailableImage = (ImageView) view.findViewById(R.id.details_isAvailableImage);
        providerRating = (RatingBar) view.findViewById(R.id.providerRatingValue);
        spokenLanguageValueLabel = (Label) view.findViewById(R.id.spokenLanguageValueLabel);
        yearsOfExpValueLabel = (Label) view.findViewById(R.id.yearsOfExpValueLabel);
        graduatedValueLabel = (Label) view.findViewById(R.id.graduatedValueLabel);
        aboutMeValueLabel = (Label) view.findViewById(R.id.aboutMeValueLabel);
        detailsButtonOne = (Button) view.findViewById(R.id.detailsButtonOne);
        detailsButtonTwo = (Button) view.findViewById(R.id.detailsButtonTwo);
        detailsButtonContinue = (Button) view.findViewById(R.id.detailsButtonContinue);
        gridView = (THSExpandableHeightGridView)view.findViewById(R.id.grid);
        detailsButtonOne.setVisibility(Button.GONE);
        detailsButtonOne.setEnabled(false);
        detailsButtonOne.setOnClickListener(mOnClickListener);
        detailsButtonTwo.setOnClickListener(mOnClickListener);
        detailsButtonContinue.setOnClickListener(mOnClickListener);

        mTimeSlotContainer = (RelativeLayout) view.findViewById(R.id.calendar_container_view);
    }

    void updateView(Provider provider){
        providerName.setText(provider.getFullName());
        swipeRefreshLayout.setRefreshing(false);
        providerRating.setRating(provider.getRating());
        spokenLanguageValueLabel.setText(getSpokenLanguages(provider.getSpokenLanguages()));
        yearsOfExpValueLabel.setText(""+provider.getYearsExperience());
        graduatedValueLabel.setText(provider.getSchoolName());
        aboutMeValueLabel.setText(provider.getTextGreeting());
        practiceName.setText(provider.getSpecialty().getName());
        checkAvailability(provider);
        updateViewBasedOnType(provider);
    }

    public void updateViewBasedOnType(Provider provider) {

        if(mThsPRoviderDetailsViewInterface.getFragmentTag()!=null && mThsPRoviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSPickTimeFragment.TAG)){
            mTimeSlotContainer.setVisibility(View.VISIBLE);
            isAvailable.setText(""+mThsPRoviderDetailsViewInterface.getAppointmentTimeSlots().size() + " " + "Available time slots");
            setAppointmentsToView();
        }else {
            detailsButtonContinue.setVisibility(View.GONE);
            mTimeSlotContainer.setVisibility(View.GONE);
            isAvailable.setText(""+provider.getVisibility());
        }
    }

    /**
     * This method checks for the provider visibility, if visible, user can schedule an appointment or start video chat right away
     * If provider is busy, user can schedule an appointment or wait in line.
     * If provider is offline, user can schedule an appointment
     * @param provider
     */
    private void checkAvailability(Provider provider) {

        if(ProviderVisibility.isOnCall(provider.getVisibility()) || ProviderVisibility.isVideoBusy(provider.getVisibility())){
            isAvailableImage.setVisibility(ImageView.GONE);
            if(isAvailableProviderData()){
                setButtonVisibilityForAvailableProvider();
            }else {
                detailsButtonOne.setVisibility(Button.VISIBLE);
                detailsButtonOne.setEnabled(true);
                detailsButtonOne.setText("I'll wait in line");
                detailsButtonTwo.setVisibility(View.VISIBLE);
                detailsButtonTwo.setText("Schedule an appointment");
            }
        }
        else if(ProviderVisibility.isVideoAvailable(provider.getVisibility())){
            isAvailableImage.setVisibility(ImageView.VISIBLE);
            if(mThsPRoviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSPickTimeFragment.TAG)){
                setButtonVisibilityForAvailableProvider();
            }else {
                detailsButtonOne.setVisibility(Button.VISIBLE);
                detailsButtonOne.setEnabled(true);
                detailsButtonOne.setText("See this doctor now");
                detailsButtonTwo.setVisibility(View.VISIBLE);
                detailsButtonTwo.setText("Schedule an appointment");
            }
        }else if(ProviderVisibility.isOffline(provider.getVisibility())){
            isAvailableImage.setVisibility(ImageView.GONE);
            if(mThsPRoviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSPickTimeFragment.TAG)){
                setButtonVisibilityForAvailableProvider();
            }else {
                detailsButtonOne.setVisibility(Button.GONE);
                detailsButtonTwo.setVisibility(View.VISIBLE);
                detailsButtonTwo.setText("Schedule an appointment");
            }
        }
    }

    private boolean isAvailableProviderData() {
        return mThsPRoviderDetailsViewInterface.getFragmentTag().equalsIgnoreCase(THSPickTimeFragment.TAG);
    }

    private void setButtonVisibilityForAvailableProvider() {
        detailsButtonOne.setVisibility(View.GONE);
        detailsButtonTwo.setVisibility(View.GONE);
        detailsButtonContinue.setVisibility(View.VISIBLE);
    }

    private void setAppointmentsToView() {
        THSAppointmentGridAdapter itemsAdapter =
                new THSAppointmentGridAdapter(mContext, mThsPRoviderDetailsViewInterface.getAppointmentTimeSlots());
        gridView.setAdapter(itemsAdapter);
        gridView.setExpanded(true);
    }

    /**
     * This method is used to break up the list of spoken languages into comma separated strings to
     * display on the screen.
     */

    protected String getSpokenLanguages(List<Language> spokenLanguages) {

        String languageList = "";
        for(int i = 0;i<spokenLanguages.size();i++){
            if(languageList.length() == 0){
                languageList = spokenLanguages.get(i).getName();
            } else {
                languageList = languageList + " , " + spokenLanguages.get(i).getName();
            }
        }
        return languageList;
    }

    void dismissRefreshLayout(){
        swipeRefreshLayout.setRefreshing(false);
    }

    void setRefreshing(){
        if(!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }
    }

}
