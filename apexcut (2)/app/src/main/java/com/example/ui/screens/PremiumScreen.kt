package com.example.ui.screens

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexBorderGlow
import com.example.ui.theme.ApexCrimson
import com.example.ui.theme.ApexCrimsonPink
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexLogoBlue
import com.example.ui.theme.ApexLogoGradient
import com.example.ui.theme.ApexLogoRed
import com.example.ui.theme.ApexOrange
import com.example.ui.theme.ApexPremiumGradient
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleDark
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.theme.ApexYellow
import com.example.ui.viewmodel.ApexCutViewModel

@Composable
fun PremiumScreen(
    viewModel: ApexCutViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val isBillingLoading by viewModel.isBillingLoading.collectAsStateWithLifecycle()
    val billingMessage by viewModel.billingMessage.collectAsStateWithLifecycle()
    val formattedPrice by viewModel.premiumPrice.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(billingMessage) {
        billingMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearBillingMessage()
        }
    }

    val features = listOf(
        PremiumFeatureItem(
            title = "جميع أدوات AI بدون حدود",
            subtitle = "استخدام غير مقيد لجميع أدوات الذكاء الاصطناعي الحالية والمستقبلية",
            icon = Icons.Default.AutoAwesome,
            accentColor = ApexPurple
        ),
        PremiumFeatureItem(
            title = "تحسين الفيديو والصور بالذكاء الاصطناعي",
            subtitle = "ترقية الدقة لـ 4K ومضاعفة الإطارات لـ 60FPS وإزالة الضوضاء",
            icon = Icons.Default.HighQuality,
            accentColor = ApexCyan
        ),
        PremiumFeatureItem(
            title = "AI Voice بدون حدود",
            subtitle = "توليد أصوات عربية احترافية واقعية ومؤثرات صوتية استوديو",
            icon = Icons.Default.GraphicEq,
            accentColor = ApexCrimsonPink
        ),
        PremiumFeatureItem(
            title = "AI Subtitles",
            subtitle = "تفريغ صوتي تلقائي ذكي فائق الدقة مع مزامنة زمنية",
            icon = Icons.Default.Subtitles,
            accentColor = ApexYellow
        ),
        PremiumFeatureItem(
            title = "AI Captions",
            subtitle = "إنشاء نصوص كابشن متحركة وجذابة لمنصات التواصل",
            icon = Icons.Default.AutoAwesome,
            accentColor = ApexOrange
        ),
        PremiumFeatureItem(
            title = "تصدير 4K",
            subtitle = "تصدير فائق الوضوح 4K Ultra HD بدون علامة مائية وبأعلى معدل بت",
            icon = Icons.Default.VideoFile,
            accentColor = ApexEmerald
        ),
        PremiumFeatureItem(
            title = "جميع الميزات المدفوعة مفتوحة",
            subtitle = "وصول كامل وشامل لجميع قوالب وفلاتر ومؤثرات ApexCut",
            icon = Icons.Default.LockOpen,
            accentColor = ApexBlue
        )
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ApexDarkBackground),
        containerColor = ApexDarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFF1E1E24), CircleShape)
                        .border(1.dp, ApexBorder, CircleShape)
                        .testTag("premium_close_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = ApexTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "ApexCut Premium",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ApexTextPrimary
                )

                Text(
                    text = "استعادة",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ApexPurpleLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.restorePurchases() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("premium_restore_btn")
                )
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = Color(0xFF121216),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222228))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isPremium) {
                        // Already Premium
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ApexEmerald.copy(alpha = 0.2f))
                                .border(1.5.dp, ApexEmerald, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = ApexEmerald)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "أنت مشترك في ApexCut Premium بالفعل 👑",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        // Subscribe Button
                        Button(
                            onClick = {
                                val activity = context as? Activity
                                if (activity != null) {
                                    viewModel.subscribeToPremium(activity)
                                }
                            },
                            enabled = !isBillingLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(12.dp, RoundedCornerShape(16.dp), ambientColor = ApexPurple, spotColor = ApexCrimson)
                                .testTag("premium_subscribe_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .background(ApexPremiumGradient, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isBillingLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.WorkspacePremium,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "اشترك الآن • $formattedPrice",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "دفع آمن ومحمي عبر Google Play • إلغاء في أي وقت من متجر Play",
                            style = MaterialTheme.typography.bodySmall,
                            color = ApexTextMuted,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Official ApexCut Logo Showcase
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .shadow(16.dp, RoundedCornerShape(26.dp), ambientColor = ApexLogoBlue, spotColor = ApexLogoRed)
                    .clip(RoundedCornerShape(26.dp))
                    .border(
                        width = 2.dp,
                        brush = ApexLogoGradient,
                        shape = RoundedCornerShape(26.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_apexcut_logo),
                    contentDescription = "ApexCut Official Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Catchy Title & Subtitle
            Text(
                text = "افتح قوة ApexCut بالكامل",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                color = ApexTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "تجربة مونتاج احترافية متكاملة مدعومة بأحدث تقنيات الذكاء الاصطناعي وبدون أي حدود",
                style = MaterialTheme.typography.bodyMedium,
                color = ApexTextSecondary,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Price Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(20.dp), ambientColor = Color.Black)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF22173B), Color(0xFF141324))
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(ApexPurple, ApexCrimsonPink)),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "وصول كامل وغير محدود",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = ApexTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(ApexCrimson, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "العرض الأفضل",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "جميع أدوات الذكاء الاصطناعي وتصدير 4K بلا قيود",
                            style = MaterialTheme.typography.bodySmall,
                            color = ApexTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = formattedPrice,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            color = ApexPurpleLight
                        )
                        Text(
                            text = "وصول كامل",
                            style = MaterialTheme.typography.bodySmall,
                            color = ApexTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section Label
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ما الذي ستحصل عليه مع Premium:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = ApexTextPrimary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Features Checklist
            features.forEach { feat ->
                FeatureRowItem(feat)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private data class PremiumFeatureItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
private fun FeatureRowItem(item: PremiumFeatureItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ApexDarkCard)
            .border(1.dp, ApexBorder, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(item.accentColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = ApexTextPrimary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = ApexTextSecondary,
                    fontSize = 10.5.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(ApexEmerald.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = ApexEmerald,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
