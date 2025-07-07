package com.example.latihanuikeren

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun EditStatusScreen(
    navController: NavController,
    statusId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var statusText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (statusId.isBlank()) {
            isLoading = false
            return@LaunchedEffect
        }
        val db = Firebase.firestore
        db.collection("statuses").document(statusId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val text = document.getString("text") ?: ""
                    statusText = text
                }
                isLoading = false
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to fetching data.", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = "Edit Status Anda")
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = statusText,
                onValueChange = { statusText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (statusText.isNotBlank()) {
                        updateStatus(statusId, statusText) { isSuccess ->
                            if (isSuccess) {
                                Toast.makeText(context, "Update Success!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Update Failed!.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update")
            }
        }
    }
}

private fun updateStatus(statusId: String, newText: String, onComplete: (Boolean) -> Unit) {
    val db = Firebase.firestore
    db.collection("statuses").document(statusId)
        .update("text", newText)
        .addOnSuccessListener { onComplete(true) }
        .addOnFailureListener { onComplete(false) }
}