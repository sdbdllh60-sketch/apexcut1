package com.example.data.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.example.data.local.AIUsageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(
    private val context: Context,
    private val aiUsageManager: AIUsageManager,
    private val scope: CoroutineScope
) : PurchasesUpdatedListener {

    private var billingClient: BillingClient? = null

    private val _isPremium = MutableStateFlow(aiUsageManager.isPremium.value)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails: StateFlow<ProductDetails?> = _productDetails.asStateFlow()

    private val _formattedPrice = MutableStateFlow("$3.99")
    val formattedPrice: StateFlow<String> = _formattedPrice.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _purchaseMessage = MutableStateFlow<String?>(null)
    val purchaseMessage: StateFlow<String?> = _purchaseMessage.asStateFlow()

    init {
        setupBillingClient()
    }

    private fun setupBillingClient() {
        val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
            .enableOneTimeProducts()
            .build()

        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases(pendingPurchasesParams)
            .build()

        startConnection()
    }

    fun startConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "BillingClient connected successfully")
                    queryAvailableProducts()
                    queryExistingPurchases()
                } else {
                    Log.w(TAG, "Billing connection failed code: ${billingResult.responseCode}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "Billing service disconnected, retrying...")
            }
        })
    }

    private fun queryAvailableProducts() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PREMIUM_PRODUCT_ID)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val premiumProduct = productDetailsList.firstOrNull { it.productId == PREMIUM_PRODUCT_ID }
                _productDetails.value = premiumProduct
                premiumProduct?.oneTimePurchaseOfferDetails?.formattedPrice?.let { price ->
                    _formattedPrice.value = price
                }
            }
        }
    }

    fun queryExistingPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient?.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(purchases)
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        _isLoading.value = false
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
            _purchaseMessage.value = "تم تفعيل اشتراك ApexCut Premium بنجاح! جميع الميزات مفتوحة الآن."
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            _purchaseMessage.value = "تم إلغاء عملية الشراء."
        } else {
            _purchaseMessage.value = "تعذر إتمام عملية الشراء (رمز: ${billingResult.responseCode})."
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        var hasValidPremium = false

        for (purchase in purchases) {
            if (purchase.products.contains(PREMIUM_PRODUCT_ID) &&
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            ) {
                hasValidPremium = true
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
            }
        }

        if (hasValidPremium) {
            _isPremium.value = true
            aiUsageManager.setPremium(true)
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        scope.launch(Dispatchers.IO) {
            billingClient?.acknowledgePurchase(acknowledgeParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Purchase acknowledged successfully: ${purchase.orderId}")
                }
            }
        }
    }

    fun launchBillingFlow(activity: Activity): Boolean {
        val details = _productDetails.value
        val client = billingClient

        if (client == null || !client.isReady) {
            startConnection()
            _purchaseMessage.value = "جاري الاتصال بخدمات Google Play..."
            return false
        }

        if (details != null) {
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(details)
                    .build()
            )

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

            _isLoading.value = true
            val billingResult = client.launchBillingFlow(activity, billingFlowParams)
            return billingResult.responseCode == BillingClient.BillingResponseCode.OK
        } else {
            // If product details not yet loaded from store, trigger query and notify
            queryAvailableProducts()
            _purchaseMessage.value = "جاري جلب تفاصيل المنتج من متجر Google Play ($3.99)..."
            return false
        }
    }

    fun restorePurchases() {
        _isLoading.value = true
        queryExistingPurchases()
        scope.launch {
            kotlinx.coroutines.delay(1000)
            _isLoading.value = false
            if (_isPremium.value) {
                _purchaseMessage.value = "تم استعادة اشتراك ApexCut Premium بنجاح!"
            } else {
                _purchaseMessage.value = "لم يتم العثور على مشتريات سابقة مرتبطة بهذا الحساب."
            }
        }
    }

    fun clearMessage() {
        _purchaseMessage.value = null
    }

    companion object {
        const val PREMIUM_PRODUCT_ID = "apexcut_premium_399"
        private const val TAG = "ApexCutBilling"
    }
}
