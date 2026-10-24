package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Blender
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.ProductEntity
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.ProductCard
import com.example.ui.components.PromoBanner
import com.example.ui.model.Language
import com.example.ui.model.Screen
import com.example.ui.theme.AmarGoldAccent
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarTealSecondary
import com.example.ui.viewmodel.AmarShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AmarShopViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistProductIds.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    val isBn = language == Language.BN
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val featuredProducts = remember(allProducts) { allProducts.filter { it.isFeatured } }
    val bestsellers = remember(allProducts) { allProducts.filter { it.isBestseller } }
    val newArrivals = remember(allProducts) { allProducts.filter { it.isNewArrival } }

    val categories = listOf(
        CategoryItem("Electronics", if (isBn) "ইলেকট্রনিক্স" else "Electronics", Icons.Default.PhoneIphone, Color(0xFF37474F)),
        CategoryItem("Men's Fashion", if (isBn) "পুরুষের পোশাক" else "Men's Fashion", Icons.Default.Checkroom, Color(0xFFBF360C)),
        CategoryItem("Women's Fashion", if (isBn) "নারীর ফ্যাশন" else "Women's Fashion", Icons.Default.Diamond, Color(0xFF880E4F)),
        CategoryItem("Groceries", if (isBn) "মুদি ও খাদ্য" else "Groceries", Icons.Default.Restaurant, Color(0xFFF57F17)),
        CategoryItem("Home & Living", if (isBn) "গৃহস্থালি" else "Home & Living", Icons.Default.Blender, Color(0xFF0277BD)),
        CategoryItem("Health & Beauty", if (isBn) "সৌন্দর্য ও স্বাস্থ্য" else "Beauty", Icons.Default.Spa, Color(0xFF2E7D32))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Search & Filter Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = if (isBn) "পণ্য, ব্র্যান্ড বা ক্যাটাগরি খুঁজুন..." else "Search products, brands, or categories...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarOrangePrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.navigate(Screen.Catalog()) }
                    .testTag("home_search_bar")
            )

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = AmarOrangePrimary.copy(alpha = 0.12f),
                modifier = Modifier.size(48.dp)
            ) {
                IconButton(
                    onClick = { showFilterSheet = true },
                    modifier = Modifier.testTag("home_filter_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = AmarOrangePrimary
                    )
                }
            }
        }

        // Hero Promotional Banner
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            PromoBanner(
                language = language,
                onShopNowClick = { viewModel.navigate(Screen.Catalog()) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Grid Horizontal Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBn) "ক্যাটাগরি সমূহ" else "Categories",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            TextButton(onClick = { viewModel.navigate(Screen.Catalog()) }) {
                Text(
                    text = if (isBn) "সব দেখুন" else "See All",
                    color = AmarOrangePrimary,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(categories) { cat ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { viewModel.setCategoryFilter(cat.key) }
                        .testTag("cat_chip_${cat.key}")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = cat.color.copy(alpha = 0.12f),
                        shadowElevation = 1.dp,
                        modifier = Modifier.size(60.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = cat.icon,
                                contentDescription = cat.title,
                                tint = cat.color,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = cat.title,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Flash Deals Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = AmarOrangePrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isBn) "ফ্ল্যাশ ডিলস (Flash Deals)" else "Flash Deals",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            TextButton(onClick = { viewModel.navigate(Screen.Catalog()) }) {
                Text(
                    text = if (isBn) "সব দেখুন" else "View All",
                    color = AmarOrangePrimary,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(featuredProducts) { product ->
                ProductCard(
                    product = product,
                    language = language,
                    isWishlisted = wishlistIds.contains(product.id),
                    onProductClick = { viewModel.navigate(Screen.ProductDetail(product.id)) },
                    onWishlistToggle = { viewModel.toggleWishlist(product.id) },
                    onQuickAddToCart = { viewModel.addToCart(product) },
                    modifier = Modifier.width(180.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Best Sellers Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBn) "সর্বোচ্চ বিক্রিত পণ্য (Best Sellers)" else "Best Sellers",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            TextButton(onClick = { viewModel.navigate(Screen.Catalog()) }) {
                Text(
                    text = if (isBn) "সব দেখুন" else "View All",
                    color = AmarOrangePrimary,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bestsellers) { product ->
                ProductCard(
                    product = product,
                    language = language,
                    isWishlisted = wishlistIds.contains(product.id),
                    onProductClick = { viewModel.navigate(Screen.ProductDetail(product.id)) },
                    onWishlistToggle = { viewModel.toggleWishlist(product.id) },
                    onQuickAddToCart = { viewModel.addToCart(product) },
                    modifier = Modifier.width(180.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // New Arrivals Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBn) "নতুন আগমন (New Arrivals)" else "New Arrivals",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            TextButton(onClick = { viewModel.navigate(Screen.Catalog()) }) {
                Text(
                    text = if (isBn) "সব দেখুন" else "View All",
                    color = AmarOrangePrimary,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(newArrivals) { product ->
                ProductCard(
                    product = product,
                    language = language,
                    isWishlisted = wishlistIds.contains(product.id),
                    onProductClick = { viewModel.navigate(Screen.ProductDetail(product.id)) },
                    onWishlistToggle = { viewModel.toggleWishlist(product.id) },
                    onQuickAddToCart = { viewModel.addToCart(product) },
                    modifier = Modifier.width(180.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Bangladesh Trust Badges
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBn) "কেন AmarShop থেকে কিনবেন?" else "Why Shop on AmarShop?",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AmarTealSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                TrustItem(
                    icon = Icons.Default.VerifiedUser,
                    title = if (isBn) "১০০% আসল পণ্যের নিশ্চয়তা" else "100% Authentic Products",
                    desc = if (isBn) "সরাসরি অনুমোদিত ব্র্যান্ড ও প্রস্তুতকারক থেকে" else "Direct from certified brands & artisans"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TrustItem(
                    icon = Icons.Default.LocalShipping,
                    title = if (isBn) "দ্রুততম ডেলিভারি ও ক্যাশ অন ডেলিভারি" else "Fast Delivery & Cash on Delivery",
                    desc = if (isBn) "ঢাকাতে ৳৬০, সমগ্র বাংলাদেশে ৳১২০" else "Inside Dhaka ৳60, Outside Dhaka ৳120"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TrustItem(
                    icon = Icons.Default.CheckCircle,
                    title = if (isBn) "সহজ ৭ দিনের রিটার্ন পলিসি" else "Easy 7-Day Free Returns",
                    desc = if (isBn) "কোনো ঝামেলা ছাড়াই দ্রুত রিফান্ড" else "Hassle-free replacement & refund guarantee"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TrustItem(
                    icon = Icons.Default.HeadsetMic,
                    title = if (isBn) "২৪/৭ কাস্টমার সাপোর্ট" else "24/7 Dedicated Support",
                    desc = if (isBn) "যে কোনো সমস্যায় আমরা আছি আপনার সাথে" else "Always ready to assist you in Bangla & English"
                )
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            filterState = filterState,
            language = language,
            sheetState = sheetState,
            onDismiss = { showFilterSheet = false },
            onApply = { newFilter ->
                viewModel.updateFilter(newFilter)
                viewModel.navigate(Screen.Catalog())
            },
            onReset = { viewModel.resetFilters() }
        )
    }
}

@Composable
private fun TrustItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AmarOrangePrimary,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private data class CategoryItem(
    val key: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
)
