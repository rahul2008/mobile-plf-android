/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;

public class SalutationDropDown {

    public interface SalutationListener {
        void onSalutationSelect(String salutation);
    }

    private UIKitListPopupWindow mPopUp;
    private SalutationListener mSalutationListener;

    public SalutationDropDown(Context context, View anchor, SalutationListener salutationListener) {
        mSalutationListener = salutationListener;
        createPopUp(anchor, context);
    }

    private void createPopUp(final View anchor, final Context context) {
        List<RowItem> rowItems = createRowItems(context);
        mPopUp = new UIKitListPopupWindow(context, anchor,
                UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);
        mPopUp.setModal(true);
        mPopUp.setOnItemClickListener(mListener);
    }

    private List<RowItem> createRowItems(Context context) {
        List<RowItem> rowItems = new ArrayList<>();
        String mr = context.getResources().getString(R.string.iap_mr);
        String ms = context.getResources().getString(R.string.iap_mrs);
        String[] desc = {mr, ms};
        rowItems.add(new RowItem(desc[0]));
        rowItems.add(new RowItem(desc[1]));
        return rowItems;
    }

    AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            String salutation = ((RowItem) parent.getItemAtPosition(position)).getDesc();
            mSalutationListener.onSalutationSelect(salutation);
            dismiss();
        }
    };


    public void show() {
        if (!isShowing()) {
            mPopUp.show();
        }
    }

    public void dismiss() {
        mPopUp.dismiss();
    }

    private boolean isShowing() {
        return mPopUp.isShowing();
    }

}