package ua.widelab.main_commands.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun PermissionsScreen(
    requestPermission: () -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.permissions_required_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(id = R.string.permissions_required_text),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = requestPermission) {
            Text(text = stringResource(id = R.string.permissions_required_action))
        }
    }
}
