package eu.example.jettipapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier = Modifier.size(40.dp)

// Function for creating a customizable round button that can be clicked with @Card
// takes an onClick event lambda
// And other parameters that are used in @Card
@Composable
fun RoundIconButton(
	modifier: Modifier = Modifier,
	imageVectorIcon: ImageVector,
	onClick: () -> Unit,
	tint: Color = Color.Black.copy(alpha = 0.8f),
	backGroundColor: Color = MaterialTheme.colors.background,
	elevation: Dp = 4.dp
) {
	Card(modifier = modifier
		.padding(all = 4.dp)
		// calls the onClick lambda we provided as argument for RoundIconButton
		.clickable { onClick.invoke() }
		.then(IconButtonSizeModifier),
		shape = CircleShape,
		backgroundColor = backGroundColor,
		elevation = elevation
		 ) {
		Icon(imageVector = imageVectorIcon, contentDescription = "Plus or minus icon",
		tint = tint)

	}

	
}