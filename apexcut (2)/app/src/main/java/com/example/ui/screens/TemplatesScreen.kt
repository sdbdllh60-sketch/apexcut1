package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.TemplateData
import com.example.model.TemplateItem
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.viewmodel.ApexCutViewModel
import com.example.ui.viewmodel.CurrentScreen

@Composable
fun TemplatesScreen(
    viewModel: ApexCutViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("الكل") }
    val categories = listOf("الكل", "ريلز وتيك توك", "سينمائي", "إيقاع نيون", "سفر وفلوج")

    val filteredList = remember(selectedCategory) {
        if (selectedCategory == "الكل") TemplateData.sampleTemplates
        else TemplateData.sampleTemplates.filter { it.category == selectedCategory }
    }

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
                        .testTag("templates_back")
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
                        text = "معرض القوالب الاحترافية",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ApexTextPrimary
                    )
                    Text(
                        text = "قوالب متزامنة مع الموسيقى بنقرة واحدة",
                        color = ApexCyan,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Categories
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                val isSel = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSel) ApexPurple else Color(0xFF171530))
                        .border(1.dp, if (isSel) ApexPurpleLight else ApexBorder, RoundedCornerShape(20.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (isSel) Color.White else ApexTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Templates Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList) { template ->
                TemplateGridItem(
                    template = template,
                    onUse = { viewModel.useTemplate(template) }
                )
            }
        }
    }
}

@Composable
private fun TemplateGridItem(
    template: TemplateItem,
    onUse: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageRes = when (template.thumbnailResName) {
        "img_ai_enhance_banner" -> R.drawable.img_ai_enhance_banner
        "ic_apexcut_logo" -> R.drawable.ic_apexcut_logo
        else -> R.drawable.img_template_cyber
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = ApexPurple)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF141228))
            .border(1.dp, ApexBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onUse)
            .testTag("template_grid_${template.id}")
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = template.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x33000000), Color(0x990A091A), Color(0xFA090818))
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color(0xCC1A1636), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(template.tag, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = template.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = template.musicTitle,
                color = ApexCyan,
                fontSize = 9.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Brush.horizontalGradient(listOf(ApexPurple, ApexBlue)))
                    .clickable(onClick = onUse),
                contentAlignment = Alignment.Center
            ) {
                Text("استخدم الآن", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
