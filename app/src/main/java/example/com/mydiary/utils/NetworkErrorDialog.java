package example.com.mydiary.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import example.com.mydiary.R;
import example.com.mydiary.databinding.ServerInternetErrorLayoutBinding;

/**
 * Created by saikr on 30-03-2018.
 */

public class NetworkErrorDialog extends Dialog {
    private NetworkErrorDialog.RetryClick mClick;
    private ServerInternetErrorLayoutBinding mServerInternetErrorLayoutBinding;
    private boolean isNetworkConnectionAvailable = false;
    private ConnectionDetector connectionDetector;
    private Activity mActivity;

    public NetworkErrorDialog(@NonNull Activity context) {
        super(context, R.style.full_screen_maintenance_dialog);
        if (getWindow() != null) {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        }
        mActivity = context;
        View view = LayoutInflater.from(context).inflate(R.layout.server_internet_error_layout, null);
        setContentView(view);
        connectionDetector = new ConnectionDetector(context);
        mServerInternetErrorLayoutBinding = DataBindingUtil.bind(view);
        mServerInternetErrorLayoutBinding.errorDialogToolbar
                .setTitleTextColor(ContextCompat.getColor(context, R.color.color_white));
        mServerInternetErrorLayoutBinding.errorDialogToolbar.setNavigationIcon(R.drawable.arrow_left);
        mServerInternetErrorLayoutBinding.errorDialogToolbar
                .setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mClick) {
                            mClick.onDismiss();
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != mClick) {
                dismiss();
                mClick.onDismiss();
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (null != window) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        }
    }

    public interface RetryClick {
        void callRetryClick();
        void onDismiss();
    }
}

