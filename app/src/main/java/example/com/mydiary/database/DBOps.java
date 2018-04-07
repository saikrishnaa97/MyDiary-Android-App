package example.com.mydiary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import example.com.mydiary.model.AllEntriesResponse;
import example.com.mydiary.model.EntryDTO;
import example.com.mydiary.model.EntryDetails;
import example.com.mydiary.model.GeneralResponse;
import example.com.mydiary.model.HomeResponse;
import example.com.mydiary.model.NewUserRequest;
import example.com.mydiary.model.UserProfile;
import example.com.mydiary.utils.Constants;
import timber.log.Timber;

public class DBOps extends SQLiteOpenHelper {

    public DBOps(Context context){
        super(context, Constants.Companion.getDB_NAME(),null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDB) {
        sqliteDB.execSQL("create table if not exists Entry (id VARCAHR(20) not null, profile_id VARCHAR(20) not null, title VARCHAR(20), message VARCHAR(255) not null, date_of_entry DATE);");
        sqliteDB.execSQL("create table if not exists UserProfile (id VARCHAR(20) not null , name VARCHAR(30), date_of_birth DATE, email_id VARCHAR(20),password VARCHAR(30), notify_hrs TINYINT(2), notify_mins TINYINT(2), consec_days SMALLINT(3), last_entry DATE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDB, int oldVersion, int newVersion) {
        sqliteDB.execSQL("DROP TABLE IF EXISTS Entry");
        sqliteDB.execSQL("DROP TABLE IF EXISTS UserProfile");
        onCreate(sqliteDB);
    }

    public Boolean registerUser(NewUserRequest newUserRequest)  {
        try {
            SQLiteDatabase sqliteDB = this.getWritableDatabase();
            String id = UUID.randomUUID().toString();
            String query = "insert into UserProfile(id,name,date_of_birth,email_id,password,notify_hrs,notify_mins,consec_days,last_entry) values " +
                    "('" + id + "','" + newUserRequest.getName() + "','" + newUserRequest.getDob() + "',' " +
                     newUserRequest.getEmailId() + "','" + newUserRequest.getPassword() + "'," + newUserRequest.getNotifyHrs() +
                    "," + newUserRequest.getNotifyMins() + ",0,'"+new Date()+"')";
            sqliteDB.execSQL(query);
            return true;
        } catch (SQLException e) {
            Timber.e(e.getMessage());
            return false;
        }
    }

    public HomeResponse getHome()    {
        Cursor mCursor;
        SQLiteDatabase sqliteDB = this.getReadableDatabase();
        HomeResponse response = new HomeResponse();
        GeneralResponse general = new GeneralResponse();
        AllEntriesResponse allEntriesResponse = new AllEntriesResponse();
        if (sqliteDB != null) {
            mCursor = sqliteDB.rawQuery("select email_id from UserProfile;", null);
            if (mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                String emailId = mCursor.getString(mCursor.getColumnIndex("email_id"));
                mCursor = sqliteDB.rawQuery("select * from UserProfile Where email_id = '" + emailId + "';", null);
                UserProfile user = new UserProfile();
                if (mCursor.getCount() > 0) {
                    mCursor.moveToFirst();
                    user.setName(mCursor.getString(mCursor.getColumnIndex("name")));
                    user.setEmailId(mCursor.getString(mCursor.getColumnIndex("email_id")));
                    user.setDateOfBirth(new Date(mCursor.getString(mCursor.getColumnIndex("date_of_birth"))));
                    user.setId(mCursor.getString(mCursor.getColumnIndex("id")));
                    user.setNotifyHrs(mCursor.getInt(mCursor.getColumnIndex("notify_hrs")));
                    user.setNotifyMins(mCursor.getInt(mCursor.getColumnIndex("notify_mins")));
                    user.setConsecDays(mCursor.getInt(mCursor.getColumnIndex("consec_days")));
                    user.setLastEntry(new Date(mCursor.getLong(mCursor.getColumnIndex("last_entry"))));

                    String entryList = "select * from Entry ORDER BY date_of_entry DESC LIMIT 10;";
                    mCursor = sqliteDB.rawQuery(entryList, null);
                    if (mCursor != null && mCursor.getCount() > 0) {
                        mCursor.moveToFirst();
                        List resultEntryList = new ArrayList<EntryDTO>();
                        do {
                            EntryDTO resultEntry = new EntryDTO();
                            resultEntry.setId(mCursor.getString(mCursor.getColumnIndex("id")));
                            resultEntry.setDoe(new Date(mCursor.getString(mCursor.getColumnIndex("date_of_entry"))));
                            resultEntry.setMessage(mCursor.getString(mCursor.getColumnIndex("message")));
                            resultEntry.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
                            resultEntryList.add(resultEntry);
                        } while (mCursor.moveToNext());
                        String count = "select count(id) as count from Entry;";
                        mCursor = sqliteDB.rawQuery(count, null);
                        if(mCursor != null && mCursor.getCount() > 0) {
                            mCursor.moveToFirst();
                            allEntriesResponse.setNumOfEntries(mCursor.getInt(mCursor.getColumnIndex("count")));
                        }
                        allEntriesResponse.setEntryList(resultEntryList);
                        response.setNotifyHrs(user.getNotifyHrs());
                        response.setNotifyMins(user.getNotifyMins());
                        allEntriesResponse.setName(user.getName());
                        allEntriesResponse.setLastDate(user.getLastEntry());
                        general.setStatusCode(1);
                    } else {
                        general.setStatusCode(0);
                        general.setErrorMessage("No Entries Found");
                    }
                }
                allEntriesResponse.setGeneral(general);
                allEntriesResponse.setEmailId(emailId);
                allEntriesResponse.setProfileID(user.getId());
                response.setNotifyMins(user.getNotifyMins());
                response.setNotifyHrs(user.getNotifyHrs());
                response.setAllentries(allEntriesResponse);
                return response;
            } else {
                response.setAllentries(allEntriesResponse);
                return response;
            }
        } else {
            response.setAllentries(allEntriesResponse);
            return response;
        }
    }

    public Boolean checkPassword(String password) {
        SQLiteDatabase sqliteDB = this.getReadableDatabase();
        Cursor mCursor = sqliteDB.rawQuery("select password from UserProfile", null);
        mCursor.moveToFirst();
        String correctPassword = mCursor.getString(mCursor.getColumnIndex("password"));
        if (correctPassword.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean  changePassword( String oldPassword, String newPassword){
        SQLiteDatabase sqliteDB = this.getWritableDatabase();
        try {
            if (checkPassword(oldPassword)) {
                sqliteDB.execSQL("update UserProfile set password = '" + newPassword + "'");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean logout() {
        SQLiteDatabase sqliteDB = this.getWritableDatabase();
        try {
            sqliteDB.execSQL("DELETE FROM Entry;");
            sqliteDB.execSQL("DELETE FROM UserProfile;");
            return true;
        } catch (Exception e) {
            Timber.e(e.getMessage());
            return false;
        }
    }

    public Boolean addEntry(EntryDTO entryDTO){
        Cursor mCursor;
        SQLiteDatabase sqliteDB = this.getReadableDatabase();
        try {
            mCursor = sqliteDB.rawQuery("select id from UserProfile;", null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                String profileId = mCursor.getString(mCursor.getColumnIndex("id"));
                entryDTO.setMessage(entryDTO.getMessage().replace("'","''"));
                entryDTO.setTitle(entryDTO.getTitle().replace("'","''"));
                sqliteDB.execSQL("INSERT INTO Entry values('" + UUID.randomUUID().toString() + "','" + profileId + "','" + entryDTO.getTitle() + "','" + entryDTO.getMessage() + "','" + entryDTO.getDoe() + "');");
                sqliteDB.execSQL("UPDATE UserProfile SET last_entry = '"+entryDTO.getDoe().toString()+"';");
                String query = "SELECT * FROM Entry ORDER BY date_of_entry DESC LIMIT 1;";
                mCursor = sqliteDB.rawQuery(query,null);
                if(mCursor != null && mCursor.getCount() > 0) {
                    mCursor.moveToFirst();
                    Date dbDate = new Date(mCursor.getString(mCursor.getColumnIndex("date_of_entry")));
                    if(dbDate.compareTo(new Date()) == -1){
                        sqliteDB.execSQL("UPDATE UserProfile SET consec_days = (select consec_days FROM UserProfile) + 1;");
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Timber.e(e.getMessage());
            return false;
        }
    }

    public Boolean  deleteEntry(String entryId){
        SQLiteDatabase sqliteDB = this.getWritableDatabase();
        try {
            sqliteDB.execSQL("DELETE FROM Entry Where id = '" + entryId + "';");
            return true;
        } catch (Exception e) {
            Timber.e(e.getMessage());
            return false;
        }
    }

    public EntryDetails   getEntry(String fullExtra) {
        String query = "SELECT * FROM Entry WHERE id = '" + fullExtra + "';";
        SQLiteDatabase sqliteDB = this.getReadableDatabase();
        Cursor mCursor = sqliteDB.rawQuery(query, null);
        EntryDetails entryDetails = new EntryDetails();
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            do {
                entryDetails.setDoe(new Date(mCursor.getString(mCursor.getColumnIndex("date_of_entry"))));
                entryDetails.setMessage(mCursor.getString(mCursor.getColumnIndex("message")));
                entryDetails.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
                entryDetails.setId(mCursor.getString(mCursor.getColumnIndex("id")));
            } while (mCursor.moveToNext());
        }
        return entryDetails;
    }

    public AllEntriesResponse getAllEntries()  {
        String queryEntry = "select * from Entry;";
        String queryProfile = "select * from UserProfile;";
        AllEntriesResponse allEntriesResponse = new AllEntriesResponse();
        EntryDTO entryDetails;
        Cursor mCursor;
        SQLiteDatabase sqliteDB = this.getReadableDatabase();
        mCursor = sqliteDB.rawQuery(queryEntry, null);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            allEntriesResponse.setNumOfEntries(mCursor.getCount());
                    allEntriesResponse.setEntryList(new ArrayList<EntryDTO>());
            do {
                entryDetails = new EntryDTO();
                entryDetails.setDoe(new Date(mCursor.getString(mCursor.getColumnIndex("date_of_entry"))));
                entryDetails.setMessage(mCursor.getString(mCursor.getColumnIndex("message")));
                entryDetails.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
                entryDetails.setId(mCursor.getString(mCursor.getColumnIndex("id")));
                allEntriesResponse.getEntryList().add(entryDetails);
            } while (mCursor.moveToNext());
        }
        mCursor = sqliteDB.rawQuery(queryProfile, null);
        if (mCursor != null && mCursor.getCount()> 0) {
            mCursor.moveToFirst();
            allEntriesResponse.setProfileID(mCursor.getString(mCursor.getColumnIndex("id")));
            allEntriesResponse.setEmailId(mCursor.getString(mCursor.getColumnIndex("email_id")));
            allEntriesResponse.setConsecDays(mCursor.getInt(mCursor.getColumnIndex("consec_days")));
                    allEntriesResponse.setLastDate(new Date(mCursor.getString(8)));
            allEntriesResponse.setName(mCursor.getString(mCursor.getColumnIndex("name")));
        }
        return allEntriesResponse;
    }

    public Boolean updateTime(int hrs, int mins) {
        String query = "UPDATE UserProfile SET notify_hrs = "+hrs+",notify_mins = "+mins+";";
        SQLiteDatabase sqliteDB = this.getWritableDatabase();
        try{
            sqliteDB.execSQL(query);
            return true;
        }
        catch (Exception e){
            Timber.e(e.getMessage());
            return false;
        }
    }
    public Boolean checkToday()  {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        String date = formatter.format(new Date());
        String query = "SELECT * FROM Entry ORDER BY date_of_entry DESC LIMIT 1;";
        Cursor mCursor;
        SQLiteDatabase sqliteDB = this.getReadableDatabase();
        mCursor = sqliteDB.rawQuery(query,null);
        if(mCursor != null && mCursor.getCount() > 0){
            mCursor.moveToFirst();
            Date dbDate = new Date(mCursor.getString(mCursor.getColumnIndex("date_of_entry")));
            String databaseDate = formatter.format(dbDate);
            if(date.equals(databaseDate)){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public int[] getNotifyTime()  {
        int[] resultTime = new int[2];
        Cursor mCursor;
        SQLiteDatabase sqliteDB = this.getReadableDatabase();
        mCursor = sqliteDB.rawQuery("select notify_hrs,notify_mins from UserProfile;",null);
        if(mCursor!= null && mCursor.getCount()> 0){
            mCursor.moveToFirst();
            resultTime[0] = mCursor.getInt(0);
            resultTime[1] = mCursor.getInt(1);
        }
        return resultTime;
    }
}
