package com.insuranceline.ui.fragments;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.insuranceline.R;
import com.insuranceline.di.component.ActivityComponent;
import com.insuranceline.di.qualifier.ActivityContext;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.fragments.containers.BaseContainerFragment;
import com.insuranceline.ui.main.MainActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class BaseFragment extends Fragment {
    private final String TAG = BaseFragment.class.getSimpleName();

    protected MessageFromFragmentInterface msgToActvInterface;

    private ActivityComponent mActivityComponent;

    private Toolbar mToolbar;

    @Inject
    @ActivityContext
    protected Context mContext;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivityComponent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Timber.d("MainActivity onAttach");

        try {
            if(context instanceof MainActivity) {
                msgToActvInterface = ((MainActivity) context);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    protected void setTitle(String title){
        if(getActivity() != null)
            getActivity().setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() != null)
            getActivity().invalidateOptionsMenu();

        if(msgToActvInterface!=null){
            msgToActvInterface.showTabHost(true);
            mToolbar = msgToActvInterface.getToolBar();
            mToolbar.setNavigationIcon(null);
            mToolbar.setNavigationOnClickListener(null);
            TextView textView = (TextView)mToolbar.findViewById(R.id.toolbarTitle);
            textView.setText("");
            textView.setCompoundDrawables(null,null,null,null);
        }
    }

    public void startFragment(Fragment frag){
        ((BaseContainerFragment) getParentFragment()).replaceFragment(frag, true);
    }

    // call this method after fragment onResume called
    public void setHomeAsUpEnabled(@DrawableRes int id){
        mToolbar.setNavigationIcon(id);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgToActvInterface.onBackPresses(BaseFragment.this);
            }
        });
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null)
            mActivityComponent =((BaseActivity) getActivity()).getActivityComponent();
        return mActivityComponent;
    }
}