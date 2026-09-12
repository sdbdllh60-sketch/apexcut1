package com.example.model

data class VideoClip(
    val id: String,
    val title: String,
    val durationSeconds: Float,
    val thumbnailResName: String,
    val filterName: String = "عادي",
    val speed: Float = 1.0f,
    val volume: Float = 1.0f,
    val isMuted: Boolean = false
)

data class FilterPreset(
    val id: String,
    val name: String,
    val category: String,
    val brightness: Float = 0f,
    val contrast: Float = 0f,
    val saturation: Float = 0f,
    val hue: Float = 0f,
    val glowColorHex: Long = 0xFF8B5CF6
)

object FilterPresetsData {
    val filters = listOf(
        FilterPreset("f_orig", "الأصل", "طبيعي", 0f, 0f, 0f, 0f, 0xFF8B5CF6),
        FilterPreset("f_cyber", "سايبر بنفسجي", "نيون", 0.1f, 0.35f, 0.4f, 280f, 0xFF9333EA),
        FilterPreset("f_electric", "أزرق نيون", "سينمائي", 0.05f, 0.4f, 0.45f, 210f, 0xFF3B82F6),
        FilterPreset("f_cinema", "هوليوود الذهبي", "سينمائي", 0.08f, 0.25f, 0.2f, 40f, 0xFFF59E0B),
        FilterPreset("f_noir", "نوار ملكي", "كلاسيك", -0.05f, 0.5f, -1.0f, 0f, 0xFFE2E8F0),
        FilterPreset("f_sunset", "غروب أرجواني", "أجواء", 0.12f, 0.2f, 0.5f, 320f, 0xFFEC4899),
        FilterPreset("f_matrix", "ماتريكس سايبر", "مؤثرات", 0.05f, 0.3f, 0.3f, 150f, 0xFF10B981)
    )
}

data class ArabicVoiceOption(
    val id: String,
    val name: String,
    val style: String,
    val gender: String,
    val previewAudioHint: String
)

object ArabicVoiceData {
    val voices = listOf(
        ArabicVoiceOption("v1", "طارق", "وثائقي فخم وعميق", "ذكر", "صوت سينمائي للأفلام الوثائقية"),
        ArabicVoiceOption("v2", "سارة", "حماسي إعلاني وعصري", "أنثى", "نبرة نشيطة لإعلانات السوشيال ميديا"),
        ArabicVoiceOption("v3", "فيصل", "هادئ ومريح (بودكاست)", "ذكر", "أسلوب روائي دافئ ومؤثر"),
        ArabicVoiceOption("v4", "نور", "أخبار وتقارير سريعة", "أنثى", "مخارج حروف دقيقة ورسمية")
    )
}
