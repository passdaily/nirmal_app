package info.passdaily.nirmala_convent_app.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class FileResultModel(
    @SerializedName("DETAILS")
    val dETAILS: String,
    @SerializedName("RESULT")
    val rESULT: String
)