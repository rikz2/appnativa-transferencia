package com.example.appnativa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appnativa.screens.MainScreen
//import com.example.appnativa.ui.theme.AppnativaTheme
import com.example.compose.AppnativaTheme
import com.example.compose.backgroundDark
import com.example.compose.primaryDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App() // Aquí se llama a tu Composable principal
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun App() {
    // Crear el NavController
    AppnativaTheme(darkTheme = true) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation() // Llamar a la navegación de la app
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("main_screen"){ MainScreen()}
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Título
        Text(
            text = "Iniciar Sesión",
            style = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Campo de Correo Electrónico
        CustomOutlinedTextField(
            text = "Correo electronico",
            backgroundColor = Color.Transparent,
            textColor = Color.White,
            onValueChange = { newText ->
                println("Nuevo texto: $newText")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Campo de Contraseña
        CustomOutlinedTextField(
            text = "Contraseña",
            backgroundColor = Color.Transparent,
            textColor = Color.White,
            onValueChange = { newText ->
                println("Nuevo texto: $newText")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Botón de Login
        CustomButton(
            text = "Iniciar Sesión",
            onClick = { onLoginSuccess(navController) }, // Pasar navController a onLoginSuccess
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            backgroundColor = Color.Black,
            contentColor = Color.White,
            fontSize = 18,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Enlace para Registrarse
        Row {
            Text(text = "¿No tienes una cuenta?", color = primaryDark)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Regístrate ahora",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.clickable {
                    navController.navigate("register") // Navegar a la pantalla de registro
                }
            )
        }
    }
}


@Composable
fun RegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Título
        Text(
            text = "Regístrate",
            style = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Campo de Nombre
        CustomOutlinedTextField(
            text = "Nombre",
            backgroundColor = Color.Transparent,
            textColor = Color.White,
            onValueChange = { newText ->
                println("Nuevo texto: $newText")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Campo de Correo Electrónico
        CustomOutlinedTextField(
            text = "Correo Electrónico",
            backgroundColor = Color.Transparent,
            textColor = Color.White,
            onValueChange = { newText ->
                println("Nuevo texto: $newText")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Campo de Contraseña
        CustomOutlinedTextField(
            text = "Contraseña",
            backgroundColor = Color.Transparent,
            textColor = Color.White,
            onValueChange = { newText ->
                println("Nuevo texto: $newText")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Botón de Registro
        CustomButton(
            text = "Registrarse",
            onClick = { onRegisterSuccess(navController) }, // Pasar navController a onRegisterSuccess
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            backgroundColor = Color.Black,
            contentColor = Color.White,
            fontSize = 18,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(text = "¿Ya tienes una cuenta?", color = primaryDark)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Accede aquí",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.clickable {
                    navController.navigate("login") // Navegar a la pantalla de registro
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onValueChange: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = inputText,
        onValueChange = { newText: String -> // Explicitly specify the type
            inputText = newText
            onValueChange(newText)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
        label = { Text(text = text, color = textColor) }, // Use text as label
        singleLine = true,
        maxLines = 1,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    fontSize: Int,
    fontWeight: FontWeight = FontWeight.Normal
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(backgroundColor)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = fontSize.sp,
            fontWeight = fontWeight
        )
    }
}

// Función para navegar a MainScreen
fun onLoginSuccess(navController: NavController) {
    Log.d("on login success", "success")
    navController.navigate("main_screen") // Asegúrate de que esta ruta esté definida en tu NavHost
}

fun onRegisterSuccess(navController: NavController) {
    Log.d("on login success", "success")
    navController.navigate("main_screen") // Asegúrate de que esta ruta esté definida en tu NavHost
}
