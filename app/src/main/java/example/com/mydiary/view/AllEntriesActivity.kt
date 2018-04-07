package example.com.mydiary.view

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
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.adapter.AllEntriesAdapter
import example.com.mydiary.adapter.HomeAdapter
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityAllEntriesBinding
import example.com.mydiary.model.AllEntriesResponse
import example.com.mydiary.model.HomeResponse
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router

class AllEntriesActivity : AppCompatActivity() , IAllEntryActivityCommunicator{
    var mRouter : Router = Router()
    private var database = DBOps()
    lateinit private var mRecyclerView : RecyclerView
    lateinit private var mToolbar : Toolbar
    private var binding : ActivityAllEntriesBinding ? = null
    private var response : AllEntriesResponse? = null
    lateinit private var mAddButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_all_entries)
        mToolbar = findViewById(R.id.home_toolbar)
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        mRecyclerView = findViewById(R.id.data_list_home)
        mAddButton = findViewById(R.id.add_entry)
        mAddButton?.setOnClickListener {
            mRouter.routeTarget(Constants.ADD_ENTRY,this,null)
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
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener{ dialog, which ->
                        if(database.logout()){
                            mRouter.routeTarget(Constants.LOGIN_REGISTER,this,null)
                            finish()
                        }
                    })
                    .setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .setTitle(R.string.logout)
            var dialog = builder.create()
            dialog.show()

        }
        return super.onOptionsItemSelected(item)
    }
    override fun openFullEntry(id: String?) {
        mRouter.routeTarget(Constants.FULL_ENTRY,this,id)
    }

    override fun delete(entryId: String) {
        if(database.deleteEntry(entryId)){
            Toast.makeText(this,getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
            updateUI()
        }
        else {
            Toast.makeText(this,getString(R.string.delete_fail), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(){
        response = database.getAllEntries()
        if(response?.entryList != null && response?.entryList?.size!! > 0) {
            mRecyclerView.layoutManager = LinearLayoutManager(this)
            mRecyclerView.adapter = AllEntriesAdapter(this, response?.entryList!!)
            mRecyclerView?.adapter.notifyDataSetChanged()
        }

    }

}
