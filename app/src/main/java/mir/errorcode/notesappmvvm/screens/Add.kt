package mir.errorcode.notesappmvvm.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import mir.errorcode.notesappmvvm.MainViewModel
import mir.errorcode.notesappmvvm.MainViewModelFactory
import mir.errorcode.notesappmvvm.model.Note
import mir.errorcode.notesappmvvm.navigation.NavRoute
import mir.errorcode.notesappmvvm.ui.theme.NotesAppMVVMTheme


@Composable
fun AddScreen(navController: NavHostController, viewModel: MainViewModel) {

    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var isButtonEnables by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Add new note",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange =  {
                    title = it
                    isButtonEnables = title.isNotEmpty() && subtitle.isNotEmpty()
                                 },
                label = {Text(text= "Note title")},
                isError = title.isEmpty()
            )
            OutlinedTextField(
                value = subtitle,
                onValueChange =  {
                    subtitle = it
                    isButtonEnables = title.isNotEmpty() && subtitle.isNotEmpty()
                                 },
                label = {Text(text= "Note subtitle")},
                isError = subtitle.isEmpty()
            )

            Button(onClick = {
                viewModel.addNote(note = Note(title = title, subtitle = subtitle)) {
                    navController.navigate(NavRoute.Main.route)
                }

            }, enabled = isButtonEnables, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Add note")

            }
        }

    }
}





@Preview(showBackground = true)
@Composable
fun pverAddScreen(){
    NotesAppMVVMTheme {
        val context = LocalContext.current
        val mViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context.applicationContext as Application))
        AddScreen(navController = rememberNavController(), viewModel = mViewModel)
    }
}