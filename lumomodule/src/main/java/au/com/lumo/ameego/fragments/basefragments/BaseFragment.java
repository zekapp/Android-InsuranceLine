package au.com.lumo.ameego.fragments.basefragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.com.lumo.ameego.BaseActivity;
import au.com.lumo.ameego.R;
import au.com.lumo.ameego.interfaces.IMessageToActivity;


/**
 * Created by zeki on 8/05/15.
 */
public abstract class BaseFragment extends Fragment {
    private final String TAG = BaseFragment.class.getSimpleName();

    protected IMessageToActivity mIActivity;

    Toolbar mToolbar;

    public BaseActivity getBaseActivity(){
        return ((BaseActivity) super.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);
    }

    @Override
    public void onAttach(Activity activity) {
//        Log.d("BaseFragment", "onAttach");
        super.onAttach(activity);
        try {
            if(activity instanceof BaseActivity) {
                mIActivity = ((BaseActivity) activity).getFragmentListener();
            }

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    protected void setTitle(String title){
        if(getActivity() != null)
            getActivity().setTitle(title);
    }

    protected void setToolbar(View view) {
        if(!hasCustomToolbar()) return;
        mToolbar = (Toolbar) view.findViewById(getToolbarId());
        mToolbar.setTitle(getTitle());
        mToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIActivity.openDrawer();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() != null)
            getActivity().invalidateOptionsMenu();

        setHasOptionsMenu(hasFragOptionsMenu());
    }

    protected @IdRes
    int getToolbarId(){
        return R.id.toolbar;
    }

    protected boolean hasFragOptionsMenu() {
        return false;
    }

    public boolean hasCustomToolbar(){
        return false;
    }

    protected String getTitle(){
        return getResources().getString(R.string.not_title_set);
    }

    protected abstract  @LayoutRes int getLayout();
}
