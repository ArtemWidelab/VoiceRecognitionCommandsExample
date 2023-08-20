package ua.widelab.main_commands.compose

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainCommandsScreen() {
    val audioPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.RECORD_AUDIO)
    if (audioPermissionState.status.isGranted) {
        CommandsScreen()
    } else {
        PermissionsScreen(
            requestPermission = audioPermissionState::launchPermissionRequest
        )
    }
}