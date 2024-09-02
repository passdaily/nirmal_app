package info.passdaily.nirmala_convent_app.typeofuser.parent.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import info.passdaily.nirmala_convent_app.MainRepository
import info.passdaily.nirmala_convent_app.services.Resource
import kotlinx.coroutines.Dispatchers

class CalendarViewModel(private val mainRepository: MainRepository) : ViewModel() {
    var TAG = "CalendarViewModel"

    fun getActivityList(studentId : Int,academicId: Int) = liveData(
        Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getActivityListNew(studentId,academicId)))
        } catch (exception: Exception) {
            Log.i(TAG, "exception $exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getHolidayList(classId : Int,studentId: Int) = liveData(Dispatchers.IO) {

        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getHolidayListNew(classId,studentId)))
        } catch (exception: Exception) {
            Log.i(TAG, "exception $exception")
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}