package io.orb.subject.db

import java.sql.{PreparedStatement, SQLException, Timestamp}
import java.util.{Date, Properties}

object TiDBDAL extends TiDBServer {

  def saveTransaction(id: String, lastName: String, firstName: String, creditCardNumber: String, itemPurchased: String, quantity: Double, price: Double, purchaseDate: Date, zipCode: String): Unit = {
            val insertSql = """insert into transactions(id, lastName, firstName, creditCardNumber, itemPurchased, quantity, price, purchaseDate, zipCode) values (?,?,?,?,?,?,?,?,?)""".stripMargin
            val preparedStmt: PreparedStatement = connection.prepareStatement(insertSql)
            preparedStmt.setString(1, id)
            preparedStmt.setString(2, lastName)
            preparedStmt.setString(3, firstName)
            preparedStmt.setString(4, creditCardNumber)
            preparedStmt.setString(5, itemPurchased)
            preparedStmt.setDouble(6, quantity)
            preparedStmt.setDouble(7, price)
            preparedStmt.setTimestamp(8, new Timestamp(purchaseDate.getTime))
            preparedStmt.setString(9, zipCode)
            preparedStmt.execute
  }
}