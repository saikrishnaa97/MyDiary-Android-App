package example.com.mydiary.view

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityFullEntryBinding
import example.com.mydiary.model.EntryDetails
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router
import timber.log.Timber
import java.text.SimpleDateFormat

class FullEntryActivity : AppCompatActivity() {
    lateinit private var mToolbar : Toolbar
    private var database = DBOps(this)
    private var binding : ActivityFullEntryBinding ? = null
    private var fullExtra : String ? = null
    private var data : EntryDetails ? = null
    private var mRouter = Router()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_full_entry)
        mToolbar = findViewById(R.id.full_entry_toolbar)
        mToolbar.title = ""
        setSupportActionBar(mToolbar)
        try {
            fullExtra = intent.getStringExtra(Constants.FULL_ENTRY_EXTRA)
        }
        catch (e : Exception){
            Timber.e(e.message)
        }
        data = database.getEntry(fullExtra)
        var formatter = SimpleDateFormat("dd/MMM/yyyy")
        var date = formatter.format(data?.getDoe())
        binding?.tvDate?.text = date?.toString()
        binding?.tvTitle?.text = data?.title
        binding?.tvMessage?.text = data?.message
        binding?.ibBack?.setOnClickListener{
            finish()
        }
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

}
