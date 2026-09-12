package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.FilterPresetsData
import com.example.ui.components.ExportDialog
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexBorderGlow
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.viewmodel.ApexCutViewModel
import com.example.ui.viewmodel.CurrentScreen

@Composable
fun PhotoEditorScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    val project by viewModel.activeProject.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
    val brightness by viewModel.brightness.collectAsStateWithLifecycle()
    val contrast by viewModel.contrast.collectAsStateWithLifecycle()
    val saturation by viewModel.saturation.collectAsStateWithLifecycle()
    val aspectRatio by viewModel.aspectRatio.collectAsStateWithLifecycle()

    val isExportOpen by viewModel.isExportDialogOpen.collectAsStateWithLifecycle()
    val isExporting by viewModel.isExporting.collectAsStateWithLifecycle()
    val exportProgress by viewModel.exportProgress.collectAsStateWithLifecycle()
    val isExportSuccess by viewModel.isExportSuccess.collectAsStateWithLifecycle()
    val exportResolution by viewModel.exportResolution.collectAsStateWithLifecycle()
    val exportFps by viewModel.exportFps.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    var photoTab by remember { mutableStateOf("تعديل") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexDarkBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateTo(CurrentScreen.Home) },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF1B1838), CircleShape)
                        .border(1.dp, ApexBorder, CircleShape)
                        .testTag("photo_editor_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ApexTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = project?.title ?: "تحرير الصور",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ApexTextPrimary
                    )
                    Text(
                        text = "محرك RAW الاحترافي 2026",
                        color = ApexCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Save / Export Button
            Button(
                onClick = { viewModel.openExportDialog() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .shadow(6.dp, RoundedCornerShape(10.dp), ambientColor = ApexBlue)
                    .background(
                        brush = Brush.horizontalGradient(listOf(ApexBlue, ApexCyan)),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .testTag("photo_editor_save_button")
            ) {
                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("حفظ الصورة", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Photo Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val aspectFloat = when (aspectRatio) {
                "16:9" -> 16f / 9f
                "4:5" -> 4f / 5f
                "9:16" -> 9f / 16f
                else -> 1f
            }

            Box(
                modifier = Modifier
                    .aspectRatio(aspectFloat, matchHeightConstraintsFirst = true)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF131124))
                    .border(1.5.dp, ApexBorderGlow, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_apexcut_logo),
                    contentDescription = "Photo Canvas",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Filter Label Overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .background(Color(0xCC000000), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "الفلتر: ${activeFilter.name}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bottom Controls Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF121024))
                .border(1.dp, ApexBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(14.dp)
        ) {
            // Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Pair("تعديل", Icons.Default.Tune),
                    Pair("فلاتر", Icons.Default.Filter),
                    Pair("أبعاد", Icons.Default.Crop),
                    Pair("ذكاء اصطناعي", Icons.Default.AutoAwesome)
                ).forEach { (tab, icon) ->
                    val isSel = photoTab == tab
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) ApexBlue else Color(0xFF1B1936))
                            .border(1.dp, if (isSel) ApexCyan else ApexBorder, RoundedCornerShape(10.dp))
                            .clickable { photoTab = tab }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSel) Color.White else ApexTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tab,
                            color = if (isSel) Color.White else ApexTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Panels
            when (photoTab) {
                "تعديل" -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        PhotoAdjustmentRow("السطوع", brightness) { viewModel.setBrightness(it) }
                        PhotoAdjustmentRow("التباين", contrast) { viewModel.setContrast(it) }
                        PhotoAdjustmentRow("التشبع", saturation) { viewModel.setSaturation(it) }
                    }
                }
                "فلاتر" -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(FilterPresetsData.filters) { filter ->
                            val isSel = activeFilter.id == filter.id
                            Column(
                                modifier = Modifier
                                    .width(76.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) Color(0xFF23255C) else Color(0xFF181530))
                                    .border(1.dp, if (isSel) ApexCyan else ApexBorder, RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setFilter(filter) }
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(Color(filter.glowColorHex), CircleShape)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = filter.name,
                                    color = if (isSel) Color.White else ApexTextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                "أبعاد" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("1:1", "4:5", "9:16", "16:9").forEach { ratio ->
                            val isSel = aspectRatio == ratio
                            Button(
                                onClick = { viewModel.setAspectRatio(ratio) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSel) ApexPurple else Color(0xFF1A1738)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(ratio, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF181734), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("رتوش الوجه والجمال الذكي AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("تنعيم البشرة وتكبير العيون تلقائياً", color = ApexCyan, fontSize = 10.sp)
                        }
                        Button(
                            onClick = {
                                viewModel.setBrightness(0.15f)
                                viewModel.setContrast(0.2f)
                                viewModel.setSaturation(0.25f)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ApexPurple)
                        ) {
                            Text("تطبيق سحري", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    // Export Dialog
    ExportDialog(
        isOpen = isExportOpen,
        isExporting = isExporting,
        progress = exportProgress,
        isSuccess = isExportSuccess,
        selectedResolution = exportResolution,
        selectedFps = exportFps,
        isPremium = isPremium,
        onSelectResolution = { viewModel.setExportResolution(it) },
        onSelectFps = { viewModel.setExportFps(it) },
        onStartExport = { viewModel.startExportSimulation() },
        onUpgradeToPremium = { viewModel.navigateTo(CurrentScreen.Premium) },
        onDismiss = { viewModel.closeExportDialog() }
    )
}

@Composable
private fun PhotoAdjustmentRow(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$title: ${(value * 100).toInt()}%",
            color = ApexTextSecondary,
            fontSize = 11.sp,
            modifier = Modifier.width(90.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = -1f..1f,
            colors = SliderDefaults.colors(
                thumbColor = ApexCyan,
                activeTrackColor = ApexBlue,
                inactiveTrackColor = Color(0xFF262248)
            ),
            modifier = Modifier.weight(1f)
        )
    }
}
