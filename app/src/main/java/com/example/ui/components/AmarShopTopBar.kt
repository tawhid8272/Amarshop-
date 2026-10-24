package com.example.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.Language
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarTealSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmarShopTopBar(
    language: Language,
    isAdminMode: Boolean,
    cartItemCount: Int,
    onToggleLanguage: () -> Unit,
    onToggleAdminMode: () -> Unit,
    onCartClick: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isAdminMode) Icons.Default.AdminPanelSettings else Icons.Default.ShoppingBag,
                        contentDescription = "AmarShop Logo",
                        tint = if (isAdminMode) AmarTealSecondary else AmarOrangePrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAdminMode) "AmarShop Admin" else "AmarShop",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            navigationIcon = {
                // Language Switcher Chip
                FilterChip(
                    selected = language == Language.BN,
                    onClick = onToggleLanguage,
                    label = {
                        Text(
                            text = if (language == Language.BN) "বাংলা" else "EN",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .testTag("lang_toggle"),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmarTealSecondary.copy(alpha = 0.2f),
                        selectedLabelColor = AmarTealSecondary
                    )
                )
            },
            actions = {
                // Admin Toggle Mode Button
                IconButton(
                    onClick = onToggleAdminMode,
                    modifier = Modifier.testTag("admin_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (isAdminMode) Icons.Default.Storefront else Icons.Default.AdminPanelSettings,
                        contentDescription = if (isAdminMode) "Switch to Store" else "Admin Console",
                        tint = if (isAdminMode) AmarOrangePrimary else AmarTealSecondary
                    )
                }

                // Cart with badge
                if (!isAdminMode) {
                    IconButton(
                        onClick = onCartClick,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .testTag("cart_top_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(
                                        containerColor = AmarOrangePrimary,
                                        contentColor = Color.White
                                    ) {
                                        Text(cartItemCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart"
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}
