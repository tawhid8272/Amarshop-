package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CartItemEntity
import com.example.data.local.CouponEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.ReviewEntity
import com.example.data.repository.AmarShopRepository
import com.example.ui.model.AdminTab
import com.example.ui.model.CartSummary
import com.example.ui.model.CheckoutFormState
import com.example.ui.model.FilterState
import com.example.ui.model.Language
import com.example.ui.model.NavTab
import com.example.ui.model.Screen
import com.example.ui.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AmarShopViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = AmarShopRepository(db)

    // Language state: English (EN) or Bengali (BN)
    private val _language = MutableStateFlow(Language.EN)
    val language: StateFlow<Language> = _language.asStateFlow()

    // Mode state: Customer view vs Admin Management
    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackstack = mutableListOf<Screen>()

    private val _activeNavTab = MutableStateFlow(NavTab.HOME)
    val activeNavTab: StateFlow<NavTab> = _activeNavTab.asStateFlow()

    private val _activeAdminTab = MutableStateFlow(AdminTab.OVERVIEW)
    val activeAdminTab: StateFlow<AdminTab> = _activeAdminTab.asStateFlow()

    // Search and Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    // Data streams from repository
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistProductIds: StateFlow<List<String>> = repository.wishlistProductIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active coupon applied
    private val _appliedCoupon = MutableStateFlow<CouponEntity?>(null)
    val appliedCoupon: StateFlow<CouponEntity?> = _appliedCoupon.asStateFlow()

    private val _couponMessage = MutableStateFlow<String?>(null)
    val couponMessage: StateFlow<String?> = _couponMessage.asStateFlow()

    // Cart calculation
    val cartSummary: StateFlow<CartSummary> = combine(
        cartItems,
        _appliedCoupon
    ) { items, coupon ->
        val subtotal = items.sumOf { it.unitPrice * it.quantity }
        val deliveryFee = if (subtotal > 0) 60.0 else 0.0
        var discount = 0.0

        if (coupon != null && subtotal >= coupon.minOrderAmount) {
            discount = (subtotal * (coupon.discountPercent / 100.0)).coerceAtMost(coupon.maxDiscount)
        }

        val total = (subtotal + deliveryFee - discount).coerceAtLeast(0.0)
        CartSummary(
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            appliedCoupon = coupon?.code,
            total = total
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())

    // Filtered products list
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _searchQuery,
        _filterState
    ) { products, query, filter ->
        var list = products

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.nameBn.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.brand.lowercase().contains(q) ||
                it.sku.lowercase().contains(q)
            }
        }

        if (filter.category != "All") {
            list = list.filter { it.category.equals(filter.category, ignoreCase = true) }
        }

        if (filter.brand != "All") {
            list = list.filter { it.brand.equals(filter.brand, ignoreCase = true) }
        }

        list = list.filter { it.discountPrice in filter.minPrice..filter.maxPrice }

        if (filter.minRating > 0f) {
            list = list.filter { it.rating >= filter.minRating }
        }

        if (filter.inStockOnly) {
            list = list.filter { !it.isOutOfStock }
        }

        when (filter.sortOption) {
            SortOption.POPULAR -> list.sortedByDescending { it.rating * it.reviewCount }
            SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.discountPrice }
            SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.discountPrice }
            SortOption.NEWEST -> list.sortedByDescending { it.isNewArrival }
            SortOption.RATING -> list.sortedByDescending { it.rating }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Checkout form state
    private val _checkoutForm = MutableStateFlow(CheckoutFormState())
    val checkoutForm: StateFlow<CheckoutFormState> = _checkoutForm.asStateFlow()

    private val _lastPlacedOrderId = MutableStateFlow<String?>(null)
    val lastPlacedOrderId: StateFlow<String?> = _lastPlacedOrderId.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.EN) Language.BN else Language.EN
    }

    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
        if (_isAdminMode.value) {
            navigate(Screen.AdminDashboard)
        } else {
            navigate(Screen.Home)
        }
    }

    fun setAdminTab(tab: AdminTab) {
        _activeAdminTab.value = tab
    }

    fun navigate(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackstack.add(_currentScreen.value)
            _currentScreen.value = screen

            // Sync bottom nav tab
            when (screen) {
                is Screen.Home -> _activeNavTab.value = NavTab.HOME
                is Screen.Catalog -> _activeNavTab.value = NavTab.CATALOG
                is Screen.OrderTracking -> _activeNavTab.value = NavTab.ORDERS
                is Screen.Wishlist -> _activeNavTab.value = NavTab.WISHLIST
                is Screen.Account, is Screen.AboutUs, is Screen.ContactUs, is Screen.FAQ, is Screen.Policy ->
                    _activeNavTab.value = NavTab.ACCOUNT
                else -> Unit
            }
        }
    }

    fun navigateBack(): Boolean {
        if (screenBackstack.isNotEmpty()) {
            val previous = screenBackstack.removeAt(screenBackstack.size - 1)
            _currentScreen.value = previous
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: FilterState) {
        _filterState.value = filter
    }

    fun setCategoryFilter(category: String) {
        _filterState.value = _filterState.value.copy(category = category)
        navigate(Screen.Catalog(category))
    }

    fun resetFilters() {
        _filterState.value = FilterState()
        _searchQuery.value = ""
    }

    fun addToCart(product: ProductEntity, size: String = "", color: String = "", qty: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(product, size, color, qty)
        }
    }

    fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, newQuantity)
        }
    }

    fun removeCartItem(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeCartItem(cartItemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            repository.toggleWishlist(productId)
        }
    }

    fun applyCoupon(code: String) {
        viewModelScope.launch {
            val coupon = repository.validateCoupon(code)
            if (coupon != null) {
                val currentSubtotal = cartItems.value.sumOf { it.unitPrice * it.quantity }
                if (currentSubtotal >= coupon.minOrderAmount) {
                    _appliedCoupon.value = coupon
                    _couponMessage.value = "Coupon '${coupon.code}' applied successfully!"
                } else {
                    _couponMessage.value = "Min order amount ৳${coupon.minOrderAmount.toInt()} required for this coupon"
                }
            } else {
                _couponMessage.value = "Invalid or expired coupon code"
            }
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponMessage.value = null
    }

    fun updateCheckoutCustomerName(name: String) {
        _checkoutForm.value = _checkoutForm.value.copy(
            customerName = name,
            nameError = if (name.isBlank()) "Name is required" else null
        )
    }

    fun updateCheckoutPhone(phone: String) {
        val isValid = AmarShopRepository.isValidBangladeshPhone(phone)
        _checkoutForm.value = _checkoutForm.value.copy(
            customerPhone = phone,
            phoneError = if (!isValid && phone.length >= 10) "Enter valid 11-digit BD number (e.g. 01712345678)" else null
        )
    }

    fun updateCheckoutDivision(division: String) {
        val districts = AmarShopRepository.BD_DISTRICTS_BY_DIVISION[division] ?: listOf("Dhaka")
        val newDistrict = districts.firstOrNull() ?: "Dhaka"
        _checkoutForm.value = _checkoutForm.value.copy(
            division = division,
            district = newDistrict
        )
    }

    fun updateCheckoutDistrict(district: String) {
        _checkoutForm.value = _checkoutForm.value.copy(district = district)
    }

    fun updateCheckoutUpazila(upazila: String) {
        _checkoutForm.value = _checkoutForm.value.copy(upazila = upazila)
    }

    fun updateCheckoutAddress(address: String) {
        _checkoutForm.value = _checkoutForm.value.copy(
            address = address,
            addressError = if (address.isBlank()) "Address is required" else null
        )
    }

    fun updateCheckoutNotes(notes: String) {
        _checkoutForm.value = _checkoutForm.value.copy(orderNotes = notes)
    }

    fun updateCheckoutPaymentMethod(method: String) {
        _checkoutForm.value = _checkoutForm.value.copy(
            paymentMethod = method,
            trxError = null
        )
    }

    fun updateCheckoutMfsPhone(phone: String) {
        _checkoutForm.value = _checkoutForm.value.copy(mfsPhone = phone)
    }

    fun updateCheckoutPaymentTrxId(trxId: String) {
        _checkoutForm.value = _checkoutForm.value.copy(
            paymentTrxId = trxId,
            trxError = if (trxId.isBlank()) "Transaction ID is required for verification" else null
        )
    }

    fun placeOrder(onSuccess: (String) -> Unit) {
        val state = _checkoutForm.value
        val isPhoneValid = AmarShopRepository.isValidBangladeshPhone(state.customerPhone)
        val isNameValid = state.customerName.isNotBlank()
        val isAddressValid = state.address.isNotBlank()
        val isTrxValid = if (state.paymentMethod != "Cash on Delivery") {
            state.paymentTrxId.isNotBlank()
        } else true

        if (!isNameValid || !isPhoneValid || !isAddressValid || !isTrxValid) {
            _checkoutForm.value = state.copy(
                nameError = if (!isNameValid) "Customer name is required" else null,
                phoneError = if (!isPhoneValid) "Valid 11-digit BD phone is required (01XXXXXXXXX)" else null,
                addressError = if (!isAddressValid) "Full delivery address is required" else null,
                trxError = if (!isTrxValid) "Enter verified Transaction ID" else null
            )
            return
        }

        viewModelScope.launch {
            val items = cartItems.value
            if (items.isEmpty()) return@launch

            val subtotal = items.sumOf { it.unitPrice * it.quantity }
            val deliveryFee = AmarShopRepository.calculateDeliveryFee(state.district)
            val coupon = _appliedCoupon.value
            var discount = 0.0
            if (coupon != null && subtotal >= coupon.minOrderAmount) {
                discount = (subtotal * (coupon.discountPercent / 100.0)).coerceAtMost(coupon.maxDiscount)
            }

            val itemsSummary = items.joinToString("; ") { "${it.productName} (x${it.quantity})" }

            val orderId = repository.createOrder(
                customerName = state.customerName,
                customerPhone = state.customerPhone,
                division = state.division,
                district = state.district,
                upazila = state.upazila,
                address = state.address,
                notes = state.orderNotes,
                paymentMethod = state.paymentMethod,
                paymentTrxId = state.paymentTrxId,
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                discount = discount,
                itemsSummary = itemsSummary
            )

            _lastPlacedOrderId.value = orderId
            _appliedCoupon.value = null
            _couponMessage.value = null
            _checkoutForm.value = CheckoutFormState() // Reset
            onSuccess(orderId)
        }
    }

    // Admin Operations
    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun updateProductStock(productId: String, newStock: Int) {
        viewModelScope.launch {
            repository.updateStock(productId, newStock)
        }
    }

    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun submitReview(productId: String, author: String, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.addReview(productId, author, rating, comment)
        }
    }

    fun getProductReviews(productId: String) = repository.getReviews(productId)
}
