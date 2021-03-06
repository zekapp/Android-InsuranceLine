package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.view.View;


import au.com.lumo.ameego.R;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.utils.Constants;

/**
 * Created by Zeki Guler on 18/02/2016.
 */
public class MoreLumoFragment extends BaseFragment {

    public static MoreLumoFragment newInstance() {
        return new MoreLumoFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_more_lumo;
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.more);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.item_more_my_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIActivity != null)
                    mIActivity.replaceWith(WebViewFragment.newInstance(Constants.Url.MY_ACCOUNT, getResources().getString(R.string.lumo_my_account)));
            }
        });
        view.findViewById(R.id.item_more_settings_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIActivity != null)
                    mIActivity.replaceWith(SettingsFragment.newInstance());
            }
        });
        view.findViewById(R.id.item_more_insurance_website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIActivity != null)
                    mIActivity.replaceWith(WebViewFragment.newInstance(Constants.Url.INSURANCE_LINE_WEB, getResources().getString(R.string.insurance_website)));
            }
        });


    }

}
