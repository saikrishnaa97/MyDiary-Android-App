package example.com.mydiary.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import example.com.mydiary.R
import example.com.mydiary.databinding.ActivityLoginRegisterBinding
import example.com.mydiary.model.MemberProfileInput
import example.com.mydiary.model.NewUserRequest
import example.com.mydiary.model.ThirdPartyLoginRequest
import example.com.mydiary.network.ILoginApi
import example.com.mydiary.utils.Constants
import example.com.mydiary.utils.OnFragmentInteractionListener
import example.com.mydiary.utils.Router
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class LoginRegisterActivity : AppCompatActivity(), OnFragmentInteractionListener,ILoginRegisterActivityCommunicator,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    lateinit private var binding : ActivityLoginRegisterBinding
    private var mFragmentManager: FragmentManager? = null
    private var mFragmentTransaction: FragmentTransaction? = null
    private var mCustomProgressDialog: CustomProgressDialog? = null
    private var mThirdPartyLoginRequest: ThirdPartyLoginRequest? = null
    private var mILoginRegisterView: ILoginRegisterView? = null
    private var mRouter = Router()
    @Inject
    lateinit var mILoginApi: ILoginApi
    private var mIsResolving = false
    private var mCredential: Credential? = null
    private var mCredentialToSave: Credential? = null
    protected var mSubscriptions: CompositeSubscription? = CompositeSubscription()

    private var googleSignInResult: GoogleSignInResult? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var emailId: String? = null
    private var password: String? = null

    companion object {
        val GOOGLE_AUTH_CLIENT_ID = "580394317052-5isvbi86a12bbf6qq89hiu8c85rgbjoo.apps.googleusercontent.com"
        private val RC_SIGN_IN = 1
        private val LOGIN_FRAGMENT_TAG: String = "LoginTabFragment"
        val USER_GRANT_TYPE = "password"
        val SCOPE = "write"
        val EMAIL_KEY = "email"
        val PUBLIC_PROFILE_KEY = "public_profile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_register)
        binding?.loginToolbar?.title = getString(R.string.app_name)
        setSupportActionBar(binding?.loginToolbar)
        commitFragmentToActivity(R.id.container, LoginTabFragment(), LOGIN_FRAGMENT_TAG)
    }

    private fun commitFragmentToActivity(container: Int, fragmentToCommit: Fragment, fragmentTag: String) {
        mFragmentManager = supportFragmentManager
        mFragmentTransaction = mFragmentManager!!.beginTransaction()
        mFragmentTransaction?.add(container, fragmentToCommit, fragmentTag)
        mFragmentTransaction?.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onFragmentMessage(TAG: String?, data: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUserProfile(isCreatedSuccessfully: Boolean) {
        if(isCreatedSuccessfully){
            Toast.makeText(this,getString(R.string.user_created_success),Toast.LENGTH_SHORT).show()
            mRouter.routeTarget(Constants.HOME,this,true)
        }
        else {
            Toast.makeText(this,getString(R.string.user_created_failure),Toast.LENGTH_SHORT).show()
        }
    }
    override fun passwordsDonotMatch() {
        Toast.makeText(this,getString(R.string.text_password_warning),Toast.LENGTH_SHORT).show()
    }
    override fun passwordChanged(isChanged: Boolean) {
        if(isChanged){
            Toast.makeText(this,getString(R.string.pw_change_success),Toast.LENGTH_SHORT).show()
            mRouter.routeTarget(Constants.HOME,this,true)
            finish()
        }
        else {
            Toast.makeText(this,getString(R.string.pw_change_fail),Toast.LENGTH_SHORT).show()
        }
    }


//    override fun callLoginApi(emailId: String, password: String) {
//        this.emailId = emailId
//        this.password = password
//        showCustomProgressDialog(DialogInterface.OnCancelListener { finish() })
// }
//
//    override fun showGoogleLoginOptions(isTryingRegistration: Boolean) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    private fun initGoogleLogin() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_AUTH_CLIENT_ID).requestEmail().build()
        unlinkGoogleClient()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Auth.CREDENTIALS_API)
                .build()
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            resultCode == Activity.RESULT_OK && requestCode == RC_SIGN_IN -> {
                googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                handleSignInResult(googleSignInResult)
            }
        }
    }

    fun handleSignInResult(googleSignInResult: GoogleSignInResult?) {
        if (googleSignInResult?.isSuccess as Boolean) {
            // Signed in successfully, show authenticated UI.
            val googleSignInAccount = googleSignInResult.signInAccount

            if (googleSignInAccount != null) {
                mThirdPartyLoginRequest = ThirdPartyLoginRequest()
                mThirdPartyLoginRequest!!.accessToken = googleSignInAccount.idToken ?: ""
                mThirdPartyLoginRequest!!.email = googleSignInAccount.email ?: ""
                mThirdPartyLoginRequest!!.loginSource = "GOOGLE"
                mThirdPartyLoginRequest!!.firstName = googleSignInAccount.displayName
                mThirdPartyLoginRequest!!.profileImageUrl = googleSignInAccount.photoUrl.toString()
                async(CommonPool) {
                    var googleAccessToken: String? = null
                    try {
                        googleAccessToken = GoogleAuthUtil.getToken(mILoginRegisterView as Context, googleSignInAccount.email,
                                "oauth2:profile email")
                    } catch (e: IOException) {
                        Timber.e(e.message)
                    } catch (e: GoogleAuthException) {
                        Timber.e(e.message)
                    }

                    if (googleAccessToken != null) {
                        googleOauthApiCall(googleAccessToken, mThirdPartyLoginRequest)
                    } else {
                        mThirdPartyLoginRequest!!.gender = ""
                    }
                }

            } else run {
                mILoginRegisterView?.showGoogleLoginFailedError()
            }
        }
    }

    private fun googleOauthApiCall(googleAccessToken: String?, thirdPartyLoginRequest: ThirdPartyLoginRequest?) {
//        mSubscriptions?.add(mILoginApi.callGoogleOauthApi(Constants.GOOGLE_GENDER_CALL + googleAccessToken).applySchedulersWithRetry().subscribe({
//            thirdPartyLoginRequest?.gender = it.gender
//        }, {
//            thirdPartyLoginRequest?.gender = ""
//        }))
    }



    private fun unlinkGoogleClient() {
        mGoogleApiClient?.stopAutoManage(this@LoginRegisterActivity)
        mGoogleApiClient?.disconnect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun finishLoginRegisterActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun showRegistrationFragment(fragment: Fragment, tag: String?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun removeFragment(tag: String) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun initNormalRegistration(userName: String, emailId: String, phoneNumber: String, password: String, memberProfileInput: NewUserRequest) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun setToolBarTitle(titleString: String) {
//
//    }

    protected fun showCustomProgressDialog(@Nullable onCancelListener: DialogInterface.OnCancelListener?) {
        if (!this.isFinishing) {
            mCustomProgressDialog?.show(onCancelListener)
        }
    }

//    fun loginApiCall(emailId: String?, password: String?, memberProfileInput: NewUserRequest? = null) {
//        mSubscriptions.add(mOauthApi.getObservableTokenSynchronously(USER_GRANT_TYPE, SCOPE,
//                emailId, password, null, environmentConfig.clientId, environmentConfig.clientSecret)
//                .schedulers().subscribe({
//                    updateLoginInfo(it, emailId, password, memberProfileInput)
//                }, {
//                    val retrofitException = it as RetrofitException
//                    if (retrofitException.response?.code() == 401) {
//                        mILoginRegisterView?.clearSmartLockCredentials()
//                        thirdPartyLoginStatusApiCall(emailId)
//                    } else {
//                        handleServerError(it)
//                    }
//                }))
//    }

}


