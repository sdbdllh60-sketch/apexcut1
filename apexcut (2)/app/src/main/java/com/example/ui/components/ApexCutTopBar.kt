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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.ApexBlue
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexPurple
import com.example.ui.theme.ApexPurpleLight
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun ApexCutTopBar(
    remainingUses: Int = 3,
    isPremium: Boolean = false,
    onOpenPremium: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenHelp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onOpenPremium)
                .testTag("app_branding_row")
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(10.dp, RoundedCornerShape(14.dp), ambientColor = Color(0xFF00B0FF), spotColor = Color(0xFFFF2A6D))
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xFF00B0FF), Color(0xFFFF2A6D))),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_apexcut_logo),
                    contentDescription = "ApexCut Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Apex",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        color = ApexTextPrimary
                    )
                    Text(
                        text = "Cut",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color(0xFFFF2A6D)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (isPremium) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFFF2A6D))),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "VIP 👑",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
                Text(
                    text = if (isPremium) "عضوية Premium مفتوحة بالكامل" else "متبقي $remainingUses من 3 استخدامات AI",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPremium) ApexPurpleLight else ApexTextSecondary,
                    fontSize = 10.5.sp
                )
            }
        }

        // Action Icons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isPremium) {
                // Premium Badge Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFF8B5CF6), Color(0xFFEC4899), Color(0xFFEF4444))
                            )
                        )
                        .clickable(onClick = onOpenPremium)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("topbar_upgrade_pill")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Premium",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Premium",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E22))
                    .border(1.dp, ApexBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onOpenHelp,
                    modifier = Modifier.size(38.dp).testTag("topbar_help_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Assistant",
                        tint = ApexPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E22))
                    .border(1.dp, ApexBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.size(38.dp).testTag("topbar_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = ApexTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
