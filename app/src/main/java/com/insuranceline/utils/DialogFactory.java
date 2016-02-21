package com.insuranceline.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.EditText;

import com.insuranceline.R;


public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static Dialog createGenericDialog(Context context,
                                             String title,
                                             String message,
                                             String positiveButton,
                                             String negativeButton,
                                             boolean isCancelable,
                                             DialogInterface.OnClickListener positiveListener,
                                             DialogInterface.OnClickListener negativeListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton(positiveButton, positiveListener)
                .setCancelable(isCancelable)
                .setNegativeButton(negativeButton, negativeListener);
        return alertDialog.create();
    }

    public static Dialog createGenericDialog(Context context,
                                             @StringRes int titleResource,
                                             @StringRes int messageResource,
                                             @StringRes int positiveButtonResource,
                                             @StringRes int negativeButtonResource,
                                             DialogInterface.OnClickListener positiveListener,
                                             DialogInterface.OnClickListener negativeListener) {

        return createGenericDialog(context,
                context.getString(titleResource),
                context.getString(messageResource),
                context.getString(positiveButtonResource),
                context.getString(negativeButtonResource),
                true,
                positiveListener,
                negativeListener);
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }


    public static Dialog createDialogWithEditText(Context context ,EditText input, String title, String message,
                                                  DialogInterface.OnClickListener positiveListener,
                                                  DialogInterface.OnClickListener negativeListener ) {
        return  new AlertDialog.Builder(context)
                .setView(input)
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("Ok", positiveListener)
                .setNegativeButton("Cancel", negativeListener)
                .create();
    }

    public static Dialog createDialogWithOption(Context context ,EditText input, String title, String message,
                                                  DialogInterface.OnClickListener positiveListener,
                                                  DialogInterface.OnClickListener negativeListener ) {
        return  new AlertDialog.Builder(context)
                .setView(input)
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("Ok", positiveListener)
                .setNegativeButton("Cancel", negativeListener)
                .create();
    }
}
