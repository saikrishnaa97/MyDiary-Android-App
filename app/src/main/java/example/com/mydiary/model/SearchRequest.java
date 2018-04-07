package example.com.mydiary.model;

import java.util.Date;

public class SearchRequest {
    private String profileId;
    private Date doe;

    public Date getDoe() {
        return doe;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void setDoe(Date doe) {
        this.doe = doe;
    }
}
