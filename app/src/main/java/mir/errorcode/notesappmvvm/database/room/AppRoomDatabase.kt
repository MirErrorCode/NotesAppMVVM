package mir.errorcode.notesappmvvm.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import mir.errorcode.notesappmvvm.database.room.dao.NoteRoomDao
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.utils.Constants.Keys.NOTES_TABLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.NOTE_DATABASE


@Database(entities = [Note::class], version = 2)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun getRoomDao(): NoteRoomDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(database : SupportSQLiteDatabase){
                database.execSQL("ALTER TABLE $NOTES_TABLE ADD COLUMN firebaseId TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getInstance(context: Context): AppRoomDatabase {
            return if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppRoomDatabase::class.java,
                    NOTE_DATABASE
                ).addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE as AppRoomDatabase
            } else INSTANCE as AppRoomDatabase
        }

    }
}