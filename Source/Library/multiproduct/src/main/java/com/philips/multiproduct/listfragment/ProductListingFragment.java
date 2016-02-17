package com.philips.multiproduct.listfragment;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.multiproduct.ProductModelSelectionHelper;
import com.philips.multiproduct.R;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;
import com.philips.multiproduct.listeners.ProductListDetailsTabletListener;
import com.philips.multiproduct.productscreen.DetailedScreenFragment;
import com.philips.multiproduct.prx.PrxSummaryDataListener;
import com.philips.multiproduct.prx.PrxWrapper;
import com.philips.multiproduct.utils.ProductSelectionLogger;

import java.util.ArrayList;

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
    private ProgressDialog mSummaryDialog = null;
    private ArrayList<SummaryModel> productList = null;
    private DetailedScreenFragment mDetailedScreenFragment = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDetailedScreenFragment = new DetailedScreenFragment();
        if (isTablet()) {
            mProductDetailsListener = new ProductListDetailsTabletListener(getActivity());
            mProductDetailsListener.addObserver(mDetailedScreenFragment);
        }
        View view = inflater.inflate(R.layout.fragment_product_listview, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProductListView = (ListView) getActivity().findViewById(R.id.productListView);

        getSummaryDataFromPRX();

        if (isTablet() && isConnectionAvailable()) {
            try {
                ProductModelSelectionHelper.getInstance().setUserSelectedProduct(productList.get(0));
                mProductDetailsListener.notifyAllProductScreens();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        mProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (isConnectionAvailable()) {
                    ProductModelSelectionHelper.getInstance().setUserSelectedProduct(productList.get(position));
                    if (!isTablet()) {
                        showFragment(mDetailedScreenFragment);
                    } else {
                        mProductDetailsListener.notifyAllProductScreens();
                    }
                }
            }
        });

        setListViewHeightBasedOnChildren();
    }

    public void setListViewHeightBasedOnChildren() {
        mProductListView.post(new Runnable() {
            @Override
            public void run() {
                if (mProductAdapter == null) {
                    return;
                }
                int totalHeight = mProductListView.getPaddingTop() + mProductListView.getPaddingBottom();
                int listWidth = mProductListView.getMeasuredWidth();
                for (int i = 0; i < mProductAdapter.getCount(); i++) {
                    View listItem = mProductAdapter.getView(i, null, mProductListView);
                    listItem.measure(
                            View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = mProductListView.getLayoutParams();
                params.height = (totalHeight + (mProductListView.getDividerHeight() * (mProductAdapter.getCount() - 1)));
                mProductListView.setLayoutParams(params);
                mProductListView.requestLayout();
            }
        });
    }

    private void getSummaryDataFromPRX() {

        if (mSummaryDialog == null)
            mSummaryDialog = new ProgressDialog(getActivity(), R.style.loaderTheme);
        mSummaryDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Large);
        mSummaryDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loader));
        mSummaryDialog.setCancelable(true);
        if (!(getActivity().isFinishing()))
            mSummaryDialog.show();

        final String[] ctnList = ProductModelSelectionHelper.getInstance().getProductCtnList();

        productList = new ArrayList<SummaryModel>();

        for (int i = 0; i < ctnList.length; i++) {
            final String ctn = ctnList[i];
            PrxWrapper prxWrapperCode = new PrxWrapper(getActivity().getApplicationContext(), ctn,
                    ProductModelSelectionHelper.getInstance().getSectorCode(),
                    ProductModelSelectionHelper.getInstance().getLocale().toString(),
                    ProductModelSelectionHelper.getInstance().getCatalogCode());

            prxWrapperCode.requestPrxSummaryData(new PrxSummaryDataListener() {
                @Override
                public void onSuccess(SummaryModel summaryModel) {

                    productList.add(summaryModel);
                    String[] ctnList = ProductModelSelectionHelper.getInstance().getProductCtnList();
                    if (ctn == ctnList[ctnList.length - 1]) {

                        mProductAdapter = new ListViewWithOptions(getActivity(), productList);
                        mProductListView.setAdapter(mProductAdapter);

                        if (getActivity() != null)
                            if (!(getActivity().isFinishing()) && mSummaryDialog.isShowing()) {
                                mSummaryDialog.dismiss();
                                mSummaryDialog.cancel();
                            }

                    }
                }


                @Override
                public void onFail(String errorMessage) {
                    ProductSelectionLogger.e(TAG, " Error : " + errorMessage);
                    String[] ctnList = ProductModelSelectionHelper.getInstance().getProductCtnList();
                    if (ctn == ctnList[ctnList.length - 1]) {

                        mProductAdapter = new ListViewWithOptions(getActivity(), productList);
                        mProductListView.setAdapter(mProductAdapter);
                        if (getActivity() != null)
                            if (!(getActivity().isFinishing()) && mSummaryDialog.isShowing()) {
                                mSummaryDialog.dismiss();
                                mSummaryDialog.cancel();
                            }
                    }
                }
            }, TAG);
        }

    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.product);
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
        mProductDetailsListener.deleteObserver(mDetailedScreenFragment);
    }
}
