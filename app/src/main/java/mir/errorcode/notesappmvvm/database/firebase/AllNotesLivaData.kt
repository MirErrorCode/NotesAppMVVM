package mir.errorcode.notesappmvvm.database.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.utils.Constants.Keys.FIRE_SUBTITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.FIRE_TITLE
import mir.errorcode.notesappmvvm.utils.FIREBASE_ID


class AllNotesLivaData : LiveData<List<Note>>() {
    private val mAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference
        .child(mAuth.currentUser?.uid.toString())

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val notes = mutableListOf<Note>()
            snapshot.children.map {
                val title = it.child(FIRE_TITLE).getValue(String::class.java) ?: ""
                val subtitle = it.child(FIRE_SUBTITLE).getValue(String::class.java) ?: ""
                val firebaseId = it.child(FIREBASE_ID).getValue(String::class.java) ?: ""
                val note = Note(id = 0, title = title, subtitle = subtitle, firebaseId = firebaseId)
                Log.d(
                    "AllNotesLiveData",
                    "Loaded note: key=${it.key}, firebaseId=$firebaseId, title=$title, subtitle=$subtitle"
                )
                Log.d("AllNotesLiveData", "Raw snapshot data: ${it.value}")
                notes.add(note)

            }
            Log.d("AllNotesLiveData", "Total notes loaded: ${notes.size}, notes=$notes")
            value = notes
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("AllNotesLiveData", "Database error: ${error.message}")
        }

    }

    override fun onActive() {
        database.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        database.removeEventListener(listener)
        super.onInactive()
    }
}