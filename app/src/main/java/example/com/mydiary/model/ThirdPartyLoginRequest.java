package example.com.mydiary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by saikr on 30-03-2018.
 */

public class ThirdPartyLoginRequest {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("login_source")
    private String loginSource;
    @SerializedName("client_secret")
    private String clientSecret;
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String mAccessToken) {
        this.accessToken = mAccessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mEmail) {
        this.email = mEmail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String mGender) {
        this.gender = mGender;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(String mLoginSource) {
        this.loginSource = mLoginSource;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

