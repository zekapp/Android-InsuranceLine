package au.com.lumo.ameego.interfaces;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.model.MShoppingCartVM;


/**
 * Created by zeki on 8/05/15.
 */
public interface IMessageToActivity {

    /**
     * This function called, if fragment has back button on toolbar/action bar call this function
     *
     * @param currentfragment fragment iteself
     * */
    void    onBackPresses(Fragment currentfragment);

    /**
     * This function is called to get current tool. You can use getActivity#getsupported action
     * instead of this function
     *
     * */
    Toolbar getToolBar();

    /**
     * This function is usefull to get common share bundle. For example get bundle and
     * set it some key so an other fragment may use this bundle to future process. It is
     * a kind of communication between fragments
     *
     * */
    Bundle getShareBundle();

    /**
     * This function is for to replace the current fragment. Current fragment sends to the
     * stack.
     *
     * @param fragment the fragment which
     * */
    void replaceWith(BaseFragment fragment);

    void setNewRoot(BaseFragment fragment);

    /**
     * This function open left side drawer
     *
     * */
    void openDrawer();

    /**
     * This function gets the current location from activity
     * */
    Location getCurrentLocation();

    void saveCartToDb(MShoppingCartVM shoppingOverall);

}