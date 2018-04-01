package example.com.mydiary.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import example.com.mydiary.R
import example.com.mydiary.databinding.LayoutEntryItemBinding
import example.com.mydiary.model.EntryDTO
import example.com.mydiary.utils.CustomAdapter
import example.com.mydiary.utils.CustomViewHolder
import example.com.mydiary.view.IHomeActivityCommunicator
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by saikr on 01-04-2018.
 */
class HomeAdapter(val mIHomeActivityCommunicator : IHomeActivityCommunicator,val data : List<EntryDTO>) : CustomAdapter<HomeAdapter.HomeViewHolder>(){



    override fun onCreateViewHolderImplementation(inflater: LayoutInflater?, parent: ViewGroup?, viewType: Int): HomeViewHolder {
        return HomeViewHolder(inflater?.inflate(R.layout.layout_entry_item , parent , false))
    }

    override fun onBindViewHolderImplement(holder: HomeViewHolder?, position: Int) {
        var formatter = SimpleDateFormat("dd/MMM/yyyy")
        var date = formatter.format(data.get(position).getDoe())
        holder?.binding?.tvDate?.text = date?.toString()
        holder?.binding?.tvTitle?.text = data.get(position).getTitle()
        holder?.binding?.tvMessage?.text = data.get(position).getMessage()?.substring(0,if(data.get(position).getMessage()?.length!! < 99){data.get(position).getMessage()?.length} else {99}!!)
        holder?.btDelete = holder?.binding?.btDelete
        holder?.btDelete?.setOnClickListener {
            mIHomeActivityCommunicator.delete(data?.get(position).getId()!!)
        }
    }

    override fun getItemCountImplementation(): Int {
        return data.size
    }

    override fun getItemViewTypeImplementation(position: Int): Int {
        return 0
    }

    class HomeViewHolder(val view : View?) : CustomViewHolder(view){
        var binding : LayoutEntryItemBinding ?= null
        var btDelete : Button ?= null
        init {
            binding = DataBindingUtil.bind(view)
        }
    }
}