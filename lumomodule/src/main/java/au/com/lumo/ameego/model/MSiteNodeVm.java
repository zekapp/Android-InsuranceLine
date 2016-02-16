package au.com.lumo.ameego.model;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MSiteNodeVm {
    private int               $id;
    private int               siteNodeId;
    private String            text;               // Display text - when present, should override any display text from child elements
    private MSiteNoteHelper   children;           // The list of child nodes for this node
    private int               order;
    private boolean           includeInMenu;      // whether this node should be displayed as part of the menu
    private MCategory         category;           // the category page that this node should generate a link for - if this is present, the custom page and custom link should be blank
    private String            customPage;         // A page within the app that has html content. If this is present, the category and custom link properties should be blank
    private String            customLink;         // A link to a web site outside of this app. If this is present, the category and custom page properties should be

    public int get$id() {
        return $id;
    }

    public int getSiteNodeId() {
        return siteNodeId;
    }

    public String getText() {
        return text;
    }

    public MSiteNoteHelper getChildren() {
        return children;
    }

    public int getOrder() {
        return order;
    }

    public boolean isIncludeInMenu() {
        return includeInMenu;
    }

    public MCategory getCategory() {
        return category;
    }

    public String getCustomLink() {
        return customLink;
    }

    public String getCustomPage() {
        return customPage;
    }
}
