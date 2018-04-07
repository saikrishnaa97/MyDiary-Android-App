package example.com.mydiary.view

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityLoginRegisterBinding
import example.com.mydiary.model.ThirdPartyLoginRequest
import example.com.mydiary.network.ILoginApi
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.OnFragmentInteractionListener
import example.com.mydiary.utils.Router
import javax.inject.Inject

class LoginRegisterActivity : AppCompatActivity(), OnFragmentInteractionListener,ILoginRegisterActivityCommunicator {

    lateinit private var binding : ActivityLoginRegisterBinding
    private var mFragmentManager: FragmentManager? = null
    private var mFragmentTransaction: FragmentTransaction? = null
    private var mCustomProgressDialog: CustomProgressDialog? = null
    private var mRouter = Router()

    companion object {
        private val LOGIN_FRAGMENT_TAG: String = "LoginTabFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_register)
        binding?.loginToolbar?.title = getString(R.string.app_name)
        setSupportActionBar(binding?.loginToolbar)
        commitFragmentToActivity(R.id.container, LoginTabFragment(), LOGIN_FRAGMENT_TAG)
    }

    private fun commitFragmentToActivity(container: Int, fragmentToCommit: Fragment, fragmentTag: String) {
        mFragmentManager = supportFragmentManager
        mFragmentTransaction = mFragmentManager!!.beginTransaction()
        mFragmentTransaction?.add(container, fragmentToCommit, fragmentTag)
        mFragmentTransaction?.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onFragmentMessage(TAG: String?, data: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUserProfile(isCreatedSuccessfully: Boolean) {
        if(isCreatedSuccessfully){
            Toast.makeText(this,getString(R.string.user_created_success),Toast.LENGTH_SHORT).show()
            mRouter.routeTarget(Constants.HOME,this,true)
            finish()
        }
        else {
            Toast.makeText(this,getString(R.string.user_created_failure),Toast.LENGTH_SHORT).show()
        }
    }

    override fun passwordsDonotMatch() {
        Toast.makeText(this,getString(R.string.text_password_warning),Toast.LENGTH_SHORT).show()
    }
    override fun passwordChanged(isChanged: Boolean) {
        if(isChanged){
            Toast.makeText(this,getString(R.string.pw_change_success),Toast.LENGTH_SHORT).show()
            mRouter.routeTarget(Constants.HOME,this,true)
            finish()
        }
        else {
            Toast.makeText(this,getString(R.string.pw_change_fail),Toast.LENGTH_SHORT).show()
        }
    }

    override fun finishLoginRegisterActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDB(): DBOps {
        return DBOps(this)
    }

}


