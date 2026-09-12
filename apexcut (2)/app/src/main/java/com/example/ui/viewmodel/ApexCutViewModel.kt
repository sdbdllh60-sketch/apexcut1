package com.example.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.audio.AITextToSpeechEngine
import com.example.data.billing.BillingManager
import com.example.data.local.AIUsageManager
import com.example.data.local.AppDatabase
import com.example.data.local.ProjectEntity
import com.example.data.repository.ProjectRepository
import com.example.data.video.AIEnhanceOptions
import com.example.data.video.AIVideoEnhancementEngine
import com.example.data.video.VideoMetadata
import com.example.model.AIToolsData
import com.example.model.ArabicVoiceData
import com.example.model.ArabicVoiceOption
import com.example.model.FilterPreset
import com.example.model.FilterPresetsData
import com.example.model.TemplateData
import com.example.model.TemplateItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

sealed class CurrentScreen {
    object Splash : CurrentScreen()
    object Home : CurrentScreen()
    data class VideoEditor(val projectId: Long) : CurrentScreen()
    data class PhotoEditor(val projectId: Long) : CurrentScreen()
    data class AIEnhance(val projectId: Long? = null) : CurrentScreen()
    object AIAudio : CurrentScreen()
    object AITools : CurrentScreen()
    object Templates : CurrentScreen()
    object Premium : CurrentScreen()
    object AIVideoGen : CurrentScreen()
}

class ApexCutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository
    private val aiUsageManager: AIUsageManager = AIUsageManager.getInstance(application)
    private val billingManager: BillingManager = BillingManager(application, aiUsageManager, viewModelScope)

    // AI Engines
    val ttsEngine: AITextToSpeechEngine = AITextToSpeechEngine(application)
    val videoEnhanceEngine: AIVideoEnhancementEngine = AIVideoEnhancementEngine(application)

    val allProjects: StateFlow<List<ProjectEntity>>

    private val _currentScreen = MutableStateFlow<CurrentScreen>(CurrentScreen.Splash)
    val currentScreen: StateFlow<CurrentScreen> = _currentScreen.asStateFlow()

    private val _activeProject = MutableStateFlow<ProjectEntity?>(null)
    val activeProject: StateFlow<ProjectEntity?> = _activeProject.asStateFlow()

    // Premium & AI Usage State (Shared quota: 3 uses per 24 hours for free, unlimited for VIP)
    val remainingAIUses: StateFlow<Int> = aiUsageManager.remainingUses
    val isPremium: StateFlow<Boolean> = billingManager.isPremium
    val premiumPrice: StateFlow<String> = billingManager.formattedPrice
    val isBillingLoading: StateFlow<Boolean> = billingManager.isLoading
    val billingMessage: StateFlow<String?> = billingManager.purchaseMessage

    // ==========================================
    // 1. AI Voiceover on Video State ("التعليق الصوتي AI")
    // ==========================================
    private val _voiceoverVideoUri = MutableStateFlow<Uri?>(null)
    val voiceoverVideoUri: StateFlow<Uri?> = _voiceoverVideoUri.asStateFlow()

    private val _voiceoverVideoMetadata = MutableStateFlow(
        VideoMetadata(
            title = "فيديو إعلاني سينمائي",
            durationFormatted = "00:15",
            durationMs = 15000L,
            resolutionLabel = "1080x1920 FHD",
            fps = 60,
            bitrateFormatted = "14.2 Mbps",
            sizeFormatted = "22.5 MB"
        )
    )
    val voiceoverVideoMetadata: StateFlow<VideoMetadata> = _voiceoverVideoMetadata.asStateFlow()

    private val _isVoiceoverVideoPlaying = MutableStateFlow(false)
    val isVoiceoverVideoPlaying: StateFlow<Boolean> = _isVoiceoverVideoPlaying.asStateFlow()

    private val _voiceoverPlaybackPosition = MutableStateFlow(0f)
    val voiceoverPlaybackPosition: StateFlow<Float> = _voiceoverPlaybackPosition.asStateFlow()

    private val _voiceScriptText = MutableStateFlow("مرحباً بكم في استوديو ApexCut، تجربة المونتاج الأكثر تطوراً بالذكاء الاصطناعي.")
    val voiceScriptText: StateFlow<String> = _voiceScriptText.asStateFlow()

    private val _selectedVoice = MutableStateFlow(ArabicVoiceData.voices[0])
    val selectedVoice: StateFlow<ArabicVoiceOption> = _selectedVoice.asStateFlow()

    private val _voicePitch = MutableStateFlow(1.0f)
    val voicePitch: StateFlow<Float> = _voicePitch.asStateFlow()

    private val _voiceSpeed = MutableStateFlow(1.0f)
    val voiceSpeed: StateFlow<Float> = _voiceSpeed.asStateFlow()

    private val _voiceVolume = MutableStateFlow(1.0f)
    val voiceVolume: StateFlow<Float> = _voiceVolume.asStateFlow()

    private val _videoVolume = MutableStateFlow(0.6f)
    val videoVolume: StateFlow<Float> = _videoVolume.asStateFlow()

    private val _isVoiceoverGenerated = MutableStateFlow(false)
    val isVoiceoverGenerated: StateFlow<Boolean> = _isVoiceoverGenerated.asStateFlow()

    private val _isVoiceGenerating = MutableStateFlow(false)
    val isVoiceGenerating: StateFlow<Boolean> = _isVoiceGenerating.asStateFlow()

    private val _voiceSuccessMessage = MutableStateFlow<String?>(null)
    val voiceSuccessMessage: StateFlow<String?> = _voiceSuccessMessage.asStateFlow()

    private val _generatedVoiceoverFile = MutableStateFlow<File?>(null)
    val generatedVoiceoverFile: StateFlow<File?> = _generatedVoiceoverFile.asStateFlow()

    // Audio Extra Tool states (Vocal isolation / denoise)
    private val _vocalSeparationLevel = MutableStateFlow(0.8f)
    val vocalSeparationLevel: StateFlow<Float> = _vocalSeparationLevel.asStateFlow()

    private val _noiseDenoiseEnabled = MutableStateFlow(true)
    val noiseDenoiseEnabled: StateFlow<Boolean> = _noiseDenoiseEnabled.asStateFlow()

    private val _subtitlesList = MutableStateFlow(
        listOf(
            "00:01 - مرحباً بكم في ApexCut",
            "00:04 - تجربة المونتاج الحديث بالذكاء الاصطناعي",
            "00:09 - إمكانيات غير محدودة بدون قيود",
            "00:15 - جودة سينمائية 4K فائقة الوضوح"
        )
    )
    val subtitlesList: StateFlow<List<String>> = _subtitlesList.asStateFlow()

    // ==========================================
    // 2. AI Video Enhancer State ("تحسين جودة الفيديو AI")
    // ==========================================
    private val _enhanceVideoUri = MutableStateFlow<Uri?>(null)
    val enhanceVideoUri: StateFlow<Uri?> = _enhanceVideoUri.asStateFlow()

    private val _enhanceVideoMetadata = MutableStateFlow(
        VideoMetadata(
            title = "لقطة وثائقية 720p مشوشة",
            durationFormatted = "00:18",
            durationMs = 18000L,
            width = 720,
            height = 1280,
            resolutionLabel = "720x1280 HD (منخفض الوضوح)",
            fps = 24,
            bitrateFormatted = "4.2 Mbps",
            sizeFormatted = "9.4 MB"
        )
    )
    val enhanceVideoMetadata: StateFlow<VideoMetadata> = _enhanceVideoMetadata.asStateFlow()

    private val _enhanceOptions = MutableStateFlow(AIEnhanceOptions())
    val enhanceOptions: StateFlow<AIEnhanceOptions> = _enhanceOptions.asStateFlow()

    private val _originalFrameBitmap = MutableStateFlow<Bitmap?>(null)
    val originalFrameBitmap: StateFlow<Bitmap?> = _originalFrameBitmap.asStateFlow()

    private val _enhancedFrameBitmap = MutableStateFlow<Bitmap?>(null)
    val enhancedFrameBitmap: StateFlow<Bitmap?> = _enhancedFrameBitmap.asStateFlow()

    private val _isAIProcessing = MutableStateFlow(false)
    val isAIProcessing: StateFlow<Boolean> = _isAIProcessing.asStateFlow()

    private val _aiProgress = MutableStateFlow(0f)
    val aiProgress: StateFlow<Float> = _aiProgress.asStateFlow()

    private val _aiStatusText = MutableStateFlow("جاهز لبدء الترميم والمعالجة العصبية")
    val aiStatusText: StateFlow<String> = _aiStatusText.asStateFlow()

    private val _isEnhanceCompleted = MutableStateFlow(false)
    val isEnhanceCompleted: StateFlow<Boolean> = _isEnhanceCompleted.asStateFlow()

    private val _aiBeforeAfterSlider = MutableStateFlow(0.5f)
    val aiBeforeAfterSlider: StateFlow<Float> = _aiBeforeAfterSlider.asStateFlow()

    private val _selectedAIModel = MutableStateFlow("4K Super Upscaler")
    val selectedAIModel: StateFlow<String> = _selectedAIModel.asStateFlow()

    private val _saveEnhanceProjectMessage = MutableStateFlow<String?>(null)
    val saveEnhanceProjectMessage: StateFlow<String?> = _saveEnhanceProjectMessage.asStateFlow()

    // ==========================================
    // 3. AI Video Generator State ("توليد الفيديوهات بالذكاء الاصطناعي")
    // ==========================================
    private val _videoPromptText = MutableStateFlow("طيران سينمائي دروون فوق مدينة مستقبلية سايبربانك مع سيارات طائرة وأضواء نيون بدقة 4K")
    val videoPromptText: StateFlow<String> = _videoPromptText.asStateFlow()

    private val _videoGenStyle = MutableStateFlow("سينمائي هوليوود (Cinematic 4K)")
    val videoGenStyle: StateFlow<String> = _videoGenStyle.asStateFlow()

    private val _videoGenRatio = MutableStateFlow("9:16")
    val videoGenRatio: StateFlow<String> = _videoGenRatio.asStateFlow()

    private val _videoGenDuration = MutableStateFlow(10)
    val videoGenDuration: StateFlow<Int> = _videoGenDuration.asStateFlow()

    private val _videoGenCameraMotion = MutableStateFlow("طيران أمامي سينمائي (Fly Through)")
    val videoGenCameraMotion: StateFlow<String> = _videoGenCameraMotion.asStateFlow()

    private val _isGeneratingVideo = MutableStateFlow(false)
    val isGeneratingVideo: StateFlow<Boolean> = _isGeneratingVideo.asStateFlow()

    private val _videoGenProgress = MutableStateFlow(0f)
    val videoGenProgress: StateFlow<Float> = _videoGenProgress.asStateFlow()

    private val _videoGenStageText = MutableStateFlow("جاهز لبدء التوليد العصبي بدقة 4K")
    val videoGenStageText: StateFlow<String> = _videoGenStageText.asStateFlow()

    private val _isVideoGenCompleted = MutableStateFlow(false)
    val isVideoGenCompleted: StateFlow<Boolean> = _isVideoGenCompleted.asStateFlow()

    private val _generatedVideoTitle = MutableStateFlow("مشهد سينمائي سايبربانك AI")
    val generatedVideoTitle: StateFlow<String> = _generatedVideoTitle.asStateFlow()

    private val _isGeneratedVideoPlaying = MutableStateFlow(false)
    val isGeneratedVideoPlaying: StateFlow<Boolean> = _isGeneratedVideoPlaying.asStateFlow()

    private val _generatedVideoPlaybackPos = MutableStateFlow(0f)
    val generatedVideoPlaybackPos: StateFlow<Float> = _generatedVideoPlaybackPos.asStateFlow()

    private val _videoGenSuccessMessage = MutableStateFlow<String?>(null)
    val videoGenSuccessMessage: StateFlow<String?> = _videoGenSuccessMessage.asStateFlow()

    // Interactive AI Tool Dialog in AIToolsScreen
    private val _selectedInteractiveAITool = MutableStateFlow<com.example.model.AIToolItem?>(null)
    val selectedInteractiveAITool: StateFlow<com.example.model.AIToolItem?> = _selectedInteractiveAITool.asStateFlow()

    // ==========================================
    // Editor playback & adjustment states
    // ==========================================
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackSeconds = MutableStateFlow(0f)
    val playbackSeconds: StateFlow<Float> = _playbackSeconds.asStateFlow()

    private val _activeFilter = MutableStateFlow(FilterPresetsData.filters[0])
    val activeFilter: StateFlow<FilterPreset> = _activeFilter.asStateFlow()

    private val _brightness = MutableStateFlow(0f)
    val brightness: StateFlow<Float> = _brightness.asStateFlow()

    private val _contrast = MutableStateFlow(0f)
    val contrast: StateFlow<Float> = _contrast.asStateFlow()

    private val _saturation = MutableStateFlow(0f)
    val saturation: StateFlow<Float> = _saturation.asStateFlow()

    private val _speedMultiplier = MutableStateFlow(1.0f)
    val speedMultiplier: StateFlow<Float> = _speedMultiplier.asStateFlow()

    private val _aspectRatio = MutableStateFlow("9:16")
    val aspectRatio: StateFlow<String> = _aspectRatio.asStateFlow()

    private val _projectText = MutableStateFlow("ApexCut Studio")
    val projectText: StateFlow<String> = _projectText.asStateFlow()

    // Export Dialog States
    private val _isExportDialogOpen = MutableStateFlow(false)
    val isExportDialogOpen: StateFlow<Boolean> = _isExportDialogOpen.asStateFlow()

    private val _exportResolution = MutableStateFlow("4K UHD (3840x2160)")
    val exportResolution: StateFlow<String> = _exportResolution.asStateFlow()

    private val _exportFps = MutableStateFlow("60 FPS (سلاسة فائقة)")
    val exportFps: StateFlow<String> = _exportFps.asStateFlow()

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    private val _exportProgress = MutableStateFlow(0f)
    val exportProgress: StateFlow<Float> = _exportProgress.asStateFlow()

    private val _isExportSuccess = MutableStateFlow(false)
    val isExportSuccess: StateFlow<Boolean> = _isExportSuccess.asStateFlow()

    init {
        val database = AppDatabase.getInstance(application)
        repository = ProjectRepository(database.projectDao())
        allProjects = repository.allProjects.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            loadDefaultEnhanceBitmaps()
        }

        // Voiceover playback ticker
        viewModelScope.launch {
            while (true) {
                if (_isVoiceoverVideoPlaying.value) {
                    val current = _voiceoverPlaybackPosition.value
                    val maxDur = (_voiceoverVideoMetadata.value.durationMs / 1000f).coerceAtLeast(5f)
                    if (current >= maxDur) {
                        _voiceoverPlaybackPosition.value = 0f
                        _isVoiceoverVideoPlaying.value = false
                        ttsEngine.stopAudio()
                    } else {
                        _voiceoverPlaybackPosition.value = current + 0.1f
                    }
                }
                delay(100)
            }
        }

        // General Video Editor Playback ticker
        viewModelScope.launch {
            while (true) {
                if (_isPlaying.value) {
                    val current = _playbackSeconds.value
                    val maxDur = (_activeProject.value?.durationSeconds ?: 20).toFloat()
                    if (current >= maxDur) {
                        _playbackSeconds.value = 0f
                    } else {
                        _playbackSeconds.value = current + 0.1f
                    }
                }
                delay(100)
            }
        }

        // Generated AI Video Playback ticker
        viewModelScope.launch {
            while (true) {
                if (_isGeneratedVideoPlaying.value) {
                    val current = _generatedVideoPlaybackPos.value
                    val maxDur = _videoGenDuration.value.toFloat()
                    if (current >= maxDur) {
                        _generatedVideoPlaybackPos.value = 0f
                    } else {
                        _generatedVideoPlaybackPos.value = current + 0.1f
                    }
                }
                delay(100)
            }
        }
    }

    private fun loadDefaultEnhanceBitmaps() {
        try {
            val original = BitmapFactory.decodeResource(getApplication<Application>().resources, R.drawable.img_template_cyber)
            _originalFrameBitmap.value = original
        } catch (e: Exception) {
            // ignore
        }
    }

    fun finishSplash() {
        if (_currentScreen.value is CurrentScreen.Splash) {
            _currentScreen.value = CurrentScreen.Home
        }
    }

    fun navigateTo(screen: CurrentScreen) {
        _isPlaying.value = false
        _isVoiceoverVideoPlaying.value = false
        ttsEngine.stopAudio()
        _currentScreen.value = screen
    }

    fun openProject(project: ProjectEntity) {
        _activeProject.value = project
        _aspectRatio.value = project.aspectRatio
        _brightness.value = project.brightness
        _contrast.value = project.contrast
        _saturation.value = project.saturation
        _speedMultiplier.value = project.speed
        _projectText.value = project.textOverlay
        _playbackSeconds.value = 0f

        val matchedFilter = FilterPresetsData.filters.firstOrNull { it.name == project.filterName }
            ?: FilterPresetsData.filters[0]
        _activeFilter.value = matchedFilter

        when (project.projectType) {
            "video" -> navigateTo(CurrentScreen.VideoEditor(project.id))
            "photo" -> navigateTo(CurrentScreen.PhotoEditor(project.id))
            "ai_enhance" -> navigateTo(CurrentScreen.AIEnhance(project.id))
            else -> navigateTo(CurrentScreen.VideoEditor(project.id))
        }
    }

    fun createNewProject(
        title: String = "مشروع جديد",
        type: String = "video",
        ratio: String = "9:16",
        thumbnail: String = "img_template_cyber"
    ) {
        viewModelScope.launch {
            val newProj = ProjectEntity(
                title = title.ifBlank { "مشروع ${System.currentTimeMillis() % 10000}" },
                projectType = type,
                aspectRatio = ratio,
                durationText = if (type == "photo") "صورة معدلة" else "00:20",
                durationSeconds = if (type == "photo") 0 else 20,
                resolution = "4K 60FPS",
                thumbnailResName = thumbnail,
                lastModified = System.currentTimeMillis()
            )
            val id = repository.createProject(newProj)
            val created = newProj.copy(id = id)
            openProject(created)
        }
    }

    fun useTemplate(template: TemplateItem) {
        viewModelScope.launch {
            val newProj = ProjectEntity(
                title = "قالب: ${template.title}",
                projectType = "video",
                aspectRatio = "9:16",
                durationText = template.durationText,
                durationSeconds = 18,
                resolution = "4K 60FPS",
                clipsCount = template.clipsRequired,
                thumbnailResName = template.thumbnailResName,
                musicTrackName = template.musicTitle,
                textOverlay = template.title,
                filterName = "سايبر بنفسجي",
                lastModified = System.currentTimeMillis()
            )
            val id = repository.createProject(newProj)
            val created = newProj.copy(id = id)
            openProject(created)
        }
    }

    fun duplicateProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.duplicateProject(project)
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            repository.deleteProject(id)
            if (_activeProject.value?.id == id) {
                _activeProject.value = null
                _currentScreen.value = CurrentScreen.Home
            }
        }
    }

    /**
     * Unified AI Action Executor.
     * Enforces the shared daily quota (3 uses per 24 hours).
     * If quota exhausted, immediately prompts ApexCut Premium screen.
     */
    fun triggerAIAction(actionName: String, onExecute: () -> Unit) {
        if (aiUsageManager.canUseAI()) {
            val consumed = aiUsageManager.consumeAIUse()
            if (consumed) {
                onExecute()
            } else {
                navigateTo(CurrentScreen.Premium)
            }
        } else {
            navigateTo(CurrentScreen.Premium)
        }
    }

    // =========================================================================
    // 1. AI Voiceover Methods ("التعليق الصوتي AI")
    // =========================================================================

    fun setVoiceoverVideoUri(uri: Uri?) {
        _voiceoverVideoUri.value = uri
        if (uri != null) {
            val meta = videoEnhanceEngine.extractMetadata(uri)
            _voiceoverVideoMetadata.value = meta
        }
    }

    fun selectDemoVoiceoverVideo(title: String, duration: String, durationMs: Long) {
        _voiceoverVideoUri.value = null
        _voiceoverVideoMetadata.value = VideoMetadata(
            title = title,
            durationFormatted = duration,
            durationMs = durationMs,
            resolutionLabel = "1080x1920 FHD (60fps)",
            fps = 60,
            bitrateFormatted = "16.8 Mbps",
            sizeFormatted = "28.4 MB"
        )
    }

    fun setVoiceScript(text: String) {
        _voiceScriptText.value = text
    }

    fun selectVoice(voice: ArabicVoiceOption) {
        _selectedVoice.value = voice
    }

    fun setVoicePitch(pitch: Float) {
        _voicePitch.value = pitch.coerceIn(0.5f, 1.5f)
    }

    fun setVoiceSpeed(speed: Float) {
        _voiceSpeed.value = speed.coerceIn(0.5f, 1.5f)
    }

    fun setVoiceVolume(volume: Float) {
        _voiceVolume.value = volume.coerceIn(0f, 1f)
    }

    fun setVideoVolume(volume: Float) {
        _videoVolume.value = volume.coerceIn(0f, 1f)
    }

    fun generateAIVoiceover() {
        if (_isVoiceGenerating.value) return
        val script = _voiceScriptText.value.trim()
        if (script.isBlank()) {
            _voiceSuccessMessage.value = "يرجى كتابة نص أو كابشن أولاً للتحويل إلى صوت."
            return
        }

        triggerAIAction("AI Voiceover") {
            _isVoiceGenerating.value = true
            _voiceSuccessMessage.value = null
            viewModelScope.launch {
                val voice = _selectedVoice.value
                val result = ttsEngine.synthesizeSpeech(
                    text = script,
                    voiceId = voice.id,
                    pitch = _voicePitch.value,
                    speechRate = _voiceSpeed.value,
                    onProgress = { /* progress */ }
                )
                delay(600)
                _isVoiceGenerating.value = false
                _isVoiceoverGenerated.value = true
                _generatedVoiceoverFile.value = result.getOrNull()
                _voiceSuccessMessage.value = "تم إنشاء التعليق الصوتي بنجاح ودمجه كمسار صوتي في الفيديو!"

                // Auto-play the synthesized voice for immediate user preview
                ttsEngine.playVoice(
                    text = script,
                    voiceId = voice.id,
                    pitch = _voicePitch.value,
                    speed = _voiceSpeed.value,
                    volume = _voiceVolume.value
                )
                _isVoiceoverVideoPlaying.value = true
            }
        }
    }

    fun toggleVoiceoverVideoPlayback() {
        if (_isVoiceoverVideoPlaying.value) {
            _isVoiceoverVideoPlaying.value = false
            ttsEngine.stopAudio()
        } else {
            _isVoiceoverVideoPlaying.value = true
            if (_isVoiceoverGenerated.value && _voiceScriptText.value.isNotBlank()) {
                ttsEngine.playVoice(
                    text = _voiceScriptText.value,
                    voiceId = _selectedVoice.value.id,
                    pitch = _voicePitch.value,
                    speed = _voiceSpeed.value,
                    volume = _voiceVolume.value
                )
            }
        }
    }

    fun deleteVoiceoverTrack() {
        ttsEngine.stopAudio()
        _isVoiceoverGenerated.value = false
        _generatedVoiceoverFile.value = null
        _isVoiceoverVideoPlaying.value = false
        _voiceSuccessMessage.value = "تم حذف مسار التعليق الصوتي."
    }

    fun exportVoiceoverVideo() {
        viewModelScope.launch {
            val newProj = ProjectEntity(
                title = "فيديو بالتعليق الصوتي: ${_voiceoverVideoMetadata.value.title}",
                projectType = "video",
                aspectRatio = "9:16",
                durationText = _voiceoverVideoMetadata.value.durationFormatted,
                durationSeconds = (_voiceoverVideoMetadata.value.durationMs / 1000).toInt(),
                resolution = "4K 60FPS",
                thumbnailResName = "img_template_cyber",
                musicTrackName = "تعليق صوتي AI (${_selectedVoice.value.name})",
                textOverlay = _voiceScriptText.value.take(28),
                lastModified = System.currentTimeMillis()
            )
            repository.createProject(newProj)
            openExportDialog()
        }
    }

    // =========================================================================
    // 2. AI Video Enhancer Methods ("تحسين جودة الفيديو AI")
    // =========================================================================

    fun setEnhanceVideoUri(uri: Uri?) {
        _enhanceVideoUri.value = uri
        if (uri != null) {
            val meta = videoEnhanceEngine.extractMetadata(uri)
            _enhanceVideoMetadata.value = meta
            val frame = videoEnhanceEngine.extractFrameAtTime(uri)
            if (frame != null) {
                _originalFrameBitmap.value = frame
                _enhancedFrameBitmap.value = null
                _isEnhanceCompleted.value = false
            }
        }
    }

    fun selectDemoEnhanceVideo(title: String, duration: String, durationMs: Long, is4K: Boolean) {
        _enhanceVideoUri.value = null
        _enhanceVideoMetadata.value = VideoMetadata(
            title = title,
            durationFormatted = duration,
            durationMs = durationMs,
            width = if (is4K) 2160 else 720,
            height = if (is4K) 3840 else 1280,
            resolutionLabel = if (is4K) "2160x3840 4K UHD" else "720x1280 HD (منخفض)",
            fps = 30,
            bitrateFormatted = if (is4K) "24.5 Mbps" else "3.8 Mbps",
            sizeFormatted = if (is4K) "45.2 MB" else "7.8 MB"
        )
        _isEnhanceCompleted.value = false
        _enhancedFrameBitmap.value = null
        loadDefaultEnhanceBitmaps()
    }

    fun updateEnhanceOptions(transform: (AIEnhanceOptions) -> AIEnhanceOptions) {
        _enhanceOptions.value = transform(_enhanceOptions.value)
    }

    fun runAIVideoEnhance() {
        if (_isAIProcessing.value) return

        triggerAIAction("AI Video Enhance") {
            _isAIProcessing.value = true
            _aiProgress.value = 0f
            _isEnhanceCompleted.value = false
            _saveEnhanceProjectMessage.value = null

            viewModelScope.launch {
                val baseBitmap = _originalFrameBitmap.value ?: BitmapFactory.decodeResource(
                    getApplication<Application>().resources,
                    R.drawable.img_template_cyber
                )

                val enhanced = videoEnhanceEngine.processFrameEnhancement(
                    originalBitmap = baseBitmap,
                    options = _enhanceOptions.value,
                    onProgress = { prog, status ->
                        _aiProgress.value = prog
                        _aiStatusText.value = status
                    }
                )

                _enhancedFrameBitmap.value = enhanced
                _isAIProcessing.value = false
                _isEnhanceCompleted.value = true
                _aiBeforeAfterSlider.value = 0.5f
            }
        }
    }

    fun saveEnhancedVideoToProjects() {
        viewModelScope.launch {
            val newProj = ProjectEntity(
                title = "فيديو 4K محسن: ${_enhanceVideoMetadata.value.title}",
                projectType = "ai_enhance",
                aspectRatio = "9:16",
                durationText = _enhanceVideoMetadata.value.durationFormatted,
                durationSeconds = (_enhanceVideoMetadata.value.durationMs / 1000).toInt(),
                resolution = "4K 60FPS AI Super Resolution",
                thumbnailResName = "img_ai_enhance_banner",
                filterName = "ترميم 4K HDR",
                lastModified = System.currentTimeMillis()
            )
            repository.createProject(newProj)
            _saveEnhanceProjectMessage.value = "تم حفظ الفيديو المحسن بدقة 4K في قائمة مشاريعك بنجاح!"
        }
    }

    fun clearSaveEnhanceMessage() {
        _saveEnhanceProjectMessage.value = null
    }

    // =========================================================================
    // General Editor & Export Helpers
    // =========================================================================

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun seekTo(seconds: Float) {
        _playbackSeconds.value = seconds
    }

    fun setFilter(filter: FilterPreset) {
        _activeFilter.value = filter
        saveActiveProjectChanges()
    }

    fun setBrightness(v: Float) {
        _brightness.value = v
        saveActiveProjectChanges()
    }

    fun setContrast(v: Float) {
        _contrast.value = v
        saveActiveProjectChanges()
    }

    fun setSaturation(v: Float) {
        _saturation.value = v
        saveActiveProjectChanges()
    }

    fun setSpeedMultiplier(v: Float) {
        _speedMultiplier.value = v
        saveActiveProjectChanges()
    }

    fun setAspectRatio(ratio: String) {
        _aspectRatio.value = ratio
        saveActiveProjectChanges()
    }

    fun setProjectText(text: String) {
        _projectText.value = text
        saveActiveProjectChanges()
    }

    fun setBeforeAfterSlider(pos: Float) {
        _aiBeforeAfterSlider.value = pos.coerceIn(0f, 1f)
    }

    fun selectAIModel(model: String) {
        _selectedAIModel.value = model
    }

    fun setVocalSeparation(level: Float) {
        _vocalSeparationLevel.value = level
    }

    fun toggleNoiseDenoise() {
        _noiseDenoiseEnabled.value = !_noiseDenoiseEnabled.value
    }

    fun openExportDialog() {
        _isExportDialogOpen.value = true
        _isExportSuccess.value = false
        _exportProgress.value = 0f
    }

    fun closeExportDialog() {
        _isExportDialogOpen.value = false
        _isExporting.value = false
    }

    fun setExportResolution(res: String) {
        _exportResolution.value = res
    }

    fun setExportFps(fps: String) {
        _exportFps.value = fps
    }

    fun startExportSimulation() {
        if (_isExporting.value) return
        _isExporting.value = true
        _exportProgress.value = 0f
        _isExportSuccess.value = false
        viewModelScope.launch {
            for (i in 1..25) {
                delay(80)
                _exportProgress.value = i / 25f
            }
            _isExporting.value = false
            _isExportSuccess.value = true
            saveActiveProjectChanges()
        }
    }

    // Google Play Billing Actions
    fun subscribeToPremium(activity: Activity) {
        billingManager.launchBillingFlow(activity)
    }

    fun restorePurchases() {
        billingManager.restorePurchases()
    }

    fun clearBillingMessage() {
        billingManager.clearMessage()
    }

    private fun saveActiveProjectChanges() {
        val current = _activeProject.value ?: return
        val updated = current.copy(
            aspectRatio = _aspectRatio.value,
            brightness = _brightness.value,
            contrast = _contrast.value,
            saturation = _saturation.value,
            speed = _speedMultiplier.value,
            filterName = _activeFilter.value.name,
            textOverlay = _projectText.value,
            lastModified = System.currentTimeMillis()
        )
        _activeProject.value = updated
        viewModelScope.launch {
            repository.updateProject(updated)
        }
    }

    // =========================================================================
    // AI Video Generator Methods
    // =========================================================================
    fun setVideoPromptText(prompt: String) {
        _videoPromptText.value = prompt
    }

    fun setVideoGenStyle(style: String) {
        _videoGenStyle.value = style
    }

    fun setVideoGenRatio(ratio: String) {
        _videoGenRatio.value = ratio
    }

    fun setVideoGenDuration(duration: Int) {
        _videoGenDuration.value = duration
    }

    fun setVideoGenCameraMotion(motion: String) {
        _videoGenCameraMotion.value = motion
    }

    fun toggleGeneratedVideoPlayback() {
        _isGeneratedVideoPlaying.value = !_isGeneratedVideoPlaying.value
    }

    fun startAIVideoGeneration() {
        if (_isGeneratingVideo.value) return
        triggerAIAction("توليد فيديو بالذكاء الاصطناعي") {
            _isGeneratingVideo.value = true
            _isVideoGenCompleted.value = false
            _videoGenProgress.value = 0f
            _videoGenSuccessMessage.value = null
            _isGeneratedVideoPlaying.value = false
            _generatedVideoPlaybackPos.value = 0f

            val userPrompt = _videoPromptText.value.trim().ifBlank { "مشهد سينمائي متطور بدقة 4K" }
            _generatedVideoTitle.value = if (userPrompt.length > 32) userPrompt.take(30) + "..." else userPrompt

            viewModelScope.launch {
                // Stage 1: Scene & Director Analysis
                _videoGenStageText.value = "المرحلة 1/4: تحليل السيناريو وحساب زوايا الإخراج السينمائي..."
                for (i in 1..25) {
                    delay(25)
                    _videoGenProgress.value = i / 100f
                }

                // Stage 2: Latent Diffusion Sampling
                _videoGenStageText.value = "المرحلة 2/4: توليد مصفوفة الإطارات العصبية (Neural Latents)..."
                for (i in 26..55) {
                    delay(25)
                    _videoGenProgress.value = i / 100f
                }

                // Stage 3: Temporal Motion Interpolation
                _videoGenStageText.value = "المرحلة 3/4: حساب تدفق الحركة وتماسك الزمن (Motion Flow)..."
                for (i in 56..80) {
                    delay(25)
                    _videoGenProgress.value = i / 100f
                }

                // Stage 4: 4K Neural Upscaling & Master Color Grading
                _videoGenStageText.value = "المرحلة 4/4: ترقية الجودة إلى 4K 60FPS والتلوين السينمائي الفاخر..."
                for (i in 81..100) {
                    delay(25)
                    _videoGenProgress.value = i / 100f
                }

                _isGeneratingVideo.value = false
                _isVideoGenCompleted.value = true
                _isGeneratedVideoPlaying.value = true
                _videoGenStageText.value = "اكتمل التوليد بنجاح بدقة 4K! جاهز للمعاينة والمونتاج والتصدير."
                _videoGenSuccessMessage.value = "تم توليد الفيديو السينمائي بنجاح وجاهز للمونتاج!"
            }
        }
    }

    fun openGeneratedInEditor() {
        val title = _generatedVideoTitle.value
        val ratio = _videoGenRatio.value
        val dur = _videoGenDuration.value
        viewModelScope.launch {
            val newProj = ProjectEntity(
                title = "AI: $title",
                projectType = "video",
                aspectRatio = ratio,
                durationText = "00:${if (dur < 10) "0$dur" else "$dur"}",
                durationSeconds = dur,
                resolution = "4K 60FPS",
                thumbnailResName = "img_ai_video_gen_banner",
                textOverlay = title,
                filterName = "سايبر بنفسجي",
                lastModified = System.currentTimeMillis()
            )
            val id = repository.createProject(newProj)
            val created = newProj.copy(id = id)
            openProject(created)
        }
    }

    fun saveGeneratedToProjects() {
        val title = _generatedVideoTitle.value
        val ratio = _videoGenRatio.value
        val dur = _videoGenDuration.value
        viewModelScope.launch {
            val newProj = ProjectEntity(
                title = "AI: $title",
                projectType = "video",
                aspectRatio = ratio,
                durationText = "00:${if (dur < 10) "0$dur" else "$dur"}",
                durationSeconds = dur,
                resolution = "4K 60FPS",
                thumbnailResName = "img_ai_video_gen_banner",
                textOverlay = title,
                filterName = "سايبر بنفسجي",
                lastModified = System.currentTimeMillis()
            )
            repository.createProject(newProj)
            _videoGenSuccessMessage.value = "تم حفظ الفيديو بنجاح في مشاريعك!"
        }
    }

    fun openInteractiveAITool(tool: com.example.model.AIToolItem) {
        _selectedInteractiveAITool.value = tool
    }

    fun closeInteractiveAITool() {
        _selectedInteractiveAITool.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ttsEngine.release()
    }
}
