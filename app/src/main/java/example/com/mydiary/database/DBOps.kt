package example.com.mydiary.database

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import example.com.mydiary.model.*
import example.com.mydiary.utils.Router
import timber.log.Timber
import java.util.*

/**
 * Created by saikr on 16-03-2018.
 */
class DBOps {

    private var sqliteDB: SQLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/example.com.mydiary/databases/mydiary.db", null)
    private var mCursor : Cursor ?= null


    fun openDB(): Boolean {
        try {
            sqliteDB.execSQL("create table if not exists Entry (id VARCAHR(20) not null, profile_id VARCHAR(20) not null, title VARCHAR(20), message TEXT not null, date_of_entry DATE);")
            sqliteDB.execSQL("create table if not exists UserProfile (id VARCHAR(20) not null , name VARCHAR(30), date_of_birth DATE, email_id VARCHAR(20),password VARCHAR(30), notify_hrs TINYINT(2), notify_mins TINYINT(2), consec_days SMALLINT(3), last_entry DATE);")
            return true
        } catch (e: SQLException) {
            return false
        }
    }

    fun registerUser(newUserRequest: NewUserRequest): Boolean {
        try {
            var id = UUID.randomUUID().toString()
            var query = "insert into UserProfile(id,name,date_of_birth,email_id,password,notify_hrs,notify_mins,consec_days) values ('"+id+"','" + newUserRequest.name + "','" + newUserRequest.dob + "',' " + newUserRequest.emailId + "','" + newUserRequest.password + "'," + newUserRequest.notifyHrs + "," + newUserRequest.notifyMins + ",0)";
            sqliteDB.execSQL(query)
            return true
        } catch (e: SQLException) {
            Timber.e(e.message)
            return false
        }
    }

    fun getHome(): HomeResponse {
        val response = HomeResponse()
        val general = GeneralResponse()
        var allEntriesResponse = AllEntriesResponse()
        if(sqliteDB != null) {
            mCursor = sqliteDB.rawQuery("select email_id from UserProfile;", null)
            if (mCursor!!.count > 0) {
                mCursor?.moveToFirst()
                var emailId = mCursor?.getString(mCursor!!.getColumnIndex("email_id"))
                mCursor = sqliteDB.rawQuery("select * from UserProfile Where email_id = '" + emailId + "';", null)
                var user = UserProfile()
                if (mCursor!!.count > 0) {
                    mCursor?.moveToFirst()
                    user.name = mCursor!!.getString(mCursor!!.getColumnIndex("name"))
                    user.emailId = mCursor!!.getString(mCursor!!.getColumnIndex("email_id"))
                    user.dateOfBirth = Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_birth")))
                    user.id = mCursor!!.getString(mCursor!!.getColumnIndex("id"))
                    user.notifyHrs = mCursor!!.getInt(mCursor!!.getColumnIndex("notify_hrs"))
                    user.notifyMins = mCursor!!.getInt(mCursor!!.getColumnIndex("notify_mins"))
                    user.consecDays = mCursor!!.getInt(mCursor!!.getColumnIndex("consec_days"))
                    user.lastEntry = Date(mCursor!!.getLong(mCursor!!.getColumnIndex("last_entry")))


                    val entryList = "select * from Entry Where profile_id = '" + user!!.id + "' ORDER BY date_of_entry DESC LIMIT 10;"
                    mCursor = sqliteDB.rawQuery(entryList, null)
                    if (mCursor !== null && mCursor!!.count > 0) {
                        mCursor!!.moveToFirst()
                        val resultEntryList = ArrayList<EntryDTO>()
                        while (mCursor!!.isAfterLast) {
                            val resultEntry = EntryDTO()
                            resultEntry.setId(mCursor!!.getString(mCursor!!.getColumnIndex("id")))
                            resultEntry.setDoe(Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_entry"))))
                            resultEntry.setMessage(mCursor!!.getString(mCursor!!.getColumnIndex("message")))
                            resultEntry.setTitle(mCursor!!.getString(mCursor!!.getColumnIndex("title")))
                            resultEntryList.add(resultEntry)
                        }
                        allEntriesResponse.entryList = resultEntryList
                        response.notifyHrs = user!!.getNotifyHrs()
                        response.notifyMins = user!!.getNotifyMins()
                        allEntriesResponse.name = user!!.getName()
                        allEntriesResponse.lastDate = user!!.getLastEntry()
                        general.statusCode = 1
                    } else {
                        general.statusCode = 0
                        general.errorMessage = "No Entries Found"
                    }
                }
                allEntriesResponse.general = general
                allEntriesResponse.emailId = emailId
                response.allentries = allEntriesResponse
                return response
            } else {
                response.allentries = allEntriesResponse
                return response
            }
        }
        else {
            response.allentries = allEntriesResponse
            return response
        }
    }

    fun checkPassword(password : String) : Boolean {
        mCursor = sqliteDB.rawQuery("select password from UserProfile",null)
        mCursor?.moveToFirst()
        var correctPassword = mCursor?.getString(mCursor?.getColumnIndex("password")!!)
        if(correctPassword.equals(password)){
            return true
        }
        else {
            return false
        }
    }
    fun changePassword(oldPassword : String,newPassword : String) : Boolean{
        try{
            if(checkPassword(oldPassword)) {
                sqliteDB.execSQL("update UserProfile set password = '" + newPassword + "'")
                return true
            }
            else {
                return false
            }
        }
        catch (e : Exception){
            return false
        }
    }
}