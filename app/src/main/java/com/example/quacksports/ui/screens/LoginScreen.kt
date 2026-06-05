package com.example.quacksports.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.ui.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val callbackManager = remember { CallbackManager.Factory.create() }
    
    // Google Sign-In Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { token ->
                viewModel.signInWithGoogle(token, onLoginSuccess)
            }
        } catch (e: ApiException) {
            viewModel.errorMessage = "Erro Google: ${e.message}"
        }
    }

    // Facebook Login (Manual trigger as SDK doesn't have a simple Compose contract yet)
    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                viewModel.signInWithFacebook(result.accessToken.token, onLoginSuccess)
            }
            override fun onCancel() {
                viewModel.errorMessage = "Login cancelado"
            }
            override fun onError(error: FacebookException) {
                viewModel.errorMessage = "Erro Facebook: ${error.message}"
            }
        })
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "QUACKSPORTS",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE51D53)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = viewModel.loginEmail,
            onValueChange = { viewModel.loginEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = viewModel.loginPassword,
            onValueChange = { viewModel.loginPassword = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        viewModel.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { viewModel.login(onLoginSuccess) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE51D53)),
            enabled = !viewModel.isLoading
        ) {
            Text("Entrar", fontSize = 16.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("OU", color = Color.Gray, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(16.dp))

        // Google Button
        OutlinedButton(
            onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("359623127969-kpde5v7pkpo6m9rg4pbj5tqihihqj1pr.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar com Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Facebook Button
        Button(
            onClick = {
                // For simplicity in this example, we trigger the Facebook login directly.
                // Note: In a real app, you might need to handle the activity result for Facebook too.
                // But typically LoginManager handles its own activity.
                LoginManager.getInstance().logInWithReadPermissions(context as androidx.activity.ComponentActivity, listOf("email", "public_profile"))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2))
        ) {
            Text("Entrar com Facebook")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onNavigateToRegister) {
            Text("Não tem uma conta? Cadastre-se", color = Color(0xFFE51D53))
        }
    }
}
