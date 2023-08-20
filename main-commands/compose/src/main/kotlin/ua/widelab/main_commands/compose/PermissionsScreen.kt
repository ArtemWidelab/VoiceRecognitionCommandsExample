package ua.widelab.main_commands.compose

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import ua.widelab.compose_components.dimensions

@Composable
internal fun PermissionsScreen(
    requestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(all = MaterialTheme.dimensions.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text(
            text = stringResource(id = R.string.permissions_required_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.bigPadding))
        Text(
            text = stringResource(id = R.string.permissions_required_text),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.bigPadding))
        Button(onClick = requestPermission) {
            Text(text = stringResource(id = R.string.permissions_required_action))
        }
        Button(onClick = {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(context, intent, null)
        }) {
            Text(text = stringResource(id = R.string.permissions_required_settings))
        }
    }
}