package au.com.lumo.ameego.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.callbacks.GenericArrayCallBack;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.callbacks.GenericTwoReturnCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MMerchantPositionVM;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.WarningUtilsMD;
import de.greenrobot.event.EventBus;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Zeki Guler on 10/07/15.
 */
public class NearMeFragment extends BaseFragment {

    private static final int DEFAULT_SEARCH_DIAMETER =  2000; //2 km
    private static final String TAG                  = NearMeFragment.class.getSimpleName();

    private SmoothProgressBar mProgressBar;
    private SearchView mSearchView;

    private SupportMapFragment              mMapFragment;
    private Location                        mCurLocation;
    private GoogleMap                       mGoogleMaps   = null;
    private ArrayList<MMerchantPositionVM>  mMerchants    = new ArrayList<>();
    private HashMap<Marker, Integer>        mHashMap      = new HashMap<>();
    private LatLng                          currentCenterOfMap;
    private LatLng                          oldCenterOfMap;
    private String                          mSearchQuery = ""; // default is empty

    public static NearMeFragment newInstance() {
        return new NearMeFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (SmoothProgressBar) view.findViewById(R.id.progress_bar);
        mSearchView  = (SearchView) view.findViewById(R.id.search_merchandise);
        view.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshClicked();
            }
        });
        setUpSearchView();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.near_me);
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
    protected boolean hasFragOptionsMenu() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        mMapFragment                    = new SupportMapFragment();

        transaction.replace(R.id.map, mMapFragment);
        transaction.commitAllowingStateLoss();

        getChildFragmentManager().executePendingTransactions();

        initializeMap();
    }

    /*@OnClick(R.id.refresh)*/
    void onRefreshClicked(){
        fetchMerchandise(currentCenterOfMap);
    }

    private void setUpSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setIconifiedByDefault(true);
