package com.example.data.repository

import com.example.data.local.ProjectDao
import com.example.data.local.ProjectEntity
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    suspend fun getProjectById(id: Long): ProjectEntity? = projectDao.getProjectById(id)

    suspend fun createProject(project: ProjectEntity): Long {
        return projectDao.insertProject(project)
    }

    suspend fun updateProject(project: ProjectEntity) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(id: Long) {
        projectDao.deleteProjectById(id)
    }

    suspend fun duplicateProject(project: ProjectEntity): Long {
        val duplicated = project.copy(
            id = 0,
            title = "${project.title} (نسخة)",
            lastModified = System.currentTimeMillis()
        )
        return projectDao.insertProject(duplicated)
    }

    suspend fun seedInitialDataIfEmpty() {
        val count = projectDao.getCount()
        if (count == 0) {
            val sampleProjects = listOf(
                ProjectEntity(
                    title = "ريلز سينمائي نيون #1",
                    projectType = "video",
                    aspectRatio = "9:16",
                    durationText = "00:28",
                    durationSeconds = 28,
                    resolution = "4K 60FPS",
                    clipsCount = 6,
                    thumbnailResName = "img_template_cyber",
                    filterName = "سايبر نيون بنفسجي",
                    textOverlay = "APEX ENERGY",
                    speed = 1.2f,
                    lastModified = System.currentTimeMillis() - 1000 * 60 * 45
                ),
                ProjectEntity(
                    title = "تحسين فيديو 4K HDR",
                    projectType = "ai_enhance",
                    aspectRatio = "16:9",
                    durationText = "01:14",
                    durationSeconds = 74,
                    resolution = "4K Crystal AI",
                    clipsCount = 2,
                    thumbnailResName = "img_ai_enhance_banner",
                    filterName = "Ultra Sharp HDR",
                    textOverlay = "AI 4K REMASTER",
                    lastModified = System.currentTimeMillis() - 1000 * 60 * 180
                ),
                ProjectEntity(
                    title = "تعديل صورة غلاف أزياء",
                    projectType = "photo",
                    aspectRatio = "4:5",
                    durationText = "صورة فائقة الدقة",
                    durationSeconds = 0,
                    resolution = "Pro RAW",
                    clipsCount = 1,
                    thumbnailResName = "ic_apexcut_logo",
                    filterName = "Cyber Fashion Glow",
                    brightness = 0.15f,
                    contrast = 0.25f,
                    saturation = 0.3f,
                    lastModified = System.currentTimeMillis() - 1000 * 60 * 60 * 24
                )
            )
            for (project in sampleProjects) {
                projectDao.insertProject(project)
            }
        }
    }
}
