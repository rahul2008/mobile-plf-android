package com.philips.cdp.productselection.fragments.listfragment;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.detailedscreen.DetailedScreenFragmentSelection;
import com.philips.cdp.productselection.fragments.homefragment.ProductSelectionBaseFragment;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ProductSelectionListingFragment class is used to showcase all possible CTNs and its details.
 *
 * @author : ritesh.jha@philips.com
 * @since : 29 Jan 2016
 */
public class ProductSelectionListingFragment extends ProductSelectionBaseFragment {

    private String TAG = ProductSelectionListingFragment.class.getSimpleName();
    private ListView mProductListView = null;
    private ListViewWithOptions mProductAdapter = null;
    private ProgressDialog mSummaryDialog = null;
    private ArrayList<SummaryModel> productList = null;

    public ProductSelectionListingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_listview, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()==null ) {return;}

        ProductSelectionLogger.i(TAG, "Displaying the list of products for user to select their product");
        mProductListView = (ListView) getView().findViewById(R.id.productListView);

        injectSummaryDataList();
        mProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (isConnectionAvailable()) {
                    mUserSelectedProduct = (productList.get(position));
                    DetailedScreenFragmentSelection detailedScreenFragmentSelection = new DetailedScreenFragmentSelection();
                    detailedScreenFragmentSelection.setUserSelectedProduct(mUserSelectedProduct);
                    showFragment(detailedScreenFragmentSelection);


                    Map<String, String> contextData = new HashMap<String, String>();
                    contextData.put(Constants.ACTION_NAME_SPECIAL_EVENT,
                            Constants.ACTION_VALUE_PRODUCT_VIEW);
                    contextData.put(Constants.ACTION_NAME_PRODUCTS, mUserSelectedProduct.getData().getProductTitle()
                            + ":" + mUserSelectedProduct.getData().getCtn());

                    ProductModelSelectionHelper.getInstance().getTaggingInterface().
                            trackActionWithInfo(Constants.ACTION_KEY_SEND_DATA, contextData);
                }
            }
        });

        if (getPreviousName() != null) {
            ProductModelSelectionHelper.getInstance().getTaggingInterface().trackPageWithInfo
                    (Constants.PAGE_LIST_SCREEN, getPreviousName(), getPreviousName());
            setPreviousPageName(Constants.PAGE_LIST_SCREEN);
        } else setPreviousPageName(Constants.PAGE_LIST_SCREEN);
    }


    private void injectSummaryDataList() {
        SummaryModel[] summaryList = ProductModelSelectionHelper.getInstance().getProductModelSelectionType().getSummaryModelList();
        if(summaryList!=null){
            ProductSelectionLogger.i(TAG, "Found " + summaryList.length + " products in region " + ProductModelSelectionHelper.getInstance().getLocale().toString());
            productList = new ArrayList<SummaryModel>();

            for (int i = 0; i < summaryList.length; i++) {
                productList.add(summaryList[i]);
            }
            if (productList.size() != 0) {
                mProductAdapter = new ListViewWithOptions(getActivity(), productList);
                mProductListView.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
            } else
                ProductModelSelectionHelper.getInstance().getProductSelectionListener().onProductModelSelected(mUserSelectedProduct);

        }
    }

    @Override
    public void onDestroyView() {
        if (mSummaryDialog != null) {

            if (!(getActivity().isFinishing()) && mSummaryDialog.isShowing()) {
                mSummaryDialog.dismiss();
                mSummaryDialog.cancel();
            }
        }
        super.onDestroyView();

    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.Product_Title);
    }


    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
