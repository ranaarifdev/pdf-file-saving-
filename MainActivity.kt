package com.example.myapplication

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("document") { DocumentScreen() }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    var CNICNO by remember { mutableStateOf("") }
    var StudentID by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Rana Arif", color = Color.Blue)

        OutlinedTextField(
            value = CNICNO,
            onValueChange = { CNICNO = it },
            label = { Text("Enter Your Name", color = Color.Blue) }
        )
        Spacer(Modifier.height(19.dp))

        OutlinedTextField(
            value = StudentID,
            onValueChange = { StudentID = it },
            label = { Text("Roll no", color = Color.Blue) }
        )
        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
                if (CNICNO == "Rana Arif" && StudentID == "45") {
                    message = "Accepted"
                    navController.navigate("document")
                } else {
                    message = "No record found"
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Sign in")
        }

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
                navController.navigate("signup")
            },
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Sign up")
        }

        Spacer(Modifier.height(20.dp))

        if (message.isNotEmpty()) {
            Text(text = message, color = Color.Red)
        }
    }
}

@Composable
fun SignUpScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf("") }
    var program by remember { mutableStateOf("") }
    var session by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up", color = Color.Blue)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Your Name", color = Color.Blue) }
        )
        Spacer(Modifier.height(19.dp))

        OutlinedTextField(
            value = rollNo,
            onValueChange = { rollNo = it },
            label = { Text("Roll no", color = Color.Blue) }
        )
        Spacer(Modifier.height(15.dp))

        OutlinedTextField(
            value = program,
            onValueChange = { program = it },
            label = { Text("Program", color = Color.Blue) }
        )
        Spacer(Modifier.height(15.dp))

        OutlinedTextField(
            value = session,
            onValueChange = { session = it },
            label = { Text("Session", color = Color.Blue) }
        )
        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && rollNo.isNotEmpty() && program.isNotEmpty() && session.isNotEmpty()) {
                    // Navigate to Document Screen
                    navController.navigate("document")
                } else {
                    message = "No record found"
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Submit")
        }

        Spacer(Modifier.height(20.dp))

        if (message.isNotEmpty()) {
            Text(text = message, color = Color.Red)
        }
    }
}

@Composable
fun DocumentScreen() {
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Register file picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            pdfUri = it
            // Save PDF to internal storage with proper context handling
            savePdfToInternalStorage(uri, context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Documents", color = Color.Blue)

        Button(
            onClick = {
                filePickerLauncher.launch("application/pdf")
            },
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Add PDF Document")
        }

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
                // Example URL for downloading PDF
                val url = "https://example.com/sample.pdf"
                downloadPdf(context, url)
            },
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Download PDF")
        }

        Spacer(Modifier.height(20.dp))

        if (message.isNotEmpty()) {
            Text(text = message, color = Color.Red)
        }
    }
}

fun savePdfToInternalStorage(uri: Uri, context: Context) {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val fileName = "saved_document.pdf"
    val file = File(context.filesDir, fileName)

    try {
        inputStream?.let {
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (it.read(buffer).also { len -> length = len } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                context.showToast("Document saved successfully")
            }
        }
    } catch (e: Exception) {
        context.showToast("Failed to save document: ${e.message}")
    }
}

fun downloadPdf(context: Context, url: String) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri = Uri.parse(url)
    val request = DownloadManager.Request(uri).apply {
        setTitle("Downloading PDF")
        setDescription("Downloading PDF document")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_document.pdf")
    }
    downloadManager.enqueue(request)
}

fun Context.showToast(message: String) {
    android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
}
