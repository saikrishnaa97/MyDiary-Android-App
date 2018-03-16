package example.com.mydiary.model;


import java.util.Date;
import java.util.List;

public class AllEntriesResponse {
    private GeneralResponse general;
    private List<EntryDTO> entryList;
    private String emailId;
    private String name;
    private Date lastDate;
    private int consecDays;

    public int getConsecDays() {
        return consecDays;
    }

    public void setConsecDays(int consecDays) {
        this.consecDays = consecDays;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public GeneralResponse getGeneral() {
        return general;
    }

    public void setGeneral(GeneralResponse general) {
        this.general = general;
    }

    public List<EntryDTO> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<EntryDTO> entryList) {
        this.entryList = entryList;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
