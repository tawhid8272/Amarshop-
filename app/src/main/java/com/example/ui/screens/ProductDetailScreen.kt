package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.ProductEntity
import com.example.data.local.ReviewEntity
import com.example.ui.components.ProductVisualPlaceholder
import com.example.ui.model.Language
import com.example.ui.model.Screen
import com.example.ui.theme.AmarDiscountBadge
import com.example.ui.theme.AmarGoldAccent
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarTealSecondary
import com.example.ui.viewmodel.AmarShopViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: AmarShopViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val language by viewModel.language.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistProductIds.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    var product by remember { mutableStateOf<ProductEntity?>(null) }
    var selectedSize by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var showReviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        val p = viewModel.repository.getProductById(productId)
        product = p
        if (p != null) {
            val sizes = p.getSizesList()
            if (sizes.isNotEmpty()) selectedSize = sizes.first()
            val colors = p.getColorsList()
            if (colors.isNotEmpty()) selectedColor = colors.first()
        }
    }

    val reviews by remember(productId) {
        viewModel.getProductReviews(productId)
    }.collectAsStateWithLifecycle(initialValue = emptyList())

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AmarOrangePrimary)
        }
        return
    }

    val currentProduct = product!!
    val isWishlisted = wishlistIds.contains(currentProduct.id)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            // Top Bar with Back and Wishlist
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                ProductVisualPlaceholder(
                    category = currentProduct.category,
                    imageUrlKey = currentProduct.imageUrl,
                    iconSize = 80.dp
                )

                // Navigation Controls Overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.navigateBack() },
                            modifier = Modifier.testTag("detail_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleWishlist(currentProduct.id) },
                            modifier = Modifier.testTag("detail_wishlist_btn")
                        ) {
                            Icon(
                                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (isWishlisted) AmarOrangePrimary else Color.White
                            )
                        }
                    }
                }

                // Discount Badge on Image
                if (currentProduct.discountPercentage > 0) {
                    Surface(
                        color = AmarDiscountBadge,
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "-${currentProduct.discountPercentage}% OFF",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Main Product Info
            Column(modifier = Modifier.padding(16.dp)) {
                // Category & Brand Tag
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = AmarTealSecondary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isBn) currentProduct.categoryBn else currentProduct.category,
                            color = AmarTealSecondary,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Brand: ${currentProduct.brand}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SKU: ${currentProduct.sku}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = if (isBn) currentProduct.nameBn else currentProduct.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Ratings & Reviews Counter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < currentProduct.rating.toInt()) AmarGoldAccent else Color.LightGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${currentProduct.rating} (${currentProduct.reviewCount} ${if (isBn) "রিভিউ" else "reviews"})",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Pricing Row
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "৳${currentProduct.discountPrice.toInt()}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AmarOrangePrimary
                        )
                    )
                    if (currentProduct.regularPrice > currentProduct.discountPrice) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "৳${currentProduct.regularPrice.toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Stock Pill
                    Surface(
                        color = if (currentProduct.isOutOfStock) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = when {
                                currentProduct.isOutOfStock -> if (isBn) "স্টক নেই" else "Out of Stock"
                                currentProduct.isLowStock -> if (isBn) "মাত্র ${currentProduct.stockQuantity}টি বাকি" else "${currentProduct.stockQuantity} Left"
                                else -> if (isBn) "স্টকে আছে" else "In Stock"
                            },
                            color = if (currentProduct.isOutOfStock) Color(0xFFC62828) else Color(0xFF2E7D32),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // Variant Selection: Sizes
                val sizes = currentProduct.getSizesList()
                if (sizes.isNotEmpty()) {
                    Text(
                        text = if (isBn) "সাইজ / ভ্যারিয়েন্ট নির্বাচন করুন" else "Select Variant / Size",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sizes.forEach { sizeOption ->
                            FilterChip(
                                selected = selectedSize == sizeOption,
                                onClick = { selectedSize = sizeOption },
                                label = { Text(sizeOption) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AmarOrangePrimary.copy(alpha = 0.15f),
                                    selectedLabelColor = AmarOrangePrimary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Variant Selection: Colors
                val colors = currentProduct.getColorsList()
                if (colors.isNotEmpty()) {
                    Text(
                        text = if (isBn) "রং নির্বাচন করুন" else "Select Color",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colors.forEach { colorOption ->
                            FilterChip(
                                selected = selectedColor == colorOption,
                                onClick = { selectedColor = colorOption },
                                label = { Text(colorOption) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AmarTealSecondary.copy(alpha = 0.15f),
                                    selectedLabelColor = AmarTealSecondary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Quantity Stepper
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBn) "পরিমাণ" else "Quantity",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                        }

                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )

                        IconButton(
                            onClick = { if (quantity < currentProduct.stockQuantity) quantity++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = if (isBn) "পণ্যের বিবরণ" else "Product Description",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isBn) currentProduct.descriptionBn else currentProduct.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Delivery & Warranty Guarantee Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = AmarTealSecondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "স্ট্যান্ডার্ড ডেলিভারি: ঢাকা ৳৬০ (২৪-৪৮ ঘণ্টা), বাইরে ৳১২০" else "Delivery: Inside Dhaka ৳60 (24-48h), Outside ৳120",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = AmarTealSecondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "ক্যাশ অন ডেলিভারি এবং ৭ দিনের রিপ্লেসমেন্ট গ্যারান্টি" else "Cash on Delivery Available & 7-Day Free Replacement",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Customer Reviews Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBn) "গ্রাহক পর্যালোচনা (${reviews.size})" else "Customer Reviews (${reviews.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    TextButton(onClick = { showReviewDialog = true }) {
                        Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBn) "রিভিউ দিন" else "Write Review",
                            color = AmarOrangePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (reviews.isEmpty()) {
                    Text(
                        text = if (isBn) "এখনো কোনো রিভিউ দেওয়া হয়নি। প্রথম রিভিউটি আপনিই দিন!" else "No customer reviews yet. Be the first to share your experience!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        reviews.forEach { rev ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = rev.authorName,
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Row {
                                            repeat(rev.rating) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = AmarGoldAccent,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = rev.comment,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sticky Bottom Action Bar (Add to Cart + Buy Now)
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.addToCart(currentProduct, selectedSize, selectedColor, quantity)
                    },
                    enabled = !currentProduct.isOutOfStock,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("detail_add_cart_btn")
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, tint = AmarOrangePrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBn) "কার্ট-এ যোগ করুন" else "Add to Cart",
                        color = AmarOrangePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        viewModel.addToCart(currentProduct, selectedSize, selectedColor, quantity)
                        viewModel.navigate(Screen.Cart)
                    },
                    enabled = !currentProduct.isOutOfStock,
                    colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("detail_buy_now_btn")
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isBn) "এখনই কিনুন" else "Buy Now",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Write Review Dialog
    if (showReviewDialog) {
        var reviewerName by remember { mutableStateOf("") }
        var reviewText by remember { mutableStateOf("") }
        var reviewRating by remember { mutableIntStateOf(5) }

        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = {
                Text(if (isBn) "রিভিউ লিখুন" else "Write a Customer Review")
            },
            text = {
                Column {
                    Text(
                        text = if (isBn) "রেটিং নির্বাচন করুন" else "Select Rating",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        (1..5).forEach { star ->
                            IconButton(
                                onClick = { reviewRating = star },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star Stars",
                                    tint = if (star <= reviewRating) AmarGoldAccent else Color.LightGray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = reviewerName,
                        onValueChange = { reviewerName = it },
                        label = { Text(if (isBn) "আপনার নাম ও শহর" else "Your Name & City") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        label = { Text(if (isBn) "আপনার অভিজ্ঞতা লিখুন..." else "Write your feedback...") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reviewerName.isNotBlank() && reviewText.isNotBlank()) {
                            viewModel.submitReview(
                                productId = currentProduct.id,
                                author = reviewerName.trim(),
                                rating = reviewRating,
                                comment = reviewText.trim()
                            )
                            showReviewDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary)
                ) {
                    Text(if (isBn) "জমা দিন" else "Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            }
        )
    }
}
