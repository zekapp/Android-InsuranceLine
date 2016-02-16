package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 24/07/15.
 */
public class MCourierType implements Serializable{
    private int     courierTypeId;
    private String  courierName;    // The display name of the courier / mail type
    private double  basePrice;      // the default cost to the member of this service
    private String  information;    // human readable information about how this service operates Max length: 2048
}
