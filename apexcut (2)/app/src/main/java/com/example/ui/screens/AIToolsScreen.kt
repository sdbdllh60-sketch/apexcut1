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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Subtitles
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.AIToolItem
import com.example.model.AIToolsData
import com.example.ui.components.AIUsageBanner
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AIToolsScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    val remainingUses by viewModel.remainingAIUses.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val selectedTool by viewModel.selectedInteractiveAITool.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexDarkBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Header
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
                        .testTag("ai_tools_back")
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
                        text = "مركز أدوات الذكاء الاصطناعي",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ApexTextPrimary
                    )
                    Text(
                        text = "مجموعة أدوات المونتاج العصبي الشاملة والمفعلة",
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

        // AI Usage Counter Banner
        AIUsageBanner(
            remainingUses = remainingUses,
            isPremium = isPremium,
            onUpgradeClick = { viewModel.navigateTo(CurrentScreen.Premium) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(AIToolsData.allTools) { tool ->
                AIToolCard(
                    tool = tool,
                    onClick = {
                        when (tool.id) {
                            "tool_video_gen" -> viewModel.navigateTo(CurrentScreen.AIVideoGen)
                            "tool_4k_upscale", "tool_smart_colorist" -> viewModel.navigateTo(CurrentScreen.AIEnhance())
                            "tool_voiceover", "tool_vocal_remover", "tool_noise_cancellation" -> viewModel.navigateTo(CurrentScreen.AIAudio)
                            else -> viewModel.openInteractiveAITool(tool)
                        }
                    }
                )
            }
        }
    }

    // Interactive AI Tool Dialog (for Cutout, Magic Eraser, Subtitles)
    if (selectedTool != null) {
        InteractiveAIToolDialog(
            tool = selectedTool!!,
            viewModel = viewModel,
            onDismiss = { viewModel.closeInteractiveAITool() }
        )
    }
}

