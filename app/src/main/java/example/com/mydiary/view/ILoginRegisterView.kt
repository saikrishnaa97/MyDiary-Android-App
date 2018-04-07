package example.com.mydiary.view

import example.com.mydiary.model.AccountData
import example.com.mydiary.model.OAuthResponse
import example.com.mydiary.utils.IMvp

interface ILoginRegisterView : IMvp {
    fun oauthApiResponse(mOAuthResponse: OAuthResponse)
    fun loginSuccess(mAccountData: AccountData, isThirdPartyLogin: Boolean)
    fun finishActivity()
    fun showGoogleLoginFailedError()
    fun showProgressDialog()
    fun hideProgressDialog()
    fun showError(errorMessage: String?, isLoginFailed: Boolean)
    fun showServerErrorDialog()
    fun initFacebookLoginPermission()
    fun clearSmartLockCredentials()
    fun showThirdPartyExistDialog(messageId: Int, isFacebookLogin: Boolean, isGoogleLogin: Boolean)
    fun userIsAlreadyExistsCallBack()
}
