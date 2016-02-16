package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.LumoSpecificUtils;

/**
 * Created by Zeki Guler on 21/07/15.
 */
public class MerchandisePlaceFragment extends BaseFragment{
    public  static final String TAG            = MerchandisePlaceFragment.class.getSimpleName();
    private static final String STOCK_ITEM_KEY = "stock_item_key";

    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMaps = null;
    private LatLng CENTER      = null;

    private ArrayList<MMerchant>     mMerchants = new ArrayList<>();
    private HashMap<Marker, Integer> mHashMap = new HashMap<>();

    private MStockItem mStockItem;

    public static MerchandisePlaceFragment newInstance(MStockItem stockItem) {

        MerchandisePlaceFragment fragment = new MerchandisePlaceFragment();
        Bundle                   bundle   = new Bundle();

        bundle.putSerializable(STOCK_ITEM_KEY, stockItem);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStockItem = (MStockItem)getArguments().getSerializable(STOCK_ITEM_KEY);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected String getTitle() {
        return mStockItem.getName();
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_near_me;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        mMapFragment                    = new SupportMapFragment();

        transaction.replace(R.id.map, mMapFragment);
        transaction.commitAllowingStateLoss();

        getChildFragmentManager().executePendingTransactions();

        initializeSiteArray();
        initializeMap();
    }

    private void initializeSiteArray() {
        mMerchants.clear();

        mMerchants.addAll(LumoSpecificUtils.getMerchandizeListFromStock(mStockItem));
    }

    private void initializeMap() {
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                CENTER = new LatLng(0, 0);
                mGoogleMaps = googleMap;

                mGoogleMaps.setIndoorEnabled(true);
                mGoogleMaps.setMyLocationEnabled(true);
                addMarkers();
                mGoogleMaps.setOnInfoWindowClickListener(new CustomMarkerInfoClickListener());
                if(mMerchants.size() > 0) // zoom first one
                    mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(mMerchants.get(0).getLatLng(), 13));
            }
        });
    }

    private void addMarkers() {
        if(mGoogleMaps == null ) return;

        for (int i = 0; i < mMerchants.size(); i++) {
            MMerchant merchant = mMerchants.get(i);
            LatLng latLng      = LumoSpecificUtils.getLatLngOfThisMerchant(merchant);

            if(latLng == null) continue;

            Marker marker = mGoogleMaps
                    .addMarker(new MarkerOptions()
                            .title(merchant.getName())
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin)));

            mHashMap.put(marker, i);
        }
    }

    private class CustomMarkerInfoClickListener implements GoogleMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {
            int pos = mHashMap.get(marker);
            MMerchant selectedMerhant = mMerchants.get(pos);
//            Toast.makeText(getActivity(), "Merchant Name: " + selectedMerhant.getName(), Toast.LENGTH_LONG).show();
        }
    }
}
