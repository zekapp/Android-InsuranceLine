package au.com.lumo.ameego.utils;

import java.util.ArrayList;

import au.com.lumo.ameego.model.MCategory;
import au.com.lumo.ameego.model.MStockItem;

/**
 * Created by Zeki Guler on 16/07/15.
 */
public class TestDataHelper {
    public static MCategory getTestCategoryData(MCategory item) {
        ArrayList<MStockItem> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add(new MStockItem());
        }
        item.getStockItemsHelper().setStockItems(list);

        return item;
    }
}
