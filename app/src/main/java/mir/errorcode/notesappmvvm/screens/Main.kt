package mir.errorcode.notesappmvvm.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mir.errorcode.notesappmvvm.MainViewModel
import mir.errorcode.notesappmvvm.MainViewModelFactory
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.navigation.NavRoute
import mir.errorcode.notesappmvvm.ui.theme.NotesAppMVVMTheme
import androidx.compose.runtime.livedata.observeAsState


@Composable
fun MainScreen(navController: NavHostController){
    val context = LocalContext.current
    val mViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

    val notes = mViewModel.readTest.observeAsState(listOf()).value


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavRoute.Add.route) }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        },
         content = { innerPadding ->
             LazyColumn(
                 modifier = Modifier
                    .padding(innerPadding)
             ) {
                 items(notes)  { note ->
                     NoteItem(note = note, navController = navController)

                 }
             }
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//            ) {
//                NoteItem(title = "Note 1", subtitle = "Subtitle fot not 1 ", navController = navController)
//                NoteItem(title = "Note 2", subtitle = "Subtitle fot not 2 ", navController = navController)
//                NoteItem(title = "Note 3", subtitle = "Subtitle fot not 3 ", navController = navController)
//                NoteItem(title = "Note 4", subtitle = "Subtitle fot not 4 ", navController = navController)
//            }


        })



}

@Composable
fun NoteItem(note: Note, navController: NavHostController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal =  24.dp)
            .clickable {
                navController.navigate(NavRoute.Note.route)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = note.title, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text(text = note.subtitle)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun prevMainScreen(){
    NotesAppMVVMTheme {
        MainScreen(navController = rememberNavController())
    }
}