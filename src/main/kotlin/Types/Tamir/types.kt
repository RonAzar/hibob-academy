package com.hibob.Types.Tamir

import jakarta.validation.constraints.Email
import org.apache.logging.log4j.message.Message
import org.jooq.impl.QOM
import java.sql.ClientInfoStatus
import java.sql.Date
import java.sql.Types
import java.time.LocalDate
import kotlin.reflect.jvm.internal.impl.name.ClassId

//1. create data class of Cart that include: client Id and list of Products
data class Cart(val clientId: Int, val products: List<Product>)
//2. product object contain id, name, price and custom - custom can be either int, string or any other type
data class Product(val id: Int, val name: String,val price: Double, val custom: Any)
//3. create sealed class of payment that can be use by the following classes:
//    CreditCard - contains number, expiryDate, type and limit (type can be VISA, MASTERCARD, DISCOVER and AMERICAN_EXPRESS)
//    PayPal - contain email
//    Cash - without args
sealed class Payment{
    data class CreditCard(val number: String, val expiryDates: LocalDate,val types: CreditType,val limit:Double) : Payment()
    {
        fun checkCreditCardLenValidation(): Boolean {
            return (this.number.length == 10 && expiryDates.isBefore(LocalDate.now()))
        }
        fun checkIfLimitAboveCheckout(chekOutSum:Double): Boolean {
            return chekOutSum < this.limit
        }
        fun checkCardTypeValidate(type: CreditType):Boolean{
            when(type){
                CreditType.VISA -> true
                CreditType.MASTERCARD-> true
                CreditType.DISCOVER-> false
                CreditType.AMERICAN_EXPRESS-> false
            }
        }
    }
    data class PayPal(val email: String) : Payment()
    {
        fun checkPayPalValidation(): Boolean {
            return (this.)
        }
    }
    data object Cash : Payment()
}
enum class CreditType {
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
///  * the return value of this function, should be a data class with employee id, status (success or failed) and total called Check
fun checkout(cart: Cart,payment: Payment){
    when (payment) {
        is Payment.PayPal -> {

        }
        is Payment.CreditCard -> {
            if (payment.checkCreditCardLenValidation() &&
                payment.checkIfLimitAboveCheckout(chekOutSum = ?)&&
                payment.checkCardTypeValidate(type = ?))
        }
        is Payment.Cash -> {}
    }
    cart.products.forEach {product ->
        if(!checkIfCustomIsValid(product.custom)){
            fail("Some product is invalid")
        }
    }
}
data class Check(val employeeId: Int, val status:Status,val total:Double)
enum class Status {
    SUCCESS,FAILED
}
// 7. implement pay method