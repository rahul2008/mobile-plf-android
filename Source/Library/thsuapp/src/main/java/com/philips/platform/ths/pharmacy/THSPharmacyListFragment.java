/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.pharmacy.PharmacyType;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.intake.THSSearchFragment;
import com.philips.platform.ths.pharmacy.customtoggle.SegmentControl;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSFileUtils;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;
import static com.philips.platform.ths.utility.THSConstants.PHARMACY_SEARCH_CONSTANT;
import static com.philips.platform.ths.utility.THSConstants.THS_PHARMACY_MAP;




public class THSPharmacyListFragment extends THSBaseFragment implements OnMapReadyCallback, View.OnClickListener,
        THSPharmacyListViewListener {

    public static String TAG = THSPharmacyListFragment.class.getSimpleName();
    private UIDNavigationIconToggler navIconToggler;
    private GoogleMap map;
    private ImageButton switchViewImageButton;
    protected SupportMapFragment mapFragment;
    private RecyclerView pharmacyListRecyclerView;
    private THSPharmacyListAdapter thsPharmacyListAdapter;
    protected THSPharmacyListPresenter thsPharmacyListPresenter;
    private LatLngBounds.Builder builder;
    private CameraUpdate cu;
    private Label selectedPharmacyName, selectedPharmacyAddressLineOne, selectedPharmacyAddressLineTwo,
            selectedPharmacyState, selectedPharmacyZip, selectedPharmacyPhone, selectedPharmacyEmail, pharmacy_segment_control_one, pharmacy_segment_control_two;
    private RelativeLayout selectedPharmacyLayout, pharmacy_list_fragment_container;
    private SegmentControl pharmacyTypeLayout;
    private Animation slideUpFromBottomAnimation, slideDownFromBottomAnimation, slideUpFromTopAnimation, slideDownFromTopAnimation;
    private boolean handleBack = false;
    private boolean isListSelected = false;
    private Button choosePharmacyButton;
    private Pharmacy pharmacy;
    protected THSConsumerWrapper thsConsumerWrapper;
    protected Address address;
    private Location location;
    private ActionBarListener actionBarListener;
    private List<Pharmacy> pharmaciesList = null;
    private boolean isSearched = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_pharmacy_list_fragment, container, false);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        setHasOptionsMenu(true);
        findViewByIDs(view);
        setOnClickListeners();
        findViewByIDs();
        setUpAnimations();
        actionBarListener = getActionBarListener();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(R.string.pharmacy_list_fragment_name, true);
        }
        if (null != location && !isSearched) {
            createCustomProgressBar(pharmacy_list_fragment_container, BIG);
            thsPharmacyListPresenter.fetchPharmacyList(thsConsumerWrapper, Double.valueOf(location.getLatitude()).floatValue(), Double.valueOf(location.getLongitude()).floatValue(), 5);
        }
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ths_provider_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ths_provider_search) {
            launchSearchFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchSearchFragment() {
        THSSearchFragment thsSearchFragment = new THSSearchFragment();
        thsSearchFragment.setFragmentLauncher(getFragmentLauncher());
        thsSearchFragment.setTargetFragment(this, THSConstants.PHARMACY_SEARCH_CONSTANT);
        thsSearchFragment.setActionBarListener(getActionBarListener());
        Bundle bundle = new Bundle();
        bundle.putInt(THSConstants.SEARCH_CONSTANT_STRING, THSConstants.PHARMACY_SEARCH_CONSTANT);
        addFragment(thsSearchFragment, THSSearchFragment.TAG, bundle, true);
    }

    public void setPharmaciesList(List<Pharmacy> pharmaciesList) {
        this.pharmaciesList = pharmaciesList;
    }


    public void setConsumerAndAddress(THSConsumerWrapper thsConsumerWrapper, Address address) {
        this.thsConsumerWrapper = thsConsumerWrapper;
        this.address = address;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void onStop() {
        super.onStop();
        navIconToggler.restoreNavigationIcon();
    }

    public void findViewByIDs() {
        switchViewImageButton.setImageDrawable(getResources().getDrawable(R.mipmap.list_icon, getActivity().getTheme()));
        pharmacyListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pharmacyListRecyclerView.setAdapter(thsPharmacyListAdapter);
        pharmacyListRecyclerView.setVisibility(View.GONE);
        pharmacy_segment_control_one.setSelected(true);
        thsPharmacyListPresenter = new THSPharmacyListPresenter(this);
        selectedPharmacyLayout.setVisibility(View.GONE);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        navIconToggler.restoreNavigationIcon();
    }

    public void setUpAnimations() {
        slideUpFromBottomAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_up_from_bottom);

        slideDownFromBottomAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_down_from_bottom);
        slideUpFromTopAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_up_from_top);

        slideDownFromTopAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_down_from_top);
    }

    public void setOnClickListeners() {
        switchViewImageButton.setOnClickListener(this);
        pharmacy_segment_control_one.setOnClickListener(this);
        pharmacy_segment_control_two.setOnClickListener(this);
        choosePharmacyButton.setOnClickListener(this);
    }

    public void findViewByIDs(View view) {
        pharmacy_list_fragment_container = (RelativeLayout) view.findViewById(R.id.pharmacy_list_fragment_container);
        pharmacy_segment_control_one = (Label) view.findViewById(R.id.segment_control_view_one);
        pharmacy_segment_control_two = (Label) view.findViewById(R.id.segment_control_view_two);
        choosePharmacyButton = (Button) view.findViewById(R.id.choose_pharmacy_button);
        selectedPharmacyLayout = (RelativeLayout) view.findViewById(R.id.selected_pharmacy_layout);
        pharmacyTypeLayout = (SegmentControl) view.findViewById(R.id.pharmacy_type_layout);
        selectedPharmacyName = (Label) view.findViewById(R.id.selected_pharmacy_name);
        selectedPharmacyAddressLineOne = (Label) view.findViewById(R.id.selected_pharmacy_address_line_one);
        selectedPharmacyAddressLineTwo = (Label) view.findViewById(R.id.selected_pharmacy_address_line_two);
        selectedPharmacyState = (Label) view.findViewById(R.id.selected_pharmacy_state);
        selectedPharmacyZip = (Label) view.findViewById(R.id.selected_pharmacy_zip_code);
        selectedPharmacyPhone = (Label) view.findViewById(R.id.selected_pharmacy_phone);
        selectedPharmacyEmail = (Label) view.findViewById(R.id.selected_pharmacy_email);
        switchViewImageButton = (ImageButton) view.findViewById(R.id.switch_view_layout);
        pharmacyListRecyclerView = (RecyclerView) view.findViewById(R.id.pharmacy_list_recyclerview);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentByTag(THSConstants.THS_MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.pharmacymap, mapFragment, THSConstants.THS_MAP_FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (null != pharmaciesList) {
            updatePharmacyListView(pharmaciesList);
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ths_pharmacy_search) {
            launchSearchFragment();
        } else {
            thsPharmacyListPresenter.onEvent(v.getId());
        }

    }


    /**
     * This method handles the "choose pharmacy" button. This sets the customers preferred pharmacy.
     */
    public void setPreferredPharmacy() {

        thsPharmacyListPresenter.updateConsumerPreferredPharmacy(pharmacy);
    }

    @Override
    public void showErrorToast(String errorMessage) {
        showToast(errorMessage);
    }

    /**
     * This method handles the filter for Mail order filtering only Mail order content.
     */
    public void showMailOrderView() {
        pharmacy_segment_control_one.setSelected(false);
        pharmacy_segment_control_two.setSelected(true);
        if (null != pharmaciesList) {
            updateView(filterList(pharmaciesList, PharmacyType.MailOrder));
        }
    }


    /**
     * This method handles the filter for Retail filtering only Retail content.
     */
    public void showRetailView() {
        pharmacy_segment_control_one.setSelected(true);
        pharmacy_segment_control_two.setSelected(false);
        if (null != pharmaciesList) {
            updateView(filterList(pharmaciesList, PharmacyType.Retail));
        }
    }


    /**
     * This method handles the switch between map view and the list view indicated by the map/list icon.
     */
    public void switchView() {
        switchView(false);
    }

    public void switchView(boolean isSearched) {

        if (isSearched && mapFragment.isHidden()) {
            hideMapFragment();
        } else if (isSearched && !mapFragment.isHidden()) {
            showMapFragment();
        } else if (!isSearched && mapFragment.isHidden()) {
            showMapFragment();
        } else if (!isSearched && !mapFragment.isHidden()) {
            hideMapFragment();
        }
    }

    public void showMapFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
        switchViewImageButton.setImageDrawable(getResources().getDrawable(R.mipmap.list_icon, getActivity().getTheme()));
        pharmacyListRecyclerView.setVisibility(View.INVISIBLE);
        isListSelected = false;
    }

    public void hideMapFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
        switchViewImageButton.setImageDrawable(getResources().getDrawable(R.mipmap.gps_icon, getActivity().getTheme()));
        pharmacyListRecyclerView.setVisibility(View.VISIBLE);
        isListSelected = true;
    }

    private List<Pharmacy> pharmacyRetailList, pharmacyMailOrderList;

    @Override
    public void updatePharmacyListView(List<Pharmacy> pharmacies) {
        pharmacyRetailList = filterList(pharmacies, PharmacyType.Retail);
        pharmacyMailOrderList = filterList(pharmacies, PharmacyType.MailOrder);
        if (pharmacyRetailList.size() == 0 && pharmacyMailOrderList.size() > 0) {
            showMailOrderView();
        } else if (pharmacyMailOrderList.size() == 0 && pharmacyRetailList.size() > 0) {
            showRetailView();
        } else if (pharmacyMailOrderList.size() > 0 && pharmacyRetailList.size() > 0) {
            showRetailView();
        }

        if (isSearched) {
            switchView(true);
        }
    }

    /**
     * This method updates the view based on the filter criteria. It will update the view to either Retail filter or Mail order filter
     *
     * @param pharmacies
     */
    public void updateView(final List<Pharmacy> pharmacies) {
        thsPharmacyListAdapter = new THSPharmacyListAdapter(pharmacies);
        thsPharmacyListAdapter.setOnPharmacyItemClickListener(new THSOnPharmacyListItemClickListener() {
            @Override
            public void onItemClick(Pharmacy pharmacy) {
                showSelectedPharmacyDetails(pharmacy);
                addMarkerOptions(pharmacies, pharmacy, true);
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude())));
            }
        });

        pharmacyListRecyclerView.setAdapter(thsPharmacyListAdapter);
        thsPharmacyListAdapter.notifyDataSetChanged();

        if (null != pharmacies && pharmacies.size() > 0) {
            setMarkerOnMap(pharmacies);
        }

    }

    /**
     * Form the list of pharmacies passed, it will filter the list to either mail order or retail.
     *
     * @param pharmacies
     * @param pharmacyType
     * @return
     */
    private List<Pharmacy> filterList(List<Pharmacy> pharmacies, PharmacyType pharmacyType) {
        Iterator<Pharmacy> pharmacyIterator = pharmacies.iterator();
        List<Pharmacy> list = new ArrayList<>();
        while (pharmacyIterator.hasNext()) {
            Pharmacy c = pharmacyIterator.next();
            if (c.getType() == pharmacyType) {
                list.add(c);
            }
        }
        return list;
    }

    /**
     * This method validates if the selected preferred pharmacy by the customer is mail order.
     * If so then Shipping address screen should be shown compulsarily
     */
    @Override
    public void validateForMailOrder(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        if (pharmacy.getType() == PharmacyType.MailOrder) {

            showShippingFragment();

        } else {

            launchInsuranceCostSummary();
        }

    }

    public void launchInsuranceCostSummary() {
        Consumer consumer = THSManager.getInstance().getPTHConsumer(getContext()).getConsumer();
        if (consumer.getSubscription() != null && consumer.getSubscription().getHealthPlan() != null) {
            final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
            addFragment(fragment, THSCostSummaryFragment.TAG, null, true);
        } else {
            final THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
            addFragment(fragment, THSInsuranceConfirmationFragment.TAG, null, true);
        }
    }

    public void showShippingFragment() {
        THSShippingAddressFragment thsShippingAddressFragment = new THSShippingAddressFragment();
        thsShippingAddressFragment.setConsumerAndAddress(thsConsumerWrapper, address);
        addFragment(thsShippingAddressFragment, THSShippingAddressFragment.TAG, null, true);
    }

    private void setMarkerOnMap(final List<Pharmacy> pharmacies) {
        updateCameraBounds(pharmacies);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showSelectedPharmacyDetails((Pharmacy) marker.getTag());
                addMarkerOptions(pharmacies, (Pharmacy) marker.getTag(), true);
                return false;
            }
        });
    }

    private void updateCameraBounds(List<Pharmacy> pharmacies) {

        builder = new LatLngBounds.Builder();
        List<LatLng> latLngs = addMarkerOptions(pharmacies, null, false);
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        /**initialize the padding for map boundary*/
        int padding = 50;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.animateCamera(cu);
            }
        });

    }



    private List<LatLng> addMarkerOptions(List<Pharmacy> pharmacies, Pharmacy pharmacy, boolean shouldReset) {

        Drawable locationDefault = THSFileUtils.getGpsDrawableFromFontIcon(getContext(),R.string.dls_location,R.color.uid_blue_level_55,24);
        Drawable locationSelected = THSFileUtils.getGpsDrawableFromFontIcon(getContext(),R.string.dls_location,R.color.uid_blue_level_55,32);
        Drawable locationUnSelected = THSFileUtils.getGpsDrawableFromFontIcon(getContext(),R.string.dls_location,R.color.uid_blue_level_5,24);

        List<LatLng> latLngList = new ArrayList<LatLng>();
        map.clear();
        for (final Pharmacy pharmacyItem : pharmacies) {
            LatLng latLng = new LatLng(pharmacyItem.getLatitude(), pharmacyItem.getLongitude());
            if (null != pharmacy && pharmacy.equals(pharmacyItem) && shouldReset) {
                map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(THSFileUtils.drawableToBitmap(locationSelected))).position(latLng)).setTag(pharmacyItem);
            } else if (shouldReset) {
                map.addMarker(new MarkerOptions().icon(fromResource(R.mipmap.unselected_gps_icon)).position(latLng)).setTag(pharmacyItem);
            } else {
                map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(THSFileUtils.drawableToBitmap(locationDefault))).position(latLng)).setTag(pharmacyItem);
            }
            latLngList.add(latLng);
        }
        return latLngList;

    }


    /**
     * This method displays the card at the bottom with the details of the selected phamracy from the list or by tapping on the map marker.
     *
     * @param pharmacy
     */
    protected void showSelectedPharmacyDetails(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        handleBack = true;
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(pharmacy.getName(), true);
        }
        if (selectedPharmacyLayout.getVisibility() == View.GONE) {
            selectedPharmacyLayout.setVisibility(View.VISIBLE);
            selectedPharmacyLayout.startAnimation(slideUpFromBottomAnimation);
            pharmacyTypeLayout.startAnimation(slideUpFromTopAnimation);
            pharmacyTypeLayout.setVisibility(View.GONE);
        }
        if (mapFragment.isHidden()) {
            getActivity().getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
            pharmacyListRecyclerView.setVisibility(View.GONE);
        }
        selectedPharmacyName.setText(pharmacy.getName());
        selectedPharmacyAddressLineOne.setText(pharmacy.getAddress().getAddress1());
        selectedPharmacyAddressLineTwo.setText(pharmacy.getAddress().getAddress2());
        selectedPharmacyState.setText(pharmacy.getAddress().getState().getName());
        selectedPharmacyZip.setText(pharmacy.getAddress().getZipCode().toString());
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(pharmacy.getPhone(), "US");
            selectedPharmacyPhone.setText(getString(R.string.ths_pharmacy_phone_text) + " " + phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        } catch (NumberParseException e) {
            AmwellLog.e("", "NumberParseException was thrown: " + e.toString());
        }
        if (null != pharmacy.getEmail()) {
            selectedPharmacyEmail.setText(getString(R.string.ths_pharmacy_email_text) + " " + pharmacy.getEmail());
        } else {
            selectedPharmacyEmail.setText(getString(R.string.ths_pharmacy_email_text) + "-");
        }

    }

    @Override
    public boolean handleBackEvent() {
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(R.string.pharmacy_list_fragment_name, true);
        }
        if (pharmacy_segment_control_one.isSelected()) {
            if (null != pharmacyRetailList && pharmacyRetailList.size() > 0) {
                addMarkerOptions(pharmacyRetailList, null, false);
                updateCameraBounds(pharmacyRetailList);
            }
        } else {
            if (null != pharmacyMailOrderList && pharmacyMailOrderList.size() > 0) {
                addMarkerOptions(pharmacyMailOrderList, null, false);
                updateCameraBounds(pharmacyMailOrderList);
            }
        }

        if (hideSelectedPharmacy()) {
            return true;
        } else {
            return handleBack;
        }
    }

    protected boolean hideSelectedPharmacy() {
        if (mapFragment.isVisible() && isListSelected) {
            getActivity().getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
            pharmacyListRecyclerView.setVisibility(View.VISIBLE);
        }
        if (selectedPharmacyLayout.getVisibility() == View.VISIBLE) {
            if (!isListSelected) {
                selectedPharmacyLayout.startAnimation(slideDownFromBottomAnimation);
                pharmacyTypeLayout.startAnimation(slideDownFromTopAnimation);
            }
            selectedPharmacyLayout.setVisibility(View.GONE);
            pharmacyTypeLayout.setVisibility(View.VISIBLE);
            handleBack = true;
            return true;
        } else {
            handleBack = false;
            return false;
        }

    }

    /**
     * Result received from THSSearchFragment when searched for pharmacy list from this fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHARMACY_SEARCH_CONSTANT) {
                isSearched = true;
                pharmaciesList = data.getParcelableArrayListExtra(THSConstants.THS_SEARCH_PHARMACY_SELECTED);
                if (null == pharmaciesList || pharmaciesList.size() == 0) {
                    showToast(getString(R.string.ths_pharmacy_fetch_error));
                } else {
                    setPharmaciesList(pharmaciesList);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_PHARMACY_MAP,null,null);
    }
}
