package com.example.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.ConstructionYellow

@Composable
fun VehicleImage(
    assetPath: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val imageBitmap = remember(assetPath) {
        try {
            val cleanPath = assetPath.removePrefix("vehicles/").removePrefix("/")
            context.assets.open("vehicles/$cleanPath").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: Exception) {
            null
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = modifier.background(Navy700),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Construction,
                contentDescription = contentDescription,
                tint = ConstructionYellow
            )
        }
    }
}
