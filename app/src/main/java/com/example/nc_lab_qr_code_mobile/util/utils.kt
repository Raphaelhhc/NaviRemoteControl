package com.example.nc_lab_qr_code_mobile.util

import android.content.Context
import android.graphics.Bitmap
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.random.Random

fun loadSessionId(context: Context): String? {
    val sharedPrefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    return sharedPrefs.getString(Constants.PREF_SESSION_ID, null)
}

fun saveSessionId(
    context: Context,
    sessionId: String
) {
    val sharedPrefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    sharedPrefs.edit().putString(Constants.PREF_SESSION_ID, sessionId).apply()
}

fun deleteSessionId(context: Context) {
    val sharedPrefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    sharedPrefs.edit().remove(Constants.PREF_SESSION_ID).apply()
}

fun randomName(): String {
    val fruits = arrayOf("🍎", "🍊", "🍌", "🍓")
    val fruit = fruits[Random.nextInt(0, fruits.count())]
    val randomNumber = Random.nextInt(100, 1000)
    return "$fruit$randomNumber"
}

fun createBarCode(
    content: String,
    width: Int = 300,
    height: Int = 300,
): Bitmap = BarcodeEncoder().encodeBitmap(
    content,
    BarcodeFormat.QR_CODE,
    width,
    height
)

fun navigateTo(
    navController: NavController,
    route: String
) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}