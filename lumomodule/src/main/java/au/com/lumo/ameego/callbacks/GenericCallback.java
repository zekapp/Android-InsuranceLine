package au.com.lumo.ameego.callbacks;

/**
 * Created by zeki on 24/11/2014.
 */
public abstract class GenericCallback<T> {
    public abstract void done(T res, Exception e);
}
