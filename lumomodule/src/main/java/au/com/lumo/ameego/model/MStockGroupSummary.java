package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MStockGroupSummary implements Serializable{
    private MStockItem parent;          // The parent stock item
    private int groupFulfilmentOptions; // The list of fulfilment types derived from the parent stock item's children
    private int groupDenominations;     // The list of card face values derived from the stock item's children
}
