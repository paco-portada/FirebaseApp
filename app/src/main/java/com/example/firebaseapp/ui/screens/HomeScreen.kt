package com.example.firebaseapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.firebaseapp.model.Task
import com.example.firebaseapp.viewmodel.AuthViewModel
import com.example.firebaseapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    taskViewModel: TaskViewModel,
    onLogout: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    LaunchedEffect(Unit) {
        taskViewModel.getTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                actions = {
                    IconButton(onClick = {
                        taskViewModel.clearTasks()
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                taskToEdit = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir tarea")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(taskViewModel.tasks) { task ->
                TaskItem(
                    task = task,
                    onEdit = {
                        taskToEdit = task
                        showDialog = true
                    },
                    onDelete = { taskViewModel.deleteTask(task.id) },
                    onToggleComplete = {
                        taskViewModel.updateTask(task.copy(completed = !task.completed))
                    }
                )
            }
        }

        if (showDialog) {
            TaskDialog(
                task = taskToEdit,
                onDismiss = { showDialog = false },
                onConfirm = { title, desc, date ->
                    if (taskToEdit == null) {
                        taskViewModel.addTask(title, desc, date)
                    } else {
                        taskViewModel.updateTask(taskToEdit!!.copy(title = title, description = desc, date = date))
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleComplete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggleComplete() }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                Text(text = task.description, style = MaterialTheme.typography.bodySmall)
                Text(text = task.date, style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
fun TaskDialog(
    task: Task?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var desc by remember { mutableStateOf(task?.description ?: "") }
    var date by remember { mutableStateOf(task?.date ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (task == null) "Añadir Tarea" else "Editar Tarea") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = date, onValueChange = { date = it }, label = { Text("Fecha") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(title, desc, date) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
