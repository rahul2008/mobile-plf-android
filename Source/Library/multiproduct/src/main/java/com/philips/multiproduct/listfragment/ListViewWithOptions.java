/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.multiproduct.listfragment;

import android.app.Activity;
import android.content.res.Resources;
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
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.multiproduct.R;
import com.philips.multiproduct.prx.VolleyWrapper;
import com.philips.multiproduct.utils.ProductSelectionLogger;

import java.util.ArrayList;
import java.util.List;


public class ListViewWithOptions extends BaseAdapter {

    private static final String TAG = ListViewWithOptions.class.getSimpleName();
    private LayoutInflater inflater = null;
    private ArrayList<String> listOfItems = null;
    private List<SummaryModel> mProductsList = null;
    private Activity mActivity = null;

    public ListViewWithOptions(Activity activity, List<SummaryModel> data) {
        if (activity != null)
            inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        this.mActivity = activity;
        mProductsList = data;
    }

    @Override
    public int getCount() {

        if (mProductsList.size() > 10)
            return 10;
        else
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
            vi = inflater.inflate(R.layout.uikit_listview_with_options_multiproduct, null);

        SummaryModel summaryModel = mProductsList.get(position);
        ;
        Data data = summaryModel.getData();
        final ImageView image = (ImageView) vi.findViewById(R.id.image);
        TextView name = (TextView) vi.findViewById(R.id.text1Name);
        TextView value = (TextView) vi.findViewById(R.id.text2value);
        TextView from = (TextView) vi.findViewById(R.id.from);

        String imagepath = data.getImageURL();
        int imageWidth = (int) (85 * Resources.getSystem().getDisplayMetrics().density);
        imagepath = imagepath + "?wid=" + imageWidth + "&;";

        ProductSelectionLogger.v(TAG, "Image : " + imagepath);

        ImageRequest request = new ImageRequest(imagepath,
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
        VolleyWrapper.getInstance(mActivity).addToRequestQueue(request);
        name.setText(data.getProductTitle());
        value.setText(data.getCtn());
       /* from.setText("from");*/
        vi.setTag(position);
        return vi;
    }
}
