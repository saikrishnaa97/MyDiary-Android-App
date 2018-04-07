package example.com.mydiary.model;


public class HomeResponse {

    private AllEntriesResponse allentries;
    private int notifyHrs;
    private int notifyMins;

    public AllEntriesResponse getAllentries() {
        return allentries;
    }

    public void setAllentries(AllEntriesResponse allentries) {
        this.allentries = allentries;
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
