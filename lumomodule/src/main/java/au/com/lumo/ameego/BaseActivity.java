package au.com.lumo.ameego;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.fragments.MoreLumoFragment;
import au.com.lumo.ameego.fragments.NearMeFragment;
import au.com.lumo.ameego.fragments.RewardsFragment;
import au.com.lumo.ameego.fragments.YourCartFragment;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.interfaces.IMessageToActivity;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.utils.Navigator;
import au.com.lumo.ameego.utils.PrefUtils;
import de.greenrobot.event.EventBus;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Created by Zeki Guler on 9/07/15.
 */
public class BaseActivity extends AppCompatActivity {

    public static final String TAG                  = BaseActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 0;

    private Toolbar                  mToolbar;
    private static Navigator mNavigator;
    private ReactiveLocationProvider locationProvider;
    private rx.Observable<Location> lastKnownLocationObservable;
    private rx.Observable<Location> locationUpdatesObservable;
    private Subscription            lastKnownLocationSubscription;
    private Subscription            updatableLocationSubscription;
    private @Nullable Location mCurrentLocation = null;
    private Drawer                  mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        setUpLocation();
        setupToolbar();
        setupNavDrawer();
        initNavigator();
        setNewRootFragment(RewardsFragment.newInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartSilently();
    }

    private void updateCartSilently() {
        NetworkManager.getLatestCart(new GenericCallback<MShoppingCartVM>() {
            @Override
            public void done(MShoppingCartVM cart, Exception e) {
                if (e == null) {
                    saveCart(cart);
                } else {
//                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void needBadgeUpdate(MShoppingCartVM cart) {
        if (cart != null && cart.getShoppingCartItemsHelper() != null)
            updateBadgeNumber(cart.getShoppingCartItemsHelper().getItems().size(), 2);
        else updateBadgeNumber(0, 2);
    }

    private void updateBadgeNumber(int badgeNum, int index) {
        IDrawerItem dItem = mDrawer.getDrawerItems().get(index);
        if( dItem != null && dItem instanceof Badgeable)
            mDrawer.updateBadge(String.valueOf(badgeNum), index);
    }

    private void saveCart(MShoppingCartVM cart) {
        PrefUtils.saveCart(this, cart);
        logCartDetail(cart);
        needBadgeUpdate(cart);
    }

    private void logCartDetail(MShoppingCartVM mShopingCart) {
//        Log.d(TAG, "getSubTotal         : " + mShopingCart.getSubTotal());
//        Log.d(TAG, "getFreightCharge    : " + mShopingCart.getFreightCharge());
//        Log.d(TAG, "getHandlingFee      : " + mShopingCart.getHandlingFee());
//        Log.d(TAG, "getTotal            : " + mShopingCart.getTotal());
    }

    private void setUpLocation() {
        locationProvider = new ReactiveLocationProvider(getApplicationContext());
        lastKnownLocationObservable = locationProvider.getLastKnownLocation();

        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
//                .setNumUpdates(5)
                .setInterval(15000);

        locationUpdatesObservable = locationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                                .setAlwaysShow(true)  //Reference: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                )
                .doOnNext(new Action1<LocationSettingsResult>() {
                    @Override
                    public void call(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                status.startResolutionForResult(BaseActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException th) {
//                                Log.e("MainActivity", "Error opening settings activity.", th);
                            }
                        }
                    }
                })
                .flatMap(new Func1<LocationSettingsResult, rx.Observable<Location>>() {
                    @Override
                    public rx.Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                        return locationProvider.getUpdatedLocation(locationRequest);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        lastKnownLocationSubscription = lastKnownLocationObservable
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
//                        Log.d(TAG, "lastKnown Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
                        mCurrentLocation = location;
                        EventBus.getDefault().post(location);
                    }
                }, new ErrorHandler());

        updatableLocationSubscription = locationUpdatesObservable
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
//                        Log.d(TAG, "Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
                        mCurrentLocation = location;
                        EventBus.getDefault().post(location);
                    }
                }, new ErrorHandler());
    }

    @Override
    protected void onStop() {
        super.onStop();
        lastKnownLocationSubscription.unsubscribe();
        updatableLocationSubscription.unsubscribe();
    }

    private class ErrorHandler implements Action1<Throwable> {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(BaseActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
//            Log.d("BaseActivity", "Error occurred", throwable);
        }
    }

    private void initNavigator() {
        if(mNavigator != null) return;
        mNavigator = new Navigator(getSupportFragmentManager(), R.id.container);
    }
    private void setNewRootFragment(BaseFragment fragment){
//        Log.d(TAG, "setNewRootFragment: " + fragment.getClass().getSimpleName());
        if(fragment.hasCustomToolbar()){
            hideActionBar();
        }else {
            showActionBar();
        }
        mNavigator.setRootFragment(fragment);
        /*mDrawerLayout.closeDrawers();*/
    }

    private void replaceWithCurrentFragment(BaseFragment fragment){
//        Log.d(TAG, "replaceWithCurrentFragment: " + fragment.getClass().getSimpleName());

        if(fragment.hasCustomToolbar()){
            hideActionBar();
        }else {
            showActionBar();
        }

        mNavigator.goTo(fragment);
    }

    public void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar); //ButterKnife.findById(this, R.id.toolbar);
        if(mToolbar == null) {
//            Log.d("TEST" ,"Didn't find a toolbar");
            return;
        }
