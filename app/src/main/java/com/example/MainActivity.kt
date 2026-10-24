package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AmarShopBottomNav
import com.example.ui.components.AmarShopTopBar
import com.example.ui.model.NavTab
import com.example.ui.model.Screen
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ContactUsScreen
import com.example.ui.screens.FAQScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.PolicyScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.AmarShopTheme
import com.example.ui.viewmodel.AmarShopViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmarShopTheme {
                AmarShopApp()
            }
        }
    }
}

@Composable
fun AmarShopApp(viewModel: AmarShopViewModel = viewModel()) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isAdminMode by viewModel.isAdminMode.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeNavTab by viewModel.activeNavTab.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistProductIds.collectAsStateWithLifecycle()

    val totalCartCount = cartItems.sumOf { it.quantity }

    // System Back Handler
    BackHandler(enabled = currentScreen !is Screen.Home) {
        val handled = viewModel.navigateBack()
        if (!handled && currentScreen != Screen.Home) {
            viewModel.navigate(Screen.Home)
        }
    }

    val isTopLevelCustomerScreen = currentScreen is Screen.Home ||
            currentScreen is Screen.Catalog ||
            currentScreen is Screen.OrderTracking ||
            currentScreen is Screen.Wishlist ||
            currentScreen is Screen.Account

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            if (currentScreen !is Screen.ProductDetail &&
                currentScreen !is Screen.AboutUs &&
                currentScreen !is Screen.ContactUs &&
                currentScreen !is Screen.FAQ &&
                currentScreen !is Screen.Policy
            ) {
                AmarShopTopBar(
                    language = language,
                    isAdminMode = isAdminMode,
                    cartItemCount = totalCartCount,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onToggleAdminMode = { viewModel.toggleAdminMode() },
                    onCartClick = { viewModel.navigate(Screen.Cart) }
                )
            }
        },
        bottomBar = {
            if (!isAdminMode && isTopLevelCustomerScreen) {
                AmarShopBottomNav(
                    activeTab = activeNavTab,
                    language = language,
                    wishlistCount = wishlistIds.size,
                    onTabSelected = { tab ->
                        when (tab) {
                            NavTab.HOME -> viewModel.navigate(Screen.Home)
                            NavTab.CATALOG -> viewModel.navigate(Screen.Catalog())
                            NavTab.ORDERS -> viewModel.navigate(Screen.OrderTracking())
                            NavTab.WISHLIST -> viewModel.navigate(Screen.Wishlist)
                            NavTab.ACCOUNT -> viewModel.navigate(Screen.Account)
                        }
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Catalog -> CatalogScreen(viewModel = viewModel)
                is Screen.ProductDetail -> ProductDetailScreen(productId = screen.productId, viewModel = viewModel)
                is Screen.Cart -> CartScreen(viewModel = viewModel)
                is Screen.Checkout -> CheckoutScreen(viewModel = viewModel)
                is Screen.OrderTracking -> OrderTrackingScreen(initialOrderId = screen.initialOrderId, viewModel = viewModel)
                is Screen.Wishlist -> WishlistScreen(viewModel = viewModel)
                is Screen.Account -> ProfileScreen(viewModel = viewModel)
                is Screen.AboutUs -> AboutUsScreen(viewModel = viewModel)
                is Screen.ContactUs -> ContactUsScreen(viewModel = viewModel)
                is Screen.FAQ -> FAQScreen(viewModel = viewModel)
                is Screen.Policy -> PolicyScreen(type = screen.type, viewModel = viewModel)
                is Screen.AdminDashboard -> AdminDashboardScreen(viewModel = viewModel)
            }
        }
    }
}
