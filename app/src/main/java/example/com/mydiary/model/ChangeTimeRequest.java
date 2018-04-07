package example.com.mydiary.model;

import java.util.UUID;

public class ChangeTimeRequest {

    private String profileId;
    private int notifyHrs;
    private int notifyMins;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getNotifyHrs() {
        return notifyHrs;
    }

    public void setNotifyHrs(int notifyHrs) {
        this.notifyHrs = notifyHrs;
    }

    public int getNotifyMins() {
        return notifyMins;
    }

    public void setNotifyMins(int notifyMins) {
        this.notifyMins = notifyMins;
    }
}
