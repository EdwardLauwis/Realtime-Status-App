package com.example.latihanuikeren

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
){
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    val statusList = viewModel.statusList.value

    var statusText by remember{ mutableStateOf("") }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, ${currentUser?.email}")
        Spacer(modifier =  Modifier.height(24.dp))
        OutlinedTextField(
            value = statusText,
            onValueChange = { statusText = it },
            label = { Text("Write your status...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier =  Modifier.height(16.dp))
        Button(
            onClick = {
                if (statusText.isNotBlank()){
                    viewModel.postStatus(statusText){ isSuccess ->
                        if (isSuccess){
                            statusText = ""
                            Toast.makeText(context, "Success to post", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to post", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Post")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn (
            modifier = Modifier.weight(1f)
        ) {
            items(statusList) { status ->
                StatusItem(
                    status = status,
                    onDelete = {
                        viewModel.deleteStatus(status.id){

                        }
                    },
                    onEdit = {
                        navController.navigate("edit_screen/${status.id}")
                    },
                    onLike = {
                        viewModel.likeStatus(status.id)
                    },
                    onProfileClick = {
                        navController.navigate("profile_screen/${status.userId}")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.signOut()
            navController.navigate("login_screen"){
                popUpTo(0)
            }
        }) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun StatusItem(
    status: Status,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onLike: () -> Unit,
    onProfileClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    Card (
        modifier = Modifier.fillMaxWidth().clickable{onProfileClick()}
    ) {
        Column (
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status.userEmail,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                if (status.userEmail == currentUser?.email){
                    Row {
                        IconButton(onClick = onEdit) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Status"
                            )
                        }
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Status"
                            )
                        }
                    }
                }
            }
            Text(
                text = status.text,
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onLike) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Like Status"
                    )
                }
                Text(
                    text = status.likeCount.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}