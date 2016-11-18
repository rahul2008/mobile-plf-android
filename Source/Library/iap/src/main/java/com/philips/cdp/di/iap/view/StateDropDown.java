package com.philips.cdp.di.iap.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;

public class StateDropDown {

    public interface StateListener {
        void onStateSelect(String state);
        void stateRegionCode(String regionCode);
    }

    UIKitListPopupWindow mPopUp;
    StateListener mStateListener;
    Context mContext;
    RegionsList mRegionList;

    public StateDropDown(Context context, View anchor, StateListener stateListener) {
        mStateListener = stateListener;
        mContext = context;
        createPopUp(anchor, context);
    }

    private void createPopUp(final View anchor, final Context context) {
        List<RowItem> rowItems = createRowItems();
        mPopUp = new UIKitListPopupWindow(context, anchor,
                UIKitListPopupWindow.UIKIT_Type.UIKIT_TOPLEFT, rowItems);
        mPopUp.setModal(true);
        mPopUp.setOnItemClickListener(mListener);
    }

    private List<RowItem> createRowItems() {
        List<RowItem> rowItems = new ArrayList<>();
        mRegionList = CartModelContainer.getInstance().getRegionList();

        if(mRegionList != null) {
            for (int i = 0; i < mRegionList.getRegions().size(); i++) {
                rowItems.add(new RowItem(mRegionList.getRegions().get(i).getName()));
            }
        }
        return rowItems;
    }

    AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            String state = ((RowItem) parent.getItemAtPosition(position)).getDesc();
            mStateListener.onStateSelect(state);
            mStateListener.stateRegionCode(mRegionList.getRegions().get(position).getIsocode());
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
