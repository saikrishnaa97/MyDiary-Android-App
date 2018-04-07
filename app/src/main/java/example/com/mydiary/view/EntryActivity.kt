package example.com.mydiary.view

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.ActivityEntryBinding
import example.com.mydiary.model.EntryDTO
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router
import java.util.*
import android.text.Editable



class EntryActivity : AppCompatActivity() {

    lateinit var mRouter : Router
    private var database = DBOps(this)
    private var binding : ActivityEntryBinding ? = null
    private var btSubmit : Button ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRouter = Router()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_entry)
        btSubmit = binding?.btSubmit
        binding?.etMessage?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length >= 255){
                    charLimitReached()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
            }
        })
        btSubmit?.setOnClickListener {
            var entryDTO = EntryDTO()
            entryDTO.setDoe(Date())
            entryDTO.setId(UUID.randomUUID().toString())
            entryDTO.setMessage(binding?.etMessage?.text.toString())
            entryDTO.setTitle(binding?.etTitle?.text.toString())
            addEntrySuccessful(database.addEntry(entryDTO))
        }
    }

    private fun charLimitReached(){
        Toast.makeText(this,"Character limit reached",Toast.LENGTH_SHORT).show()
    }

    private fun addEntrySuccessful(isEntered : Boolean) {
        if(isEntered) {
            Toast.makeText(this, R.string.add_entry_success,Toast.LENGTH_SHORT).show()
            mRouter.routeTarget(Constants.HOME,this,true)
            finish()
        }
        else {
            Toast.makeText(this, R.string.add_entry_failure,Toast.LENGTH_SHORT).show()
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
