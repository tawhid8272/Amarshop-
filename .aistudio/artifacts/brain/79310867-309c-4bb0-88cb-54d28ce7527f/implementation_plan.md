# AmarShop - Bangladesh Multi-Category E-Commerce Platform

AmarShop is a complete, production-grade Android shopping platform and merchant management app tailored specifically for Bangladesh, inspired by Daraz, Amazon, and Alibaba. It delivers a rich consumer shopping journey (bilingual English/Bengali, Bangladeshi Taka ৳, district/upazila address selection, bKash/Nagad/COD checkout, order tracking, wishlist, reviews) seamlessly integrated with a powerful Admin Management Panel.

### User Review & Critical Decisions

> [!IMPORTANT]
> The plan unifies all customer shopping and merchant administration requirements into a coherent, high-performance Jetpack Compose application. Please review the confirmed choices below before building commences.

- **Confirmed Decision 1: Brand & Market Identity**: Named **AmarShop** with an authentic Bangladeshi marketplace aesthetic (Sunset Orange `#FF5722`, Heritage Teal `#00897B`, Warm Surface `#FAF9F6`), native Bengali (`বাংলা`) and English bilingual toggle, and Bangladeshi Taka (`৳`) currency formatting throughout.
- **Confirmed Decision 2: Dual Mode (Storefront & Admin Control)**: A seamless, accessible role switch allowing testing and operation of both the high-converting consumer storefront (16 core pages/flows) and the merchant Admin Dashboard (KPI metrics, product CRUD, inventory adjustments, order status updates, and revenue tracking).
- **Confirmed Decision 3: Localized Bangladeshi Payment & Delivery**: Checkout engine with real delivery calculations (Inside Dhaka ৳60 vs. Outside Dhaka ৳120 across all 64 districts), strict 11-digit Bangladeshi mobile number validation (`01XXXXXXXXX`), voucher discounts, and realistic payment verification for Cash on Delivery, bKash (with TrxID verification simulation), and Nagad.
- **Confirmed Decision 4: Room Local Persistence with Rich Seed Catalog**: Robust device-local Room database seeded with 20+ authentic products across Electronics, Fashion, Grocery, Home & Living, and Health & Beauty, ensuring persistence of products, orders, cart, wishlist, and reviews across app sessions.

---

### 1. Overview & Core Concept

- **What It Does**: AmarShop gives Bangladeshi shoppers an intuitive mobile marketplace to discover products, filter by category/brand/price/rating, inspect multi-image carousels and variants (size, color), manage their cart with coupons, and place orders with localized payment methods. Merchants and admins can monitor sales analytics, restock items, add new products, and advance order statuses through delivery.
- **Target Audience / Persona**: Everyday Bangladeshi consumers on Android devices looking for affordable, reliable shopping with familiar payment methods (bKash/Nagad/COD); local online store owners requiring mobile management of their catalog, inventory, and fulfillment pipeline.
- **Key Value**: Delivers the responsiveness and offline-first reliability of modern Android Jetpack Compose while honoring local commercial standards (Bangladeshi Taka, dual language, 64 districts, and local mobile financial services).

---

### 2. User Experience & Visual Design

#### Key User Flows
1. **Discovery & Browsing**:
   - Hero banner with promotional sliders (flash deals, Ramadan/Eid specials, free shipping vouchers).
   - Category grid (Electronics, Men's Fashion, Women's Fashion, Groceries, Home Appliances, Beauty).
   - Live Search bar with instant suggestions, search results screen, and multi-parameter filters (Price slider, Brand chips, Minimum rating, In-stock toggle, Sort by Price/Popularity).
2. **Product Details & Customization**:
   - Multi-image carousel with zoom preview.
   - Dynamic pricing with regular price, discounted price, and percent-off discount badge.
   - Variant pickers: Color swatches, Size chips (S, M, L, XL), quantity stepper.
   - Stock status indicators (e.g., "In Stock - 14 left", "Low Stock", or "Out of Stock").
   - Customer ratings breakdown with user reviews and review submission dialog.
   - Related products and recently viewed items rail.
3. **Cart & Localized Checkout**:
   - Sticky cart summary with subtotal, voucher discount, and delivery fee calculation.
   - Coupon code entry (e.g., `AMAR20`, `EIDFREE`) with instant discount deduction.
   - Address form pre-populated with Bangladesh Division, 64 Districts, Thana/Upazila selector, and validated 11-digit mobile number.
   - Payment selection: **Cash on Delivery (COD)**, **bKash** (with merchant wallet prompt and Transaction ID input), and **Nagad**.
   - Confirmation step generating a unique formatted Order ID (e.g., `BD-847291`).
4. **Order Tracking & Customer Account**:
   - "My Orders" tab with status badge pills (Pending, Confirmed, Processing, Shipped, Delivered, Cancelled).
   - Visual timeline stepper showing progress from warehouse packing to courier handover and final delivery.
   - Wishlist toggle on all product cards with dedicated Wishlist screen.
   - Profile management with saved delivery addresses and bilingual language switcher (English / বাংলা).
   - Dedicated informational pages: About Us, Contact Us, FAQ, Privacy Policy, Delivery & Refund Policy.
