/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.platform.ths.R;

public class THSHealthPlanListAdapter extends BaseAdapter {

    private Context mContext;
    private THSHealthPlan mTHSHealthPlan;


    public THSHealthPlanListAdapter(Context context, THSHealthPlan tHSHealthPlan) {

        this.mContext = context;
        mTHSHealthPlan = tHSHealthPlan;

    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if (null != mTHSHealthPlan && null != mTHSHealthPlan.getHealthPlanList() && !mTHSHealthPlan.getHealthPlanList().isEmpty()) {
            return mTHSHealthPlan.getHealthPlanList().size();
        } else {
            return 0;
        }
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        if (null != mTHSHealthPlan && null != mTHSHealthPlan.getHealthPlanList() && !mTHSHealthPlan.getHealthPlanList().isEmpty()) {
            return mTHSHealthPlan.getHealthPlanList().get(position);
        } else {
            return null;
        }
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (null == rowView) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutInflater.inflate(R.layout.ths_list_row, null);
            ViewHolder holder = new ViewHolder();
            holder.HealthPlan = (TextView) rowView.findViewById(R.id.ths_list_row_label);
            rowView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String healthPlan = mTHSHealthPlan.getHealthPlanList().get(position).getName();
        holder.HealthPlan.setText(healthPlan);
        return rowView;
    }


    public class ViewHolder {
        private TextView HealthPlan;
    }

}
