package com.example.android_template_project.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android_template_project.R
import com.example.android_template_project.presentation.viewModels.LocationTrackingViewModel

@Composable
fun LocationTrackingScreen(
    modifier: Modifier,
    viewModel: LocationTrackingViewModel = hiltViewModel()
) {
    val pendingLogs by viewModel.pendingLogs.collectAsStateWithLifecycle()
    val isOnline = rememberNetworkStatus()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (isOnline) Color.Green else Color.Red)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isOnline) stringResource(R.string.online) else stringResource(R.string.offline),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = stringResource(R.string.pending_logs, pendingLogs),
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = { viewModel.startTracking() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.start_tracking))
        }

        Button(
            onClick = { viewModel.stopTracking() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
        ) {
            Text(text = stringResource(R.string.stop_tracking))
        }
    }
}
