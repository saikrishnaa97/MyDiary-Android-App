package example.com.mydiary.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import example.com.mydiary.R
import example.com.mydiary.databinding.LayoutEntryItemBinding
import example.com.mydiary.model.AllEntriesResponse
import example.com.mydiary.model.EntryDTO
import example.com.mydiary.utils.CustomAdapter
import example.com.mydiary.utils.CustomViewHolder
import example.com.mydiary.view.IAllEntryActivityCommunicator
import java.text.SimpleDateFormat

/**
 * Created by saikr on 04-04-2018.
 */
class AllEntriesAdapter(var mIAllEntryActivityCommunicator: IAllEntryActivityCommunicator,val data: MutableList<EntryDTO>) : CustomAdapter<AllEntriesAdapter.AllEntryViewHolder>(){
    override fun onCreateViewHolderImplementation(inflater: LayoutInflater?, parent: ViewGroup?, viewType: Int): AllEntriesAdapter.AllEntryViewHolder {
        return AllEntryViewHolder(inflater?.inflate(R.layout.layout_entry_item,parent,false))
    }

    override fun onBindViewHolderImplement(holder: AllEntriesAdapter.AllEntryViewHolder?, position: Int) {
        var formatter = SimpleDateFormat("dd/MMM/yyyy")
        var date = formatter.format(data.get(position).getDoe())
        holder?.binding?.tvDate?.text = date?.toString()
        holder?.binding?.tvTitle?.text = data.get(position).getTitle()
        holder?.binding?.tvMessage?.text = data.get(position).getMessage()?.substring(0, if (data.get(position).getMessage()?.length!! < 99) {
            data.get(position).getMessage()?.length
        } else {
            99
        }!!)
        holder?.binding?.clEntryItem?.setOnClickListener{
            mIAllEntryActivityCommunicator.openFullEntry(data?.get(position)?.getId())
        }
        holder?.btDelete = holder?.binding?.btDelete
        holder?.btDelete?.setOnClickListener {
            mIAllEntryActivityCommunicator.delete(data?.get(position)?.getId()!!)
        }
    }

    override fun getItemCountImplementation(): Int {
        return data.size
    }

    override fun getItemViewTypeImplementation(position: Int): Int {
        return 0
    }


    class AllEntryViewHolder(val view : View?) : CustomViewHolder(view){
        var binding : LayoutEntryItemBinding?= null
        var btDelete : Button ?= null
        init {
            binding = DataBindingUtil.bind(view!!)
        }
    }

}