//        Log.d("TEST" ,"ToolBar attached");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.hide();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.show();
    }

    private void setupNavDrawer(){

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.rewards_categories).withIcon(R.drawable.icon_deals_idle).withSelectedIcon(R.drawable.icon_deals_active).withIdentifier(1),
                        /*new PrimaryDrawerItem().withName(R.string.news).withIcon(GoogleMaterial.Icon.gmd_local_library).withSelectedIcon(R.drawable.icon_deals_idle).withIdentifier(2),*/
                        new PrimaryDrawerItem().withName(R.string.near_me).withIcon(R.drawable.icon_nearme_idle).withSelectedIcon(R.drawable.icon_nearme_active).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.your_cart).withIcon(R.drawable.icon_cart_idle).withSelectedIcon(R.drawable.icon_cart_active).withBadge("0").withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.more).withIcon(R.drawable.icon_more_idle).withSelectedIcon(R.drawable.icon_more_active).withIdentifier(4)
                        /*new PrimaryDrawerItem().withName(R.string.lumo_my_account).withIcon(GoogleMaterial.Icon.gmd_person).withIdentifier(4),*/
                        /*new PrimaryDrawerItem().withName(R.string.setting_support).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(5)*/
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if(drawerItem == null) return false;

                        switch (drawerItem.getIdentifier()){
                            case 1: setNewRootFragment(RewardsFragment.newInstance());  break;
                            /*case 2: setNewRootFragment(WebViewFragment.newInstance(Constants.Url.TWITTER_URL, getResources().getString(R.string.news))); break;*/
                            case 2: setNewRootFragment(NearMeFragment.newInstance());   break;
                            case 3: setNewRootFragment(YourCartFragment.newInstance()); break;
                            case 4: setNewRootFragment(MoreLumoFragment.newInstance()); break;
                            /*case 4: setNewRootFragment(WebViewFragment.newInstance(Constants.Url.MY_ACCOUNT, getResources().getString(R.string.lumo_my_account))); break;*/
                            /*case 5: setNewRootFragment(SettingsFragment.newInstance()); break;*/
                        }
                        return false;
                    }
                })
                .build();
    }

    public IMessageToActivity getFragmentListener(){
        return new IMessageToActivity() {
            @Override
            public void onBackPresses(Fragment currentfragment) {
            }

            @Override
            public Bundle getShareBundle() {
                return null;
            }

            @Override
            public void replaceWith(BaseFragment fragment) {
                replaceWithCurrentFragment(fragment);
            }

            @Override
            public void setNewRoot(BaseFragment fragment) {
                setNewRootFragment(fragment);
            }

            @Override
            public void openDrawer() {
                mDrawer.openDrawer();
            }

            @Override
            public Location getCurrentLocation() {
                return mCurrentLocation;
            }

            @Override
            public void saveCartToDb(MShoppingCartVM shoppingOverall) {
                saveCart(shoppingOverall);
            }

            @Override
            public Toolbar getToolBar() {
                return mToolbar;
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen())
            mDrawer.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public void finish() {
        mNavigator = null;
        super.finish();
    }

}
