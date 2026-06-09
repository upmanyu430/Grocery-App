package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    var showOtp by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var isSendingOtp by remember { mutableStateOf(false) }
    var isVerifyingOtp by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to Grocery",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = com.example.ui.theme.Slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sign in to continue your fresh shopping",
            style = MaterialTheme.typography.bodyLarge,
            color = com.example.ui.theme.Slate500,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (!showOtp) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = com.example.ui.theme.BorderWhite40
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (phoneNumber.isNotBlank()) {
                        isSendingOtp = true
                        // Simulate sending OTP request
                        // Real application would call Firebase Auth or similar here
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isSendingOtp) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Send OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text("OR", style = MaterialTheme.typography.bodyMedium, color = com.example.ui.theme.Slate500)
            
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    onLoginSuccess() // Simulating Google sign in success
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = com.example.ui.theme.Slate800
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderWhite40)
            ) {
                Text("Sign in with Google", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Text(
                text = "Enter the 6-digit code sent to\n$phoneNumber",
                style = MaterialTheme.typography.bodyMedium,
                color = com.example.ui.theme.Slate600,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = otpCode,
                onValueChange = { if (it.length <= 6) otpCode = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("6-Digit OTP") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = com.example.ui.theme.BorderWhite40
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (otpCode.length == 6) {
                        isVerifyingOtp = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isVerifyingOtp) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Verify & Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = { 
                    showOtp = false
                    otpCode = ""
                }
            ) {
                Text("Back to Phone Number", color = MaterialTheme.colorScheme.primary)
            }
        }
    }

    LaunchedEffect(isSendingOtp) {
        if (isSendingOtp) {
            kotlinx.coroutines.delay(1000)
            isSendingOtp = false
            showOtp = true
        }
    }

    LaunchedEffect(isVerifyingOtp) {
        if (isVerifyingOtp) {
            kotlinx.coroutines.delay(1000)
            isVerifyingOtp = false
            onLoginSuccess()
        }
    }
}
