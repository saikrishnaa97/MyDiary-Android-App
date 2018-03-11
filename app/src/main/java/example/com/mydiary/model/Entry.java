package example.com.mydiary.model;

import java.util.Date;


public class Entry {

    private String id;
    private String profileId;
    private String title;
    private String message;
    private Date dateOfEntry;
    private boolean markedForDelete;

    @Override
    public String toString() {
        return "Entry{" +
                "id='" + id + '\'' +
                ", profileId='" + profileId + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", dateOfEntry=" + dateOfEntry +
                ", markedForDelete=" + markedForDelete +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }
}
