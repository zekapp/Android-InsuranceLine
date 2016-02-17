package au.com.lumo.ameego.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import au.com.lumo.ameego.LumoController;
import au.com.lumo.ameego.DispatchActivity;
import au.com.lumo.ameego.R;
import au.com.lumo.ameego.adapters.RewardsSubAdapter;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.callbacks.WarnYesNoSelectCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MCategory;
import au.com.lumo.ameego.uiUtils.DividerItemDecoration;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.LumoSpecificUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;
import au.com.lumo.ameego.viewHolders.RewardsSubViewHolder;


/**
 * Created by Zeki Guler on 13/07/15.
 */
public class RewardsSubFragment extends BaseFragment{

    private final int UNKONW_OPERTION       = -1;
    private final int DIRECT_TO_REWARD_LIST = 0;
    private final int DIRECT_TO_CUSTOM_LINK = 2;

    private final static String TAG                 = RewardsSubFragment.class.getSimpleName();
    private final static String REWARD_SUB_FRAG_KEY = "reward_sub_frag_key";

    /*@Bind(R.id.simpleList)  */RecyclerView mRecyclerView;
    /*@Bind(R.id.empty_state) */ImageView mEmptyStateImage;

    private static MCategory   mMainCategories;
    private RewardsSubAdapter  mAdapter;

    public static RewardsSubFragment newInstance(MCategory items) {

        RewardsSubFragment fragment = new RewardsSubFragment();
        Bundle             bundle   = new Bundle();

        bundle.putSerializable(REWARD_SUB_FRAG_KEY, items);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) mMainCategories = (MCategory)getArguments().getSerializable(REWARD_SUB_FRAG_KEY);
        else                        mMainCategories = null;

    }

    @Override
    protected String getTitle() {
        return mMainCategories.getCategoryName();
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
        return R.layout.fragment_reward_sub_category;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAdapter();
        setList();
        updateView();
    }

    private void updateView() {
        if(mAdapter != null && !mAdapter.isEmpty()){
            mEmptyStateImage.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }else
            mEmptyStateImage.setVisibility(View.VISIBLE);
    }

    public void setList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initializeAdapter() {
        if (mAdapter == null && mMainCategories != null) {
            mAdapter = new RewardsSubAdapter();
            mAdapter.setIRewardSubAdapterCallback(new CustomCallback());
            mAdapter.addItems(mMainCategories.getChildren().get$values());
        }
    }

    private class CustomCallback implements RewardsSubAdapter.IRewardSubAdapter {
        @Override
        public void onCardClicked(View view, int position, RewardsSubViewHolder viewHolder) {

            MCategory category = viewHolder.getItem();

            if (category.getCustomLink() != null && !category.getCustomLink().isEmpty()){
                goToCustomLink(category);
            }else{
                goRewardList(category);
            }
        }
    }

    private void goToCustomLink(MCategory category) {

        String url;
        if(URLUtil.isValidUrl(category.getCustomLink()))
            url = category.getCustomLink();
        else
            url = Constants.Server.SERVER_URL + category.getCustomLink();

        if(mIActivity != null)
            mIActivity.replaceWith(WebViewFragment.newInstance(url, category.getCategoryName()));
    }

    private void goRewardList(MCategory category) {
        int subCategoryId = category.getCategoryId();
        if(Constants.Config.DEVELOPER_MODE) Toast.makeText(getActivity(),"subCategoryId: " + subCategoryId + " clicked", Toast.LENGTH_LONG).show();

        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity(), false);
        NetworkManager.fetchSubCategory(subCategoryId, new GenericCallback<MCategory>() {
            @Override
            public void done(MCategory res, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    if(LumoSpecificUtils.hasCategoryValidStocks(res)){
                        mIActivity.replaceWith(StockListFragment.newInstance(res));
                    }else{
                        warnAndLogoutUser();
                    }
                } else {
//                    Log.e(TAG, "Error: " + e.getMessage());
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), "Sorry, We couldn't get t, please try later...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void warnAndLogoutUser() {
        WarningUtilsMD.alertDialogYesNo("Session expired", "Your session has expired due to inactivity. Please log in again.", getActivity(), "OK", "", new WarnYesNoSelectCallback() {
            @Override
            public void done(boolean isYes) {
                logout();
            }
        });
    }

    private void logout() {
        LumoController.getInstance().saveUser(null);
        Intent intent = new Intent(getActivity(), DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
