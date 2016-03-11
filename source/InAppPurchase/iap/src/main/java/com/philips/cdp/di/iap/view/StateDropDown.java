package com.philips.cdp.di.iap.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;


public class StateDropDown {
    public interface StateListener {
        void onStateSelect(String state);
    }

    UIKitListPopupWindow mPopUp;
    StateListener mStateListener;

    public StateDropDown(Context context, View anchor, StateListener stateListener) {
        mStateListener = stateListener;
        createPopUp(anchor, context);
    }

    private void createPopUp(final View anchor, final Context context) {
        List<RowItem> rowItems = createRowItems();
        mPopUp = new UIKitListPopupWindow(context, anchor,
                UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);
        mPopUp.setModal(true);
        mPopUp.setOnItemClickListener(mListener);
    }

    private List<RowItem> createRowItems() {
        List<RowItem> rowItems = new ArrayList<>();
        String[] desc = {"Karnataka", "Tamilnadu"};
        rowItems.add(new RowItem(desc[0]));
        rowItems.add(new RowItem(desc[1]));
        return rowItems;
    }

    AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            String state = ((RowItem) parent.getItemAtPosition(position)).getDesc();
            mStateListener.onStateSelect(state);
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

    public boolean isShowing() {
        return mPopUp.isShowing();
    }

}
