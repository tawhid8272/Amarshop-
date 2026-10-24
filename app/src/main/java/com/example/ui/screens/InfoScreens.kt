package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.model.Language
import com.example.ui.model.PolicyType
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarTealSecondary
import com.example.ui.viewmodel.AmarShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(viewModel: AmarShopViewModel) {
    BackHandler { viewModel.navigateBack() }
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isBn) "আমাদের সম্পর্কে" else "About AmarShop") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = if (isBn) "বাংলাদেশের শীর্ষস্থানীয় ডিজিটাল মার্কেটপ্লেস" else "Bangladesh’s Premier Digital Marketplace",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = AmarOrangePrimary)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isBn) {
                    "AmarShop হলো বাংলাদেশের ক্রেতা ও বিক্রেতাদের জন্য নির্মিত একটি আধুনিক, নিরাপদ ও নির্ভরযোগ্য ই-কমার্স প্ল্যাটফর্ম। আমাদের লক্ষ্য রাজধানী ঢাকা থেকে শুরু করে দেশের প্রতিটি জেলা, থানা ও প্রত্যন্ত অঞ্চলের মানুষের ঘরে ঘরে খাঁটি পণ্য, ন্যায্য মূল্য এবং বিশ্বস্ত সেবা পৌঁছে দেওয়া।\n\nআমরা স্থানীয় উৎপাদক, তাঁতি, ইলেকট্রনিক্স ব্র্যান্ড এবং দেশীয় উদ্যোক্তাদের সাথে সরাসরি যুক্ত হয়ে কাজ করি।"
                } else {
                    "AmarShop is a modern, dependable e-commerce platform built specifically for Bangladesh. Inspired by the convenience of Daraz, Amazon, and Alibaba, our mission is to empower both urban shoppers and rural consumers across all 64 districts with genuine products, fair pricing, and rapid delivery.\n\nWe directly collaborate with verified manufacturers, handloom weavers in Tangail and Demra, and top national brands to ensure 100% authenticity."
                },
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(viewModel: AmarShopViewModel) {
    BackHandler { viewModel.navigateBack() }
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isBn) "যোগাযোগ ও কাস্টমার কেয়ার" else "Contact Us & Help Center") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isBn) "২৪/৭ কাস্টমার হেল্পলাইন" else "24/7 Dedicated Helpline",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AmarOrangePrimary)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = AmarTealSecondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "09612-AMARBD (09612-262723)", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = AmarTealSecondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "support@amarshop.com.bd", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AmarTealSecondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Plot 14, Road 11, Gulshan-1, Dhaka-1212, Bangladesh", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(viewModel: AmarShopViewModel) {
    BackHandler { viewModel.navigateBack() }
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isBn) "সাধারণ জিজ্ঞাসা" else "FAQ") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FAQCard(
                q = if (isBn) "ডেলিভারি চার্জ কত এবং কতদিন সময় লাগে?" else "What is the delivery fee and timeframe?",
                a = if (isBn) "ঢাকার অভ্যন্তরে ডেলিভারি চার্জ মাত্র ৳৬০ (২৪-৪৮ ঘণ্টার মধ্যে)। ঢাকার বাইরে সকল ৬৩টি জেলা ও উপজেলায় ডেলিভারি চার্জ ৳১২০ (২-৪ কার্যদিবস)।" else "Inside Dhaka delivery fee is ৳60 (delivered within 24-48 hours). Outside Dhaka delivery fee is ৳120 across all other 63 districts (delivered within 2-4 business days)."
            )
            FAQCard(
                q = if (isBn) "বিকাশ বা নগদ দিয়ে কীভাবে পেমেন্ট করব?" else "How do I pay using bKash or Nagad?",
                a = if (isBn) "চেকআউটের সময় বিকাশ বা নগদ নির্বাচন করুন। আমাদের মার্চেন্ট নম্বরে মোট টাকা পাঠিয়ে আপনার ট্রানজেকশন আইডি (TrxID) ইনপুট দিলেই অর্ডার তাৎক্ষণিক কনফার্ম হয়ে যাবে।" else "Select bKash or Nagad at checkout, send the total payable to our merchant number, and enter your Transaction ID (TrxID) for immediate verification."
            )
            FAQCard(
                q = if (isBn) "ক্যাশ অন ডেলিভারি সুবিধা আছে কি?" else "Is Cash on Delivery (COD) supported?",
                a = if (isBn) "হ্যাঁ! বাংলাদেশের যে কোনো প্রান্তে ক্যাশ অন ডেলিভারি প্রযোজ্য। পণ্য হাতে পাওয়ার পর মূল্য পরিশোধ করতে পারবেন।" else "Yes! Cash on Delivery is available across all 64 districts in Bangladesh with zero advance fees."
            )
            FAQCard(
                q = if (isBn) "পণ্য পছন্দ না হলে কীভাবে রিটার্ন করব?" else "How do I return a damaged or wrong product?",
                a = if (isBn) "পণ্য পাওয়ার ৭ দিনের মধ্যে আমাদের হেল্পলাইনে যোগাযোগ করুন। আমরা সম্পূর্ণ বিনামূল্যে পণ্য পরিবর্তন অথবা রিফান্ড নিশ্চিত করব।" else "Notify our customer support within 7 days of receiving the item. We provide free doorstep pickup and full refunds or replacements."
            )
        }
    }
}

