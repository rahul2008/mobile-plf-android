/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.cart.IAPCartListener;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.List;

public abstract class InAppBaseFragment extends Fragment implements BackEventListener {
    private Context mContext;
    private ActionBarListener mActionbarUpdateListener;
    protected IAPListener mIapListener;
    private ProgressDialog mProgressDialog = null;

    String mTitle = "";

    protected IAPCartListener mProductCountListener = new IAPCartListener() {
        @Override
        public void onSuccess(final int count) {
            updateCount(count);
            dismissProgressDialog();
        }

        @Override
        public void onFailure(final Message msg) {
            dismissProgressDialog();
        }
    };

    public void setActionBarListener(ActionBarListener actionBarListener, IAPListener iapListener) {
        mActionbarUpdateListener = actionBarListener;
        mIapListener = iapListener;
    }

    public enum AnimationType {
        NONE
    }

    protected void setTitleAndBackButtonVisibility(int resourceId, boolean isVisible) {
        mTitle = getString(resourceId);
        if (mActionbarUpdateListener != null)
            mActionbarUpdateListener.updateActionBar(resourceId, isVisible);
    }


    protected void setTitleAndBackButtonVisibility(String title, boolean isVisible) {
        mTitle = title;
        if (mActionbarUpdateListener != null)
            mActionbarUpdateListener.updateActionBar(title, isVisible);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartIconVisibility(false); //Check whether it is required ?
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        NetworkUtility.getInstance().dismissErrorDialog();
        if (isProgressDialogShowing())
            dismissProgressDialog();
    }

    public void showProgressDialog(Context context, String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(message + "...");

        if ((!mProgressDialog.isShowing()) && !((Activity) context).isFinishing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public boolean isProgressDialogShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void changeProgressMessage(String message) {
        mProgressDialog.setMessage(message);
    }

    public void addFragment(InAppBaseFragment newFragment,
                            String newFragmentTag) {
        if (mActionbarUpdateListener == null || mIapListener == null)
            new RuntimeException("ActionBarListner and IAPListner cant be null");
        else {
            newFragment.setActionBarListener(mActionbarUpdateListener, mIapListener);
            if (getActivity() != null && !getActivity().isFinishing()) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(getId(), newFragment, newFragmentTag);
                transaction.addToBackStack(newFragmentTag);
                transaction.commitAllowingStateLoss();

                IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                        + newFragmentTag + ")");
            }
        }
    }

    public void showFragment(String fragmentTag) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);
        }
    }

    public boolean moveToPreviousFragment() {
        return getFragmentManager().popBackStackImmediate();
    }

    public void showProductCatalogFragment(String fragmentTag) {
        Fragment fragment = getFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment == null) {
            getFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            addFragment(ProductCatalogFragment.createInstance(new Bundle(),
                    AnimationType.NONE), ProductCatalogFragment.TAG);
        } else {
            getFragmentManager().popBackStack(ProductCatalogFragment.TAG, 0);
        }
    }

    public void clearFragmentStack() {
        if (getActivity() != null && getActivity() instanceof IAPActivity && !getActivity().isFinishing()) {
            FragmentManager fragManager = getActivity().getSupportFragmentManager();
            int count = fragManager.getBackStackEntryCount();
            for (; count >= 0; count--) {
                List<Fragment> fragmentList = fragManager.getFragments();
                if (fragmentList != null && fragmentList.size() > 0) {
                    fragManager.popBackStack();
                }
            }
        }
    }

    public void moveToVerticalAppByClearingStack() {
        clearFragmentStack();
        finishActivity();
    }

    protected boolean isNetworkConnected() {
        if (mContext != null && !NetworkUtility.getInstance().isNetworkAvailable(mContext)) {
            NetworkUtility.getInstance().showErrorDialog(mContext,
                    getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_you_are_offline), getString(R.string.iap_no_internet));
            return false;
        } else {
            return true;
        }
    }

    public void updateCount(final int count) {
        if (mIapListener != null) {
            mIapListener.onGetCartCount(count);
            dismissProgressDialog();
        }
    }

    public void setCartIconVisibility(final boolean shouldShow) {
        if (mIapListener != null) {
            mIapListener.updateCartIconVisibility(shouldShow);
        }
    }

    protected void finishActivity() {
        if (getActivity() != null && getActivity() instanceof IAPActivity && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void handleDeliveryMode(Message msg, AddressController addressController) {
        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
            dismissProgressDialog();
        } else if ((msg.obj instanceof IAPNetworkError)) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
            dismissProgressDialog();
        } else if ((msg.obj instanceof GetDeliveryModes)) {
            GetDeliveryModes deliveryModes = (GetDeliveryModes) msg.obj;
            List<DeliveryModes> deliveryModeList = deliveryModes.getDeliveryModes();
            if (deliveryModeList.size() > 0) {
                CartModelContainer.getInstance().setDeliveryModes(deliveryModeList);
                addressController.setDeliveryMode(deliveryModeList.get(0).getCode());
            }
        }
    }
}
