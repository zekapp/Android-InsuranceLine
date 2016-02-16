package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by zeki on 3/08/15.
 */
public class MResponseItem implements Serializable{
    private String  code;
    private String  stringValue;
    private boolean booleanValue;
    private double  doubleValue;
    private int     integerValue;
    private String  dateTimeValue;

    public void setDateTimeValue(String dateTimeValue) {
        this.dateTimeValue = dateTimeValue;
    }

    public void setIntegerValue(int integerValue) {
        this.integerValue = integerValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
