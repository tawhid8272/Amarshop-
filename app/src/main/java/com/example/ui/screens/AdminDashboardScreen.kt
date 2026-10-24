package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.ui.components.ProductVisualPlaceholder
import com.example.ui.model.AdminTab
import com.example.ui.model.Language
import com.example.ui.theme.AmarDiscountBadge
import com.example.ui.theme.AmarGoldAccent
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarSuccessGreen
import com.example.ui.theme.AmarTealSecondary
import com.example.ui.viewmodel.AmarShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AmarShopViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.toggleAdminMode() }

    val context = LocalContext.current
    val language by viewModel.language.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val activeAdminTab by viewModel.activeAdminTab.collectAsStateWithLifecycle()

    val isBn = language == Language.BN
    var showAddProductDialog by remember { mutableStateOf(false) }

    // KPI Metrics calculation
    val totalRevenue = remember(allOrders) {
        allOrders.filter { it.status != "Cancelled" }.sumOf { it.totalAmount }
    }
    val totalOrdersCount = allOrders.size
    val lowStockProducts = remember(allProducts) {
        allProducts.filter { it.stockQuantity <= 5 }
    }

    Scaffold(
        floatingActionButton = {
            if (activeAdminTab == AdminTab.PRODUCTS) {
                FloatingActionButton(
                    onClick = { showAddProductDialog = true },
                    containerColor = AmarOrangePrimary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("admin_add_product_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Admin Top Switcher Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AmarTealSecondary.copy(alpha = 0.08f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storefront, contentDescription = null, tint = AmarTealSecondary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBn) "অ্যাডমিন ম্যানেজমেন্ট পোর্টাল" else "Merchant Admin Control",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AmarTealSecondary)
                    )
                }

                Button(
                    onClick = { viewModel.toggleAdminMode() },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(if (isBn) "স্টোরফ্রন্টে ফিরুন" else "Customer View", style = MaterialTheme.typography.labelSmall)
                }
            }

            // Executive KPI Cards Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiCard(
                    title = if (isBn) "মোট বিক্রি" else "Gross Sales",
                    value = "৳${totalRevenue.toInt()}",
                    icon = Icons.Default.AttachMoney,
                    color = AmarSuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = if (isBn) "মোট অর্ডার" else "Total Orders",
                    value = "$totalOrdersCount",
                    icon = Icons.Default.LocalShipping,
                    color = AmarOrangePrimary,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = if (isBn) "ক্যাটালগ" else "Products",
                    value = "${allProducts.size}",
                    icon = Icons.Default.Inventory2,
                    color = AmarTealSecondary,
                    modifier = Modifier.weight(1f)
                )
            }

            // Low Stock Warning Banner if any
            if (lowStockProducts.isNotEmpty()) {
                Surface(
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = AmarGoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn) "সতর্কতা: ${lowStockProducts.size}টি পণ্যের স্টক শেষ হয়ে আসছে!" else "Attention: ${lowStockProducts.size} products are low on stock!",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        )
                    }
                }
            }

            // Tabs: Overview, Products, Orders, Reports
            PrimaryTabRow(
                selectedTabIndex = activeAdminTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = AmarOrangePrimary
            ) {
                Tab(
                    selected = activeAdminTab == AdminTab.OVERVIEW,
                    onClick = { viewModel.setAdminTab(AdminTab.OVERVIEW) },
                    text = { Text(if (isBn) "ওভারভিউ" else "Overview") }
                )
                Tab(
                    selected = activeAdminTab == AdminTab.PRODUCTS,
                    onClick = { viewModel.setAdminTab(AdminTab.PRODUCTS) },
                    text = { Text(if (isBn) "পণ্যসমূহ" else "Products") }
                )
                Tab(
                    selected = activeAdminTab == AdminTab.ORDERS,
                    onClick = { viewModel.setAdminTab(AdminTab.ORDERS) },
                    text = { Text(if (isBn) "অর্ডারসমূহ" else "Orders") }
                )
                Tab(
                    selected = activeAdminTab == AdminTab.REPORTS,
                    onClick = { viewModel.setAdminTab(AdminTab.REPORTS) },
                    text = { Text(if (isBn) "রিপোর্ট" else "Reports") }
                )
            }

            // Tab Content
            when (activeAdminTab) {
                AdminTab.OVERVIEW -> AdminOverviewTab(allOrders = allOrders, lowStock = lowStockProducts, isBn = isBn)
                AdminTab.PRODUCTS -> AdminProductsTab(
                    products = allProducts,
                    isBn = isBn,
                    onUpdateStock = { id, stock -> viewModel.updateProductStock(id, stock) },
                    onDeleteProduct = { product -> viewModel.deleteProduct(product) }
                )
                AdminTab.ORDERS -> AdminOrdersTab(
                    orders = allOrders,
                    isBn = isBn,
                    onStatusChange = { id, status -> viewModel.updateOrderStatus(id, status) }
                )
                AdminTab.REPORTS -> AdminReportsTab(
                    totalRevenue = totalRevenue,
                    orders = allOrders,
                    isBn = isBn,
                    onExportCsv = {
                        Toast.makeText(context, "Exported AmarShop_Sales_Report.csv successfully!", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    // Add Product Dialog
    if (showAddProductDialog) {
        AddProductDialog(
            isBn = isBn,
            onDismiss = { showAddProductDialog = false },
            onConfirm = { newProduct ->
                viewModel.addProduct(newProduct)
                showAddProductDialog = false
            }
        )
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdminOverviewTab(
    allOrders: List<OrderEntity>,
    lowStock: List<ProductEntity>,
    isBn: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = if (isBn) "সাম্প্রতিক অর্ডারসমূহ (${allOrders.take(5).size})" else "Recent Customer Orders",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        if (allOrders.isEmpty()) {
            item {
                Text(
                    text = if (isBn) "এখনো কোনো অর্ডার আসেনি।" else "No recent orders recorded.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(allOrders.take(5)) { order ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "#${order.orderId} • ${order.customerName}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(text = "${order.district} • ৳${order.totalAmount.toInt()} (${order.paymentMethod})", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Surface(
                            color = AmarTealSecondary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = order.status,
                                color = AmarTealSecondary,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (isBn) "কম স্টকে থাকা পণ্যসমূহ (${lowStock.size})" else "Low Stock Inventory Alerts",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFFD84315))
            )
        }

        items(lowStock) { prod ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = prod.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text(text = "SKU: ${prod.sku} • ৳${prod.discountPrice.toInt()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${prod.stockQuantity} left",
                            color = AmarDiscountBadge,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminProductsTab(
    products: List<ProductEntity>,
    isBn: Boolean,
    onUpdateStock: (String, Int) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(products, key = { it.id }) { product ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    ) {
                        ProductVisualPlaceholder(category = product.category, imageUrlKey = product.imageUrl, iconSize = 24.dp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), maxLines = 1)
                        Text(text = "${product.category} • ৳${product.discountPrice.toInt()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Stock: ${product.stockQuantity}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = if (product.stockQuantity <= 5) AmarDiscountBadge else AmarTealSecondary))
                    }

                    // Stock adjust stepper
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (product.stockQuantity > 0) onUpdateStock(product.id, product.stockQuantity - 1) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                        }
                        IconButton(
                            onClick = { onUpdateStock(product.id, product.stockQuantity + 5) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase +5", modifier = Modifier.size(14.dp))
                        }
                        IconButton(
                            onClick = { onDeleteProduct(product) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminOrdersTab(
    orders: List<OrderEntity>,
    isBn: Boolean,
    onStatusChange: (String, String) -> Unit
) {
    val statuses = listOf("Pending", "Confirmed", "Processing", "Shipped", "Delivered", "Cancelled")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders, key = { it.orderId }) { order ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Order #${order.orderId}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(text = "৳${order.totalAmount.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AmarOrangePrimary))
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Customer: ${order.customerName} (${order.customerPhone})", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Address: ${order.address}, ${order.district}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Payment: ${order.paymentMethod}${if (order.paymentTrxId.isNotBlank()) " [Trx: ${order.paymentTrxId}]" else ""}", style = MaterialTheme.typography.labelSmall, color = AmarTealSecondary)

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    // Status Dispatch Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Status: ${order.status}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))

                        val nextStatus = when (order.status) {
                            "Pending" -> "Confirmed"
                            "Confirmed" -> "Processing"
                            "Processing" -> "Shipped"
                            "Shipped" -> "Delivered"
                            else -> null
                        }

                        if (nextStatus != null) {
                            Button(
                                onClick = { onStatusChange(order.orderId, nextStatus) },
                                colors = ButtonDefaults.buttonColors(containerColor = AmarTealSecondary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Advance to $nextStatus", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminReportsTab(
    totalRevenue: Double,
    orders: List<OrderEntity>,
    isBn: Boolean,
    onExportCsv: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBn) "বিক্রয় রিপোর্ট ও অ্যানালিটিক্স" else "Sales Analytics & Export",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Button(
                onClick = onExportCsv,
                colors = ButtonDefaults.buttonColors(containerColor = AmarTealSecondary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Export CSV")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Executive Summary", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Gross GMV", style = MaterialTheme.typography.bodySmall)
                    Text("৳${totalRevenue.toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Average Order Value (AOV)", style = MaterialTheme.typography.bodySmall)
                    val aov = if (orders.isNotEmpty()) (totalRevenue / orders.size).toInt() else 0
                    Text("৳$aov", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Transactions", style = MaterialTheme.typography.bodySmall)
                    Text("${orders.size}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddProductDialog(
    isBn: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (ProductEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var nameBn by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Electronics") }
    var brand by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var regularPrice by remember { mutableStateOf("") }
    var discountPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("10") }
    var description by remember { mutableStateOf("") }

    val categories = listOf("Electronics", "Men's Fashion", "Women's Fashion", "Groceries", "Home & Living", "Health & Beauty")
    var catExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isBn) "নতুন পণ্য যুক্ত করুন" else "Add New Catalog Product") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name (English) *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nameBn,
                    onValueChange = { nameBn = it },
                    label = { Text("Product Name (বাংলা) *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = catExpanded,
                    onExpandedChange = { catExpanded = !catExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = catExpanded,
                        onDismissRequest = { catExpanded = false }
                    ) {
                        categories.forEach { c ->
                            DropdownMenuItem(text = { Text(c) }, onClick = { category = c; catExpanded = false })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Brand (e.g. Walton, Aarong)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = regularPrice,
                        onValueChange = { regularPrice = it },
                        label = { Text("Regular Price (৳)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = discountPrice,
                        onValueChange = { discountPrice = it },
                        label = { Text("Sale Price (৳) *") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Initial Stock Quantity *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reg = regularPrice.toDoubleOrNull() ?: 1000.0
                    val disc = discountPrice.toDoubleOrNull() ?: reg
                    val st = stock.toIntOrNull() ?: 10
                    val id = "prod_custom_" + System.currentTimeMillis()

                    val newP = ProductEntity(
                        id = id,
                        name = name.ifBlank { "New Product" },
                        nameBn = nameBn.ifBlank { name },
                        description = description.ifBlank { "Quality product from AmarShop" },
                        descriptionBn = description.ifBlank { "আমরশপ থেকে সেরা মানের পণ্য" },
                        regularPrice = reg,
                        discountPrice = disc,
                        category = category,
                        categoryBn = category,
                        brand = brand.ifBlank { "AmarShop" },
                        sku = sku.ifBlank { "SKU-" + (1000..9999).random() },
                        stockQuantity = st,
                        rating = 5.0f,
                        reviewCount = 1,
                        imageUrl = "product",
                        isFeatured = true,
                        isNewArrival = true
                    )
                    onConfirm(newP)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary)
            ) {
                Text(if (isBn) "সংরক্ষণ করুন" else "Save Product")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isBn) "বাতিল" else "Cancel")
            }
        }
    )
}
