package com.philips.multiproduct.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.multiproduct.R;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;
import com.philips.multiproduct.productscreen.DetailedScreenFragment;

/**
 * ProductListingFragment class is used to showcase all possible CTNs and its details.
 *
 * @author : ritesh.jha@philips.com
 * @since : 29 Jan 2016
 */
public class ProductListingFragment extends MultiProductBaseFragment {

    private String TAG = ProductListingFragment.class.getSimpleName();
    private ListView mProductListView = null;
    private ListViewWithOptions mProductAdapter = null;
//    private ImageView mImageView = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_listview, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProductListView = (ListView) getActivity().findViewById(R.id.productListView);
        mProductAdapter = new ListViewWithOptions(getActivity());
        mProductListView.setAdapter(mProductAdapter);

        mProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                showFragment(new DetailedScreenFragment());
            }
        });
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.abc_search_hint);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
