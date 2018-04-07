package example.com.mydiary.view

import android.database.sqlite.SQLiteDatabase
import android.support.v4.app.Fragment
import example.com.mydiary.database.DBOps
import example.com.mydiary.model.NewUserRequest
import org.jetbrains.annotations.NotNull

/**
 * Created by saikr on 30-03-2018.
 */
interface ILoginRegisterActivityCommunicator {

//    fun callLoginApi(emailId: String, password: String)
//    fun showGoogleLoginOptions(isTryingRegistration: Boolean)
    fun finishLoginRegisterActivity()
//    fun showRegistrationFragment(fragment: Fragment, tag: String?)
//    fun removeFragment(tag: String)
//    fun initNormalRegistration(userName: String, emailId: String, phoneNumber: String, password: String, memberProfileInput: NewUserRequest)
//    fun setToolBarTitle(@NotNull titleString: String)
    fun createUserProfile(isCreatedSuccessfully : Boolean)
    fun passwordChanged(isChanged : Boolean)
    fun passwordsDonotMatch()
    fun getDB(): DBOps
}