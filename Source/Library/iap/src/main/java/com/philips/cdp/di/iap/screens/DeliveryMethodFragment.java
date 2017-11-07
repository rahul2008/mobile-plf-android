package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.DeliveryModeAdapter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.session.NetworkConstants;

import java.util.List;

public class DeliveryMethodFragment extends InAppBaseFragment implements OnSetDeliveryModeListener, AddressController.AddressListener {

    private Context mContext;
    private RecyclerView mDeliveryRecyclerView;
    private AddressController mAddressController;

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
        mDeliveryRecyclerView = view.findViewById(R.id.iap_parcel_delivery_list);
        mAddressController = new AddressController(mContext, this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDeliveryRecyclerView.setLayoutManager(layoutManager);
        settingDataToAdapter();
    }

    private void settingDataToAdapter() {
        List<DeliveryModes> mDeliveryModes = CartModelContainer.getInstance().getDeliveryModes();
        if (mDeliveryModes == null) return;
        DeliveryModeAdapter mDeliveryModeAdapter = new DeliveryModeAdapter(mDeliveryModes, this);
        mDeliveryRecyclerView.setAdapter(mDeliveryModeAdapter);
        mDeliveryModeAdapter.notifyDataSetChanged();
    }

//    private View.OnClickListener mConfirmBtnClick = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            //do nothing
//        }
//    };

    @Override
    public void onItemClick(int position) {
        final List<DeliveryModes> deliveryModes = CartModelContainer.getInstance().getDeliveryModes();

        if (!isProgressDialogShowing())
            showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
        mAddressController.setDeliveryMode(deliveryModes.get(position).getCode());

    }

    @Override
    public void onGetRegions(Message msg) {
    //do nothing
    }

    @Override
    public void onGetUser(Message msg) {
    //do nothing
    }

    @Override
    public void onCreateAddress(Message msg) {
    //do nothing
    }

    @Override
    public void onGetAddress(Message msg) {
    //do nothing
    }

    @Override
    public void onSetDeliveryAddress(Message msg) {
    //do nothing
    }

    @Override
    public void onGetDeliveryModes(Message msg) {
    //do nothing
    }

    @Override
    public void onSetDeliveryMode(Message msg) {
        dismissProgressDialog();
        getFragmentManager().popBackStack();
    }
}
