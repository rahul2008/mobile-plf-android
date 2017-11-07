/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.ths.R;
import com.philips.platform.uid.view.widget.Label;


public class THSExistingMedicationListAdapter extends BaseAdapter {
    private THSMedication mPTHExistingMedication;
    private Context mContext;


    public THSExistingMedicationListAdapter(Context context) {
        this.mContext = context;

    }

    void setData(THSMedication pTHMedication) {
        this.mPTHExistingMedication = pTHMedication;
        notifyDataSetChanged();
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if (null != mPTHExistingMedication && null != mPTHExistingMedication.getMedicationList() && !mPTHExistingMedication.getMedicationList().isEmpty()) {
            return mPTHExistingMedication.getMedicationList().size();
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
        if (null != mPTHExistingMedication && null != mPTHExistingMedication.getMedicationList() && !mPTHExistingMedication.getMedicationList().isEmpty()) {
            return mPTHExistingMedication.getMedicationList().get(position);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (null == rowView) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.ths_existing_medication_list_row, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.MedicineName = (TextView) rowView.findViewById(R.id.pth_existing_medication_list_row_label);
            viewHolder.DeleteButton = (Label) rowView.findViewById(R.id.pth_existing_medication_list_row_delete);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        String medicineName = mPTHExistingMedication.getMedicationList().get(position).getName();
        holder.MedicineName.setText(medicineName);
        holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete selected medicine
                mPTHExistingMedication.getMedicationList().remove(position);
                notifyDataSetChanged();
            }
        });


        return rowView;
    }

    private class ViewHolder {
        private TextView MedicineName;
        private Label DeleteButton;

    }
}
