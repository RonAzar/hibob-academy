package com.hibob.Types.Tamir
import java.time.LocalDate

//1. create data class of Cart that include: client Id and list of Products
data class Cart(val clientId: String, val products: List<Product>)
//2. product object contain id, name, price and custom - custom can be either int, string or any other type
data class Product(val id: String, val name: String,val price: Double, val custom: Any)
//3. create sealed class of payment that can be use by the following classes:
//    CreditCard - contains number, expiryDate, type and limit (type can be VISA, MASTERCARD, DISCOVER and AMERICAN_EXPRESS)
//    PayPal - contain email
//    Cash - without args
sealed class Payment{
    data class CreditCard(val number: String, val expiryDates: LocalDate,val types: CreditCardType,val limit:Double) : Payment()
    {
        fun checkCreditCardLenValidation(): Boolean {
            return (this.number.length == 10)
        }
        fun checkExpiryDateValidation(): Boolean {
            return (this.expiryDates.isAfter(LocalDate.now()))
        }
        fun checkIfLimitAboveCheckout(checkOutSum:Double): Boolean {
            return checkOutSum < this.limit
        }
        fun checkCardTypeValidate(type: CreditCardType):Boolean{
            return when(type){
                CreditCardType.VISA -> true
                CreditCardType.MASTERCARD-> true
                CreditCardType.DISCOVER-> false
                CreditCardType.AMERICAN_EXPRESS-> false
            }
        }
    }
    data class PayPal(val email: String) : Payment()
    {
        fun checkPayPalValidation(): Boolean {
            return (this.toString().contains('@'))
        }
    }
    data object Cash : Payment()
}
enum class CreditCardType {
    VISA, MASTERCARD, DISCOVER, AMERICAN_EXPRESS
}
//4. add fail function that get message an argument and throw IllegalStateException
fun fail(message: String):Nothing{
    throw IllegalStateException(message)
}
//5. write function check if custom is true and only if its true its valid product.
fun checkIfCustomIsValid(custom: Any):Boolean {
    return (custom as? Boolean) ?: false
}
//6. write function called checkout and get cart and payment that pay the money
//   * only custom with true are valid
//   * cash payment is not valid to pay so if the payment is cash use fail function
//   * in case of credit card need to validate the expiryDate is after the current date + limit is bigger than the total we need to pay and we allow to use only  VISA or MASTERCARD
//   * in case of payPal validate we hae @
///  * the return value of this function, should be a data class with employee id, status (success or FAILURE) and total called Check
fun checkout(cart: Cart,payment: Payment):Check{
    val total = cart.products
        .filter { product -> checkIfCustomIsValid(product.custom) }
        .sumOf { product -> product.price }
    if (checkPaymentMethod(payment,total)) {
        return Check(cart.clientId, Statuses.SUCCESS, total)
    }
    return Check(cart.clientId, Statuses.FAILURE, 0.0)
}

fun checkPaymentMethod(payment: Payment,price: Double):Boolean{
    return when (payment) {
        is Payment.PayPal -> {
            payment.checkPayPalValidation()
        }
        is Payment.CreditCard -> {
            (payment.checkCreditCardLenValidation() &&
                    payment.checkIfLimitAboveCheckout(price)&&
                    payment.checkCardTypeValidate(payment.types)&&
                    payment.checkExpiryDateValidation())
        }
        is Payment.Cash -> {
            fail("Error... You can't use CASH...")
        }
    }
}

data class Check(val employeeId: String, val statuses:Statuses,val total:Double)
enum class Statuses {
    SUCCESS,FAILURE
}