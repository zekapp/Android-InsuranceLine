package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 24/07/15.
 */
public class MCardDetails implements Serializable{
    private String  cardNumber;              // RequiredData type: CreditCard Matching regular expression pattern: \b\d{16}\b
    private String  cardNumberObfuscated;    // none
    private int     cardType;                /** {@link au.com.lumo.ameego.utils.Constants.CardType}*/
    private int     paymentType;             /** {@link au.com.lumo.ameego.utils.Constants.PaymentType} */
    private String  cardName;                // Required Max length: 64
    private int     expiryMonth;             // Required Range: inclusive between 1 and 12
    private int     expiryYear;              // Required Range: inclusive between 2015 and 2030
    private String  cvv;                     // Required Matching regular expression pattern: (?:[0-9]){3,4} Range: inclusive between 100 and 9999

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumberObfuscated() {
        return cardNumberObfuscated;
    }

    public void setCardNumberObfuscated(String cardNumberObfuscated) {
        this.cardNumberObfuscated = cardNumberObfuscated;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
