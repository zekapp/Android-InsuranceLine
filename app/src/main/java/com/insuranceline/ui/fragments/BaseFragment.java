package com.insuranceline.ui.fragments;

import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.insuranceline.R;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.main.MainActivity;

import timber.log.Timber;

public class BaseFragment extends Fragment {
    private final String TAG = BaseFragment.class.getSimpleName();

    protected MessageFromFragmentInterface msgToActvInterface;

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

        Toolbar toolbar;
        if(msgToActvInterface!=null){
            msgToActvInterface.showTabHost(true);
            toolbar=msgToActvInterface.getToolBar();
            TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTitle);
            textView.setText("");
            textView.setCompoundDrawables(null,null,null,null);
        }
    }
}