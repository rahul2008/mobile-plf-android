package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkConstants;

public class EmptyPurchaseHistoryFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener {

    private Context mContext;
    private Button mContinueShoppingBtn;
    private Button mContactConsumerCare;

    public static EmptyPurchaseHistoryFragment createInstance(Bundle args,
                                                              BaseAnimationSupportFragment.AnimationType animType) {
        EmptyPurchaseHistoryFragment fragment = new EmptyPurchaseHistoryFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_empty_puchase_history, container, false);
        mContinueShoppingBtn = (Button) rootView.findViewById(R.id.btn_continue_shopping);
        mContactConsumerCare = (Button) rootView.findViewById(R.id.btn_contact_consumer_care);

        mContinueShoppingBtn.setOnClickListener(this);
        mContactConsumerCare.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_order_history);
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment != null) {
            getFragmentManager().popBackStack();
        } else {
            finishActivity();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == mContinueShoppingBtn) {
            launchProductCatalog();
        } else if (v == mContactConsumerCare) {
            //Launch digital care contact us
            Toast.makeText(mContext, "Not Implemented", Toast.LENGTH_SHORT).show();
        }
    }
}
