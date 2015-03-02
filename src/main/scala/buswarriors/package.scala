
package object buswarriors {

  type SKU = String

  type TransactionId = String

  case class Quantity(val q: Int) { require(q >= 0, s"${q} is less than zero") }

  implicit def quantity(q: Int) = Quantity(q)
  implicit def quantity(q: BigInt) = new Quantity(q.toInt)

  case class Price(val p: BigDecimal) {
    override def toString(): String = p.toString()
    require(p >= 0.0, s"${p} is less than zero")
  }

  implicit def price(d: BigDecimal) = Price(d)
  implicit def price(d: Double) = Price(BigDecimal(d))

  case class Product(sku: SKU, name: String, price: Price) {
    require(sku != null, "sku is null")
    require(name != null, "name is null")
  }

  case class ProductInventory(product: Product, quantity: Quantity)

  case class LineItem(sku: SKU, quantity: Quantity, name: String, price: Price, subtotal: Price)

  case class Transaction(transId: TransactionId, total: Price, lineItems: Seq[LineItem]) {
    require(
      total.p == lineItems.map(_.price.p).sum,
      s"transaction total, ${total}, not equal to line item total, ${lineItems.map(_.price.p).sum}")
  }
}
