package com.example.firebaseapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.firebaseapp.model.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class TaskViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private var snapshotListener: ListenerRegistration? = null
    
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> = _tasks

    fun getTasks() {
        val userId = auth.currentUser?.uid ?: return
        
        // Evitar múltiples listeners
        if (snapshotListener != null) return

        snapshotListener = db.collection("tasks")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                if (value != null) {
                    _tasks.clear()
                    val taskList = value.toObjects(Task::class.java)
                    _tasks.addAll(taskList)
                }
            }
    }

    fun clearTasks() {
        snapshotListener?.remove()
        snapshotListener = null
        _tasks.clear()
    }

    override fun onCleared() {
        super.onCleared()
        snapshotListener?.remove()
    }

    fun addTask(title: String, description: String, date: String) {
        val userId = auth.currentUser?.uid ?: return
        val id = db.collection("tasks").document().id
        val task = Task(id, title, description, date, false, userId)
        db.collection("tasks").document(id).set(task)
    }

    fun updateTask(task: Task) {
        db.collection("tasks").document(task.id).set(task)
    }

    fun deleteTask(taskId: String) {
        db.collection("tasks").document(taskId).delete()
    }
}
