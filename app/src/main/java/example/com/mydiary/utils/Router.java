package example.com.mydiary.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import example.com.mydiary.view.LoginRegisterActivity;

/**
 * Created by saikr on 19-03-2018.
 */

@ApplicationScope
public class Router {
    private final static String TAG = "RouterActivity";
    private final static String URI_KEY = "uri";

    private Intent intent;
    private Bundle bundle;
    @Inject
    public Router() {
        // No Implementation required
    }

    public boolean routeTarget(String urlString, Activity context, Object... objects) {
        intent = new Intent(context,LoginRegisterActivity.class);
        bundle = new Bundle();
        bundle.putString(Constants.Companion.getLOGIN_REGISTER_EXTRA(),urlString);
        intent.putExtras(bundle);
        context.startActivity(intent);
        return true;
    }
}
