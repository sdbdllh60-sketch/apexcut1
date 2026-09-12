package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.ProjectEntity
import com.example.model.TemplateItem
import com.example.ui.components.AIQualityEnhanceCard
import com.example.ui.components.AIUsageBanner
import com.example.ui.components.AIVideoGenHeroBanner
import com.example.ui.components.ApexCutTopBar
import com.example.ui.components.NewProjectBanner
import com.example.ui.components.ProjectsSection
import com.example.ui.components.QuickModesGrid
import com.example.ui.components.TemplatesSection
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexDarkNav
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleDark
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.viewmodel.ApexCutViewModel
import com.example.ui.viewmodel.CurrentScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.allProjects.collectAsStateWithLifecycle()
    val remainingUses by viewModel.remainingAIUses.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ApexDarkBackground),
        containerColor = ApexDarkBackground,
        topBar = {
            ApexCutTopBar(
                remainingUses = remainingUses,
                isPremium = isPremium,
                onOpenPremium = { viewModel.navigateTo(CurrentScreen.Premium) },
                onOpenSettings = { viewModel.navigateTo(CurrentScreen.Premium) },
                onOpenHelp = { viewModel.navigateTo(CurrentScreen.AITools) },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            // Sophisticated Dark Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ApexDarkNav)
                    .border(width = 1.dp, color = Color(0xFF1E1E22))
                    .navigationBarsPadding()
                    .padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Home (الرئيسية)
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        label = "الرئيسية",
                        isSelected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            coroutineScope.launch { scrollState.animateScrollTo(0) }
                        },
                        tag = "nav_home"
                    )

                    // 2. Projects (المشاريع)
                    BottomNavItem(
                        icon = Icons.Default.Folder,
                        label = "المشاريع",
                        isSelected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            coroutineScope.launch { scrollState.animateScrollTo(scrollState.maxValue) }
                        },
                        tag = "nav_projects"
                    )

                    // 3. AI Tools & Education (التعليم / أدوات AI)
                    BottomNavItem(
                        icon = Icons.Default.School,
                        label = "أدوات AI",
                        isSelected = selectedTab == 2,
                        onClick = {
                            selectedTab = 2
                            viewModel.navigateTo(CurrentScreen.AITools)
                        },
                        tag = "nav_ai_tools"
                    )

                    // 4. Me / Premium (VIP / Premium)
                    BottomNavItem(
                        icon = Icons.Default.Person,
                        label = "Premium",
                        isSelected = selectedTab == 3,
                        onClick = {
                            selectedTab = 3
                            viewModel.navigateTo(CurrentScreen.Premium)
                        },
                        tag = "nav_profile"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Unified AI Usage Quota Banner
            AIUsageBanner(
                remainingUses = remainingUses,
                isPremium = isPremium,
                onUpgradeClick = { viewModel.navigateTo(CurrentScreen.Premium) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Hero New Project Banner
            NewProjectBanner(
                onCreateProject = { title, type, ratio ->
                    viewModel.createNewProject(title = title, type = type, ratio = ratio)
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // AI Video Generator Hero Card (Text-to-Video 4K)
            AIVideoGenHeroBanner(
                onOpenVideoGen = {
                    viewModel.navigateTo(CurrentScreen.AIVideoGen)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Quick Modes Grid (Video, Photo, AI Audio, AI Tools, AI Video Gen)
            QuickModesGrid(
                onOpenVideoEditor = {
                    viewModel.createNewProject(title = "فيديو سينمائي 4K", type = "video", ratio = "9:16")
                },
                onOpenPhotoEditor = {
                    viewModel.createNewProject(title = "تعديل صورة احترافية", type = "photo", ratio = "1:1")
                },
                onOpenAIAudio = {
                    viewModel.navigateTo(CurrentScreen.AIAudio)
                },
                onOpenAITools = {
                    viewModel.navigateTo(CurrentScreen.AITools)
                },
                onOpenAIVideoGen = {
                    viewModel.navigateTo(CurrentScreen.AIVideoGen)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. AI Quality Enhancement Banner
            AIQualityEnhanceCard(
                onOpenAIEnhance = {
                    viewModel.navigateTo(CurrentScreen.AIEnhance())
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 4. Discover More / اكتشف المزيد Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "اكتشف المزيد",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = ApexTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DiscoverItemCard(
                        title = "أدوات AI",
                        gradientColors = listOf(Color(0xFFEC4899), Color(0xFFF97316)),
                        onClick = { viewModel.navigateTo(CurrentScreen.AITools) },
                        modifier = Modifier.weight(1f)
                    )
                    DiscoverItemCard(
                        title = "القوالب",
                        gradientColors = listOf(Color(0xFF34D399), Color(0xFF3B82F6)),
                        onClick = { viewModel.navigateTo(CurrentScreen.Templates) },
                        modifier = Modifier.weight(1f)
                    )
                    DiscoverItemCard(
                        title = "مشاريعي",
                        gradientColors = listOf(Color(0xFFFBBF24), Color(0xFFEF4444)),
                        onClick = {
                            coroutineScope.launch { scrollState.animateScrollTo(scrollState.maxValue) }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 5. Templates Section
            TemplatesSection(
                onSelectTemplate = { template ->
                    viewModel.useTemplate(template)
                },
                onViewAllTemplates = {
                    viewModel.navigateTo(CurrentScreen.Templates)
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 6. My Projects Section (Room Database)
            ProjectsSection(
                projects = projects,
                onOpenProject = { project ->
                    viewModel.openProject(project)
                },
                onDuplicateProject = { project ->
                    viewModel.duplicateProject(project)
                },
                onDeleteProject = { id ->
                    viewModel.deleteProject(id)
                },
                onCreateNew = {
                    viewModel.createNewProject(title = "مشروع جديد", type = "video", ratio = "9:16")
                }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun DiscoverItemCard(
    title: String,
    gradientColors: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black)
            .clip(RoundedCornerShape(18.dp))
            .background(ApexDarkCard)
            .border(1.dp, ApexBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        brush = Brush.linearGradient(gradientColors),
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = ApexTextPrimary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .testTag(tag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) ApexPurple else ApexTextMuted,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = if (isSelected) ApexPurple else ApexTextMuted,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

