package au.com.lumo.ameego.model;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class MListItem {

    private int     $id;
    private String  listCode;           // The code - this is the response to the question and is the value that is added to the Code attribute of the ResponseItemVM value
    private String  listText;           // The text to display to the respondent adjacent to this selection item
    private boolean forceSinglePunch;   // Whether selection of this response value is mutually exclusive of all other responses to this question.
    private boolean isLabel;            // Whether this list item works as a list divisor - i.e. devides a list up into "sub lists"

    private boolean isItemSelected = false;

    public String getListCode() {
        return listCode;
    }

    public String getListText() {
        return listText;
    }

    public boolean isForceSinglePunch() {
        return forceSinglePunch;
    }

    public boolean isLabel() {
        return isLabel;
    }

    public boolean isItemSelected() {
        return isItemSelected;
    }

    public void setItemSelected(boolean isItemSelected) {
        this.isItemSelected = isItemSelected;
    }
}
