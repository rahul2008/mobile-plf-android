package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.List;

public class THSPharmacyListFragment extends THSBaseFragment implements OnMapReadyCallback,View.OnClickListener,THSPharmacyListViewListener,BackEventListener{

    private UIDNavigationIconToggler navIconToggler;
    private GoogleMap map;
    private ImageButton imageButton;
    private SupportMapFragment mapFragment;
    private RecyclerView pharmacyListRecyclerView;
    private THSPharmacyListAdapter thsPharmacyListAdapter;
    private THSPharmacyListPresenter thsPharmacyListPresenter;
    private THSConsumer thsConsumer;
    private ActionBarListener actionBarListener;
    private LatLngBounds.Builder builder;
    private CameraUpdate cu;
    private Label selectedPharmacyName,selectedPharmacyAddressLineOne,selectedPharmacyAddressLineTwo,
    selectedPharmacyState,selectedPharmacyZip,selectedPharmacyPhone,selectedPharmacyEmail;
    private RelativeLayout selectedPharmacyLayout,pharmacyTypeLayout;
    private Animation slideUpFromBottomAnimation, slideDownFromBottomAnimation,slideUpFromTopAnimation,slideDownFromTopAnimation;
    private boolean handleBack = false;
    private boolean isListSelected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_pharmacy_list_fragment,container,false);
        thsPharmacyListPresenter = new THSPharmacyListPresenter(this) ;
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.pharmacymap);
        selectedPharmacyLayout = (RelativeLayout)view.findViewById(R.id.selected_pharmacy_layout);
        selectedPharmacyLayout.setVisibility(View.GONE);
        pharmacyTypeLayout = (RelativeLayout)view.findViewById(R.id.pharmacy_type_layout);
        selectedPharmacyName = (Label)view.findViewById(R.id.selected_pharmacy_name);
        selectedPharmacyAddressLineOne = (Label)view.findViewById(R.id.selected_pharmacy_address_line_one);
        selectedPharmacyAddressLineTwo = (Label)view.findViewById(R.id.selected_pharmacy_address_line_two);
        selectedPharmacyState = (Label)view.findViewById(R.id.selected_pharmacy_state);
        selectedPharmacyZip = (Label)view.findViewById(R.id.selected_pharmacy_zip_code);
        selectedPharmacyPhone = (Label)view.findViewById(R.id.selected_pharmacy_phone);
        selectedPharmacyEmail = (Label)view.findViewById(R.id.selected_pharmacy_email);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        navIconToggler.restoreNavigationIcon();
        mapFragment.getMapAsync(this);
        imageButton = (ImageButton) view.findViewById(R.id.switchpharmacyview);
        imageButton.setOnClickListener(this);
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

    @Override
    public void onResume() {
        super.onResume();
        if(null != actionBarListener){
            actionBarListener.updateActionBar("Pharmacy list",true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        thsPharmacyListPresenter.fetchPharmacyList(thsConsumer,null,thsConsumer.getConsumer().getLegalResidence(),null);

    }

    public void setConsumer(THSConsumer thsConsumer){
        this.thsConsumer = thsConsumer;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.switchpharmacyview){
            if(mapFragment.isHidden()) {
                getActivity().getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
                pharmacyListRecyclerView.setVisibility(View.GONE);
                isListSelected = false;
            }else {
                getActivity().getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                pharmacyListRecyclerView.setVisibility(View.VISIBLE);
                isListSelected = true;
            }
        }
    }

    @Override
    public void updatePharmacyListView(List<Pharmacy> pharmacies) {
        thsPharmacyListAdapter = new THSPharmacyListAdapter(pharmacies);
        thsPharmacyListAdapter.setOnPharmacyItemClickListener(new OnPharmacyListItemClickListener() {
            @Override
            public void onItemClick(Pharmacy pharmacy) {
                showSelectedPharmacyDetails(pharmacy);
            }
        });
        pharmacyListRecyclerView.setAdapter(thsPharmacyListAdapter);
        setMarkerOnMap(pharmacies);
    }

    private void setMarkerOnMap(List<Pharmacy> pharmacies) {
        List<LatLng> latLngList = new ArrayList<LatLng>();
        for(Pharmacy pharmacy:pharmacies){
            LatLng latLng = new LatLng(pharmacy.getLatitude(),pharmacy.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(pharmacy.getName())).setTag(pharmacy);
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    showSelectedPharmacyDetails((Pharmacy)marker.getTag());
                    Log.v("Marker clicked name : ",""+((Pharmacy)marker.getTag()).getName());
                    return false;
                }
            });
            latLngList.add(latLng);
        }

        builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngList) {
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

    private void showSelectedPharmacyDetails(Pharmacy pharmacy) {
        handleBack = true;
        if(selectedPharmacyLayout.getVisibility() == View.GONE) {
            selectedPharmacyLayout.setVisibility(View.VISIBLE);
            selectedPharmacyLayout.startAnimation(slideUpFromBottomAnimation);
            pharmacyTypeLayout.startAnimation(slideUpFromTopAnimation);
            pharmacyTypeLayout.setVisibility(View.GONE);
        }
        if(mapFragment.isHidden()) {
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
            selectedPharmacyPhone.setText("phone: "+ phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        } catch (NumberParseException e) {
            Log.e("","NumberParseException was thrown: " + e.toString());
        }
        selectedPharmacyEmail.setText("email: "+pharmacy.getEmail());
    }

    @Override
    public boolean handleBackEvent() {
        if(hideSelectedPharmacy()){
            return true;
        }
        else {
            return handleBack;
        }
    }

    private boolean hideSelectedPharmacy() {
        if(mapFragment.isVisible() && isListSelected){
            getActivity().getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
            pharmacyListRecyclerView.setVisibility(View.VISIBLE);
        }
        if(selectedPharmacyLayout.getVisibility() == View.VISIBLE) {
            if(!isListSelected) {
                selectedPharmacyLayout.startAnimation(slideDownFromBottomAnimation);
                pharmacyTypeLayout.startAnimation(slideDownFromTopAnimation);
            }
            selectedPharmacyLayout.setVisibility(View.GONE);
            pharmacyTypeLayout.setVisibility(View.VISIBLE);
            handleBack = true;
            return true;
        }
        else {
            handleBack = false;
            return false;
        }

    }
}
