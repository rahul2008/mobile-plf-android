package com.philips.platform.ths.pharmacy;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.americanwell.sdk.entity.pharmacy.Pharmacy;
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
import com.philips.platform.ths.pharmacy.customtoggle.SegmentControl;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.List;

public class THSPharmacyListFragment extends THSBaseFragment implements OnMapReadyCallback, View.OnClickListener, THSPharmacyListViewListener, BackEventListener {

    private UIDNavigationIconToggler navIconToggler;
    private GoogleMap map;
    private ImageButton switchViewImageButton;
    private SupportMapFragment mapFragment;
    private RecyclerView pharmacyListRecyclerView;
    private THSPharmacyListAdapter thsPharmacyListAdapter;
    private THSPharmacyListPresenter thsPharmacyListPresenter;
    private THSConsumer thsConsumer;
    private ActionBarListener actionBarListener;
    private LatLngBounds.Builder builder;
    private CameraUpdate cu;
    private Label selectedPharmacyName, selectedPharmacyAddressLineOne, selectedPharmacyAddressLineTwo,
            selectedPharmacyState, selectedPharmacyZip, selectedPharmacyPhone, selectedPharmacyEmail, pharmacy_segment_control_one, pharmacy_segment_control_two;
    private RelativeLayout selectedPharmacyLayout;
    private SegmentControl pharmacyTypeLayout;
    private Animation slideUpFromBottomAnimation, slideDownFromBottomAnimation, slideUpFromTopAnimation, slideDownFromTopAnimation;
    private boolean handleBack = false;
    private boolean isListSelected = false;
    private List<Pharmacy> pharmacyList;
    private int REQUEST_LOCATION = 1001;
    private Button choosePharmacyButton;
    private Pharmacy pharmacy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_pharmacy_list_fragment, container, false);

        checkPermission();


        pharmacy_segment_control_one = (Label) view.findViewById(R.id.segment_control_view_one);
        pharmacy_segment_control_two = (Label) view.findViewById(R.id.segment_control_view_two);
        pharmacy_segment_control_one.setOnClickListener(this);
        pharmacy_segment_control_two.setOnClickListener(this);
        choosePharmacyButton = (Button) view.findViewById(R.id.choose_pharmacy_button);
        choosePharmacyButton.setOnClickListener(this);
        thsPharmacyListPresenter = new THSPharmacyListPresenter(this);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.pharmacymap);
        selectedPharmacyLayout = (RelativeLayout) view.findViewById(R.id.selected_pharmacy_layout);
        selectedPharmacyLayout.setVisibility(View.GONE);
        pharmacyTypeLayout = (SegmentControl) view.findViewById(R.id.pharmacy_type_layout);
        selectedPharmacyName = (Label) view.findViewById(R.id.selected_pharmacy_name);
        selectedPharmacyAddressLineOne = (Label) view.findViewById(R.id.selected_pharmacy_address_line_one);
        selectedPharmacyAddressLineTwo = (Label) view.findViewById(R.id.selected_pharmacy_address_line_two);
        selectedPharmacyState = (Label) view.findViewById(R.id.selected_pharmacy_state);
        selectedPharmacyZip = (Label) view.findViewById(R.id.selected_pharmacy_zip_code);
        selectedPharmacyPhone = (Label) view.findViewById(R.id.selected_pharmacy_phone);
        selectedPharmacyEmail = (Label) view.findViewById(R.id.selected_pharmacy_email);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        navIconToggler.restoreNavigationIcon();
        mapFragment.getMapAsync(this);
        switchViewImageButton = (ImageButton) view.findViewById(R.id.switch_view_layout);
        switchViewImageButton.setImageDrawable(getResources().getDrawable(R.mipmap.list_icon, getActivity().getTheme()));
        switchViewImageButton.setOnClickListener(this);
        pharmacyListRecyclerView = (RecyclerView) view.findViewById(R.id.pharmacy_list_recyclerview);

        pharmacyListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pharmacyListRecyclerView.setAdapter(thsPharmacyListAdapter);
        pharmacyListRecyclerView.setVisibility(View.GONE);

        slideUpFromBottomAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_up_from_bottom);

        slideDownFromBottomAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_down_from_bottom);
        slideUpFromTopAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_up_from_top);

        slideDownFromTopAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_down_from_top);
        return view;
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            fetchLocation();
        }
    }

    private void fetchLocation() {


    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Pharmacy list", true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        thsPharmacyListPresenter.fetchPharmacyList(thsConsumer, null, thsConsumer.getConsumer().getLegalResidence(), null);

    }

    public void setConsumer(THSConsumer thsConsumer) {
        this.thsConsumer = thsConsumer;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.switch_view_layout) {
            if (mapFragment.isHidden()) {
                getActivity().getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
                switchViewImageButton.setImageDrawable(getResources().getDrawable(R.mipmap.list_icon, getActivity().getTheme()));
                pharmacyListRecyclerView.setVisibility(View.GONE);
                isListSelected = false;
            } else {
                getActivity().getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                switchViewImageButton.setImageDrawable(getResources().getDrawable(R.mipmap.gps_icon, getActivity().getTheme()));
                pharmacyListRecyclerView.setVisibility(View.VISIBLE);
                isListSelected = true;
            }
        }
        if (v.getId() == R.id.segment_control_view_one) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), new THSPharmacyAndShippingFragment(), "PharmacyShipping").commit();
        }
        if (v.getId() == R.id.segment_control_view_two) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), new THSShippingAddressFragment(), "ShippingAddress").commit();
        }
        if(v.getId() == R.id.choose_pharmacy_button){
            thsPharmacyListPresenter.updateConsumerPreferredPharmacy(thsConsumer,pharmacy);
        }
    }

    @Override
    public void updatePharmacyListView(List<Pharmacy> pharmacies) {
        thsPharmacyListAdapter = new THSPharmacyListAdapter(pharmacies);
        thsPharmacyListAdapter.setOnPharmacyItemClickListener(new OnPharmacyListItemClickListener() {
            @Override
            public void onItemClick(Pharmacy pharmacy) {
                showSelectedPharmacyDetails(pharmacy);
                addMarkerOptions(pharmacyList, pharmacy, true);
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude())));
            }
        });
        pharmacyListRecyclerView.setAdapter(thsPharmacyListAdapter);
        pharmacyList = pharmacies;
        setMarkerOnMap();
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
    }

    private void setMarkerOnMap() {
        updateCameraBounds();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showSelectedPharmacyDetails((Pharmacy) marker.getTag());
                addMarkerOptions(pharmacyList, (Pharmacy) marker.getTag(), true);
                return false;
            }
        });
    }

    private void updateCameraBounds() {

        builder = new LatLngBounds.Builder();
        List<LatLng> latLngs = addMarkerOptions(pharmacyList, null, false);
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        /**initialize the padding for map boundary*/
        int padding = 50;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.animateCamera(cu);
    }

    private List<LatLng> addMarkerOptions(List<Pharmacy> pharmacies, Pharmacy pharmacy, boolean shouldReset) {

        List<LatLng> latLngList = new ArrayList<LatLng>();
        map.clear();
        for (final Pharmacy pharmacyItem : pharmacies) {
            LatLng latLng = new LatLng(pharmacyItem.getLatitude(), pharmacyItem.getLongitude());
            if (null != pharmacy && pharmacy.equals(pharmacyItem) && shouldReset) {
                map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.selected_gps_icon)).position(latLng)).setTag(pharmacyItem);
            } else if (shouldReset) {
                map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.unselected_gps_icon)).position(latLng)).setTag(pharmacyItem);
            } else {
                map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_icon)).position(latLng)).setTag(pharmacyItem);
            }
            latLngList.add(latLng);
        }
        return latLngList;

    }

    private void showSelectedPharmacyDetails(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        handleBack = true;
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
            selectedPharmacyPhone.setText("phone: " + phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        } catch (NumberParseException e) {
            Log.e("", "NumberParseException was thrown: " + e.toString());
        }
        if (null != pharmacy.getEmail()) {
            selectedPharmacyEmail.setText("email: " + pharmacy.getEmail());
        } else {
            selectedPharmacyEmail.setText("email: -");
        }

    }

    @Override
    public boolean handleBackEvent() {
        addMarkerOptions(pharmacyList, null, false);
        updateCameraBounds();
        if (hideSelectedPharmacy()) {
            return true;
        } else {
            return handleBack;
        }
    }

    private boolean hideSelectedPharmacy() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(getActivity(), "Permission denied : Going to search", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
