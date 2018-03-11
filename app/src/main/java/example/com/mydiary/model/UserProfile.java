package example.com.mydiary.model;


import java.util.Date;


public class UserProfile {

    private String id;
    private String name;
    private Date dateOfBirth;
    private String emailId;
    private Double mobileNumber;
    private String userName;
    private String password;
    private int notifyHrs;
    private int notifyMins;
    private int consec_days;
    private Date lastEntry;

    @Override
    public String toString() {
        return "UserProfile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", emailId='" + emailId + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", notifyHrs=" + notifyHrs +
                ", notifyMins=" + notifyMins +
                ", consec_days=" + consec_days +
                ", lastEntry=" + lastEntry +
                '}';
    }

    public Date getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(Date lastEntry) {
        this.lastEntry = lastEntry;
    }

    public int getConsec_days() {
        return consec_days;
    }

    public void setConsec_days(int consec_days) {
        this.consec_days = consec_days;
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

    public Double getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Double mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
