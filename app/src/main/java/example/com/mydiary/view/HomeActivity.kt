package example.com.mydiary.view

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityHomeBinding
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router
import javax.inject.Inject

class HomeActivity : AppCompatActivity(){

    lateinit private var mToolbar : Toolbar
    lateinit private var mRecyclerView : RecyclerView
    lateinit private var mAddButton : ImageButton
    lateinit private var binding : ActivityHomeBinding
    private var database = DBOps()

    lateinit @Inject
    var mRouter : Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRouter = Router()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        mToolbar = findViewById(R.id.home_toolbar)
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        mRecyclerView = findViewById(R.id.data_list_home)
        mAddButton = findViewById(R.id.add_entry)
        mAddButton?.setOnClickListener {
            Toast.makeText(this,"Add button clicked",Toast.LENGTH_SHORT).show()
        }
        database.openDB()
        try {
            if (database.getHome().allentries.entryList.size > 0)
                Toast.makeText(this, Constants.USER_FOUND, Toast.LENGTH_SHORT).show()
            else {
                mRouter.routeTarget(Constants.REGISTER,this,Constants.LOGIN_REGISTER)
                Toast.makeText(this, Constants.NO_USER_FOUND, Toast.LENGTH_SHORT).show()
            }
        }
        catch(e:Exception){
            mRouter.routeTarget(Constants.REGISTER,this,Constants.LOGIN_REGISTER)
            Toast.makeText(this, Constants.NO_USER_FOUND, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }


}
