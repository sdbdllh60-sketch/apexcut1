package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VideoFile
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.ArabicVoiceData
import com.example.model.ArabicVoiceOption
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

@Composable
fun AIAudioScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    val voiceoverVideoUri by viewModel.voiceoverVideoUri.collectAsStateWithLifecycle()
    val videoMetadata by viewModel.voiceoverVideoMetadata.collectAsStateWithLifecycle()
    val isVideoPlaying by viewModel.isVoiceoverVideoPlaying.collectAsStateWithLifecycle()
    val playbackPosition by viewModel.voiceoverPlaybackPosition.collectAsStateWithLifecycle()
    val scriptText by viewModel.voiceScriptText.collectAsStateWithLifecycle()
    val selectedVoice by viewModel.selectedVoice.collectAsStateWithLifecycle()
    val voicePitch by viewModel.voicePitch.collectAsStateWithLifecycle()
    val voiceSpeed by viewModel.voiceSpeed.collectAsStateWithLifecycle()
    val voiceVolume by viewModel.voiceVolume.collectAsStateWithLifecycle()
    val videoVolume by viewModel.videoVolume.collectAsStateWithLifecycle()
    val isVoiceoverGenerated by viewModel.isVoiceoverGenerated.collectAsStateWithLifecycle()
    val isVoiceGenerating by viewModel.isVoiceGenerating.collectAsStateWithLifecycle()
    val voiceSuccessMessage by viewModel.voiceSuccessMessage.collectAsStateWithLifecycle()
    val remainingUses by viewModel.remainingAIUses.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val isExportDialogOpen by viewModel.isExportDialogOpen.collectAsStateWithLifecycle()
    val isExporting by viewModel.isExporting.collectAsStateWithLifecycle()
    val exportProgress by viewModel.exportProgress.collectAsStateWithLifecycle()
    val isExportSuccess by viewModel.isExportSuccess.collectAsStateWithLifecycle()
    val exportResolution by viewModel.exportResolution.collectAsStateWithLifecycle()
    val exportFps by viewModel.exportFps.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    // Photo/Video Picker Launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setVoiceoverVideoUri(uri)
        }
    }

    val samplePrompts = listOf(
        "إعلان تسويقي" to "اكتشف القوة غير المحدودة في مونتاج الفيديوهات مع ApexCut، حيث يتحول كل مشهد إلى تحفة سينمائية.",
        "وثائقي فخم" to "في أعماق الطبيعة الساحرة، تتجلى روعة الحياة البرية بألوانها البراقة وتفاصيلها المدهشة.",
        "سرد قصصي" to "كانت تلك اللحظة هي البداية لكل شيء، حين تغير مسار الحكاية إلى الأبد.",
        "تقرير إخباري" to "نقدم لكم التقرير الحصري اليومي عن أبرز التطورات التكنولوجية في عالم الذكاء الاصطناعي."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexDarkBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 1. Top Header
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
                        .testTag("ai_audio_back")
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
                        text = "التعليق الصوتي AI",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ApexTextPrimary
                    )
                    Text(
                        text = "تحويل النصوص إلى أصوات سينمائية ودمجها على الفيديو",
                        color = ApexCyan,
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

        // AI Usage Counter Banner
        AIUsageBanner(
            remainingUses = remainingUses,
            isPremium = isPremium,
            onUpgradeClick = { viewModel.navigateTo(CurrentScreen.Premium) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 1: مربع إضافة فيديو ومعاينة الفيديو داخل الشاشة
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ApexDarkCard)
                .border(1.dp, if (voiceoverVideoUri != null) ApexPurple else ApexBorder, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = ApexPurple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "إضافة فيديو",
                            fontWeight = FontWeight.Bold,
                            color = ApexTextPrimary,
                            fontSize = 15.sp
                        )
                    }

                    Button(
                        onClick = {
                            videoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ApexPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("btn_pick_voiceover_video")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("اختيار فيديو من الهاتف", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Demo Presets Quick Selector
                Text(
                    text = "أو اختر فيديو تجريبي سريع:",
                    color = ApexTextMuted,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        DemoVideoChip(
                            title = "فيديو إعلاني",
                            isSelected = videoMetadata.title.contains("إعلاني"),
                            onClick = {
                                viewModel.selectDemoVoiceoverVideo(
                                    "فيديو إعلاني سينمائي",
                                    "00:15",
                                    15000L
                                )
                            }
                        )
                    }
                    item {
                        DemoVideoChip(
                            title = "فيديو وثائقي طبيعة",
                            isSelected = videoMetadata.title.contains("طبيعة"),
                            onClick = {
                                viewModel.selectDemoVoiceoverVideo(
                                    "فيديو وثائقي طبيعة وسياحة",
                                    "00:22",
                                    22000L
                                )
                            }
                        )
                    }
                    item {
                        DemoVideoChip(
                            title = "فيديو تقني نيون",
                            isSelected = videoMetadata.title.contains("نيون"),
                            onClick = {
                                viewModel.selectDemoVoiceoverVideo(
                                    "فيديو موشن جرافيك تكنولوجي",
                                    "00:18",
                                    18000L
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Video Preview Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F0E17))
                        .border(1.dp, ApexBorderGlow, RoundedCornerShape(14.dp))
                ) {
                    Image(
                        painter = painterResource(
                            id = if (videoMetadata.title.contains("طبيعة")) R.drawable.img_ai_enhance_banner
                            else R.drawable.img_template_cyber
                        ),
                        contentDescription = "Video Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Video Overlay Info
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${videoMetadata.title} • ${videoMetadata.durationFormatted}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Play/Pause Overlay Button
                    IconButton(
                        onClick = { viewModel.toggleVoiceoverVideoPlayback() },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(54.dp)
                            .background(ApexPurple.copy(alpha = 0.85f), CircleShape)
                            .testTag("btn_play_voiceover_preview")
                    ) {
                        Icon(
                            imageVector = if (isVideoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isVideoPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // Subtitle Overlay if Generated
                    if (isVoiceoverGenerated && scriptText.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.75f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = ApexCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = scriptText,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = {
                        val maxD = (videoMetadata.durationMs / 1000f).coerceAtLeast(1f)
                        (playbackPosition / maxD).coerceIn(0f, 1f)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = ApexPurple,
                    trackColor = Color(0xFF262338),
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 2: مربع نص الكابشن أو النص المراد تحويله لصوت
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ApexDarkCard)
                .border(1.dp, ApexBorder, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اكتب الكابشن أو النص الذي تريد تحويله إلى صوت",
                        fontWeight = FontWeight.Bold,
                        color = ApexTextPrimary,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "${scriptText.length} حرف",
                        color = ApexTextMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = scriptText,
                    onValueChange = { viewModel.setVoiceScript(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .testTag("input_voiceover_script"),
                    placeholder = {
                        Text(
                            "اكتب هنا النص العربي أو الكابشن الإعلاني لتحويله إلى تعليق صوتي فائق النقاء...",
                            color = ApexTextMuted,
                            fontSize = 12.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ApexPurple,
                        unfocusedBorderColor = ApexBorder,
                        focusedContainerColor = Color(0xFF131122),
                        unfocusedContainerColor = Color(0xFF131122),
                        focusedTextColor = ApexTextPrimary,
                        unfocusedTextColor = ApexTextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Prompt Chips
                Text("اقتراحات نصوص جاهزة:", color = ApexTextMuted, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(samplePrompts) { (label, text) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1B1830))
                                .border(1.dp, ApexBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setVoiceScript(text) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(label, color = ApexCyan, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 3: اختيار نوع/صوت المتحدث
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ApexDarkCard)
                .border(1.dp, ApexBorder, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اختيار صوت المتحدث",
                        fontWeight = FontWeight.Bold,
                        color = ApexTextPrimary,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "نبرات عربية واقعية",
                        color = ApexPurpleLight,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Voice Cards
                ArabicVoiceData.voices.forEach { voice ->
                    val isSelected = selectedVoice.id == voice.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) ApexPurpleBg else Color(0xFF161426))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) ApexPurple else ApexBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.selectVoice(voice) }
                            .padding(12.dp)
                            .testTag("voice_option_${voice.id}")
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
                                        .background(
                                            if (isSelected) ApexPurple else Color(0xFF242040),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RecordVoiceOver,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = voice.name,
                                            fontWeight = FontWeight.Bold,
                                            color = ApexTextPrimary,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF282348), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = voice.gender,
                                                color = ApexCyan,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Text(
                                        text = voice.style,
                                        color = ApexTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = ApexPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Voice Customization Sliders
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "سرعة النطق: ${String.format("%.1fx", voiceSpeed)}",
                            color = ApexTextSecondary,
                            fontSize = 11.sp
                        )
                        Slider(
                            value = voiceSpeed,
                            onValueChange = { viewModel.setVoiceSpeed(it) },
                            valueRange = 0.7f..1.4f,
                            colors = SliderDefaults.colors(
                                thumbColor = ApexPurple,
                                activeTrackColor = ApexPurple,
                                inactiveTrackColor = Color(0xFF2B2844)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "طبقة الصوت (Pitch): ${String.format("%.1fx", voicePitch)}",
                            color = ApexTextSecondary,
                            fontSize = 11.sp
                        )
                        Slider(
                            value = voicePitch,
                            onValueChange = { viewModel.setVoicePitch(it) },
                            valueRange = 0.7f..1.3f,
                            colors = SliderDefaults.colors(
                                thumbColor = ApexCyan,
                                activeTrackColor = ApexCyan,
                                inactiveTrackColor = Color(0xFF2B2844)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 4: زر إنشاء التعليق الصوتي
        // =========================================================================
        Button(
            onClick = { viewModel.generateAIVoiceover() },
            enabled = !isVoiceGenerating,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp), ambientColor = ApexPurple, spotColor = ApexPurple)
                .testTag("btn_generate_ai_voiceover"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ApexPurple)
        ) {
            if (isVoiceGenerating) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("جاري توليد ومعالجة الصوت بالذكاء الاصطناعي...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("إنشاء التعليق الصوتي", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        if (voiceSuccessMessage != null) {
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
                    text = voiceSuccessMessage ?: "",
                    color = ApexEmerald,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 5: المسار الصوتي والتحكم بمستوى الصوت وحذف وإعادة الإنشاء
        // =========================================================================
        AnimatedVisibility(
            visible = isVoiceoverGenerated,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(ApexDarkCard)
                    .border(1.dp, ApexEmerald.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = ApexEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "مسار التعليق الصوتي المدمج",
                                fontWeight = FontWeight.Bold,
                                color = ApexTextPrimary,
                                fontSize = 14.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFF0F3223), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("نشط ومدمج بالفيديو", color = ApexEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Voiceover Track Waveform Visualizer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF141224))
                            .border(1.dp, ApexBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.toggleVoiceoverVideoPlayback() },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(ApexEmerald, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (isVideoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Preview",
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "صوت ${selectedVoice.name} (${selectedVoice.style})",
                                        color = ApexTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "جودة 48kHz Stereo AI Studio",
                                        color = ApexCyan,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Row {
                                IconButton(
                                    onClick = { viewModel.generateAIVoiceover() },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Re-generate",
                                        tint = ApexCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.deleteVoiceoverTrack() },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = ApexPink,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Volume Controls
                    Text(
                        text = "مستوى صوت التعليق: ${(voiceVolume * 100).toInt()}%",
                        color = ApexTextSecondary,
                        fontSize = 12.sp
                    )
                    Slider(
                        value = voiceVolume,
                        onValueChange = { viewModel.setVoiceVolume(it) },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = ApexEmerald,
                            activeTrackColor = ApexEmerald,
                            inactiveTrackColor = Color(0xFF2B2844)
                        )
                    )

                    Text(
                        text = "مستوى صوت الفيديو الأصلي (خلفية): ${(videoVolume * 100).toInt()}%",
                        color = ApexTextSecondary,
                        fontSize = 12.sp
                    )
                    Slider(
                        value = videoVolume,
                        onValueChange = { viewModel.setVideoVolume(it) },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = ApexPurple,
                            activeTrackColor = ApexPurple,
                            inactiveTrackColor = Color(0xFF2B2844)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =========================================================================
        // SECTION 6: زر تصدير الفيديو النهائي مع التعليق الصوتي
        // =========================================================================
        Button(
            onClick = { viewModel.exportVoiceoverVideo() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_export_final_voiceover_video"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981)
            )
        ) {
            Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "تصدير الفيديو النهائي مع التعليق الصوتي",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
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

@Composable
private fun DemoVideoChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) ApexPurpleBg else Color(0xFF18162A))
            .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.VideoFile,
                contentDescription = null,
                tint = if (isSelected) ApexPurple else ApexCyan,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                color = if (isSelected) ApexTextPrimary else ApexTextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
