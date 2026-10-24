package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.model.Language
import com.example.ui.model.PolicyType
import com.example.ui.model.Screen
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarTealSecondary
import com.example.ui.viewmodel.AmarShopViewModel

@Composable
fun ProfileScreen(
    viewModel: AmarShopViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val language by viewModel.language.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = if (isBn) "আমার অ্যাকাউন্ট" else "My Account",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(14.dp))

        // User Avatar & Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = AmarOrangePrimary.copy(alpha = 0.15f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = AmarOrangePrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tawhidul Basher",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = AmarTealSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "mdtawhidulbasher002@gmail.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "+880 1712-345678 • Dhaka, Bangladesh",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmarTealSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preferences & Mode Switch
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBn) "অ্যাপ সেটিংস ও ভাষা" else "Preferences & Controls",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = AmarTealSecondary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Language toggle row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = AmarOrangePrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBn) "ভাষা: বাংলা" else "Language: English",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = if (isBn) "ইংরেজি করতে টগল করুন" else "Switch to বাংলা",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = language == Language.BN,
                        onCheckedChange = { viewModel.toggleLanguage() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AmarOrangePrimary,
                            checkedTrackColor = AmarOrangePrimary.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // Switch to Admin Console Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleAdminMode() }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = AmarTealSecondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBn) "অ্যাডমিন ড্যাশবোর্ড মোড" else "Admin Management Console",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isBn) "পণ্য, ইনভেন্টরি ও অর্ডার ম্যানেজমেন্ট" else "Manage inventory, orders & sales analytics",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Navigation Links
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBn) "শপিং ও সহায়িকা" else "Shopping & Help Center",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = AmarTealSecondary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.LocalShipping,
                    title = if (isBn) "আমার অর্ডার ট্র্যাকিং" else "My Orders & Tracking",
                    onClick = { viewModel.navigate(Screen.OrderTracking()) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Favorite,
                    title = if (isBn) "পছন্দের তালিকা" else "My Wishlist",
                    onClick = { viewModel.navigate(Screen.Wishlist) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Help,
                    title = if (isBn) "সাধারণ জিজ্ঞাসা (FAQ)" else "Frequently Asked Questions (FAQ)",
                    onClick = { viewModel.navigate(Screen.FAQ) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = if (isBn) "আমাদের সম্পর্কে (About Us)" else "About AmarShop Bangladesh",
                    onClick = { viewModel.navigate(Screen.AboutUs) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.ContactSupport,
                    title = if (isBn) "যোগাযোগ ও কাস্টমার কেয়ার" else "Contact Us & Helpline",
                    onClick = { viewModel.navigate(Screen.ContactUs) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Policy,
                    title = if (isBn) "রিটার্ন, রিফান্ড ও ডেলিভারি পলিসি" else "Return, Refund & Delivery Policy",
                    onClick = { viewModel.navigate(Screen.Policy(PolicyType.RETURN_REFUND_DELIVERY)) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Security,
                    title = if (isBn) "প্রাইভেসি ও শর্তাবলী" else "Privacy Policy & Terms",
                    onClick = { viewModel.navigate(Screen.Policy(PolicyType.PRIVACY)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AmarShop Bangladesh v1.0.0 • Production Ready",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AmarOrangePrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
        )
    }
}
