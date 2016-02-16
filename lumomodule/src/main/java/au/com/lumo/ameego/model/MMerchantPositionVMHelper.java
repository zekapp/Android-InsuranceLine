package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by zeki on 26/07/15.
 */
public class MMerchantPositionVMHelper implements Serializable{
    private int                           $id;
    private MMerchantPositionVMCollection places;
    private boolean                       success;
    private MErrorHelper                  errors;

    public MMerchantPositionVMCollection getPlacesHelper() {
        return places;
    }

    public boolean isSuccess() {
        return success;
    }

    public MErrorHelper getErrors() {
        return errors;
    }
}
