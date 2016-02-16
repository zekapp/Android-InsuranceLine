package au.com.lumo.ameego.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;

import au.com.lumo.ameego.callbacks.WarnOptionSelectCallback;
import au.com.lumo.ameego.callbacks.WarnYesNoSelectCallback;

/**
 * Created by zeki on 19/05/15.
 */
public class WarningUtilsMD {

    private static ProgressDialog mProcessDialog;// = DialogFactory.createProgressDialog(getActivity(), "Please wait...");

    public static void startProgresslDialog(String content, Context context){
        mProcessDialog = new ProgressDialog(context);
        mProcessDialog.setMessage(content);
        mProcessDialog.show();

/*        materialDialog = new MaterialDialog.Builder(context)
                .content(content)
                .progress(true, 0)
                .show();*/
    }

    public static void startProgresslDialog(String content, Context context, boolean isCancelable){
        mProcessDialog = new ProgressDialog(context);
        mProcessDialog.setMessage(content);
        mProcessDialog.setCancelable(isCancelable);
        mProcessDialog.show();
    }

    public static void stopProgress(){

        try {
            if ((mProcessDialog != null) && mProcessDialog.isShowing()) {
                mProcessDialog.dismiss();
            }
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            mProcessDialog = null;
        }
    }


    public static void alertDialogYesNo(String title, String message, final Context context,
                                        String positiveButtonText, String negativeButtonText,
                                        final WarnYesNoSelectCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.done(true);
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.done(false);
                    }
                })
                .create()
                .show();

/*        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(positiveButtonText)
                .negativeText(negativeButtonText)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        if (callback != null)
                            callback.done(true);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        if (callback != null)
                            callback.done(false);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                    }
                })
                .show();*/
    }

    public static void alertDialogYesNo(String title, String message, final Context context,
                                        String positiveButtonText, String negativeButtonText, boolean cancelable,
                                        final WarnYesNoSelectCallback callback) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.done(true);
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.done(false);
                    }
                })
                .create()
                .show();
/*
        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(positiveButtonText)
                .cancelable(cancelable)
                .negativeText(negativeButtonText)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        if (callback != null)
                            callback.done(true);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        if (callback != null)
                            callback.done(false);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                    }
                })
                .show();*/
    }

    public static void alertDialogOption(Context context, String title, @ArrayRes int arrayId, final WarnOptionSelectCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(arrayId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.done(which);
                    }
                })
                .create()
                .show();

/*        new MaterialDialog.Builder(context)
                .title(title)
                .items(arrayId)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (callback != null)
                            callback.done(which);
                    }
                })
                .show();*/
    }

    public static void alertDialogOption(Context context, String title, String[] array, final WarnOptionSelectCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.done(which);
                    }
                })
                .create()
                .show();

/*        new MaterialDialog.Builder(context)
                .title(title)
                .items(array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(callback !=null)
                            callback.done(which);
                    }
                })
                .show();*/
    }
}
