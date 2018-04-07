package example.com.mydiary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saikr on 31-03-2018.
 */

public class AccountData {
    @SerializedName("isNeedVerifiedPhoneNumber")
    @Expose
    private boolean isNeedVerifiedPhoneNumber;
    @SerializedName("id")
    @Expose
    private String id;
//    @SerializedName("recoveryAccount")
//    @Expose
//    private RecoveryAccount recoveryAccount;
    private String profileImageUrl;

    public boolean isNeedVerifiedPhoneNumber() {
        return isNeedVerifiedPhoneNumber;
    }

//    public RecoveryAccount getRecoveryAccount() {
//        return recoveryAccount;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}

