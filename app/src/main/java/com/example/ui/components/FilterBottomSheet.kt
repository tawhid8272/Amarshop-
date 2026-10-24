package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.model.FilterState
import com.example.ui.model.Language
import com.example.ui.model.SortOption
import com.example.ui.theme.AmarOrangePrimary
import com.example.ui.theme.AmarTealSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    filterState: FilterState,
    language: Language,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onApply: (FilterState) -> Unit,
    onReset: () -> Unit
) {
    val isBn = language == Language.BN

    var currentFilter by remember(filterState) { mutableStateOf(filterState) }
    var priceRange by remember(filterState) {
        mutableStateOf(currentFilter.minPrice.toFloat()..currentFilter.maxPrice.toFloat())
    }

    val categories = listOf(
        "All" to if (isBn) "সবগুলো" else "All Categories",
        "Electronics" to if (isBn) "ইলেকট্রনিক্স" else "Electronics",
        "Men's Fashion" to if (isBn) "পুরুষের পোশাক" else "Men's Fashion",
        "Women's Fashion" to if (isBn) "নারীর ফ্যাশন" else "Women's Fashion",
        "Groceries" to if (isBn) "মুদি ও খাদ্য" else "Groceries",
        "Home & Living" to if (isBn) "গৃহস্থালি" else "Home & Living",
        "Health & Beauty" to if (isBn) "সৌন্দর্য ও স্বাস্থ্য" else "Health & Beauty"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBn) "ফিল্টার ও সাজানো" else "Filter & Sort",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = if (isBn) "রিসেট" else "Reset All",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = AmarOrangePrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable {
                            currentFilter = FilterState()
                            priceRange = 0f..70000f
                            onReset()
                        }
                        .padding(4.dp)
                        .testTag("filter_reset_btn")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sort By Options
            Text(
                text = if (isBn) "সাজান (Sort By)" else "Sort By",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    SortOption.POPULAR to (if (isBn) "জনপ্রিয়" else "Popular"),
                    SortOption.PRICE_LOW_HIGH to (if (isBn) "দাম: কম থেকে বেশি" else "Price: Low to High"),
                    SortOption.PRICE_HIGH_LOW to (if (isBn) "দাম: বেশি থেকে কম" else "Price: High to Low"),
                    SortOption.RATING to (if (isBn) "সেরা রেটিং" else "Top Rated"),
                    SortOption.NEWEST to (if (isBn) "নতুন আগমন" else "New Arrivals")
                ).forEach { (option, label) ->
                    FilterChip(
                        selected = currentFilter.sortOption == option,
                        onClick = { currentFilter = currentFilter.copy(sortOption = option) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmarOrangePrimary.copy(alpha = 0.15f),
                            selectedLabelColor = AmarOrangePrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips
            Text(
                text = if (isBn) "ক্যাটাগরি" else "Category",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catKey, label) ->
                    FilterChip(
                        selected = currentFilter.category == catKey,
                        onClick = { currentFilter = currentFilter.copy(category = catKey) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmarTealSecondary.copy(alpha = 0.15f),
                            selectedLabelColor = AmarTealSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Price Range Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isBn) "মূল্যসীমা" else "Price Range",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "৳${priceRange.start.toInt()} - ৳${priceRange.endInclusive.toInt()}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AmarOrangePrimary
                    )
                )
            }
            RangeSlider(
                value = priceRange,
                onValueChange = { range ->
                    priceRange = range
                    currentFilter = currentFilter.copy(
                        minPrice = range.start.toDouble(),
                        maxPrice = range.endInclusive.toDouble()
                    )
                },
                valueRange = 0f..70000f,
                colors = SliderDefaults.colors(
                    thumbColor = AmarOrangePrimary,
                    activeTrackColor = AmarOrangePrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // In Stock Only Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBn) "শুধু স্টকে আছে এমন পণ্য" else "In Stock Only",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = if (isBn) "স্টক শেষ হওয়া পণ্য বাদ দিন" else "Exclude unavailable items",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = currentFilter.inStockOnly,
                    onCheckedChange = { currentFilter = currentFilter.copy(inStockOnly = it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AmarOrangePrimary,
                        checkedTrackColor = AmarOrangePrimary.copy(alpha = 0.3f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }

                Button(
                    onClick = {
                        onApply(currentFilter)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarOrangePrimary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("apply_filter_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isBn) "প্রয়োগ করুন" else "Apply Filters")
                }
            }
        }
    }
}
