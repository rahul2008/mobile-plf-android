/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.cdp.prxsample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.philips.cdp.prxclient.PRXDependencies;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.assets.Asset;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.datamodels.assets.Assets;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.datamodels.support.RichText;
import com.philips.cdp.prxclient.datamodels.support.RichTexts;
import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.request.ProductSupportRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.List;

public class PrxLauncherActivity extends AppCompatActivity {

    private static final String TAG = PrxLauncherActivity.class.getSimpleName();

    private String selectedCtn, selectedCountry;

    private final String mRequestTag = null;
    private Spinner mSector_spinner_prx;
    private Spinner mSector_catalog_prx;
    private Spinner spinner_ctn;
    private Spinner spinner_country;
    private String mSector[], mCatalog[], mCtn[], mCountry[];
    private PrxConstants.Sector selectedSector;
    private PrxConstants.Catalog selectedCatalog;

    private PRXDependencies prxDependencies;
    private AppInfraInterface mAppInfra;
    private ListView listview;
    private ImageView imageView;
    private TextView descTextView;
    private TextView priceTextView;
    private TextView productTitleText;
    private TextView subtitelText;
    private RequestManager mRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Button mSummaryButton = (Button) findViewById(R.id.summary_reqst_button);
        Button msupportButton = (Button) findViewById(R.id.support_rqst_button);
        Button mAssetButton = (Button) findViewById(R.id.assets_reqst_button);
        TextView appversion = (TextView) findViewById(R.id.appversion);
        mRequestManager = new RequestManager();
        appversion.setText(mRequestManager.getLibVersion());

        imageView = (ImageView) findViewById(R.id.imageView);

        descTextView = (TextView) findViewById(R.id.descText);
        priceTextView = (TextView) findViewById(R.id.priceText);
        productTitleText = (TextView) findViewById(R.id.productText);
        subtitelText = (TextView) findViewById(R.id.subtitleText);


        listview = (ListView) findViewById(R.id.details);

        mAppInfra = new AppInfra.Builder().build(this);
        prxDependencies = new PRXDependencies(getApplicationContext(), mAppInfra,"PRX DEMO");
        prxDependencies.mAppInfraLogging = mAppInfra.getLogging().createInstanceForComponent(String.format("%s/prx", prxDependencies.getParentTLA()), mRequestManager.getLibVersion());

        // setting sector spinner
        mSector_spinner_prx = (Spinner) findViewById(R.id.prxSpinnerSector);
        mSector = getResources().getStringArray(R.array.sector_list);
        spinner_ctn = (Spinner) findViewById(R.id.prxSpinnerCTN);
        mCtn = getResources().getStringArray(R.array.ctn_list);

        spinner_country = (Spinner) findViewById(R.id.prxSpinnerCountry);
        mCountry = getResources().getStringArray(R.array.country_code);

