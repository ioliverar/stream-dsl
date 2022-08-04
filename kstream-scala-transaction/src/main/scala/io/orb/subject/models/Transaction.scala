package io.orb.subject.models

import java.util.Date
case class Transaction (lastName: String, firstName: String, var creditCardNumber: String, itemPurchased: String, quantity: Double, price: Double, purchaseDate: Date, zipCode: String){
  private val CC_NUMBER_REPLACEMENT = "xxxx-xxxx-xxxx-"
  def maskCreditCard: Transaction = {
    val parts: Array[String] = this.creditCardNumber.split("-")
    if(parts.length < 4) this.creditCardNumber = "xxxx"
    else {
      val last4Digits: String = this.creditCardNumber.split("-")(3)
      creditCardNumber = CC_NUMBER_REPLACEMENT + last4Digits
    }
    this
  }
  override def toString = {
    s"Transaction( $lastName, $firstName, $creditCardNumber, $itemPurchased, $quantity, $price, $purchaseDate, $zipCode)"
  }
}