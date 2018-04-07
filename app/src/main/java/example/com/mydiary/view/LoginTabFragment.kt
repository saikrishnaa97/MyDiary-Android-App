package example.com.mydiary.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.mydiary.R
import example.com.mydiary.database.DBOps
import example.com.mydiary.databinding.LoginRegisterTabLayoutBinding
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.Router
import example.com.mydiary.utils.ZoomOutPageTransformer

class LoginTabFragment : Fragment() {

    private var binding : LoginRegisterTabLayoutBinding? = null
    private var database = DBOps()

    companion object {
        private var LOGIN_FRAGMENT = 0
        private var REGISTER_FRAGMENT = 1
        private var TOTAL_NO_OF_FRAGMENTS_IN_VIEW_PAGER = 1
        private val loginFragmentInstance = LoginFragment()
        private val registerFragmentInstance = RegisterFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
         inflater?.inflate(R.layout.login_register_tab_layout,container,false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view!!)
        if(database.getHome().allentries.emailId == null){
            REGISTER_FRAGMENT = 0
            LOGIN_FRAGMENT = 1
        }
        else {
            REGISTER_FRAGMENT = 1
            LOGIN_FRAGMENT = 0
        }
        with(binding?.vpViewpager) {
            this?.adapter = MyAdapter(childFragmentManager, context)
            this?.setPageTransformer(true, ZoomOutPageTransformer())
        }
        binding?.tabs?.setupWithViewPager(binding?.vpViewpager)
    }

    private class MyAdapter(fragmentManager: FragmentManager, context: Context) : FragmentPagerAdapter(fragmentManager) {

        lateinit var fragment: Fragment
        var context: Context? = null

        init {
            this.context = context
        }

        override fun getItem(position: Int): Fragment? {
            when (position) {
                LOGIN_FRAGMENT -> fragment = loginFragmentInstance
                REGISTER_FRAGMENT -> fragment = registerFragmentInstance
            }
            return fragment
        }

        override fun getCount(): Int = TOTAL_NO_OF_FRAGMENTS_IN_VIEW_PAGER

        override fun getPageTitle(position: Int): CharSequence? =
                when (position) {
                    LOGIN_FRAGMENT ->
                        Constants.LOGIN
                    REGISTER_FRAGMENT ->
                        Constants.REGISTER
                    else -> ""
                }
    }
}
