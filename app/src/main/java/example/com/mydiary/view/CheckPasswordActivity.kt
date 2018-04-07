package example.com.mydiary.view

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityCheckPasswordBinding
import example.com.mydiary.model.HomeResponse
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router

class CheckPasswordActivity : AppCompatActivity() {

    private var mActivityCheckPasswordBinding : ActivityCheckPasswordBinding? = null
    lateinit private var mToolbar : Toolbar
    var mRouter : Router = Router()
    private var database = DBOps(this)
    var homeExtra : Boolean ? = null
    private var response : HomeResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityCheckPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_check_password)
        mToolbar = findViewById(R.id.home_toolbar)
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        try{
            homeExtra = intent.getBooleanExtra(Constants.CHECK_PASSWORD_EXTRA,false)
        }
        catch (e : Exception){

        }

        try {
            response = database.getHome()
            if (response?.allentries?.emailId != null && !homeExtra!!){
                //No implementation required
            }
            else if(homeExtra!!){
                mRouter.routeTarget(Constants.HOME, this, true)
                finish()
            }
            else {
                mRouter.routeTarget(Constants.LOGIN_REGISTER,this,null)
                finish()
            }
        }
        catch(e:Exception){
            mRouter.routeTarget(Constants.LOGIN_REGISTER,this,null)
            finish()
        }

        mActivityCheckPasswordBinding?.submit?.setOnClickListener{
            if(mActivityCheckPasswordBinding?.password?.text != null) {
                if (database.checkPassword(mActivityCheckPasswordBinding?.password?.text.toString())) {
                    mRouter.routeTarget(Constants.HOME, this, true)
                    finish()
                }
                else {
                    Toast.makeText(this,getString(R.string.wrong_password),Toast.LENGTH_SHORT).show()
                }
            }
                else {
                    Toast.makeText(this,getString(R.string.pw_invalid),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }