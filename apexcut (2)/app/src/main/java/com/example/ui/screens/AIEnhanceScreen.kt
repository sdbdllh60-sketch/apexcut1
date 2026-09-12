package com.example.ui.screens

import android.graphics.Bitmap
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
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

@Composable
fun AIEnhanceScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    val enhanceVideoUri by viewModel.enhanceVideoUri.collectAsStateWithLifecycle()
    val videoMetadata by viewModel.enhanceVideoMetadata.collectAsStateWithLifecycle()
    val enhanceOptions by viewModel.enhanceOptions.collectAsStateWithLifecycle()
    val originalBitmap by viewModel.originalFrameBitmap.collectAsStateWithLifecycle()
    val enhancedBitmap by viewModel.enhancedFrameBitmap.collectAsStateWithLifecycle()
    val isAIProcessing by viewModel.isAIProcessing.collectAsStateWithLifecycle()
    val aiProgress by viewModel.aiProgress.collectAsStateWithLifecycle()
    val aiStatusText by viewModel.aiStatusText.collectAsStateWithLifecycle()
    val isEnhanceCompleted by viewModel.isEnhanceCompleted.collectAsStateWithLifecycle()
    val beforeAfterSlider by viewModel.aiBeforeAfterSlider.collectAsStateWithLifecycle()
    val saveMessage by viewModel.saveEnhanceProjectMessage.collectAsStateWithLifecycle()
    val remainingUses by viewModel.remainingAIUses.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val isExportDialogOpen by viewModel.isExportDialogOpen.collectAsStateWithLifecycle()
    val isExporting by viewModel.isExporting.collectAsStateWithLifecycle()
    val exportProgress by viewModel.exportProgress.collectAsStateWithLifecycle()
    val isExportSuccess by viewModel.isExportSuccess.collectAsStateWithLifecycle()
    val exportResolution by viewModel.exportResolution.collectAsStateWithLifecycle()
    val exportFps by viewModel.exportFps.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    // Video Picker Launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setEnhanceVideoUri(uri)
        }
    }

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
                        .testTag("ai_enhance_back")
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
                        text = "تحسين جودة الفيديو AI",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ApexTextPrimary
                    )
                    Text(
                        text = "ترقية الدقة إلى 4K HDR والترميم العصبي السينمائي",
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
        // SECTION 1: مربع إضافة فيديو ومعاينة الفيديو وعرض المعلومات
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ApexDarkCard)
                .border(1.dp, if (enhanceVideoUri != null) ApexPurple else ApexBorder, RoundedCornerShape(18.dp))
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
                            imageVector = Icons.Default.HighQuality,
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
                            .testTag("btn_pick_enhance_video")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("اختيار فيديو من الهاتف", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Demo Presets Quick Selector
                Text(
                    text = "أو اختر لقطة تجريبية لمعاينتها فوراً:",
                    color = ApexTextMuted,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        DemoEnhanceChip(
                            title = "فيديو 720p مشوش",
                            isSelected = videoMetadata.title.contains("مشوشة"),
                            onClick = {
                                viewModel.selectDemoEnhanceVideo(
                                    "لقطة وثائقية 720p مشوشة",
                                    "00:18",
                                    18000L,
                                    is4K = false
                                )
                            }
                        )
                    }
                    item {
                        DemoEnhanceChip(
                            title = "لقطة ليلية ضعيفة الإضاءة",
                            isSelected = videoMetadata.title.contains("ليلية"),
                            onClick = {
                                viewModel.selectDemoEnhanceVideo(
                                    "لقطة شارع ليلي منخفضة الإضاءة",
                                    "00:14",
                                    14000L,
                                    is4K = false
                                )
                            }
                        )
                    }
                    item {
                        DemoEnhanceChip(
                            title = "فيديو قديم 480p",
                            isSelected = videoMetadata.title.contains("قديم"),
                            onClick = {
                                viewModel.selectDemoEnhanceVideo(
                                    "تسجيل قديم بدقة 480p",
                                    "00:25",
                                    25000L,
                                    is4K = false
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Video Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F0E17))
                        .border(1.dp, ApexBorderGlow, RoundedCornerShape(14.dp))
                ) {
                    if (originalBitmap != null) {
                        Image(
                            bitmap = originalBitmap!!.asImageBitmap(),
                            contentDescription = "Video Frame Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.img_template_cyber),
                            contentDescription = "Fallback Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Resolution Tag Overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = videoMetadata.resolutionLabel,
                            color = ApexCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Video Information Metadata Table
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF141224))
                        .border(1.dp, ApexBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "معلومات الفيديو المكتشفة:",
                            fontWeight = FontWeight.Bold,
                            color = ApexTextPrimary,
                            fontSize = 12.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MetadataItem(label = "الدقة", value = "${videoMetadata.width}x${videoMetadata.height}")
                            MetadataItem(label = "المدة", value = videoMetadata.durationFormatted)
                            MetadataItem(label = "الإطارات", value = "${videoMetadata.fps} FPS")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MetadataItem(label = "معدل البت", value = videoMetadata.bitrateFormatted)
                            MetadataItem(label = "حجم الملف", value = videoMetadata.sizeFormatted)
                            MetadataItem(label = "الترميز", value = "H.264 / AAC")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 2: خيارات التحسين (Enhancement Options)
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
                        text = "خيارات التحسين بالذكاء الاصطناعي",
                        fontWeight = FontWeight.Bold,
                        color = ApexTextPrimary,
                        fontSize = 14.sp
                    )
                    Text("خوارزميات 2026", color = ApexPurpleLight, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 1. زيادة الوضوح (Super Resolution)
                EnhanceOptionRow(
                    title = "زيادة الوضوح (Super Resolution 4K)",
                    subtitle = "مضاعفة كثافة البكسلات وتوليد تفاصيل فائقة النقاء",
                    icon = Icons.Default.HighQuality,
                    enabled = enhanceOptions.enableSuperResolution,
                    onToggle = { isChecked ->
                        viewModel.updateEnhanceOptions { it.copy(enableSuperResolution = isChecked) }
                    }
                )

                // 2. تحسين التفاصيل (Detail Enhancement)
                EnhanceOptionRow(
                    title = "تحسين التفاصيل والحواف",
                    subtitle = "إبراز ملامح الوجوه والأنسجة الدقيقة في المشهد",
                    icon = Icons.Default.Layers,
                    enabled = enhanceOptions.enableDetailEnhance,
                    onToggle = { isChecked ->
                        viewModel.updateEnhanceOptions { it.copy(enableDetailEnhance = isChecked) }
                    }
                )

                // 3. تقليل التشويش (Denoising)
                EnhanceOptionRow(
                    title = "تقليل التشويش والضجيج الرقمي (AI Denoise)",
                    subtitle = "إزالة التحبيب وتصفية الإطارات من الشوائب",
                    icon = Icons.Default.Tune,
                    enabled = enhanceOptions.enableDenoise,
                    onToggle = { isChecked ->
                        viewModel.updateEnhanceOptions { it.copy(enableDenoise = isChecked) }
                    }
                )

                // 4. تحسين الإضاءة (Low-light Boost & HDR)
                EnhanceOptionRow(
                    title = "تحسين الإضاءة وتوسيع مدى HDR",
                    subtitle = "إبراز المناطق المظلمة وموازنة السطوع تلقائياً",
                    icon = Icons.Default.BrightnessMedium,
                    enabled = enhanceOptions.enableLightingHdr,
                    onToggle = { isChecked ->
                        viewModel.updateEnhanceOptions { it.copy(enableLightingHdr = isChecked) }
                    }
                )

                // 5. تحسين الألوان (Color Grading)
                EnhanceOptionRow(
                    title = "تحسين الألوان والتدرج السينمائي",
                    subtitle = "تطبيق حيوية سينمائية وضبط التوازن اللوني",
                    icon = Icons.Default.ColorLens,
                    enabled = enhanceOptions.enableColorGrade,
                    onToggle = { isChecked ->
                        viewModel.updateEnhanceOptions { it.copy(enableColorGrade = isChecked) }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 3: زر تحسين الفيديو بالذكاء الاصطناعي وشريط التقدم
        // =========================================================================
        Button(
            onClick = { viewModel.runAIVideoEnhance() },
            enabled = !isAIProcessing,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp), ambientColor = ApexPurple, spotColor = ApexPurple)
                .testTag("btn_start_ai_video_enhance"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ApexPurple)
        ) {
            if (isAIProcessing) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("جاري المعالجة والترميم العصبي...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("تحسين الفيديو بالذكاء الاصطناعي", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Live Processing Progress & Status Text
        if (isAIProcessing) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF161329))
                    .border(1.dp, ApexPurple.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = aiStatusText,
                            color = ApexCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${(aiProgress * 100).toInt()}%",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    LinearProgressIndicator(
                        progress = { aiProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ApexPurple,
                        trackColor = Color(0xFF262142),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // SECTION 4: بعد انتهاء المعالجة - مقارنة Before / After التفاعلية
        // =========================================================================
        AnimatedVisibility(
            visible = isEnhanceCompleted,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(ApexDarkCard)
                    .border(1.dp, ApexEmerald.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
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
                                imageVector = Icons.Default.Compare,
                                contentDescription = null,
                                tint = ApexEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "مقارنة Before / After التفاعلية",
                                fontWeight = FontWeight.Bold,
                                color = ApexTextPrimary,
                                fontSize = 14.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFF0D3222), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("تمت الترقية إلى 4K", color = ApexEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "اسحب الخط الفاصل يميناً ويساراً لمقارنة جودة التفاصيل قبل وبعد التحسين:",
                        color = ApexTextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Split Interactive Comparison Viewer
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black)
                            .border(1.dp, ApexBorderGlow, RoundedCornerShape(14.dp))
                    ) {
                        val boxWidth = constraints.maxWidth.toFloat()

                        // Layer 1: Enhanced Image (Base)
                        if (enhancedBitmap != null) {
                            Image(
                                bitmap = enhancedBitmap!!.asImageBitmap(),
                                contentDescription = "Enhanced Frame",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_template_cyber),
                                contentDescription = "Enhanced",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Layer 2: Original Frame on Top clipped to the slider position
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width((maxWidth * beforeAfterSlider))
                                .clip(RoundedCornerShape(0.dp))
                        ) {
                            if (originalBitmap != null) {
                                Image(
                                    bitmap = originalBitmap!!.asImageBitmap(),
                                    contentDescription = "Original Frame",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // Divider Line & Handle
                        Box(
                            modifier = Modifier
                                .offset { IntOffset((boxWidth * beforeAfterSlider).toInt() - 16, 0) }
                                .fillMaxHeight()
                                .width(32.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val newPos = (beforeAfterSlider + dragAmount.x / boxWidth).coerceIn(0.05f, 0.95f)
                                        viewModel.setBeforeAfterSlider(newPos)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            // Vertical Line
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2.dp)
                                    .background(Color.White)
                            )

                            // Center Handle Knob
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(ApexPurple, CircleShape)
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Compare,
                                    contentDescription = "Slider Handle",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Badges: Before vs After
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("قبل: أصلي مشوش", color = Color(0xFFEF4444), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("بعد: 4K محسّن AI", color = ApexEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Slider(
                        value = beforeAfterSlider,
                        onValueChange = { viewModel.setBeforeAfterSlider(it) },
                        colors = SliderDefaults.colors(
                            thumbColor = ApexPurple,
                            activeTrackColor = ApexPurple,
                            inactiveTrackColor = Color(0xFF2E2B4A)
                        )
                    )
                }
            }
        }

        if (saveMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F2E22))
                    .border(1.dp, ApexEmerald.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ApexEmerald, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = saveMessage ?: "",
                        color = ApexEmerald,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =========================================================================
        // SECTION 5: زر حفظ الفيديو المحسن وزر التصدير
        // =========================================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.saveEnhancedVideoToProjects() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_save_enhanced_video"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E1B38)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, ApexPurple)
            ) {
                Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = ApexPurple, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("حفظ الفيديو المحسن", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Button(
                onClick = { viewModel.openExportDialog() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_export_enhanced_video"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981)
                )
            ) {
                Icon(Icons.Default.IosShare, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("تصدير 4K", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
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
private fun DemoEnhanceChip(
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

@Composable
private fun MetadataItem(label: String, value: String) {
    Column {
        Text(text = label, color = ApexTextMuted, fontSize = 10.sp)
        Text(text = value, color = ApexTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EnhanceOptionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (enabled) Color(0xFF1A172F) else Color(0xFF141222))
            .border(1.dp, if (enabled) ApexPurple.copy(alpha = 0.5f) else ApexBorder, RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(if (enabled) ApexPurple else Color(0xFF221F38), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = ApexTextPrimary,
                    fontSize = 12.sp
                )
                Text(
                    text = subtitle,
                    color = ApexTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ApexPurple,
                uncheckedThumbColor = Color(0xFF8E8B9E),
                uncheckedTrackColor = Color(0xFF26233B)
            )
        )
    }
}
