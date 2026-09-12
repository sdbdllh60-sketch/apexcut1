package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ProjectEntity
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexBorderGlow
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexCyanLight
import com.example.ui.theme.ApexDarkCard
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectsSection(
    projects: List<ProjectEntity>,
    onOpenProject: (ProjectEntity) -> Unit,
    onDuplicateProject: (ProjectEntity) -> Unit,
    onDeleteProject: (Long) -> Unit,
    onCreateNew: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("الكل") }

    val filteredProjects = when (selectedFilter) {
        "فيديو" -> projects.filter { it.projectType == "video" }
        "صور" -> projects.filter { it.projectType == "photo" }
        "AI" -> projects.filter { it.projectType == "ai_enhance" }
        else -> projects
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "مشاريعي",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = ApexTextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(ApexBlue.copy(alpha = 0.2f), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${projects.size}",
                        color = ApexCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick Add icon
            IconButton(
                onClick = onCreateNew,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF1E1E22), CircleShape)
                    .border(1.dp, ApexBorder, CircleShape)
                    .testTag("projects_section_quick_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Project",
                    tint = ApexPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("الكل", "فيديو", "صور", "AI").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ApexPurple else Color(0xFF1E1E22))
                        .border(
                            1.dp,
                            if (isSelected) ApexPurple else ApexBorder,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else ApexTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredProjects.isEmpty()) {
            // Empty State Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(ApexDarkCard)
                    .border(1.dp, ApexBorder, RoundedCornerShape(20.dp))
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color(0xFF1E1E22), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = ApexPurple,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لا توجد مشاريع في هذا القسم",
                        style = MaterialTheme.typography.titleMedium,
                        color = ApexTextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ابدأ بإنشاء مشروعك الأول أو استخدم القوالب الجاهزة",
                        style = MaterialTheme.typography.bodySmall,
                        color = ApexTextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                filteredProjects.forEach { project ->
                    ProjectRowCard(
                        project = project,
                        onOpen = { onOpenProject(project) },
                        onDuplicate = { onDuplicateProject(project) },
                        onDelete = { onDeleteProject(project.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectRowCard(
    project: ProjectEntity,
    onOpen: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val imageRes = when (project.thumbnailResName) {
        "img_ai_enhance_banner" -> R.drawable.img_ai_enhance_banner
        "ic_apexcut_logo" -> R.drawable.ic_apexcut_logo
        else -> R.drawable.img_template_cyber
    }

    val typeIcon: ImageVector = when (project.projectType) {
        "photo" -> Icons.Default.Photo
        "ai_enhance" -> Icons.Default.HighQuality
        else -> Icons.Default.Movie
    }

    val timeFormatted = remember(project.lastModified) {
        val sdf = SimpleDateFormat("dd/MM/yyyy • hh:mm a", Locale("ar"))
        sdf.format(Date(project.lastModified))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black)
            .clip(RoundedCornerShape(18.dp))
            .background(ApexDarkCard)
            .border(1.dp, ApexBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onOpen)
            .padding(12.dp)
            .testTag("project_item_${project.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Project Thumbnail with duration/aspect badge
            Box(
                modifier = Modifier
                    .size(width = 86.dp, height = 68.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, ApexBorderGlow, RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = project.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                // Aspect Ratio and type overlay badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color(0xCC080718), RoundedCornerShape(topEnd = 6.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = project.aspectRatio,
                        color = ApexCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (project.projectType != "photo") {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(Color(0xCC000000), RoundedCornerShape(topStart = 6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = project.durationText,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Project Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = typeIcon,
                        contentDescription = null,
                        tint = ApexPurpleLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = ApexTextPrimary,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF221E46), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = project.resolution,
                            color = ApexCyanLight,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = project.filterName,
                        color = ApexTextMuted,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = timeFormatted,
                    color = ApexTextMuted,
                    fontSize = 9.sp
                )
            }

            // Options menu
            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = ApexTextSecondary
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(Color(0xFF1C1938))
                ) {
                    DropdownMenuItem(
                        text = { Text("فتح في المحرر", color = ApexTextPrimary, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = ApexPurpleLight) },
                        onClick = {
                            menuExpanded = false
                            onOpen()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("تكرار المشروع", color = ApexTextPrimary, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = ApexCyan) },
                        onClick = {
                            menuExpanded = false
                            onDuplicate()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("حذف المشروع", color = Color(0xFFEF4444), fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444)) },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
