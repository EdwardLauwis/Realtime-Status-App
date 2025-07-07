package com.example.latihanuikeren

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.latihanuikeren.ui.theme.LatihanUIKerenTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bagian UI Anda sudah benar
        Text(text = "WELCOME", fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Login to continue", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        val context = LocalContext.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        OutlinedTextField(value = email, onValueChange = { email = it}, label = { Text("Email") }, /*...*/)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it}, label = { Text("Password") }, /*...*/)
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val auth = FirebaseAuth.getInstance()
                if (email.isNotBlank() && password.isNotBlank()){
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("home_screen"){
                                popUpTo("login_screen") { inclusive = true }
                            }
                        }
                        else Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show()
                    }
                } else Toast.makeText(context, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("L o g i n")
        }

        // TAMBAHKAN INI: Teks untuk navigasi
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString("Belum punya akun? Daftar di sini"),
            onClick = {
                navController.navigate("signup_screen")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LatihanUIKerenTheme {
        // Kita buat NavController palsu agar preview tidak error
        LoginScreen(navController = rememberNavController())
    }
}