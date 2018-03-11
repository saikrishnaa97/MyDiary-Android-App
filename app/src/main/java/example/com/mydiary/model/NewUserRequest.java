package example.com.mydiary.model;

import java.util.Date;

public class NewUserRequest {

    private String name;
    private Date dob;
    private String emailId;
    private Double mobileNumber;
    private String userName;
    private String password;
    private int notifyHrs;
    private int notifyMins;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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
