package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.callbacks.WarnYesNoSelectCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MAddress;
import au.com.lumo.ameego.model.MCardDetails;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.PaymentDetails;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;

/**
 * Created by Zeki Guler on 22/07/15.
 */
public class ConfirmationFragment extends BaseFragment{
    public static final String TAG = ConfirmationFragment.class.getSimpleName();

    /*@Bind(R.id.conf_card_type)   */TextView mCardTypeTv;
    /*@Bind(R.id.conf_card_name)   */TextView mCardNameTv;
    /*@Bind(R.id.conf_card_number) */TextView mCardNumberTv;
    /*@Bind(R.id.conf_expiry)      */TextView mCardExpiryDateTv;
    /*@Bind(R.id.conf_ccv)         */TextView mCardCCVTv;
    /*@Bind(R.id.conf_user_name)   */TextView mUserNameTv;
    /*@Bind(R.id.conf_address)     */TextView mUserAddressTv;
    /*@Bind(R.id.conf_email)       */TextView mUserEmailTv;
    /*@Bind(R.id.conf_total_cost)  */TextView mTotalCostTv;
    /*@Bind(R.id.conf_phone)       */TextView mPhoneTv;



    private PaymentDetails paymentDetails;
    private MShoppingCartVM mMShoppingCartVM;

    public static ConfirmationFragment newInstance() {
        return new ConfirmationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentDetails   = PrefUtils.getPaymentDetails(getActivity());
        mMShoppingCartVM = PrefUtils.getCart(getActivity());
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.confirmation_title);
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_confirmation;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateFields();
    }

    private void updateFields() {
        if(paymentDetails == null ||
                paymentDetails.getCardDetails() == null ||
                    paymentDetails.getDeliveryAddress() == null)  return;

        MAddress address        = paymentDetails.getDeliveryAddress();
        MCardDetails cardDetails = paymentDetails.getCardDetails();


        String[] cardTypes = getResources().getStringArray(R.array.card_type);

        mCardTypeTv      .setText(cardTypes[cardDetails.getCardType() >> 1]);
        mCardNameTv      .setText(cardDetails.getCardName());
        mCardNumberTv    .setText(String.valueOf(cardDetails.getCardNumber()));
        mCardExpiryDateTv.setText(cardDetails.getExpiryMonth() + "/" + cardDetails.getExpiryYear());
        mCardCCVTv       .setText(String.valueOf(cardDetails.getCvv()));
        mUserNameTv      .setText(paymentDetails.getName() + " " + paymentDetails.getSurname());
        mUserAddressTv   .setText(address.getAddress1() + ", " + address.getSuburb() + ", " + address.getState() + ", " + address.getPostCode());
        mUserEmailTv     .setText(paymentDetails.getContactEmail());
        mTotalCostTv     .setText(mMShoppingCartVM != null ? "$" + String.valueOf(String.format("%.2f", mMShoppingCartVM.getTotal())) : "-");
        mPhoneTv         .setText(paymentDetails.getContactPhoneNumber());
    }

    /*@OnClick(R.id.confirmation_button)*/
    void onConfirmClicked(View view){
        checkCartHasSomeStuffs();
    }

    private void checkCartHasSomeStuffs() {
        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity(), false );
        NetworkManager.getLatestCart(new GenericCallback<MShoppingCartVM>() {
            @Override
            public void done(MShoppingCartVM cart, Exception e) {
                if (e == null) {
                    mIActivity.saveCartToDb(cart);
                    processPaymentIfCartIsNotEmpty(cart);
                } else {
                    WarningUtilsMD.stopProgress();
//                    Log.d(TAG, "" + e.getMessage());
                }
            }
        });
    }

    private void processPaymentIfCartIsNotEmpty(MShoppingCartVM cart) {
        if (hasCartSomeItems(cart)) pay();
        else                        removeFragment();
    }

    private void pay() {
        NetworkManager.pay(paymentDetails, new GenericCallback<MShoppingCartVM>() {
            @Override
            public void done(MShoppingCartVM res, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    warnUserThatPaymentDone();
                } else {
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void warnUserThatPaymentDone() {
        String title = "Payment successful";
        String body  = "Thank you. Your payment has been successfully received. Your rewards will be dispatched shortly.";

        PrefUtils.deletePaymentDetails(getActivity());

        WarningUtilsMD.alertDialogYesNo(title, body, getActivity(), "Done", "", false, new WarnYesNoSelectCallback() {
            @Override
            public void done(boolean isYes) {
                clearCart();
            }
        });
    }

    private void clearCart() {
        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity(), false );
        NetworkManager.getLatestCart(new GenericCallback<MShoppingCartVM>() {
            @Override
            public void done(MShoppingCartVM cart, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    mIActivity.saveCartToDb(cart);
                    removeFragment();
                } else {
                    WarningUtilsMD.stopProgress();
//                    Log.d(TAG, "" + e.getMessage());
                }
            }
        });
    }

    private void removeFragment() {
        //cart is empty. No item can be buy. Remove fragment.
        WarningUtilsMD.stopProgress();
        mIActivity.replaceWith(YourCartFragment.newInstance());
    }

    private boolean hasCartSomeItems(MShoppingCartVM cart) {
        return  cart != null &&
                cart.getShoppingCartItemsHelper() != null &&
                cart.getShoppingCartItemsHelper().getItems() != null &&
                cart.getShoppingCartItemsHelper().getItems().size() > 0;
    }

}
