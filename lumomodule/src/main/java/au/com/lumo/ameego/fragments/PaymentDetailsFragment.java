package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.callbacks.WarnOptionSelectCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.model.PaymentDetails;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;

/**
 * Created by Zeki Guler on 22/07/15.
 */
public class PaymentDetailsFragment extends BaseFragment {
    public static final String TAG = PaymentDetailsFragment.class.getSimpleName();

    private  EditText mNameOnCardEt;
    private  EditText mCardNumberEt;
    private  EditText mCardCvvEt;
    private  EditText mCardTypeEt;
    private  EditText mCardExpMonthEt;
    private  EditText mCardExpYearEt;

    private PaymentDetails mPaymentDetail;

    private int mCardType = 1;      // Default is VISA
    private int mExpMonth = 1;      // Default is January
    private int mExpYear = 2015;   // Default is 2015

    public static PaymentDetailsFragment newInstance() {
        return new PaymentDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaymentDetail = PrefUtils.getPaymentDetails(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameOnCardEt    = (EditText) view.findViewById(R.id.detail_name_on_card);
        mCardNumberEt    = (EditText) view.findViewById(R.id.detail_card_number);
        mCardCvvEt       = (EditText) view.findViewById(R.id.detail_cvv);
        mCardTypeEt      = (EditText) view.findViewById(R.id.detail_card_type);
        mCardExpMonthEt  = (EditText) view.findViewById(R.id.detail_card_expiry);
        mCardExpYearEt   = (EditText) view.findViewById(R.id.detail_card_exp_year);

        view.findViewById(R.id.detail_card_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardType(v);
            }
        });

        view.findViewById(R.id.detail_card_expiry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onExpMont(v);
            }
        });
        view.findViewById(R.id.detail_card_exp_year).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onExpYear(v);
            }
        });
        view.findViewById(R.id.detail_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onNextClicked(v);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        changeCardNumberLength();
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.payment_detail_title);
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
        return R.layout.fragment_payment_detail;
    }

    /*@OnClick(R.id.detail_card_type)*/
    void onCardType(final View view) {
        final String[] cardTypes = getResources().getStringArray(R.array.card_type);
        WarningUtilsMD.alertDialogOption(getActivity(), "Select Card Type", R.array.card_type, new WarnOptionSelectCallback() {
            @Override
            public void done(int which) {
                ((EditText) view).setText(cardTypes[which]);
                mCardType = 1 << which;
//                Log.d(TAG, "Card Type: " + mCardType);

                changeCardNumberLength();

                mCardNumberEt.setText("");
                mCardCvvEt.setText("");

            }

            @Override
            public void cancel() {

            }
        });
    }

    private void changeCardNumberLength(){
        /**
         *  Karl added
         */
        int cardNumberLength = 16;
        int cvvLength = 3;
        if (mCardType == 4) {//AMEX
            cardNumberLength = 15;
            cvvLength = 4;
        }
        InputFilter[] numberArray = new InputFilter[1];
        numberArray[0] = new InputFilter.LengthFilter(cardNumberLength);
        mCardNumberEt.setFilters(numberArray);
        InputFilter[] cvvArray = new InputFilter[1];
        cvvArray[0] = new InputFilter.LengthFilter(cvvLength);
        mCardCvvEt.setFilters(cvvArray);
    }

    /*@OnClick(R.id.detail_card_exp_month)*/
    void onExpMont(final View view) {
        final String[] months = getResources().getStringArray(R.array.months_unmber);
        WarningUtilsMD.alertDialogOption(getActivity(), "Select Card Expiry Month", R.array.months_unmber, new WarnOptionSelectCallback() {
            @Override
            public void done(int which) {
//                ((EditText) view).setText(months[which]);

                mExpMonth = which + 1;

                onExpYear(view);
//                Log.d(TAG, "Card Exp Month: " + mExpMonth);
            }

            @Override
            public void cancel() {

            }
        });
    }

    /*@OnClick(R.id.detail_card_exp_year)*/
    void onExpYear(final View view) {
        final String[] years = getResources().getStringArray(R.array.years);
        WarningUtilsMD.alertDialogOption(getActivity(), "Select Card Expiry Year", R.array.years, new WarnOptionSelectCallback() {
            @Override
            public void done(int which) {
                String year = years[which];
                String month = String.valueOf(mExpMonth);

                ((EditText) view).setText(String.format("%s/%s",month,year));



                mExpYear = 2015 + which;
//                Log.d(TAG, "Card Exp Year: " + mExpYear);
            }

            @Override
            public void cancel() {

            }
        });
    }

    /*@OnClick(R.id.detail_next)*/
    void onNextClicked(View view) {
        String nameOnCard = mNameOnCardEt.getText().toString();
        String cardNumber = mCardNumberEt.getText().toString();
        String cardCvv = mCardCvvEt.getText().toString();
        String cardType = mCardTypeEt.getText().toString();
//        String expMonth = mCardExpMonthEt.getText().toString();
//        String expYear = mCardExpYearEt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nameOnCard)) {
            mNameOnCardEt.setError(getString(R.string.error_field_required));
            focusView = mNameOnCardEt;
            cancel = true;
        }

        if (TextUtils.isEmpty(cardNumber)) {
            mCardNumberEt.setError(getString(R.string.error_field_required));
            focusView = mCardNumberEt;
            cancel = true;
        }

        if (cardNumber.length() != 16 && mCardType != 4) {
            mCardNumberEt.setError(getString(R.string.error_size_card_number));
            focusView = mCardNumberEt;
            cancel = true;
        }

        if (cardNumber.length() != 15 && mCardType == 4) {
            mCardNumberEt.setError(getString(R.string.error_size_card_number2));
            focusView = mCardNumberEt;
            cancel = true;
        }


        if (cardCvv.length() != 3 && mCardType != 4) {
            mCardCvvEt.setError(getString(R.string.error_size_cvv_number));
            focusView = mCardCvvEt;
            cancel = true;
        }

        if (cardCvv.length() != 4 && mCardType == 4) {
            mCardCvvEt.setError(getString(R.string.error_size_cvv_number2));
            focusView = mCardCvvEt;
            cancel = true;
        }

        if (TextUtils.isEmpty(cardType)) {
            mCardTypeEt.setError(getString(R.string.error_field_required));
            focusView = mCardTypeEt;
            cancel = true;
        }

        if (TextUtils.isEmpty(String.valueOf(mExpMonth))) {
            mCardExpMonthEt.setError(getString(R.string.error_field_required));
            focusView = mCardExpMonthEt;
            cancel = true;
        }

        if (TextUtils.isEmpty(String.valueOf(mExpYear))) {
            mCardExpYearEt.setError(getString(R.string.error_field_required));
            focusView = mCardExpYearEt;
            cancel = true;
        }

        if (cancel) {
            Toast.makeText(getActivity(), "Please fill all fields to continue", Toast.LENGTH_LONG).show();
            focusView.requestFocus();
        } else {
            mPaymentDetail.setNameOnCard(nameOnCard)
                    .setCardNumber(cardNumber)
                    .setCardCCV(String.valueOf(cardCvv))
                    .setCardType(mCardType)
                    .setExpMonth(mExpMonth)
                    .setExpYear(mExpYear);

            PrefUtils.savePaymentDetails(getActivity(), mPaymentDetail);

            if (mIActivity != null) mIActivity.replaceWith(ConfirmationFragment.newInstance());
        }

    }
}
