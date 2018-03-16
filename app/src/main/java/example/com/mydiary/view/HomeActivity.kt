package example.com.mydiary.view

import android.Manifest
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(){

    lateinit private var mToolbar : Toolbar
    lateinit private var mRecyclerView : RecyclerView
    lateinit private var mAddButton : ImageButton
    lateinit private var binding : ActivityHomeBinding
    private var database = DBOps()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
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
        try{
        database.getHome().allentries.entryList.size > 0
            Toast.makeText(this,"there is a user",Toast.LENGTH_SHORT).show()
        }
        catch(e:Exception){
            Toast.makeText(this,"there is no user",Toast.LENGTH_SHORT).show()
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
