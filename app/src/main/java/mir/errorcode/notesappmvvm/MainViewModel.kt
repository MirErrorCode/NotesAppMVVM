package mir.errorcode.notesappmvvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.utils.TYPE_FIREBASE
import mir.errorcode.notesappmvvm.utils.TYPE_ROOM
import kotlin.getValue
import kotlin.setValue

class MainViewModel(application : Application) : AndroidViewModel(application) {

    val readTest: MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>()
    }

    val dbType: MutableLiveData<String> by lazy {
        MutableLiveData<String>(TYPE_ROOM)
    }

    init {
        readTest.value =
            when(dbType.value){
                TYPE_ROOM -> { listOf<Note>(
                    Note(title = "Note 1", subtitle = "Subtitle fot note 1"),
                    Note(title = "Note 2", subtitle = "Subtitle fot note 2"),
                    Note(title = "Note 3", subtitle = "Subtitle fot note 3"),
                    Note(title = "Note 4", subtitle = "Subtitle fot note 4"),
                ) }

                TYPE_FIREBASE -> listOf()
                else -> listOf()

            }
    }


    fun initDatabase(type: String){
        dbType.value = type
        Log.d("checkData", "MainViewModel initDatabase with type: $type")
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