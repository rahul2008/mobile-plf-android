/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;


import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.adapters.UIPickerAdapter;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.UIPicker;

public class CountDropDown implements AdapterView.OnItemClickListener {
    private UIPicker mPopUp;
    private int mStart;
    private int mEnd;
    private int mCurrent;
    private int mCurrentViewIndex;
    private Context context;
    private View anchor;


    private CountUpdateListener mUpdateListener;

    public interface CountUpdateListener {
        void countUpdate(int oldCount, int newCount);
    }

    public CountDropDown(View anchor, Context context, int end, int currentCount, CountUpdateListener listener) {
        this(anchor, context, 1, end, currentCount, listener);
    }


    CountDropDown(View anchor, Context context, int start, int end, int currentCount, CountUpdateListener listener) {
        this.context = context;
        mUpdateListener = listener;
        mStart = start;
        mEnd = end;
        mCurrent = currentCount;
        mCurrentViewIndex = currentCount - mStart;
        this.anchor = anchor;
    }

    public void show() {
        mPopUp.show();
        mPopUp.getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mPopUp.setSelection(mCurrentViewIndex);
    }

    public UIPicker getPopUpWindow() {
        return mPopUp;
    }

    public void dismiss() {
        mPopUp.dismiss();
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        if (mUpdateListener != null) {
            int count = Integer.parseInt(((String) parent.getItemAtPosition(position)));
            mUpdateListener.countUpdate(mCurrent, count);
            mCurrent = count;
            mCurrentViewIndex = position;
        }
        mPopUp.dismiss();
    }

    public void createPopUp(View v, int quantity) {
        // List<RowItem> rowItems = getRowItems();
        Context popupThemedContext = UIDHelper.getPopupThemedContext(context);
        mPopUp = new UIPicker(popupThemedContext);

//        int offset = (int) context.getResources().getDimension(R.dimen.iap_count_drop_down_horizontal_offset);
//        mPopUp.setHorizontalOffset(offset);
//        mPopUp.setWidth((int) context.getResources().getDimension(R.dimen
//                .iap_count_drop_down_popup_width));
//        mPopUp.setHeight((int) context.getResources().getDimension(R.dimen
//                .iap_count_drop_down_popup_height));
        mPopUp.setAdapter(new UIPickerAdapter(popupThemedContext, R.layout.ecs_uipicker_item_text, createRowItems(quantity)));
        mPopUp.setAnchorView(v);
        mPopUp.setModal(true);
        mPopUp.setOnItemClickListener(this);
    }


    //Fill the data for the drop down.
    //Can we do it better? instead of running a loop...
//    private List<RowItem> getRowItems() {
//        int total = mEnd + 1; //handle for extra loop condition
//        List<RowItem> items = new ArrayList<RowItem>();
//        for (int i = mStart; i < total; i++) {
//            items.add(new RowItem(String.valueOf(i)));
//        }
//        return items;
//    }

    //    private String[] createRowItems(Context context) {
//        int total = mEnd + 1;
//        String[] items = new String[10];
//        return items;
//    }
    private String[] createRowItems(int items) {
        String[] rowItems = new String[items];
        for (int i = 0; i < items; i++) {
            rowItems[i] = String.valueOf(i + 1);
        }
        return rowItems;
    }

    //To highlight the selected index, we need custom adapter.
    //Must be removed if we don't support this feature
//    class CountAdapter extends ArrayAdapter<String> {
//        private LayoutInflater inflater;
//        private int resID;
//
//        public CountAdapter(final Context context, final int resource, final String[] objects) {
//            super(context, resource, objects);
//            resID = resource;
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            inflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(context));
//        }
//
//        @Override
//        public View getView(final int position, final View convertView, final ViewGroup parent) {
//            Label view;
//            if (convertView == null) {
//                view = (Label) inflater.inflate(resID, parent, false);
//            } else {
//                view = (Label) convertView;
//            }
//            view.setText(getItem(position));
////            if (position == mCurrentViewIndex) {
////                TextView countView = view.findViewById(R.id.item_text);
////                String count = getItem(position);
////                Spanned boldCount = Html.fromHtml("<b>" + count + "</b>");
////                countView.setText(boldCount);
////            }
//            return view;
//        }
//    }
}