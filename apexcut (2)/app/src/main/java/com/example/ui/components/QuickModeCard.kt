package com.example.ui.components

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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexPink
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun QuickModesGrid(
    onOpenVideoEditor: () -> Unit,
    onOpenPhotoEditor: () -> Unit,
    onOpenAIAudio: () -> Unit,
    onOpenAITools: () -> Unit,
    onOpenAIVideoGen: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "أقسام الاستوديو السريعة",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = ApexTextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (onOpenAIVideoGen != null) {
            QuickModeCardItem(
                title = "توليد الفيديوهات بالذكاء الاصطناعي ✨",
                subtitle = "تحويل النصوص والأفكار إلى فيديو سينمائي 4K بنقرة واحدة",
                badge = "جديد AI 3.0",
                icon = Icons.Default.AutoAwesome,
                gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFFEC4899), Color(0xFF06B6D4)),
                accentColor = ApexPink,
                onClick = onOpenAIVideoGen,
                tag = "quick_mode_video_gen",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Video Editor Card
            QuickModeCardItem(
                title = "تحرير الفيديو",
                subtitle = "قص، تأثيرات، 4K 60FPS",
                badge = "Pro Studio",
                icon = Icons.Default.MovieFilter,
                gradientColors = listOf(Color(0xFF7C3AED), Color(0xFF4F46E5)),
                accentColor = ApexPurple,
                onClick = onOpenVideoEditor,
                tag = "quick_mode_video",
                modifier = Modifier.weight(1f)
            )

            // Photo Editor Card
            QuickModeCardItem(
                title = "تحرير الصور",
                subtitle = "تلوين، رتوش، إزالة خلفية",
                badge = "Ultra RAW",
                icon = Icons.Default.PhotoCamera,
                gradientColors = listOf(Color(0xFF2563EB), Color(0xFF0284C7)),
                accentColor = ApexBlue,
                onClick = onOpenPhotoEditor,
                tag = "quick_mode_photo",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // AI Audio Card
            QuickModeCardItem(
                title = "الصوت بالذكاء الاصطناعي",
                subtitle = "عزل، تعليق عربي، إيقاع",
                badge = "AI Audio 2.0",
                icon = Icons.Default.GraphicEq,
                gradientColors = listOf(Color(0xFF0D9488), Color(0xFF0284C7)),
                accentColor = ApexCyan,
                onClick = onOpenAIAudio,
                tag = "quick_mode_audio",
                modifier = Modifier.weight(1f)
            )

            // AI Tools Card
            QuickModeCardItem(
                title = "أدوات AI الشاملة",
                subtitle = "ممحاة سحرية، تفريغ نصوص",
                badge = "8 أدوات ذكية",
                icon = Icons.Default.AutoAwesome,
                gradientColors = listOf(Color(0xFFDB2777), Color(0xFF9333EA)),
                accentColor = ApexPink,
                onClick = onOpenAITools,
                tag = "quick_mode_ai_tools",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickModeCardItem(
    title: String,
    subtitle: String,
    badge: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    accentColor: Color,
    onClick: () -> Unit,
    tag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.5f), spotColor = accentColor.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(22.dp))
            .background(ApexDarkCard)
            .border(1.dp, ApexBorder, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag(tag)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = accentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .border(0.5.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        color = accentColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = ApexTextPrimary
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = ApexTextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}
