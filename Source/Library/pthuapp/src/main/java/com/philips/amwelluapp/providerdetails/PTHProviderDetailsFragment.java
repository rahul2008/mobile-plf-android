package com.philips.amwelluapp.providerdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;

public class PTHProviderDetailsFragment extends PTHBaseFragment implements PTHPRoviderDetailsViewInterface ,SwipeRefreshLayout.OnRefreshListener{
    private Consumer consumer;
    private ProviderInfo providerInfo;
    private PTHProviderDetailsPresenter providerDetailsPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView providerImage,isAvailableImage;
    private Label providerName,practiceName,isAvailable,spokenLanguageValueLabel,yearsOfExpValueLabel,graduatedValueLabel,aboutMeValueLabel;
    private RatingBar providerRating;
    private Button detailsButtonOne,detailsButtonTwo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pth_provider_details_fragment, container, false);
        setViews(view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeProviderLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        providerDetailsPresenter = new PTHProviderDetailsPresenter(this);
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

        detailsButtonOne.setVisibility(Button.GONE);
        detailsButtonOne.setEnabled(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(null != getActionBarListener()) {
            getActionBarListener().updateActionBar("Provider details", true);
        }
    }


    public void setProviderAndConsumer(ProviderInfo providerInfo,Consumer consumer){
        this.consumer = consumer;
        this.providerInfo = providerInfo;
    }
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
    public Consumer getConsumerInfo() {
        return consumer;
    }

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
        Toast.makeText(getActivity(),""+provider.getSchoolName(),Toast.LENGTH_SHORT).show();;

    }

    private void checkAvailability(Provider provider) {
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
    }

    private String getSpokenLanguages(List<Language> spokenLanguages) {
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
    public void onRefresh() {
        if(!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }
        providerDetailsPresenter.fetchProviderDetails();
    }
}
