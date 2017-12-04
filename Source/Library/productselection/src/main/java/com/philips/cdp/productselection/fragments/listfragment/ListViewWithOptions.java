/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.productselection.fragments.listfragment;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.prx.VolleyWrapper;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.List;


public class ListViewWithOptions extends BaseAdapter implements Filterable {

    private static final String TAG = ListViewWithOptions.class.getSimpleName();
    private LayoutInflater inflater = null;
    private ArrayList<String> listOfItems = null;
    private List<SummaryModel> mProductsList = null;
    private Activity mActivity = null;
    private CustomFilter mCustomFilter;
    private List<SummaryModel> mOriginalSet;
    private TextView productNameView;
    private TextView ctnView;
    //private ImageView imageView;
    private Activity activity;

    SummaryModel mData = null;

    public ListViewWithOptions(Activity activity, List<SummaryModel> data) {
        this.activity = activity;
        if (activity != null)
            inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        this.mActivity = activity;
        this.mProductsList = data;
        this.mOriginalSet = data;
    }

    @Override
    public int getCount() {
        return mProductsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProductsList.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi = convertView;
        if ( convertView == null) {
            vi = inflater.inflate(R.layout.fragment_listscreen_adaper_view, null);

            SummaryModel summaryModel = mProductsList.get(position);

            Data data = summaryModel.getData();
            final ImageView image = (ImageView) vi.findViewById(R.id.image);
            productNameView = (TextView) vi.findViewById(R.id.product_name_view);
            //imageView = (ImageView) vi.findViewById(R.id.image);
            ctnView = (TextView) vi.findViewById(R.id.ctn_view);

            String imagepath = data.getImageURL();
            int imageWidth = (int) (85 * Resources.getSystem().getDisplayMetrics().density);
            imagepath = /*imagepath + "?wid=" + imageWidth + "&;";*/
                    imagepath + "?wid=" + imageWidth +
                            "&hei=" + imageWidth +
                            "&fit=fit,1";

            ProductSelectionLogger.d(TAG, "Image URL's of the listed Products : " + imagepath);

            final  ImageRequest request = new ImageRequest(imagepath,
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

            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    VolleyWrapper.getInstance(mActivity).addToRequestQueue(request);
                }
            });
            productNameView.setText(data.getProductTitle());
            ctnView.setText(data.getCtn());
            vi.setTag(position);
            setData(summaryModel);
        }

        return vi;
    }


    public SummaryModel getData(){
        return mData;
    }

    private void setData(SummaryModel data){
        mData = data;
    }

    private class CustomFilter extends Filter {

        private String constraintStr;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraintStr = constraint.toString();
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<SummaryModel> filteredResultModelSet = new ArrayList<SummaryModel>();
                for (int i = 0; i < mOriginalSet.size(); i++) {
                    SummaryModel summaryModel = mOriginalSet.get(i);
                    Data data = summaryModel.getData();
                    SummaryModel filteredResultModel = new SummaryModel();
                    filteredResultModel.setData(data);
                    String productMachTitle = data.getProductTitle().toUpperCase();
                    if ((productMachTitle).contains(constraint.toString().toUpperCase())){
                        filteredResultModel.getData().setCtn(data.getCtn());
                        filteredResultModel.getData().setProductTitle(data.getProductTitle());
                        filteredResultModel.getData().setImageURL(data.getImageURL());
                        filteredResultModelSet.add(filteredResultModel);
                    }
                }
                filterResults.count = filteredResultModelSet.size();
                filterResults.values = filteredResultModelSet;
            } else {
                synchronized (this) {
                    filterResults.count = mOriginalSet.size();
                    filterResults.values = mOriginalSet;
                }
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results.count != 0){
                mProductsList = (ArrayList<SummaryModel>) results.values;
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        if (mCustomFilter == null) {
            mCustomFilter = new CustomFilter();
        }
        return mCustomFilter;
    }
}
