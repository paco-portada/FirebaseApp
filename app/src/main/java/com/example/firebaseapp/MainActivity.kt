package com.example.firebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseapp.ui.screens.HomeScreen
import com.example.firebaseapp.ui.screens.LoginScreen
import com.example.firebaseapp.ui.screens.RegisterScreen
import com.example.firebaseapp.ui.theme.FirebaseAppTheme
import com.example.firebaseapp.viewmodel.AuthViewModel
import com.example.firebaseapp.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()

    val startDestination = if (authViewModel.currentUser.value != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                authViewModel = authViewModel,
                taskViewModel = taskViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
