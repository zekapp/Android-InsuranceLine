package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MCategory implements Serializable {
    private int               $id;
    private int               categoryId;
    private int               categoryLinkBehaviour;  /** {@link au.com.lumo.ameego.utils.Constants.LinkBehaviour}    */
    private String            headerImageId;          // the id of the link to the category header image
    private String            subCategoryImageId;     // the id of the link to the subcategory image
    private String            customLink;             // a customised link to the category
    private MMerchantsHelper  merchants;              // the list of merchants that have rewards associated with this category
    private MStockItemsHelper stockItems;             // the list of stock items associated with this category
    private MCategoryHelper   children;               // sub categories
    private String            categoryName;           // the name of the category

    public int get$id() {
        return $id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getCategoryLinkBehaviour() {
        return categoryLinkBehaviour;
    }

    public String getHeaderImageId() {
        return headerImageId;
    }

    public String getSubCategoryImageId() {
        return subCategoryImageId;
    }

    public String getCustomLink() {
        return customLink;
    }

    public MMerchantsHelper getMerchantsHelper() {
        return merchants;
    }

    public MStockItemsHelper getStockItemsHelper() {
        return stockItems;
    }

    public MCategoryHelper  getChildren() {
        return children;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
