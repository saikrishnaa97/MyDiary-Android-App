package example.com.mydiary.utils;

import example.com.mydiary.view.LoginFragment;
import example.com.mydiary.view.RegisterFragment;

/**
 * Created by saikr on 20-03-2018.
 */

public interface OnFragmentInteractionListener extends LoginFragment.OnFragmentInteractionListener,RegisterFragment.OnFragmentInteractionListener{
    public void onFragmentMessage(String TAG, Object data);
}
