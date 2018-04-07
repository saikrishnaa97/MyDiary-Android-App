package example.com.mydiary.view

import android.app.Activity
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.adapter.HomeAdapter
import example.com.mydiary.background.NotificationService
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityHomeBinding
import example.com.mydiary.model.HomeResponse
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router
import timber.log.Timber
import java.util.*


class HomeActivity : AppCompatActivity(),IHomeActivityCommunicator{

    lateinit private var mRecyclerView : RecyclerView
    lateinit private var mAddButton : ImageButton
    lateinit private var binding : ActivityHomeBinding
    lateinit private var mToolbar : Toolbar
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
        try{
            if(intent.getIntExtra(Constants.ADD_BUTTON_CLICKED,1) == 0){
                finish()
            }
        }
        catch (e:Exception){
            Timber.e(e.message)
        }
        mAddButton?.setOnClickListener {
            mRouter.routeTarget(Constants.ADD_ENTRY,this,null)
            finish()
        }
        binding?.allEntries?.setOnClickListener{
            mRouter.routeTarget(Constants.ALL_ENTRY,this,null)
        }
        database.openDB()
        try {
            response = database.getHome()
            if (response?.allentries?.emailId != null && !homeExtra!!){
                mRouter.routeTarget(Constants.CHECK_PASSWORD, this, false)
            }
            else if(homeExtra!! && response?.allentries?.entryList != null){
                updateUI()
            }
        }
        catch(e:Exception){
            mRouter.routeTarget(Constants.LOGIN_REGISTER,this,null)
            finish()
        }
    }

    override fun onResume() {
        updateUI()
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
        else if(item?.itemId == R.id.change_time){
            TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (database?.updateTime(hourOfDay, minute)) {
                    Toast.makeText(this, R.string.time_updated_success, Toast.LENGTH_SHORT).show()
                    updateUI()
                }
            },response?.notifyHrs!!,response?.notifyMins!!,true).show()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun openFullEntry(id: String?) {
        mRouter.routeTarget(Constants.FULL_ENTRY,this,id)
    }

    override fun delete(entryId: String) {
        if(database.deleteEntry(entryId)){
            Toast.makeText(this,getString(R.string.delete_success),Toast.LENGTH_SHORT).show()
            updateUI()
        }
        else {
            Toast.makeText(this,getString(R.string.delete_fail),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(){
        response = database.getHome()
        mRouter.routeTarget(Constants.NOTIFICATION_SERVICE,this,response?.notifyHrs,response?.notifyMins)
        if(response?.allentries?.entryList != null && response?.allentries?.entryList?.size!! > 0) {
            mRecyclerView.layoutManager = LinearLayoutManager(this)
            mRecyclerView.adapter = HomeAdapter(this, response?.allentries?.entryList!!)
            mRecyclerView?.adapter.notifyDataSetChanged()
            if(response?.allentries?.numOfEntries!! > 10){
                binding?.allEntries.visibility = View.VISIBLE
            }
        }
        else {
            binding?.clNoEntries.visibility = View.VISIBLE
            binding?.dataListHome.visibility = View.GONE
        }
    }

}
