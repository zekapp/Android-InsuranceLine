package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.adapters.StockAdapter;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.callbacks.GenericTwoReturnCallback;
import au.com.lumo.ameego.callbacks.WarnOptionSelectCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MCategory;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.WarningUtilsMD;
import au.com.lumo.ameego.viewHolders.StockItemViewHolder;

/**
 * Created by Zeki Guler on 15/07/15.
 */
public class StockListFragment extends BaseFragment{
    public static final String TAG = StockListFragment.class.getSimpleName();

    private static final String CATEGORY_KEY                = "stock_list_key";
    private static final float HOW_FAR_FROM_CURENT_LOCATION = 3000; // 3 km

    private static final int HIGH_TO_LOW        = 0;
    private static final int LOW_TO_HIGH        = 1;
    private static final int A_TO_Z             = 2;
    private static final int Z_TO_A             = 3;
    private static final int GIFT_CARDS_ONLY    = 4;
    private static final int DISCOUNT_ONLY      = 5;
    private static final int NEAR_ME            = 6;
    private static final int SHOW_ALL           = 7;

    /*@Bind(R.id.simpleList)        */ RecyclerView            mRecyclerView;
    /*@Bind(R.id.coordinator_layout)*/ CoordinatorLayout       mCoordinatorLayout;
    /*@Bind(R.id.collapsing_toolbar)*/ CollapsingToolbarLayout mCollapsingToolbar;
    /*@Bind(R.id.headerImageId)     */ ImageView               mHeaderImage;
    /*@Bind(R.id.empty_state)       */ ImageView               mEmptyState;

    private MCategory              mCategory;
    private StockAdapter           mAdapter;

