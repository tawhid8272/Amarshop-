package com.example.ui.model

enum class Language {
    EN, BN
}

enum class NavTab {
    HOME,
    CATALOG,
    ORDERS,
    WISHLIST,
    ACCOUNT
}

enum class AdminTab {
    OVERVIEW,
    PRODUCTS,
    ORDERS,
    REPORTS
}

enum class SortOption {
    POPULAR,
    PRICE_LOW_HIGH,
    PRICE_HIGH_LOW,
    NEWEST,
    RATING
}

sealed interface Screen {
    data object Home : Screen
    data class Catalog(val initialCategory: String? = null) : Screen
    data class ProductDetail(val productId: String) : Screen
    data object Cart : Screen
    data object Checkout : Screen
    data class OrderTracking(val initialOrderId: String? = null) : Screen
    data object Wishlist : Screen
    data object Account : Screen
    data object AboutUs : Screen
    data object ContactUs : Screen
    data object FAQ : Screen
    data class Policy(val type: PolicyType) : Screen
    data object AdminDashboard : Screen
}

enum class PolicyType {
    PRIVACY,
    TERMS,
    RETURN_REFUND_DELIVERY
}

data class FilterState(
    val category: String = "All",
    val brand: String = "All",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 70000.0,
    val minRating: Float = 0f,
    val inStockOnly: Boolean = false,
    val sortOption: SortOption = SortOption.POPULAR
)

data class CartSummary(
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 60.0,
    val discount: Double = 0.0,
    val appliedCoupon: String? = null,
    val total: Double = 0.0
)

data class CheckoutFormState(
    val customerName: String = "",
    val customerPhone: String = "",
    val division: String = "Dhaka",
    val district: String = "Dhaka",
    val upazila: String = "",
    val address: String = "",
    val orderNotes: String = "",
    val paymentMethod: String = "Cash on Delivery", // "Cash on Delivery", "bKash", "Nagad"
    val mfsPhone: String = "",
    val paymentTrxId: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val addressError: String? = null,
    val trxError: String? = null
)
