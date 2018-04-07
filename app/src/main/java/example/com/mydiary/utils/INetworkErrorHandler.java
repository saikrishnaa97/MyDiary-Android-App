package example.com.mydiary.utils;

/**
 * Created by saikr on 30-03-2018.
 */

public interface INetworkErrorHandler {

    void addToQueue(int subscriptionType, Object... objects);

    void cancelDialog();

    void onErrorServer();

}
