package eu.example.jettipapp

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
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
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
import kotlin.math.absoluteValue

// my Import for state
import eu.example.jettipapp.util.calculateTotalPerPerson
import eu.example.jettipapp.util.calculateTotalTip

// Add dependencys to gradle Icons
// Icons
// implementation "androidx.compose.material:material-icons-extended:$compose_version"

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
fun TopHeader(totalPerPerson: Double){
    androidx.compose.material.Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
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
                text = "I alt pr. Person",
                style = MaterialTheme.typography.h5)
            Text(
                text = "$total Kr.",
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
    // State Hoisting the states to the top @Composable function.

    // Used for remember State of splitByState add or remove RoundIconButton
    val splitByState = remember {
        mutableStateOf(1)
    }

    // Not sure where this came from, seems like it was edited in, but not shown in video ?
    val range = IntRange(start = 1, endInclusive = 100)

    var tipAmountState = remember {
        mutableStateOf(0.0) // double
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    Column {

        // Get the value from onValChange(totalBillState.value) -
        // when we enter an amount in the @InputField
        // We can then manipulate the input with code logic
        BillForm(
            splitByState = splitByState,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState){

            // Using logic to convert billAmount type and multiply by 10
                billAmount ->
            Log.d("Amount", "MainContent: ${billAmount.toInt() * 10}")
        }
    }
}

// host Inputfield, so we can pass its value to other composables
@ExperimentalComposeUiApi
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
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

    // remember, and recompose position of slider
    var sliderPositionState by remember {
        mutableStateOf(0f) // passing a float for the slider
    }
    // convert float to
    val tipPercentage = (sliderPositionState * 100).toInt()

    // Calling TopHeader
    TopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier
            .padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            InputField(modifier.fillMaxWidth(),
                valueState = totalBillState,
                labelId = "Regnings beløb i alt",
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
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // show the rest of UI if ValidState is true
                    // Text(text = "ValidState true")
                    Text(modifier = Modifier
                        .align(alignment = Alignment.CenterVertically),
                        text = "Antal personer:",)
                    Spacer(modifier = Modifier
                        .width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {

                        // Remove Button
                        RoundIconButton(
                            imageVectorIcon = Icons.Default.Remove, // Icon with minus sign
                            onClick = { 
                                // Subtract 1 from splitByState we we press remove button -
                                // only if splitByState is greater than 1 -
                                // So we are making sure the split can never be less than 1 people
                                splitByState.value =
                                    if (splitByState.value > 1) {splitByState.value - 1} else 1

                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitByState.value,
                                        tipPercentage = tipPercentage)

                                // Just for testing
                                Log.d("Press", "MainContent: pressMinus button was clicked")})

                        // Counter between the roundButtons
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp),
                            text = splitByState.value.toString())

                        // Add Button
                        RoundIconButton(
                            imageVectorIcon = Icons.Default.Add, // Icon with plus sign
                            onClick = { // add 1 to splitByState when we press add button
                            splitByState.value += 1

                            totalPerPersonState.value =
                                calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage)
                            }
                        )
                    }
                }
            // Tip Row
            Row(modifier = Modifier
                .padding(horizontal = 3.dp, vertical = 12.dp)
            ) {
                Text(modifier = Modifier
                    .align(Alignment.CenterVertically),
                    text = "Drikkepenge: ")
                Spacer(modifier = Modifier
                    .width(180.dp))
                Text(modifier = Modifier
                    .align(Alignment.CenterVertically),
                    text = "${tipAmountState.value} Kr.") //

            }
            Column(modifier = Modifier
                .padding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tip Text value from slider
                Text(text = "${tipPercentage} %")
                Spacer(modifier = Modifier.height(14.dp))

                // Slider
                Slider(modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                    // steps = 4, //
                    value = sliderPositionState, // We don't need to specify . value, when using by remember


                    // change the value ( newVal) when we interact with the slider
                    onValueChange = {newVal ->
                        sliderPositionState = newVal
                        tipAmountState.value =
                                calculateTotalTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentage)

                        totalPerPersonState.value = calculateTotalPerPerson(
                            totalBill = totalBillState.value.toDouble(),
                            splitBy = splitByState.value,
                            tipPercentage = tipPercentage)

                        // testing
                        Log.d("Tax", "Tax %: ${newVal} ")
                    },
                    onValueChangeFinished = {
                        // this parameter can execute a lambda when the user stops holding the slider
                        Log.d("SliderPos", "I stopped the slider at point ${sliderPositionState} ")
                    })

                
            }


            } // put an empty box if ValidState is false
            else {
                Box(){
                }
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