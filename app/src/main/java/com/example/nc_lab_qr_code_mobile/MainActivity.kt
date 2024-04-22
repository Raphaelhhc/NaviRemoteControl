package com.example.nc_lab_qr_code_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nc_lab_qr_code_mobile.data.SessionRepository
import com.example.nc_lab_qr_code_mobile.templates.CreateScreen
import com.example.nc_lab_qr_code_mobile.templates.QRCodeScreen
import com.example.nc_lab_qr_code_mobile.ui.theme.NclabqrcodemobileTheme
import com.example.nc_lab_qr_code_mobile.util.loadSessionId
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionId = loadSessionId(this)

        setContent {
            NclabqrcodemobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = if (sessionId == null) "create" else "qrCode",
                    ) {
                        composable("create") {
                            CreateScreen(
                                context = this@MainActivity,
                                sessionRepository = sessionRepository,
                                navController = navController,
                            )
                        }
                        composable("qrCode") {
                            QRCodeScreen(
                                context = this@MainActivity,
                                sessionRepository = sessionRepository,
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        println("onRestart!")
    }
    override fun onStart() {
        super.onStart()
        println("onStart!")
    }
    override fun onResume() {
        super.onResume()
        println("onResume!")
    }
    override fun onPause() {
        super.onPause()
        println("onPause!")
    }
    override fun onStop() {
        super.onStop()
        println("onStop!")
    }
    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy!")
        sessionRepository.stopListen()
    }

}
