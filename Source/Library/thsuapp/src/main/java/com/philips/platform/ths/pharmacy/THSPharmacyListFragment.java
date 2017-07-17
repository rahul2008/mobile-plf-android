package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.americanwell.sdk.entity.pharmacy.PharmacyType;
import com.google.android.gms.common.ConnectionResult;
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
import com.philips.platform.ths.pharmacy.customtoggle.SegmentControl;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class THSPharmacyListFragment extends THSPharmacyListBaseFragment implements OnMapReadyCallback, View.OnClickListener,
        THSPharmacyListViewListener,
        BackEventListener{

    private UIDNavigationIconToggler navIconToggler;
    private GoogleMap map;
    private ImageButton switchViewImageButton;
    private SupportMapFragment mapFragment;
    private RecyclerView pharmacyListRecyclerView;
    private THSPharmacyListAdapter thsPharmacyListAdapter;
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
    private Button choosePharmacyButton;
    private Pharmacy pharmacy;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_pharmacy_list_fragment, container, false);

        checkPermission();
        findViewByIDs(view);
        setOnClickListeners();
        findViewByIDs();
        setUpAnimations();

        return view;
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
        mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.pharmacymap, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Pharmacy list", true);
        }
        //thsPharmacyListPresenter.fetchPharmacyList(thsConsumer, null, thsConsumer.getConsumer().getLegalResidence(), null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onClick(View v) {
        thsPharmacyListPresenter.onEvent(v.getId());
    }


    /**
     * This method handles the "choose pharmacy" button. This sets the customers preferred pharmacy.
     */
    public void setPreferredPharmacy() {
        thsPharmacyListPresenter.updateConsumerPreferredPharmacy(thsConsumer, pharmacy);
    }

    /**
     * This method handles the filter for Mail order filtering only Mail order content.
     */
    public void showMailOrderView() {
        pharmacy_segment_control_one.setSelected(false);
        pharmacy_segment_control_two.setSelected(true);
        updateView(pharmacyMailOrderList);
    }


    /**
     * This method handles the filter for Retail filtering only Retail content.
     */
    public void showRetailView() {
        pharmacy_segment_control_one.setSelected(true);
        pharmacy_segment_control_two.setSelected(false);
        updateView(pharmacyRetailList);
    }


    /**
     * This method handles the swtich between map view and the list view indicated by the map/list icon.
     */
    public void switchView() {

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

    private List<Pharmacy> pharmacyRetailList, pharmacyMailOrderList;

    @Override
    public void updatePharmacyListView(List<Pharmacy> pharmacies) {
        pharmacyRetailList = filterList(pharmacies, PharmacyType.Retail);
        pharmacyMailOrderList = filterList(pharmacies, PharmacyType.MailOrder);
        updateView(pharmacyRetailList);
    }

    /**
     *This method updates the view based on the filter criteria. It will update the view to either Retail filter or Mail order filter
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
        if(null != pharmacies && pharmacies.size() > 0){
            setMarkerOnMap(pharmacies);
        }

    }

    /**
     * Form the list of pharmacies passed, it will filter the list to either mail order or retail.
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
    public void validateForMailOrder() {
        if (pharmacy.getType() == PharmacyType.MailOrder) {
            THSShippingAddressFragment thsShippingAddressFragment = new THSShippingAddressFragment();
            thsShippingAddressFragment.setActionBarListener(getActionBarListener());
            thsShippingAddressFragment.setConsumerAndAddress(thsConsumer, address);
            getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), thsShippingAddressFragment, "ShippingAddressFragment").addToBackStack(null).commit();
        }
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
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


    /**
     * This method displays the card at the bottom with the details of the selected phamracy from the list or by tapping on the map marker.
     * @param pharmacy
     */
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
        if (pharmacy_segment_control_one.isSelected()) {
            addMarkerOptions(pharmacyRetailList, null, false);
            updateCameraBounds(pharmacyRetailList);
        } else {
            addMarkerOptions(pharmacyMailOrderList, null, false);
            updateCameraBounds(pharmacyMailOrderList);
        }

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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
