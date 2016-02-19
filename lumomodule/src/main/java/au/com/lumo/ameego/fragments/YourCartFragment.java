package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.adapters.ScopingCartAdapter;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MCartItemInfo;
import au.com.lumo.ameego.model.MShoppingCartItem;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MUserShoppingCart;
import au.com.lumo.ameego.uiUtils.DividerItemDecoration;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.LumoSpecificUtils;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;
import au.com.lumo.ameego.viewHolders.ShoppingCardFooterViewHolder;
import au.com.lumo.ameego.viewHolders.ShoppingCartViewHolder;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Zeki Guler on 10/07/15.
 */
public class YourCartFragment extends BaseFragment{
    public static final String TAG = YourCartFragment.class.getSimpleName();

    private RecyclerView      mRecyclerView;
    private ImageView         mEmptyState;
    private SmoothProgressBar mProgressBar;

    private View                         mFooter;
    private ScopingCartAdapter           mAdapter;
    private MShoppingCartVM              mShoppingCart;
    private ShoppingCardFooterViewHolder mFooterViewHolder;
    private ArrayList<MCartItemInfo>     mCurrentCartItemInfo;
    private TextView                     mSubTotalTv;
    private TextView                     mDeliveryFeeTv;
    private TextView                     mHandlingFeeTv;
    private TextView                     mTotalFeeTv;
    private TextView                     mCreditCardFee;

    public static YourCartFragment newInstance() {
        return new YourCartFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.simpleList);
        mEmptyState   = (ImageView) view.findViewById(R.id.empty_state);
        mProgressBar  = (SmoothProgressBar) view.findViewById(R.id.progress_bar);

