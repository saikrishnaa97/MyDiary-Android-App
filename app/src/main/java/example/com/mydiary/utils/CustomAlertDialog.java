package example.com.mydiary.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import example.com.mydiary.R;

/**
 * Created by saikr on 30-03-2018.
 */

public class CustomAlertDialog {
    private AlertDialog mAlertDialog;
    private Activity mActivity;

    public CustomAlertDialog(@NonNull Activity mActivity, boolean isCancelable) {
        this.mActivity = mActivity;
        initAlertDialog(mActivity, isCancelable);
    }

    public CustomAlertDialog(@NonNull Activity mActivity, boolean isCancelable, int styleThemeId) {
        this.mActivity = mActivity;
        initAlertDialog(mActivity, isCancelable, styleThemeId);
    }

    private void initAlertDialog(@NonNull Activity mActivity, boolean isCancelable) {
        mAlertDialog = new AlertDialog.Builder(mActivity, getAlertDialogTheme()).create();
        mAlertDialog.setCancelable(isCancelable);
        mAlertDialog.setCanceledOnTouchOutside(true);
    }

    private int getAlertDialogTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return R.style.Theme_AppCompat_Light_Dialog_Alert;
        } else {
            return R.style.Custom_Alert_Dialog_Lolipop;
        }
    }

    private void initAlertDialog(Activity mActivity, boolean isCancelable, int styleThemeId) {
        mAlertDialog =
                new AlertDialog.Builder(new ContextThemeWrapper(mActivity, styleThemeId)).create();
        mAlertDialog.setCancelable(isCancelable);
        mAlertDialog.setCanceledOnTouchOutside(true);
    }

    public void setAlertDialogData(@NonNull String message, @NonNull String title,
                                   @NonNull String positiveButtonText, @NonNull String negativeButtonText,
                                   DialogInterface.OnClickListener onPositiveClickListener,
                                   DialogInterface.OnClickListener onNegativeClickListener) {
        if (mAlertDialog != null) {
            mAlertDialog.setTitle(title);
            mAlertDialog.setMessage(message);
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonText,
                    onPositiveClickListener);
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonText,
                    onNegativeClickListener);
        }
    }

    public void setAlertDialogData(@NonNull String message, @NonNull String positiveButtonText,
                                   DialogInterface.OnClickListener onPositiveClickListener) {
        if (mAlertDialog != null) {
            mAlertDialog.setMessage(message);
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonText,
                    onPositiveClickListener);
        }
    }

    public void setAlertDialogData(@NonNull String message, @NonNull String title,
                                   @NonNull String positiveButtonText, DialogInterface.OnClickListener onPositiveClickListener) {
        if (mAlertDialog != null) {
            mAlertDialog.setTitle(title);
            mAlertDialog.setMessage(message);
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonText,
                    onPositiveClickListener);
        }
    }

    public void setAlertDialogData(@NonNull String message) {
        if (mAlertDialog != null) {
            mAlertDialog.setMessage(message);
        }
    }

    public void showCustomAlertDialog() {
        if (!mActivity.isFinishing() && mAlertDialog != null && !mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    public void showCustomAlertDialog(@NonNull DialogInterface.OnCancelListener onCancelListener) {
        setCancelListener(onCancelListener);
        if (!mActivity.isFinishing() && mAlertDialog != null && !mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    public void hideCustomAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    public void setAlertDialogCancelable(boolean isCancelable) {
        if (mAlertDialog != null) {
            mAlertDialog.setCancelable(isCancelable);
        }
    }

    public void setCancelOutsideTouchListener(boolean isCancelable) {
        if (mAlertDialog != null) {
            mAlertDialog.setCanceledOnTouchOutside(isCancelable);
        }
    }

    public void setCancelListener(@NonNull DialogInterface.OnCancelListener onCancelListener) {
        if (mAlertDialog != null) {
            mAlertDialog.setOnCancelListener(onCancelListener);
        }
    }

    public void setCustomPositiveButton() {
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positive = mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setBackgroundResource(R.drawable.button_shape_login_register);
                positive.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                positive.setMinimumHeight(1);
                positive.setMinHeight(1);
                ViewGroup.LayoutParams lp = positive.getLayoutParams();
                lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                positive.setLayoutParams(lp);
            }
        });
    }

    public void setCustomView(@NonNull View customView) {
        if (mAlertDialog != null) {
            mAlertDialog.setView(customView);
        }
    }

    public Window getCustomAlertDialogWindow() {
        return mAlertDialog != null ? mAlertDialog.getWindow() : null;
    }
}

