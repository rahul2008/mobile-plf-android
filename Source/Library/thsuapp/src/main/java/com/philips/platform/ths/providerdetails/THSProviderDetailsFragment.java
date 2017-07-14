package com.philips.platform.ths.providerdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;

import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.R.id.swipeRefreshLayout;

/**
 * This class is used to display the provider details selected by the user.
 */
public class THSProviderDetailsFragment extends THSBaseFragment implements View.OnClickListener, THSPRoviderDetailsViewInterface,SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = THSProviderDetailsFragment.class.getSimpleName();
    private Consumer consumer;
    private THSProviderInfo mThsProviderInfo;
    private THSAvailableProvider mThsAvailableProvider;
    private THSProviderDetailsPresenter providerDetailsPresenter;
    private Practice mPractice;
    private THSProviderDetailsDisplayHelper mThsProviderDetailsDisplayHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_provider_details_fragment, container, false);

        mThsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(getContext(),this,this,this);
        mThsProviderDetailsDisplayHelper.setViews(view);

        providerDetailsPresenter = new THSProviderDetailsPresenter(this);
        return view;
    }

    /**
     * This method is used to set the provider details in the provider details screen.
     * @param provider
     */
    @Override
    public void updateView(Provider provider) {
        mThsProviderDetailsDisplayHelper.updateView(provider);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(null != getActionBarListener()) {
            getActionBarListener().updateActionBar("Provider details", true);
        }
    }

    public void setTHSProviderEntity(THSProviderEntity  thsProviderEntity){
        if(thsProviderEntity instanceof THSProviderInfo) {
            this.mThsProviderInfo = (THSProviderInfo) thsProviderEntity;
        }else {
            this.mThsAvailableProvider = (THSAvailableProvider)thsProviderEntity;
        }
    }

    public void setConsumerAndPractice(Consumer consumer, Practice practice){
        this.consumer = consumer;
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
    public THSProviderInfo getTHSProviderInfo() {
        if (mThsProviderInfo != null) {
            return mThsProviderInfo;
        } else {
            THSProviderInfo thsProviderInfo = new THSProviderInfo();
            thsProviderInfo.setTHSProviderInfo(mThsAvailableProvider.getProviderInfo());
            return thsProviderInfo;
        }
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
        mThsProviderDetailsDisplayHelper.dismissRefreshLayout(false);
    }

    @Override
    public List<Date> getAppointmentTimeSlots() {
        return mThsAvailableProvider.getAvailableAppointmentTimeSlots();
    }

    @Override
    public String getFragmentTag() {
        return THSProviderDetailsFragment.TAG;
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
        mThsProviderDetailsDisplayHelper.setRefreshing();
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
