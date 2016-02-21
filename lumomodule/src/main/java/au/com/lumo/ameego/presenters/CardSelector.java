package au.com.lumo.ameego.presenters;

import android.content.Context;

import java.util.ArrayList;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.callbacks.WarnOptionSelectCallback;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MCartItemInfo;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.LumoSpecificUtils;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.StringUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;

/**
 * Created by Zeki Guler on 28/07/15.
 */
public class CardSelector {


    private Context                  mContext;
    private MCartItemInfo            selectedItem;
    private ICardSelectorResponse    mI;


    private static int               selectedType          = Constants.FulfilmentType.DIGITAL; // default
    private static final String      TAG                   = CardSelector.class.getSimpleName();
    private ArrayList<MStockItem>    digitalDelTypeStocks  = new ArrayList<>();
    private ArrayList<MStockItem>    physicalDelTypeStocks = new ArrayList<>();
    private ArrayList<MCartItemInfo> mCartItems;

    public interface ICardSelectorResponse {
        void itemSelected    (MCartItemInfo selectedCard, int deliveryType, String price );
        void cardAddedToStore(MShoppingCartVM shoppingCartOverall);
        void quantityChanged (int quantity);
    }

    public CardSelector(Context activity, MStockItem stockItem, ICardSelectorResponse callback) {
        this.mContext = activity;

        mI = callback;

        getCartFromDb();

        initArrays(stockItem);

        setDefaultType();

        setInitialValues();
    }

    private void getCartFromDb() {
        MShoppingCartVM mShoppingCartItems = PrefUtils.getCart(mContext);
        mCartItems = LumoSpecificUtils.getCartItemsInfo(mShoppingCartItems);
    }

    private void setInitialValues() {
        stockItemSelected(0); // select first item aas default
    }

    private void setDefaultType() {
        if(!digitalDelTypeStocks.isEmpty()) selectedType = Constants.FulfilmentType.DIGITAL;
        else                                selectedType = Constants.FulfilmentType.PHYSICAL;
    }

    private void initArrays(MStockItem stockItem) {
        if(stockItem.getChildren() != null && stockItem.getChildren().getStockItems() != null){

            for(MStockItem item : stockItem.getChildren().getStockItems()){
                if(item == null) continue;

                if(item.getFulfilmentType() == Constants.FulfilmentType.DIGITAL)
                    digitalDelTypeStocks.add(item);
                else
                    physicalDelTypeStocks.add(item);
            }
        }
    }

    private void stockItemSelected(int index){
        if(selectedType == Constants.FulfilmentType.DIGITAL){
            reportThatDigitalItemSelected(index);
        }else{
            reportThatPhysicalItemSelected(index);
        }
    }

    private void reportThatDigitalItemSelected(int itemIndex) {
        if(digitalDelTypeStocks.size() > itemIndex && mI != null){
            MStockItem selectedStockItem = digitalDelTypeStocks.get(itemIndex);
            selectedItem   = convertStockItemToValidFormat(selectedStockItem);
            int delType    = Constants.FulfilmentType.DIGITAL;

            mI.itemSelected(selectedItem, delType, getPrice(selectedStockItem));
        }
    }

    private void reportThatPhysicalItemSelected(int index) {
        if(physicalDelTypeStocks.size() > index && mI != null){
            MStockItem selectedStockItem = physicalDelTypeStocks.get(index);
            selectedItem   = convertStockItemToValidFormat(selectedStockItem);
            int delType    = Constants.FulfilmentType.PHYSICAL;

            mI.itemSelected(selectedItem, delType,  getPrice(selectedStockItem));
        }
    }

    private String getPrice(MStockItem selectedStockItem) {
        if(selectedStockItem.isDisplayNameAsCardType())
            return String.valueOf(selectedStockItem.getName());
        else
            return String.valueOf((int)selectedStockItem.getPurchasePrice());
    }

