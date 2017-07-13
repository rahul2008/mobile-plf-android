package com.philips.platform.ths.providerdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSPickTimeFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class is used to display the provider details selected by the user.
 */
public class THSProviderDetailsFragment extends THSBaseFragment implements View.OnClickListener, THSPRoviderDetailsViewInterface,SwipeRefreshLayout.OnRefreshListener{
    private Consumer consumer;
    private ProviderInfo providerInfo;
    private THSProviderDetailsPresenter providerDetailsPresenter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ImageView providerImage,isAvailableImage;
    protected Label providerName,practiceName,isAvailable,spokenLanguageValueLabel,yearsOfExpValueLabel,graduatedValueLabel,aboutMeValueLabel;
    protected RatingBar providerRating;
    protected Button detailsButtonOne,detailsButtonTwo,detailsButtonContinue;
    private Practice mPractice;
    private RelativeLayout mTimeSlotContainer;
    private GridView gridView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_provider_details_fragment, container, false);
        setViews(view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeProviderLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        providerDetailsPresenter = new THSProviderDetailsPresenter(this);
        return view;
    }

    private void setViews(View view) {
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
        gridView = (GridView)view.findViewById(R.id.grid);

        detailsButtonOne.setVisibility(Button.GONE);
        detailsButtonOne.setEnabled(false);
        detailsButtonOne.setOnClickListener(this);
        detailsButtonTwo.setOnClickListener(this);
        detailsButtonContinue.setOnClickListener(this);

        mTimeSlotContainer = (RelativeLayout) view.findViewById(R.id.calendar_container_view);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(null != getActionBarListener()) {
            getActionBarListener().updateActionBar("Provider details", true);
        }
    }


    public void setProviderAndConsumerAndPractice(ProviderInfo providerInfo, Consumer consumer, Practice practice){
        this.consumer = consumer;
        this.providerInfo = providerInfo;
        this.mPractice = practice;
    }

    /**
     * As soon as the activity is created for the component, onRefresh method is called so that the
     * provider details are fetched. This will,avoid code duplication in creating new method which
     * will inturn call the same getprovider details method.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();

    }

    @Override
    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    @Override
    public Practice getPracticeInfo() {
        return mPractice;
    }

    @Override
    public Consumer getConsumerInfo() {
        return consumer;
    }

    @Override
    public void dismissRefreshLayout(){
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public List<Date> getAppointmentTimeSlots() {
        return null;
    }

    /**
     * This method is used to set the provider details in the provider details screen.
     * @param provider
     */
    @Override
    public void updateView(Provider provider) {

        checkAvailability(provider);
        swipeRefreshLayout.setRefreshing(false);
        providerName.setText(provider.getFullName());
        practiceName.setText(provider.getSpecialty().getName());
        isAvailable.setText(""+provider.getVisibility());
        providerRating.setRating(provider.getRating());
        spokenLanguageValueLabel.setText(getSpokenLanguages(provider.getSpokenLanguages()));
        yearsOfExpValueLabel.setText(""+provider.getYearsExperience());
        graduatedValueLabel.setText(provider.getSchoolName());
        aboutMeValueLabel.setText(provider.getTextGreeting());

    }

    /**
     * This method checks for the provider visibility, if visible, user can schedule an appointment or start video chat right away
     * If provider is busy, user can schedule an appointment or wait in line.
     * If provider is offline, user can schedule an appointment
     * @param provider
     */
    private void checkAvailability(Provider provider) {

        FragmentManager.BackStackEntry backStackEntryAt = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-1);
        String name = backStackEntryAt.getName();

        if(ProviderVisibility.isOnCall(provider.getVisibility()) || ProviderVisibility.isVideoBusy(provider.getVisibility())){
            isAvailableImage.setVisibility(ImageView.GONE);
            detailsButtonOne.setVisibility(Button.VISIBLE);
            detailsButtonOne.setEnabled(true);
            detailsButtonOne.setText("I'll wait in line");
            detailsButtonTwo.setText("Schedule an appointment");
        }
        else if(ProviderVisibility.isVideoAvailable(provider.getVisibility())){
            isAvailableImage.setVisibility(ImageView.VISIBLE);
            detailsButtonOne.setVisibility(Button.VISIBLE);
            detailsButtonOne.setEnabled(true);
            detailsButtonOne.setText("See this doctor now");
            detailsButtonTwo.setText("Schedule an appointment");
        }else if(ProviderVisibility.isOffline(provider.getVisibility())){
            isAvailableImage.setVisibility(ImageView.GONE);
            detailsButtonOne.setVisibility(Button.GONE);
            detailsButtonTwo.setText("Schedule an appointment");
        }

        if(name!=null && name.equalsIgnoreCase(THSPickTimeFragment.TAG)){
            detailsButtonOne.setVisibility(View.GONE);
            detailsButtonTwo.setVisibility(View.GONE);
            detailsButtonContinue.setVisibility(View.VISIBLE);
            mTimeSlotContainer.setVisibility(View.VISIBLE);


            GridAdapter itemsAdapter =
                    new GridAdapter(getContext(), getAppointmentTimeSlots());
            gridView.setAdapter(itemsAdapter);
        }else {
            detailsButtonContinue.setVisibility(View.GONE);
            mTimeSlotContainer.setVisibility(View.GONE);
        }

    }

    /**
     * This method is used to break up the list of spoken languages into comma separated strings to
     * display on the screen.
     */

    protected String getSpokenLanguages(List<Language> spokenLanguages) {

        String languageList = "";
        for(Language language: spokenLanguages){
            if(languageList.length() == 0){
                languageList = language.getName();
            } else {
                languageList = languageList + " , " + language.getName();
            }
        }
        return languageList;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    @Override
    public void onRefresh() {
        if(!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }
        providerDetailsPresenter.fetchProviderDetails();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.detailsButtonOne) {
            providerDetailsPresenter.onEvent(R.id.detailsButtonOne);
        }else if(i == R.id.detailsButtonTwo){
            providerDetailsPresenter.onEvent(R.id.detailsButtonTwo);
        }
    }
}
