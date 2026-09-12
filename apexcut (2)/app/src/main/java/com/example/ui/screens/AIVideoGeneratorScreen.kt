package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropLandscape
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.AIUsageBanner
import com.example.ui.components.ExportDialog
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexBorderGlow
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexPink
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleBg
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.viewmodel.ApexCutViewModel
import com.example.ui.viewmodel.CurrentScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AIVideoGeneratorScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    val promptText by viewModel.videoPromptText.collectAsStateWithLifecycle()
    val selectedStyle by viewModel.videoGenStyle.collectAsStateWithLifecycle()
    val selectedRatio by viewModel.videoGenRatio.collectAsStateWithLifecycle()
    val selectedDuration by viewModel.videoGenDuration.collectAsStateWithLifecycle()
    val selectedMotion by viewModel.videoGenCameraMotion.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGeneratingVideo.collectAsStateWithLifecycle()
    val progress by viewModel.videoGenProgress.collectAsStateWithLifecycle()
    val stageText by viewModel.videoGenStageText.collectAsStateWithLifecycle()
    val isCompleted by viewModel.isVideoGenCompleted.collectAsStateWithLifecycle()
    val generatedTitle by viewModel.generatedVideoTitle.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isGeneratedVideoPlaying.collectAsStateWithLifecycle()
    val playbackPos by viewModel.generatedVideoPlaybackPos.collectAsStateWithLifecycle()
    val successMessage by viewModel.videoGenSuccessMessage.collectAsStateWithLifecycle()

    val remainingUses by viewModel.remainingAIUses.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val isExportDialogOpen by viewModel.isExportDialogOpen.collectAsStateWithLifecycle()
    val isExporting by viewModel.isExporting.collectAsStateWithLifecycle()
    val exportProgress by viewModel.exportProgress.collectAsStateWithLifecycle()
    val isExportSuccess by viewModel.isExportSuccess.collectAsStateWithLifecycle()
    val exportResolution by viewModel.exportResolution.collectAsStateWithLifecycle()
    val exportFps by viewModel.exportFps.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val creativePrompts = listOf(
        "🏙️ مدينة سايبربانك ليلية مع سيارات طائرة وأضواء نيون بدقة 4K",
        "🌊 شاطئ سينمائي عند الغروب مع أمواج ذهبية فائقة الواقعية",
        "🚀 رائد فضاء يستكشف كوكباً بلورياً متوهجاً في مجرة بعيدة",
        "🏎️ سيارة رياضية خارقة تنطلق بأقصى سرعة في شوارع دبي ليلاً",
        "🦅 نسر سينمائي يحلق فوق قمم جبال شاهقة مغطاة بالجليد",
        "☕ فنجان قهوة فاخر يتصاعد منه البخار في مقهى دافئ وممطر"
    )

    val styles = listOf(
        "سينمائي هوليوود (Cinematic 4K)",
        "سايبربانك نيون (Cyberpunk Neon)",
        "رسوم متحركة 3D (Anime Ultra)",
        "لقطة دروون جوية (Aerial Drone)",
        "وثائقي واقعي (Hyperrealistic)"
    )

    val ratios = listOf(
        Triple("9:16", "ريلز / تيك توك", Icons.Default.CropPortrait),
        Triple("16:9", "يوتيوب سينمائي", Icons.Default.CropLandscape),
        Triple("1:1", "إنستغرام مربع", Icons.Default.CropSquare)
    )

    val durations = listOf(5, 10, 15)

    val cameraMotions = listOf(
        "طيران أمامي سينمائي (Fly Through)",
        "زووم للداخل ديناميكي (Zoom In)",
        "تدوير بانورامي 360 (Orbit Pan)",
        "لقطة ثابتة هادئة (Static Cinematic)"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexDarkBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                        .testTag("ai_video_back_button")
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "توليد الفيديوهات AI",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ApexTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(ApexPink.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text("AI 3.0 Pro", color = ApexPink, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        text = "تحويل النصوص والأفكار إلى فيديو سينمائي 4K",
                        color = ApexPurpleLight,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isPremium) Brush.horizontalGradient(listOf(ApexPurple, ApexBlue))
                        else Brush.horizontalGradient(listOf(ApexEmerald, Color(0xFF0D9488)))
                    )
                    .clickable { viewModel.navigateTo(CurrentScreen.Premium) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isPremium) "VIP 👑" else "3 يومياً مجاناً",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // AI Quota Banner
        AIUsageBanner(
            remainingUses = remainingUses,
            isPremium = isPremium,
            onUpgradeClick = { viewModel.navigateTo(CurrentScreen.Premium) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // PREVIEW CANVAS / GENERATION DISPLAY
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(if (selectedRatio == "16:9") 16f / 9f else if (selectedRatio == "1:1") 1f else 9f / 14f)
                .clip(RoundedCornerShape(22.dp))
                .background(ApexDarkCard)
                .border(1.dp, if (isCompleted) ApexPurple else ApexBorder, RoundedCornerShape(22.dp))
                .shadow(12.dp, RoundedCornerShape(22.dp), ambientColor = ApexPurple.copy(alpha = 0.3f))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_ai_video_gen_banner),
                contentDescription = "AI Video Preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark Cinematic Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Top Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
                        .border(1.dp, ApexPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HighQuality, contentDescription = null, tint = ApexCyan, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("4K 60FPS Neural", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(selectedRatio, color = ApexPink, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // In-Progress State
            if (isGenerating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.82f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { progress },
                                color = ApexPurple,
                                trackColor = Color(0xFF2E2B4A),
                                strokeWidth = 5.dp,
                                modifier = Modifier.size(80.dp)
                            )
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stageText,
                            color = ApexCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            color = ApexCyan,
                            trackColor = Color(0xFF28243C),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                    }
                }
            }

            // Bottom Controls on Preview (When completed or idle)
            if (isCompleted && !isGenerating) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.toggleGeneratedVideoPlayback() },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(ApexEmerald, CircleShape)
                                    .testTag("btn_play_generated_video")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.Black,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = generatedTitle,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "00:${String.format("%02d", playbackPos.toInt())} / 00:${String.format("%02d", selectedDuration)}",
                                    color = ApexCyan,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Row {
                            IconButton(
                                onClick = { viewModel.startAIVideoGeneration() },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Regenerate", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Slider(
                        value = playbackPos,
                        onValueChange = { /* Scrubbing */ },
                        valueRange = 0f..selectedDuration.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = ApexCyan,
                            activeTrackColor = ApexCyan,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (!isGenerating) {
                // Idle hint
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "✨ أدخل وصف مشهدك أدناه واضغط زر التوليد لصنع فيديو سينمائي فائق الدقة",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Action Row if video is generated
        AnimatedVisibility(
            visible = isCompleted && !isGenerating,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Open in Video Editor
                    Button(
                        onClick = { viewModel.openGeneratedInEditor() },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_open_generated_editor"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ApexPurple)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("فتح في المحرر", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Save to Projects
                    Button(
                        onClick = { viewModel.saveGeneratedToProjects() },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_save_generated_project"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282348))
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = ApexCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("حفظ في مشاريعي", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ApexCyan)
                    }

                    // Export 4K
                    Button(
                        onClick = { viewModel.openExportDialog() },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_export_generated_video"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ApexEmerald)
                    ) {
                        Icon(Icons.Default.IosShare, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تصدير 4K", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                if (successMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F2E22))
                            .border(1.dp, ApexEmerald.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = successMessage ?: "",
                            color = ApexEmerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // =========================================================================
        // PROMPT INPUT SECTION
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ApexDarkCard)
                .border(1.dp, ApexBorder, RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ApexPurple, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("وصف المشهد بالفيديو (Prompt)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = ApexTextPrimary, fontSize = 14.sp)
                    }
                    Text("${promptText.length} حرف", color = ApexTextMuted, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = promptText,
                    onValueChange = { viewModel.setVideoPromptText(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("input_ai_video_prompt"),
                    placeholder = { Text("اكتب بالتفصيل المشهد أو الفكرة التي تريد من الذكاء الاصطناعي توليدها وتحويلها إلى فيديو سينمائي...", color = ApexTextMuted, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ApexTextPrimary,
                        unfocusedTextColor = ApexTextPrimary,
                        focusedBorderColor = ApexPurple,
                        unfocusedBorderColor = ApexBorder,
                        focusedContainerColor = Color(0xFF131124),
                        unfocusedContainerColor = Color(0xFF131124)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("أفكار وسيناريوهات ملهمة جاهزة:", color = ApexTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    creativePrompts.forEach { prompt ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF221F3A))
                                .border(1.dp, ApexBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setVideoPromptText(prompt) }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(text = prompt, color = ApexCyan, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =========================================================================
        // STYLES SELECTOR
        // =========================================================================
        Text(
            text = "النمط السينمائي للإخراج:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ApexTextPrimary,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(styles) { style ->
                val isSelected = selectedStyle == style
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ApexPurpleBg else ApexDarkCard)
                        .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(12.dp))
                        .clickable { viewModel.setVideoGenStyle(style) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = style,
                        color = if (isSelected) ApexTextPrimary else ApexTextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =========================================================================
        // ASPECT RATIO & DURATION
        // =========================================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ratio
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "أبعاد الفيديو:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ApexTextPrimary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ratios.forEach { (ratio, _, icon) ->
                        val isSelected = selectedRatio == ratio
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ApexPurpleBg else ApexDarkCard)
                                .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(10.dp))
                                .clickable { viewModel.setVideoGenRatio(ratio) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(icon, contentDescription = null, tint = if (isSelected) ApexPurple else ApexTextMuted, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(ratio, color = if (isSelected) ApexTextPrimary else ApexTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Duration
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "مدة الفيديو:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ApexTextPrimary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    durations.forEach { dur ->
                        val isSelected = selectedDuration == dur
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ApexPurpleBg else ApexDarkCard)
                                .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(10.dp))
                                .clickable { viewModel.setVideoGenDuration(dur) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$dur ثواني",
                                color = if (isSelected) ApexTextPrimary else ApexTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Camera Motion
        Text(
            text = "حركة الكاميرا والإخراج:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ApexTextPrimary,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            cameraMotions.forEach { motion ->
                val isSelected = selectedMotion == motion
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) ApexPurpleBg else ApexDarkCard)
                        .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(10.dp))
                        .clickable { viewModel.setVideoGenCameraMotion(motion) }
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = motion,
                        color = if (isSelected) ApexCyan else ApexTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // =========================================================================
        // GIANT ACTION BUTTON: توليد الفيديو بالذكاء الاصطناعي
        // =========================================================================
        Button(
            onClick = { viewModel.startAIVideoGeneration() },
            enabled = !isGenerating,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(16.dp, RoundedCornerShape(18.dp), ambientColor = ApexPurple, spotColor = ApexPink)
                .testTag("btn_start_ai_video_generation"),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = androidx.compose.foundation.layout.PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (isGenerating) listOf(Color(0xFF4A4660), Color(0xFF332F4A))
                            else listOf(Color(0xFF8B5CF6), Color(0xFFEC4899), Color(0xFF06B6D4))
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "جاري التوليد العصبي... ${(progress * 100).toInt()}%",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCompleted) "إعادة توليد الفيديو ✨" else "توليد الفيديو بالذكاء الاصطناعي ✨",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }

    // Export Dialog
    ExportDialog(
        isOpen = isExportDialogOpen,
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
