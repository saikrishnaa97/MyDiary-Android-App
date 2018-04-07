package example.com.mydiary.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import example.com.mydiary.R;

/**
 * Created by saikr on 30-03-2018.
 */

public class CustomProgressDialog {

    private ProgressDialog mProgressDialog;
    private Activity mActivity;

    /**
     * @param activity is activity reference
     * @param messageResourceId message id from string file
     */
    public CustomProgressDialog(Activity activity, int messageResourceId, boolean isCancelable) {
        this.mActivity = activity;
        init(activity, messageResourceId, isCancelable);
    }

    /**
     * @param context is activity reference
     * @param messageResourceId message id from string file
     */
    public void init(Context context, int messageResourceId, boolean isCancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        int style;
        if (messageResourceId > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style = android.R.style.Theme_Material_Light_Dialog;
        } else {
            // noinspection deprecation
            style = R.style.MyTheme;
        }

        mProgressDialog = new ProgressDialog(context, style);
        if (messageResourceId > 0) {
            mProgressDialog.setMessage(context.getResources().getString(messageResourceId));
        }
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * method to dismiss dialog
     */
    public void dismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * method to show dialog
     */
    public void show(DialogInterface.OnCancelListener onCancelListener) {
        if (!mActivity.isFinishing() && mProgressDialog != null && !mProgressDialog.isShowing()) {
            if (onCancelListener != null) {
                mProgressDialog.setOnCancelListener(onCancelListener);
            }
            mProgressDialog.show();
        }
    }

    /**
     * method to show the progressDialog which has no onCancelListener
     */
    public void show() {
        show(null);
    }

    /**
     * method to set the custom message on progressDialog
     *
     * @param message is the desired message which we want to show on progressDialog
     */
    public void setProgressDialogMessage(String message) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(message);
        }
    }

    /**
     * method to set the Cancelable true or false for progressDialog
     *
     * @param isCancelable is the desired boolean value which we want to set for progressDialog
     */
    public void setProgressDialogCancelable(boolean isCancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.setCancelable(isCancelable);
        }
    }

}
