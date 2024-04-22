package com.example.nc_lab_qr_code_mobile.templates

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nc_lab_qr_code_mobile.R
import com.example.nc_lab_qr_code_mobile.data.SessionRepository
import com.example.nc_lab_qr_code_mobile.util.navigateTo
import com.example.nc_lab_qr_code_mobile.util.saveSessionId

@Composable
fun CreateScreen(
    context: Context,
    sessionRepository: SessionRepository,
    navController: NavController
) {
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
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_remote
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(40.dp)
                        .size(280.dp),
                    colorFilter = ColorFilter.tint(Color.Blue)
                )
                Button(
                    onClick = {
                        sessionRepository.create {
                            if (it == null) {
                                Toast.makeText(
                                    context,
                                    "Failed to create session!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Session created!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                saveSessionId(context, it)
                                navigateTo(navController, "qrCode")
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(300.dp, 100.dp)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Create QR Code",
                        fontSize = 25.sp
                    )
                }
            }
        }

    }
}