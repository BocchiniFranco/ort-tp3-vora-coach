package ar.edu.ort.voracoach.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.ProfileSettings
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraTextField
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintBorderTranslucent
import ar.edu.ort.voracoach.ui.theme.VoraMintTranslucent
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

/** 7.2 Edit Profile (Figma "Profile / 02 — Edit Profile"). */
@Composable
fun EditProfileScreen(
    draft: ProfileSettings,
    onDraftChange: ((ProfileSettings) -> ProfileSettings) -> Unit,
    onChangePhotoClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    ProfileSubScreen(title = "Edit profile", onBackClick = onBackClick) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.img_avatar_alex),
                contentDescription = "Profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(CircleShape)
            )
            Text(
                text = "Change photo",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = VoraGreen,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onChangePhotoClick).padding(4.dp)
            )
        }
        VoraTextField(
            value = draft.firstName,
            onValueChange = { value -> onDraftChange { it.copy(firstName = value) } },
            label = "First name"
        )
        VoraTextField(
            value = draft.lastName,
            onValueChange = { value -> onDraftChange { it.copy(lastName = value) } },
            label = "Last name"
        )
        VoraTextField(
            value = draft.dateOfBirth,
            onValueChange = { value -> onDraftChange { it.copy(dateOfBirth = value) } },
            label = "Date of birth"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            VoraTextField(
                value = draft.height,
                onValueChange = { value -> onDraftChange { it.copy(height = value) } },
                label = "Height",
                modifier = Modifier.weight(1f)
            )
            VoraTextField(
                value = draft.weight,
                onValueChange = { value -> onDraftChange { it.copy(weight = value) } },
                label = "Weight",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.weight(1f)
            )
        }
        VoraTextField(
            value = draft.gender,
            onValueChange = { value -> onDraftChange { it.copy(gender = value) } },
            label = "Gender"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(VoraMintTranslucent)
                .border(1.dp, VoraMintBorderTranslucent, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Outlined.Info, contentDescription = null, tint = VoraGreen, modifier = Modifier.size(18.dp))
            Text(
                text = "Used to personalise your recommendations.",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = VoraGreen
            )
        }
        VoraPrimaryButton(text = "Save changes", onClick = onSaveClick, modifier = Modifier.padding(top = 12.dp))
        Text(
            text = "Your information is used to personalise your VORA experience.",
            style = MaterialTheme.typography.bodySmall,
            color = VoraTextTertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
