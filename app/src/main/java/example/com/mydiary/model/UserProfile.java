package example.com.mydiary.model;


import java.util.Date;


public class UserProfile {

    private String id;
    private String name;
    private Date dateOfBirth;
    private String emailId;
    private int notifyHrs;
    private int notifyMins;
    private int consecDays;
    private Date lastEntry;

    @Override
    public String toString() {
        return "UserProfile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", emailId='" + emailId + '\'' +
                ", notifyHrs=" + notifyHrs +
                ", notifyMins=" + notifyMins +
                ", consecDays=" + consecDays +
                ", lastEntry=" + lastEntry +
                '}';
    }

    public Date getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(Date lastEntry) {
        this.lastEntry = lastEntry;
    }

    public int getConsecDays() {
        return consecDays;
    }

    public void setConsecDays(int consecDays) {
        this.consecDays = consecDays;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
