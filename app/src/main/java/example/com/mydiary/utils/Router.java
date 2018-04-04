package example.com.mydiary.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import example.com.mydiary.view.AllEntriesActivity;
import example.com.mydiary.view.CheckPasswordActivity;
import example.com.mydiary.view.EntryActivity;
import example.com.mydiary.view.FullEntryActivity;
import example.com.mydiary.view.HomeActivity;
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
        if(urlString != null){
            if(urlString.equals(Constants.Companion.getLOGIN_REGISTER())) {
                context.startActivity(new Intent(context, LoginRegisterActivity.class));
            }
            else if(urlString.equals(Constants.Companion.getCHECK_PASSWORD())){
                Intent intent = new Intent(context, CheckPasswordActivity.class);
                intent.putExtra(Constants.Companion.getCHECK_PASSWORD_EXTRA(),Boolean.valueOf(objects[0].toString()));
                context.startActivity(intent);
            }
            else if(urlString.equals(Constants.Companion.getHOME())){
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra(Constants.Companion.getCHECK_PASSWORD_EXTRA(),Boolean.valueOf(objects[0].toString()));
                context.startActivity(intent);
            }
            else if(urlString.equals(Constants.Companion.getCHANGE_PASSWORD())){
                context.startActivity(new Intent(context,LoginRegisterActivity.class));
            }
            else if(urlString.equals(Constants.Companion.getADD_ENTRY())){
                context.startActivity(new Intent(context, EntryActivity.class));
            }
            else if(urlString.equals(Constants.Companion.getFULL_ENTRY())){
                Intent intent = new Intent(context, FullEntryActivity.class);
                intent.putExtra(Constants.Companion.getFULL_ENTRY_EXTRA(),objects[0].toString());
                context.startActivity(intent);
            }
            else if(urlString.equals(Constants.Companion.getALL_ENTRY())){
                Intent intent = new Intent(context, AllEntriesActivity.class);
                context.startActivity(intent);
            }
        }
        return true;
    }
}