        mProgressBar.progressiveStop();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.simpleList);
        mEmptyState   = (ImageView) view.findViewById(R.id.empty_state);
        mProgressBar  = (SmoothProgressBar) view.findViewById(R.id.progress_bar);

        initListView();
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.your_cart);
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
        return R.layout.fragment_your_cart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingCart = PrefUtils.getCart(getActivity());
        mCurrentCartItemInfo = LumoSpecificUtils.getCartItemsInfo(mShoppingCart);
    }

    private ArrayList<MUserShoppingCart> getTestData() {
        ArrayList<MUserShoppingCart> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            MUserShoppingCart userShoppingCart = new MUserShoppingCart();
            userShoppingCart.setCardName("Cart Name is " + i);
            userShoppingCart.setDetails("Cart " + i + " details are bla bla bla ...");
            userShoppingCart.setItemUrl(Constants.getRandTestPicture());
            userShoppingCart.setQuantity((int) (Math.random() * 6));
            userShoppingCart.setPrice("$" + (int)(Math.random()*155));

            list.add(userShoppingCart);
        }

        return list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mFooter == null){
            mFooter         = inflater.inflate(R.layout.shopping_cart_footer,container,false);
        }

        mSubTotalTv     = (TextView         )mFooter.findViewById(R.id.footer_subtotal);
        mDeliveryFeeTv  = (TextView         )mFooter.findViewById(R.id.footer_delivery_fee);
        mHandlingFeeTv  = (TextView         )mFooter.findViewById(R.id.footer_handling_fee);
        mTotalFeeTv     = (TextView         )mFooter.findViewById(R.id.footer_total);
        mCreditCardFee  = (TextView         )mFooter.findViewById(R.id.footer_creditcard_fee);

        mFooter.findViewById(R.id.footer_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), " checkout clicked...", Toast.LENGTH_SHORT).show();
                /**
                 * Karl added
                 */
                if(mShoppingCart.isValid()) {
                    runCheckoutProcess();
                }else{
                    WarningUtilsMD.alertDialogYesNo("Error", mShoppingCart.getErrorMessages().get$values().get(0), getActivity(), "OK", "", true, null);
                }
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void runCheckoutProcess() {
        mAdapter.removeFooterView(1);
        if(mIActivity != null) mIActivity.replaceWith(CheckoutFragment.newInstance());
    }

    private void initListView() {
        if(mShoppingCart == null || mShoppingCart.getShoppingCartItemsHelper() == null){
            updateView();
            return;
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        if(mFooterViewHolder == null)
            mFooterViewHolder = new ShoppingCardFooterViewHolder(mFooter);

        if(mAdapter == null){
            mAdapter = new ScopingCartAdapter(getActivity(), mShoppingCart.getShoppingCartItemsHelper().getItems(), new CustomViewHolderListener());
        }
        mAdapter.addFooterView(1, mFooterViewHolder);
        mRecyclerView.setAdapter(mAdapter);

        updateView();
    }

    private void updateCart() {

        mProgressBar.progressiveStart();

        NetworkManager.updateCart(mCurrentCartItemInfo, new GenericCallback<MShoppingCartVM>() {

            @Override
            public void done(MShoppingCartVM res, Exception e) {

                mProgressBar.progressiveStop();

                if (e == null) {
                    mShoppingCart = res;
                    mCurrentCartItemInfo = LumoSpecificUtils.getCartItemsInfo(mShoppingCart);

                    mIActivity.saveCartToDb(res);

                    mAdapter.updateData(res.getShoppingCartItemsHelper().getItems());

                    updateView();
                } else {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void updateView() {
        if (mAdapter != null && !mAdapter.isEmpty()) {
            updateFooter();
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    private void updateFooter() {
        logCartDetail();

        mSubTotalTv   .setText("$" + String.valueOf(String.format("%.2f", mShoppingCart.getSubTotal())));
        mDeliveryFeeTv.setText("$" + String.valueOf(String.format("%.2f", mShoppingCart.getFreightCharge())));
        mHandlingFeeTv.setText("$" + String.valueOf(String.format("%.2f", mShoppingCart.getHandlingFee())));
        mCreditCardFee.setText("$" + String.valueOf(String.format("%.2f", mShoppingCart.getCreditCardFee())));
        mTotalFeeTv   .setText("$" + String.valueOf(String.format("%.2f", mShoppingCart.getTotal())));
    }

    private void logCartDetail() {
        if (mShoppingCart == null ) return;
//        Log.d(TAG, "getSubTotal         : " + mShoppingCart.getSubTotal());
//        Log.d(TAG, "getFreightCharge    : " + mShoppingCart.getFreightCharge());
//        Log.d(TAG, "getHandlingFee      : " + mShoppingCart.getHandlingFee());
//        Log.d(TAG, "getCreditCardFee    : " + mShoppingCart.getCreditCardFee());
//        Log.d(TAG, "getTotal            : " + mShoppingCart.getTotal());
    }

    private class CustomViewHolderListener implements ShoppingCartViewHolder.IViewHolderClick {
        @Override
        public void onItemClicked(View view, int position, ShoppingCartViewHolder viewHolder) {
//            Toast.makeText(getActivity(), "onItemClicked clicked Postion: " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRemoveItemClicked(View view, int position, ShoppingCartViewHolder viewHolder) {
//            Toast.makeText(getActivity(), "onRemoveItemClicked clicked Postion: " + position, Toast.LENGTH_SHORT).show();
            mAdapter.removeItem(position);
            removeCartItem(viewHolder.getItem());

        }

        @Override
        public void onIncItemClicked(View view, int position, ShoppingCartViewHolder viewHolder) {
//            Toast.makeText(getActivity(), "onIncItemClicked clicked Postion: " + position, Toast.LENGTH_SHORT).show();
            increaseQuantity(viewHolder.getItem());
        }

        @Override
        public void onDecItemClicked(View view, int position, ShoppingCartViewHolder viewHolder) {
//            Toast.makeText(getActivity(), "onDecItemClicked clicked Postion: " + position, Toast.LENGTH_SHORT).show();
            decreaseQuantity(viewHolder.getItem());
        }
    }

    private void removeCartItem(MShoppingCartItem item) {
        Iterator<MCartItemInfo> itr = mCurrentCartItemInfo.iterator();
        while (itr.hasNext()){
            MCartItemInfo itemInfo = itr.next();

            if(itemInfo.getStockItemId() == item.getStockItemId()) itr.remove();
        }

        updateCart();
    }

    private void increaseQuantity(MShoppingCartItem item) {
        for(MCartItemInfo current : mCurrentCartItemInfo){
            if(current.getStockItemId() == item.getStockItemId())
                current.increaseQuantity();
        }

        updateCart();
    }

    private void decreaseQuantity(MShoppingCartItem item) {
        for(MCartItemInfo current : mCurrentCartItemInfo){
            if(current.getStockItemId() == item.getStockItemId())
                current.decreaseQuantity();
        }

        updateCart();
    }
}
