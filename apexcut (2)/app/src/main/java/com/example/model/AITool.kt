package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.ui.graphics.vector.ImageVector

data class AIToolItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val badge: String = "AI 2.0",
    val category: String, // "quality", "audio", "effects", "text", "video"
    val isHot: Boolean = false
)

object AIToolsData {
    val allTools = listOf(
        AIToolItem(
            id = "tool_video_gen",
            title = "توليد الفيديوهات بالذكاء الاصطناعي (Text to Video)",
            subtitle = "حوّل أفكارك وسيناريوهاتك إلى فيديوهات سينمائية بدقة 4K بنقرة واحدة",
            icon = Icons.Default.MovieFilter,
            badge = "جديد AI 3.0",
            category = "video",
            isHot = true
        ),
        AIToolItem(
            id = "tool_4k_upscale",
            title = "تحسين جودة الفيديو AI",
            subtitle = "زيادة وضوح وتفاصيل الفيديو لـ 4K وتقليل التشويش وتحسين الألوان",
            icon = Icons.Default.HighQuality,
            badge = "فائق السرعة",
            category = "quality",
            isHot = true
        ),
        AIToolItem(
            id = "tool_cutout",
            title = "القص الذكي وعزل الخلفية",
            subtitle = "إزالة خلفية الفيديو أو الصور بنقرة واحدة بدون شاشة خضراء",
            icon = Icons.Default.ContentCut,
            badge = "تلقائي",
            category = "effects",
            isHot = true
        ),
        AIToolItem(
            id = "tool_vocal_remover",
            title = "فصل الصوت عن الموسيقى",
            subtitle = "عزل صوت المغني أو استخراج الموسيقى التصويرية بدقة نقية",
            icon = Icons.Default.GraphicEq,
            badge = "ستوديو",
            category = "audio",
            isHot = true
        ),
        AIToolItem(
            id = "tool_voiceover",
            title = "التعليق الصوتي AI",
            subtitle = "تحويل النصوص إلى تعليق صوتي سينمائي مدمج على الفيديو",
            icon = Icons.Default.RecordVoiceOver,
            badge = "أصوات طبيعية",
            category = "audio",
            isHot = true
        ),
        AIToolItem(
            id = "tool_auto_subtitles",
            title = "التفريغ النصي التلقائي",
            subtitle = "توليد نصوص متحركة متزامنة مع الكلام باللغة العربية",
            icon = Icons.Default.Subtitles,
            badge = "دقة 99%",
            category = "text",
            isHot = true
        ),
        AIToolItem(
            id = "tool_magic_eraser",
            title = "الممحاة السحرية الذكية",
            subtitle = "إزالة الأشخاص والأشياء غير المرغوب فيها من الفيديو والصور",
            icon = Icons.Default.BlurOn,
            badge = "ذكي",
            category = "effects"
        ),
        AIToolItem(
            id = "tool_noise_cancellation",
            title = "عزل الضوضاء والرياح",
            subtitle = "تنقية الصوت الميكروفوني من أصوات الهواء والضجيج المحيط",
            icon = Icons.Default.Mic,
            badge = "نقاء كريستالي",
            category = "audio"
        ),
        AIToolItem(
            id = "tool_smart_colorist",
            title = "التلوين السينمائي التلقائي",
            subtitle = "تطبيق درجات ألوان أفلام هوليوود المتطابقة مع الإضاءة",
            icon = Icons.Default.AutoAwesome,
            badge = "HDR AI",
            category = "quality"
        )
    )
}
