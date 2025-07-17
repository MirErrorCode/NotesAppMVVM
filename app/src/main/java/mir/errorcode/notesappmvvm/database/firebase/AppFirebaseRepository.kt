package mir.errorcode.notesappmvvm.database.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import mir.errorcode.notesappmvvm.database.DatabaseRepository
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.utils.Constants.Keys.FIRE_SUBTITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.FIRE_TITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.SUBTITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.TITLE
import mir.errorcode.notesappmvvm.utils.FIREBASE_ID
import mir.errorcode.notesappmvvm.utils.LOGIN
import mir.errorcode.notesappmvvm.utils.PASSWORD

class AppFirebaseRepository : DatabaseRepository {

    private val mAuth = FirebaseAuth.getInstance()

    private val database = com.google.firebase.ktx.Firebase.database.reference
        .child(mAuth.currentUser?.uid.toString())

    override val readAll: LiveData<List<Note>> = AllNotesLivaData()

    override suspend fun create(
        note: Note,
        onSuccess: () -> Unit
    ) {
        //  val noteId = database.push().key.toString()
        val noteId = note.firebaseId
        val mapNotes = hashMapOf<String, Any>()

        mapNotes[FIREBASE_ID] = noteId
        mapNotes[FIRE_TITLE] = note.title
        mapNotes[FIRE_SUBTITLE] = note.subtitle
        Log.d(
            "AppFirebaseRepository",
            "Creating note: noteId=$noteId, inputFirebaseId=${note.firebaseId}, title=${note.title}, subtitle=${note.subtitle}"
        )
        database.child(noteId).updateChildren(mapNotes)
            .addOnSuccessListener {
                onSuccess()
                Log.d("AppFirebaseRepository", "Note created successfully: noteId=$noteId")
            }
            .addOnFailureListener {
                Log.e(
                    "AppFirebaseRepository",
                    "Failure to add new note: ${it.message}"
                )
            }
    }

    override suspend fun update(
        note: Note,
        onSuccess: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
        note: Note,
        onSuccess: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun singOut() {
        mAuth.signOut()
    }

    override fun connectToDatabase(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        Log.d("StartScreen", "Attempting sign-in with email=$LOGIN")
        mAuth.signInWithEmailAndPassword(LOGIN, PASSWORD)
            .addOnSuccessListener {
                Log.d("StartScreen", "Sign-in successful: UID=${mAuth.currentUser?.uid}")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("StartScreen", "Sign-in failed: ${exception.message}")
                onFail(exception.message.toString())
            }
    }
}