@Composable
private fun FAQCard(q: String, a: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = q, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = AmarOrangePrimary))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = a, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(type: PolicyType, viewModel: AmarShopViewModel) {
    BackHandler { viewModel.navigateBack() }
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isBn = language == Language.BN

    val title = when (type) {
        PolicyType.PRIVACY -> if (isBn) "গোপনীয়তা ও প্রাইভেসি পলিসি" else "Privacy Policy"
        PolicyType.TERMS -> if (isBn) "শর্তাবলী" else "Terms of Service"
        PolicyType.RETURN_REFUND_DELIVERY -> if (isBn) "রিটার্ন, রিফান্ড ও ডেলিভারি পলিসি" else "Return, Refund & Delivery Policy"
    }

    val body = when (type) {
        PolicyType.PRIVACY -> if (isBn) {
            "AmarShop গ্রাহকের তথ্যের সুরক্ষা ও নিরাপত্তাকে সর্বোচ্চ অগ্রাধিকার দেয়। আপনার নাম, ফোন নম্বর ও ডেলিভারি ঠিকানা শুধুমাত্র পার্সেল প্রেরণের কাজে ব্যবহৃত হয়। কোনো তৃতীয় পক্ষের কাছে গ্রাহকের তথ্য বিক্রি বা হস্তান্তর করা হয় না।"
        } else {
            "AmarShop Bangladesh is committed to safeguarding customer confidentiality. Personal information including customer name, mobile numbers, and delivery addresses is strictly processed for fulfilling purchases and courier tracking. We comply with all applicable Bangladesh digital security standards."
        }
        PolicyType.TERMS -> if (isBn) {
            "AmarShop অ্যাপ ব্যবহারের মাধ্যমে আপনি আমাদের সকল নিয়ম ও শর্ত মেনে নিচ্ছেন। প্ল্যাটফর্মে প্রদর্শিত সকল মূল্য বাংলাদেশি টাকায় (৳) নির্ধারিত।"
        } else {
            "By using AmarShop, you agree to our standard terms of service. All prices are transparently displayed in Bangladeshi Taka (৳)."
        }
        PolicyType.RETURN_REFUND_DELIVERY -> if (isBn) {
            "১. ডেলিভারি: ঢাকাতে ২৪-৪৮ ঘণ্টা (৳৬০), ঢাকার বাইরে ২-৪ দিন (৳১২০)।\n২. রিটার্ন: পণ্য ক্রটির ক্ষেত্রে ডেলিভারির ৭ দিনের মধ্যে বিনামূল্যে রিটার্নযোগ্য।\n৩. রিফান্ড: রিটার্ন গৃহীত হওয়ার ৩-৫ কার্যদিবসের মধ্যে বিকাশ/নগদে মূল্য ফেরত দেওয়া হবে।"
        } else {
            "1. Delivery: Inside Dhaka within 24-48 hours (৳60), outside Dhaka within 2-4 business days (৳120).\n2. Returns: 7-day hassle-free replacement for defective or mismatched goods.\n3. Refunds: Issued back to your original payment method (bKash, Nagad, or Bank) within 3-5 business days."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = AmarOrangePrimary))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = body, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
        }
    }
}
