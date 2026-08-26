package com.example.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoriesScreen(
    repoUrlInput: String,
    isLoading: Boolean,
    errorMessage: String?,
    onRepoUrlChange: (String) -> Unit,
    onFetchRepo: () -> Unit,
    onBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var keiyoushiEnabled by remember { mutableStateOf(true) }
    var officialEnabled by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("repo_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Repositories",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = { showAddDialog = true }, modifier = Modifier.testTag("add_repo_btn")) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Repository",
                        tint = LuminaVioletSecondary
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = LuminaVioletPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_repo")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Repository")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CARD 1: KEIYOUSHI
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Keiyoushi",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = LuminaEmerald.copy(alpha = 0.15f),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, LuminaEmerald)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(LuminaEmerald)
                                        )
                                        Text(
                                            text = "ENABLED",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = LuminaEmerald,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = TextSecondary)
                        }

                        Text(
                            text = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.pb",
                            style = MaterialTheme.typography.bodySmall,
                            color = LuminaCyanAccent,
                            fontSize = 12.sp
                        )

                        Text(
                            text = "420 Extensions • Updated 2 hours ago",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )

                        HorizontalDivider(color = LuminaBorder, thickness = 0.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .clickable { onFetchRepo() }
                                    .padding(vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = "Sync",
                                    tint = LuminaVioletSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isLoading) "Syncing..." else "Sync Now",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = LuminaVioletSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Switch(
                                checked = keiyoushiEnabled,
                                onCheckedChange = { keiyoushiEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = LuminaVioletPrimary
                                )
                            )
                        }
                    }
                }
            }

            // CARD 2: LUMINA OFFICIAL [SYSTEM]
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Lumina Official",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = LuminaVioletPrimary.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, LuminaVioletSecondary)
                                ) {
                                    Text(
                                        text = "SYSTEM",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LuminaVioletSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = TextSecondary)
                        }

                        Text(
                            text = "Bundled Extensions",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Text(
                            text = "12 Extensions • Pre-verified",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )

                        HorizontalDivider(color = LuminaBorder, thickness = 0.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Auto-managed",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )

                            Switch(
                                checked = officialEnabled,
                                onCheckedChange = { officialEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = LuminaVioletPrimary
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = LuminaSurfaceVariant,
            titleContentColor = Color.White,
            textContentColor = TextSecondary,
            title = { Text("Add Repository") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Enter Keiyoushi Protocol Buffer index URL:")
                    OutlinedTextField(
                        value = repoUrlInput,
                        onValueChange = onRepoUrlChange,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuminaVioletSecondary,
                            unfocusedBorderColor = LuminaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAddDialog = false
                        onFetchRepo()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LuminaVioletPrimary)
                ) {
                    Text("Add & Sync")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
