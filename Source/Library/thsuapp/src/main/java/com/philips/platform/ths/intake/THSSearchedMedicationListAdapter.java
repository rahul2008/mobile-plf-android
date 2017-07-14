package com.philips.platform.ths.intake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.platform.ths.R;

/**
 * Created by philips on 6/30/17.
 */

public class THSSearchedMedicationListAdapter extends BaseAdapter {
    THSMedication mPTHSearchedMedication;
    Context mContext;

    public THSSearchedMedicationListAdapter(Context context, THSMedication pTHMedication) {
        this.mContext = context;
        this.mPTHSearchedMedication = pTHMedication;
    }

    void setData(THSMedication pTHSearchedMedication){
        mPTHSearchedMedication=pTHSearchedMedication;
        notifyDataSetChanged();
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if (null != mPTHSearchedMedication && null != mPTHSearchedMedication.getMedicationList() && !mPTHSearchedMedication.getMedicationList().isEmpty()) {
            return mPTHSearchedMedication.getMedicationList().size();
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
        if (null != mPTHSearchedMedication && null != mPTHSearchedMedication.getMedicationList() && !mPTHSearchedMedication.getMedicationList().isEmpty()) {
            return mPTHSearchedMedication.getMedicationList().get(position);
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

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.ths_list_row, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.MedicineName = (TextView) rowView.findViewById(R.id.ths_list_row_label);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        String medicineName = mPTHSearchedMedication.getMedicationList().get(position).getName();
        holder.MedicineName.setText(medicineName);

        return rowView;
    }


    private class ViewHolder {
        private TextView MedicineName;


    }
}
