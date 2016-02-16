package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MResourse implements Serializable{
    private int    $id;
    private String resourceId;  // The unique identifier for the resource - also the key by which the image or file is requested from the system
    private String role;        // The purpose of this resource - whether it is a thumbnail or a main image etc ...
    private String url;         // The url that should be used to retrieve the resource

    public String getResourceId() {
        return resourceId;
    }

    public String getRole() {
        return role;
    }

    public String getUrl() {
        return url;
    }
}