        ArrayAdapter<String> mSector_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mSector);
        mSector_spinner_prx.setAdapter(mSector_adapter);
        mSector_spinner_prx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSector = PrxConstants.Sector.valueOf(parent.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> ctn_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                mCtn);
        spinner_ctn.setAdapter(ctn_adapter);

        spinner_ctn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCtn = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> country_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                mCountry);
        spinner_country.setAdapter(country_adapter);

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = parent.getAdapter().getItem(position).toString();
                mAppInfra.getServiceDiscovery().setHomeCountry(selectedCountry.toUpperCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // setting catalog spinner
        mSector_catalog_prx = (Spinner) findViewById(R.id.prxSpinnerCatalog);
        mCatalog = getResources().getStringArray(R.array.catalog_list);
        ArrayAdapter<String> mCatalogy_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCatalog);
        mSector_catalog_prx.setAdapter(mCatalogy_adapter);
        mSector_catalog_prx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCatalog = PrxConstants.Catalog.valueOf(parent.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSummaryRequest();
            }
        });
        msupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSupportRequest();
            }
        });
        mAssetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAssetRequest();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void productAssetRequest() {
        ProductAssetRequest mProductAssetBuilder = new ProductAssetRequest(selectedCtn, selectedSector, selectedCatalog, mRequestTag);
        //  mProductAssetBuilder.setSector(selectedSector);
        //mProductAssetBuilder.setCatalog(selectedCatalog);
        onRequestManagerCalled(mProductAssetBuilder);
    }

    private void productSupportRequest() {
        ProductSupportRequest mProductSupportBuilder = new ProductSupportRequest(selectedCtn, selectedSector, selectedCatalog, mRequestTag);
        // mProductSupportBuilder.setSector(selectedSector);
        // mProductSupportBuilder.setCatalog(selectedCatalog);
        onRequestManagerCalled(mProductSupportBuilder);
    }

    private void productSummaryRequest() {
        ProductSummaryRequest mProductSummeryBuilder = new ProductSummaryRequest(selectedCtn, selectedSector, selectedCatalog, mRequestTag);
        //  mProductSummeryBuilder.setSector(selectedSector);
        //mProductSummeryBuilder.setCatalog(selectedCatalog);
        onRequestManagerCalled(mProductSummeryBuilder);
    }

    private void onRequestManagerCalled(PrxRequest prxRequest) {
        mRequestManager.init(prxDependencies);
        mRequestManager.executeRequest(prxRequest, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                String str = responseData.getClass().toString();
                if (responseData instanceof SummaryModel) {
                    SummaryModel mSummaryModel = (SummaryModel) responseData;
                    //aiLogging.log(AppInfraLogging.LogLevel.DEBUG,TAG,"Support Response Data AI : " + mSummaryModel.isSuccess());
                    com.philips.cdp.prxclient.datamodels.summary.Data mData = mSummaryModel.getData();
                    String url = mData.getImageURL();
                    imageView.setVisibility(View.VISIBLE);
                    descTextView.setVisibility(View.VISIBLE);
                    priceTextView.setVisibility(View.VISIBLE);
                    productTitleText.setVisibility(View.VISIBLE);
                    subtitelText.setVisibility(View.VISIBLE);

                    listview.setVisibility(View.GONE);
                    Glide.with(PrxLauncherActivity.this).load(url)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);

                    if (mData.getWow() != null) descTextView.setText("Desc" + " " + mData.getWow());
                    if (mData.getPrice() != null && mData.getPrice().getFormattedDisplayPrice() != null)
                        priceTextView.setText("Price" + " " + mData.getPrice().getFormattedDisplayPrice());
                    if (mData.getProductTitle() != null)
                        productTitleText.setText("ProdcutTitle" + " " + mData.getProductTitle());
                    if (mData.getSubWOW() != null)
                        subtitelText.setText("SubTitle" + "  " + mData.getSubWOW());


                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " SummaryModel Positive Response Data :"+ mSummaryModel.isSuccess());
                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " SummaryModel Positive Response Data Brand: " + mData.getBrand());
                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " SummaryModel Positive Response Data CTN: " + mData.getCtn());
                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " SummaryModel Positive Response Data Product Title: " + mData.getProductTitle());


                } else if (responseData instanceof AssetModel) {
                    AssetModel mAssetModel = (AssetModel) responseData;
                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " Support Response Data : " + mAssetModel.isSuccess());
                    com.philips.cdp.prxclient.datamodels.assets.Data myyData = mAssetModel.getData();
                    if (myyData != null) {
                        Assets assets = myyData.getAssets();
                        if (assets != null) {
                            List<Asset> asset = assets.getAsset();
                            imageView.setVisibility(View.GONE);
                            descTextView.setVisibility(View.GONE);
                            priceTextView.setVisibility(View.GONE);
                            productTitleText.setVisibility(View.GONE);
                            subtitelText.setVisibility(View.GONE);
                            listview.setVisibility(View.VISIBLE);
                            AssetModelAdapter adapter = new AssetModelAdapter(PrxLauncherActivity.this, asset);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listview.setOnItemClickListener(null);
                        } else {
                            descTextView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            priceTextView.setVisibility(View.GONE);
                            productTitleText.setVisibility(View.GONE);
                            subtitelText.setVisibility(View.GONE);
                            listview.setVisibility(View.GONE);
                            descTextView.setText("Asset is null");
                        }
                        prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " AssetModel Positive Response Data assets : " + myyData.getAssets());
                    }
                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " AssetModel Positive Response Data : " + mAssetModel.isSuccess());


                } else {
                    SupportModel mSupportModel = (SupportModel) responseData;
                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " Support Response Data : " + mSupportModel.isSuccess());
                    com.philips.cdp.prxclient.datamodels.support.Data msupportData = mSupportModel.getData();
                    if (msupportData != null) {
                        RichTexts text = msupportData.getRichTexts();
                        if (text != null && text.getRichText() != null) {
                            List<RichText> listText = text.getRichText();
                            imageView.setVisibility(View.GONE);
                            descTextView.setVisibility(View.GONE);
                            priceTextView.setVisibility(View.GONE);
                            productTitleText.setVisibility(View.GONE);
                            subtitelText.setVisibility(View.GONE);

                            listview.setVisibility(View.VISIBLE);
                            SupportModelAdapter adapter = new SupportModelAdapter(PrxLauncherActivity.this, listText);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listview.setOnItemClickListener(null);
                            prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " SupportModel Positive Response Data RichText: " + msupportData.getRichTexts());
                        } else {
                            descTextView.setVisibility(View.VISIBLE);
                            priceTextView.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                            productTitleText.setVisibility(View.GONE);
                            subtitelText.setVisibility(View.GONE);
                            listview.setVisibility(View.GONE);
                            descTextView.setText("Support data is null");
                        }
                    }

                    prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " SupportModel Positive Response Data : " + mSupportModel.isSuccess());
                }
            }

            @Override
            public void onResponseError(PrxError prxError) {
                prxDependencies.mAppInfraLogging.log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, " Response Error Message PRX: " + prxError.getDescription());
                descTextView.setVisibility(View.VISIBLE);
                priceTextView.setVisibility(View.GONE);
                productTitleText.setVisibility(View.GONE);
                subtitelText.setVisibility(View.GONE);

                imageView.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                descTextView.setText(prxError.getDescription() + " " + "Code" + " " + prxError.getStatusCode());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
