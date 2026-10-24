package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Blender
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProductVisualPlaceholder(
    category: String,
    imageUrlKey: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp
) {
    val (gradientColors, icon) = when {
        imageUrlKey.contains("phone") -> Pair(
            listOf(Color(0xFF37474F), Color(0xFF263238)),
            Icons.Default.PhoneIphone
        )
        imageUrlKey.contains("watch") -> Pair(
            listOf(Color(0xFF00695C), Color(0xFF004D40)),
            Icons.Default.Watch
        )
        imageUrlKey.contains("earbuds") -> Pair(
            listOf(Color(0xFF283593), Color(0xFF1A237E)),
            Icons.Default.Headphones
        )
        imageUrlKey.contains("panjabi") -> Pair(
            listOf(Color(0xFFBF360C), Color(0xFF870000)),
            Icons.Default.Checkroom
        )
        imageUrlKey.contains("saree") -> Pair(
            listOf(Color(0xFF880E4F), Color(0xFF4A148C)),
            Icons.Default.Diamond
        )
        imageUrlKey.contains("oil") || imageUrlKey.contains("tea") || imageUrlKey.contains("honey") -> Pair(
            listOf(Color(0xFFF57F17), Color(0xFFE65100)),
            Icons.Default.Restaurant
        )
        imageUrlKey.contains("ac") || imageUrlKey.contains("cooker") -> Pair(
            listOf(Color(0xFF0277BD), Color(0xFF01579B)),
            Icons.Default.Blender
        )
        imageUrlKey.contains("facewash") -> Pair(
            listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)),
            Icons.Default.Spa
        )
        category.contains("Fashion") -> Pair(
            listOf(Color(0xFFE65100), Color(0xFFBF360C)),
            Icons.Default.Checkroom
        )
        category.contains("Electronics") -> Pair(
            listOf(Color(0xFF37474F), Color(0xFF263238)),
            Icons.Default.PhoneIphone
        )
        category.contains("Groceries") -> Pair(
            listOf(Color(0xFFF57F17), Color(0xFFE65100)),
            Icons.Default.Restaurant
        )
        category.contains("Home") -> Pair(
            listOf(Color(0xFF0277BD), Color(0xFF01579B)),
            Icons.Default.Blender
        )
        else -> Pair(
            listOf(Color(0xFFFF5722), Color(0xFFE64A19)),
            Icons.Default.LocalMall
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.radialGradient(gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(iconSize)
        )
    }
}
