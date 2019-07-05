/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.UIPickerAdapter;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.UIPicker;

public class SalutationDropDown {

    public interface SalutationListener {
        void onSalutationSelect(View view, String salutation);
    }

    private UIPicker mPopUp;
    private View mAnchor;
    private SalutationListener mSalutationListener;

    public SalutationDropDown(Context context, View anchor, SalutationListener salutationListener) {
        mSalutationListener = salutationListener;
        mAnchor = anchor;
        createPopUp(anchor, context);
    }

    private void createPopUp(final View anchor, final Context context) {
        //   List<String> rowItems = createRowItems(context);
        Context popupThemedContext = UIDHelper.getPopupThemedContext(context);
        mPopUp = new UIPicker(popupThemedContext);
        mPopUp.setAdapter(new UIPickerAdapter(popupThemedContext, R.layout.iap_uipicker_item_text, createRowItems(context)));
        mPopUp.setAnchorView(anchor);
//        mPopUp.setHorizontalOffset(-18);
//        mPopUp.setWidth((int) context.getResources().getDimension(R.dimen
//                .iap_count_drop_down_popup_width));
        mPopUp.setModal(true);
        mPopUp.setOnItemClickListener(mListener);
    }

    //    private List<String> createRowItems(Context context) {
//        List<String> rowItems = new ArrayList<>();
//        String mr = context.getResources().getString(R.string.iap_mr);
//        String ms = context.getResources().getString(R.string.iap_mrs);
//        rowItems.add(mr + ".");
//        rowItems.add(ms + ".");
//        return rowItems;
//    }
    private String[] createRowItems(Context context) {

        String mr = context.getResources().getString(R.string.iap_mr);
        String ms = context.getResources().getString(R.string.iap_mrs);
        String[] rowItems = new String[2];
        rowItems[0] = mr;
        rowItems[1] = ms;

        return rowItems;
    }

    private AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            String salutation = (String) parent.getItemAtPosition(position);
            mSalutationListener.onSalutationSelect(mAnchor, salutation);
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