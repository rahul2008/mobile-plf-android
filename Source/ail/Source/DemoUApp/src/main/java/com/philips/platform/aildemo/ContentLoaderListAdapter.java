package com.philips.platform.aildemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310238114 on 11/16/2016.
 */
public class ContentLoaderListAdapter extends BaseAdapter {
List<ContentLoader> mContentLoaderList;
    Context mContext;
    public ContentLoaderListAdapter(Context pContext, List<ContentLoader> pContentLoaderList){
        mContext=pContext;
        mContentLoaderList=pContentLoaderList;
    }

    void setData(ArrayList<ContentLoader> pContentLoaderList){
        mContentLoaderList=pContentLoaderList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(null!=mContentLoaderList&&mContentLoaderList.size()>0){
            return mContentLoaderList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(null!=mContentLoaderList&&mContentLoaderList.size()>0){
            return mContentLoaderList.get(i);
        }else {
            return null;
        }

    }

    @Override
    public long getItemId(int i) {
        if(null!=mContentLoaderList&&mContentLoaderList.size()>0){
            return mContentLoaderList.get(i).hashCode();
        }else {
            return -1;
        }

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.content_loader_row, viewGroup, false);

        TextView serviceID = (TextView) rowView.findViewById(R.id.contentLoaderServiceID);
        serviceID.setText("Service ID :\n"+mContentLoaderList.get(i).getmServiceId());

        return rowView;
    }


}
