package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.callbacks.WarnOptionSelectCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.model.PaymentDetails;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.StringUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;

/**
 * Created by Zeki Guler on 22/07/15.
 */
public class CheckoutFragment extends BaseFragment{
    public static final String TAG = CheckoutFragment.class.getSimpleName();

    private EditText mSuburbEditText;
    private EditText mStateEditText;
    private EditText mEmailEditText;
    private EditText mPostCodeEditText;
    private EditText mLastNameEditText;
    private EditText mFirstNameEditText;
    private EditText mStreetAddressEditText;
    private EditText mContactEditText;

    private PaymentDetails mPaymentDetails;

    public static CheckoutFragment newInstance() {
        return new CheckoutFragment();
    }
    @Override
    protected String getTitle() {
        return getResources().getString(R.string.checkout_title);
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
        return R.layout.fragment_checkout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPaymentDetails = PrefUtils.getPaymentDetails(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSuburbEditText         = (EditText) view.findViewById(R.id.checkout_suburb);
        mStateEditText          = (EditText) view.findViewById(R.id.checkout_state);
        mEmailEditText          = (EditText) view.findViewById(R.id.checkout_email);
        mPostCodeEditText       = (EditText) view.findViewById(R.id.checkout_postcode);
        mLastNameEditText       = (EditText) view.findViewById(R.id.checkout_last_name);
        mFirstNameEditText      = (EditText) view.findViewById(R.id.checkout_first_name);
        mStreetAddressEditText  = (EditText) view.findViewById(R.id.checkout_street_address);
        mContactEditText        = (EditText) view.findViewById(R.id.checkout_contact_number);
        view.findViewById(R.id.footer_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckout(v);
            }
        });
        view.findViewById(R.id.checkout_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStateClicked(v);
            }
        });
        updateFields();
    }

    private void updateFields() {
        PaymentDetails checkout = PrefUtils.getPaymentDetails(getActivity());

        if(checkout == null) return;

        mEmailEditText        .setText(checkout.getContactEmail());
        mStateEditText        .setText(checkout.getState());
        mSuburbEditText       .setText(checkout.getSuburb());
        mContactEditText      .setText(checkout.getPhone());
        mFirstNameEditText    .setText(checkout.getName());
        mLastNameEditText     .setText(checkout.getSurname());
        mPostCodeEditText     .setText(checkout.getPostcode());
        mStreetAddressEditText.setText(checkout.getStreetAd());
    }


    @SuppressWarnings("unused")
    /*@OnClick(R.id.footer_checkout)*/
    public void onCheckout(View view){
        String name     = mFirstNameEditText.getText().toString();
        String surname  = mLastNameEditText.getText().toString();
        String email    = mEmailEditText.getText().toString();
        String state    = mStateEditText.getText().toString();
        String postcode = mPostCodeEditText.getText().toString();
        String suburb   = mSuburbEditText.getText().toString();
        String streetAd = mStreetAddressEditText.getText().toString();
        String phone    = mContactEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError(getString(R.string.error_field_required));
            focusView = mEmailEditText;
            cancel = true;
        } else if (!StringUtils.isEmailValid(email)) {
            mEmailEditText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(name)){
            mFirstNameEditText.setError(getString(R.string.error_field_required));
            focusView = mFirstNameEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(surname)){
            mLastNameEditText.setError(getString(R.string.error_field_required));
            focusView = mLastNameEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(state)){
            mStateEditText.setError(getString(R.string.error_field_required));
            focusView = mStateEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(postcode)){
            mPostCodeEditText.setError(getString(R.string.error_field_required));
            focusView = mPostCodeEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(suburb)){
            mSuburbEditText.setError(getString(R.string.error_field_required));
            focusView = mSuburbEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(streetAd)){
            mStreetAddressEditText.setError(getString(R.string.error_field_required));
            focusView = mStreetAddressEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(phone)){
            mContactEditText.setError(getString(R.string.error_field_required));
            focusView = mContactEditText;
            cancel = true;
        }

        if (phone.length() != 10){
            mContactEditText.setError(getString(R.string.error_size_phone));
            focusView = mContactEditText;
            cancel = true;
        }

        if(cancel){
            Toast.makeText(getActivity(), "Please fill all fields to continue", Toast.LENGTH_LONG).show();
            focusView.requestFocus();
        }else{

            if(mPaymentDetails == null)
                mPaymentDetails = new PaymentDetails();

            mPaymentDetails.setName(name).setSurname(surname).setEmail(email)
                    .setState(state).setPostCode(postcode).setSuburb(suburb)
                    .setStreetAddress(streetAd).setPhone(phone);

            PrefUtils.savePaymentDetails(getActivity(), mPaymentDetails);

            if(mIActivity != null) mIActivity.replaceWith(PaymentDetailsFragment.newInstance());
        }
    }

    /*@OnClick(R.id.checkout_state)*/
    void onStateClicked(final View view){
        final String[] states = getResources().getStringArray(R.array.states);
        WarningUtilsMD.alertDialogOption(getActivity(), "Select State", R.array.states, new WarnOptionSelectCallback() {
            @Override
            public void done(int which) {
                ((EditText) view).setText(states[which]);
            }

            @Override
            public void cancel() {

            }
        });
    }

}
