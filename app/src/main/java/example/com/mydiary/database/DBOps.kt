package example.com.mydiary.database

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import example.com.mydiary.model.*
import java.util.*

/**
 * Created by saikr on 16-03-2018.
 */
class DBOps {

    lateinit private var sqliteDB: SQLiteDatabase
    private var mCursor : Cursor ?= null

    fun openDB(): Boolean {

        sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/example.com.mydiary/databases", null)
        try {
            sqliteDB.execSQL("create table if not exists Entry (id VARCAHR(20) not null, profile_id VARCHAR(20) not null, title VARCHAR(20), message TEXT not null, date_of_entry DATE, marked_for_delete BOOLEAN);")
            sqliteDB.execSQL("create table if not exists UserProfile (id VARCHAR(20) not null , name VARCHAR(30), date_of_birth DATE, email_id VARCHAR(20), notify_hrs TINYINT(2), notify_mins TINYINT(2), consec_days SMALLINT(3), last_entry DATE);")
            return true
        } catch (e: SQLException) {
            return false
        }
    }

    fun registerUser(newUserRequest: NewUserRequest): Boolean {
        try {
            var query = "insert into UserProfile(name,date_of_birth,email_id,notify_hrs,notify_mins,consec_days) ('" + newUserRequest.name + "','" + newUserRequest.dob + "',' " + newUserRequest.emailId + "'," + newUserRequest.notifyHrs + "," + newUserRequest.notifyMins + ",0)";
            sqliteDB.execSQL(query)
            return true
        } catch (e: SQLException) {
            return false
        }
    }

    fun getHome(): HomeResponse {
        val response = HomeResponse()
        val general = GeneralResponse()
        var allEntriesResponse = AllEntriesResponse()
        mCursor = sqliteDB.rawQuery("select email_id from UserProfile;",null)
        if(mCursor!!.count > 0) {
            mCursor?.moveToFirst()
            var emailId = mCursor?.getString(mCursor!!.getColumnIndex("email_id"))
            mCursor = sqliteDB.rawQuery("select * from UserProfile Where email_id = '" + emailId + "';", null)
            var user = UserProfile()
            if (mCursor != null) {
                mCursor?.moveToFirst()
                user.name = mCursor!!.getString(mCursor!!.getColumnIndex("name"))
                user.emailId = mCursor!!.getString(mCursor!!.getColumnIndex("email_id"))
                user.dateOfBirth = Date(mCursor!!.getString(mCursor!!.getColumnIndex("date_of_birth")))
                user.id = mCursor!!.getString(mCursor!!.getColumnIndex("id"))
                user.notifyHrs = mCursor!!.getInt(mCursor!!.getColumnIndex("notify_hrs"))
                user.notifyMins = mCursor!!.getInt(mCursor!!.getColumnIndex("notify_mins"))
                user.consecDays = mCursor!!.getInt(mCursor!!.getColumnIndex("consec_days"))
                user.lastEntry = Date(mCursor!!.getString(mCursor!!.getColumnIndex("last_entry")))


                val entryList = "select TOP 10 * from Entry Where marked_for_delete = false AND profile_id = '" + user!!.id + "' ORDER BY date_of_entry DESC;"
                mCursor = sqliteDB.rawQuery(entryList, null)
                if (mCursor !== null) {
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
                    allEntriesResponse.emailId = emailId
                    allEntriesResponse.name = user!!.getName()
                    allEntriesResponse.lastDate = user!!.getLastEntry()
                    general.statusCode = 1
                } else {
                    general.statusCode = 0
                    general.errorMessage = "User Not Found"
                }
            }
            allEntriesResponse.general = general
            response.allentries = allEntriesResponse
            return response
        }
        else {
            response.allentries = allEntriesResponse
            return response
        }

    }
}