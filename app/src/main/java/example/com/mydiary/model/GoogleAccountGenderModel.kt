package example.com.mydiary.model

import com.google.gson.annotations.SerializedName

class GoogleAccountGenderModel {
    @SerializedName("gender")
    private var gender: String? = null

    fun getGender(): String? {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }
}
