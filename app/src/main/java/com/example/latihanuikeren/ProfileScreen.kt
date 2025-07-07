package com.example.latihanuikeren

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String,
    modifier: Modifier = Modifier
) {
    var statusList by remember { mutableStateOf(listOf<Status>()) }
    var userEmail by remember { mutableStateOf("") }

    // Ambil data status untuk userId yang spesifik
    LaunchedEffect(userId) {
        if (userId.isBlank()) return@LaunchedEffect

        val db = Firebase.firestore
        db.collection("statuses")
            .whereEqualTo("userId", userId) // Filter berdasarkan userId yang diterima
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { return@addSnapshotListener }

                if (snapshot != null) {
                    val statuses = snapshot.documents.mapNotNull { document ->
                        val statusData = document.toObject(Status::class.java)
                        // Ambil email dari data pertama untuk ditampilkan di judul
                        if (userEmail.isEmpty()) {
                            userEmail = statusData?.userEmail ?: ""
                        }
                        statusData?.copy(id = document.id)
                    }
                    statusList = statuses
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tampilkan judul halaman profil
        Text(
            text = "Status dari: $userEmail",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))


        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(statusList) { status ->
                val homeViewModel: HomeViewModel = viewModel()
                StatusItem(
                    status = status,
                    onEdit = { navController.navigate("edit_screen/${status.id}") },
                    onDelete = { homeViewModel.deleteStatus(status.id) {} },
                    onLike = { homeViewModel.likeStatus(status.id) },
                    onProfileClick = { /* Tidak melakukan apa-apa di halaman profil */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}