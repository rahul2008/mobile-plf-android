/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.multiproduct.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.multiproduct.R;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

/**
 * <b></b> ListWithOptions is class to demonstrate the use of uikit_listview_with_options_custom_layout with an adapter </b>
 * <p/>
 * <b></b> We have 2 types Of Such Lists.One with Header and one without Header</b></br>
 * <pre>
 * ImageView image = (ImageView) vi.findViewById(R.id.image);
 * TextView name = (TextView) vi.findViewById(R.id.text1Name);
 * TextView value = (TextView) vi.findViewById(R.id.text2value);
 * TextView from = (TextView) vi.findViewById(R.id.from);
 *
 *        </pre>
 */
public class ListViewWithOptions extends BaseAdapter {

    private LayoutInflater inflater = null;
    private ArrayList<String> listOfItems = null;
    private ArrayList<Integer> images = new ArrayList<>(9);
    private Context mContext = null;
    private  FontIconTextView mArrowRight = null;

    public ListViewWithOptions(Activity activity) {
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

        mContext = activity;

        images.add(R.drawable.image);
        images.add(R.drawable.toothbrush);
        images.add(R.drawable.image);
        images.add(R.drawable.toothbrush);
        images.add(R.drawable.image);
        images.add(R.drawable.toothbrush);
        images.add(R.drawable.image);
        images.add(R.drawable.toothbrush);
        images.add(R.drawable.toothbrush);

        listOfItems = new ArrayList<>(9);
        listOfItems.add("DiamondClean");
        listOfItems.add("Sonicare for Kids");
        listOfItems.add("HealthyWhite");
        listOfItems.add("Sonicare FlexCare+");
        listOfItems.add("Sonicare FlexCare Platinum");
        listOfItems.add("DiamondClean");
        listOfItems.add("Sonicare for Kids");
        listOfItems.add("HealthyWhite");
        listOfItems.add("Sonicare FlexCare+");
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(final int position) {
        return position;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    private RelativeLayout mRelativeLayout = null;

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi = convertView;


        if (convertView == null) {
            vi = inflater.inflate(R.layout.uikit_listview_with_options_multiproduct, null);
        }

        ImageView image = (ImageView) vi.findViewById(R.id.image);
        TextView name = (TextView) vi.findViewById(R.id.text1Name);
        TextView value = (TextView) vi.findViewById(R.id.text2value);
        TextView from = (TextView) vi.findViewById(R.id.from);
        image.setImageResource(images.get(position));
        //image.setColorFilter(Color.GREEN);
        name.setText(listOfItems.get(position));
        value.setText("€ 209,99*");
        from.setText("from");
        vi.setTag(position);

        return vi;
    }
}
