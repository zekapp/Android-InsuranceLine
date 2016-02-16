package au.com.lumo.ameego.model;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MSiteHelper {
    private boolean            success;
    private NodesHelper        nodes;
    private MUser              member;
    private MErrorHelper       errors;

    public boolean isSuccess() {
        return success;
    }

    public MErrorHelper getErrors() {
        return errors;
    }

    public NodesHelper getNodes() {
        return nodes;
    }

    public MUser getMember() {
        return member;
    }
}
