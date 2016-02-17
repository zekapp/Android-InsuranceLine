package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.adapters.RewardsAdapter;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MSiteHelper;
import au.com.lumo.ameego.model.MSiteNodeVm;
import au.com.lumo.ameego.uiUtils.DividerItemDecoration;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.LumoSpecificUtils;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;
import au.com.lumo.ameego.viewHolders.RewardsViewHolder;

/**
 * Created by Zeki Guler on 9/07/15.
 */
public class RewardsFragment extends BaseFragment {

    private final int UNKONW_OPERTION       = -1;
    private final int DIRECT_TO_SUBCATEGORY = 0;
    private final int DIRECT_TO_CUSTOM_PAGE = 1;
    private final int DIRECT_TO_CUSTOM_LINK = 2;
    private final int NO_SUB_CATEGORY       = 3;


    private RewardsAdapter     mAdapter;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static RewardsFragment newInstance() {
        return new RewardsFragment();
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.rewards_categories);
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
        return R.layout.fragment_reward_category;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView       = (RecyclerView) view.findViewById(R.id.simpleList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        initializeAdapter();
        trySetupSwipeRefresh();
        initializeList();
        updateView();
    }

    private void initializeAdapter() {
        if(mAdapter == null) {
            mAdapter = new RewardsAdapter();
            mAdapter.setIRewardAdapterCallback(new CustomCallback());
        }
    }

    private void updateList() {
        mSwipeRefreshLayout.setRefreshing(true);
        NetworkManager.fetchSiteNodeVM(new GenericCallback<MSiteHelper>() {
            @Override
            public void done(MSiteHelper site, Exception e) {
                mSwipeRefreshLayout.setRefreshing(false);
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    try {

//                        updateAndSaveSite(site);
//                        updateAndSaveUser(site);
//
//                        directUserToAccordingToResponse(site);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Toast.makeText(getActivity(), "" + e1.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initializeList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        ArrayList<MSiteNodeVm> list = PrefUtils.getSavedSiteNodeList(getActivity());

        if(list != null && !list.isEmpty()) mAdapter.addItems(list);
        else                                updateList();

        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateView() {
        if(mAdapter != null && !mAdapter.isEmpty()){
//            mEmptyImage.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void trySetupSwipeRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3,
                    R.color.refresh_progress_4);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateList();
                }
            });
        }
    }

    private class CustomCallback implements RewardsAdapter.IRewardAdapter {
        @Override
        public void onCardClicked(View view, int position, RewardsViewHolder viewHolder) {
            MSiteNodeVm site = viewHolder.getItem();

            switch(getNextOp(site)){
                case DIRECT_TO_CUSTOM_LINK: goToCustomLink(site);   break;
                case DIRECT_TO_CUSTOM_PAGE: goToCustomPage(site);   break;
                case DIRECT_TO_SUBCATEGORY: goToSubCategory(site);  break;
                case NO_SUB_CATEGORY:       gotToEmptySubCat(site); break;
                case UNKONW_OPERTION:       unknownOp();            break;

            }
        }

        @Override
        public void onCategoryNameClicked(View view, int position, RewardsViewHolder viewHolder) {
        }
    }

    private void gotToEmptySubCat(MSiteNodeVm site) {
        if(mIActivity != null)
            mIActivity.replaceWith(WebViewFragment.newInstance(Constants.Url.EMPTY_CASE_URL, "No Link"));
    }

    private void unknownOp() {
        Toast.makeText(getActivity(),"Unknown Operation",Toast.LENGTH_SHORT).show();
    }

    private void goToCustomPage(MSiteNodeVm site) {
        Toast.makeText(getActivity()," Not implemented...", Toast.LENGTH_LONG).show();
    }

    private int getNextOp(MSiteNodeVm site) {
        if (site.getCustomLink() != null) return DIRECT_TO_CUSTOM_LINK;
        if (site.getCustomPage() != null) return DIRECT_TO_CUSTOM_PAGE;
        if (hasSubCategories(site))       return DIRECT_TO_SUBCATEGORY;
        else                              return NO_SUB_CATEGORY;
    }

    private boolean hasSubCategories(MSiteNodeVm site) {
        return LumoSpecificUtils.hasItemValidSubCategories(site);
    }

    private void goToCustomLink(MSiteNodeVm site) {
        if(mIActivity != null)
            mIActivity.replaceWith(WebViewFragment.newInstance(site.getCustomLink(), "eSTORE"));
    }

    private void goToSubCategory(MSiteNodeVm site) {
        if(mIActivity != null)
            mIActivity.replaceWith(RewardsSubFragment.newInstance(site.getCategory()));
    }
}
