package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexGoldGradient
import com.example.ui.theme.ApexLogoGradient
import com.example.ui.theme.ApexOrange
import com.example.ui.theme.ApexPremiumGradient
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.theme.ApexYellow

@Composable
fun AIUsageBanner(
    remainingUses: Int,
    isPremium: Boolean,
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isPremium) {
        // Premium User Badge Banner
        Box(
            modifier = modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF1F1338), Color(0xFF141934))
                    )
                )
                .border(1.dp, ApexPurple.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .testTag("ai_usage_banner_premium")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(ApexPremiumGradient, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Premium Active",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ApexCut Premium نشط 👑",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = ApexTextPrimary
                        )
                        Text(
                            text = "جميع أدوات الذكاء الاصطناعي وتصدير 4K مفتوحة بدون حدود",
                            style = MaterialTheme.typography.bodySmall,
                            color = ApexPurpleLight,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    } else {
        // Free User Shared 3-Use Counter Banner
        val maxQuota = 3
        val progress = (remainingUses.toFloat() / maxQuota.toFloat()).coerceIn(0f, 1f)
        val statusColor = when {
            remainingUses >= 2 -> ApexEmerald
            remainingUses == 1 -> ApexOrange
            else -> Color(0xFFEF4444)
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black)
                .clip(RoundedCornerShape(18.dp))
                .background(ApexDarkCard)
                .border(
                    width = 1.dp,
                    color = if (remainingUses == 0) Color(0xFFEF4444).copy(alpha = 0.6f) else ApexBorder,
                    shape = RoundedCornerShape(18.dp)
                )
                .animateContentSize()
                .padding(14.dp)
                .testTag("ai_usage_banner_free")
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(statusColor.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "متبقي لك $remainingUses من $maxQuota استخدامات AI اليوم",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = ApexTextPrimary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "العداد مشترك بين جميع أدوات الذكاء الاصطناعي (تجدد كل 24 ساعة)",
                                style = MaterialTheme.typography.bodySmall,
                                color = ApexTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Upgrade Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(ApexPremiumGradient)
                            .clickable(onClick = onUpgradeClick)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("ai_banner_upgrade_btn")
                    ) {
                        Text(
                            text = "ترقية 👑",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = statusColor,
                    trackColor = Color(0xFF22222A)
                )

                if (remainingUses == 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "استهلكت جميع استخدامات AI المجانية لليوم. اشترك بـ $3.99 لفتح كل الميزات بدون حدود!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFCA5A5),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
