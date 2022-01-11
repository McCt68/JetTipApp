package eu.example.jettipapp.util

// Having the functions in another package
// remember to import it in the file where its used

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {

	// calculate totalBill only if its > 1 and its not empty
	// else return a double of 0.0
	return if (totalBill > 1 && totalBill.toString().isNotEmpty())
		(totalBill * tipPercentage) / 100
	else 0.0
}

fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercentage: Int): Double{

	// Calling calculateTotalTip
	val bill = calculateTotalTip(totalBill, tipPercentage) + totalBill
	return (bill / splitBy)
}