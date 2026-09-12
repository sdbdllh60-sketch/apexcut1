package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TextFields
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.FilterPreset
import com.example.model.FilterPresetsData
import com.example.ui.components.ExportDialog
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexBorderGlow
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexCyanLight
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexPink
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.viewmodel.ApexCutViewModel
import com.example.ui.viewmodel.CurrentScreen

@Composable
fun VideoEditorScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    val project by viewModel.activeProject.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val playbackSec by viewModel.playbackSeconds.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
    val brightness by viewModel.brightness.collectAsStateWithLifecycle()
    val contrast by viewModel.contrast.collectAsStateWithLifecycle()
    val saturation by viewModel.saturation.collectAsStateWithLifecycle()
    val speed by viewModel.speedMultiplier.collectAsStateWithLifecycle()
    val textOverlay by viewModel.projectText.collectAsStateWithLifecycle()

    val isExportOpen by viewModel.isExportDialogOpen.collectAsStateWithLifecycle()
    val isExporting by viewModel.isExporting.collectAsStateWithLifecycle()
    val exportProgress by viewModel.exportProgress.collectAsStateWithLifecycle()
    val isExportSuccess by viewModel.isExportSuccess.collectAsStateWithLifecycle()
    val exportResolution by viewModel.exportResolution.collectAsStateWithLifecycle()
    val exportFps by viewModel.exportFps.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("فلاتر") }

    val durationSec = (project?.durationSeconds ?: 20).toFloat()
    val currentFormatted = String.format("%02d:%02d", (playbackSec / 60).toInt(), (playbackSec % 60).toInt())
    val totalFormatted = String.format("%02d:%02d", (durationSec / 60).toInt(), (durationSec % 60).toInt())

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
                        .testTag("video_editor_back")
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
                        text = project?.title ?: "محرر الفيديو",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ApexTextPrimary,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${project?.aspectRatio ?: "9:16"} • 4K 60FPS",
                            color = ApexCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Export CTA Button
            Button(
                onClick = { viewModel.openExportDialog() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .shadow(6.dp, RoundedCornerShape(10.dp), ambientColor = ApexPurple)
                    .background(
                        brush = Brush.horizontalGradient(listOf(ApexPurple, ApexBlue)),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .testTag("video_editor_export_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "تصدير 4K",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Center Video Canvas Player
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.3f)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            val canvasAspect = when (project?.aspectRatio) {
                "16:9" -> 16f / 9f
                "1:1" -> 1f
                else -> 9f / 16f
            }

            Box(
                modifier = Modifier
                    .aspectRatio(canvasAspect, matchHeightConstraintsFirst = true)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D0C1A))
                    .border(1.dp, ApexBorderGlow, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Video Image / Preview Simulation
                Image(
                    painter = painterResource(id = R.drawable.img_template_cyber),
                    contentDescription = "Video Canvas",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Text Overlay
                if (textOverlay.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(Color(0x88000000), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = textOverlay,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                // Filter Aura Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color(0xCC1A1636), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "فلتر: ${activeFilter.name}",
                        color = ApexPurpleLight,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Play / Pause Button Overlay
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x990A0918))
                        .border(1.dp, ApexCyan, CircleShape)
                        .clickable { viewModel.togglePlayPause() }
                        .testTag("video_player_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Timecode Overlay at bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color(0x99000000))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$currentFormatted / $totalFormatted",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${speed}x السرعة",
                        color = ApexCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Multi-Track Timeline & Seekbar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF110F23))
                .border(1.dp, ApexBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(12.dp)
        ) {
            // Seekbar
            Slider(
                value = playbackSec,
                onValueChange = { viewModel.seekTo(it) },
                valueRange = 0f..durationSec,
                colors = SliderDefaults.colors(
                    thumbColor = ApexCyan,
                    activeTrackColor = ApexPurple,
                    inactiveTrackColor = Color(0xFF26224A)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .testTag("timeline_seekbar")
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Multi-Track Filmstrip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF191734))
                    .border(1.dp, ApexBorder, RoundedCornerShape(8.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MovieFilter, contentDescription = null, tint = ApexPurpleLight, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF2E265C))
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text("مسار الفيديو الرئيسي (Clip #1)", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Audio Waveform Track
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF15132A))
                    .border(1.dp, Color(0xFF252044), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = ApexCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF163E5C))
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text("مسار الموسيقى: ${project?.musicTrackName ?: "Apex Beat"}", color = ApexCyanLight, fontSize = 9.sp)
                }
            }
        }

        // Bottom Tool Shelf Tab Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0C0B18))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                Pair("فلاتر", Icons.Default.FormatColorFill),
                Pair("قص وتقسيم", Icons.Default.ContentCut),
                Pair("السرعة", Icons.Default.Speed),
                Pair("صوت وموسيقى", Icons.AutoMirrored.Filled.VolumeUp),
                Pair("نصوص وعناوين", Icons.Default.TextFields),
                Pair("تلوين", Icons.Default.Tune)
            )

            items(tabs) { (title, icon) ->
                val isSelected = activeTab == title
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) ApexPurple else Color(0xFF181530))
                        .border(1.dp, if (isSelected) ApexPurpleLight else ApexBorder, RoundedCornerShape(10.dp))
                        .clickable { activeTab = title }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else ApexTextSecondary,
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
        }

        // Tool Content Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(Color(0xFF0A0915))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            when (activeTab) {
                "فلاتر" -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(FilterPresetsData.filters) { filter ->
                            val isSel = activeFilter.id == filter.id
                            Column(
                                modifier = Modifier
                                    .width(72.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF282155) else Color(0xFF15132B))
                                    .border(1.dp, if (isSel) ApexPurpleLight else ApexBorder, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setFilter(filter) }
                                    .padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(filter.glowColorHex), CircleShape)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = filter.name,
                                    color = if (isSel) Color.White else ApexTextSecondary,
                                    fontSize = 9.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                "السرعة" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(0.5f, 1.0f, 1.5f, 2.0f, 4.0f).forEach { s ->
                            val isSel = speed == s
                            Button(
                                onClick = { viewModel.setSpeedMultiplier(s) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSel) ApexCyan else Color(0xFF1B1838)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("${s}x", color = if (isSel) Color(0xFF051D2D) else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                "تلوين" -> {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("السطوع: ${(brightness * 100).toInt()}%", color = ApexTextSecondary, fontSize = 10.sp, modifier = Modifier.width(70.dp))
                            Slider(
                                value = brightness,
                                onValueChange = { viewModel.setBrightness(it) },
                                valueRange = -1f..1f,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("التباين: ${(contrast * 100).toInt()}%", color = ApexTextSecondary, fontSize = 10.sp, modifier = Modifier.width(70.dp))
                            Slider(
                                value = contrast,
                                onValueChange = { viewModel.setContrast(it) },
                                valueRange = -1f..1f,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "أدوات متطورة جاهزة للتطبيق على المقطع المختار",
                            color = ApexTextSecondary,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(ApexPurple.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .border(1.dp, ApexPurple, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setProjectText("ApexCut 4K") }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("تطبيق تلقائي", color = ApexPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
