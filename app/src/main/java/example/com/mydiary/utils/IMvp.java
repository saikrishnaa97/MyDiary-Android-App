package example.com.mydiary.utils;

import rx.subjects.PublishSubject;

/**
 * Created by saikr on 30-03-2018.
 */

public interface IMvp {

    PublishSubject<Integer> noInternetConnection(Throwable throwable,
                                                 INetworkErrorHandler iNetworkErrorHandler, Object... objects);

    void showDialogError(String message, IFalseResponseHandler falseResponseHandler);

    void onOauthError(String errorCode, String errorDescription, Throwable e);

}
