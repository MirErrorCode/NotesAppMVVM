package mir.errorcode.notesappmvvm.navigation

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mir.errorcode.notesappmvvm.screens.Add
import mir.errorcode.notesappmvvm.screens.Main
import mir.errorcode.notesappmvvm.screens.Start
import mir.errorcode.notesappmvvm.screens.Note


sealed class NavRoute(val route: String){
    object Start: NavRoute("start_screen")
    object Main: NavRoute("main_screen")
    object Add: NavRoute("add_screen")
    object Note: NavRoute("note_screen")
}



@Composable
fun NotesNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Start.route) {
        composable (NavRoute.Start.route) { Start(navController = navController) }
        composable (NavRoute.Main.route) { Main(navController = navController) }
        composable (NavRoute.Add.route) { Add(navController = navController) }
        composable (NavRoute.Note.route) { Note(navController = navController) }
    }
}