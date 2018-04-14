package example.com.mydiary.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView

import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.FragmentLoginBinding
import android.widget.Toast
import android.view.KeyEvent.KEYCODE_ENTER



class LoginFragment : Fragment() {

    private lateinit var mILoginRegisterActivityCommunicator: ILoginRegisterActivityCommunicator
    private lateinit var database: DBOps
    private var mListener: OnFragmentInteractionListener? = null
    lateinit private var mFragmentLoginBinding : FragmentLoginBinding
    private var btSubmit : Button ?= null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        database = mILoginRegisterActivityCommunicator?.getDB()
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mFragmentLoginBinding = DataBindingUtil.bind(view!!)!!
        btSubmit = mFragmentLoginBinding?.btSubmit
        btSubmit?.setOnClickListener {
            textSubmit()
        }
        mFragmentLoginBinding?.confirmPw.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //Perform Code
                textSubmit()
                return@OnKeyListener true
            }
            false
        })
    }

    fun textSubmit() {
        if(mFragmentLoginBinding?.newPw1.text.toString().equals(mFragmentLoginBinding?.confirmPw.text.toString())) {
            mILoginRegisterActivityCommunicator?.passwordChanged(database?.changePassword(mFragmentLoginBinding?.oldPw.text.toString(), mFragmentLoginBinding?.newPw1.text.toString())!!)
        }
        else {
            mILoginRegisterActivityCommunicator?.passwordsDonotMatch()
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
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
        mListener = null
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
