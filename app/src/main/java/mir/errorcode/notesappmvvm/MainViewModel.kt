package mir.errorcode.notesappmvvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mir.errorcode.notesappmvvm.database.firebase.AppFirebaseRepository
import mir.errorcode.notesappmvvm.database.room.AppRoomDatabase
import mir.errorcode.notesappmvvm.database.room.repository.RoomRepository
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.utils.Constants.Keys.EMPTY
import mir.errorcode.notesappmvvm.utils.DB_TYPE
import mir.errorcode.notesappmvvm.utils.REPOSITORY
import mir.errorcode.notesappmvvm.utils.TYPE_FIREBASE
import mir.errorcode.notesappmvvm.utils.TYPE_ROOM
import kotlin.getValue
import kotlin.setValue

class MainViewModel(application : Application) : AndroidViewModel(application) {

    val context = application



    fun initDatabase(type: String, onSuccess: () -> Unit, onFail: (String) -> Unit = {}) {
        Log.d("checkData", "MainViewModel initDatabase with type: $type")
        when(type) {
            TYPE_ROOM -> {
                val dao = AppRoomDatabase.getInstance(context = context).getRoomDao()
                REPOSITORY = RoomRepository(dao)
                onSuccess()
            }
            TYPE_FIREBASE -> {
                REPOSITORY = AppFirebaseRepository()
                REPOSITORY.connectToDatabase(onSuccess, onFail)
            }
        }
    }

    fun addNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.create(note = note) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }



    fun updateNote(note: Note, onSuccess: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.update(note= note) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun deleteNote(note: Note, onSuccess: () -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.delete(note = note){
                viewModelScope.launch(Dispatchers.Main){
                    onSuccess()
                }
            }
        }
    }


    fun readAllNotes() = REPOSITORY.readAll

    fun signOut(onSuccess: () -> Unit){
        when(DB_TYPE.value){
            TYPE_FIREBASE,
                TYPE_ROOM -> {
                    REPOSITORY.singOut()
                    DB_TYPE.value = EMPTY
                    onSuccess()
                }
            else -> {Log.d("MainViewModel", "singOut: Else: ${DB_TYPE.value}" )}
        }

    }

}

class MainViewModelFactory(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}