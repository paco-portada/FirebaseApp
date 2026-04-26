package com.example.firebaseapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _currentUser = mutableStateOf(auth.currentUser)
    val currentUser: State<FirebaseUser?> = _currentUser

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al iniciar sesión")
                }
            }
    }

    fun register(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al registrarse")
                }
            }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }
}
