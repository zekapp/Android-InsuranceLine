package au.com.lumo.ameego.callbacks;

import java.util.List;

/**
 * Created by zeki on 20/04/15.
 */
public abstract class GenericListCallback<T> {
    public abstract void  done(List<T> list, Exception e);
}
