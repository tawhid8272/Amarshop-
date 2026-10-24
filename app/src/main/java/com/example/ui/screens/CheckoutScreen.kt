package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.AmarShopRepository
import com.example.ui.model.Language
import com.example.ui.model.Screen
import com.example.ui.theme.AmarGoldAccent
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarTealSecondary
import com.example.ui.viewmodel.AmarShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: AmarShopViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val language by viewModel.language.collectAsStateWithLifecycle()
    val form by viewModel.checkoutForm.collectAsStateWithLifecycle()
    val cartSummary by viewModel.cartSummary.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    var divisionExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }
    var confirmedOrderId by remember { mutableStateOf<String?>(null) }

    val currentDistricts = remember(form.division) {
        AmarShopRepository.BD_DISTRICTS_BY_DIVISION[form.division] ?: listOf("Dhaka")
    }

    val dynamicDeliveryFee = remember(form.district) {
        AmarShopRepository.calculateDeliveryFee(form.district)
    }

    val finalPayable = remember(cartSummary.subtotal, dynamicDeliveryFee, cartSummary.discount) {
        (cartSummary.subtotal + dynamicDeliveryFee - cartSummary.discount).coerceAtLeast(0.0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = if (isBn) "নিরাপদ চেকআউট (Secure Checkout)" else "Secure Checkout",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = if (isBn) "ডেলিভারি ঠিকানা ও পেমেন্ট পদ্ধতি নিশ্চিত করুন" else "Confirm your delivery details & preferred payment",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Step 1: Customer Contact Information
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = AmarOrangePrimary.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AmarOrangePrimary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "গ্রাহকের তথ্য ও যোগাযোগের নম্বর" else "Contact Information",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = form.customerName,
                    onValueChange = { viewModel.updateCheckoutCustomerName(it) },
                    label = { Text(if (isBn) "আপনার পুরো নাম" else "Full Name *") },
                    isError = form.nameError != null,
                    supportingText = form.nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_name_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = form.customerPhone,
                    onValueChange = { viewModel.updateCheckoutPhone(it) },
                    label = { Text(if (isBn) "মোবাইল নম্বর (যেমন 017XXXXXXXX)" else "Mobile Number (e.g. 017XXXXXXXX) *") },
                    isError = form.phoneError != null,
                    supportingText = form.phoneError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_phone_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 2: Bangladesh Delivery Address
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = AmarTealSecondary.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "2",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AmarTealSecondary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "ডেলিভারি ঠিকানা (বাংলাদেশ)" else "Delivery Address (Bangladesh)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Division Dropdown
                ExposedDropdownMenuBox(
                    expanded = divisionExpanded,
                    onExpandedChange = { divisionExpanded = !divisionExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = form.division,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isBn) "বিভাগ (Division)" else "Division *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = divisionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = divisionExpanded,
                        onDismissRequest = { divisionExpanded = false }
                    ) {
                        AmarShopRepository.BD_DIVISIONS.forEach { div ->
                            DropdownMenuItem(
                                text = { Text(div) },
                                onClick = {
                                    viewModel.updateCheckoutDivision(div)
                                    divisionExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // District Dropdown
                ExposedDropdownMenuBox(
                    expanded = districtExpanded,
                    onExpandedChange = { districtExpanded = !districtExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = form.district,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isBn) "জেলা (District)" else "District *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = districtExpanded,
                        onDismissRequest = { districtExpanded = false }
                    ) {
                        currentDistricts.forEach { dist ->
                            DropdownMenuItem(
                                text = { Text(dist) },
                                onClick = {
                                    viewModel.updateCheckoutDistrict(dist)
                                    districtExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.upazila,
                    onValueChange = { viewModel.updateCheckoutUpazila(it) },
                    label = { Text(if (isBn) "উপজেলা / থানা (ঐচ্ছিক)" else "Thana / Upazila (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.address,
                    onValueChange = { viewModel.updateCheckoutAddress(it) },
                    label = { Text(if (isBn) "বিস্তারিত ঠিকানা (বাড়ি/রোড/এলাকা) *" else "Complete Street Address (House, Road, Area) *") },
                    isError = form.addressError != null,
                    supportingText = form.addressError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_address_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.orderNotes,
                    onValueChange = { viewModel.updateCheckoutNotes(it) },
                    label = { Text(if (isBn) "ডেলিভারি নোট (ঐচ্ছিক)" else "Delivery Instructions / Notes (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Delivery Fee Notice
                Surface(
                    color = AmarTealSecondary.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AmarTealSecondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (form.district.equals("Dhaka", ignoreCase = true)) {
                                if (isBn) "ঢাকার অভ্যন্তরে ডেলিভারি চার্জ: ৳৬০" else "Inside Dhaka Delivery: ৳60 (Fast 24h Courier)"
                            } else {
                                if (isBn) "ঢাকার বাইরে ডেলিভারি চার্জ: ৳১২০" else "Outside Dhaka Delivery: ৳120 (All 63 Districts)"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = AmarTealSecondary
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 3: Payment Method
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = AmarGoldAccent.copy(alpha = 0.2f),
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "3",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AmarOrangePrimary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "পেমেন্ট পদ্ধতি নির্বাচন করুন" else "Payment Method",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Option 1: Cash on Delivery
                PaymentOptionRow(
                    title = if (isBn) "ক্যাশ অন ডেলিভারি (Cash on Delivery)" else "Cash on Delivery (COD)",
                    desc = if (isBn) "পণ্য হাতে পেয়ে নগদ টাকা পরিশোধ করুন" else "Pay in cash when goods are delivered to your door",
                    isSelected = form.paymentMethod == "Cash on Delivery",
                    icon = Icons.Default.LocalAtm,
                    onSelect = { viewModel.updateCheckoutPaymentMethod("Cash on Delivery") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option 2: bKash
                PaymentOptionRow(
                    title = "bKash (বিকাশ অনলাইন পেমেন্ট)",
                    desc = "Merchant Wallet: 01888-AMARBD • Instant Verification",
                    isSelected = form.paymentMethod == "bKash",
                    icon = Icons.Default.AccountBalanceWallet,
                    onSelect = { viewModel.updateCheckoutPaymentMethod("bKash") }
                )

                // Option 3: Nagad
                PaymentOptionRow(
                    title = "Nagad (নগদ অনলাইন পেমেন্ট)",
                    desc = "Merchant Wallet: 01777-AMARBD • Post-Payment Verification",
                    isSelected = form.paymentMethod == "Nagad",
                    icon = Icons.Default.PhoneAndroid,
                    onSelect = { viewModel.updateCheckoutPaymentMethod("Nagad") }
                )

                // If bKash or Nagad selected, show MFS Transaction verification inputs
                if (form.paymentMethod != "Cash on Delivery") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "${form.paymentMethod} Payment Verification:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "1. Send ৳${finalPayable.toInt()} to AmarShop merchant wallet.\n2. Enter your account number and Transaction ID (TrxID) below:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = form.mfsPhone,
                                onValueChange = { viewModel.updateCheckoutMfsPhone(it) },
                                label = { Text("Your ${form.paymentMethod} Number") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = form.paymentTrxId,
                                onValueChange = { viewModel.updateCheckoutPaymentTrxId(it) },
                                label = { Text("Transaction ID (TrxID) *") },
                                placeholder = { Text("e.g. 9J4K28LS") },
                                isError = form.trxError != null,
                                supportingText = form.trxError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("checkout_trx_input")
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Final Payable Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBn) "চূড়ান্ত হিসাব" else "Payment Breakdown",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(if (isBn) "পণ্যের মোট মূল্য" else "Subtotal", style = MaterialTheme.typography.bodySmall)
                    Text("৳${cartSummary.subtotal.toInt()}", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(if (isBn) "ডেলিভারি ফি (${form.district})" else "Delivery Fee (${form.district})", style = MaterialTheme.typography.bodySmall)
                    Text("৳${dynamicDeliveryFee.toInt()}", style = MaterialTheme.typography.bodySmall)
                }
                if (cartSummary.discount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (isBn) "কুপন ছাড়" else "Discount", style = MaterialTheme.typography.bodySmall, color = AmarOrangePrimary)
                        Text("-৳${cartSummary.discount.toInt()}", style = MaterialTheme.typography.bodySmall, color = AmarOrangePrimary)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(if (isBn) "সর্বমোট প্রদেয়" else "Total Payable", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text(
                        text = "৳${finalPayable.toInt()}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AmarOrangePrimary
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm & Place Order CTA
        Button(
            onClick = {
                viewModel.placeOrder { newOrderId ->
                    confirmedOrderId = newOrderId
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("confirm_order_btn")
        ) {
            Text(
                text = if (isBn) "অর্ডার সম্পন্ন করুন (৳${finalPayable.toInt()})" else "Confirm & Place Order (৳${finalPayable.toInt()})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Success Dialog
    confirmedOrderId?.let { orderId ->
        AlertDialog(
            onDismissRequest = {
                confirmedOrderId = null
                viewModel.navigate(Screen.OrderTracking(orderId))
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AmarTealSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isBn) "অর্ডার সফল হয়েছে!" else "Order Placed Successfully!")
                }
            },
            text = {
                Column {
                    Text(
                        text = if (isBn) "ধন্যবাদ! আপনার অর্ডারটি গ্রহণ করা হয়েছে।\nঅর্ডার আইডি: $orderId" else "Thank you! Your order has been placed successfully.\nOrder ID: $orderId",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBn) "ডেলিভারি ম্যান দ্রুততম সময়ে আপনার ঠিকানায় পৌঁছাবে।" else "You can track the live progress of your order in the Orders tab.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        confirmedOrderId = null
                        viewModel.navigate(Screen.OrderTracking(orderId))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary)
                ) {
                    Text(if (isBn) "অর্ডার ট্র্যাকিং দেখুন" else "Track Order")
                }
            }
        )
    }
}

@Composable
private fun PaymentOptionRow(
    title: String,
    desc: String,
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onSelect: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) AmarOrangePrimary.copy(alpha = 0.08f) else Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isSelected) AmarOrangePrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = AmarOrangePrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) AmarOrangePrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