    private MCartItemInfo convertStockItemToValidFormat(MStockItem stockItem) {
        MCartItemInfo itemInfo = new MCartItemInfo();
        itemInfo.setQuantity(1);
        itemInfo.setStockItemId(stockItem.getStockItemId());
        return itemInfo;
    }

    public void deliveryTypeClicked() {
        final String[] resArray;
        String[] delArray = mContext.getResources().getStringArray(R.array.card_delivery_type);

        if (physicalDelTypeStocks.size() > 0 && digitalDelTypeStocks.size() > 0)
            resArray  = delArray;
        else if(digitalDelTypeStocks.size() > 0){
            resArray = new String[]{delArray[0]};
            selectedType = Constants.FulfilmentType.DIGITAL;
        }
        else{
            resArray = new String[]{delArray[1]};
            selectedType = Constants.FulfilmentType.PHYSICAL;
        }

        WarningUtilsMD.alertDialogOption(mContext, mContext.getString(R.string.delivery_type_tite), resArray, new WarnOptionSelectCallback() {
            @Override
            public void done(int which) {
                if(resArray.length == 2){
                    switch (which) {
                        case 0:
                            selectedType = Constants.FulfilmentType.DIGITAL;
                            break;
                        case 1:
                            selectedType = Constants.FulfilmentType.PHYSICAL;
                            break;
                    }
                }

                stockItemSelected(0); // set first item as default
            }

            @Override
            public void cancel() {

            }
        });
    }


    public void priceClicked() {
        String[] priceList = getPriceListAccordingToTheSelectedType();
        WarningUtilsMD.alertDialogOption(mContext, mContext.getString(R.string.price_tite), priceList, new WarnOptionSelectCallback(){

            @Override
            public void done(int which) {
                stockItemSelected(which);
            }

            @Override
            public void cancel() {

            }
        });
    }

    public void cardCountClicked(){
        String[] countList = mContext.getResources().getStringArray(R.array.card_count);
        WarningUtilsMD.alertDialogOption(mContext, mContext.getString(R.string.count_tite), countList, new WarnOptionSelectCallback(){

            @Override
            public void done(int which) {
                selectedItem.setQuantity(which + 1);
                if( mI != null) mI.quantityChanged(selectedItem.getQuantity());

            }

            @Override
            public void cancel() {

            }
        });
    }

    private String[] getPriceListAccordingToTheSelectedType() {
        String[] res;
        if(selectedType == Constants.FulfilmentType.DIGITAL){
            res = new String[digitalDelTypeStocks.size()];
            int i = 0;
            for(MStockItem item : digitalDelTypeStocks){
                if(item.isDisplayNameAsCardType())
                    res[i] = String.valueOf(item.getName());
                else
                    res[i] = String.valueOf(StringUtils.formatDoubleValue(item.getPurchasePrice()));
                i++;
            }
        }else{
            res = new String[physicalDelTypeStocks.size()];
            int i = 0;
            for(MStockItem item : physicalDelTypeStocks){
                if(item.isDisplayNameAsCardType())
                    res[i] = String.valueOf(item.getName());
                else
                    res[i] = String.valueOf(StringUtils.formatDoubleValue(item.getPurchasePrice()));
                i++;
            }
        }

        return res;
    }

    public void increaseQuantityByOne() {
        selectedItem.increaseQuantity();
        if( mI != null) mI.quantityChanged(selectedItem.getQuantity());
    }

    public void decreaseQuantityByOne() {
        selectedItem.decreaseQuantity();
        if( mI != null ) mI.quantityChanged(selectedItem.getQuantity());
    }

    public void addToCart() {
        mCartItems.add(selectedItem);

        notifiyServer();
    }

    private void notifiyServer() {
        WarningUtilsMD.startProgresslDialog("Adding product to cart...", mContext);
        NetworkManager.updateCart(mCartItems, new GenericCallback<MShoppingCartVM>() {
            @Override
            public void done(MShoppingCartVM shoppingCar, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    mI.cardAddedToStore(shoppingCar);
                } else {
//                    Log.d(TAG, "Error: " + e.getMessage());
//                    Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
