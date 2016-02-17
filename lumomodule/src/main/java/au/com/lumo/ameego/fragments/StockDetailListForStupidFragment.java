package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.adapters.StockAdapter;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.callbacks.GenericTwoReturnCallback;
import au.com.lumo.ameego.callbacks.WarnOptionSelectCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.WarningUtilsMD;
import au.com.lumo.ameego.viewHolders.StockItemViewHolder;

/**
 * Created by Zeki Guler on 6/08/15.
 */
public class StockDetailListForStupidFragment extends BaseFragment{


    private static final String MERCHANT_ITEM_KEY   = "MERCHANT_ITEM_KEY";
    private static final String STOCK_ITEMS_KEY     = "STOCK_ITEMS_KEY";
    private static final float HOW_FAR_FROM_CURENT_LOCATION = 3000; // 3 km


    private static final int HIGH_TO_LOW        = 0;
    private static final int LOW_TO_HIGH        = 1;
    private static final int A_TO_Z             = 2;
    private static final int Z_TO_A             = 3;
    private static final int GIFT_CARDS_ONLY    = 4;
    private static final int DISCOUNT_ONLY      = 5;
    private static final int NEAR_ME            = 6;
    private static final int SHOW_ALL           = 7;

    private RecyclerView   mRecyclerView;
    private ImageView mEmptyState;

    private MMerchant               mMerchandt;
    private ArrayList<MStockItem>   mStockItems;
    private StockAdapter            mAdapter;

    public static StockDetailListForStupidFragment newInstance(MMerchant merchant, ArrayList<MStockItem> items) {
        StockDetailListForStupidFragment fragment = new StockDetailListForStupidFragment();
        Bundle bundle                             = new Bundle();

        bundle.putSerializable(MERCHANT_ITEM_KEY, merchant);
        bundle.putSerializable(STOCK_ITEMS_KEY, items);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mMerchandt = (MMerchant )getArguments().getSerializable(MERCHANT_ITEM_KEY);
            mStockItems = (ArrayList<MStockItem>)getArguments().getSerializable(STOCK_ITEMS_KEY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.simpleList);
        mEmptyState   = (ImageView) view.findViewById(R.id.empty_state);
        view.findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     onfilterclicked();
            }
        });
        setList();
    }

    @Override
    protected String getTitle() {
        return mMerchandt.getName();
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
        return R.layout.fragment_stocks_of_merhandise;
    }

    @Override
    protected boolean hasFragOptionsMenu() {
        return true;
    }

    private void setList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        mRecyclerView.setLayoutManager(layoutManager);
        if(mAdapter == null) {

            ArrayList<Object> list = new ArrayList<>();

            for (MStockItem item : mStockItems){
                list.add(item);
            }

            list.add(mMerchandt);

            mAdapter = new StockAdapter(getActivity(), list, new IStockAdapterMessage());
        }
        mRecyclerView.setAdapter(mAdapter);

        if(mAdapter.isEmpty()) mEmptyState.setVisibility(View.VISIBLE);
        else                   mEmptyState.setVisibility(View.GONE);

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

    private class IStockAdapterMessage implements StockAdapter.IStockListMessage{
        @Override
        public void onItemClicked(View view, int position, StockItemViewHolder viewHolder) {

            if(view.getId() == R.id.card){
                goForStockItem(view,position, viewHolder);
            } else {
                Toast.makeText(getActivity(), "No item selected.", Toast.LENGTH_LONG).show();
            }
/*            switch (view.getId()){
                case R.id.card: goForStockItem(view,position, viewHolder); break;
                default: Toast.makeText(getActivity(), "No item selected.", Toast.LENGTH_LONG).show(); break;
            }*/
        }
    }

    private void goForStockItem(View view, int position, StockItemViewHolder viewHolder) {

        if      (viewHolder.getItem() instanceof MStockItem) fetchStockItemRelatedDetails   ((MStockItem) viewHolder.getItem());
        else if (viewHolder.getItem() instanceof MMerchant)  fetchAssociatedStockItemDetails((MMerchant) viewHolder.getItem());

    }

    private void fetchStockItemRelatedDetails(MStockItem item) {
        if(Constants.Config.DEVELOPER_MODE) Toast.makeText(getActivity(),"itemId: " + item.getStockItemId(), Toast.LENGTH_SHORT).show();
        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity());
        NetworkManager.fetchStockDetail(item.getStockItemId(), new GenericCallback<MStockItem>() {
            @Override
            public void done(MStockItem item, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {

                    if (item.getFulfilmentType() == Constants.FulfilmentType.DISPLAY)
                        mIActivity.replaceWith(StockDetailType1Fragment.newInstance(item));
                    else
                        mIActivity.replaceWith(StockDetailType2Fragment.newInstance(item));

                } else {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void fetchAssociatedStockItemDetails(MMerchant item) {
        if(Constants.Config.DEVELOPER_MODE) Toast.makeText(getActivity(),"merhandiseID: " + item.getMerchantId() + " stockId: " + item.getAssociatedStockItemId(), Toast.LENGTH_SHORT).show();
        WarningUtilsMD.startProgresslDialog("Loading", getActivity());
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
