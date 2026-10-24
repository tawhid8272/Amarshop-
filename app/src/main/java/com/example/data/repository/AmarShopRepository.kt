package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.CartItemEntity
import com.example.data.local.CouponEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.WishlistItemEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AmarShopRepository(private val db: AppDatabase) {

    val allProducts: Flow<List<ProductEntity>> = db.productDao().getAllProducts()
    val cartItems: Flow<List<CartItemEntity>> = db.cartDao().getCartItems()
    val allOrders: Flow<List<OrderEntity>> = db.orderDao().getAllOrders()
    val wishlistProductIds: Flow<List<String>> = db.wishlistDao().getWishlistProductIds()

    suspend fun getProductById(id: String): ProductEntity? = db.productDao().getProductById(id)

    suspend fun insertProduct(product: ProductEntity) = db.productDao().insertProduct(product)

    suspend fun updateProduct(product: ProductEntity) = db.productDao().updateProduct(product)

    suspend fun deleteProduct(product: ProductEntity) = db.productDao().deleteProduct(product)

    suspend fun updateStock(productId: String, newStock: Int) =
        db.productDao().updateStock(productId, newStock)

    suspend fun addToCart(
        product: ProductEntity,
        size: String = "",
        color: String = "",
        qty: Int = 1
    ) {
        val existingItem = db.cartDao().addToCart(
            CartItemEntity(
                productId = product.id,
                productName = product.name,
                productNameBn = product.nameBn,
                unitPrice = if (product.discountPrice > 0) product.discountPrice else product.regularPrice,
                selectedSize = size,
                selectedColor = color,
                quantity = qty,
                imageUrl = product.imageUrl
            )
        )
    }

    suspend fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        if (newQuantity <= 0) {
            db.cartDao().removeItem(cartItemId)
        } else {
            db.cartDao().updateQuantity(cartItemId, newQuantity)
        }
    }

    suspend fun removeCartItem(cartItemId: Long) = db.cartDao().removeItem(cartItemId)

    suspend fun clearCart() = db.cartDao().clearCart()

    suspend fun toggleWishlist(productId: String) {
        val exists = db.wishlistDao().isWishlisted(productId)
        if (exists) {
            db.wishlistDao().removeFromWishlist(productId)
        } else {
            db.wishlistDao().addToWishlist(WishlistItemEntity(productId))
        }
    }

    fun getReviews(productId: String): Flow<List<ReviewEntity>> =
        db.reviewDao().getReviewsForProduct(productId)

    suspend fun addReview(productId: String, author: String, rating: Int, comment: String) {
        db.reviewDao().insertReview(
            ReviewEntity(
                productId = productId,
                authorName = author,
                rating = rating,
                comment = comment
            )
        )
    }

    suspend fun validateCoupon(code: String): CouponEntity? =
        db.couponDao().getCoupon(code.trim().uppercase())

    suspend fun createOrder(
        customerName: String,
        customerPhone: String,
        division: String,
        district: String,
        upazila: String,
        address: String,
        notes: String,
        paymentMethod: String,
        paymentTrxId: String,
        subtotal: Double,
        deliveryFee: Double,
        discount: Double,
        itemsSummary: String
    ): String {
        val orderId = "BD-" + (100000 + (Math.random() * 900000).toInt())
        val total = (subtotal + deliveryFee - discount).coerceAtLeast(0.0)

        val order = OrderEntity(
            orderId = orderId,
            customerName = customerName,
            customerPhone = customerPhone,
            division = division,
            district = district,
            upazila = upazila,
            address = address,
            orderNotes = notes,
            paymentMethod = paymentMethod,
            paymentTrxId = paymentTrxId,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            totalAmount = total,
            status = "Pending",
            itemsSummary = itemsSummary
        )

        db.orderDao().insertOrder(order)
        db.cartDao().clearCart()
        return orderId
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) =
        db.orderDao().updateOrderStatus(orderId, newStatus)

    suspend fun seedDatabaseIfEmpty() {
        if (db.productDao().getCount() > 0) return

        val seedProducts = listOf(
            ProductEntity(
                id = "prod_01",
                name = "Redmi Note 13 Pro 5G (8GB/256GB)",
                nameBn = "রেডমি নোট ১৩ প্রো ৫জি (৮জিবি/২৫৬জিবি)",
                description = "Ultra-clear 200MP camera with OIS, 1.5K 120Hz AMOLED curved display, 67W turbo charge with 5000mAh battery. Official Bangladesh warranty.",
                descriptionBn = "২০০ মেগাপিক্সেল ওআইএস ক্যামেরা, ১.৫কে ১২০ হার্টজ অ্যামোলেড কার্ভড ডিসপ্লে, ৬৭ ওয়াট টার্বো চার্জ ও ৫০০০ এমএএইচ ব্যাটারি। অফিসিয়াল বিটিআরসি অনুমোদিত ওয়ারেন্টি।",
                regularPrice = 36999.0,
                discountPrice = 33999.0,
                category = "Electronics",
                categoryBn = "ইলেকট্রনিক্স",
                brand = "Xiaomi",
                sku = "REDMI-N13P-256",
                stockQuantity = 18,
                rating = 4.8f,
                reviewCount = 142,
                imageUrl = "electronics_phone",
                sizesCsv = "8GB/256GB, 12GB/512GB",
                colorsCsv = "Midnight Black, Ocean Teal, Aurora Purple",
                isFeatured = true,
                isBestseller = true,
                isNewArrival = false
            ),
            ProductEntity(
                id = "prod_02",
                name = "Aarong Premium Cotton Festive Panjabi",
                nameBn = "আড়ং প্রিমিয়াম সুতি উৎসব পাঞ্জাবি",
                description = "Crafted from 100% fine combed cotton with hand-embroidered neckline, mandarin collar, and mother-of-pearl buttons. Ideal for Eid & festive celebrations.",
                descriptionBn = "১০০% খাঁটি সুতি কাপড়ে আকর্ষণীয় হাতের সুতার কাজ, মান্দারিন কলার এবং প্রিমিয়াম বোতাম। ঈদ ও যেকোনো উৎসবের জন্য সেরা পছন্দ।",
                regularPrice = 3850.0,
                discountPrice = 3450.0,
                category = "Men's Fashion",
                categoryBn = "পুরুষের পোশাক",
                brand = "Aarong",
                sku = "AAR-PANJ-09",
                stockQuantity = 24,
                rating = 4.9f,
                reviewCount = 89,
                imageUrl = "fashion_panjabi",
                sizesCsv = "38 (M), 40 (L), 42 (XL), 44 (XXL)",
                colorsCsv = "Deep Maroon, Royal Navy, Pure White",
                isFeatured = true,
                isBestseller = true,
                isNewArrival = true
            ),
            ProductEntity(
                id = "prod_03",
                name = "Authentic Dhakai Jamdani Handloom Saree",
                nameBn = "আসল ঢাকাই জামদানি তাঁতের শাড়ি",
                description = "Traditional 84-count pure cotton thread Jamdani with intricate floral jaal motifs handwoven by master artisans in Demra, Narayanganj. Includes blouse piece.",
                descriptionBn = "ডেমরা নারায়ণগঞ্জের ঐতিহ্যবাহী তাঁতিদের হাতে বোনা ৮৪ কাউন্টের নিখুঁত সুতি ঢাকাই জামদানি। রাজকীয় ও আভিজাত্যপূর্ণ আকর্ষণ। সাথে ব্লাউজ পিস অন্তর্ভুক্ত।",
                regularPrice = 8500.0,
                discountPrice = 6990.0,
                category = "Women's Fashion",
                categoryBn = "নারীর ফ্যাশন",
                brand = "Heritage Bengal",
                sku = "JAM-DHAKA-102",
                stockQuantity = 7,
                rating = 4.9f,
                reviewCount = 76,
                imageUrl = "fashion_saree",
                sizesCsv = "Standard 12 Haath (with Blouse Piece)",
                colorsCsv = "Ruby Red & Gold, Emerald Green, Royal Black",
                isFeatured = true,
                isBestseller = false,
                isNewArrival = true
            ),
            ProductEntity(
                id = "prod_04",
                name = "Haylou Solar Plus RT3 AMOLED Smartwatch",
                nameBn = "হায়লু সোলার প্লাস আরটি৩ অ্যামোলেড স্মার্টওয়াচ",
                description = "1.43\" AMOLED display, Bluetooth phone calling, 105 workout modes, 24/7 SpO2 and heart rate monitor. IP68 water resistant with 7-day battery life.",
                descriptionBn = "১.৪৩ ইঞ্চি অ্যামোলেড স্ক্রিন, সরাসরি ব্লুটুথ কলিং, ১০৫টি স্পোর্টস মোড এবং সার্বক্ষণিক হার্ট রেট ও রক্তে অক্সিজেনের মাত্রা নিরীক্ষণ। আইপি৬৮ ওয়াটারপ্রুফ।",
                regularPrice = 4999.0,
                discountPrice = 4250.0,
                category = "Electronics",
                categoryBn = "ইলেকট্রনিক্স",
                brand = "Haylou",
                sku = "HAY-RT3-BLK",
                stockQuantity = 15,
                rating = 4.7f,
                reviewCount = 94,
                imageUrl = "electronics_watch",
                sizesCsv = "Standard (Adjustable Strap)",
                colorsCsv = "Matte Black, Silver Gray",
                isFeatured = true,
                isBestseller = true,
                isNewArrival = false
            ),
            ProductEntity(
                id = "prod_05",
                name = "Radhuni Pure Mustard Oil 1L (Kachi Ghani)",
                nameBn = "রাঁধুনী খাঁটি সরিষার তেল ১ লিটার",
                description = "Cold pressed from the finest grade mustard seeds with a natural pungent aroma and authentic Bangladeshi flavor for traditional cooking and bhortas.",
                descriptionBn = "বাছাইকৃত সরিষা দানা থেকে কোল্ড প্রেস পদ্ধতিতে তৈরি খাঁটি ঝাঁঝালো ঘানির সরিষার তেল। দেশিয় রান্নায় ও ভর্তায় এনে দেয় অতুলনীয় স্বাদ ও গন্ধ।",
                regularPrice = 360.0,
                discountPrice = 330.0,
                category = "Groceries",
                categoryBn = "মুদি ও নিত্যপণ্য",
                brand = "Radhuni",
                sku = "RAD-OIL-1L",
                stockQuantity = 50,
                rating = 4.9f,
                reviewCount = 312,
                imageUrl = "grocery_oil",
                sizesCsv = "1 Litre Pet Bottle",
                colorsCsv = "Golden Yellow",
                isFeatured = false,
                isBestseller = true,
                isNewArrival = false
            ),
            ProductEntity(
                id = "prod_06",
                name = "Ispahani Mirzapore Best Leaf Tea 400g",
                nameBn = "ইস্পাহানি মির্জাপুর বেস্ট লিফ চা ৪০০ গ্রাম",
                description = "Selected from the lush tea gardens of Sreemangal and Chittagong. Rich liquor, invigorating aroma, and deeply satisfying cup every single morning.",
                descriptionBn = "শ্রীমঙ্গল ও চট্টগ্রামের সেরা বাগানের সতেজ চা পাতার ব্লেন্ড। দারুণ লিকার ও সতেজকারী ঘ্রাণ যা প্রতিটি চুমুকে এনে দেয় মানসিক প্রফুল্লতা।",
                regularPrice = 240.0,
                discountPrice = 225.0,
                category = "Groceries",
                categoryBn = "মুদি ও নিত্যপণ্য",
                brand = "Ispahani",
                sku = "ISP-TEA-400",
                stockQuantity = 85,
                rating = 4.8f,
                reviewCount = 205,
                imageUrl = "grocery_tea",
                sizesCsv = "400g Foil Pack",
                colorsCsv = "Standard",
                isFeatured = false,
                isBestseller = true,
                isNewArrival = false
            ),
            ProductEntity(
                id = "prod_07",
                name = "Walton WIZ-18K Inverter 1.5 Ton AC",
                nameBn = "ওয়ালটন ১.৫ টন ইনভার্টার স্মার্ট এসি",
                description = "Up to 60% energy saving with dual inverter technology, ionizer air purification, rapid 30s cooling, and 100% copper condenser. Walton official 10-year compressor warranty.",
                descriptionBn = "ডুয়াল ইনভার্টার টেকনোলজি যা ৬০% পর্যন্ত বিদ্যুৎ সাশ্রয় করে। বাতাস জীবাণুমুক্ত করার বিশেষ ফিল্টার ও দ্রুত ঠান্ডা করার সুবিধা। ১০ বছরের অফিসিয়াল কম্প্রেসার ওয়ারেন্টি।",
                regularPrice = 64900.0,
                discountPrice = 57900.0,
                category = "Home & Living",
                categoryBn = "গৃহস্থালি সরঞ্জাম",
                brand = "Walton",
                sku = "WALT-AC-15T",
                stockQuantity = 4,
                rating = 4.7f,
                reviewCount = 38,
                imageUrl = "home_ac",
                sizesCsv = "1.5 Ton (Up to 180 sq.ft)",
                colorsCsv = "Pearl White",
                isFeatured = true,
                isBestseller = false,
                isNewArrival = true
            ),
            ProductEntity(
                id = "prod_08",
                name = "Vision Infrared Cooker Touch Control 2200W",
                nameBn = "ভিশন ইনফ্রারেড কুকার ২২০০ ওয়াট",
                description = "Compatible with any metal, ceramic, or glass pot. 8 cooking functions, child lock, digital LED display, and shatterproof ceramic glass plate.",
                descriptionBn = "সব ধরণের পাত্রে রান্না উপযোগী আধুনিক ইনফ্রারেড কুকার। ৮টি রান্নার মোড, ডিজিটাল টাচ কন্ট্রোল এবং শক্তিশালী সিরামিক প্লেট। দ্রুত ও সাশ্রয়ী রান্না।",
                regularPrice = 3900.0,
                discountPrice = 3450.0,
                category = "Home & Living",
                categoryBn = "গৃহস্থালি সরঞ্জাম",
                brand = "Vision",
                sku = "VIS-INFRA-22",
                stockQuantity = 12,
                rating = 4.6f,
                reviewCount = 67,
                imageUrl = "home_cooker",
                sizesCsv = "2200 Watt Standard",
                colorsCsv = "Obsidian Black",
                isFeatured = false,
                isBestseller = true,
                isNewArrival = false
            ),
            ProductEntity(
                id = "prod_09",
                name = "Sundarbans Wild Pure Raw Honey 500g",
                nameBn = "সুন্দরবনের খাঁটি প্রাকৃতিক মধু ৫০০ গ্রাম",
                description = "Collected directly by traditional Mouwals from deep mangroves of Sundarbans. 100% unprocessed, natural floral nectar with no added sugar or chemicals.",
                descriptionBn = "সুন্দরবনের গভীর ম্যানগ্রোভ থেকে সরাসরি মৌয়ালদের সংগৃহীত খাঁটি মধু। কোনো রাসায়নিক বা চিনি মিশ্রণ ছাড়া সম্পূর্ণ প্রাকৃতিক ও পুষ্টিকর।",
                regularPrice = 750.0,
                discountPrice = 650.0,
                category = "Groceries",
                categoryBn = "মুদি ও নিত্যপণ্য",
                brand = "Khaas Food",
                sku = "HONEY-SUN-500",
                stockQuantity = 22,
                rating = 5.0f,
                reviewCount = 188,
                imageUrl = "grocery_honey",
                sizesCsv = "500g Glass Jar",
                colorsCsv = "Amber Amber",
                isFeatured = true,
                isBestseller = true,
                isNewArrival = false
            ),
            ProductEntity(
                id = "prod_10",
                name = "Apex Men's Genuine Leather Executive Shoes",
                nameBn = "এপেক্স পুরুষদের জেনুইন লেদার জুতো",
                description = "Handcrafted genuine leather formal shoes with cushioned memory foam insole and slip-resistant rubber outsole. Built for all-day comfort.",
                descriptionBn = "জেনুইন লেদারে তৈরি আকর্ষণীয় ও আরামদায়ক ফরমাল জুতো। সফট মেমোরি ফোম ইনসোল যা সারাদিন পায়ে কোনো ক্লান্তি আনে না।",
                regularPrice = 4590.0,
                discountPrice = 3990.0,
                category = "Men's Fashion",
                categoryBn = "পুরুষের পোশাক",
                brand = "Apex",
                sku = "APX-SHOE-M9",
                stockQuantity = 11,
                rating = 4.8f,
                reviewCount = 43,
                imageUrl = "fashion_shoes",
                sizesCsv = "39, 40, 41, 42, 43, 44",
                colorsCsv = "Deep Brown, Classic Black",
                isFeatured = false,
                isBestseller = false,
                isNewArrival = true
            ),
            ProductEntity(
                id = "prod_11",
                name = "Anker Soundcore Life P2i True Wireless Earbuds",
                nameBn = "অ্যাংকার সাউন্ডকোর পি২আই ব্লুটুথ ইয়ারবাডস",
                description = "10mm drivers with BassUp mode, AI-enhanced dual mics for crystal-clear phone calls, 28-hour playtime with fast charging case (10 min charge = 1 hr playtime).",
                descriptionBn = "১০ মিমি ড্রাইভার ও শক্তিশালী ব্যাস-আপ মোড। স্পষ্ট কথা বলার জন্য এআই নয়েজ রিডাকশন সমৃদ্ধ ডাবল মাইক্রোফোন। টানা ২৮ ঘণ্টার ব্যাটারি ব্যাকআপ।",
                regularPrice = 2850.0,
                discountPrice = 2490.0,
                category = "Electronics",
                categoryBn = "ইলেকট্রনিক্স",
                brand = "Anker",
                sku = "ANK-P2I-TWS",
                stockQuantity = 30,
                rating = 4.9f,
                reviewCount = 210,
                imageUrl = "electronics_earbuds",
                sizesCsv = "Standard with 3 Ear-tip Sizes",
                colorsCsv = "Matte Black, Cloud White",
                isFeatured = true,
                isBestseller = true,
                isNewArrival = false
            ),
            ProductEntity(
                id = "prod_12",
                name = "Himalaya Purifying Neem Face Wash 150ml",
                nameBn = "হিমালয়া পিউরিফাইং নিম ফেস ওয়াশ ১৫০ মিলি",
                description = "Soap-free herbal formulation with Neem and Turmeric that cleanses impurities, prevents acne, and leaves skin refreshed and glowing without drying.",
                descriptionBn = "নিম ও হলুদের প্রাকৃতিক ভেষজ উপাদানে তৈরি সাবান-মুক্ত ফেস ওয়াশ। ব্রণের সমস্যা দূর করে এবং ত্বককে সতেজ ও প্রাণবন্ত রাখে।",
                regularPrice = 320.0,
                discountPrice = 285.0,
                category = "Health & Beauty",
                categoryBn = "সৌন্দর্য ও স্বাস্থ্য",
                brand = "Himalaya",
                sku = "HIM-NEEM-150",
                stockQuantity = 60,
                rating = 4.8f,
                reviewCount = 175,
                imageUrl = "beauty_facewash",
                sizesCsv = "150ml Tube",
                colorsCsv = "Herbal Green",
                isFeatured = false,
                isBestseller = true,
                isNewArrival = false
            )
        )

        db.productDao().insertProducts(seedProducts)

        val seedCoupons = listOf(
            CouponEntity("AMAR20", 20, 300.0, 1000.0, "20% discount up to ৳300 on orders over ৳1000"),
            CouponEntity("EIDFREE", 100, 120.0, 500.0, "Free delivery on orders over ৳500"),
            CouponEntity("DESHI10", 10, 500.0, 1500.0, "10% off up to ৳500 on festive collections")
        )
        db.couponDao().insertCoupons(seedCoupons)

        // Seed sample reviews
        db.reviewDao().insertReview(
            ReviewEntity(
                productId = "prod_01",
                authorName = "Tanvir Ahmed (Dhanmondi, Dhaka)",
                rating = 5,
                comment = "Darun phone! Camera quality is superb, battery easily lasts full day with 67W fast charging. Delivery was super fast via AmarShop."
            )
        )
        db.reviewDao().insertReview(
            ReviewEntity(
                productId = "prod_02",
                authorName = "Kazi Shuvo (Chittagong)",
                rating = 5,
                comment = "Aarong er panjabi ta khub sundor o aramdayok. Eid er jonno perfect purchase."
            )
        )
        db.reviewDao().insertReview(
            ReviewEntity(
                productId = "prod_03",
                authorName = "Nusrat Jahan (Uttara, Dhaka)",
                rating = 5,
                comment = "Jamdani saree ti khub soft ebong abhijato. Rong ta ekdom chobi te jemon chilo temon."
            )
        )
    }

    companion object {
        // Bangladesh Geographic Reference Data
        val BD_DIVISIONS = listOf(
            "Dhaka", "Chattogram", "Sylhet", "Rajshahi", "Khulna", "Barishal", "Rangpur", "Mymensingh"
        )

        val BD_DISTRICTS_BY_DIVISION = mapOf(
            "Dhaka" to listOf("Dhaka", "Gazipur", "Narayanganj", "Tangail", "Kishoreganj", "Manikganj", "Munshiganj", "Narsingdi", "Faridpur", "Gopalganj", "Madaripur", "Rajbari", "Shariatpur"),
            "Chattogram" to listOf("Chattogram", "Cox's Bazar", "Cumilla", "Feni", "Brahmanbaria", "Noakhali", "Chandpur", "Lakshmipur", "Rangamati", "Khagrachhari", "Bandarban"),
            "Sylhet" to listOf("Sylhet", "Moulvibazar", "Habiganj", "Sunamganj"),
            "Rajshahi" to listOf("Rajshahi", "Bogura", "Pabna", "Sirajganj", "Naogaon", "Natore", "Chapainawabganj", "Joypurhat"),
            "Khulna" to listOf("Khulna", "Jashore", "Kushtia", "Satkhira", "Bagerhat", "Jhenaidah", "Chuadanga", "Magura", "Meherpur", "Narail"),
            "Barishal" to listOf("Barishal", "Patuakhali", "Bhola", "Pirojpur", "Barguna", "Jhalokati"),
            "Rangpur" to listOf("Rangpur", "Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari", "Panchagarh", "Thakurgaon"),
            "Mymensingh" to listOf("Mymensingh", "Jamalpur", "Netrokona", "Sherpur")
        )

        fun calculateDeliveryFee(district: String): Double {
            return if (district.equals("Dhaka", ignoreCase = true)) 60.0 else 120.0
        }

        fun isValidBangladeshPhone(phone: String): Boolean {
            val cleaned = phone.trim().replace("+88", "").replace(" ", "").replace("-", "")
            return cleaned.matches(Regex("^01[3-9]\\d{8}$"))
        }
    }
}
