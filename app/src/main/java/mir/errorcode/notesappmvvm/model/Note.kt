package mir.errorcode.notesappmvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName
import mir.errorcode.notesappmvvm.utils.Constants.Keys.FIRE_SUBTITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.FIRE_TITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.NOTES_TABLE
import mir.errorcode.notesappmvvm.utils.FIREBASE_ID


@Entity(tableName = NOTES_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    @PropertyName(FIRE_TITLE)
    val title: String = "",
    @ColumnInfo
    @PropertyName(FIRE_SUBTITLE)
    val subtitle: String = "",
    @PropertyName(FIREBASE_ID)
    val firebaseId: String = "",
)
