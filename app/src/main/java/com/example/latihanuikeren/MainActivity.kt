package com.example.latihanuikeren

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.latihanuikeren.ui.theme.LatihanUIKerenTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LatihanUIKerenTheme {
                Scaffold( modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "home_screen" else "login_screen"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable ("login_screen") {
            LoginScreen(navController = navController)
        }
        composable ("signup_screen") {
            SignUpScreen(navController = navController)
        }
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
        composable("edit_screen/{statusId}", arguments = listOf(navArgument("statusId"){ type = NavType.StringType})) { backStackEntry ->
            val statusId = backStackEntry.arguments?.getString("statusId") ?: ""
            EditStatusScreen(
                navController = navController,
                statusId = statusId
            )
        }
        composable(
            route = "profile_screen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                navController = navController,
                userId = userId
            )
        }
    }
}