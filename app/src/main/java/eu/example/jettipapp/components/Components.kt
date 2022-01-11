package eu.example.jettipapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Add dependency to gradle Icons
// Icons
// implementation "androidx.compose.material:material-icons-extended:$compose_version"

// Creating a customizable TextField ( EditText in XML)
// It has a modifier parameter which is set to default modifier -
// so we can modify the InputField to our liking when we use it
// It also has a parameter valueState of type MutableState: String
// And a few other self explaining parameters
@Composable
fun InputField(
        modifier: Modifier = Modifier,
        valueState: MutableState<String>,
        labelId: String,
        enabled: Boolean,
        isSingleLine: Boolean,
        keyboardType: KeyboardType = KeyboardType.Number,
        imeAction: ImeAction = ImeAction.Next, // Shows user what to do next on the keyboard ??
        onAction: KeyboardActions = KeyboardActions.Default
            ) {
            OutlinedTextField(modifier = modifier
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                value = valueState.value,
                onValueChange = { valueState.value = it },
                label = { Text(text = labelId) },
                leadingIcon = { Icon(imageVector = Icons.Rounded.CreditCard, contentDescription = "Money Icon")},
                singleLine = isSingleLine,
                textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType,
                imeAction = imeAction),
                keyboardActions = onAction
                )

    // Icons.Rounded.AttachMoney

}