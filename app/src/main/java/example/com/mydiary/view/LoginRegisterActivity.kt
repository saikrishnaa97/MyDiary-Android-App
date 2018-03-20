package example.com.mydiary.view

import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import example.com.mydiary.R
import example.com.mydiary.databinding.ActivityLoginRegisterBinding
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.OnFragmentInteractionListener

class LoginRegisterActivity : AppCompatActivity(), OnFragmentInteractionListener {

    lateinit private var binding : ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var landingPage = intent.extras.getString(Constants.LOGIN_REGISTER_EXTRA,Constants.REGISTER)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_register)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if(landingPage.equals(Constants.LOGIN)) {
            binding?.loginToolbar.setTitle(Constants.LOGIN)
            fragmentTransaction.add(LoginFragment(), Constants.LOGIN)
        }
        else if(landingPage.equals(Constants.REGISTER)) {
            binding?.loginToolbar.setTitle(Constants.REGISTER)
            fragmentTransaction.add(RegisterFragment(), Constants.REGISTER)
        }
        setSupportActionBar(binding?.loginToolbar)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onFragmentMessage(TAG: String?, data: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
