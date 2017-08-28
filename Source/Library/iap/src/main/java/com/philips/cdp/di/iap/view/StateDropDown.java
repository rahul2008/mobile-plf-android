package com.philips.cdp.di.iap.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.UIPickerAdapter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.UIPicker;

public class StateDropDown {

    public interface StateListener {
        void onStateSelect(String state);

        void stateRegionCode(String regionCode);
    }

    private UIPicker mPopUp;
    //    UIKitListPopupWindow mPopUp;
    private StateListener mStateListener;
    private Context mContext;
    private RegionsList mRegionList;

    public StateDropDown(Context context, View anchor, StateListener stateListener) {
        mStateListener = stateListener;
        mContext = context;
        createPopUp(anchor, context);
    }

    private void createPopUp(final View anchor, final Context context) {
        // List<String> rowItems = createRowItems();

//        mPopUp = new UIKitListPopupWindow(context, anchor,
//                UIKitListPopupWindow.UIKIT_Type.UIKIT_TOPLEFT, rowItems);
        Context popupThemedContext = UIDHelper.getPopupThemedContext(context);
        mPopUp = new UIPicker(popupThemedContext);
        //mPopUp.setAdapter(new UIPickerAdapter(popupThemedContext, R.layout.iap_uipicker_item_text, rowItems));
        ArrayAdapter adapter = new UIPickerAdapter(popupThemedContext, R.layout.iap_uipicker_item_text, createRowItems());
        mPopUp.setAdapter(adapter);
        mPopUp.setAnchorView(anchor);
        //mPopUp.setHorizontalOffset(-18);
//        mPopUp.setHeight((int) context.getResources().getDimension(R.dimen
//                .iap_count_drop_down_popup_height));
        // mPopUp.setHeight(500);
        mPopUp.setModal(true);
        mPopUp.setOnItemClickListener(mListener);
    }

    private String[] createRowItems() {
        // List<String> rowItems = new ArrayList<>();
        mRegionList = CartModelContainer.getInstance().getRegionList();
        String[] rowItems = new String[mRegionList.getRegions().size()];

        if (mRegionList != null) {
            for (int i = 0; i < mRegionList.getRegions().size(); i++) {
                //rowItems.add(mRegionList.getRegions().get(i).getName());
                rowItems[i] = mRegionList.getRegions().get(i).getName();
            }
        }
        return rowItems;
    }

    AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            String state = ((String) parent.getItemAtPosition(position));
            String isocode = mRegionList.getRegions().get(position).getIsocode();
            String stateCode = isocode.substring(isocode.length() - 2);
            mStateListener.stateRegionCode(isocode);
            mStateListener.onStateSelect(stateCode);
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

//    class StatePickerAdapter extends ArrayAdapter<String> {
//
//        private LayoutInflater inflater;
//        private int resID;
//
//        public StatePickerAdapter(Context context, int resource, String[] states) {
//            super(context, resource, states);
//            resID = resource;
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            inflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(context));
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            Label view;
//            if (convertView == null) {
//                view = (Label) inflater.inflate(resID, parent, false);
//            } else {
//                view = (Label) convertView;
//            }
//            view.setText(getItem(position));
//            return view;
//        }
//    }

}