5. **Admin Management Suite**:
   - Executive dashboard cards: Total Gross Sales (৳), Total Orders, Active Customers, Low Stock Alerts.
   - Product Catalog Manager: Add new product with image URLs, set category, regular/sale price, stock count, and variants; edit or delete existing products.
   - Order Dispatch Console: Filter orders by status, inspect customer delivery address and phone number, and advance order state with 1 tap.
   - Sales Reports & CSV summary exporter preview.

#### Visual Identity & Theme
- **Aesthetic Direction**: Modern Bengali commerce—warm, dependable, and vibrant.
- **Color Palette**:
  - Primary: `Sunset Orange` (`#FF5722`) – action-oriented, vibrant, classic e-commerce energy.
  - Secondary: `Heritage Teal` (`#00897B`) – trust, financial security for payments, high contrast.
  - Tertiary / Accent: `Golden Amber` (`#FFA000`) – flash deals, star ratings, discount tags.
  - Background: `Warm Cream Surface` (`#FAF9F6`) in light mode; `Deep Charcoal` (`#121316`) in dark mode.
  - Card & Container: `#FFFFFF` with soft 1.dp tonal borders and subtle elevation (`2.dp`).
- **Typography & Hierarchy**:
  - Display & Headings: Bold modern typography with distinct styling for product names and currency amounts.
  - Body & Labels: Medium/Regular weight optimized for crisp rendering of English text and Bengali unicode glyphs (`বাংলা`).
- **Component Styling & Layout**:
  - Material 3 Navigation Bar with badge counters for Cart and Wishlist.
  - 16.dp rounded corners for product cards; 24.dp pill buttons for primary actions.
  - Floating Back-to-Top and Sticky Bottom Bar on Product Details (Add to Cart + Buy Now).

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Dual-Perspective Architecture (Customer Storefront + Admin Console)**
  - *Chosen Approach*: Single cohesive app with an easy top-bar / profile switch between "Customer View" and "Admin Console".
  - *Why*: Allows the user, testers, and stakeholders to immediately evaluate both the front-facing customer shopping experience and the back-office inventory/order fulfillment logic in the same emulator session without needing separate APKs.
  - *Alternatives Considered*: Hiding admin behind an obscure PIN or building two separate apps, which would make testing cumbersome.
- **Decision 2: Local Persistence with Android Room & Pre-populated Seed Data**
  - *Chosen Approach*: Use Android Room Database with KSP (`AppDatabase`, `ProductDao`, `OrderDao`, `CartDao`, `WishlistDao`, `ReviewDao`) pre-seeded with 20+ realistic Bangladeshi retail items.
  - *Why*: Ensures instant zero-latency loading, complete offline capability, and guarantees that additions to the cart, placed orders, or admin inventory updates survive app restarts.
  - *Alternatives Considered*: Cloud-only Firebase without local cache, which would require external network requests and manual authentication setups before experiencing the app.
- **Decision 3: Authentic Bangladesh Commerce Features**
  - *Chosen Approach*: Implement full BD geography (Districts & Upazilas), realistic delivery fees (Inside Dhaka ৳60 / Outside ৳120), valid 11-digit mobile formatting (`017...`, `018...`, `019...`, `013...`), and authentic payment interfaces for bKash, Nagad, and Cash on Delivery.
  - *Why*: Directly satisfies the target market requirement and makes AmarShop look and behave like a real Bangladeshi commercial application.

---

### 4. Technical Architecture & Data Strategy *(Technical Reference)*

#### Architecture & Component Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                              MainActivity                              │
│         (Edge-to-Edge, AmarShopTheme, TopAppBar with Mode Switch)      │
└────────────────────────────────────┬───────────────────────────────────┘
                                     │
           ┌─────────────────────────┴─────────────────────────┐
           ▼                                                   ▼
┌────────────────────────────────────┐       ┌───────────────────────────────────┐
│     Customer Storefront Flow       │       │       Admin Management Suite      │
├────────────────────────────────────┤       ├───────────────────────────────────┤
│ • HomeScreen (Banner, Flash Deals) │       │ • DashboardOverview (Sales, KPIs) │
│ • CatalogScreen (Filters, Sort)    │       │ • ProductManager (Add/Edit/Stock) │
│ • ProductDetail (Variants, Review) │       │ • OrderManager (Status Dispatch)  │
│ • CartScreen (Coupons, Summary)    │       │ • SalesReportsScreen (Analytics)  │
│ • CheckoutScreen (Districts, MFS)  │       │ • BannerCouponManager             │
│ • OrderTrackingScreen (Timeline)   │       └─────────────────┬─────────────────┘
│ • WishlistScreen & AccountScreen   │                         │
│ • InfoPages (About, FAQ, Policies) │                         │
└──────────────────┬─────────────────┘                         │
                   │                                           │
                   └─────────────────────┬─────────────────────┘
                                         ▼
                      ┌───────────────────────────────────────┐
                      │          AmarShopViewModel            │
                      │  (UI State, Language, Search, Filter) │
                      └──────────────────┬────────────────────┘
                                         ▼
                      ┌───────────────────────────────────────┐
                      │          AmarShopRepository           │
                      │ (Business Rules, Currency, Validation)│
                      └──────────────────┬────────────────────┘
                                         ▼
                      ┌───────────────────────────────────────┐
                      │             Room Database             │
                      │ ┌───────────────┬───────────────────┐ │
                      │ │ ProductDao    │ OrderDao          │ │
                      │ │ CartDao       │ WishlistDao       │ │
                      │ │ ReviewDao     │ SettingDao        │ │
                      │ └───────────────┴───────────────────┘ │
                      └───────────────────────────────────────┘