@Composable
private fun AIToolCard(
    tool: AIToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFlagship = tool.id == "tool_video_gen"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(if (isFlagship) 8.dp else 4.dp, RoundedCornerShape(18.dp), ambientColor = if (isFlagship) ApexPurple else Color.Black)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isFlagship) Color(0xFF1F1B38) else ApexDarkCard)
            .border(
                width = if (isFlagship) 1.5.dp else 1.dp,
                brush = if (isFlagship) Brush.horizontalGradient(listOf(ApexPurple, ApexPink, ApexCyan)) else Brush.linearGradient(listOf(ApexBorder, ApexBorder)),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag("ai_tool_item_${tool.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        brush = if (isFlagship) Brush.linearGradient(listOf(ApexPurple, ApexPink)) else Brush.linearGradient(listOf(Color(0xFF221F38), Color(0xFF19172B))),
                        shape = RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = null,
                    tint = if (isFlagship) Color.White else ApexPurple,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tool.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                        color = ApexTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(if (isFlagship) ApexPink.copy(alpha = 0.2f) else ApexCyan.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(tool.badge, color = if (isFlagship) ApexPink else ApexCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = tool.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = ApexTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null,
                tint = if (isFlagship) ApexPink else ApexTextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InteractiveAIToolDialog(
    tool: AIToolItem,
    viewModel: ApexCutViewModel,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isProcessing by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var isCompleted by remember { mutableStateOf(false) }
    var stageText by remember { mutableStateOf("جاهز لتنفيذ العملية الذكية") }

    // Cutout State
    var cutoutMode by remember { mutableStateOf("شفاف (Alpha PNG)") }
    var edgeFeather by remember { mutableFloatStateOf(0.5f) }

    // Magic Eraser State
    var brushSize by remember { mutableFloatStateOf(35f) }
    var eraseTarget by remember { mutableStateOf("الأشخاص والمارة") }

    // Subtitles State
    var subtitleDialect by remember { mutableStateOf("العربية الفصحى (دقة 99%)") }
    var subtitleStyle by remember { mutableStateOf("🟡 تيك توك أصفر ديناميكي") }

    Dialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(ApexDarkCard)
                    .border(1.dp, ApexPurple.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Top Bar inside dialog
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ApexPurpleBg, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(tool.icon, contentDescription = null, tint = ApexPurple, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = tool.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = ApexTextPrimary,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = tool.badge,
                                    color = ApexCyan,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            enabled = !isProcessing,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = ApexTextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Preview Visual Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF141224))
                            .border(1.dp, ApexBorder, RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_template_cyber),
                            contentDescription = "AI Tool Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Subtitle overlay preview if subtitle tool
                        if (tool.id == "tool_auto_subtitles") {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 18.dp)
                                    .background(
                                        if (subtitleStyle.contains("أصفر")) Color.Black.copy(alpha = 0.8f) else Color(0xFF581C87).copy(alpha = 0.85f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "مرحباً بكم في ApexCut Studio ✨",
                                    color = if (subtitleStyle.contains("أصفر")) Color(0xFFFACC15) else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        // Processing overlay
                        if (isProcessing) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.85f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(color = ApexPurple, strokeWidth = 3.dp, modifier = Modifier.size(44.dp))
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(stageText, color = ApexCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        color = ApexCyan,
                                        trackColor = Color(0xFF2E2B4A),
                                        modifier = Modifier.width(140.dp).height(3.dp).clip(RoundedCornerShape(2.dp))
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Specific Tool Options
                    when (tool.id) {
                        "tool_cutout" -> {
                            Text("نوع خلفية العزل المطلوبة:", color = ApexTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val modes = listOf("شفاف (Alpha PNG)", "شاشة خضراء كروما", "استوديو نيون سايبر", "بوكيه ضبابي")
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                modes.forEach { mode ->
                                    val isSelected = cutoutMode == mode
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) ApexPurpleBg else Color(0xFF1B192E))
                                            .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(8.dp))
                                            .clickable { cutoutMode = mode }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(mode, color = if (isSelected) ApexTextPrimary else ApexTextSecondary, fontSize = 11.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("نعومة الحواف وتدريج الشعر: ${(edgeFeather * 100).toInt()}%", color = ApexTextSecondary, fontSize = 11.sp)
                            Slider(
                                value = edgeFeather,
                                onValueChange = { edgeFeather = it },
                                colors = SliderDefaults.colors(thumbColor = ApexPurple, activeTrackColor = ApexPurple)
                            )
                        }

                        "tool_magic_eraser" -> {
                            Text("عنصر المسح المستهدف:", color = ApexTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val targets = listOf("الأشخاص والمارة", "الشعارات والنصوص", "الأسلاك والشوائب")
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                targets.forEach { target ->
                                    val isSelected = eraseTarget == target
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) ApexPurpleBg else Color(0xFF1B192E))
                                            .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(8.dp))
                                            .clickable { eraseTarget = target }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(target, color = if (isSelected) ApexCyan else ApexTextSecondary, fontSize = 11.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("قطر فرشاة المسح الذكية: ${brushSize.toInt()}px", color = ApexTextSecondary, fontSize = 11.sp)
                            Slider(
                                value = brushSize,
                                onValueChange = { brushSize = it },
                                valueRange = 15f..80f,
                                colors = SliderDefaults.colors(thumbColor = ApexPink, activeTrackColor = ApexPink)
                            )
                        }

                        "tool_auto_subtitles" -> {
                            Text("لغة ونموذج التفريغ الصوتي:", color = ApexTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val dialects = listOf("العربية الفصحى (دقة 99%)", "اللهجة الخليجية", "اللهجة المصرية", "اللهجة الشامية")
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                dialects.forEach { dialect ->
                                    val isSelected = subtitleDialect == dialect
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) ApexPurpleBg else Color(0xFF1B192E))
                                            .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(8.dp))
                                            .clickable { subtitleDialect = dialect }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(dialect, color = if (isSelected) ApexCyan else ApexTextSecondary, fontSize = 11.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("نمط النصوص المتحركة (Captions Style):", color = ApexTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val styles = listOf("🟡 تيك توك أصفر ديناميكي", "🟣 نيون سايبر متوهج", "⚪ أبيض كلاسيكي")
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                styles.forEach { st ->
                                    val isSelected = subtitleStyle == st
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) ApexPurpleBg else Color(0xFF1B192E))
                                            .border(1.dp, if (isSelected) ApexPurple else ApexBorder, RoundedCornerShape(8.dp))
                                            .clickable { subtitleStyle = st }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(st, color = if (isSelected) ApexTextPrimary else ApexTextSecondary, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Button
                    Button(
                        onClick = {
                            viewModel.triggerAIAction(tool.title) {
                                isProcessing = true
                                isCompleted = false
                                coroutineScope.launch {
                                    stageText = "تحليل الوسائط والتعرف على العناصر..."
                                    for (i in 1..40) {
                                        delay(20)
                                        progress = i / 100f
                                    }
                                    stageText = "تطبيق الخوارزميات العصبية وإعادة البناء..."
                                    for (i in 41..85) {
                                        delay(20)
                                        progress = i / 100f
                                    }
                                    stageText = "اكتمال المعالجة بدقة فائقة!"
                                    for (i in 86..100) {
                                        delay(15)
                                        progress = i / 100f
                                    }
                                    isProcessing = false
                                    isCompleted = true

                                    // Open in Video or Photo Editor
                                    delay(500)
                                    onDismiss()
                                    viewModel.createNewProject(
                                        title = "${tool.title} AI",
                                        type = if (tool.id == "tool_magic_eraser") "photo" else "video",
                                        ratio = "9:16"
                                    )
                                }
                            }
                        },
                        enabled = !isProcessing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_execute_interactive_ai_tool"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ApexPurple)
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("جاري التنفيذ... ${(progress * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تنفيذ ${tool.title} وبدء المونتاج ✨",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
