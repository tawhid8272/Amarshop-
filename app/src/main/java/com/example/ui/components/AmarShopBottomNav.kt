package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.model.Language
import com.example.ui.model.NavTab
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarOrangeOnContainer

@Composable
fun AmarShopBottomNav(
    activeTab: NavTab,
    language: Language,
    wishlistCount: Int,
    onTabSelected: (NavTab) -> Unit
) {
    NavigationBar(
        windowInsets = WindowInsets.navigationBars,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        val isBn = language == Language.BN

        // Home
        NavigationBarItem(
            selected = activeTab == NavTab.HOME,
            onClick = { onTabSelected(NavTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (activeTab == NavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(if (isBn) "হোম" else "Home") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AmarOrangePrimary.copy(alpha = 0.15f),
                selectedIconColor = AmarOrangePrimary,
                selectedTextColor = AmarOrangePrimary
            ),
            modifier = Modifier.testTag("nav_home")
        )

        // Catalog
        NavigationBarItem(
            selected = activeTab == NavTab.CATALOG,
            onClick = { onTabSelected(NavTab.CATALOG) },
            icon = {
                Icon(
                    imageVector = if (activeTab == NavTab.CATALOG) Icons.Filled.Category else Icons.Outlined.Category,
                    contentDescription = "Catalog",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(if (isBn) "পণ্যসমূহ" else "Catalog") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AmarOrangePrimary.copy(alpha = 0.15f),
                selectedIconColor = AmarOrangePrimary,
                selectedTextColor = AmarOrangePrimary
            ),
            modifier = Modifier.testTag("nav_catalog")
        )

        // Orders
        NavigationBarItem(
            selected = activeTab == NavTab.ORDERS,
            onClick = { onTabSelected(NavTab.ORDERS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == NavTab.ORDERS) Icons.Filled.LocalShipping else Icons.Outlined.LocalShipping,
                    contentDescription = "Orders",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(if (isBn) "অর্ডার" else "Orders") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AmarOrangePrimary.copy(alpha = 0.15f),
                selectedIconColor = AmarOrangePrimary,
                selectedTextColor = AmarOrangePrimary
            ),
            modifier = Modifier.testTag("nav_orders")
        )

        // Wishlist
        NavigationBarItem(
            selected = activeTab == NavTab.WISHLIST,
            onClick = { onTabSelected(NavTab.WISHLIST) },
            icon = {
                BadgedBox(
                    badge = {
                        if (wishlistCount > 0) {
                            Badge(containerColor = AmarOrangePrimary) {
                                Text(wishlistCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (activeTab == NavTab.WISHLIST) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { Text(if (isBn) "পছন্দ" else "Wishlist") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AmarOrangePrimary.copy(alpha = 0.15f),
                selectedIconColor = AmarOrangePrimary,
                selectedTextColor = AmarOrangePrimary
            ),
            modifier = Modifier.testTag("nav_wishlist")
        )

        // Account
        NavigationBarItem(
            selected = activeTab == NavTab.ACCOUNT,
            onClick = { onTabSelected(NavTab.ACCOUNT) },
            icon = {
                Icon(
                    imageVector = if (activeTab == NavTab.ACCOUNT) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = "Account",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(if (isBn) "প্রোফাইল" else "Account") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AmarOrangePrimary.copy(alpha = 0.15f),
                selectedIconColor = AmarOrangePrimary,
                selectedTextColor = AmarOrangePrimary
            ),
            modifier = Modifier.testTag("nav_account")
        )
    }
}