```

#### Core Data Entities
1. **ProductEntity**: `id`, `name`, `nameBn`, `description`, `descriptionBn`, `price`, `discountPrice`, `category`, `brand`, `sku`, `stockQuantity`, `rating`, `reviewCount`, `imageUrlsJson`, `sizesJson`, `colorsJson`, `isFeatured`, `isBestseller`.
2. **CartItemEntity**: `id`, `productId`, `productName`, `productNameBn`, `price`, `selectedSize`, `selectedColor`, `quantity`, `imageUrl`.
3. **OrderEntity**: `orderId`, `customerName`, `customerPhone`, `division`, `district`, `upazila`, `fullAddress`, `orderNotes`, `paymentMethod`, `paymentTrxId`, `subtotal`, `deliveryFee`, `discount`, `totalAmount`, `status` (PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED), `createdAtMillis`, `itemsJson`.
4. **WishlistItemEntity**: `id`, `productId`, `addedAtMillis`.
5. **ReviewEntity**: `id`, `productId`, `authorName`, `rating`, `comment`, `createdAtMillis`.
6. **CouponEntity**: `code`, `discountPercentage`, `maxDiscount`, `minOrderAmount`, `isActive`.

#### Interactive Component & State Mapping
- **Search & Filter State**: Live search query with debouncing; reactive category chips, price range slider, and sort order dropdown (`PRICE_LOW_HIGH`, `PRICE_HIGH_LOW`, `TOP_RATED`, `NEWEST`).
- **Cart & Voucher State**: Reactive quantity updates, atomic deletion, coupon code verification applying instant calculation to `subtotal + deliveryFee - discount`.
- **Checkout State Machine**:
  1. Address Entry (Customer name, mobile validation `^01[3-9]\d{8}$`, division, district, upazila, street address).
  2. Delivery Calculation (Auto-sets ৳60 if Dhaka, ৳120 for other 63 districts).
  3. Payment Mode (COD selected -> instant checkout; bKash/Nagad selected -> opens MFS PIN / Transaction ID dialog).
  4. Success State -> Clears cart, records `OrderEntity` with timestamp, routes to Order Tracking.
- **Admin State Dispatch**: Admin can change order status via dropdown or swipe action; updates propagate reactively to customer's "My Orders" screen.

---

### Step-by-Step Implementation Sequence (Post-Approval)

1. **Step 1: Resource Setup & Localization Strings**:
   - Update `app/src/main/res/values/strings.xml` with app title `AmarShop`, bilingual labels, district names, and categories.
   - Synchronize `metadata.json` with `AmarShop`.
2. **Step 2: Room Database & Data Layer**:
   - Create Room Entities, DAOs, and `AppDatabase`.
   - Implement `AmarShopRepository` with rich Bangladeshi seed catalog (smartphones, sarees, panjabis, spices, darjeeling tea, smart watches, groceries).
3. **Step 3: State Management & ViewModel**:
   - Create `AmarShopViewModel` managing active screen routing, search, active filters, cart operations, language toggle (`en` / `bn`), checkout, and admin state.
4. **Step 4: M3 Theme & Common Components**:
   - Update `Color.kt`, `Theme.kt`, and `Type.kt` with AmarShop's vibrant orange and teal aesthetic.
   - Build reusable components: `ProductCard`, `PriceTag`, `RatingStars`, `BannerCarousel`, `LanguageSwitcher`, `FilterSheet`.
5. **Step 5: Customer Screens**:
   - Build `HomeScreen`, `CatalogScreen`, `ProductDetailScreen`, `CartScreen`, `CheckoutScreen`, `OrderTrackingScreen`, `WishlistScreen`, `ProfileScreen`, and Policy pages (`AboutUs`, `FAQ`, `PolicyView`).
6. **Step 6: Admin Dashboard Screens**:
   - Build `AdminDashboardScreen` with KPI analytics cards, `AdminProductManagerScreen` (Add/Edit product dialog), and `AdminOrderManagerScreen` (Order status updater).
7. **Step 7: Verification**:
   - Compile the Android application using `compile_applet` and verify zero errors, seamless navigation, and smooth user flow.