//        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchQuery = query;
                clearMarkerAndArray();
                fetchMerchandise(currentCenterOfMap);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                Toast.makeText(getActivity(), "mSearchView on Closed", Toast.LENGTH_LONG).show();
                mSearchQuery = "";
                clearMarkerAndArray();
                fetchMerchandise(currentCenterOfMap);
                mSearchView.clearFocus();
                return false;
            }
        });

    }

    private void clearMarkerAndArray() {
        if (mGoogleMaps == null ) return; // no action

        mGoogleMaps.clear();
        mHashMap.clear();
        mMerchants.clear();


    }

    private void getNearestMerchandise() {
        if (mCurLocation == null) {
            warnAboutLocation();
            return;
        }

        mMerchants.clear();

        LatLng latLng = new LatLng(mCurLocation.getLatitude(), mCurLocation.getLongitude());

        fetchMerchandise(latLng);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHashMap.clear();
        mHashMap = null;
    }

    private void fetchMerchandise(LatLng location) {
        mProgressBar.progressiveStart();
        NetworkManager.fetchNearMerchandise(location, mSearchQuery, DEFAULT_SEARCH_DIAMETER, new GenericArrayCallBack<MMerchantPositionVM>() {
            @Override
            public void done(ArrayList<MMerchantPositionVM> list, Exception e) {
                mProgressBar.progressiveStop();
                if (e == null) {
                    //remove duplicate elements
                    ArrayList<MMerchantPositionVM> temp = removeDuplicateElements(list);

                    if(temp.size() > 0)
                        mMerchants.addAll(temp);

                    addMarkers();

//                    Toast.makeText(getActivity(), "Array size: " + mMerchants.size(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private ArrayList<MMerchantPositionVM> removeDuplicateElements(ArrayList<MMerchantPositionVM> list) {
        ArrayList<MMerchantPositionVM> temp = new ArrayList<>();
        boolean isNewMerhandise;
        for(MMerchantPositionVM item : list){
            isNewMerhandise = true;
            for(MMerchantPositionVM old : mMerchants){
                if(item.getMerchantId() == old.getMerchantId()){
                    isNewMerhandise = false;
                    break;
                }
            }
            if(isNewMerhandise)
                temp.add(item);
        }

        if(temp.size() > 0)
            mMerchants.addAll(temp);

        return temp;
    }

    private void warnAboutLocation() {
        Toast.makeText(getActivity(),"You location is getting. Please wait...",Toast.LENGTH_LONG).show();
    }

    private void initializeMap() {
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMaps = googleMap;
                mGoogleMaps.setIndoorEnabled(true);
                mGoogleMaps.setMyLocationEnabled(true);
                mGoogleMaps.setOnInfoWindowClickListener(new CustomMarkerInfoClickListener());

                mGoogleMaps.setOnCameraChangeListener(new CustomCameraChangeListener());
                mCurLocation = mIActivity.getCurrentLocation();
                zoomUserCurrentLocation(mCurLocation);
                getNearestMerchandise();
            }
        });


    }

    private void addMarkers() {
        if(mGoogleMaps == null ) return;

        for (int i = 0; i < mMerchants.size(); i++) {
            MMerchantPositionVM merchant = mMerchants.get(i);
            LatLng latLng                = new LatLng(merchant.getLatitude(), merchant.getLongitude());

            String name    = merchant.getMerchantName();
            String address = merchant.getAddress1();

            Marker marker = mGoogleMaps
                    .addMarker(new MarkerOptions()
                            .title(name)
                            .snippet(address)
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));

            mHashMap.put(marker, i);
        }
    }

    private class CustomMarkerInfoClickListener implements GoogleMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {
            int pos = mHashMap.get(marker);
            MMerchantPositionVM selectedMerchant = mMerchants.get(pos);
//            Toast.makeText(getActivity(),"Merchant Name: " + selectedMerhant.getMerchantName(),Toast.LENGTH_LONG).show();
            if (hasManyAssociatedStocks(selectedMerchant)){
                mIActivity.replaceWith(StockDetailListFragment.newInstance(selectedMerchant));
            }else{
                MStockItem item = selectedMerchant.getMerchantStockItems().getStockItems().get(0);

                goForAssociatedStock(selectedMerchant.getMerchantId(), item.getStockItemId());
//                goForJustStock(item.getStockItemId());
            }
        }
    }

    private void goForAssociatedStock(int merchantId, int stockItemId) {
//        Log.d(TAG, "merchantId: " + merchantId + " stockItemId: " + stockItemId);
        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity());
        NetworkManager.fetchAssociatedStockDetail(merchantId, stockItemId, new GenericTwoReturnCallback<MMerchant,MStockItem>() {
            @Override
            public void done(MMerchant merchant, MStockItem stockItem, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    if(merchant.isDisplayAsReward()) {
                        ArrayList<MStockItem> items = new ArrayList<>();
                        items.add(stockItem);
                        mIActivity.replaceWith(StockDetailListForStupidFragment.newInstance(merchant, items));      // never work with stupid people
//                        mIActivity.replaceWith(StockDetailType3Fragment.newInstance(merchant, stockItem));
                    }
                    else if(stockItem.getFulfilmentType() == Constants.FulfilmentType.DISPLAY)
                        mIActivity.replaceWith(StockDetailType1Fragment.newInstance(stockItem));
                    else
                        mIActivity.replaceWith(StockDetailType2Fragment.newInstance(stockItem));

                } else {
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goForJustStock(int stockItemId) {
        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity());
        NetworkManager.fetchStockDetail(stockItemId, new GenericCallback<MStockItem>() {
            @Override
            public void done(MStockItem item, Exception e) {
                WarningUtilsMD.stopProgress();
                if(e == null){

                    if(item.getFulfilmentType() == Constants.FulfilmentType.DISPLAY)
                        mIActivity.replaceWith(StockDetailType1Fragment.newInstance(item));
                    else
                        mIActivity.replaceWith(StockDetailType2Fragment.newInstance(item));

                }else{
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean hasManyAssociatedStocks(MMerchantPositionVM merchant) {

        return  merchant.getMerchantStockItems()                   != null  &&
                merchant.getMerchantStockItems().getStockItems()   != null  &&
                merchant.getMerchantStockItems().getStockItems().size() > 1;

    }

    public void onEvent(Location location){
        if(mCurLocation == null){
//            Toast.makeText(getActivity(), "frag Lat: " + location.getLatitude() + " Lon: " + location.getLongitude(), Toast.LENGTH_LONG).show();
            mCurLocation = location;
            zoomUserCurrentLocation(location);
            getNearestMerchandise();
        }
    }

    private void zoomUserCurrentLocation(Location location) {
        if(mGoogleMaps != null && location != null){

            mCurLocation       = location;
            currentCenterOfMap = new LatLng(location.getLatitude(), location.getLongitude());
            oldCenterOfMap     = currentCenterOfMap;

            mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCenterOfMap, 15));
        }
    }


    private class CustomCameraChangeListener implements GoogleMap.OnCameraChangeListener {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            currentCenterOfMap = mGoogleMaps.getCameraPosition().target;

            if(isDistanceBigEnough(currentCenterOfMap, oldCenterOfMap)){
                oldCenterOfMap = currentCenterOfMap;

                fetchMerchandise(currentCenterOfMap);
            }

        }
    }

    private boolean isDistanceBigEnough(LatLng cP, LatLng oP) {
        float [] dist = new float[1];
        Location.distanceBetween(
                cP.latitude, cP.longitude,
                oP.latitude, oP.longitude, dist);

//        Toast.makeText(getActivity(),"dist: " + dist[0],Toast.LENGTH_LONG).show();

        return dist[0] > 1000;
    }
}
