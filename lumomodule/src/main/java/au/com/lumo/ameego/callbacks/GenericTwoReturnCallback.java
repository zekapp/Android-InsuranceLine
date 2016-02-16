package au.com.lumo.ameego.callbacks;

/**
 * Created by Zeki Guler on 27/07/15.
 */
public abstract class GenericTwoReturnCallback<T,K> {
    public abstract void done(T t, K k, Exception e);
}
