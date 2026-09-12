package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AIUsageManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _remainingUses = MutableStateFlow(MAX_FREE_DAILY_USES)
    val remainingUses: StateFlow<Int> = _remainingUses.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    init {
        _isPremium.value = prefs.getBoolean(KEY_IS_PREMIUM, false)
        refreshQuota()
    }

    /**
     * Refreshes and returns the remaining uses in the current 24-hour cycle.
     */
    fun refreshQuota(): Int {
        if (_isPremium.value) {
            _remainingUses.value = UNLIMITED_USES
            return UNLIMITED_USES
        }

        val now = System.currentTimeMillis()
        val windowStart = prefs.getLong(KEY_WINDOW_START, 0L)
        val usedCount = prefs.getInt(KEY_USED_COUNT, 0)

        val isExpired = windowStart == 0L || (now - windowStart) >= CYCLE_DURATION_MS

        val remaining = if (isExpired) {
            // Reset quota for new 24h cycle
            prefs.edit()
                .putLong(KEY_WINDOW_START, now)
                .putInt(KEY_USED_COUNT, 0)
                .apply()
            MAX_FREE_DAILY_USES
        } else {
            (MAX_FREE_DAILY_USES - usedCount).coerceAtLeast(0)
        }

        _remainingUses.value = remaining
        return remaining
    }

    /**
     * Checks whether the user can perform an AI action.
     */
    fun canUseAI(): Boolean {
        if (_isPremium.value) return true
        return refreshQuota() > 0
    }

    /**
     * Consumes 1 AI use from the shared unified counter.
     * Returns true if successfully consumed or unlimited, false if depleted.
     */
    fun consumeAIUse(): Boolean {
        if (_isPremium.value) return true

        val remaining = refreshQuota()
        if (remaining <= 0) {
            return false
        }

        val now = System.currentTimeMillis()
        var windowStart = prefs.getLong(KEY_WINDOW_START, 0L)
        if (windowStart == 0L) {
            windowStart = now
        }

        val currentUsed = prefs.getInt(KEY_USED_COUNT, 0)
        val newUsed = currentUsed + 1

        prefs.edit()
            .putLong(KEY_WINDOW_START, windowStart)
            .putInt(KEY_USED_COUNT, newUsed)
            .apply()

        val newRemaining = (MAX_FREE_DAILY_USES - newUsed).coerceAtLeast(0)
        _remainingUses.value = newRemaining
        return true
    }

    /**
     * Sets the user's verified Premium status.
     */
    fun setPremium(premium: Boolean) {
        _isPremium.value = premium
        prefs.edit().putBoolean(KEY_IS_PREMIUM, premium).apply()
        if (premium) {
            _remainingUses.value = UNLIMITED_USES
        } else {
            refreshQuota()
        }
    }

    /**
     * Gets milliseconds remaining until the 24-hour cycle resets.
     */
    fun getMillisUntilReset(): Long {
        val windowStart = prefs.getLong(KEY_WINDOW_START, 0L)
        if (windowStart == 0L) return 0L
        val elapsed = System.currentTimeMillis() - windowStart
        return (CYCLE_DURATION_MS - elapsed).coerceAtLeast(0L)
    }

    companion object {
        const val MAX_FREE_DAILY_USES = 3
        const val UNLIMITED_USES = 999999
        private const val CYCLE_DURATION_MS = 24 * 60 * 60 * 1000L // 24 hours
        private const val PREFS_NAME = "apexcut_ai_usage_prefs"
        private const val KEY_USED_COUNT = "key_ai_used_count"
        private const val KEY_WINDOW_START = "key_ai_window_start"
        private const val KEY_IS_PREMIUM = "key_is_premium"

        @Volatile
        private var INSTANCE: AIUsageManager? = null

        fun getInstance(context: Context): AIUsageManager {
            return INSTANCE ?: synchronized(this) {
                val instance = AIUsageManager(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
