package au.com.lumo.ameego.callbacks;

import java.util.ArrayList;

/**
 * Created by zeki on 24/11/2014.
 */
public abstract class GenericArrayCallBack<T>{
    public abstract void done(ArrayList<T> list, Exception e);
}
