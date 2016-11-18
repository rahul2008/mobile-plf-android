/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.view;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.utils.PopupOverAdapter;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;

public class CountDropDown implements AdapterView.OnItemClickListener {
    private UIKitListPopupWindow mPopUp;
    private int mStart;
    private int mEnd;
    private int mCurrent;
    private int mCurrentViewIndex;

    private CountUpdateListener mUpdateListener;

    public interface CountUpdateListener {
        void countUpdate(int oldCount, int newCount);
    }

    public CountDropDown(View anchor, int end, int currentCount, CountUpdateListener listener) {
        this(anchor, 1, end, currentCount, listener);
    }

    public CountDropDown(View anchor, int start, int end, int currentCount, CountUpdateListener listener) {
        Context context = anchor.getContext();
        mUpdateListener = listener;
        mStart = start;
        mEnd = end;
        mCurrent = currentCount;
        mCurrentViewIndex = currentCount - mStart;
        createPopUp(anchor, context);
    }

    public void show() {
        mPopUp.show();
        mPopUp.getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mPopUp.setSelection(mCurrentViewIndex);
    }

    public UIKitListPopupWindow getPopUpWindow() {
        return mPopUp;
    }

    public void dismiss() {
        mPopUp.dismiss();
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        if (mUpdateListener != null) {
            int count = Integer.parseInt(((RowItem) parent.getItemAtPosition(position)).getDesc());
            mUpdateListener.countUpdate(mCurrent, count);
            mCurrent = count;
            mCurrentViewIndex = position;
        }
        mPopUp.dismiss();
    }

    private void createPopUp(final View anchor, final Context context) {
        List<RowItem> rowItems = getRowItems();
        mPopUp = new UIKitListPopupWindow(context, anchor,
                UIKitListPopupWindow.UIKIT_Type.UIKIT_TOPLEFT, rowItems);
        int offset = (int) context.getResources().getDimension(R.dimen.iap_count_drop_down_horizontal_offset);
        mPopUp.setHorizontalOffset(offset);
        mPopUp.setWidth((int) context.getResources().getDimension(R.dimen
                .iap_count_drop_down_popup_width));
        mPopUp.setHeight((int) context.getResources().getDimension(R.dimen
                .iap_count_drop_down_popup_height));
        mPopUp.setModal(true);
        mPopUp.setAdapter(new CountAdapter(context, R.layout.uikit_simple_list_image_text, rowItems));
        mPopUp.setOnItemClickListener(this);
    }

    //Fill the data for the drop down.
    //Can we do it better? instead of running a loop...
    private List<RowItem> getRowItems() {
        int total = mEnd + 1; //handle for extra loop condition
        List<RowItem> items = new ArrayList<RowItem>();
        for (int i = mStart; i < total; i++) {
            items.add(new RowItem(String.valueOf(i)));
        }
        return items;
    }

    //To highlight the selected index, we need custom adapter.
    //Must be removed if we don't support this feature
    private class CountAdapter extends PopupOverAdapter {
        public CountAdapter(final Context context, final int resource, final List objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            if (position == mCurrentViewIndex) {
                TextView countView = (TextView) view.findViewById(R.id.listtextview);
                String count = getItem(position).getDesc();
                Spanned boldCount = Html.fromHtml("<b>" + count + "</b>");
                countView.setText(boldCount);
            }
            return view;
        }
    }
}