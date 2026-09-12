package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexBorderGlow
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleDark
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun ExportDialog(
    isOpen: Boolean,
    isExporting: Boolean,
    progress: Float,
    isSuccess: Boolean,
    isPremium: Boolean,
    selectedResolution: String,
    selectedFps: String,
    onSelectResolution: (String) -> Unit,
    onSelectFps: (String) -> Unit,
    onStartExport: () -> Unit,
    onUpgradeToPremium: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val resolutionOptions = listOf(
        ExportResolutionItem("480p SD (854x480)", isPremiumOnly = false, label = "مجاني"),
        ExportResolutionItem("720p HD (1280x720)", isPremiumOnly = false, label = "مجاني"),
        ExportResolutionItem("1080p FHD (1920x1080)", isPremiumOnly = false, label = "مجاني"),
        ExportResolutionItem("2K QHD (2560x1440)", isPremiumOnly = false, label = "مجاني"),
        ExportResolutionItem("4K Ultra HD (3840x2160)", isPremiumOnly = true, label = "🔒 Premium")
    )
    val fpsOptions = listOf("60 FPS (سلاسة فائقة)", "30 FPS (قياسي)", "24 FPS (سينمائي)")

    Dialog(onDismissRequest = { if (!isExporting) onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(24.dp, RoundedCornerShape(24.dp), ambientColor = ApexPurple, spotColor = ApexBlue)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    1.5.dp,
                    Brush.linearGradient(listOf(ApexPurple, ApexCyan)),
                    RoundedCornerShape(24.dp)
                ),
            color = Color(0xFF131126)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Brush.linearGradient(listOf(ApexPurple, ApexBlue)),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "تصدير الفيديو",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ApexTextPrimary
                            )
                            Text(
                                text = if (isPremium) "عضوية Premium: جودة 4K مفتوحة" else "480p إلى 2K مجاناً • 4K يتطلب Premium",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isPremium) ApexPurpleLight else ApexCyan,
                                fontSize = 10.sp
                            )
                        }
                    }

                    if (!isExporting) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = ApexTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isSuccess) {
                    // Success View
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(ApexEmerald.copy(alpha = 0.2f), CircleShape)
                                .border(2.dp, ApexEmerald, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ApexEmerald,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "تم التصدير بنجاح وبأعلى جودة!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ApexTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "تم حفظ الفيديو بدقة $selectedResolution بمعدل $selectedFps في معرض جهازك بدون علامة مائية",
                            style = MaterialTheme.typography.bodySmall,
                            color = ApexTextSecondary,
                            fontSize = 11.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("export_done_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ApexEmerald)
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تم ومتابعة المونتاج", fontWeight = FontWeight.Bold)
                        }
                    }
                } else if (isExporting) {
                    // Progress Rendering View
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "جاري الرندر والمعالجة الذكية...",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ApexTextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${(progress * 100).toInt()}% مكتمل",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            color = ApexPurpleLight
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = ApexCyan,
                            trackColor = Color(0xFF221F46),
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF191734), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Memory, contentDescription = null, tint = ApexCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("تسريع الأجهزة Hardware Accel", color = ApexTextSecondary, fontSize = 10.sp)
                            }
                            Text("نشط 100%", color = ApexEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Export Settings Config View
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "دقة الفيديو (Resolution):",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = ApexTextPrimary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        resolutionOptions.forEach { opt ->
                            val isSel = selectedResolution == opt.name
                            val isLocked = opt.isPremiumOnly && !isPremium

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isLocked -> Color(0xFF181524)
                                            isSel -> Color(0xFF262052)
                                            else -> Color(0xFF16142A)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isLocked -> Color(0xFF332A44)
                                            isSel -> ApexPurpleLight
                                            else -> ApexBorder
                                        },
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        if (isLocked) {
                                            onUpgradeToPremium()
                                        } else {
                                            onSelectResolution(opt.name)
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = opt.name,
                                        color = if (isLocked) ApexTextMuted else if (isSel) Color.White else ApexTextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isLocked) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFEF4444).copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                                .border(0.5.dp, Color(0xFFEF4444), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "🔒 $3.99 Premium",
                                                color = Color(0xFFFCA5A5),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        if (opt.isPremiumOnly) {
                                            Text(
                                                text = "👑 VIP",
                                                color = ApexPurpleLight,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                        }
                                        if (isSel) {
                                            Icon(Icons.Default.Done, contentDescription = null, tint = ApexCyan, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "معدل الإطارات (Frame Rate):",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = ApexTextPrimary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        fpsOptions.forEach { fps ->
                            val isSel = selectedFps == fps
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) Color(0xFF262052) else Color(0xFF16142A))
                                    .border(1.dp, if (isSel) ApexPurpleLight else ApexBorder, RoundedCornerShape(10.dp))
                                    .clickable { onSelectFps(fps) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = fps,
                                    color = if (isSel) Color.White else ApexTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSel) {
                                    Icon(Icons.Default.Done, contentDescription = null, tint = ApexCyan, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Export Button
                        Button(
                            onClick = {
                                if (selectedResolution.startsWith("4K") && !isPremium) {
                                    onUpgradeToPremium()
                                } else {
                                    onStartExport()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shadow(10.dp, RoundedCornerShape(14.dp), ambientColor = ApexPurple, spotColor = ApexBlue)
                                .testTag("start_export_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(
                                        Brush.horizontalGradient(listOf(ApexPurpleDark, ApexPurple, ApexBlue)),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (selectedResolution.startsWith("4K") && !isPremium) "تصدير 4K (يتطلب Premium)" else "بدء التصدير السريع الآن",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class ExportResolutionItem(
    val name: String,
    val isPremiumOnly: Boolean,
    val label: String
)
