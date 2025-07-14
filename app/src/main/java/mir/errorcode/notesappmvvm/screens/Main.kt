package mir.errorcode.notesappmvvm.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mir.errorcode.notesappmvvm.navigation.NavRoute
import mir.errorcode.notesappmvvm.ui.theme.NotesAppMVVMTheme


@Composable
fun MainScreen(navController: NavHostController){

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NoteItem(title = "Note 1", subtitle = "Subtitle fot not 1 ", navController = navController)
                NoteItem(title = "Note 2", subtitle = "Subtitle fot not 2 ", navController = navController)
                NoteItem(title = "Note 3", subtitle = "Subtitle fot not 3 ", navController = navController)
                NoteItem(title = "Note 4", subtitle = "Subtitle fot not 4 ", navController = navController)
            }


        })



}

@Composable
fun NoteItem(title: String, subtitle: String, navController: NavHostController){
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
            Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle)
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