    public static StockListFragment newInstance(MCategory category) {

        StockListFragment fragment = new StockListFragment();
        Bundle            bundle   = new Bundle();

        bundle.putSerializable(CATEGORY_KEY, category);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_stock_list;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = (MCategory)getArguments().getSerializable(CATEGORY_KEY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollapsingToolbar.setTitle(getTitle());
        mCollapsingToolbar.setExpandedTitleColor(0x00ffffff);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setList();
        loadHeaderImage();
    }

    private void loadHeaderImage() {
//        Log.d(TAG, "Header ImgL " + Constants.Url.IMAGE_BASE + mCategory.getSubCategoryImageId());
        if(mCategory.getSubCategoryImageId() != null) {
            Picasso.with(getActivity())//http://lumorewards.com.au/Default/Resources/Image/bddb9301-b137-497d-9734-81c2a59122d4
                    .load(Constants.Url.IMAGE_BASE + mCategory.getSubCategoryImageId())
                    .into(mHeaderImage);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_stock_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
//        Log.d(TAG, "onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(), "onOptionsItemSelected", Toast.LENGTH_LONG).show();
        int id = item.getItemId();
        if      (id ==  R.id.action_high_to_low)     mAdapter.filterHighToLow();
        else if (id ==  R.id.action_low_to_high)     mAdapter.filterLowToHigh();
        else if (id ==  R.id.action_a_to_z)          mAdapter.filterAtoZ();
        else if (id ==  R.id.action_z_to_a)          mAdapter.filterZtoA();
        else if (id ==  R.id.action_gift_cards_only) ;
        else if (id ==  R.id.action_discount_only)   ;
        else if (id ==  R.id.action_show_near_me)    ;
        else if (id ==  R.id.action_show_all)        ;

/*        switch (item.getItemId()){
            case R.id.action_high_to_low:     mAdapter.filterHighToLow();   break;
            case R.id.action_low_to_high:     mAdapter.filterLowToHigh();   break;
            case R.id.action_a_to_z:          mAdapter.filterAtoZ();        break;
            case R.id.action_z_to_a:          mAdapter.filterZtoA();        break;
            case R.id.action_gift_cards_only: break;
            case R.id.action_discount_only:   break;
            case R.id.action_show_near_me:    break;
            case R.id.action_show_all:        break;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /*@OnClick(R.id.menu_button)*/
    void onfilterclicked(){
        WarningUtilsMD.alertDialogOption(getActivity(), "Select Filter", R.array.filters, new WarnOptionSelectCallback() {
            @Override
            public void done(int which) {
                optionSelected(which);
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void optionSelected(int which) {
        switch (which){
            case HIGH_TO_LOW:     mAdapter.filterHighToLow();   break;
            case LOW_TO_HIGH:     mAdapter.filterLowToHigh();   break;
            case A_TO_Z:          mAdapter.filterAtoZ();        break;
            case Z_TO_A:          mAdapter.filterZtoA();        break;
            case GIFT_CARDS_ONLY: mAdapter.showGiftCardsOnly(); break;
            case DISCOUNT_ONLY:   mAdapter.showDiscCodesOnly(); break;
            case NEAR_ME:         showNearMeOnly();             break;
            case SHOW_ALL:        mAdapter.showAll();           break;
        }
    }

    private void showNearMeOnly() {
        if(mIActivity.getCurrentLocation() != null){
            mAdapter.showNearMe(mIActivity.getCurrentLocation(),HOW_FAR_FROM_CURENT_LOCATION);
        }
    }


    private void setList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        mRecyclerView.setLayoutManager(layoutManager);
        if(mAdapter == null) {

            ArrayList<Object> list = new ArrayList<>();

            for(MStockItem stockItem : mCategory.getStockItemsHelper().getStockItems()){
                list.add(stockItem);
            }

            for(MMerchant merchant : mCategory.getMerchantsHelper().getMerchants()){
                if(merchant.isDisplayAsReward())
                    list.add(merchant);
            }

            mAdapter = new StockAdapter(getActivity(), list, new IStockAdapterMessage());
        }
        mRecyclerView.setAdapter(mAdapter);

        if(mAdapter.isEmpty()) mEmptyState.setVisibility(View.VISIBLE);
        else                   mEmptyState.setVisibility(View.GONE);

    }

    @Override
    protected String getTitle() {
        return mCategory.getCategoryName();
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected boolean hasFragOptionsMenu() {
        return false;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    private class IStockAdapterMessage implements StockAdapter.IStockListMessage{
        @Override
        public void onItemClicked(View view, int position, StockItemViewHolder viewHolder) {
            if(view.getId() == R.id.card ){
                goForStockItem(view,position, viewHolder);
            } else{
                Toast.makeText(getActivity(),"No item selected.",Toast.LENGTH_LONG).show();
            }

/*            switch (view.getId()){
                case R.id.card: goForStockItem(view,position, viewHolder); break;
                default: Toast.makeText(getActivity(),"No item selected.",Toast.LENGTH_LONG).show(); break;
            }*/
        }
    }

    private void goForStockItem(View view, int position, StockItemViewHolder viewHolder) {

        if      (viewHolder.getItem() instanceof MStockItem) fetchStockItemRelatedDetails   ((MStockItem) viewHolder.getItem());
        else if (viewHolder.getItem() instanceof MMerchant)  fetchAssociatedStockItemDetails((MMerchant) viewHolder.getItem());

    }

    private void fetchStockItemRelatedDetails(MStockItem item) {
        if(Constants.Config.DEVELOPER_MODE) Toast.makeText(getActivity(),"itemId: " + item.getStockItemId(), Toast.LENGTH_SHORT).show();
        WarningUtilsMD.startProgresslDialog("Please Wait...", getActivity());
        NetworkManager.fetchStockDetail(item.getStockItemId(), new GenericCallback<MStockItem>() {
            @Override
            public void done(MStockItem item, Exception e) {
                WarningUtilsMD.stopProgress();
                if(e == null){

                    if(item.getFulfilmentType() == Constants.FulfilmentType.DISPLAY)
                        mIActivity.replaceWith(StockDetailType1Fragment.newInstance(item));
                    else
                        mIActivity.replaceWith(StockDetailType2Fragment.newInstance(item));

                }else{
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void fetchAssociatedStockItemDetails(MMerchant item) {
        if(Constants.Config.DEVELOPER_MODE) Toast.makeText(getActivity(),"merhandiseID: " + item.getMerchantId() + " stockId: " + item.getAssociatedStockItemId(), Toast.LENGTH_SHORT).show();
        WarningUtilsMD.startProgresslDialog("Please Wait...", getActivity());
        NetworkManager.fetchAssociatedStockDetail(item.getMerchantId(), item.getAssociatedStockItemId(), new GenericTwoReturnCallback<MMerchant,MStockItem>() {
            @Override
            public void done(MMerchant merchant, MStockItem stockItem, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    mIActivity.replaceWith(StockDetailType3Fragment.newInstance(merchant, stockItem));
                } else {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
