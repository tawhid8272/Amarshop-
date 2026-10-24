package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val nameBn: String,
    val description: String,
    val descriptionBn: String,
    val regularPrice: Double,
    val discountPrice: Double,
    val category: String,
    val categoryBn: String,
    val brand: String,
    val sku: String,
    val stockQuantity: Int,
    val rating: Float,
    val reviewCount: Int,
    val imageUrl: String,
    val sizesCsv: String = "",       // e.g. "S,M,L,XL" or "64GB,128GB,256GB"
    val colorsCsv: String = "",      // e.g. "Black,Blue,Red"
    val isFeatured: Boolean = false,
    val isBestseller: Boolean = false,
    val isNewArrival: Boolean = false
) {
    val discountPercentage: Int
        get() = if (regularPrice > discountPrice && regularPrice > 0) {
            (((regularPrice - discountPrice) / regularPrice) * 100).toInt()
        } else 0

    val isOutOfStock: Boolean
        get() = stockQuantity <= 0

    val isLowStock: Boolean
        get() = stockQuantity in 1..5

    fun getSizesList(): List<String> =
        if (sizesCsv.isBlank()) emptyList() else sizesCsv.split(",").map { it.trim() }

    fun getColorsList(): List<String> =
        if (colorsCsv.isBlank()) emptyList() else colorsCsv.split(",").map { it.trim() }
}

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val productName: String,
    val productNameBn: String,
    val unitPrice: Double,
    val selectedSize: String = "",
    val selectedColor: String = "",
    val quantity: Int = 1,
    val imageUrl: String = ""
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val customerName: String,
    val customerPhone: String,
    val division: String,
    val district: String,
    val upazila: String,
    val address: String,
    val orderNotes: String = "",
    val paymentMethod: String, // "Cash on Delivery", "bKash", "Nagad"
    val paymentTrxId: String = "",
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val totalAmount: Double,
    val status: String = "Pending", // Pending, Confirmed, Processing, Shipped, Delivered, Cancelled
    val timestamp: Long = System.currentTimeMillis(),
    val itemsSummary: String
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val authorName: String,
    val rating: Int,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey val code: String,
    val discountPercent: Int,
    val maxDiscount: Double,
    val minOrderAmount: Double,
    val description: String,
    val isActive: Boolean = true
)
