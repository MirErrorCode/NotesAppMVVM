package mir.errorcode.notesappmvvm.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mir.errorcode.notesappmvvm.MainViewModel
import mir.errorcode.notesappmvvm.MainViewModelFactory
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.navigation.NavRoute
import mir.errorcode.notesappmvvm.ui.theme.NotesAppMVVMTheme
import mir.errorcode.notesappmvvm.utils.Constants.Keys.DELETE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.EDIT_NOTE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.EMPTY
import mir.errorcode.notesappmvvm.utils.Constants.Keys.NAV_BACK
import mir.errorcode.notesappmvvm.utils.Constants.Keys.NONE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.SUBTITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.TITLE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.UPDATE
import mir.errorcode.notesappmvvm.utils.Constants.Keys.UPDATE_NOTE
import mir.errorcode.notesappmvvm.utils.DB_TYPE
import mir.errorcode.notesappmvvm.utils.TYPE_FIREBASE
import mir.errorcode.notesappmvvm.utils.TYPE_ROOM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(navController: NavHostController, viewModel: MainViewModel, noteId: String?) {
    Log.d("NoteScreen", "Received noteId = $noteId")
    val notes = viewModel.readAllNotes().observeAsState(listOf()).value
    Log.d("NoteScreen", "All notes = $notes")
    val note = when (DB_TYPE) {
        TYPE_ROOM -> {
            notes.firstOrNull { it.id == noteId?.toInt() } ?: Note()
            // Log.d("NoteScreen", "Invalid noteId for ROOM = $noteId")

        }

        TYPE_FIREBASE -> {
            Log.d("NoteScreen", "Searching Firebase note with firebaseId=$noteId")
            notes.firstOrNull { it.firebaseId == noteId } ?: Note()
        }

        else -> Note()
    }
    var title by remember { mutableStateOf(EMPTY) }
    var subtitle by remember { mutableStateOf(EMPTY) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true },
    )

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = note.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = note.subtitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    title = note.title
                    subtitle = note.subtitle
                    coroutineScope.launch { bottomSheetState.show() }
                }) {
                    Text(text = UPDATE)
                }

                Button(onClick = {
                    viewModel.deleteNote(note = note) {
                        navController.navigate(NavRoute.Main.route)
                    }
                }) {
                    Text(text = DELETE)
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                onClick = {
                    navController.navigate(NavRoute.Main.route)
                }
            ) {
                Text(text = NAV_BACK)
            }
        }
    }

    if (bottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { bottomSheetState.hide() }
            },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        ) {
            Surface {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)) {
                    Text(
                        text = EDIT_NOTE,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(text = TITLE) },
                        isError = title.isEmpty()
                    )
                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text(text = SUBTITLE) },
                        isError = subtitle.isEmpty()
                    )
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            viewModel.updateNote(
                                note =
                                    Note(
                                        id = note.id,
                                        title = title,
                                        subtitle = subtitle,
                                        firebaseId = note.firebaseId
                                    )
                            ) {
                                coroutineScope.launch { bottomSheetState.hide() }
                            }
                        }
                    ) {
                        Text(text = UPDATE_NOTE)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun prevNoteScreen() {
    NotesAppMVVMTheme {
        val context = LocalContext.current
        val mViewModel: MainViewModel =
            viewModel(factory = MainViewModelFactory(context.applicationContext as Application))
        NoteScreen(
            navController = rememberNavController(),
            viewModel = mViewModel,
            noteId = "1"
        )
    }
}