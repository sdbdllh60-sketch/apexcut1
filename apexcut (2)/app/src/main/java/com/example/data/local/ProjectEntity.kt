package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val projectType: String, // "video", "photo", "ai_enhance", "template"
    val aspectRatio: String = "9:16", // "9:16", "16:9", "1:1", "4:5"
    val durationText: String = "00:15",
    val durationSeconds: Int = 15,
    val resolution: String = "4K 60FPS",
    val clipsCount: Int = 1,
    val thumbnailResName: String = "img_template_cyber",
    val thumbnailUri: String? = null,
    val lastModified: Long = System.currentTimeMillis(),
    val filterName: String = "سينمائي نيون",
    val brightness: Float = 0f,
    val contrast: Float = 0f,
    val saturation: Float = 0f,
    val speed: Float = 1.0f,
    val musicTrackName: String = "Apex Cinematic Pulse",
    val textOverlay: String = "ApexCut Pro",
    val isFavorite: Boolean = false
)
