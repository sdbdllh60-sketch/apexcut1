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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CropLandscape
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexCyanLight
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexIndigo
import com.example.ui.theme.ApexPink
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleDark
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun NewProjectBanner(
    onCreateProject: (title: String, type: String, ratio: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRatio by remember { mutableStateOf("9:16") }
    var selectedType by remember { mutableStateOf("video") }

    val ratios = listOf(
        Triple("9:16", "ريلز / تيك توك", Icons.Default.CropPortrait),
        Triple("16:9", "يوتيوب سينمائي", Icons.Default.CropLandscape),
        Triple("1:1", "إنستغرام مربع", Icons.Default.CropSquare)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(26.dp), ambientColor = ApexPurple.copy(alpha = 0.2f), spotColor = ApexBlue.copy(alpha = 0.2f))
            .clip(RoundedCornerShape(26.dp))
            .background(ApexDarkCard)
            .border(
                width = 1.dp,
                color = ApexBorder,
                shape = RoundedCornerShape(26.dp)
            )
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(ApexPurple, ApexBlue)),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "مشروع جديد",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = ApexTextPrimary
                        )
                        Text(
                            text = "ابدأ بصنع سحرك الخاص وبجودة 4K",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ApexTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Type Toggle (Video / Photo)
                Row(
                    modifier = Modifier
                        .background(Color(0xFF1E1E22), RoundedCornerShape(12.dp))
                        .border(1.dp, ApexBorder, RoundedCornerShape(12.dp))
                        .padding(3.dp)
                ) {
                    ProjectModeChip(
                        title = "فيديو",
                        icon = Icons.Default.VideoLibrary,
                        isSelected = selectedType == "video",
                        onClick = { selectedType = "video" }
                    )
                    ProjectModeChip(
                        title = "صور",
                        icon = Icons.Default.PhotoLibrary,
                        isSelected = selectedType == "photo",
                        onClick = { selectedType = "photo" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Aspect Ratio Selector
            Text(
                text = "أبعاد العمل الموصى بها:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = ApexTextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ratios.forEach { (ratio, label, icon) ->
                    val isSelected = selectedRatio == ratio
                    RatioSelectorItem(
                        ratio = ratio,
                        label = label,
                        icon = icon,
                        isSelected = isSelected,
                        onClick = { selectedRatio = ratio },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Giant Create Project CTA
            Button(
                onClick = {
                    val defaultTitle = if (selectedType == "video") "فيديو جديد ${selectedRatio}" else "صورة جديدة"
                    onCreateProject(defaultTitle, selectedType, selectedRatio)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(16.dp, RoundedCornerShape(22.dp), ambientColor = ApexPurpleDark.copy(alpha = 0.4f), spotColor = ApexIndigo.copy(alpha = 0.4f))
                    .testTag("create_new_project_button"),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = androidx.compose.foundation.layout.PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF7C3AED),
                                    Color(0xFF6366F1),
                                    Color(0xFF3B82F6)
                                )
                            ),
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = if (selectedType == "video") "مشروع جديد ($selectedRatio)" else "تعديل صورة جديدة",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "ابدأ بصنع سحرك الخاص",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectModeChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) ApexPurple else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else ApexTextMuted,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else ApexTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun RatioSelectorItem(
    ratio: String,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Color(0xFF2D1B4E) else Color(0xFF1E1E24))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) ApexPurple else ApexBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) ApexPurpleLight else ApexTextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = ratio,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            ),
            color = if (isSelected) Color.White else ApexTextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) ApexPurpleLight else ApexTextMuted,
            fontSize = 9.sp,
            maxLines = 1
        )
    }
}
