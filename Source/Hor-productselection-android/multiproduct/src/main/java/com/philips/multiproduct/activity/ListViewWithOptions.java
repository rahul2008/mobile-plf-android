/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.multiproduct.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.philips.multiproduct.R;
import com.philips.multiproduct.prx.MySingleton;
import com.philips.multiproduct.prx.ProductData;
import com.philips.multiproduct.utils.MLogger;

import java.util.ArrayList;
import java.util.List;


public class ListViewWithOptions extends BaseAdapter {

    private static final String TAG = ListViewWithOptions.class.getSimpleName();
    private LayoutInflater inflater = null;
    private ArrayList<String> listOfItems = null;
    private List<ProductData> mProductsList = null;
    private Activity mActivity = null;

    public ListViewWithOptions(Activity activity, List<ProductData> data) {
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        this.mActivity = activity;
        mProductsList = data;
        MLogger.v(TAG, "Product data loaded from CTN are : " + mProductsList.size());
    }

    @Override
    public int getCount() {
        return mProductsList.size();
    }

    @Override
    public Object getItem(final int position) {
        return position;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi = convertView;


        if (convertView == null)
            vi = inflater.inflate(R.layout.uikit_listview_with_options_custom_layout, null);

        ProductData data = mProductsList.get(position);
        final ImageView image = (ImageView) vi.findViewById(R.id.image);
        TextView name = (TextView) vi.findViewById(R.id.text1Name);
        TextView value = (TextView) vi.findViewById(R.id.text2value);
        TextView from = (TextView) vi.findViewById(R.id.from);

        ImageRequest request = new ImageRequest(data.getImage(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MySingleton.getInstance(mActivity).addToRequestQueue(request);
        name.setText(data.getProductName());
        value.setText(data.getProductVariant());
        from.setText("from");
        vi.setTag(position);
        return vi;
    }
}
