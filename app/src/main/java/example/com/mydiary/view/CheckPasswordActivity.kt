package example.com.mydiary.view

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityCheckPasswordBinding
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router
import javax.inject.Inject

class CheckPasswordActivity : AppCompatActivity() {

    private var mActivityCheckPasswordBinding : ActivityCheckPasswordBinding? = null
    lateinit private var mToolbar : Toolbar
    var mRouter : Router = Router()
    private var database = DBOps()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityCheckPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_check_password)
        mToolbar = findViewById(R.id.home_toolbar)
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        mActivityCheckPasswordBinding?.submit?.setOnClickListener{
            if(mActivityCheckPasswordBinding?.password?.text != null) {
                if (database.checkPassword(mActivityCheckPasswordBinding?.password?.text.toString())) {
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