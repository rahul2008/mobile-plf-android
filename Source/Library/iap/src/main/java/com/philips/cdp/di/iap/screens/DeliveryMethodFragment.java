package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.DeliveryModeAdapter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.session.NetworkConstants;

import java.util.List;

/**
 * Created by philips on 8/11/17.
 */

public class DeliveryMethodFragment extends InAppBaseFragment {

    private Context mContext;
    private RecyclerView mDeliveryList;

    public static DeliveryMethodFragment createInstance(final Bundle args, final AnimationType animType) {
        DeliveryMethodFragment fragment = new DeliveryMethodFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(mContext.getResources().getString(R.string.iap_delivery_method), true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_delivery_method_fragment, container, false);
        mDeliveryList = (RecyclerView) view.findViewById(R.id.iap_parcel_delivery_list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDeliveryList.setLayoutManager(layoutManager);
        settingDataToAdapter();
    }

    private void settingDataToAdapter(){
        List<DeliveryModes> mDeliveryModes = CartModelContainer.getInstance().getDeliveryModes();
        if (mDeliveryModes == null) return;
        DeliveryModeAdapter mDeliveryModeAdapter = new DeliveryModeAdapter(mContext, R.layout.iap_delivery_mode_spinner_item, mDeliveryModes);
       // mDeliveryList.setClickable(true);
        mDeliveryList.setAdapter(mDeliveryModeAdapter);
        mDeliveryModeAdapter.notifyDataSetChanged();
    }
}
