package com.example.nc_lab_qr_code_mobile.templates

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nc_lab_qr_code_mobile.data.SessionRepository
import com.example.nc_lab_qr_code_mobile.util.Constants.QR_CODE_BASE_URL
import com.example.nc_lab_qr_code_mobile.util.createBarCode
import com.example.nc_lab_qr_code_mobile.util.deleteSessionId
import com.example.nc_lab_qr_code_mobile.util.loadSessionId
import com.example.nc_lab_qr_code_mobile.util.navigateTo

@Composable
fun QRCodeScreen(
    context: Context,
    sessionRepository: SessionRepository,
    navController: NavController
) {
    val sessionId = loadSessionId(context)
    val session = sessionRepository.session

    if (sessionId != null && session == null) {
        sessionRepository.fetch(sessionId) { updated ->
            if (updated) {
                navigateTo(navController, "qrCode")
            } else {
                Toast.makeText(
                    context,
                    "Failed to fetch session",
                    Toast.LENGTH_SHORT
                ).show()
                navigateTo(navController, "create")
            }
        }
    }

    session?.let {
        sessionRepository.listen(it.sessionId) { keyword ->
            if (keyword.isNotEmpty()) {
                val query = "geo:0,0?q=$keyword"
                val gmmIntentUri = Uri.parse(query)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "QR Code",
                fontSize = 40.sp,
                modifier = Modifier.padding(16.dp),
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (session == null) {
                    CircularProgressIndicator()
                } else {
                    val bitmap = createBarCode("$QR_CODE_BASE_URL/$sessionId")
                    val imageBitmap = remember(bitmap) {
                        bitmap.asImageBitmap()
                    }

                    Image(
                        bitmap = imageBitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .aspectRatio(1f)
                    )

                    Text(
                        text = session.name,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(
                        onClick = {
                            sessionId?.let {
                                sessionRepository.delete(it) { deleted ->
                                    if (deleted) {
                                        Toast.makeText(
                                            context,
                                            "Session deleted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        sessionRepository.stopListen()
                                        deleteSessionId(context)
                                        navigateTo(navController, "create")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed to delete session",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(300.dp, 100.dp)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Delete",
                            fontSize = 25.sp
                        )
                    }

                    Button(
                        onClick = {
                            sessionId?.let {
                                sessionRepository.getKeyword(it) { keyword ->
                                    if (keyword.isNotEmpty()) {
                                        val query = "geo:0,0?q=$keyword"
                                        val gmmIntentUri = Uri.parse(query)
                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        context.startActivity(mapIntent)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No Keyword Entered!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(300.dp, 100.dp)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Google Map",
                            fontSize = 25.sp
                        )
                    }
                }
            }

        }
    }
}