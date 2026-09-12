package com.example.model

data class TemplateItem(
    val id: String,
    val title: String,
    val category: String, // "ريلز وتيك توك", "سينمائي", "إيقاع نيون", "سفر وفلوج", "بودكاست"
    val durationText: String,
    val clipsRequired: Int,
    val musicTitle: String,
    val bpm: Int,
    val thumbnailResName: String,
    val tag: String = "شائع",
    val description: String
)

object TemplateData {
    val sampleTemplates = listOf(
        TemplateItem(
            id = "tpl_cyber_beat",
            title = "انتقالات نيون سايبربانك",
            category = "إيقاع نيون",
            durationText = "00:15",
            clipsRequired = 5,
            musicTitle = "Apex Cyber Glitch 140BPM",
            bpm = 140,
            thumbnailResName = "img_template_cyber",
            tag = "الأكثر استخداماً",
            description = "تزامن إيقاعي فوري مع مؤثرات فلاش نيون بنفسجية واهتزاز سينمائي رائع"
        ),
        TemplateItem(
            id = "tpl_cinema_4k",
            title = "إنترو سينمائي فخم 4K",
            category = "سينمائي",
            durationText = "00:22",
            clipsRequired = 3,
            musicTitle = "Deep Ambient Euphoria",
            bpm = 95,
            thumbnailResName = "img_ai_enhance_banner",
            tag = "جودة 4K",
            description = "ألوان هوليوودية متدرجة ونصوص ثلاثية الأبعاد بظلال متوهجة"
        ),
        TemplateItem(
            id = "tpl_reels_fast",
            title = "ريلز سريع للتريند",
            category = "ريلز وتيك توك",
            durationText = "00:12",
            clipsRequired = 8,
            musicTitle = "Speed Trap Drop",
            bpm = 160,
            thumbnailResName = "img_template_cyber",
            tag = "تريند تيك توك",
            description = "تقطيع سريع متطابق مع إيقاع الطبول ووميض إضاءة احترافي"
        ),
        TemplateItem(
            id = "tpl_vlog_aesthetic",
            title = "فلوج يوميات هادئ وبسيط",
            category = "سفر وفلوج",
            durationText = "00:30",
            clipsRequired = 6,
            musicTitle = "Sunset Lo-Fi Chill",
            bpm = 85,
            thumbnailResName = "ic_apexcut_logo",
            tag = "أنيق ومريح",
            description = "تدرجات ألوان دافئة وخطوط عربية عصرية مع تأثيرات فيلم كلاسيكي"
        )
    )
}
