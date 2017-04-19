package com.philips.platform.baseapp.screens.cocoversion;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.settingscreen.SettingListItem;
import com.philips.platform.baseapp.screens.settingscreen.SettingListItemType;
import com.philips.platform.baseapp.screens.settingscreen.SettingsAdapter;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

/**
 * Created by philips on 4/18/17.
 */

public class CocoVersionAdapter extends BaseAdapter {
    private Context activityContext;
    private LayoutInflater inflater = null;
    private ArrayList<CocoVersionItem> cocoVersionsItemList = null;
    private UIBasePresenter fragmentPresenter;
    private String viewHolderTag = null;
    public CocoVersionAdapter(Context context, ArrayList<CocoVersionItem> cocoItemList,
                              UIBasePresenter fragmentPresenter){
        activityContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cocoVersionsItemList = cocoItemList;
        this.fragmentPresenter = fragmentPresenter;
    }
    @Override
    public int getCount() {
        return cocoVersionsItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    public static class VerticalViewHolder {
        public TextView name , description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CocoVersionAdapter.VerticalViewHolder verticalViewHolder = null;
        if (convertView == null) {

                convertView = inflater.inflate(R.layout.uikit_listview_without_icons, parent, false);
                verticalViewHolder = new VerticalViewHolder();
            verticalViewHolder.name = (TextView) convertView.findViewById(R.id.ifo);

            verticalViewHolder.description = (TextView) convertView.findViewById(R.id.text_description_without_icons);
                convertView.setTag(verticalViewHolder);

        }
        verticalViewHolder = (VerticalViewHolder) convertView.getTag();
        verticalAppView(position, verticalViewHolder);
        return convertView;
    }

    protected void verticalAppView(int position, VerticalViewHolder viewHolder) {

        CocoVersionItemType type = cocoVersionsItemList.get(position).type;

        switch (type) {
            case HEADER:
                headerSection(position,viewHolder);
                break;
            case CONTENT:
                subSection(position,viewHolder);
                break;

        }
    }
    private void subSection(int position,VerticalViewHolder viewHolder) {
        if (null != viewHolder.name && null !=  viewHolder.description ) {
            viewHolder.name.setText(cocoVersionsItemList.get(position).title);
            viewHolder.description.setVisibility(View.GONE);
            viewHolderTag = String.valueOf(SettingListItemType.CONTENT);
        }
    }

    private void headerSection(int position,VerticalViewHolder viewHolder) {
        CharSequence titleText;
        titleText = cocoVersionsItemList.get(position).title;
        if (null != viewHolder.name && null != viewHolder.description ) {
            viewHolder.name.setText(titleText);

            viewHolder.description.setVisibility(View.GONE);

            viewHolderTag = String.valueOf(SettingListItemType.HEADER);
        }

    }
}
