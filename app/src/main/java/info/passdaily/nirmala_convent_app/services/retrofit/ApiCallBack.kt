package info.passdaily.nirmala_convent_app.services.retrofit

interface ApiCallBack<T> {

    fun  onFailure(error : String)
    fun  onError(error : String)
    fun  onSuccess(response : T)
}