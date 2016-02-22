package com.insuranceline.ui.fragments.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.ui.login.connect.FBConnectActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MoreFragment extends BaseFragment {

    @Inject
    DataManager mDataManager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("More");
//        setHasOptionsMenu(true);
    }

    @OnClick(R.id.more_1)
    public void more1(){
        Intent intent = new Intent(getActivity(), FBConnectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.more_2)
    public void more2(){

    }

    @OnClick(R.id.more_3)
    public void more3(){

    }

    @OnClick(R.id.more_4)
    public void more4(){

    }

    @OnClick(R.id.more_5)
    public void more5(){
        Toast.makeText(mContext,"Ok",Toast.LENGTH_LONG).show();
        startFragment(AboutFragment.getInstance());
    }

    @OnClick(R.id.more_6)
    public void more6(){

    }

    @OnClick(R.id.more_7)
    public void more7(){
        mDataManager.deleteEdgeUser();
        Intent intent = new Intent(getActivity(), DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            openTestFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openTestFragment() {

        startFragment(DebugModeFragment.getInstance());

/*
        final EditText input = new EditText(getActivity());
        input.setHeight(100);
        input.setWidth(140);
        input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setGravity(Gravity.LEFT);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);


        DialogFactory.createDialogWithEditText
                (getActivity(), input, "Test Alert", "Set test Goal.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String target = input.getText().toString();
                                try{
                                    mDashboardPresenter.resetGoal(Integer.valueOf(target));
                                }catch (Exception e){
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .show();
*/
    }
}
