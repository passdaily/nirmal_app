package info.passdaily.nirmala_convent_app.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ZakAccessTokenModel(
    @SerializedName("token")
    var token: String
)