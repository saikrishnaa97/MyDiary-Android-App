package example.com.mydiary.network

import example.com.mydiary.model.GoogleAccountGenderModel
import retrofit2.http.GET
import retrofit2.http.Url
import rx.Observable

/**
 * Created by saikr on 31-03-2018.
 */
interface ILoginApi {

    @GET
    fun callGoogleOauthApi(@Url url: String): Observable<GoogleAccountGenderModel>

}