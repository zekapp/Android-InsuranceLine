package com.insuranceline.ui.fragments.containers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.insuranceline.R;
import com.insuranceline.di.component.ActivityComponent;
import com.insuranceline.ui.main.MainActivity;


/**
 * Created by Zeki Guler on 2/02/15.
 */
public class BaseContainerFragment extends Fragment {
    public static final String TAG = BaseContainerFragment.class.getSimpleName();

    private Fragment mCurrentFragment;

    private ActivityComponent mActivityComponent;

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null)
            mActivityComponent =((MainActivity) getActivity()).getActivityComponent();
        return mActivityComponent;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        hideKeyboard();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commitAllowingStateLoss();
        getChildFragmentManager().executePendingTransactions();

        mCurrentFragment = fragment;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int animType) {
        hideKeyboard();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commitAllowingStateLoss();
        getChildFragmentManager().executePendingTransactions();

        mCurrentFragment = fragment;
    }

    public boolean popFragment() {
        Log.e(TAG, "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();

        }
        return isPop;
    }

    private void hideKeyboard() {
        Log.d(TAG, "hideKeyboard called");

        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = getActivity().getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getChildFragmentManager().getFragments()) {

            if(fragment != null){
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
