package example.com.mydiary.model

import com.google.gson.annotations.SerializedName

/**
 * Created by saikr on 31-03-2018.
 */
data class MemberProfileInput(@SerializedName("firstName") var firstName: String?,
                              @SerializedName("emailAddress") var emailAddress: String?,
                              @SerializedName("gender") var gender: String?,
                              @SerializedName("handphone") var handphone: String? = null)