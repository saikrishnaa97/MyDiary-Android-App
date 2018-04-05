package example.com.mydiary.database

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import example.com.mydiary.model.*
import example.com.mydiary.utils.Router
import timber.log.Timber
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by saikr on 16-03-2018.
 */
class DBOps {

    private var sqliteDB: SQLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/example.com.mydiary/databases/mydiary.db", null)
    private var mCursor: Cursor? = null

    fun openDB(): Boolean {
        try {
            sqliteDB.execSQL("create table if not exists Entry (id VARCAHR(20) not null, profile_id VARCHAR(20) not null, title VARCHAR(20), message VARCHAR(255) not null, date_of_entry DATE);")
            sqliteDB.execSQL("create table if not exists UserProfile (id VARCHAR(20) not null , name VARCHAR(30), date_of_birth DATE, email_id VARCHAR(20),password VARCHAR(30), notify_hrs TINYINT(2), notify_mins TINYINT(2), consec_days SMALLINT(3), last_entry DATE);")
            return true
        } catch (e: SQLException) {
            return false
        }
    }

    fun registerUser(newUserRequest: NewUserRequest): Boolean {
        try {
            var id = UUID.randomUUID().toString()
            var query = "insert into UserProfile(id,name,date_of_birth,email_id,password,notify_hrs,notify_mins,consec_days,last_entry) values ('" + id + "','" + newUserRequest.name + "','" + newUserRequest.dob + "',' " + newUserRequest.emailId + "','" + newUserRequest.password + "'," + newUserRequest.notifyHrs + "," + newUserRequest.notifyMins + ",0,'"+Date()+"')";
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
        if (sqliteDB != null) {
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

                    val entryList = "select * from Entry ORDER BY date_of_entry DESC LIMIT 10;"
                    mCursor = sqliteDB.rawQuery(entryList, null)
                    if (mCursor !== null && mCursor!!.count > 0) {
                        mCursor!!.moveToFirst()
                        val resultEntryList = ArrayList<EntryDTO>()
                        do {
                            val resultEntry = EntryDTO()
                            resultEntry.setId(mCursor!!.getString(mCursor!!.getColumnIndex("id")))
                            resultEntry.setDoe(Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_entry"))))
                            resultEntry.setMessage(mCursor!!.getString(mCursor!!.getColumnIndex("message")))
                            resultEntry.setTitle(mCursor!!.getString(mCursor!!.getColumnIndex("title")))
                            resultEntryList.add(resultEntry)
                        } while (mCursor!!.moveToNext())
                        val count = "select count(id) as count from Entry;"
                        mCursor = sqliteDB.rawQuery(count, null)
                        if(mCursor != null && mCursor?.count!! > 0) {
                            mCursor?.moveToFirst()
                            allEntriesResponse.numOfEntries = mCursor?.getInt(mCursor?.getColumnIndex("count")!!)!!
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
                allEntriesResponse.profileID = user.id
                response.notifyMins = user.notifyMins
                response.notifyHrs = user.notifyHrs
                response.allentries = allEntriesResponse
                return response
            } else {
                response.allentries = allEntriesResponse
                return response
            }
        } else {
            response.allentries = allEntriesResponse
            return response
        }
    }

    fun checkPassword(password: String): Boolean {
        mCursor = sqliteDB.rawQuery("select password from UserProfile", null)
        mCursor?.moveToFirst()
        var correctPassword = mCursor?.getString(mCursor?.getColumnIndex("password")!!)
        if (correctPassword.equals(password)) {
            return true
        } else {
            return false
        }
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        try {
            if (checkPassword(oldPassword)) {
                sqliteDB.execSQL("update UserProfile set password = '" + newPassword + "'")
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            return false
        }
    }

    fun logout(): Boolean {
        try {
            sqliteDB.execSQL("DELETE FROM Entry;")
            sqliteDB.execSQL("DELETE FROM UserProfile;")
            return true
        } catch (e: Exception) {
            Timber.e(e.message.toString())
            return false
        }
    }

    fun addEntry(entryDTO: EntryDTO): Boolean {
        try {
            mCursor = sqliteDB.rawQuery("select id from UserProfile;", null)
            if (mCursor != null && mCursor!!.count > 0) {
                mCursor?.moveToFirst()
                var profileId = mCursor?.getString(mCursor?.getColumnIndex("id")!!)
                entryDTO?.setMessage(entryDTO?.getMessage()?.replace("'","''",false)!!)
                entryDTO?.setTitle(entryDTO?.getTitle()?.replace("'","''",false)!!)
                sqliteDB.execSQL("INSERT INTO Entry values('" + UUID.randomUUID().toString() + "','" + profileId + "','" + entryDTO.getTitle() + "','" + entryDTO.getMessage() + "','" + entryDTO.getDoe() + "');")
                sqliteDB.execSQL("UPDATE UserProfile SET last_entry = '"+entryDTO.getDoe().toString()+"';")
                var query = "SELECT * FROM Entry ORDER BY date_of_entry DESC LIMIT 1;"
                mCursor = sqliteDB.rawQuery(query,null)
                if(mCursor != null && mCursor?.count!! > 0) {
                    mCursor?.moveToFirst()
                    var dbDate = Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_entry")))
                    if(dbDate.compareTo(Date()) == -1){
                             sqliteDB.execSQL("UPDATE UserProfile SET consec_days = (select consec_days FROM UserProfile) + 1;")
                            }
                }
            }
            return true
        } catch (e: Exception) {
            Timber.e(e.message)
            return false
        }
    }

    fun deleteEntry(entryId: String): Boolean {
        try {
            sqliteDB.execSQL("DELETE FROM Entry Where id = '" + entryId + "';")
            return true
        } catch (e: Exception) {
            Timber.e(e.message)
            return false
        }
    }

    fun getEntry(fullExtra: String?): EntryDetails {
        var query = "SELECT * FROM Entry WHERE id = '" + fullExtra + "';"
        mCursor = sqliteDB.rawQuery(query, null)
        var entryDetails = EntryDetails()
        if (mCursor != null && mCursor?.count!! > 0) {
            mCursor?.moveToFirst()
            do {
                entryDetails.doe = (Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_entry"))))
                entryDetails.message = mCursor?.getString(mCursor?.getColumnIndex("message")!!)
                entryDetails.title = mCursor?.getString(mCursor?.getColumnIndex("title")!!)
                entryDetails.id = mCursor?.getString(mCursor?.getColumnIndex("id")!!)
            } while (mCursor?.moveToNext()!!)
        }
        return entryDetails
    }

    fun getAllEntries(): AllEntriesResponse {
        var queryEntry = "select * from Entry;"
        var queryProfile = "select * from UserProfile;"
        var allEntriesResponse = AllEntriesResponse()
        var entryDetails: EntryDTO
        mCursor = sqliteDB.rawQuery(queryEntry, null)
        if (mCursor != null && mCursor?.count!! > 0) {
            mCursor?.moveToFirst()
            allEntriesResponse.numOfEntries = mCursor?.count!!
            allEntriesResponse.entryList =  ArrayList<EntryDTO>()
            do {
                entryDetails = EntryDTO()
                entryDetails.setDoe(Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_entry"))))
                entryDetails.setMessage(mCursor?.getString(mCursor?.getColumnIndex("message")!!)!!)
                entryDetails.setTitle(mCursor?.getString(mCursor?.getColumnIndex("title")!!)!!)
                entryDetails.setId(mCursor?.getString(mCursor?.getColumnIndex("id")!!)!!)
                allEntriesResponse?.entryList.add(entryDetails)
            } while (mCursor?.moveToNext()!!)
        }
        mCursor = sqliteDB.rawQuery(queryProfile, null)
        if (mCursor != null && mCursor?.count!! > 0) {
            mCursor?.moveToFirst()
            allEntriesResponse.profileID = mCursor?.getString(mCursor?.getColumnIndex("id")!!)
            allEntriesResponse.emailId = mCursor?.getString(mCursor?.getColumnIndex("email_id")!!)
            allEntriesResponse.consecDays = mCursor?.getInt(mCursor?.getColumnIndex("consec_days")!!)!!
            allEntriesResponse.lastDate = Date(mCursor?.getString(8))
            allEntriesResponse.name = mCursor?.getString(mCursor?.getColumnIndex("name")!!)
        }
        return allEntriesResponse
    }

    fun updateTime(hrs: Int, mins: Int)  : Boolean{
        var query = "UPDATE UserProfile SET notify_hrs = "+hrs+",notify_mins = "+mins+";"
        try{
            sqliteDB.execSQL(query)
            return true
        }
        catch (e:Exception){
            Timber.e(e.message)
            return false
        }
    }
    fun checkToday() : Boolean{
        var formatter = SimpleDateFormat("dd/MMM/yyyy")
        var date = formatter.format(Date())
        var query = "SELECT * FROM Entry ORDER BY date_of_entry DESC LIMIT 1;"
        mCursor = sqliteDB.rawQuery(query,null)
        if(mCursor != null && mCursor?.count!! > 0){
            mCursor?.moveToFirst()
            var dbDate = Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_entry")))
            var databaseDate = formatter.format(dbDate)
            if(date.equals(databaseDate)){
                return true
            }
            else {
                return false
            }
        }
        else {
            return false
        }
    }

    fun getNotifyTime() : IntArray {
        var resultTime : IntArray? = null
        mCursor = sqliteDB.rawQuery("select notify_hrs,notify_mins from UserProfile;",null)
        if(mCursor!= null && mCursor?.count!! > 0){
            mCursor?.moveToFirst()
            resultTime = intArrayOf(mCursor?.getInt(0)!!,mCursor?.getInt(1)!!)
        }
        return resultTime!!
    }

}