package info.passdaily.nirmala_convent_app.lib

interface ImageUploadCallback {
    fun onProgressUpdate(percentage: Int)
    fun onError(message: String?)
    fun onSuccess(message: String?)
}