package eu.example.jettipapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.example.jettipapp.components.InputField
import eu.example.jettipapp.ui.theme.JetTipAppTheme
import eu.example.jettipapp.widgets.RoundIconButton

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()

            }

        }
    }
}

//Top level Container which take a Composable function as parameter
@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipAppTheme() {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()


        }

    }

}

// Preview
@Composable
fun TopHeader(totalPerPerson: Double = 135.0){
    androidx.compose.material.Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        // different ways of making a shape
        // .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
        // here we use CircleShape and overwrite its default properties with .copy
        .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7) //
    ) {
        Column(modifier = Modifier
            .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson) // format total Person with 2 decimals
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h5)
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )


        }

    }

}

@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent(){
    // Get the value from onValChange(totalBillState.value) -
    // when we enter an amount in the @InputField
    // We can then manipulate the input with code logic
    BillForm(){

        // Using logic to convert billAmount type and multiply by 10
        billAmount ->
        Log.d("Amount", "MainContent: ${billAmount.toInt() * 10}")
    }





}

// host Inputfield, so we can pass its value to other composables
@ExperimentalComposeUiApi
@Composable
fun BillForm(modifier: Modifier = Modifier,
             onValChange: (String) -> Unit = {}
) {
    // Used for holding the value of InputField
    // It will set the initial value to an empty string -
    // And then update the value to whatever the user input in the InputField
    val totalBillState = remember {
        mutableStateOf("")
    }
    // Used the check if there is something inside the totalBillState
    // Returns true if field is not empty ( The user has written something inside it)
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    //
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier
            .padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    //
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide() // hide the keyboard when we press done
                })
            if (validState){
                Row(modifier = Modifier
                    .padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // show the rest of UI if ValidState is true
                    // Text(text = "ValidState true")
                    Text(modifier = Modifier
                        .align(alignment = Alignment.CenterVertically),
                        text = "Split",)
                    Spacer(modifier = Modifier
                        .width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {

                        // Call RoundIconButton
                        RoundIconButton(
                            imageVectorIcon = Icons.Default.Remove, // Icon with minus sign
                            onClick = { /*TODO*/

                                // Just for testing
                                Log.d("Press", "MainContent: pressMinus button was clicked")})
                        RoundIconButton(
                            imageVectorIcon = Icons.Default.Add, // Icon with plus sign
                            onClick = { /*TODO*/ })

                    }

                }

            } else {
                Box(){} // put an empty box if ValidState is false
            }

        }

    }

}





// @Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {

    }
}