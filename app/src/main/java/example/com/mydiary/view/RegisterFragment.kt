package example.com.mydiary.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.FragmentRegisterBinding
import example.com.mydiary.model.NewUserRequest
import java.sql.Time
import java.util.*

class RegisterFragment : Fragment() {

    private var database = DBOps()
    private var mILoginRegisterActivityCommunicator : ILoginRegisterActivityCommunicator? = null
    private lateinit var binding : FragmentRegisterBinding

    private var button : Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
     return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)
        button = binding?.btSubmit
        button?.setOnClickListener{
                var newUserRequest = NewUserRequest()
                newUserRequest?.name = binding?.etName?.text.toString()
                newUserRequest?.emailId = binding?.etEmail?.text.toString()
                newUserRequest?.dob = Date(binding?.etDob?.text.toString())
                var time = (binding?.etNotifyTime?.text.toString().split(":"))
                newUserRequest?.notifyHrs = Integer.parseInt(time[0])
                newUserRequest.notifyMins = Integer.parseInt(time[1])
                newUserRequest?.password = binding?.etPassword?.text.toString()
                mILoginRegisterActivityCommunicator?.createUserProfile(database.registerUser(newUserRequest))
            }
        }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mILoginRegisterActivityCommunicator = context as ILoginRegisterActivityCommunicator
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement ILoginRegisterActivityCommunicator")
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */

}// Required empty public constructor
