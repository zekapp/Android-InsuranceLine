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
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MMerchantPositionVM;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.WarningUtilsMD;
import au.com.lumo.ameego.viewHolders.StockItemViewHolder;

/**
 * Created by Zeki Guler on 29/07/15.
 */
public class StockDetailListFragment extends BaseFragment {
    public  static final String TAG            = StockDetailListFragment.class.getSimpleName();
    public  static final String STOCK_ITEM_KEY = "STOCK_ITEM_KEY_LIST";

    private RecyclerView   mRecyclerView;
    private ImageView      mEmptyState;

    private MMerchantPositionVM mMMerchantPositionVM;
    private StockAdapter        mAdapter;

    public static StockDetailListFragment newInstance(MMerchantPositionVM merhandiseWithAsociatedStockItem) {
        StockDetailListFragment fragment = new StockDetailListFragment();
        Bundle bundle                    = new Bundle();

        bundle.putSerializable(STOCK_ITEM_KEY, merhandiseWithAsociatedStockItem);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mMMerchantPositionVM = (MMerchantPositionVM)getArguments().getSerializable(STOCK_ITEM_KEY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.simpleList);
        mEmptyState   = (ImageView) view.findViewById(R.id.empty_state);
        setList();
    }

    private void setList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        mRecyclerView.setLayoutManager(layoutManager);
        if(mAdapter == null) {

            ArrayList<Object> list = new ArrayList<>();

            for(MStockItem stockItem : mMMerchantPositionVM.getMerchantStockItems().getStockItems()){
                list.add(stockItem);
            }

            mAdapter = new StockAdapter(getActivity(), list, new IStockAdapterMessage());
        }
        mRecyclerView.setAdapter(mAdapter);

        if(mAdapter.isEmpty()) mEmptyState.setVisibility(View.VISIBLE);
        else                   mEmptyState.setVisibility(View.GONE);

    }

    @Override
    protected String getTitle() {
        return mMMerchantPositionVM.getMerchantName();
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

    private class IStockAdapterMessage implements StockAdapter.IStockListMessage{
        @Override
        public void onItemClicked(View view, int position, StockItemViewHolder viewHolder) {
            if (view.getId() == R.id.card){
                goForStockItem(view,position, viewHolder);
            }else{
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
//        Toast.makeText(getActivity(),"itemId: " + item.getStockItemId(), Toast.LENGTH_SHORT).show();
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
//        Toast.makeText(getActivity(),"merhandiseID: " + item.getMerchantId() + " stockId: " + item.getAssociatedStockItemId(), Toast.LENGTH_SHORT).show();
        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity());
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
