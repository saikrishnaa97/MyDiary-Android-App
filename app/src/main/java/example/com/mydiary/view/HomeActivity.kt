package example.com.mydiary.view

import android.app.Dialog
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.adapter.HomeAdapter
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityHomeBinding
import example.com.mydiary.model.HomeResponse
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router
import javax.inject.Inject

class HomeActivity : AppCompatActivity(),IHomeActivityCommunicator{

    lateinit private var mToolbar : Toolbar
    lateinit private var mRecyclerView : RecyclerView
    lateinit private var mAddButton : ImageButton
    lateinit private var binding : ActivityHomeBinding
    private var response : HomeResponse ? = null

    var homeExtra : Boolean ? = null
    lateinit var mRouter : Router
    private var database = DBOps()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRouter = Router()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        mToolbar = findViewById(R.id.home_toolbar)
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        mRecyclerView = findViewById(R.id.data_list_home)
        mAddButton = findViewById(R.id.add_entry)
        try{
            homeExtra = intent.getBooleanExtra(Constants.CHECK_PASSWORD_EXTRA,false)
        }
        catch (e : Exception){

        }
        mAddButton?.setOnClickListener {
            mRouter.routeTarget(Constants.ADD_ENTRY,this,null)
        }
        database.openDB()
        try {
            response = database.getHome()
            if (response?.allentries?.emailId != null && !homeExtra!!){
                mRouter.routeTarget(Constants.CHECK_PASSWORD, this, false)
            }
            else if(homeExtra!!){
                mRecyclerView.layoutManager = LinearLayoutManager(this)
                mRecyclerView.adapter = HomeAdapter(this,response?.allentries?.entryList!!)
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
    }

    override fun onResume() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        response = database.getHome()
        mRecyclerView.adapter = HomeAdapter(this,response?.allentries?.entryList!!)
        mRecyclerView?.adapter.notifyDataSetChanged()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.change_password){
            mRouter?.routeTarget(Constants.CHANGE_PASSWORD,this,null)
        }
        else if(item?.itemId == R.id.logout){
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.logout_confirmation)
                    .setPositiveButton(R.string.yes,DialogInterface.OnClickListener{dialog,which ->
                        if(database.logout()){
                            mRouter.routeTarget(Constants.LOGIN_REGISTER,this,null)
                            finish()
                        }
                    })
                    .setNegativeButton(R.string.no,DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .setTitle(R.string.logout)
            var dialog = builder.create()
            dialog.show()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun delete(entryId: String) {
        if(database.deleteEntry(entryId)){
            Toast.makeText(this,getString(R.string.delete_success),Toast.LENGTH_SHORT).show()
            response = database.getHome()
            mRecyclerView.adapter = HomeAdapter(this,response?.allentries?.entryList!!)
            mRecyclerView.adapter.notifyDataSetChanged()
        }
        else {
            Toast.makeText(this,getString(R.string.delete_fail),Toast.LENGTH_SHORT).show()
        }
    }

}
