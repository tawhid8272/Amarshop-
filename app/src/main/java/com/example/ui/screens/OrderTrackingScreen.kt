package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.OrderEntity
import com.example.ui.model.Language
import com.example.ui.model.Screen
import com.example.ui.theme.AmarDiscountBadge
import com.example.ui.theme.AmarGoldAccent
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarSuccessGreen
import com.example.ui.theme.AmarTealSecondary
import com.example.ui.viewmodel.AmarShopViewModel

@Composable
fun OrderTrackingScreen(
    initialOrderId: String? = null,
    viewModel: AmarShopViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val language by viewModel.language.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (isBn) "আমার অর্ডার ও ট্র্যাকিং" else "My Orders & Tracking",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = if (isBn) "আপনার প্রতিটি অর্ডারের বাস্তব সময়ের অবস্থা দেখুন" else "Track live fulfillment and delivery status",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (allOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isBn) "এখনো কোনো অর্ডার পাওয়া যায়নি" else "No orders placed yet",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isBn) "পণ্য অর্ডার করার পর এখানে স্ট্যাটাস ট্র্যাক করতে পারবেন।" else "Once you place an order, live tracking steps will appear here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.navigate(Screen.Catalog()) },
                        colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isBn) "কেনাকাটা করুন" else "Shop Now")
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allOrders, key = { it.orderId }) { order ->
                    OrderTrackingCard(order = order, language = language)
                }
            }
        }
    }
}

@Composable
private fun OrderTrackingCard(
    order: OrderEntity,
    language: Language
) {
    val isBn = language == Language.BN

    val statusIndex = when (order.status.lowercase()) {
        "pending" -> 0
        "confirmed" -> 1
        "processing" -> 2
        "shipped" -> 3
        "delivered" -> 4
        else -> 0
    }

    val (badgeBg, badgeText) = when (order.status.lowercase()) {
        "pending" -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        "confirmed" -> Pair(Color(0xFFE3F2FD), Color(0xFF1976D2))
        "processing" -> Pair(Color(0xFFE0F2F1), AmarTealSecondary)
        "shipped" -> Pair(Color(0xFFEDE7F6), Color(0xFF673AB7))
        "delivered" -> Pair(Color(0xFFE8F5E9), AmarSuccessGreen)
        else -> Pair(Color(0xFFFFEBEE), AmarDiscountBadge)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.orderId}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Order ID & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.orderId}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Placed on ${java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(order.timestamp)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = badgeBg,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = order.status,
                        color = badgeText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Progress Timeline
            val steps = listOf(
                if (isBn) "অর্ডার গৃহীত" else "Placed",
                if (isBn) "নিশ্চিত" else "Confirmed",
                if (isBn) "প্যাকিং" else "Processing",
                if (isBn) "কুরিয়ারে প্রেরিত" else "Shipped",
                if (isBn) "ডেলিভারি সম্পন্ন" else "Delivered"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, stepName ->
                    val isCompleted = index <= statusIndex
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isCompleted) AmarTealSecondary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(26.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stepName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            fontSize = 9.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Items & Customer Information
            Text(
                text = if (isBn) "অর্ডারের পণ্যসমূহ:" else "Items Summary:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = order.itemsSummary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isBn) "ডেলিভারি ঠিকানা: ${order.address}, ${order.district}" else "Delivery: ${order.address}, ${order.district} (${order.customerPhone})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Payment: ${order.paymentMethod}${if (order.paymentTrxId.isNotBlank()) " (Trx: ${order.paymentTrxId})" else ""}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AmarTealSecondary
                )

                Text(
                    text = "Total: ৳${order.totalAmount.toInt()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = AmarOrangePrimary
                    )
                )
            }
        }
    }
}
